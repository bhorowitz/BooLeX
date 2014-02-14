package boolex.logic.elements.signals;

import boolex.logic.elements.signals.actions.BLXNullSignalAction;
import boolex.logic.elements.signals.actions.BLXSignalAction;

/**
 * Created by dani on 2/11/14.
 */
public abstract class BLXSignal implements Comparable<BLXSignal> {
    private BLXSignalable target;
    private BLXSignalAction action;
    private int propagationDelay;

    public BLXSignal(BLXSignalable target, int delay) {
        this(target,delay,new BLXNullSignalAction());
    }

    public BLXSignal(BLXSignalable target, int delay, BLXSignalAction action) {
        this.target = target;
        this.propagationDelay = Math.max(delay,0);
        this.action = action;
    }

    public void signal(BLXSignalQueue queue) {
        if (queue != null && target != null)
            if (propagationDelay == 0)
                target.signal(this, queue);
    }

    public void decrement() {
        propagationDelay = Math.max(propagationDelay-1,0);
    }

    public int getDelay() {
        return propagationDelay;
    }

    public BLXSignalable getTarget() {
        return target;
    }

    @Override
    public int compareTo(BLXSignal other) {
        return propagationDelay - other.propagationDelay;
    }

    public BLXSignalAction getAction() {
        return action;
    }

    public abstract BLXSignal propagate(BLXSignalable newTarget, int delay);
}
