package boolex.logic.elements.signals;

import org.jetbrains.annotations.NotNull;

/**
 * A BLXSignal is used to simulate signals propagated through a circuit
 * comprised of BLXSockets and BLXGates.
 *
 * @author dani
 */
public class BLXSignal implements Comparable<BLXSignal> {
    private BLXSignalReceiver target;
    private Boolean value;
    private int propagationDelay;

    /**
     * Standard constructor for a BLXSignal
     * @param target The target receiver (either a socket or a gate) for this signal
     * @param value The value of this signal
     * @param delay The number of gate delays for this signal to wait before propagating
     */
    public BLXSignal(BLXSignalReceiver target, Boolean value, int delay) {
        setTarget(target);
        setValue(value);
        setDelay(delay);
    }

    /**
     * Fire the signal
     * @param queue The SignalQueue from which this signal emerged
     */
    public void signal(BLXSignalQueue queue) {
        if (queue != null && target != null)
            if (propagationDelay == 0)
                target.receive(this, queue);
    }

    /**
     * Decrement the delay (when delay reaches 0, the signal can be fired)
     */
    public void decrement() {
        propagationDelay = Math.max(propagationDelay-1,0);
    }

    /**
     * Get the current value of the signal
     * @return The value of the signal
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * Get the number of remaining gate delays
     * @return Number of remaining gate delays
     */
    public int getDelay() {
        return propagationDelay;
    }

    /**
     * Get the target receiver for this signal
     * @return This signal's target
     */
    public BLXSignalReceiver getTarget() {
        return target;
    }

    /**
     * Set the number of gate delays remaining before this signal can be fired
     * @param delay The new delay count
     */
    public void setDelay(int delay) {
        this.propagationDelay = Math.max(delay, 0);
    }

    /**
     * Set the target receiver for this signal
     * @param target The new receiver
     */
    public void setTarget(BLXSignalReceiver target) {
        this.target = target;
    }

    /**
     * Set the value of this signal
     * @param value The new value
     */
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public int compareTo(@NotNull BLXSignal other) {
        return propagationDelay - other.propagationDelay;
    }

    /**
     * Propagate this signal onward to a new target
     * @param newTarget The new target for the propagated signal
     * @param value The value of the signal to propagate
     * @param delay The delay count of the propagated signal
     * @return The propagated signal
     */
    public BLXSignal propagate(BLXSignalReceiver newTarget, Boolean value, int delay) {
        BLXSignal signal = new BLXSignal(newTarget,value,delay);
        return signal;
    }
}