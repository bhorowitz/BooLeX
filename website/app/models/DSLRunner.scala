package models

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import boolex.antlr.BooLeXParser.ModuleContext
import boolex.antlr.{BooLeXParser, BooLeXLexer}
import boolex.logic.elements.circuitbuilder.{BLXCircuit, BLXModelGenerator}
import boolex.logic.elements.core.{BLXSocket, BLXEventManager}
import boolex.logic.elements.signals.{BLXSignalReceiver, BLXSignalQueue}
import boolex.typechecker.BooLeXTypeChecker

import java.util

import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.iteratee._
import play.api.libs.json._
import play.libs.Akka

import scala.collection.JavaConversions._
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.language.postfixOps
import java.lang.management.ManagementFactory

/**
 * Created by ajr64 on 4/4/14.
 */

object DSLRunner {
  implicit val timeout = Timeout(3 seconds)

  def addCircuit(): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    val evaluator: ActorRef = Akka.system.actorOf(Props[DSLRunner])

    (evaluator ? OpenConnection()).map {
      case Ready(enumerator) =>
        val iteratee = Iteratee.foreach[JsValue] {
          event =>
            (event \ "command").as[String] match {
              case "initialize" =>
                val dslResult = (event \ "dsl").validate[String]
                dslResult match {
                  case JsSuccess(dsl, _) =>
                    (evaluator ? Initialize(dsl)).map {
                      case TypeError(error) => evaluator ! Die(error)
                      case _ => /* ignore */
                    }
                  case _ => evaluator ! Die("Malformed initialization -- no dsl field.")
                }
              case "start" =>
                val request = (event \ "initialValues").validate[List[SocketAssignment]]
                request match {
                  case JsSuccess(inits, _) =>
                    evaluator ! BeginEvaluation(inits)
                  case _ => /* ignore */
                }
              case "update" =>
                val request = (event \ "socket").validate[SocketAssignment]
                request match {
                  case JsSuccess(socket, _) =>
                    evaluator ! UpdateSocket(socket)
                  case _ => /* ignore */
                }
              case "stop" => evaluator ! Die("")
              case _ => /* ignore */
            }
        }.map { _ => evaluator ! Die("") }
        (iteratee, enumerator)
    }
  }
}

class DSLRunner extends Actor {
  var outputChannel: Concurrent.Channel[JsValue] = null
  val outSocket = Concurrent.unicast[JsValue] {
    c => outputChannel = c
  }

  val INITIALIZE_TO_FALSE = false
  val typeChecker: BooLeXTypeChecker = new BooLeXTypeChecker()
  val modelGenerator: BLXModelGenerator = new BLXModelGenerator(INITIALIZE_TO_FALSE)

  var circuit : BLXCircuit = null
  var eventManager : BLXEventManager = null

  def update(sockAssign : SocketAssignment) = sockAssign match {
    case SocketAssignment(name, value) =>
      val sockets: List[BLXSocket] = circuit.getInputSockets.toList
      val socketMap : Map[String, BLXSocket] = (sockets map { sock => (sock.getId, sock) }).toMap
      eventManager.update(socketMap.get(name).get, value.asInstanceOf[java.lang.Boolean])
  }

  def receive = {
    case OpenConnection() =>
      sender ! Ready(outSocket)

    case Initialize(dsl) =>
      val bl: BooLeXLexer = new BooLeXLexer(new ANTLRInputStream(dsl))
      val bp: BooLeXParser = new BooLeXParser(new CommonTokenStream(bl))
      val parseTree: ModuleContext = bp.module()

      if (bp.getNumberOfSyntaxErrors > 0) {
        sender ! TypeError("Error! Your code has syntax errors.")
      } else if (!typeChecker.visit(parseTree)) {
        sender ! TypeError("Error! Your code has semantic errors.")
      } else {
        circuit = modelGenerator.visit(parseTree) // compile the dsl
        sender ! CodeOk()
      }

    case BeginEvaluation(initialValues) =>
      if(circuit != null && eventManager == null) {
        eventManager = new BLXEventManager(100, new CircuitHandler)
        eventManager.start(circuit)

        initialValues.foreach(update)
      }

    case UpdateSocket(socket) => update(socket)

    case Die(message : String) =>
      if(eventManager != null)
        eventManager.stop() // THIS IS IMPORTANT
      if (outputChannel != null) {
        if (message != "")
          outputChannel.push(Json.toJson(Map("error" -> message)))
        outputChannel.eofAndEnd()
      }
      context.stop(self)
  }

  private class CircuitHandler extends BLXSignalQueue.BLXSignalQueueCallback {
    override def onSignalEvent(components: util.Set[BLXSignalReceiver]): Unit = {
      if(components != null)
        components.toSet.filter(_.isInstanceOf[BLXSocket]).foreach(component => {
          val socket: BLXSocket = component.asInstanceOf[BLXSocket]
          if(socket.getId != null && socket.getId != "_" && outputChannel != null) {
            outputChannel.push(Json.toJson(Map(
                "command" -> Json.toJson("update"),
                "socket" -> Json.toJson(Map(
                  "name" -> Json.toJson(socket.getId),
                  "value" -> Json.toJson(socket.getValue.toString))))))
          }
        })
    }
  }
}


/* Initialization messages */
case class OpenConnection()

case class Initialize(dsl: String)

case class Ready(enumerator: Enumerator[JsValue])

case class CodeOk()

case class TypeError(error: String)

/* Runtime messages */
case class BeginEvaluation(initialValues : List[SocketAssignment])
case class UpdateSocket(socket  : SocketAssignment)

/* Updates */
case class SocketAssignment(name: String, value: Boolean)

object SocketAssignment extends ((String, Boolean) => SocketAssignment) {
  implicit val socketAssignmentReads: Reads[SocketAssignment] = Json.reads[SocketAssignment]
}

/* Destruction message */
case class Die(message : String)
