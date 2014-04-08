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
    private boolean heisenberg;

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
        if (index < 0 || index >= outputSockets.size())
            return null;
        else
            return outputSockets.get(index);
    }

    public void setInputSocket(int index, Boolean value) {
        setInputSocket(index, null, value);
    }
      
    public void setInputSocket(int index, String id, Boolean value) {
        if (index >= 0) {
            BLXSocket socket = new BLXSocket(id, value);
            setInputSocket(index,socket);
        }
    }

    public void setInputSocket(int index, BLXSocket socket) {
        if (socket != null && index >= 0) {
            socket.addTarget(this);
            if (index >= inputSockets.size())
                inputSockets.add(index, socket);
            else
                inputSockets.set(index, socket);
        }
    }

    public void setOutputSocket(int index, Boolean value) {
        setOutputSocket(index, null, value);
    }

    public void setOutputSocket(int index, String id, Boolean value) {
        if (index >= 0) {
            BLXSocket socket = new BLXSocket(id, value);
            setOutputSocket(index, socket);
        }
    }

    public void setOutputSocket(int index, BLXSocket socket) {
        if (socket != null && index >= 0) {
            if (index >= outputSockets.size())
                outputSockets.add(index, socket);
            else
                outputSockets.set(index, socket);
        }
    }

    @Override
    public void receive(BLXSignal signal, BLXSignalQueue queue) {
        if (queue != null && signal != null) {
            for (int i = 0; i < outputSockets.size(); i++) {
                queue.signal(computeSignalForOutputSocket(signal, getOutputSocket(i)));
            }
        }
    }

    protected abstract BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket);
}
