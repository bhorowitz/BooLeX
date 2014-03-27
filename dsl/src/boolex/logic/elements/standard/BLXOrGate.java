package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;

import static boolex.helpers.LogicHelper.isFalse;
import static boolex.helpers.LogicHelper.isTrue;

/**
 * Created by dani on 2/10/14.
 */
public class BLXOrGate extends BLXGate {

    public BLXOrGate(Boolean defaultValue) {
        setInputSocket(0, defaultValue);
        setInputSocket(1, defaultValue);
        setOutputSocket(0, defaultValue);
    }

    @Override
    protected BLXSignal computeSignalForOutputSocket(BLXSignal incomingSignal, BLXSocket outputSocket) {
        BLXSocket input0 = getInputSocket(0);
        BLXSocket input1 = getInputSocket(1);
        // If something went wrong, return unknown value signal
        if (input0 == null || input1 == null)
            return incomingSignal.propagate(outputSocket, null, 1);
        else {
            Boolean value0 = input0.getValue();
            Boolean value1 = input1.getValue();
            if (isTrue(value0) || isTrue(value1))
                return incomingSignal.propagate(outputSocket, true, 1);
            else if (isFalse(value0) && isFalse(value1))
                return incomingSignal.propagate(outputSocket, false, 1);
            else
                return incomingSignal.propagate(outputSocket, null, 1);
        }
    }
}
