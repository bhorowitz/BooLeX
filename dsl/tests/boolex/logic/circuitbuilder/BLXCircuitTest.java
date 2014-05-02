package boolex.logic.circuitbuilder;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dani on 4/30/14.
 */
public class BLXCircuitTest {
    private BLXCircuit circuit1;
    private BLXCircuit circuit2;
    private BLXCircuit circuit3;

    @Before
    public void setUp() throws Exception {
        Boolean defaultValue = false;
        circuit1 = new BLXCircuit("foo",defaultValue);
        circuit2 = new BLXCircuit("bar",defaultValue);
        circuit3 = new BLXCircuit("baz",defaultValue);
    }

    @Test
    public void testDriveInputByConstant() throws Exception {
        circuit1.driveInputByConstant(true,0);
        circuit2.driveInputByConstant(false,0);
        assertTrue(circuit1.getInputSockets().isEmpty());
        assertTrue(circuit2.getInputSockets().isEmpty());
        assertFalse(circuit3.getInputSockets().isEmpty());

        assertTrue(circuit1.getTrueSocket().getTargets().contains(circuit1.getOutputSockets().get(0)));
        assertFalse(circuit1.getFalseSocket().getTargets().contains(circuit1.getOutputSockets().get(0)));
        assertTrue(circuit2.getFalseSocket().getTargets().contains(circuit2.getOutputSockets().get(0)));
        assertFalse(circuit2.getTrueSocket().getTargets().contains(circuit2.getOutputSockets().get(0)));
    }
}
