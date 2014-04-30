package boolex.logic.elements.core;

/**
 * A BLXConstantSocket is a special case of sockets that has a constant unchanging value.
 * A signal from the constant sockets is propagated through the circuit when the simulation begins.
 *
 * @author Dani Dickstein
 */
public class BLXConstantSocket extends BLXSocket {
    /**
     * Constructor for BLXConstantSocket
     * @param constantValue A constant socket value
     */
    public BLXConstantSocket(boolean constantValue) {
        super(constantValue);
    } 

    @Override
    public void setValue(Boolean value) { /* override and do nothing */}
}
