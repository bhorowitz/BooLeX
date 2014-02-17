package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */
public class BLXStoreSignal extends BLXSignal {
    public BLXStoreSignal(BLXSignalReceiver target) {
        super(target, 0);
    }

    @Override
    public BLXSignal propagate(BLXSignalReceiver newTarget, int delay) {
        return new BLXStoreSignal(newTarget);
    }
}