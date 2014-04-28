package boolex.logic.elements.core;

import boolex.logic.elements.circuitbuilder.BLXCircuit;
import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A BLXEventManager is used to manage circuit events and start/stop the animation.
 *
 * @author dani
 */
public class BLXEventManager {
    private EventRunner runner;
    private Thread runnerThread;

    /**
     * Constructor for BLXEventManager
     * @param delayTime The amount of time (in milliseconds) to delay between simulated gate delays
     * @param callback The callback function to invoke after each gate delay
     */
    public BLXEventManager(int delayTime, BLXSignalQueue.BLXSignalQueueCallback callback) {
        runner = new EventRunner(delayTime, callback);
        runnerThread = new Thread(runner);
    }

    /**
     * Update a socket in the circuit with a new value and propagate the signal
     * @param socket The socket to update
     * @param value The new value
     */
    public void update(BLXSocket socket, Boolean value) {
        runner.update(socket, value);
    }

    /**
     * Start the simulation
     */
    public void start() {
        runnerThread.start();
    }

    public void start(BLXCircuit circuit) {
        runnerThread.start();
        update(circuit.getTrueSocket(), true);
        update(circuit.getFalseSocket(), false);
    }

    /**
     * Stop the simulation
     */
    public void stop() {
        runnerThread.interrupt();
    }
}

/**
 * An EventRunner is used to spin off the circuit simulation
 */
class EventRunner implements Runnable {
    BlockingQueue<BLXSignal> signals = new LinkedBlockingQueue<>();
    private BLXSignalQueue queue;

    /**
     * Constructor for EventRunner
     * @param delayTime The amount of time (in milliseconds) to delay between simulated gate delays
     * @param callback The callback function to invoke after each gate delay
     */
    public EventRunner(int delayTime, BLXSignalQueue.BLXSignalQueueCallback callback) {
        this.queue = new BLXSignalQueue(delayTime, callback);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                BLXSignal signal = signals.take();
                if (signal.getTarget() == null) // poison pill
                    throw new InterruptedException();
                queue.signal(signal);
            }
        } catch (InterruptedException e) {
            this.stop();
        } finally {
            this.stop();
        }
    }

    /**
     * Stop the simulation
     */
    private void stop() {
        signals.clear();
        update(null, null); // Poison Pill
        queue.stop();
    }

    /**
     * Update a socket in the circuit with a new value and propagate the signal
     * @param socket The socket to update
     * @param value The new value
     */
    public void update(BLXSocket socket, Boolean value) {
        try {
            signals.put(new BLXSignal(socket, value, 1));
        } catch (InterruptedException e) {
            System.err.println("This shouldn't happen");
        }
    }
}