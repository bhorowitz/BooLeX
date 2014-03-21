package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;
import net.sf.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        signals = new BLXSignal[startSignals.size()];
        int index = 0;
        for (Map.Entry<BLXSocket,Boolean> signal : startSignals.entrySet()) {
            signals[index++] = new BLXSignal(signal.getKey(),signal.getValue(),0);
        }
        // initialize the signal queue and define its behavior for each iteration
        this.queue = new BLXSignalQueue(delayTime, (components) -> {
            JSONArray socketMap = JSONBuilder.buildSocketMap(components);
            /* TODO fill this in with Alex -
             *   also find out if we want to make this class
             *   or the queue class responsible for weeding
             *   out gates from sockets
             */
        });
    }

    public void start() {
        // load signals into signal queue and start
        queue.signal(signals);
    }

    public void stop() {
        queue.stop();
    }

}

class JSONBuilder {
    public static JSONArray buildSocketMap(Set<BLXSignalReceiver> components) {
        HashMap<String,Boolean> socketMap = new HashMap<>();
        for (BLXSignalReceiver component : components) {
            if (component instanceof BLXSocket) {
                BLXSocket socket = (BLXSocket)component;
                socketMap.put(socket.getId(),socket.getValue());
            }
        }
        return JSONArray.fromObject(socketMap);
    }
}