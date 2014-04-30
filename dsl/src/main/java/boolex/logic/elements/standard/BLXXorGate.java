package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignal;

import static boolex.helpers.LogicHelper.isNull;

/**
 * This class is designed to simulate a standard XOR gate
 * @author Dani Dickstein
 */
public class BLXXorGate extends BLXGate {

    /**
     * Standard constructor for XOR gate
     * @param defaultValue The default value of the input sockets
     */
    public BLXXorGate(Boolean defaultValue) {
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
            if (isNull(value0) || isNull(value1))
                return incomingSignal.propagate(outputSocket, null, 1);
            else
                return incomingSignal.propagate(outputSocket,value0 && !value1 || !value0 && value1,1);
        }
    }
}
