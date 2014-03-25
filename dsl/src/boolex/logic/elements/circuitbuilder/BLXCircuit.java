package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXConstantSocket;
import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.standard.*;

import static boolex.helpers.PrettyPrintHelper.arrayToString;

import java.util.*;

/**
 * A BLXCircuit object is used to instantiate a logic circuit.  Most methods are used to join together sockets and
 * gates, but embedding circuits within circuits is also permitted.  This class is used in tandem with the BooLeX
 * type-checker, and as the DSL code from the front end is type-checked the BLXCircuit is built.
 * 
 * After the circuit has been built, getInputSignals() can be called to get a list of all the signals that should be
 * fired when the circuit simulation begins.
 * 
 * One of the convenient aspects of BLXCircuit is that after the last gate is attached, the result of the gate is saved
 * so that gates can be chained together.
 * 
 * Example usage:
 *   Say you are dealing with a D-latch (inputs are "D" and "CLK"), where all wires are initialized to 0.
 *   The circuit can be constructed as follows:
 *   
 *   new BLXCircuit("D",true).not("dNot").and("CLK","S").nor("Q","Qnot").nor("R","Q").load("D").and("CLK","R");
 * 
 *   A D-latch is a sequential circuit, but whether sequential or combinational, the circuits can be written in this
 *   linear fashion.
 * 
 * @author Dani Dickstein
 * @version 0.1
 */
public class BLXCircuit {

    /**
     * A runtime exception should two different output sockets in the circuit be assigned the same id.  This should be
     * an error caught by the type-checker, so an exception is thrown if it is detected at this stage.
     */
    public static class DuplicateIdException extends RuntimeException {
        public DuplicateIdException(String id) {
            super("two different output sockets were assigned id "+id);
        }
    }

    /**
     * A runtime exception is thrown if a gate is passed a null id as an input.  The DSL does not offer support for
     * null inputs to gates, so this should actually be an error that would be caught by the parser, even before
     * type-checking.
     */
    public static class MissingIdException extends RuntimeException {
        public MissingIdException() {
            super("received null id as input to gate");
        }
    }

    /**
     * A runtime exception is thrown if a gate is passed a null circuit as an input.  This should never happen.
     */
    public static class MissingIntegratedCircuitException extends RuntimeException {
        public MissingIntegratedCircuitException() {
            super("received null integrated circuit as input to a gate");
        }
    }

    /**
     * An exception is thrown if this circuit still has inputs that have not been given initial values (in other words,
     * we are trying to simulate an unfinished circuit).  TODO Ask Alex if this is even permitted by the type-checker.
     * TODO find out if we want to raise an exception or just generate null signals -> do we allow unfinished circuits?
     */
    public static class UnresolvedCircuitInputsException extends Exception {
        public UnresolvedCircuitInputsException(Set<String> unresolvedInputs) {
            super("circuit inputs "+ arrayToString(
                    unresolvedInputs.toArray(new String[unresolvedInputs.size()])
            ) +" must be resolved before launching simulation");
        }
    }

    private Map<String,BLXSocket> unclaimedOutputSockets;
    private Map<String,BLXSocket> claimedOutputSockets;
    private Map<String,BLXSocket> markedOutputSockets;
    private BLXSocket currentOutputSocket;
    private Boolean defaultValue;

    private final BLXConstantSocket trueSocket = new BLXConstantSocket(true);
    private final BLXConstantSocket falseSocket = new BLXConstantSocket(false);

    /**
     * Constructor for a BLXCircuit
     * @param firstInputId The id of the first socket in the circuit (does not have to be an actual circuit input)
     * @param initializeToFalse Set to True if all sockets should initially begin at 0;
     *                          Set to False if all sockets should initially begin at Undefined (NULL)
     */
    public BLXCircuit(String firstInputId, boolean initializeToFalse) {
        unclaimedOutputSockets = new HashMap<>();
        claimedOutputSockets = new HashMap<>();
        defaultValue = initializeToFalse ? false : null;
        markedOutputSockets = new HashMap<>();
        load(firstInputId);
    }

    /**
     * Get a map of input sockets and initial values for this circuit, given a map of socket ids to initial values.
     * @param idMap A map of socket ids to initial values.  If a value is given for a socket that is not an "input
     *              socket" (it is embedded in the circuit and receives its input from the output of a gate), that
     *              value is ignored and no signal is generated for it.
     * @return A map of BLXSockets to Boolean initial values
     * @throws UnresolvedCircuitInputsException if missing initial value for a circuit input
     */
    public Map<BLXSocket,Boolean> getInputSocketMap(Map<String,Boolean> idMap)
    throws UnresolvedCircuitInputsException {
        Map<BLXSocket,Boolean> socketMap = new HashMap<>();
        Set<String> unresolvedInputs = getUnresolvedInputs();
        for (Map.Entry<String,Boolean> entry : idMap.entrySet()) {
            String socketId = entry.getKey();
            Boolean initialValue = entry.getValue();
            if (unclaimedOutputSockets.containsKey(socketId)) {
                socketMap.put(unclaimedOutputSockets.get(socketId), initialValue);
                unresolvedInputs.remove(socketId);
            }
        }
        if (unresolvedInputs.size() > 0)
            throw new UnresolvedCircuitInputsException(unresolvedInputs);
        // add signals for the constant sockets
        socketMap.put(trueSocket, true);
        socketMap.put(falseSocket, false);
        return socketMap;
    }

    /**
     * Get a list of the sockets (by id) that have yet to be resolved
     * @return Ids of all the sockets that have not yet been resolved
     */
    public Set<String> getUnresolvedInputs() {
        return unclaimedOutputSockets.keySet();
    }

    /**
     * Check to see if there are any unresolved inputs
     * @return True if there are unresolved inputs remaining, False otherwise
     */
    public boolean hasUnresolvedInputs() {
        return !unclaimedOutputSockets.isEmpty();
    }

    /**
     * Load (or create) the socket with this id in the circuit.  Call this method when you want to load a new
     * output socket for chaining a different part of the circuit.  You will need to call this method unless your
     * circuit can be described as a perfect cycle (in which case it can be built in a single chained call).
     * 
     * @param socketId The id of the socket to load (or create)
     * @return This BLXCircuit object
     * @throws MissingIdException if socketId is null
     */
    public BLXCircuit load(String socketId) {
        if (socketId == null)
            throw new MissingIdException();
        currentOutputSocket = getOrCreateSocket(socketId);
        return this;
    }

    /**
     * Attach a NOT gate to the currently stored socket, and store the gate's output socket.
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit not(String outputId) {
        if (outputId != null)
            currentOutputSocket = configureUnaryGate(new BLXNotGate(), currentOutputSocket, outputId);
        return this;
    }

    /**
     * Attach a buffered gate to the currently stored socket, and store the gate's output socket.
     * @param outputId The id of the output socket for this buffer
     * @return This BLXCircuit object
     */
    public BLXCircuit buffer(String outputId) {
        if (outputId != null)
            currentOutputSocket = configureUnaryGate(new BLXBuffer(), currentOutputSocket, outputId);
        return this;
    }

    /**
     * Attach an AND gate to the currently stored socket, and store the gate's output socket.
     * @param secondInputId The id of the second input socket to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit and(String secondInputId, String outputId) {
        return binaryJoin(new BLXAndGate(), secondInputId, outputId);
    }

    /**
     * Attach an AND gate to the currently stored socket, and store the gate's output socket.
     * 
     * Note that since this gate takes a constant value as its second input, one of the two inputs will be moot.
     * This means that if this method is called, the circuit can be simplified (may be useful in implementing a later
     * circuit simplification feature).
     * 
     * @param constValue The second input to this gate will be a constant boolean value of constValue
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    //public BLXCircuit and(boolean constValue, String outputId) {
    //    return binaryJoin(new BLXAndGate(), constValue, outputId);
    //}

    /**
     * Attach an AND gate to the currently stored socket, and store the gate's output socket.
     * @param inputCircuit The output of the socket that is currently stored by inputCircuit will be passed as the
     *                     second input to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit and(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXAndGate(), inputCircuit, outputId);
    }

    /**
     * Attach an OR gate to the currently stored socket, and store the gate's output socket.
     * @param secondInputId The id of the second input socket to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit or(String secondInputId, String outputId) {
        return binaryJoin(new BLXOrGate(), secondInputId, outputId);
    }

    /**
     * Attach an OR gate to the currently stored socket, and store the gate's output socket.
     *
     * Note that since this gate takes a constant value as its second input, one of the two inputs will be moot.
     * This means that if this method is called, the circuit can be simplified (may be useful in implementing a later
     * circuit simplification feature).
     *
     * @param constValue The second input to this gate will be a constant boolean value of constValue
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    //public BLXCircuit or(boolean constValue, String outputId) {
    //    return binaryJoin(new BLXOrGate(), constValue, outputId);
    //}

    /**
     * Attach an OR gate to the currently stored socket, and store the gate's output socket.
     * @param inputCircuit The output of the socket that is currently stored by inputCircuit will be passed as the
     *                     second input to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit or(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXOrGate(), inputCircuit, outputId);
    }

    /**
     * Attach an XOR gate to the currently stored socket, and store the gate's output socket.
     * @param secondInputId The id of the second input socket to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit xor(String secondInputId, String outputId) {
        return binaryJoin(new BLXXorGate(), secondInputId, outputId);
    }

    /**
     * Attach an XOR gate to the currently stored socket, and store the gate's output socket.
     * @param constValue The second input to this gate will be a constant boolean value of constValue
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    //public BLXCircuit xor(boolean constValue, String outputId) {
    //    return binaryJoin(new BLXXorGate(), constValue, outputId);
    //}

    /**
     * Attach an XOR gate to the currently stored socket, and store the gate's output socket.
     * @param inputCircuit The output of the socket that is currently stored by inputCircuit will be passed as the
     *                     second input to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit xor(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXXorGate(), inputCircuit, outputId);
    }

    /**
     * Attach a NAND gate to the currently stored socket, and store the gate's output socket.
     * @param secondInputId The id of the second input socket to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit nand(String secondInputId, String outputId) {
        return binaryJoin(new BLXNandGate(), secondInputId, outputId);
    }

    /**
     * Attach a NAND gate to the currently stored socket, and store the gate's output socket.
     *
     * Note that since this gate takes a constant value as its second input, one of the two inputs will be moot.
     * This means that if this method is called, the circuit can be simplified (may be useful in implementing a later
     * circuit simplification feature).
     *
     * @param constValue The second input to this gate will be a constant boolean value of constValue
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    //public BLXCircuit nand(boolean constValue, String outputId) {
    //    return binaryJoin(new BLXNandGate(), constValue, outputId);
    //}
    //
    /**
     * Attach a NAND gate to the currently stored socket, and store the gate's output socket.
     * @param inputCircuit The output of the socket that is currently stored by inputCircuit will be passed as the
     *                     second input to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit nand(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXNandGate(), inputCircuit, outputId);
    }

    /**
     * Attach a NOR gate to the currently stored socket, and store the gate's output socket.
     * @param secondInputId The id of the second input socket to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit nor(String secondInputId, String outputId) {
        return binaryJoin(new BLXNorGate(), secondInputId, outputId);
    }

    /**
     * Attach a NOR gate to the currently stored socket, and store the gate's output socket.
     *
     * Note that since this gate takes a constant value as its second input, one of the two inputs will be moot.
     * This means that if this method is called, the circuit can be simplified (may be useful in implementing a later
     * circuit simplification feature).
     *
     * @param constValue The second input to this gate will be a constant boolean value of constValue
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    //public BLXCircuit nor(boolean constValue, String outputId) {
    //    return binaryJoin(new BLXNorGate(), constValue, outputId);
    //}

    /**
     * Attach a NOR gate to the currently stored socket, and store the gate's output socket.
     * @param inputCircuit The output of the socket that is currently stored by inputCircuit will be passed as the
     *                     second input to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit nor(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXNorGate(), inputCircuit, outputId);
    }

    /**
     * Attach an XNOR gate to the currently stored socket, and store the gate's output socket.
     * @param secondInputId The id of the second input socket to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit xnor(String secondInputId, String outputId) {
        return binaryJoin(new BLXXnorGate(), secondInputId, outputId);
    }

    /**
     * Attach an XNOR gate to the currently stored socket, and store the gate's output socket.
     * @param constValue The second input to this gate will be a constant boolean value of constValue
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    //public BLXCircuit xnor(boolean constValue, String outputId) {
    //    return binaryJoin(new BLXXnorGate(), constValue, outputId);
    //}

    /**
     * Attach an XNOR gate to the currently stored socket, and store the gate's output socket.
     * @param inputCircuit The output of the socket that is currently stored by inputCircuit will be passed as the
     *                     second input to this gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    public BLXCircuit xnor(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXXnorGate(), inputCircuit, outputId);
    }

    /**
     * Specify the input source of a socket in the circuit as a constant value.
     * @param constValue The value to which the socket will be set
     * @param inputId The id of the socket to set to this constant value
     * @return This BLXCircuit object
     */
    public BLXCircuit input(boolean constValue, String inputId) {
        BLXSocket inputSocket = constValue ? trueSocket : falseSocket;
        BLXSocket outputSocket = claimOutputSocket(inputId);
        inputSocket.addTarget(outputSocket);
        return this;
    }

    /**
     * Specify the input source of a socket in the circuit as the currently stored socket of the parameter circuit
     * @param inputCircuit The circuit whose current output socket will serve as input to a socket in this circuit
     * @param inputId The id of the socket whose input source is being specified
     * @return This BLXCircuit object
     */
    public BLXCircuit input(BLXCircuit inputCircuit, String inputId) {
        BLXSocket outputSocket = claimOutputSocket(inputId);
        inputCircuit.currentOutputSocket.addTarget(outputSocket);
        return this;
    }

    /**
     * Specify the inputs to this circuit by a mapping of names of sockets in this circuit to external output sockets
     * @param inputSocketMap A mapping of input socket ids to their external sources
     * @return This BLXCircuit object
     */
    public BLXCircuit input(Map<String, BLXSocket> inputSocketMap) {
        for (Map.Entry<String, BLXSocket> socketConnection : inputSocketMap.entrySet()) {
            String socketId = socketConnection.getKey();
            BLXSocket inputSocket = socketConnection.getValue();
            BLXSocket outputSocket = claimOutputSocket(socketId);
            inputSocket.addTarget(outputSocket);
        }
        return this;
    }

    public BLXCircuit remarkOutputSocket(String oldSocketId, String newSocketId) {
        return unmarkOutputSocket(oldSocketId).markOutputSocket(newSocketId);
    }

    public BLXCircuit markOutputSocket(String socketId) {
        markedOutputSockets.put(socketId, getOrCreateSocket(socketId));
        return this;
    }

    public BLXCircuit markOutputSockets(List<String> socketIds) {
        socketIds.forEach(this::markOutputSocket);
        return this;
    }

    public BLXCircuit unmarkOutputSocket(String socketId) {
        markedOutputSockets.remove(socketId);
        return this;
    }

    public BLXCircuit unmarkOutputSockets(List<String> socketIds) {
        socketIds.forEach(this::unmarkOutputSocket);
        return this;
    }

    public Map<String,BLXSocket> getMarkedOutputSockets() {
        return markedOutputSockets;
    }

    /**
     * Precondition: There are no sockets in the parameter circuit that share ids with the sockets in this one.
     * @param circuit The circuit to consume into this circuit
     * @return This BLXCircuit object
     */
    public BLXCircuit consume(BLXCircuit circuit) {
        unclaimedOutputSockets.putAll(circuit.unclaimedOutputSockets);
        claimedOutputSockets.putAll(circuit.claimedOutputSockets);
        markedOutputSockets.putAll(circuit.markedOutputSockets);
        mergeConstantSockets(circuit);
        return this;
    }

    //------------------------------------------------------------------------------------\\
    //                             Private Methods Begin Here                             \\
    //------------------------------------------------------------------------------------\\

    private void mergeConstantSockets(BLXCircuit circuit) {
        trueSocket.addTargets(circuit.trueSocket.getTargets());
        falseSocket.addTargets(circuit.falseSocket.getTargets());
    }

    /**
     * Join the stored socket and a second input socket with a binary gate, and store the gate's output socket
     * @param gate The binary gate
     * @param secondInputId The id of the second input to the gate
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     * @throws MissingIdException if secondInputId is null
     */
    private BLXCircuit binaryJoin(BLXGate gate, String secondInputId, String outputId) {
        if (secondInputId == null)
            throw new MissingIdException();
        return binaryJoin(gate, getOrCreateSocket(secondInputId), outputId);
    }

    /**
     * Join the stored socket and a constant boolean value with a binary gate, and store the gate's output socket
     * @param gate The binary gate
     * @param constValue The second input to this gate will be a constant boolean value of constValue
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    //private BLXCircuit binaryJoin(BLXGate gate, boolean constValue, String outputId) {
    //    return binaryJoin(gate, constValue ? trueSocket : falseSocket, outputId);
    //}

    /**
     * Join the stored socket of this circuit and of the parameter circuit, and store the gate's output socket
     * @param gate The binary gate
     * @param inputCircuit The circuit whose current output socket will serve as the second input
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     * @throws MissingIntegratedCircuitException if inputCircuit is null
     */
    private BLXCircuit binaryJoin(BLXGate gate, BLXCircuit inputCircuit, String outputId) {
        if (inputCircuit == null)
            throw new MissingIntegratedCircuitException();
        mergeConstantSockets(inputCircuit);
        return binaryJoin(gate, inputCircuit.currentOutputSocket, outputId);
    }

    /**
     * Join the stored socket of this circuit and another output socket as inputs to this gate, and store the gate's
     * output socket
     * @param gate The binary gate
     * @param secondOutputSocket The source of this gate's second input
     * @param outputId The id of the output socket for this gate
     * @return This BLXCircuit object
     */
    private BLXCircuit binaryJoin(BLXGate gate, BLXSocket secondOutputSocket, String outputId) {
        if (outputId != null)
            currentOutputSocket = configureBinaryGate(gate, currentOutputSocket, secondOutputSocket, outputId);
        return this;
    }

    /**
     * Get the socket with this id if it already exists, or create one if it does not
     * @param socketId The id of the socket to get or create
     * @return The socket
     */
    private BLXSocket getOrCreateSocket(String socketId) {
        BLXSocket socket;
        if (claimedOutputSockets.containsKey(socketId))
            socket = claimedOutputSockets.get(socketId);
        else if (unclaimedOutputSockets.containsKey(socketId))
            socket = unclaimedOutputSockets.get(socketId);
        else {
            socket = new BLXSocket(socketId, defaultValue);
            unclaimedOutputSockets.put(socketId,socket);
        }
        return socket;
    }

    /**
     * Connect an input socket to a unary gate
     * @param gate The unary gate
     * @param input The socket that will be connected as input
     * @param outputId The id of the gate's output socket
     * @return The output socket
     */
    private BLXSocket configureUnaryGate(BLXGate gate, BLXSocket input, String outputId) {
        linkOutputSocketToGateInput(input, 0, gate);
        BLXSocket gateOutputSocket = claimOutputSocket(outputId);
        gate.setOutputSocket(0, gateOutputSocket);
        return gateOutputSocket;
    }

    /**
     * Connect two input sockets to a binary gate
     * @param gate The binary gate
     * @param input0 The first socket that will be connected as input
     * @param input1 The second socket that will be connected as input
     * @param outputId The id of the gate's output socket
     * @return The output socket
     */
    private BLXSocket configureBinaryGate(BLXGate gate, BLXSocket input0, BLXSocket input1, String outputId) {
        linkOutputSocketToGateInput(input0, 0, gate);
        linkOutputSocketToGateInput(input1, 1, gate);

        BLXSocket gateOutputSocket = claimOutputSocket(outputId);
        gate.setOutputSocket(0, gateOutputSocket);
        return gateOutputSocket;
    }

    /**
     * Connect an output socket with a gate's input socket
     * @param outputSocket The output socket to connect (source)
     * @param inputIndex The index of the gate's input socket (destination)
     * @param gate The gate to which the output socket is being connected
     */
    private void linkOutputSocketToGateInput(BLXSocket outputSocket, int inputIndex, BLXGate gate) {
        BLXSocket inputSocket = new BLXSocket(outputSocket.getId(), outputSocket.getValue());
        outputSocket.addTarget(inputSocket);
        gate.setInputSocket(inputIndex, inputSocket);
    }

    /**
     * Claim an output socket.  This method is invoked when the source of a socket has been identified.
     * @param outputId The id of the output socket being claimed
     * @return The claimed socket
     * @throws DuplicateIdException if the output socket has already been claimed
     */
    private BLXSocket claimOutputSocket(String outputId) {
        BLXSocket outputSocket;
        if (claimedOutputSockets.containsKey(outputId))
            throw new DuplicateIdException(outputId);
        else if (unclaimedOutputSockets.containsKey(outputId))
            outputSocket = unclaimedOutputSockets.remove(outputId);
        else
            outputSocket = new BLXSocket(outputId, defaultValue);
        claimedOutputSockets.put(outputId, outputSocket);
        return outputSocket;
    }
}