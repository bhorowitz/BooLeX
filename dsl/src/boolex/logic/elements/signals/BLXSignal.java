package boolex.logic.elements.signals;

import java.lang.reflect.Constructor;

/**
 * Created by dani on 2/11/14.
 */
public abstract class BLXSignal implements Comparable<BLXSignal> {
    private BLXSignalReceiver target;
    private BLXSignalReceiver origin;
    private int propagationDelay;

    public BLXSignal(BLXSignalReceiver target, int delay) {
        setTarget(target);
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

    @Override
    public int compareTo(BLXSignal other) {
        return propagationDelay - other.propagationDelay;
    }

    public abstract BLXSignal propagate(BLXSignalReceiver newTarget, int delay);

}