package boolex.logic.elements.circuitbuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dani on 4/30/14.
 */
public class BLXCircuitBuilderTest {
    private BLXCircuitBuilder circuitBuilder;
    private BLXCircuit circuit1;
    private BLXCircuit circuit2;

    @Before
    public void setUp() throws Exception {
        Boolean defaultValue = false;
        circuitBuilder = new BLXCircuitBuilder(defaultValue);
        circuit1 = new BLXCircuit("foo",defaultValue);
        circuit2 = new BLXCircuit("bar",defaultValue);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBuildCircuit() throws Exception {
        //circuitBuilder.buildCircuit(circuit1, circuit2, )
    }

    @Test
    public void testNot() throws Exception {

    }

    @Test
    public void testBuffer() throws Exception {

    }

    @Test
    public void testAnd() throws Exception {

    }

    @Test
    public void testOr() throws Exception {

    }

    @Test
    public void testXor() throws Exception {

    }

    @Test
    public void testNand() throws Exception {

    }

    @Test
    public void testNor() throws Exception {

    }

    @Test
    public void testXnor() throws Exception {

    }

    @Test
    public void testMerge() throws Exception {

    }

    @Test
    public void testChain() throws Exception {

    }
}
