package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.ArrayList;

/**
 * The BLXGate class is used to simulate a logic gate in a circuit.
 * It has a set of input and output sockets.  Every time an input
 * socket receives a signal, values for all output sockets are
 * recomputed and propagated.
 *
 * @author Dani Dickstein
 */
public abstract class BLXGate implements BLXSignalReceiver {
    private ArrayList<BLXSocket> inputSockets;
    private ArrayList<BLXSocket> outputSockets;

    /**
     * Constructor for a BLXGate.
     */
    public BLXGate() {
        inputSockets = new ArrayList<>();
        outputSockets = new ArrayList<>();
    }

    /**
     * Get the input socket of this gate at a certain index
     * @param index The index of the input socket
     * @return The input socket at the specified index
     */
    public BLXSocket getInputSocket(int index) {
        if (index < 0 || index >= inputSockets.size())
            return null;
        else
            return inputSockets.get(index);
    }

    /**
     * Get the output socket of this gate at a certain index
     * @param index The index of the output socket
     * @return The output socket at the specified index
     */
    public BLXSocket getOutputSocket(int index) {
        if (index < 0 || index >= outputSockets.size())
            return null;
        else
            return outputSockets.get(index);
    }

    /**
     * Set the input socket of this gate at a certain index to a given value
     * @param index The index of the socket
     * @param value The new value for the socket
     */
    public void setInputSocket(int index, Boolean value) {
        setInputSocket(index, null, value);
    }

    /**
     * Set the input socket of this gate at a certain index to a certain value
     * @param index The index of the socket
     * @param id The socket id
     * @param value The new value for the socket
     */
    public void setInputSocket(int index, String id, Boolean value) {
        if (index >= 0) {
            BLXSocket socket = new BLXSocket(id, value);
            setInputSocket(index,socket);
        }
    }

    /**
     * Set a new input socket for this gate at a given index
     * @param index The index at which to set the new socket
     * @param socket The socket to set
     */
    public void setInputSocket(int index, BLXSocket socket) {
        if (socket != null && index >= 0) {
            socket.addTarget(this);
            if (index >= inputSockets.size())
                inputSockets.add(index, socket);
            else
                inputSockets.set(index, socket);
        }
    }

    /**
     * Set the output socket of this gate at a certain index to a certain value
     * @param index The index of the socket
     * @param value The new value for the socket
     */
    public void setOutputSocket(int index, Boolean value) {
        setOutputSocket(index, null, value);
    }

    /**
     * Set the output socket of this gate at a certain index to a certain value
     * @param index The index of the socket
     * @param id The socket id
     * @param value The new value for the socket
     */
    public void setOutputSocket(int index, String id, Boolean value) {
        if (index >= 0) {
            BLXSocket socket = new BLXSocket(id, value);
            setOutputSocket(index, socket);
        }
    }

    /**
     * Set a new output socket for this gate at a given index
     * @param index The index at which to set the new socket
     * @param socket The socket to set
     */
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

    /**
     * Determine the value to propagate to a given output socket of this gate
     * @param incomingSignal The incoming signal to this gate
     * @param outputSocket The socket to which the signal is propagated
     * @return The new propagated signal
     */
    protected abstract BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket);
}
