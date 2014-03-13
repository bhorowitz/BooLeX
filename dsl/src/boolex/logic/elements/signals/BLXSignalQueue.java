package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */

import java.util.Queue;
import java.util.PriorityQueue;
import java.util.HashSet;

public class BLXSignalQueue {
    public final static int DEFAULT_DELAY_TIME = 100; //milliseconds

    public interface BLXSignalQueueCallback {
        public void onSignalEvent(HashSet<BLXSignalReceiver> components);
    }

    private Queue<BLXSignal> queue;
    private BLXSignalQueueCallback callback;
    private int delayTime; // milliseconds
    private boolean interrupted;

    public BLXSignalQueue() {
        this(DEFAULT_DELAY_TIME, null);
    }

    public BLXSignalQueue(int delayTime) {
        this(delayTime,null);
    }

    public BLXSignalQueue(BLXSignalQueueCallback callback) {
        this(DEFAULT_DELAY_TIME, callback);
    }

    public BLXSignalQueue(int delayTime, BLXSignalQueueCallback callback) {
        queue = new PriorityQueue<>();
        interrupted = false;
        setDelayTime(delayTime);
        setCallback(callback);
    }

    public void signal(BLXSignal signal) {
        if (signal != null) {
            interrupted = false;
            add(signal);
            while (!queue.isEmpty() && !interrupted) {
                HashSet<BLXSignalReceiver> receivers = signalZeroes();
                decrementChain();
                if (callback != null) {
                    try {
                        Thread.sleep(delayTime);
                    }
                    catch (InterruptedException e) { }
                    finally {
                        //TODO confirm that this should happen even on an interrupted exception
                        //TODO find out when an "interrupted exception" would occur.
                        callback.onSignalEvent(receivers);
                    }
                }
            }
        }
    }

    public void add(BLXSignal signal) {
        queue.add(signal);
    }

    private HashSet<BLXSignalReceiver> signalZeroes() {
        HashSet<BLXSignalReceiver> receivers = new HashSet<>();
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

    public void setCallback(BLXSignalQueueCallback callback) {
        this.callback = callback;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = Math.max(delayTime,0);
    }

    public int getDelayTime() {
        return delayTime;
    } 
    public void stop() {
        interrupted = true;
    }
}
