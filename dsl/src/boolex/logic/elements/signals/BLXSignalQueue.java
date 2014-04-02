package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */

import java.util.*;

public class BLXSignalQueue {
    public final static int DEFAULT_DELAY_TIME = 100; //milliseconds
    private Queue<BLXSignal> queue;
    private BLXSignalQueueCallback callback;
    private int delayTime; // milliseconds
    private boolean interrupted;
    private boolean animating;

    public BLXSignalQueue() {
        this(DEFAULT_DELAY_TIME, null);
    }

    public BLXSignalQueue(int delayTime, BLXSignalQueueCallback callback) {
        queue = new PriorityQueue<>();
        interrupted = false;
        animating = false;
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
        if (!interrupted && signal != null && signal.getValue() != null) {
            queue.add(signal);
            if (!animating)
                animate();
        }
    }

    private void animate() {
        animating = true;
        while (!interrupted && !queue.isEmpty()) {
            Set<BLXSignalReceiver> receivers = signalZeroes();
            decrementChain();
            if (callback != null) {
                try {
                    Thread.sleep(delayTime);
                } catch (InterruptedException ignored) {
                } finally {
                    callback.onSignalEvent(receivers);
                }
            }
        }
        animating = false;
    }

    private Set<BLXSignalReceiver> signalZeroes() {
        Set<BLXSignalReceiver> receivers = new HashSet<>();
        Stack<BLXSignal> nullStack = new Stack<>();
        while (queue.peek() != null && queue.peek().getDelay() == 0) {
            BLXSignal nextSignal = queue.poll();
            if (nextSignal.getValue() == null) {
                nullStack.push(nextSignal);
            } else {
                nextSignal.signal(this);
                receivers.add(nextSignal.getTarget());
            }
        }
        assert(nullStack.empty());
        if (!nullStack.empty()) {
            BLXSignal nextSignal = nullStack.pop();
            if (!receivers.contains(nextSignal.getTarget())) {
                nextSignal.signal(this);
                receivers.add(nextSignal.getTarget());
            }
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
