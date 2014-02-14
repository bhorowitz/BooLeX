package boolex.logic.elements.signals;

import boolex.logic.elements.signals.actions.BLXRestoreSignalAction;

/**
 * Created by dani on 2/11/14.
 */
public class BLXRestoreSignal extends BLXSignal {
    public BLXRestoreSignal(BLXSignalable target) {
        super(target, 0, new BLXRestoreSignalAction());
    }

    @Override
    public BLXSignal propagate(BLXSignalable newTarget, int delay) {
        return new BLXRestoreSignal(newTarget);
    }
}
