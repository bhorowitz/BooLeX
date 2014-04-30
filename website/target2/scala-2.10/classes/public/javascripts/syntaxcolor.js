function syntaxColor(textContent) 
      {
      	function spanStr (oper,color){
      		return	"<b><span style=\"color:"+color+";\">"+ oper + "</span></b>";
      	} 
      	var mapObj = {
   			"and": spanStr("and","#0066FF"),
   			"or" : spanStr("or","#0066FF"),
   			"not": spanStr("not","#0066FF"),
   			"xor": spanStr("xor","#0066FF"),
        "nand": spanStr("nand","#0066FF"),
        "xnor": spanStr("xnor","#0066FF"),
        "nor": spanStr("nor","#0066FF"),
        "main": spanStr("main","#993300"),
        "circuit": spanStr("circuit","#993300"),
        "out": spanStr("out","#993300"),
        "end": spanStr("end","#993300"),
        "=": spanStr("=","#CC0099"),
        ",": spanStr(",","#CC0099")
	   	  };
    		var mapOp = {
  			"\\*": spanStr("\*","#0066FF"),
  			"\\+": spanStr("\+","#0066FF"),
  			"\\'": spanStr("\'","#0066FF"),
        "\\^": spanStr("\^","#0066FF"),
		    };
        //var textContent = document.getElementById("test").innerText;
        // Code adaptation from http://stackoverflow.com/questions/20743683
        var reObj = new RegExp(Object.keys(mapObj).join("|"),"g");
        textContent = textContent.replace(reObj, function(matched){
        return mapObj[matched];})
        var reOp = new RegExp(Object.keys(mapOp).join("|"),"g");
        textContent = textContent.replace(reOp, function(matched){
        return mapOp["\\"+matched];})
        //document.getElementById("test").style.whiteSpace = "pre";
        //document.getElementById("test").innerHTML = textContent;
        return textContent
      } 