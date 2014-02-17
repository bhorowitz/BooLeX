package boolex.logic.elements;

import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.standard.BLXAndGate;
import boolex.logic.elements.standard.BLXBuffer;
import boolex.logic.elements.standard.BLXOrGate;
import boolex.logic.elements.standard.BLXXorGate;

/**
 * Created by dani on 2/11/14.
 */
public class BLXLogicElementTest {
    public void testLogic() {
        BLXAndGate and = new BLXAndGate();
        BLXOrGate  or  = new BLXOrGate();
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

        BLXSocket i1 = alpha.getInputSocket(0);
        BLXSocket i2 = beta.getInputSocket(0);
        BLXSocket o1 = xor.getOutputSocket(0);

        System.out.println("The following tests are not self-contained.");
        System.out.println("Test 1: " + (o1.getValue() == null));
        i1.signal(true);
        System.out.println("Test 2: " + (o1.getValue() == null));
        i2.signal(true);
        System.out.println("Test 3: " + (o1.getValue() != null && !o1.getValue()));
        i1.signal(false);
        System.out.println("Test 4: " + (o1.getValue() != null && o1.getValue()));
        i2.signal(false);
        System.out.println("Test 5: " + (o1.getValue() != null && !o1.getValue()));
        i1.signal(true);
        System.out.println("Test 6: " + (o1.getValue() != null && o1.getValue()));
    }
}
