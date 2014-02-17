package boolex.logic.elements.signals;

/**
 * Created by dani on 2/11/14.
 */
public interface BLXSignalReceiver {
    public void receive(BLXSignal signal, BLXSignalQueue queue);
}
