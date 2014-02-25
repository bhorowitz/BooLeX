package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXValueSignal;

/**
 * Created by dani on 2/17/14.
 */
public class BLXEventManager {
    private BLXSocket startSocket;
    private Boolean value;
    private BLXSignalQueue queue;

    public BLXEventManager(BLXSocket startSocket, Boolean startValue, Boolean test) {
        this.startSocket = startSocket;
        this.value = startValue;
        this.queue = new BLXSignalQueue(test);
    }

    public void start() {
        queue.signal(new BLXValueSignal(startSocket,value,0));
    }

}
