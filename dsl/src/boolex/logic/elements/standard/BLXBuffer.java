package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXValueSignal;

/**
 * Created by dani on 2/17/14.
 */
public class BLXBuffer extends BLXGate {

    public BLXBuffer() {
        super();
        setInputSocket(0,null);
        setOutputSocket(0, null);
    }

    @Override
    protected BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket) {
        BLXSocket input0 = getInputSocket(0);
        // If something went wrong, return unknown value signal
        if (input0 == null)
            return new BLXValueSignal(outputSocket, null, 1);
        else if (incomingSignal instanceof BLXValueSignal)
            return new BLXValueSignal(outputSocket, input0.getValue(), 1);
        else
            return incomingSignal.propagate(outputSocket,1);
    }
}