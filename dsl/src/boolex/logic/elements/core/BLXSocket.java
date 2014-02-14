package boolex.logic.elements.core;

/**
 * Created by dani on 2/10/14.
 */

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;
import boolex.logic.elements.signals.BLXSignalable;
import java.util.Set;
import java.util.HashSet;

public class BLXSocket implements BLXSignalable {

    public static enum BLXSocketDirection { SIGNAL_TO_GATE, SIGNAL_FROM_GATE };

    private Boolean value;
    private Boolean defaultValue;
    private Boolean cache;
    private BLXGate gate;
    private Set<BLXSocket> connections;
    private BLXSocketDirection signalDirection;

    public BLXSocket(BLXGate gate, BLXSocketDirection direction) {
        this(gate, direction, null);
    }

    public BLXSocket(BLXGate gate, BLXSocketDirection direction, Boolean defaultValue) {
        this.value = this.defaultValue = defaultValue;
        this.gate = gate;
        this.signalDirection = direction;
        this.connections = new HashSet<>();
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

    public void addConnection(BLXSocket connection) {
        if (!connections.contains(connection)) {
            connections.add(connection);
            connection.addConnection(this);
        }
    }

    @Override
    public void signal(BLXSignal signal, BLXSignalQueue queue) {
        signal.getAction().performSocketAction(this);
        if (signalDirection == BLXSocketDirection.SIGNAL_TO_GATE) {
            queue.add(signal.propagate(gate,0));
        }
        else if (signalDirection == BLXSocketDirection.SIGNAL_FROM_GATE) {
            for (BLXSocket connection : connections) {
                queue.add(signal.propagate(connection,0));
            }
        }
        // if signalDirection is null, then signal will not propagate.
    }
}
