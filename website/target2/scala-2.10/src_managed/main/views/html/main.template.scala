
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object main extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Html,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(title: String)(content: Html):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.32*/("""

<!DOCTYPE html>
<html>
  <head>
    <title>BooLeX</title>
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href=""""),_display_(Seq[Any](/*9.35*/routes/*9.41*/.Assets.at("stylesheets/main.css"))),format.raw/*9.75*/("""">
    <script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/portal/1.0rc3/portal.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/socket.io/0.9.16/socket.io.min.js"></script>
    <script src=""""),_display_(Seq[Any](/*14.19*/routes/*14.25*/.Assets.at("javascripts/easeljs-NEXT.min.js"))),format.raw/*14.70*/(""""></script>
    <script src=""""),_display_(Seq[Any](/*15.19*/routes/*15.25*/.Assets.at("javascripts/main.js"))),format.raw/*15.58*/(""""></script>
    <script src=""""),_display_(Seq[Any](/*16.19*/routes/*16.25*/.Assets.at("javascripts/syntaxcolor.js"))),format.raw/*16.65*/(""""></script>
    <script src=""""),_display_(Seq[Any](/*17.19*/routes/*17.25*/.Assets.at("javascripts/starttutorial.js"))),format.raw/*17.67*/(""""></script>
  </head>
  <body>
    <div class="header">
      <div class="navbar navbar-default">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
          <div class="navbar-header">
            <a class="navbar-brand" href="#">BooLeX</a>
          </div>

          <div class="nav navbar-nav navbar-right">
            <button class="btn btn-success navbar-btn" id="start-stop-button">Start</button>
            <div class="btn-group">
              <button class="btn btn-default navbar-btn" id="integrated-circuit-button"><i class="fa fa-sign-in"></i> Create Integrated Circuit</button>
              <button class="btn btn-default navbar-btn" id="load-circuit-button"><i class="fa fa-file-text-o"></i> Load Circuit</button>
            </div>
            <button class="btn btn-default navbar-btn" id="new-button"><i class="fa fa-edit"></i> New</button>
            <button class="btn btn-default navbar-btn" id="tutorial-button">Tutorial</button>
          </div>
        </div>
      </div>
    </div>
    <div class="container">
      <canvas id="boolex-stage"></canvas>
    </div>
    <div class="panel panel-default code-panel">
      <div class="panel-heading">
        <h3 class="panel-title">BooLeX Code</h3>
      </div>
      <div class="panel-body">
        <pre id="code"></pre>
      </div>
    </div>
    <div class="modal fade" id="load-circuit-modal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            <h4 class="modal-title">Modal title</h4>
          </div>
          <div class="modal-body" id="modal-body-load">
            <p>Enter the BooLeX code for the circuit below:</p>
            <textarea name="circuit-dsl" id="circuit-dsl" class="form-control" placeholder="circuit example()..."></textarea>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
            <button type="button" class="btn btn-primary" id="insert-circuit-button">Insert</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <div class="tutorial" id="tutorial-container">
      <div class="tutorial-body" id="tutorial-div">
      </div>

      <div class="tutorial-footer">
        <button class="btn btn-primary" id="previous-button">Previous</button>
        <button class="btn btn-primary" id="next-button">Next</button> 
        <button class="btn btn-default" id="cancel-button">Cancel</button>
        
      </div>
    </div>
    
  </body>
</html>
"""))}
    }
    
    def render(title:String,content:Html): play.api.templates.HtmlFormat.Appendable = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Mon Apr 28 14:11:42 EDT 2014
                    SOURCE: /Users/graham/Documents/Sophomore/cpsc/439/BooLeX/website/app/views/main.scala.html
                    HASH: 49c02e2a85dc1cae84113bf448dcdd999bf19d7d
                    MATRIX: 560->1|684->31|1018->330|1032->336|1087->370|1500->747|1515->753|1582->798|1648->828|1663->834|1718->867|1784->897|1799->903|1861->943|1927->973|1942->979|2006->1021
                    LINES: 19->1|22->1|30->9|30->9|30->9|35->14|35->14|35->14|36->15|36->15|36->15|37->16|37->16|37->16|38->17|38->17|38->17
                    -- GENERATED --
                */
            