package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;

/**
 * Created by dani on 2/17/14.
 */
public class BLXBuffer extends BLXGate {

    public BLXBuffer(Boolean defaultValue) {
        setInputSocket(0, defaultValue);
        setOutputSocket(0, defaultValue);
    }

    @Override
    protected BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket) {
        return incomingSignal.propagate(outputSocket, incomingSignal.getValue(), 1);
    }
}
