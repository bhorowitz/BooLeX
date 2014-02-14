package boolex.logic.elements.signals;

import boolex.logic.elements.signals.actions.BLXTrueSignalAction;

/**
 * Created by dani on 2/11/14.
 */
public class BLXTrueSignal extends BLXSignal {
    public BLXTrueSignal(BLXSignalable target, int delay) {
        super(target, delay, new BLXTrueSignalAction());
    }


    @Override
    public BLXSignal propagate(BLXSignalable newTarget, int delay) {
        return new BLXTrueSignal(newTarget, delay);
    }
}
