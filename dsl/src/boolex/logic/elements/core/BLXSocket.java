package boolex.logic.elements.core;

/**
 * Created by dani on 2/10/14.
 */

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;
import boolex.logic.elements.signals.BLXValueSignal;

import java.util.HashSet;
import java.util.Set;

public class BLXSocket implements BLXSignalReceiver {
    private Boolean value;
    private Boolean defaultValue;
    private Boolean cache;
    private Set<BLXSignalReceiver> targets;

    public BLXSocket(Boolean defaultValue) {
        this.value = this.defaultValue = defaultValue;
        this.targets = new HashSet<>();
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public void restore() {
        value = cache;
        cache = null;
    }

    public void store() {
        cache = value;
        value = defaultValue;
    }

    public void signal(Boolean value) {
        BLXSignalQueue queue = new BLXSignalQueue(false);
        queue.signal(new BLXValueSignal(this,value,0));
    }

    public void testSignal(Boolean value) {
        BLXSignalQueue testQueue = new BLXSignalQueue(true);
        testQueue.signal(new BLXValueSignal(this,value,0));
    }

    public void addTarget(BLXSignalReceiver target) {
        targets.add(target);
    }

    @Override
    public void receive(BLXSignal signal, BLXSignalQueue queue) {
        if (queue != null) {
            BLXSocketActionFactory.getSocketAction(signal).accept(this);
            for (BLXSignalReceiver target : targets) {
                queue.add(signal.propagate(target,0));
            }
        }
    }
}
