package boolex.logic.elements.core;

/**
 * Created by dani on 2/10/14.
 */

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.HashSet;
import java.util.Set;

public class BLXSocket implements BLXSignalReceiver {
    private Boolean value;
    private String id;
    private Set<BLXSignalReceiver> targets;

    public BLXSocket(String id) {
        this(id,null);
    }

    public BLXSocket(Boolean value) {
        this(null,value);
    }

    public BLXSocket(String id, Boolean value) {
        this.id = id;
        this.value = value;
        this.targets = new HashSet<>();
    }

    public String getId() {
        return id;
    }
    
    public Boolean getValue() {
        return value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public void addTarget(BLXSignalReceiver target) {
        targets.add(target);
    }

    @Override
    public void receive(BLXSignal signal, BLXSignalQueue queue) {
        if (queue != null) {
            setValue(signal.getValue());
            for (BLXSignalReceiver target : targets) {
                queue.add(signal.propagate(target, signal.getValue(), 0));
            }
        }
    }
}
