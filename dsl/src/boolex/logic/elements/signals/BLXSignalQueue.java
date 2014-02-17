package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */

import java.util.Queue;
import java.util.PriorityQueue;
public class BLXSignalQueue {
    private Boolean test;
    private Queue<BLXSignal> queue;

    public BLXSignalQueue(Boolean test) {
        this.test = test;
        queue = new PriorityQueue<>();
    }

    public void add(BLXSignal signal) {
        queue.add(signal);
    }

    public void signal(BLXSignal signal) {
        if (test != null && test) {
            BLXSignalQueue storeQueue = new BLXSignalQueue(null);
            BLXSignalQueue restoreQueue = new BLXSignalQueue(null);
            BLXSignalReceiver target = signal.getTarget();
            storeQueue.signal(new BLXStoreSignal(target));
            add(signal);
            signalChain();
            restoreQueue.signal(new BLXRestoreSignal(target));
        }
        else {
            add(signal);
            signalChain();
        }
    }

    private void signalChain() {
        while (!queue.isEmpty()) {
            signalZeroes();
            decrementChain();
        }
    }

    private void signalZeroes() {
        while (queue.peek() != null && queue.peek().getDelay() == 0) {
            queue.poll().signal(this);
        }
    }

    private void decrementChain() {
        for (BLXSignal signal : queue) {
            signal.decrement();
        }
    }
}
