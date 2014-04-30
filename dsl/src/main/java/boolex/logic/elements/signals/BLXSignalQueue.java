package boolex.logic.elements.signals;
import boolex.helpers.StablePriorityQueue;

import java.util.HashSet;
import java.util.Set;

/**
 * The BLXSignalQueue class is used to manage a set of signals as they propagate
 * through a logic circuit.
 *
 * @author Dani Dickstein
 */
public class BLXSignalQueue {

    /**
     * Interface for the BLXSignalQueue's callback method
     */
    public interface BLXSignalQueueCallback {
        public void onSignalEvent(Set<BLXSignalReceiver> components);
    }

    public final static int DEFAULT_DELAY_TIME = 100; //milliseconds
    private StablePriorityQueue<BLXSignal> queue;
    private BLXSignalQueueCallback callback;
    private int delayTime; // milliseconds
    private Thread animation;

    /**
     * Constructor for BLXSignalQueue
     * @param delayTime The amount of time to sleep between gate delays
     * @param callback The callback method to invoke after each gate delay simulation
     */
    public BLXSignalQueue(int delayTime, BLXSignalQueueCallback callback) {
        queue = new StablePriorityQueue<>();
        animation = new Thread(this::animate);
        setDelayTime(delayTime);
        setCallback(callback);
    }

    /**
     * Set the callback method to invoke after each gate delay simulation
     * @param callback The callback method
     */
    public void setCallback(BLXSignalQueueCallback callback) {
        this.callback = callback;
    }

    /**
     * Simulate a signal as it propagates through the circuit
     * @param signal The signal to transmit
     */
    public void signal(BLXSignal signal) {
        if(signal != null && signal.getValue() != null)
            queue.add(signal);
        if(!animation.isAlive()) {
            animation = new Thread(this::animate);
            animation.start();
        }
    }

    /**
     * Begin the animation sequence for a propagating signal
     */
    private void animate() {
        while (!Thread.interrupted() && !queue.isEmpty()) {
            Set<BLXSignalReceiver> receivers = signalZeroes();
            decrementChain();
            if (callback != null) {
                try {
                    Thread.sleep(getDelayTime());
                } catch (InterruptedException ignored) {
                } finally {
                    callback.onSignalEvent(receivers);
                }
            }
        }
    }

    /**
     * Signal all signals in the queue with a delay of 0
     * @return A set of all receivers that were touched in this iteration
     */
    private Set<BLXSignalReceiver> signalZeroes() {
        Set<BLXSignalReceiver> receivers = new HashSet<>();
        while (queue.peek() != null && queue.peek().getDelay() == 0) {
            BLXSignal nextSignal = queue.poll();
            nextSignal.signal(this);
            receivers.add(nextSignal.getTarget());
        }
        return receivers;
    }

    /**
     * Decrement delay counts of all signals remaining by 1
     */
    private void decrementChain() {
        for (BLXSignal signal : queue) {
            signal.decrement();
        }
    }

    /**
     * Get the amount of simulated sleep time per gate delay
     * @return The amount of time spent per delay
     */
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * Set the amount of simulated sleep time per gate delay
     * @param delayTime The amount of time spent per delay
     */
    public void setDelayTime(int delayTime) {
        this.delayTime = Math.max(delayTime, 0);
    }

    /**
     * Stop the animation sequence
     */
    public void stop() {
        queue.clear();
        animation.interrupt();
    }
}
