package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */

import boolex.helpers.StablePriorityQueue;

import java.util.HashSet;
import java.util.Set;

public class BLXSignalQueue {
    public final static int DEFAULT_DELAY_TIME = 100; //milliseconds
    private StablePriorityQueue<BLXSignal> queue;
    private BLXSignalQueueCallback callback;
    private int delayTime; // milliseconds
    private Thread animation;

    public BLXSignalQueue() {
        this(DEFAULT_DELAY_TIME, null);
    }

    public BLXSignalQueue(int delayTime, BLXSignalQueueCallback callback) {
        queue = new StablePriorityQueue<>();
        animation = new Thread(this::animate);
        setDelayTime(delayTime);
        setCallback(callback);
    }

    public void setCallback(BLXSignalQueueCallback callback) {
        this.callback = callback;
    }

    public BLXSignalQueue(int delayTime) {
        this(delayTime, null);
    }

    public BLXSignalQueue(BLXSignalQueueCallback callback) {
        this(DEFAULT_DELAY_TIME, callback);
    }

    public void signal(BLXSignal signal) {
        if(signal != null && signal.getValue() != null)
            queue.add(signal);
        if(!animation.isAlive()) {
            animation = new Thread(this::animate);
            animation.start();
        }
    }

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

    private Set<BLXSignalReceiver> signalZeroes() {
        Set<BLXSignalReceiver> receivers = new HashSet<>();
        while (queue.peek() != null && queue.peek().getDelay() == 0) {
            BLXSignal nextSignal = queue.poll();
            nextSignal.signal(this);
            receivers.add(nextSignal.getTarget());
        }
        return receivers;
    }

    private void decrementChain() {
        for (BLXSignal signal : queue) {
            signal.decrement();
        }
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = Math.max(delayTime, 0);
    }

    public void stop() {
        queue.clear();
        animation.interrupt();
    }

    public interface BLXSignalQueueCallback {
        public void onSignalEvent(Set<BLXSignalReceiver> components);
    }
}
