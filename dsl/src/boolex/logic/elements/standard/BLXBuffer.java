package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;

/**
 * Created by dani on 2/17/14.
 */
public class BLXBuffer extends BLXGate {

    public BLXBuffer() {
        super();
        setInputSocket(0, (Boolean)null);
        setOutputSocket(0, (Boolean)null);
    }

    @Override
    protected BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket) {
        return incomingSignal.propagate(outputSocket, incomingSignal.getValue(), 1);
    }
}
