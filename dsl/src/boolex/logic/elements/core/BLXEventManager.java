package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by dani on 2/17/14.
 */
public class BLXEventManager {
    private BLXSignalQueue queue;
    private BLXSignal[] signals;

    public BLXEventManager(Map<BLXSocket,Boolean> startSignals) {
        this(startSignals,BLXSignalQueue.DEFAULT_DELAY_TIME);
    }

    public BLXEventManager(Map<BLXSocket,Boolean> startSignals, int delayTime) {
        this(startSignals, delayTime, FrontEndIntegrator::integrate);
    }

    public BLXEventManager(Map<BLXSocket, Boolean> startSignals, int delayTime, BLXSignalQueue.BLXSignalQueueCallback callback) {
        // store the signals as an array of BLXSignals
        if (startSignals != null) {
            signals = new BLXSignal[startSignals.size()];
            int index = 0;
            for (Map.Entry<BLXSocket, Boolean> signal : startSignals.entrySet()) {
                signals[index++] = new BLXSignal(signal.getKey(), signal.getValue(), 0);
            }
        }
        // initialize the signal queue and define its behavior for each iteration
        this.queue = new BLXSignalQueue(delayTime, callback);
    }

    public void update(BLXSocket socket, Boolean value) {
        queue.signal(new BLXSignal(socket, value, queue.getDelayTime()));
    }

    public void update(BLXSocket socket, Boolean value) {
        queue.signal(new BLXSignal(socket, value, queue.getDelayTime()));
    }

    public void start() {
        // load signals into signal queue and start
        if (signals != null) {
            for (BLXSignal signal : signals)
                queue.signal(signal);
        }
    }

    public void stop() {
        queue.stop();
    }

}

class FrontEndIntegrator {
    //TODO why are we using a TreeMap rather than HashMap?
    private static Map<String, Boolean> currentValues = new TreeMap<>();

    public static void integrate(Set<BLXSignalReceiver> components) {
        String output = "";
        currentValues.putAll(getSocketMap(components));

        for(Map.Entry<String, Boolean> socket : currentValues.entrySet())
            if (!socket.getKey().equals(""))
                output += socket.getKey() + ": " + socket.getValue() + ", ";

        if(!output.equals(""))
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