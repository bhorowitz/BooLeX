package controllers

import play.api.mvc._
import play.api.libs.Comet
import play.api.libs.iteratee._
import play.api.libs.concurrent.Promise
import scala.concurrent._
import ExecutionContext.Implicits.global
// import boolex.typechecker.BooLeXTypeChecker
import boolex.logic.elements.circuitbuilder._
import play.api.libs.json.JsValue
import models.DSLRunner

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def nothing = Action {
    Ok("")
  }

  def boolex(dsl : String) = WebSocket.async[JsValue] { request =>
    DSLRunner.addCircuit(dsl)
  }

}