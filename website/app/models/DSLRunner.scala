package models

import play.libs.Akka

import scala.concurrent.Future

import akka.actor._
import scala.concurrent.duration._
import scala.language.postfixOps

import play.api.libs.json._
import play.api.libs.iteratee._

import akka.util.Timeout
import akka.pattern.ask

import play.api.libs.concurrent.Execution.Implicits._
import boolex.antlr.{BooLeXParser, BooLeXLexer}
import boolex.typechecker.BooLeXTypeChecker
import boolex.logic.elements.circuitbuilder.BLXModelGenerator
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import boolex.antlr.BooLeXParser.ModuleContext


/**
 * Created by ajr64 on 4/4/14.
 */

object DSLRunner {
  implicit val timeout = Timeout(3 seconds)

  def addCircuit(dsl: String): Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    val evaluator: ActorRef = Akka.system.actorOf(Props[DSLRunner])

    (evaluator ? Begin(dsl)).map {
      case CodeOk(enumerator) =>
        val iteratee = Iteratee.foreach[JsValue] {
          event =>
            evaluator ! PrintMe(event)
        }.map {
          _ =>
          evaluator ! Die()
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
  var outputChannel : Concurrent.Channel[JsValue] = null
  val outSocket = Concurrent.unicast[JsValue] { c => outputChannel = c }

  val INITIALIZE_TO_FALSE = false
  val typeChecker: BooLeXTypeChecker = new BooLeXTypeChecker()
  var modelGenerator: BLXModelGenerator = new BLXModelGenerator(INITIALIZE_TO_FALSE)

  def receive = {
    case Begin(dsl) =>
      val bl : BooLeXLexer = new BooLeXLexer(new ANTLRInputStream(dsl))
      val bp : BooLeXParser = new BooLeXParser(new CommonTokenStream(bl))
      val parseTree: ModuleContext = bp.module()
      if(bp.getNumberOfSyntaxErrors > 0) {
        sender ! TypeError("Error! Your code has syntax errors.")
      } else if(!typeChecker.visit(parseTree)) {
        sender ! TypeError("Error! Your code has semantic errors.")
      } else {
        sender ! CodeOk(outSocket)
      }

    case PrintMe(json) =>
      outputChannel.push(json)

    case Die() =>
      if(outputChannel != null)
        outputChannel.end()
      context.stop(self)
  }
}


case class Begin(dsl: String)

case class CodeOk(enumerator: Enumerator[JsValue])

case class TypeError(error: String)

case class PrintMe(json : JsValue)

case class Die()
