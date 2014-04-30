// @SOURCE:/Users/graham/Documents/Sophomore/cpsc/439/BooLeX/website/conf/routes
// @HASH:7d1dd644f727639927a8916861589fa2449ccf09
// @DATE:Mon Apr 28 01:38:27 EDT 2014


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
        

// @LINE:7
private[this] lazy val controllers_Application_boolex1 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("boolex"))))
        

// @LINE:8
private[this] lazy val controllers_Application_save2 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("save"))))
        

// @LINE:9
private[this] lazy val controllers_Application_load3 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("load"))))
        

// @LINE:12
private[this] lazy val controllers_Assets_at4 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """boolex""","""controllers.Application.boolex"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """save""","""controllers.Application.save(dsl:String)"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """load""","""controllers.Application.load(hash:String)"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(controllers.Application.index, HandlerDef(this, "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
   }
}
        

// @LINE:7
case controllers_Application_boolex1(params) => {
   call { 
        invokeHandler(controllers.Application.boolex, HandlerDef(this, "controllers.Application", "boolex", Nil,"GET", """""", Routes.prefix + """boolex"""))
   }
}
        

// @LINE:8
case controllers_Application_save2(params) => {
   call(params.fromQuery[String]("dsl", None)) { (dsl) =>
        invokeHandler(controllers.Application.save(dsl), HandlerDef(this, "controllers.Application", "save", Seq(classOf[String]),"POST", """""", Routes.prefix + """save"""))
   }
}
        

// @LINE:9
case controllers_Application_load3(params) => {
   call(params.fromQuery[String]("hash", None)) { (hash) =>
        invokeHandler(controllers.Application.load(hash), HandlerDef(this, "controllers.Application", "load", Seq(classOf[String]),"POST", """""", Routes.prefix + """load"""))
   }
}
        

// @LINE:12
case controllers_Assets_at4(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        
}

}
     