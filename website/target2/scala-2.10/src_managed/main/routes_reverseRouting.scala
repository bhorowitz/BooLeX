// @SOURCE:/Users/graham/Documents/Sophomore/cpsc/439/BooLeX/website/conf/routes
// @HASH:7d1dd644f727639927a8916861589fa2449ccf09
// @DATE:Mon Apr 28 01:38:27 EDT 2014

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:12
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers {

// @LINE:12
class ReverseAssets {
    

// @LINE:12
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          

// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
class ReverseApplication {
    

// @LINE:9
def load(hash:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "load" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("hash", hash)))))
}
                                                

// @LINE:8
def save(dsl:String): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "save" + queryString(List(Some(implicitly[QueryStringBindable[String]].unbind("dsl", dsl)))))
}
                                                

// @LINE:6
def index(): Call = {
   Call("GET", _prefix)
}
                                                

// @LINE:7
def boolex(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "boolex")
}
                                                
    
}
                          
}
                  


// @LINE:12
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.javascript {

// @LINE:12
class ReverseAssets {
    

// @LINE:12
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              

// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
class ReverseApplication {
    

// @LINE:9
def load : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.load",
   """
      function(hash) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "load" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("hash", hash)])})
      }
   """
)
                        

// @LINE:8
def save : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.save",
   """
      function(dsl) {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "save" + _qS([(""" + implicitly[QueryStringBindable[String]].javascriptUnbind + """)("dsl", dsl)])})
      }
   """
)
                        

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

// @LINE:7
def boolex : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.boolex",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "boolex"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:12
// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
package controllers.ref {


// @LINE:12
class ReverseAssets {
    

// @LINE:12
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          

// @LINE:9
// @LINE:8
// @LINE:7
// @LINE:6
class ReverseApplication {
    

// @LINE:9
def load(hash:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.load(hash), HandlerDef(this, "controllers.Application", "load", Seq(classOf[String]), "POST", """""", _prefix + """load""")
)
                      

// @LINE:8
def save(dsl:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.save(dsl), HandlerDef(this, "controllers.Application", "save", Seq(classOf[String]), "POST", """""", _prefix + """save""")
)
                      

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:7
def boolex(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.boolex(), HandlerDef(this, "controllers.Application", "boolex", Seq(), "GET", """""", _prefix + """boolex""")
)
                      
    
}
                          
}
        
    