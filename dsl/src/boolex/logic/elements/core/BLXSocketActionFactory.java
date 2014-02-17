package boolex.logic.elements.core;

import boolex.logic.elements.signals.BLXRestoreSignal;
import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXStoreSignal;
import boolex.logic.elements.signals.BLXValueSignal;

import java.util.function.Consumer;

/**
 * Created by dani on 2/15/14.
 */
class BLXSocketActionFactory {
    public static Consumer<BLXSocket> getSocketAction(BLXSignal signal) {
        if (signal instanceof BLXValueSignal) {
            Boolean value = ((BLXValueSignal)signal).getValue();
            return socket -> socket.setValue(value);
        }
        else if (signal instanceof BLXStoreSignal)   return socket -> socket.store();
        else if (signal instanceof BLXRestoreSignal) return socket -> socket.restore();
        return socket -> {}; // do nothing
    }
}
