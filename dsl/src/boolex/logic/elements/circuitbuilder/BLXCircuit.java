package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXConstantSocket;
import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.standard.*;

import java.util.*;

import static boolex.logic.elements.helpers.PrettyPrintHelper.arrayToString;

/**
 * Created by dani on 3/13/14.
 */
public class BLXCircuit {
    public static class DuplicateIdException extends RuntimeException {
        public DuplicateIdException(String id) {
            super("two different output sockets were assigned id "+id);
        }
    }

    public static class MissingIdException extends RuntimeException {
        public MissingIdException() {
            super("received null id as input to gate");
        }
    }

    public static class MissingIntegratedCircuitException extends RuntimeException {
        public MissingIntegratedCircuitException() {
            super("received null integrated circuit as input to a gate");
        }
    }

    public static class UnresolvedCircuitInputsException extends Exception {
        public UnresolvedCircuitInputsException(Set<String> unresolvedInputs) {
            super("your circuit expects inputs " + arrayToString(
                    unresolvedInputs.toArray(new String[unresolvedInputs.size()])
            ) + " but will never receive them");
        }
    }

    private Map<String,BLXSocket> unclaimedOutputSockets;
    private Map<String,BLXSocket> claimedOutputSockets;
    private BLXSocket currentOutputSocket;
    private Boolean defaultValue;

    private final BLXConstantSocket trueSocket = new BLXConstantSocket(true);
    private final BLXConstantSocket falseSocket = new BLXConstantSocket(false);

    public BLXCircuit(String firstInputId, boolean initializeToFalse) {
        unclaimedOutputSockets = new HashMap<>();
        claimedOutputSockets = new HashMap<>();
        defaultValue = initializeToFalse ? false : null;
        load(firstInputId);
    }

    public BLXCircuit load(String socketId) {
        if (socketId == null)
            throw new MissingIdException();
        currentOutputSocket = getOrCreateSocket(this,socketId);
        return this;
    }

    public Set<String> getUnresolvedInputs() {
        return unclaimedOutputSockets.keySet();
    }

    public BLXCircuit not(String outputId) {
        if (outputId != null) {
            currentOutputSocket = configureUnaryGate(new BLXNotGate(), currentOutputSocket, this, outputId);
        }
        return this;
    }

    public BLXCircuit not(BLXCircuit targetCircuit, String targetId) throws UnresolvedCircuitInputsException {
        if (targetCircuit != null && targetId != null) {
            configureUnaryGate(new BLXNotGate(), currentOutputSocket, targetCircuit, targetId);
            if (!targetCircuit.getUnresolvedInputs().isEmpty())
                throw new UnresolvedCircuitInputsException(targetCircuit.getUnresolvedInputs());
            currentOutputSocket = targetCircuit.currentOutputSocket;
        }
        return this;
    }

    public BLXCircuit and(String secondInputId, String outputId) {
        return binaryJoin(new BLXAndGate(), secondInputId, outputId);
    }

    public BLXCircuit and(boolean constValue, String outputId) {
        return binaryJoin(new BLXAndGate(), constValue, outputId);
    }

    public BLXCircuit and(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXAndGate(), inputCircuit, outputId);
    }

    public BLXCircuit and(String secondInputId, BLXCircuit targetCircuit, String targetId)
                          throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXAndGate(), secondInputId, targetCircuit, targetId);
    }

    public BLXCircuit and(boolean constValue, BLXCircuit targetCircuit, String targetId)
                          throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXAndGate(), constValue, targetCircuit, targetId);
    }

    public BLXCircuit and(BLXCircuit inputCircuit, BLXCircuit targetCircuit, String targetId)
                          throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXAndGate(), inputCircuit, targetCircuit, targetId);
    }

    public BLXCircuit or(String secondInputId, String outputId) {
        return binaryJoin(new BLXOrGate(), secondInputId, outputId);
    }

    public BLXCircuit or(boolean constValue, String outputId) {
        return binaryJoin(new BLXOrGate(), constValue, outputId);
    }

    public BLXCircuit or(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXOrGate(), inputCircuit, outputId);
    }

    public BLXCircuit or(String secondInputId, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXOrGate(), secondInputId, targetCircuit, targetId);
    }

    public BLXCircuit or(boolean constValue, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXOrGate(), constValue, targetCircuit, targetId);
    }

    public BLXCircuit or(BLXCircuit inputCircuit, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXOrGate(), inputCircuit, targetCircuit, targetId);
    }

    public BLXCircuit xor(String secondInputId, String outputId) {
        return binaryJoin(new BLXXorGate(), secondInputId, outputId);
    }

    public BLXCircuit xor(boolean constValue, String outputId) {
        return binaryJoin(new BLXXorGate(), constValue, outputId);
    }

    public BLXCircuit xor(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXXorGate(), inputCircuit, outputId);
    }

    public BLXCircuit xor(String secondInputId, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXXorGate(), secondInputId, targetCircuit, targetId);
    }

    public BLXCircuit xor(boolean constValue, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXXorGate(), constValue, targetCircuit, targetId);
    }

    public BLXCircuit xor(BLXCircuit inputCircuit, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXXorGate(), inputCircuit, targetCircuit, targetId);
    }

    public BLXCircuit nand(String secondInputId, String outputId) {
        return binaryJoin(new BLXNandGate(), secondInputId, outputId);
    }

    public BLXCircuit nand(boolean constValue, String outputId) {
        return binaryJoin(new BLXNandGate(), constValue, outputId);
    }

    public BLXCircuit nand(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXNandGate(), inputCircuit, outputId);
    }

    public BLXCircuit nand(String secondInputId, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXNandGate(), secondInputId, targetCircuit, targetId);
    }

    public BLXCircuit nand(boolean constValue, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXNandGate(), constValue, targetCircuit, targetId);
    }

    public BLXCircuit nand(BLXCircuit inputCircuit, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXNandGate(), inputCircuit, targetCircuit, targetId);
    }

    public BLXCircuit nor(String secondInputId, String outputId) {
        return binaryJoin(new BLXNorGate(), secondInputId, outputId);
    }

    public BLXCircuit nor(boolean constValue, String outputId) {
        return binaryJoin(new BLXNorGate(), constValue, outputId);
    }

    public BLXCircuit nor(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXNorGate(), inputCircuit, outputId);
    }

    public BLXCircuit nor(String secondInputId, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXNorGate(), secondInputId, targetCircuit, targetId);
    }

    public BLXCircuit nor(boolean constValue, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXNorGate(), constValue, targetCircuit, targetId);
    }

    public BLXCircuit nor(BLXCircuit inputCircuit, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXNorGate(), inputCircuit, targetCircuit, targetId);
    }

    public BLXCircuit xnor(String secondInputId, String outputId) {
        return binaryJoin(new BLXXnorGate(), secondInputId, outputId);
    }

    public BLXCircuit xnor(boolean constValue, String outputId) {
        return binaryJoin(new BLXXnorGate(), constValue, outputId);
    }

    public BLXCircuit xnor(BLXCircuit inputCircuit, String outputId) {
        return binaryJoin(new BLXXnorGate(), inputCircuit, outputId);
    }

    public BLXCircuit xnor(String secondInputId, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXXnorGate(), secondInputId, targetCircuit, targetId);
    }

    public BLXCircuit xnor(boolean constValue, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXXnorGate(), constValue, targetCircuit, targetId);
    }

    public BLXCircuit xnor(BLXCircuit inputCircuit, BLXCircuit targetCircuit, String targetId)
            throws UnresolvedCircuitInputsException {
        return binaryJoin(new BLXXnorGate(), inputCircuit, targetCircuit, targetId);
    }

    //-----------------------------------------------------------------------------------
    // Private Methods Begin Here (defines the core behavior of the binary operators)
    //-----------------------------------------------------------------------------------

    private BLXCircuit binaryJoin(BLXGate gate, String secondInputId, String outputId) {
        if (secondInputId == null)
            throw new MissingIdException();
        return binaryJoin(gate, getOrCreateSocket(this, secondInputId), outputId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, boolean constValue, String outputId) {
        return binaryJoin(gate, constValue ? trueSocket : falseSocket, outputId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, BLXCircuit inputCircuit, String outputId) {
        if (inputCircuit == null)
            throw new MissingIntegratedCircuitException();
        return binaryJoin(gate, inputCircuit.currentOutputSocket, outputId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, BLXSocket secondOutputSocket, String outputId) {
        if (outputId != null)
            currentOutputSocket = configureBinaryGate(gate, currentOutputSocket, secondOutputSocket, this, outputId);
        return this;
    }

    private BLXCircuit binaryJoin(BLXGate gate, String secondInputId, BLXCircuit targetCircuit, String targetId)
                                  throws UnresolvedCircuitInputsException {
        if (secondInputId == null)
            throw new MissingIdException();
        return binaryJoin(gate, getOrCreateSocket(this, secondInputId), targetCircuit, targetId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, boolean constValue, BLXCircuit targetCircuit, String targetId)
                                  throws UnresolvedCircuitInputsException {
        return binaryJoin(gate, constValue ? trueSocket : falseSocket, targetCircuit, targetId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, BLXCircuit inputCircuit, BLXCircuit targetCircuit, String targetId)
                                  throws UnresolvedCircuitInputsException {
        if (inputCircuit == null)
            throw new MissingIntegratedCircuitException();
        return binaryJoin(gate, inputCircuit.currentOutputSocket, targetCircuit, targetId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, BLXSocket secondOutputSocket, BLXCircuit targetCircuit,
                                  String targetId) throws UnresolvedCircuitInputsException {
        if (targetCircuit != null && targetId != null) {
            configureBinaryGate(gate, currentOutputSocket, secondOutputSocket, targetCircuit, targetId);
            if (!targetCircuit.getUnresolvedInputs().isEmpty())
                throw new UnresolvedCircuitInputsException(targetCircuit.getUnresolvedInputs());
            currentOutputSocket = targetCircuit.currentOutputSocket;
        }
        return this;
    }

    private static BLXSocket getOrCreateSocket(BLXCircuit circuit, String socketId) {
        BLXSocket socket;
        if (circuit.claimedOutputSockets.containsKey(socketId))
            socket = circuit.claimedOutputSockets.get(socketId);
        else if (circuit.unclaimedOutputSockets.containsKey(socketId))
            socket = circuit.unclaimedOutputSockets.get(socketId);
        else {
            socket = new BLXSocket(socketId, circuit.defaultValue);
            circuit.unclaimedOutputSockets.put(socketId,socket);
        }
        return socket;
    }

    private static BLXSocket configureUnaryGate(BLXGate gate, BLXSocket input,
                                                BLXCircuit outputSource, String outputId) {
        linkOutputSocketToGateInput(input, 0, gate);
        BLXSocket gateOutputSocket = claimOutputSocket(outputSource, outputId);
        gate.setOutputSocket(0, gateOutputSocket);
        return gateOutputSocket;
    }

    private static BLXSocket configureBinaryGate(BLXGate gate, BLXSocket input0, BLXSocket input1,
                                                 BLXCircuit outputSource, String outputId) {
        linkOutputSocketToGateInput(input0, 0, gate);
        linkOutputSocketToGateInput(input1, 1, gate);

        BLXSocket gateOutputSocket = claimOutputSocket(outputSource, outputId);
        gate.setOutputSocket(0, gateOutputSocket);
        return gateOutputSocket;
    }

    private static void linkOutputSocketToGateInput(BLXSocket outputSocket, int inputIndex, BLXGate gate) {
        BLXSocket inputSocket = new BLXSocket(outputSocket.getId(), outputSocket.getValue());
        outputSocket.addTarget(inputSocket);
        gate.setInputSocket(inputIndex, inputSocket);
    }

    private static BLXSocket claimOutputSocket(BLXCircuit circuit, String outputId) {
        BLXSocket outputSocket;
        if (circuit.claimedOutputSockets.containsKey(outputId))
            throw new DuplicateIdException(outputId);
        else if (circuit.unclaimedOutputSockets.containsKey(outputId))
            outputSocket = circuit.unclaimedOutputSockets.remove(outputId);
        else
            outputSocket = new BLXSocket(outputId, circuit.defaultValue);
        circuit.claimedOutputSockets.put(outputId, outputSocket);
        return outputSocket;
    }
}