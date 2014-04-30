package boolex.logic.elements.signals;

/**
 * A BLXSignalReceiver is an object that can receive a BLXSignal.
 * These are currently only BLXGates and BLXSockets.
 *
 * @author Dani Dickstein
 */
public interface BLXSignalReceiver {
    /**
     * Perform a given action when a signal is received
     * @param signal The received signal
     * @param queue The queue from which the signal is sent
     */
    public void receive(BLXSignal signal, BLXSignalQueue queue);
}
