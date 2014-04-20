package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by dani on 2/17/14.
 */
public class BLXEventManager {
    private EventRunner runner;
    private Thread runnerThread;

    public BLXEventManager() {
        this(BLXSignalQueue.DEFAULT_DELAY_TIME);
    }

    public BLXEventManager(int delayTime) {
        this(delayTime, FrontEndIntegrator::integrate);
    }

    public BLXEventManager(int delayTime, BLXSignalQueue.BLXSignalQueueCallback callback) {
        runner = new EventRunner(delayTime, callback);
        runnerThread = new Thread(runner);
    }

    public void update(BLXSocket socket, Boolean value) {
        runner.update(socket, value);
    }

    public void start() {
        runnerThread.start();
    }

    public void stop() {
        runnerThread.interrupt();
    }

}

class EventRunner implements Runnable {
    BlockingQueue<BLXSignal> signals = new LinkedBlockingQueue<>();
    private BLXSignalQueue queue;

    public EventRunner(int delayTime, BLXSignalQueue.BLXSignalQueueCallback callback) {
        this.queue = new BLXSignalQueue(delayTime, callback);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                BLXSignal signal = signals.take();
                if (signal.getTarget() == null) // Poison Pill
                    throw new InterruptedException();
                queue.signal(signal);
            }
        } catch (InterruptedException e) {
            this.stop();
        } finally {
            this.stop();
        }
    }

    private void stop() {
        signals.clear();
        update(null, null); // Poison Pill
        queue.stop();
    }

    public void update(BLXSocket socket, Boolean value) {
        try {
            signals.put(new BLXSignal(socket, value, 1));
        } catch (InterruptedException e) {
            System.err.println("This shouldn't happen");
        }
    }
}

class FrontEndIntegrator {
    private static Map<String, Boolean> currentValues = new TreeMap<>();

    public static void integrate(Set<BLXSignalReceiver> components) {
        String output = "(" + components.size() + ") ";
        currentValues.putAll(getSocketMap(components));

        for (Map.Entry<String, Boolean> socket : currentValues.entrySet())
            if (!socket.getKey().equals(""))
                output += socket.getKey() + ": " + socket.getValue() + ", ";

        if (!output.equals(""))
            System.out.println("[" + output + "]");
    }

    public static Map<String, Boolean> getSocketMap(Set<BLXSignalReceiver> components) {
        Map<String, Boolean> socketMap = new HashMap<>();
        if (components != null) {
            components.stream().filter(component -> component instanceof BLXSocket).forEach((BLXSignalReceiver component) -> {
                BLXSocket socket = (BLXSocket) component;
                //TODO remove _ part after Alex adds this as feature
                if (socket.getId() != null && !socket.getId().equals("_"))
                    socketMap.put(socket.getId(), socket.getValue());
            });
        }
        return socketMap;
    }
}