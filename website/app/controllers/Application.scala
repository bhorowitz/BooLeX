package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.Comet
import play.api.libs.iteratee._
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Promise
import scala.concurrent._
import ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def nothing = Action {
    Ok("")
  }

  def echo(dsl: String) = Action {
    var i = 0
    Akka.future {
      while (true) {
        i += 1
        Thread.sleep(1000)
      }
    }
    val timeStream = Enumerator(dsl) >>> Enumerator.repeatM {
      Promise.timeout(i.toString, 1000)
    }
    Ok.stream(timeStream.map(_.toString) &> Comet(callback = "parent.cometMessage"))
  }

}