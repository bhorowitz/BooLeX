package boolex.logic.elements.signals;

import boolex.logic.elements.signals.actions.BLXUnknownSignalAction;

/**
 * Created by dani on 2/11/14.
 */
public class BLXUnknownSignal extends BLXSignal {
    public BLXUnknownSignal(BLXSignalable target, int delay) {
        super(target, delay, new BLXUnknownSignalAction());
    }

    @Override
    public BLXSignal propagate(BLXSignalable newTarget, int delay) {
        return new BLXUnknownSignal(newTarget, delay);
    }
}
