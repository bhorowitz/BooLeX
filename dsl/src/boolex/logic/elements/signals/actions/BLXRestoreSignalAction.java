package boolex.logic.elements.signals.actions;

import boolex.logic.elements.core.BLXSocket;

/**
 * Created by dani on 2/14/14.
 */
public class BLXRestoreSignalAction extends BLXSignalAction {
    @Override
    public void performSocketAction(BLXSocket socket) {
        socket.restore();
    }
}
