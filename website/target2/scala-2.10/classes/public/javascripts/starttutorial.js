
var currentslide = 0;
var heading1 = "<h2><span style=\"color: #777;\">BOOLEAN LOGIC</span></h2>";
var heading2 = "<h2><span style=\"color: #777;\">SIMPLE STATEMENTS</span></h2>";
var heading3 = "<h2><span style=\"color: #777;\">A FIRST BooLeX CIRCUIT</span></h2>";
var heading4 = "<h2><span style=\"color: #777;\">WHY 0 and 1?</span></h2>";
var heading5 = "<h2><span style=\"color: #777;\">ENTER LOGICAL OPERATORS</span></h2>"; 
var heading6 = "<h2><span style=\"color: #777;\">A STEP UP : THE FULL ADDER</span></h2>";
var heading7 = "<h2><span style=\"color: #777;\">THANKS!</span></h2>";
var str1 = "Computers are made of electronic parts that carry electric signals around. How do we work up from that level to a device that runs websites and displays tutorials about itself on a screen?<br><br>That is a complicated question. Boolean algebra is one of the concepts that we need to answer it. This tutorial is a very basic introduction to boolean logic (a small part of boolean algebra), and the BooLeX interface.<br><br>";
var str2 = "Boolean algebra is normal algebra (math) done using only two digits – 0 and 1. Using these digits we can use boolean logic.";
var str3 = "For boolean logic we need only one mantra :<BR> <BR> <B> 0 is false; 1 is true. </B><BR><BR> Got that memorized? Great. That is really the only thing you need to remember.<BR><br> Now, when someone says, “The sun rises in the East”, one usually goes “yes, that's true”. Then, in Boolean logic that statement is assigned a “truth” value of 1. ";
var str4 = "Similarly, if one were presented with the statement, “The Earth is 100 years old”, that's clearly false. So we assign a truth value of 0 to this statement.<br><br>More generally, if A is a statement, then it must classifiable as true or false, and:<br><br>If A is true, then A evaluates to 1.<br><br<br><br>If A is false, then A evaluates to 0.<br><br>";
var str5 = "Let's see what this concept might look like as a circuit. First, click on the switch in the toolbox on the left. Then, Drag and drop it onto the workspace....";
var nextstr = "<br><br><br><br><span style=\"color: #777;\">Hit next when you're done!</span>";
var str6 = "Great! Now, select the bulb from the toolbox and place it next to the switch.....";
var str7 = "Finally, connect the switch to the bulb by clicking on the black spot to the right of the switch and dragging a line to the bulb's spot. What you get should look like this:";
var str8 = "Perfect. We've just built our first circuit. Before we take it or a spin, let's see how it ties back to our theory of boolean logic. <br><br> The switch represents some statement A. This statement A is an input to our circuit.<br> If the statement A is true, we can turn the switch on by clicking the switch. If A is false, we flip the switch off.<br><br>";
var str9 = "The bulb is an output. If the bulb is glowing (on), then our circuit output at that point is 1 or true. If it is off, then the output at that point is false.<br><br>";
var str10= "The line joining the switch and the bulb is a wire through which current can flow.<br> If the switch is off (which corresponds to A being false), there is no current in the wire and the bulb does not glow, confirming that A is false. <br><br> However if A is true...something more interesting happens. Hit <b>Start</b> above and try flipping on the switch...<br>";
var imageswitch = "<br><br><br><br><img src=\"/assets/images/tutorialswitch.png\">";
var image1 = "<br><br><br><br><img src=\"/assets/images/tutorial2.png\">";
var image2 = "<br><br><br><br><img src=\"/assets/images/tutorial1.png\">";
var imagebulb = "<br><br><br><br><img src=\"/assets/images/lightbulb_off.png\">";
var imageor = "<br><br><img src=\"/assets/images/or_gate.png\"><br><br><br>";
var imageand = "<br><br><img src=\"/assets/images/and_gate.png\"><br>";
var imagenot = "<br><br><img src=\"/assets/images/not_gate.png\"><br>";
var imageha = "<br><br><img src=\"/assets/images/tutorialha.png\"><br>";
var imagefa = "<br><br><img src=\"/assets/images/tutorialfa.png\"><br>";
var str11 = "The bulb now glows, confirm the truth value of the A (and effectively, the whole circuit).";
var str12 = "A fundamental question one may ask at this point is why do we only use two digits, and not three, or four, or ten.<br><br> A part of the reason is the nature of boolean logic – there are only two “states” - a true state or a false state.<br> The other reason is the electric signals we talked about. Using only two digits allows us to translate boolean logic into electric signals : a “0” is represented as a no-current, low current or “off” output and a “1” as high-current or “on” output. This is clearly demonstrated with the switch and gate circuit we just built.";
var str13 ="<br><br>However, boolean logic can achieve much more using just these digits in combination using logical operators."
var str14 ="Using operators, we can transform simple statements into more complex logical statements to evaluate. <br><br> <b> NOT </b> : The NOT gate takes a statement A and inverts its truth value. If A is true, it returns false. If A is false, it returns true.";
var str15 ="<b> AND </b>: The AND gate takes two statements A and B and returns true if and only if both A and B are true.<br><br>";
var str16 ="<b> OR </b>: The OR gate takes two statements A and B and returns true if at least one of A and B are true. <br><br>";
var str17 = "At this point we strongly recommend you try playing around with these three basic logic gates and begin to get comfortable with the idea of operators.";
var str18 = "Now that we have a basic idea of what boolean logic looks like, let's see how powerful it can get. To demonstrate the potential of boolean logic, let us create a full adder, a circuit to add two numbers:<br><br>";
var str19 = "First, we write a half adder. This system takes two inputs, A and B. It sends out two outputs, labelled cout and sum in the figure respectively. Sum is the sum of the two digits, and cout is the value carried over:<br><br>";
var str20 = "<br><br> Here, the new gate used is an XOR, or exclusive or. It only returns true if and only if one of A and B are true (not both). The carry out bit so obtained can be passed as a carry in to the next place. Thus, we now use the old cout as a third digit to add to A and B at that position.";
var str21 = "By combining half adders and forwarding the carry outs, we can make full adders for arbitrarily big numbers.<br><br><br>";
var str22 = "This gives us a glimpse into how boolean circuits translate into the computers we use today. Let's see how close you get to building yours! <br>";
var thanks = "Thanks for trying out our project. We'll be updating and scaling as and when we have free time, so do check back on and off. This project was brought to you by: <br><br> <b>Alex Reinking <br><br> Graham Kaemmer <br><br> Dani Dickstein <br><br> Abhishek Chandra";

var tutorialtextarray = [heading1+str1+str2,heading2+str3+str4, heading3+str5+imageswitch+nextstr,heading3+str6+imagebulb+nextstr,heading3+str7+image1+nextstr,
		heading3+str8+str9+str10+nextstr,heading3+str11+image2,heading4+str12+str13, heading5+str14+imagenot+str15+imageand+str16+imageor+str17,heading6+str18+str19+imageha+str20,heading6+str21+imagefa+str22,heading7+thanks];
var upperbound = tutorialtextarray.length;
function startTutorial() {
	if (document.getElementById("tutorial-container").style.display=="inline"){
		document.getElementById("tutorial-container").style.display="none";
	}
	else{
	document.getElementById("tutorial-container").style.display="inline";
	document.getElementById("tutorial-div").innerHTML=tutorialtextarray[currentslide];
	}
}

function nextSlide() {
	if (currentslide < upperbound - 1) {
	currentslide = currentslide + 1;
	}
	document.getElementById("tutorial-div").innerHTML=tutorialtextarray[currentslide];
}

function previousSlide() {
	if (currentslide > 0) {
	currentslide = currentslide - 1;
	}
	document.getElementById("tutorial-div").innerHTML=tutorialtextarray[currentslide];
}

function stopTutorial() {
	currentslide=0;
	document.getElementById("tutorial-container").style.display="none";
}