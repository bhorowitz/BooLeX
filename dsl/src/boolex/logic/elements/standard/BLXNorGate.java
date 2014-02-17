package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXValueSignal;

/**
 * Created by dani on 2/10/14.
 */
public class BLXNorGate extends BLXGate {

    public BLXNorGate() {
        super();
        setInputSocket(0,null);
        setInputSocket(1,null);
        setOutputSocket(0, null);
    }

    @Override
    protected BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket) {
        BLXSocket input0 = getInputSocket(0);
        BLXSocket input1 = getInputSocket(1);
        // If something went wrong, return unknown value signal
        if (input0 == null || input1 == null)
            return new BLXValueSignal(outputSocket, null, 1);
        else if (incomingSignal instanceof BLXValueSignal) {
            Boolean value0 = input0.getValue();
            Boolean value1 = input1.getValue();
            if (value0 != null && value0 || value1 != null && value1)
                return new BLXValueSignal(outputSocket, false, 1);
            else if (value0 != null && !value0 && value1 != null && !value1)
                return new BLXValueSignal(outputSocket, true, 1);
            else
                return new BLXValueSignal(outputSocket, null, 1);
        }
        else
            return incomingSignal.propagate(outputSocket,1);
    }
}
