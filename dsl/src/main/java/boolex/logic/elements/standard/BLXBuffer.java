package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;

/**
 * This class is designed to simulate a standard buffer
 * @author Dani Dickstein
 */
public class BLXBuffer extends BLXGate {

    /**
     * Standard constructor for buffer
     * @param defaultValue The default value of the input socket
     */
    public BLXBuffer(Boolean defaultValue) {
        setInputSocket(0, defaultValue);
        setOutputSocket(0, defaultValue);
    }

    @Override
    protected BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket) {
        return incomingSignal.propagate(outputSocket, incomingSignal.getValue(), 1);
    }
}
