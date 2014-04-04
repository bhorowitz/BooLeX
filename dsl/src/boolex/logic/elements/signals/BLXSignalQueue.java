package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */

import boolex.helpers.StablePriorityQueue;

import java.util.*;

public class BLXSignalQueue {
    public final static int DEFAULT_DELAY_TIME = 100; //milliseconds
    private StablePriorityQueue<BLXSignal> queue;
    private BLXSignalQueueCallback callback;
    private int delayTime; // milliseconds
    private boolean interrupted;

    public BLXSignalQueue() {
        this(DEFAULT_DELAY_TIME, null);
    }

    public BLXSignalQueue(int delayTime, BLXSignalQueueCallback callback) {
        queue = new StablePriorityQueue<>();
        interrupted = false;
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

    public void signal(BLXSignal[] signals) {
        interrupted = false;
        if(signals != null) {
            for (BLXSignal signal : signals)
                add(signal);
        }
        while (!queue.isEmpty() && !interrupted) {
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

    public void add(BLXSignal signal) {
        if (signal != null)
            queue.add(signal);
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
        interrupted = true;
    }

    public interface BLXSignalQueueCallback {
        public void onSignalEvent(Set<BLXSignalReceiver> components);
    }
}
