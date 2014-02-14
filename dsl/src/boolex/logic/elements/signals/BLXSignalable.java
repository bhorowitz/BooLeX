package boolex.logic.elements.signals;

import boolex.logic.elements.signals.BLXSignal;
import boolex.logic.elements.signals.BLXSignalQueue;

/**
 * Created by dani on 2/11/14.
 */
public interface BLXSignalable {
    public void signal(BLXSignal signal, BLXSignalQueue queue);
}
