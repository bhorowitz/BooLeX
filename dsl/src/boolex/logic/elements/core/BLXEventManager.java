package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;
import com.google.gson.Gson;

import java.util.*;

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
        // store the signals as an array of BLXSignals
        if (startSignals != null) {
            signals = new BLXSignal[startSignals.size()];
            int index = 0;
            for (Map.Entry<BLXSocket, Boolean> signal : startSignals.entrySet()) {
                signals[index++] = new BLXSignal(signal.getKey(), signal.getValue(), 0);
            }
        }
        // initialize the signal queue and define its behavior for each iteration
        this.queue = new BLXSignalQueue(delayTime, (components) -> {
            FrontEndIntegrator.integrate(components);
        });
    }

    public void update(Map<BLXSocket,Boolean> updateSignals, int delayTime) {
        for (Map.Entry<BLXSocket,Boolean> signal : updateSignals.entrySet())
            queue.add(new BLXSignal(signal.getKey(), signal.getValue(), 0));
    }

    public void start() {
        // load signals into signal queue and start
        queue.signal(signals);
    }

    public void stop() {
        queue.stop();
    }

}

class FrontEndIntegrator {
    public static void integrate(Set<BLXSignalReceiver> components) {
        Map<String, Boolean> socketMap = getSocketMap(components);
        socketMap.forEach((socketId, value) -> System.out.println(socketId+": "+value));
        System.out.println("---");
    }

    public static Map<String, Boolean> getSocketMap(Set<BLXSignalReceiver> components) {
        Map<String, Boolean> socketMap = new HashMap<>();
        if (components != null) {
            for (BLXSignalReceiver component : components) {
                if (component instanceof BLXSocket) {
                    BLXSocket socket = (BLXSocket) component;
                    if (socket.getId() != null && !socket.getId().equals("_")) //TODO remove _ part after Alex adds this as feature
                        socketMap.put(socket.getId(), socket.getValue());
                }
            }
        }
        return socketMap;
    }
}