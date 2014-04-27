package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.HashSet;
import java.util.Set;

/**
 * A BLXSocket is used on either end of a gate for transmitting
 * signals through a circuit.
 */
public class BLXSocket implements BLXSignalReceiver {
    private Boolean value;
    private String id;
    private Set<BLXSignalReceiver> targets;

    /**
     * Constructor for BLXSocket
     * @param id The socket id
     */
    public BLXSocket(String id) {
        this(id,null);
    }

    /**
     * Constructor for BLXSocket
     * @param value The initial value for this socket
     */
    public BLXSocket(Boolean value) {
        this(null,value);
    }

    /**
     * Constructor for BLXSocket
     * @param id The socket id
     * @param value The initial value for this socket
     */
    public BLXSocket(String id, Boolean value) {
        this.id = id;
        this.value = value;
        this.targets = new HashSet<>();
    }

    /**
     * Get this socket's id
     * @return This socket's id
     */
    public String getId() {
        return id;
    }

    /**
     * Get this socket's value
     * @return This socket's value
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * Set this socket's id
     * @param id The new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Set this socket's value
     * @param value The new value
     */
    public void setValue(Boolean value) {
        this.value = value;
    }

    /**
     * Add a target to this socket
     * @param target Target to add to this socket
     */
    public void addTarget(BLXSignalReceiver target) {
        targets.add(target);
    }

    /**
     * Add a set of targets to this socket
     * @param targets Targets to add to this socket
     */
    public void addTargets(Set<BLXSignalReceiver> targets) {
        targets.forEach(this::addTarget);
    }

    /**
     * Get the targets for this socket
     * @return list of targets for socket
     */
    public Set<BLXSignalReceiver> getTargets() {
        return targets;
    }

    @Override
    public void receive(BLXSignal signal, BLXSignalQueue queue) {
        if (queue != null) {
            Boolean receivedValue = signal.getValue();
            setValue(receivedValue);
            for (BLXSignalReceiver target : targets) {
                queue.signal(signal.propagate(target, receivedValue, 0));
                assert(getValue() == receivedValue);
            }
        }
    }

}
