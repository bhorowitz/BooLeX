package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;

/**
 * Created by dani on 2/10/14.
 */
public class BLXNotGate extends BLXGate {

    public BLXNotGate(Boolean defaultValue) {
        setInputSocket(0, defaultValue);
        setOutputSocket(0, defaultValue);
    }

    @Override
    protected BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket) {
        // If something went wrong, return unknown value signal
        if (incomingSignal.getValue() == null)
            return incomingSignal.propagate(outputSocket, null, 1);
        else
            return incomingSignal.propagate(outputSocket, !incomingSignal.getValue(), 1);
    }
}
