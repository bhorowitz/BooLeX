package boolex.logic.elements.standard;

import boolex.logic.elements.core.BLXGate;

/**
 * Created by dani on 2/10/14.
 */
public class BLXOrGate extends BLXGate {

    public BLXOrGate() {
        super(2,1);
    }

    @Override
    public Boolean[] evaluate(Boolean[] inputValues) {
        return new Boolean[] {inputValues[0] || inputValues[1]};
    }
}
