package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;

import static boolex.logic.elements.helpers.LogicHelper.isNull;

/**
 * Created by dani on 2/10/14.
 */
public class BLXNotGate extends BLXGate {

    public BLXNotGate() {
        super();
        setInputSocket(0, (Boolean)null);
        setOutputSocket(0, (Boolean)null);
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
