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
//        System.err.println("Now setting " + id + " to " + value);
        this.value = value;
    }

    public void addTarget(BLXSignalReceiver target) {
        targets.add(target);
    }

    public void addTargets(Set<BLXSignalReceiver> targets) {
        targets.forEach((BLXSignalReceiver target) -> { addTarget(target); });
    }

    public Set<BLXSignalReceiver> getTargets() {
        return targets;
    }

    @Override
    public void receive(BLXSignal signal, BLXSignalQueue queue) {
        assert(queue != null);
        if (queue != null) {
            Boolean receivedValue = signal.getValue();
            setValue(receivedValue);
            for (BLXSignalReceiver target : targets) {
                queue.add(signal.propagate(target, receivedValue, 0));
                assert(getValue() == receivedValue);
            }
        }
    }

}
