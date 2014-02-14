package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;

/**
 * Created by dani on 2/10/14.
 */
public class BLXNotGate extends BLXGate {

    public BLXNotGate() {
        super(1,1);
    }

    @Override
    public Boolean[] evaluate(Boolean[] inputValues) {
        return new Boolean[] {!inputValues[0]};
    }
}
