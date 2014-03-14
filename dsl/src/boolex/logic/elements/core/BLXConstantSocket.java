package boolex.logic.elements.core;

/**
 * Created by dani on 3/12/14.
 */
public class BLXConstantSocket extends BLXSocket {
    public BLXConstantSocket(String id, boolean constantValue) {
        super(id, constantValue);
    } 
    @Override
    public void setValue(Boolean value) { /* override and do nothing */}
}
