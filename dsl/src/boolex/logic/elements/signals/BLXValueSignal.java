package boolex.logic.elements.signals;

/**
 * Created by dani on 2/15/14.
 */
public class BLXValueSignal extends BLXSignal {
    private Boolean value;

    public BLXValueSignal(BLXSignalReceiver target, Boolean value, int delay) {
        super(target, delay);
        this.value = value;
    }

    public Boolean getValue() {
        return this.value;
    }

    @Override
    public BLXSignal propagate(BLXSignalReceiver newTarget, int delay) {
        return new BLXValueSignal(newTarget, value, delay);
    }
}
