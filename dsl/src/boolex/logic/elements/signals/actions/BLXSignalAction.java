package boolex.logic.elements.signals.actions;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;

/**
 * Created by dani on 2/14/14.
 */
public abstract class BLXSignalAction {
    public abstract void performSocketAction(BLXSocket socket);
    //public abstract void performGateAction(BLXGate gate);
}
