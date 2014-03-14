package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXConstantSocket;
import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.standard.*;

import java.util.*;

/**
 * Created by dani on 3/13/14.
 */
public class BLXCircuit {
    class DuplicateIdException extends RuntimeException {
        public DuplicateIdException(String id) {
            super("two different output sockets were assigned id "+id);
        }
    }

    class MissingIdException extends RuntimeException {
        public MissingIdException() {
            super("received null id as input to gate");
        }
    }

    class MissingIntegratedCircuitException extends RuntimeException {
        public MissingIntegratedCircuitException() {
            super("received null integrated circuit as input to a gate");
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

    public BLXCircuit load(String id) {
        if (id == null)
            throw new MissingIdException();
        else if (claimedOutputSockets.containsKey(id)) {
            currentOutputSocket = claimedOutputSockets.get(id);
        }
        else if (unclaimedOutputSockets.containsKey(id)) {
            currentOutputSocket = unclaimedOutputSockets.get(id);
        }
        else {
            currentOutputSocket = new BLXSocket(id, defaultValue);
            unclaimedOutputSockets.put(id, currentOutputSocket);
        }
        return this;
    }

    public Set<String> getMissingInputs() {
        return unclaimedOutputSockets.keySet();
    }

    public BLXCircuit not(String outputId) {
        if (outputId == null)
            return this;
        BLXNotGate gate = new BLXNotGate();
        linkOutputSocketToGateInput(currentOutputSocket, 0, gate);
        claimOutputSocket(gate, 0, outputId);
        return this;
    }

    public BLXCircuit and(String secondInputId, String outputId) {
        return binaryJoin(new BLXAndGate(), secondInputId, outputId);
    }

    public BLXCircuit and(boolean constValue, String outputId) {
        return binaryJoin(new BLXAndGate(), constValue, outputId);
    }

    public BLXCircuit and(BLXCircuit circuit, String outputId) {
        return binaryJoin(new BLXAndGate(), circuit, outputId);
    }

    public BLXCircuit or(String secondInputId, String outputId) {
        return binaryJoin(new BLXOrGate(), secondInputId, outputId);
    }

    public BLXCircuit or(boolean constValue, String outputId) {
        return binaryJoin(new BLXOrGate(), constValue, outputId);
    }

    public BLXCircuit or(BLXCircuit circuit, String outputId) {
        return binaryJoin(new BLXOrGate(), circuit, outputId);
    }

    public BLXCircuit xor(String secondInputId, String outputId) {
        return binaryJoin(new BLXXorGate(), secondInputId, outputId);
    }

    public BLXCircuit xor(boolean constValue, String outputId) {
        return binaryJoin(new BLXXorGate(), constValue, outputId);
    }

    public BLXCircuit xor(BLXCircuit circuit, String outputId) {
        return binaryJoin(new BLXXorGate(), circuit, outputId);
    }

    public BLXCircuit nand(String secondInputId, String outputId) {
        return binaryJoin(new BLXNandGate(), secondInputId, outputId);
    }

    public BLXCircuit nand(boolean constValue, String outputId) {
        return binaryJoin(new BLXNandGate(), constValue, outputId);
    }

    public BLXCircuit nand(BLXCircuit circuit, String outputId) {
        return binaryJoin(new BLXNandGate(), circuit, outputId);
    }

    public BLXCircuit nor(String secondInputId, String outputId) {
        return binaryJoin(new BLXNorGate(), secondInputId, outputId);
    }

    public BLXCircuit nor(boolean constValue, String outputId) {
        return binaryJoin(new BLXNorGate(), constValue, outputId);
    }

    public BLXCircuit nor(BLXCircuit circuit, String outputId) {
        return binaryJoin(new BLXNorGate(), circuit, outputId);
    }

    public BLXCircuit xnor(String secondInputId, String outputId) {
        return binaryJoin(new BLXXnorGate(), secondInputId, outputId);
    }

    public BLXCircuit xnor(boolean constValue, String outputId) {
        return binaryJoin(new BLXXnorGate(), constValue, outputId);
    }

    public BLXCircuit xnor(BLXCircuit circuit, String outputId) {
        return binaryJoin(new BLXXnorGate(), circuit, outputId);
    }

    //-----------------------------------------------------------------------------------
    // Private Methods Begin Here (defines the core behavior of the binary operators)
    //-----------------------------------------------------------------------------------

    private BLXCircuit binaryJoin(BLXGate gate, String secondInputId, String outputId) {
        if (secondInputId == null)
            throw new MissingIdException();
        BLXSocket secondOutputSocket;
        if (claimedOutputSockets.containsKey(secondInputId))
            secondOutputSocket = claimedOutputSockets.get(secondInputId);
        else if (unclaimedOutputSockets.containsKey(secondInputId))
            secondOutputSocket = unclaimedOutputSockets.get(secondInputId);
        else {
            secondOutputSocket = new BLXSocket(secondInputId, defaultValue);
            unclaimedOutputSockets.put(secondInputId,secondOutputSocket);
        }
        return binaryJoin(gate, secondOutputSocket, outputId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, boolean constValue, String outputId) {
        return binaryJoin(gate, constValue ? trueSocket : falseSocket, outputId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, BLXCircuit circuit, String outputId) {
        if (circuit == null)
            throw new MissingIntegratedCircuitException();
        return binaryJoin(gate, circuit.currentOutputSocket, outputId);
    }

    private BLXCircuit binaryJoin(BLXGate gate, BLXSocket secondOutputSocket, String outputId) {
        if (outputId == null)
            return this;
        linkOutputSocketToGateInput(currentOutputSocket, 0, gate);
        linkOutputSocketToGateInput(secondOutputSocket,  1, gate);
        claimOutputSocket(gate, 0, outputId);
        return this;
    }

    private void linkOutputSocketToGateInput(BLXSocket outputSocket, int inputIndex, BLXGate gate) {
        BLXSocket inputSocket = new BLXSocket(outputSocket.getId(), outputSocket.getValue());
        outputSocket.addTarget(inputSocket);
        gate.setInputSocket(inputIndex, inputSocket);
    }

    private void claimOutputSocket(BLXGate gate, int outputIndex, String outputId) {
        BLXSocket gateOutputSocket;
        if (claimedOutputSockets.containsKey(outputId))
            throw new DuplicateIdException(outputId);
        else if (unclaimedOutputSockets.containsKey(outputId))
            gateOutputSocket = unclaimedOutputSockets.remove(outputId);
        else
            gateOutputSocket = new BLXSocket(outputId, defaultValue);

        gate.setOutputSocket(outputIndex, gateOutputSocket);
        claimedOutputSockets.put(outputId, gateOutputSocket);
        currentOutputSocket = gateOutputSocket;
    }
}