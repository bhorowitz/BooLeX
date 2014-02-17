/**
 * Created by dani on 2/10/14.
 */

package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.ArrayList;

public abstract class BLXGate implements BLXSignalReceiver {
    private ArrayList<BLXSocket> inputSockets;
    private ArrayList<BLXSocket> outputSockets;

    public BLXGate() {
        inputSockets = new ArrayList<>();
        outputSockets = new ArrayList<>();
    }

    public BLXSocket getInputSocket(int index) {
        if (index < 0 || index >= inputSockets.size())
            return null;
        else
            return inputSockets.get(index);
    }

    public BLXSocket getOutputSocket(int index) {
        if (index < 0 || index >= inputSockets.size())
            return null;
        else
            return outputSockets.get(index);
    }

    public void setInputSocket(int index, Boolean defaultValue) {
        if (index >= 0) {
            BLXSocket socket = new BLXSocket(defaultValue);
            socket.addTarget(this);
            if (index >= inputSockets.size())
                inputSockets.add(index, socket);
            else
                inputSockets.set(index, socket);
        }

    }

    public void setOutputSocket(int index, Boolean defaultValue) {
        if (index >= 0) {
            BLXSocket socket = new BLXSocket(defaultValue);
            if (index >= outputSockets.size())
                outputSockets.add(index, socket);
            else
                outputSockets.set(index, socket);
        }
    }

    public void connectTo(BLXGate gate, int fromIndex, int toIndex) {
        if (gate != null) {
            BLXSocket inputSocket = gate.getInputSocket(toIndex);
            if (inputSocket == null) {
                gate.setInputSocket(toIndex, null);
                inputSocket = gate.getInputSocket(toIndex);
            }
            BLXSocket outputSocket = getOutputSocket(fromIndex);
            if (outputSocket == null) {
                setOutputSocket(fromIndex, null);
                outputSocket = getOutputSocket(fromIndex);
            }
            outputSocket.addTarget(inputSocket);
        }
    }

    @Override
    public void receive(BLXSignal signal, BLXSignalQueue queue) {
        if (queue != null) {
            for (int i = 0; i < outputSockets.size(); i++) {
                queue.add(computeSignalForOutputSocket(signal, getOutputSocket(i)));
            }
        }
    }

    protected abstract BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket socket);
}
