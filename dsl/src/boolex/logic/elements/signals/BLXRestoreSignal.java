package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */
public class BLXRestoreSignal extends BLXSignal {
    public BLXRestoreSignal(BLXSignalReceiver target) {
        super(target, 0);
    }

    @Override
    public BLXSignal propagate(BLXSignalReceiver newTarget, int delay) {
        return new BLXRestoreSignal(newTarget);
    }
}
