/**
 * Created by dani on 2/10/14.
 */

package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalActionFactory;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.ArrayList;

public abstract class BLXGate implements BLXSignalReceiver {
    private ArrayList<BLXSocket> inputSockets;
    private ArrayList<BLXSocket> outputSockets;
    private BLXTruthTable table;

    public BLXGate(BLXTruthTable table) {
        inputSockets = new ArrayList<>();
        outputSockets = new ArrayList<>();
        this.table = table;
    }

//    public int numInputSockets() {
//        return inputSockets.size();
//    }
//
//    public int numOutputSockets() {
//        return outputSockets.size();
//    }
//
//    public BLXSocket getInputSocket(int index) {
//        return inputSockets.get(index);
//    }
//
//    public BLXSocket getOutputSocket(int index) {
//        return outputSockets.get(index);
//    }
//
//    public void setInputSocket(int index, Boolean defaultValue) {
//        inputSockets.set(index, new BLXSocket(defaultValue));
//    }
//
//    public void setOutputSocket(int index, Boolean defaultValue) {
//        outputSockets.set(index, new BLXSocket(defaultValue));
//    }

    public void connectTo(BLXGate gate, int fromIndex, int toIndex) {
        BLXSocket outputSocket = outputSockets.get(fromIndex);
        gate.inputSockets.set(toIndex, outputSocket);
    }

    private boolean isOperational() {
        return table != null
            && table.getNumInputs()  == inputSockets.size()
            && table.getNumOutputs() == outputSockets.size();
    }

    @Override
    public void signal(BLXSignal signal, BLXSignalQueue queue) {
        if (isOperational()) {

        }





        BLXSignalActionFactory.getSocketAction(signal).performSocketAction(this);
        if (queue == null) {
            signal(signal);
        }
        else {
            for (BLXSignalReceiver target : targets) {
                queue.add(signal.propagate(target,0));
            }
        }
    }















    public void signal(BLXSocket socket) {
        signal(socket, false);
    }

    public void signal(BLXSocket socket, boolean test) {
        assert(inputSockets.contains(socket));
        if (inputSockets.contains(socket)) {
            Boolean[] inputValues = test ? getInputValues() : getTestInputValues();
            Boolean[] outputValues = evaluate(inputValues);
            for (int i = 0; i < outputValues.length; i++) {
                outputSockets.get(i).signal(outputValues[i],test);
            }
        }
    }

    public Boolean[] getInputValues() {
        Boolean[] values = new Boolean[inputSockets.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = inputSockets.get(i).getValue();
        }
        return values;
    }

    public Boolean[] getTestInputValues() {
        Boolean[] testValues = new Boolean[inputSockets.size()];
        for (int i = 0; i < testValues.length; i++) {
            testValues[i] = inputSockets.get(i).getTestValue();
        }
        return testValues;
    }

    public abstract Boolean[] evaluate(Boolean[] inputValues);
}
