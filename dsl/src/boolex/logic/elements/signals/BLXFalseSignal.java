package boolex.logic.elements.signals;

import boolex.logic.elements.signals.actions.BLXFalseSignalAction;

/**
 * Created by dani on 2/11/14.
 */
public class BLXFalseSignal extends BLXSignal {
    public BLXFalseSignal(BLXSignalable target, int delay) {
        super(target, delay, new BLXFalseSignalAction());
    }

    @Override
    public BLXSignal propagate(BLXSignalable newTarget, int delay) {
        return new BLXFalseSignal(newTarget, delay);
    }
}
