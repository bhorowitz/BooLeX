package boolex.logic.elements.signals;

import boolex.logic.elements.signals.actions.BLXStoreSignalAction;

/**
 * Created by dani on 2/11/14.
 */
public class BLXStoreSignal extends BLXSignal {
    public BLXStoreSignal(BLXSignalable target) {
        super(target, 0, new BLXStoreSignalAction());
    }

    @Override
    public BLXSignal propagate(BLXSignalable newTarget, int delay) {
        return new BLXStoreSignal(newTarget);
    }
}