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

/**
 * Created by ajr64 on 4/4/14.
 */

object DSLRunner {
  implicit val timeout = Timeout(3 seconds)

  def addCircuit(dsl: String): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    val evaluator: ActorRef = Akka.system.actorOf(Props[DSLRunner])

    (evaluator ? Initialize(dsl)).map {
      case CodeOk(enumerator) =>
        val iteratee = Iteratee.foreach[JsValue] {
          event =>
            (event \ "command").as[String] match {
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
              case "stop" => evaluator ! Die()
              case _ => /* ignore */
            }
        }.map {
          _ => evaluator ! Die()
        }

        (iteratee, enumerator)

      case TypeError(error) =>
        evaluator ! Die()

        val iteratee = Done[JsValue, Unit]((), Input.EOF)
        val enumerator = Enumerator[JsValue](JsObject(Seq("error" -> JsString(error)))) >>> Enumerator.enumInput(Input.EOF)

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

  def receive = {
    case Initialize(dsl) =>
      val bl: BooLeXLexer = new BooLeXLexer(new ANTLRInputStream(dsl))
      val bp: BooLeXParser = new BooLeXParser(new CommonTokenStream(bl))
      val parseTree: ModuleContext = bp.module()

      if (bp.getNumberOfSyntaxErrors > 0) {
        sender ! TypeError("Error! Your code has syntax errors.")
      } else if (!typeChecker.visit(parseTree)) {
        sender ! TypeError("Error! Your code has semantic errors.")
      } else {
        circuit = modelGenerator.visit(parseTree)
        sender ! CodeOk(outSocket)
      }

    case BeginEvaluation(initialValues) =>
      if(circuit != null && eventManager == null) {
        val sockets: List[BLXSocket] = circuit.getInputSockets.toList
        val socketMap : Map[String, BLXSocket] = (sockets map { sock => (sock.getId, sock) }).toMap
        val inits = initialValues.map({
          case SocketAssignment(name, value) => (socketMap.get(name).get, value.asInstanceOf[java.lang.Boolean])
        }).toMap

        eventManager = new BLXEventManager(inits, 250, new CircuitHandler)
        eventManager.start()
      }

    case UpdateSocket(socket) => //TODO: implement

    case Die() =>
      if (outputChannel != null) {
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
case class Initialize(dsl: String)

case class CodeOk(enumerator: Enumerator[JsValue])

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
case class Die()
