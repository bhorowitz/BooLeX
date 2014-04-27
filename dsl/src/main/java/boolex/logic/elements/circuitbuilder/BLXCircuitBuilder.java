package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignalReceiver;
import boolex.logic.elements.standard.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A BLXCircuitBuilder is a helper class for assembling BLXCircuits.  As the parse
 * tree nodes are visited, the BLXCircuitBuilder assembles the circuit by connecting
 * the circuits returned from the branches.  There are multiple connection methods,
 * such as gate connections, parallel circuit merge, and series circuit merge.
 *
 * @author alex and dani
 */
public class BLXCircuitBuilder {
    /**
     * UnchainableCircuitsException is raised when two circuits cannot be chained
     * together due to a discrepancy in input/output socket numbers
     */
    public class UnchainableCircuitsException extends RuntimeException {
        public UnchainableCircuitsException(int numOutputs, int numInputs) {
            super("attempted to chain a circuit with " + numOutputs + " outputs " +
                    "to a circuit with " + numInputs + " inputs");
        }
    }

    private Boolean defaultValue;

    /**
     * Constructor for BLXCircuitBuilder
     * @param defaultValue The default value for sockets
     */
    public BLXCircuitBuilder(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Construct a black-boxed circuit around two different circuits, where inputs
     * are given by one circuit and outputs are given by another.
     * @param startCircuit The circuit whose inputs will be used as inputs in the merged circuit
     * @param endCircuit The circuit whose outputs will be used as outputs in the merged circuit
     * @return The merged circuit
     */
    public BLXCircuit buildCircuit(BLXCircuit startCircuit, BLXCircuit endCircuit) {
        List<BLXSocket> inputSockets = startCircuit.getInputSockets();
        List<BLXSocket> outputSockets = endCircuit.getOutputSockets();
        Set<BLXSignalReceiver> trueTargets = startCircuit.getTrueSocket().getTargets();
        Set<BLXSignalReceiver> falseTargets = startCircuit.getFalseSocket().getTargets();
        trueTargets.addAll(endCircuit.getTrueSocket().getTargets());
        falseTargets.addAll(endCircuit.getFalseSocket().getTargets());
        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }

    /**
     * Connect the circuit to a not gate
     * @param input The circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit not(BLXCircuit input) {
        BLXNotGate gate = new BLXNotGate(defaultValue);
        return connectToGate(input, gate);
    }

    /**
     * Connect the circuit to a buffer
     * @param input The circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit buffer(BLXCircuit input) {
        BLXBuffer buffer = new BLXBuffer(defaultValue);
        return connectToGate(input, buffer);
    }

    /**
     * Connect two circuits to an and gate
     * @param input0 The first circuit to connect
     * @param input1 The second circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit and(BLXCircuit input0, BLXCircuit input1) {
        BLXAndGate gate = new BLXAndGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    /**
     * Connect two circuits to an or gate
     * @param input0 The first circuit to connect
     * @param input1 The second circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit or(BLXCircuit input0, BLXCircuit input1) {
        BLXOrGate gate = new BLXOrGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    /**
     * Connect two circuits to an xor gate
     * @param input0 The first circuit to connect
     * @param input1 The second circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit xor(BLXCircuit input0, BLXCircuit input1) {
        BLXXorGate gate = new BLXXorGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    /**
     * Connect two circuits to a nand gate
     * @param input0 The first circuit to connect
     * @param input1 The second circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit nand(BLXCircuit input0, BLXCircuit input1) {
        BLXNandGate gate = new BLXNandGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    /**
     * Connect two circuits to a nor gate
     * @param input0 The first circuit to connect
     * @param input1 The second circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit nor(BLXCircuit input0, BLXCircuit input1) {
        BLXNorGate gate = new BLXNorGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    /**
     * Connect two circuits to an xnor gate
     * @param input0 The first circuit to connect
     * @param input1 The second circuit to connect
     * @return The resulting circuit
     */
    public BLXCircuit xnor(BLXCircuit input0, BLXCircuit input1) {
        BLXXnorGate gate = new BLXXnorGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    /**
     * Merge a set of parallel circuits together into one circuit
     * @param circuits The circuits to merge
     * @return The merged circuit
     */
    public BLXCircuit merge(List<BLXCircuit> circuits) {
        List<BLXSocket> inputSockets = new ArrayList<>();
        List<BLXSocket> outputSockets = new ArrayList<>();
        Set<BLXSignalReceiver> trueTargets = new HashSet<>();
        Set<BLXSignalReceiver> falseTargets = new HashSet<>();

        for (BLXCircuit circuit : circuits) {
            inputSockets.addAll(circuit.getInputSockets());
            outputSockets.addAll(circuit.getOutputSockets());
            trueTargets.addAll(circuit.getTrueSocket().getTargets());
            falseTargets.addAll(circuit.getFalseSocket().getTargets());
        }

        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }

    /**
     * Chain two circuits into a series circuit
     * @param source The source circuit
     * @param destination The destination circuit
     * @return The chaind circuit
     */
    public BLXCircuit chain(BLXCircuit source, BLXCircuit destination) {
        if (source.getOutputSockets().size() != destination.getInputSockets().size()) {
            throw new UnchainableCircuitsException(source.getOutputSockets().size(),
                    destination.getInputSockets().size());
        }
        for (int i = 0; i < source.getOutputSockets().size(); i++) {
            source.getOutputSockets().get(i).addTarget(destination.getInputSockets().get(i));
        }
        List<BLXSocket> inputSockets = source.getInputSockets();
        List<BLXSocket> outputSockets = destination.getOutputSockets();
        Set<BLXSignalReceiver> trueTargets = new HashSet<>();
        Set<BLXSignalReceiver> falseTargets = new HashSet<>();

        trueTargets.addAll(source.getTrueSocket().getTargets());
        trueTargets.addAll(destination.getTrueSocket().getTargets());
        falseTargets.addAll(source.getFalseSocket().getTargets());
        falseTargets.addAll(destination.getFalseSocket().getTargets());

        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }

    /**
     * Connect a circuit to a unary gate
     * @param input The circuit to connect
     * @param gate The gate
     * @return The connected circuit
     */
    private BLXCircuit connectToGate(BLXCircuit input, BLXGate gate) {
        List<BLXSocket> inputSockets = input.getInputSockets();
        BLXSocket sourceSocket = input.getOutputSockets().get(0);
        List<BLXSocket> outputSockets = new ArrayList<>();

        BLXSocket inputSocket = new BLXSocket(sourceSocket.getId(), sourceSocket.getValue());
        sourceSocket.addTarget(inputSocket);
        gate.setInputSocket(0, inputSocket);
        outputSockets.add(gate.getOutputSocket(0));

        Set<BLXSignalReceiver> trueTargets = input.getTrueSocket().getTargets();
        Set<BLXSignalReceiver> falseTargets = input.getFalseSocket().getTargets();

        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }

    /**
     * Connect two circuits to a binary gate
     * @param input0 The first circuit to connect
     * @param input1 The second circuit to connect
     * @param gate The gate
     * @return The connected circuit
     */
    private BLXCircuit connectToGate(BLXCircuit input0, BLXCircuit input1, BLXGate gate) {
        List<BLXSocket> inputSockets = input0.getInputSockets();
        BLXSocket sourceSocket0 = input0.getOutputSockets().get(0);
        List<BLXSocket> outputSockets = new ArrayList<>();

        inputSockets.addAll(input1.getInputSockets());
        BLXSocket sourceSocket1 = input1.getOutputSockets().get(0);

        BLXSocket inputSocket0 = new BLXSocket(sourceSocket0.getId(), sourceSocket0.getValue());
        BLXSocket inputSocket1 = new BLXSocket(sourceSocket1.getId(), sourceSocket1.getValue());
        sourceSocket0.addTarget(inputSocket0);
        sourceSocket1.addTarget(inputSocket1);
        gate.setInputSocket(0, inputSocket0);
        gate.setInputSocket(1, inputSocket1);
        outputSockets.add(gate.getOutputSocket(0));

        Set<BLXSignalReceiver> trueTargets = input0.getTrueSocket().getTargets();
        Set<BLXSignalReceiver> falseTargets = input0.getFalseSocket().getTargets();
        trueTargets.addAll(input1.getTrueSocket().getTargets());
        falseTargets.addAll(input1.getFalseSocket().getTargets());

        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }


}
