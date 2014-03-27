package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignalReceiver;
import boolex.logic.elements.standard.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static boolex.helpers.ListHelper.exclude;

/**
 * Created by ajr64 on 3/26/14.
 */
public class BLXCircuitBuilder {
    public class UnchainableCircuitsException extends RuntimeException {
        public UnchainableCircuitsException(int numOutputs, int numInputs) {
            super("attempted to chain a circuit with " + numOutputs + " outputs " +
                  "to a circuit with " + numInputs + " inputs");
        }
    }

    private Boolean defaultValue;

    public BLXCircuitBuilder(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BLXCircuit buildCircuit(BLXCircuit startCircuit, BLXCircuit endCircuit) {
        List<BLXSocket> inputSockets  = startCircuit.getInputSockets();
        List<BLXSocket> outputSockets = endCircuit.getOutputSockets();
        Set<BLXSignalReceiver> trueTargets = startCircuit.getTrueSocket().getTargets();
        Set<BLXSignalReceiver> falseTargets = startCircuit.getFalseSocket().getTargets();
        trueTargets.addAll(endCircuit.getTrueSocket().getTargets());
        falseTargets.addAll(endCircuit.getFalseSocket().getTargets());
        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }

    public BLXCircuit not(BLXCircuit input) {
        BLXNotGate gate = new BLXNotGate(defaultValue);
        return connectToGate(input, gate);
    }

    public BLXCircuit buffer(BLXCircuit input) {
        BLXBuffer buffer = new BLXBuffer(defaultValue);
        return connectToGate(input, buffer);
    }

    public BLXCircuit and(BLXCircuit input0, BLXCircuit input1) {
        BLXAndGate gate = new BLXAndGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    public BLXCircuit or(BLXCircuit input0, BLXCircuit input1) {
        BLXOrGate gate = new BLXOrGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    public BLXCircuit xor(BLXCircuit input0, BLXCircuit input1) {
        BLXXorGate gate = new BLXXorGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    public BLXCircuit nand(BLXCircuit input0, BLXCircuit input1) {
        BLXNandGate gate = new BLXNandGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    public BLXCircuit nor(BLXCircuit input0, BLXCircuit input1) {
        BLXNorGate gate = new BLXNorGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

    public BLXCircuit xnor(BLXCircuit input0, BLXCircuit input1) {
        BLXXnorGate gate = new BLXXnorGate(defaultValue);
        return connectToGate(input0, input1, gate);
    }

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

    public BLXCircuit chain(BLXCircuit source, BLXCircuit destination) {
        if (source.getOutputSockets().size() != destination.getInputSockets().size()) {
            throw new UnchainableCircuitsException(source.getOutputSockets().size(),
                                                   destination.getInputSockets().size());
        }
        for (int i = 0; i < source.getOutputSockets().size(); i++) {
            source.getOutputSockets().get(i).addTarget(destination.getInputSockets().get(i));
        }
        List<BLXSocket> inputSockets  = source.getInputSockets();
        List<BLXSocket> outputSockets = destination.getOutputSockets();
        Set<BLXSignalReceiver> trueTargets = new HashSet<>();
        Set<BLXSignalReceiver> falseTargets = new HashSet<>();

        trueTargets.addAll(source.getTrueSocket().getTargets());
        trueTargets.addAll(destination.getTrueSocket().getTargets());
        falseTargets.addAll(source.getFalseSocket().getTargets());
        falseTargets.addAll(destination.getFalseSocket().getTargets());

        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }

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

        Set<BLXSignalReceiver> trueTargets  = input0.getTrueSocket().getTargets();
        Set<BLXSignalReceiver> falseTargets = input0.getFalseSocket().getTargets();
        trueTargets.addAll(input1.getTrueSocket().getTargets());
        falseTargets.addAll(input1.getFalseSocket().getTargets());

        return new BLXCircuit(inputSockets, outputSockets, trueTargets, falseTargets);
    }


}
