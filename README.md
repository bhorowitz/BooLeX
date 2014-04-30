## Overview ##

We developed BooLeX to provide a convenient tool for new Computer Science and Electrical Engineering students to conceptualize Boolean circuits.  We also found many circuit simulators already available (even proprietary ones) to be rather inadequate or ineffective.  Currently introductory CS and EE courses (for example, Yale's EENG 201 course) depend on industrial-strength simulators, and require their students to master Verilog or VHDL in order to demonstrate basic circuit architecture.  We wanted to create a lightweight alternative that still had large expressive power, so we created BooLeX.

BooLeX's primary feature is a circuit lab that lets users drag and drop circuit elements to assemble any logic circuit of their design.  The design is then translated into BooLeX's domain specific language (DSL) and the code is sent to the back end for interpretation.  The back end interprets the DSL and reconstructs the circuit from the code, and then begins to simulate signals being transmitted through the various gates.  Updates are forwarded back to the front end, creating an animated simulator of the circuit design.

The primary advantage of the DSL translation is that users can also directly write DSL code themselves (the language is far simpler than either Verilog or VHDL as it is stripped down to its most basic essentials) and then that code will be passed to the back end for results.

## How to Use ##
<font color="red">This section needs to be completed by Graham and Abhishek.  It should discuss the circuit lab, the tutorial, and the editor.</font>

## Underlying Architecture ##
The basic architecture of BooLeX is comprised of a web front end and a Java back end connected by the Java Play framework.

### The Front End ###
<font color="red">This section needs to be completed by Graham and Abhishek.</font>

When a circuit element is dragged into the circuit lab, the code that would be used to represent the circuit is generated behind the scenes.  As each additional element is added, the DSL is updated to reflect the new circuit.  Finally, when the start button is pressed, the generated code is passed back to the middleware for the simulation to begin.

The front end is coded using HTML, CSS, and JavaScript, and also makes use of CoffeeScript to improve the readability of what would otherwise be complex JavaScript code.  Currently there are some complex JavaScript files that could be written in CoffeeScript, and we hope to make this transition soon.

### Middleware ###

The BooLeX middleware consists of a few Scala functions in the Play framework that integrate the front end with the back end.  <font color="red">This section needs to be filled in by Alex.</font>

### The Back End ###
The generated BooLeX code is passed to [ANTLR](http://www.antlr.org/) for lexical and syntactic analysis.  ANTLR generates a parse tree of the code that is then type-checked and translated into a BooLeX circuit object which consists of a configuration of gates and sockets.  Afterward, a new thread is spun off for the circuit simulation, and the initial signals are propagated through the circuit.  After each gate delay, the back end transmits the list of all updated sockets with their new values to the front end so the drawn circuit can be animated.  This process continues until the back end receives the stop signal, at which time the thread is terminated.

### Testing ###
We used JUnit to write the unit and functional tests for back end circuit simulation.  We did extensive manual testing for the front end JavaScript implementation of circuits.

The following major use cases are tested:
- Ripple Carry Adder
- SR Latch
- D Latch
- Positive-Edge D Flip-FLop

## Contributing ##
If you would like to contribute to this project, fork the repository and submit a pull request.  You will need to configure your IntelliJ IDEA environment as follows:

<font color="red">This section needs to be filled in by Alex.</font>

## Development Team ##
Abhishek Chandra
- Developed JS editor widget with syntax highlighting
- Wrote the BooLeX tutorial on the basics of Boolean logic

Dani Dickstein
- Co-designed BooLeX language for representing circuits
- Co-wrote translation of BooLeX into circuit object
- Developed socket, gate, and circuit objects to build up connections from circuit definition
- Implemented simulation of signal I/O updates for sockets in the circuit
- Co-wrote front end circuit generators for basic integrated circuits

Graham Kaemmer
- Developed front end circuit lab for drag-and-drop circuit builder
- Designed basic building blocks for circuits (basic gates, switches, and lights)
- Designed clean display of socket connections
- Developed tool for creating integrated circuits
- Developed import/export of integrated circuits

Alex Reinking
- Co-designed BooLeX language for representing circuits
- Developed middlware using the Play framework
- Wrote ANTLR grammar for BooLeX
- Wrote semantic analysis (type-checker) for BooLeX language
- Co-wrote translation of BooLeX into circuit object
- Co-wrote front end circuit generators for basic integrated circuits
- Wrote functional tests for back end and unit tests for semantic analysis of BooLeX