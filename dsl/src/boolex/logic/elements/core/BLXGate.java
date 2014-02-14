/**
 * Created by dani on 2/10/14.
 */

package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignalable;

import java.util.ArrayList;

public abstract class BLXGate implements BLXSignalable {
    private ArrayList<BLXSocket> inputSockets;
    private ArrayList<BLXSocket> outputSockets;

    public BLXGate(int numInputs, int numOutputs) {
        inputSockets = new ArrayList<>();
        for (int i = 0; i < numInputs; i++) {
            setInputSocket(i,new BLXSocket());
        }
        outputSockets = new ArrayList<>();
        for (int i = 0; i < numOutputs; i++) {
            setOutputSocket(i,new BLXSocket());
        }
    }

    public BLXSocket getInputSocket(int index) {
        return inputSockets.get(index);
    }

    public BLXSocket getOutputSocket(int index) {
        return outputSockets.get(index);
    }

    public int numInputSockets() {
        return inputSockets.size();
    }

    public int numOutputSockets() {
        return outputSockets.size();
    }

    public void setInputSocket(int index, BLXSocket socket) {
        inputSockets.set(index,socket);
        socket.setOutputGate(this);
    }

    public void setOutputSocket(int index, BLXSocket socket) {
        outputSockets.set(index,socket);
        socket.setInputGate(this);
    }

    public void connectTo(BLXGate gate, int fromIndex, int toIndex) {
        BLXSocket outputSocket = getOutputSocket(fromIndex);
        gate.setInputSocket(toIndex,outputSocket);
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
