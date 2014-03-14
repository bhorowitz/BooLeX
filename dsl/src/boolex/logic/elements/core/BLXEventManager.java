package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;
import net.sf.json.JSONArray;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by dani on 2/17/14.
 */
public class BLXEventManager {
    private BLXSocket startSocket;
    private Boolean value;
    private BLXSignalQueue queue;

    public BLXEventManager(BLXSocket startSocket, Boolean startValue) {
        this(startSocket,startValue,BLXSignalQueue.DEFAULT_DELAY_TIME);
    }

    public BLXEventManager(BLXSocket startSocket, Boolean startValue, int delayTime) {
        this.startSocket = startSocket;
        this.value = startValue;
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
        queue.signal(new BLXSignal(startSocket,value,0));
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