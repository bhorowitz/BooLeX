package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */
public class BLXSignal implements Comparable<BLXSignal> {
    private BLXSignalReceiver target;
    private BLXSignalReceiver origin;
    private Boolean value;
    private int propagationDelay;

    public BLXSignal(BLXSignalReceiver target, Boolean value, int delay) {
        setTarget(target);
        setValue(value);
        setDelay(delay);
    }

    public void signal(BLXSignalQueue queue) {
        if (queue != null && target != null)
            if (propagationDelay == 0)
                target.receive(this, queue);
    }

    public void decrement() {
        propagationDelay = Math.max(propagationDelay-1,0);
    }

    public Boolean getValue() {
        return value;
    }

    public int getDelay() {
        return propagationDelay;
    }

    public BLXSignalReceiver getTarget() {
        return target;
    }

    public BLXSignalReceiver getOrigin() {
        return origin;
    }

    public void setDelay(int delay) {
        this.propagationDelay = Math.max(delay, 0);
    }

    public void setTarget(BLXSignalReceiver target) {
        this.target = target;
    }

    public void setOrigin(BLXSignalReceiver origin) {
        this.origin = origin;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
     public int compareTo(BLXSignal other) {
        return propagationDelay - other.propagationDelay;
    }

    public BLXSignal propagate(BLXSignalReceiver newTarget, Boolean value, int delay) {
        BLXSignal signal = new BLXSignal(newTarget,value,delay);
        signal.setOrigin(getTarget());
        return signal;
    }
}