import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.standard.BLXAndGate;
import boolex.logic.elements.standard.BLXBuffer;
import boolex.logic.elements.standard.BLXOrGate;
import boolex.logic.elements.standard.BLXXorGate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alex Reinking on 2/18/14.
 * This file is a part of BooLeX
 */
public class BLXLogicTest {
    private BLXSocket i1;
    private BLXSocket i2;
    private BLXSocket o1;

    @Before
    public void setUp() throws Exception {
        BLXAndGate and = new BLXAndGate();
        BLXOrGate or  = new BLXOrGate();
        BLXXorGate xor = new BLXXorGate();

        and.connectTo(xor,0,0);
        or.connectTo(xor,0,1);

        BLXBuffer alpha = new BLXBuffer();
        BLXBuffer beta  = new BLXBuffer();

        alpha.connectTo(and,0,0);
        alpha.connectTo(or,0,0);
        beta.connectTo(and,0,1);
        beta.connectTo(or,0,1);

        alpha.setInputSocket(0,null);
        beta.setInputSocket(0,null);

        i1 = alpha.getInputSocket(0);
        i2 = beta.getInputSocket(0);
        o1 = xor.getOutputSocket(0);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBLXLogic() throws Exception {
        assertEquals("Output must initially be null.", o1.getValue(), null);

        i1.signal(true);
        assertEquals("i1 should not determine the circuit.", o1.getValue(), null);

        i2.signal(true);
        assertFalse("i1 and i2 => not o1", o1.getValue());

        i1.signal(false);
        assertTrue("not i1 and i2 => o1", o1.getValue());

        i2.signal(false);
        assertFalse("not i1 and not i2 => not o2", o1.getValue());

        i1.signal(true);
        assertTrue("i1 and not i2 => o1", o1.getValue());
    }
}
