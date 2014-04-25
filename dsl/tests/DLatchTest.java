import boolex.antlr.BooLeXLexer;
import boolex.antlr.BooLeXParser;
import boolex.logic.elements.circuitbuilder.BLXCircuit;
import boolex.logic.elements.circuitbuilder.BLXModelGenerator;
import boolex.logic.elements.core.BLXEventManager;
import boolex.logic.elements.core.BLXSocket;
import boolex.typechecker.BooLeXTypeChecker;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DLatchTest {
    private DLatchRunner dlatch;
    static private BooLeXTypeChecker typeChecker = new BooLeXTypeChecker();
    static private BLXModelGenerator modelGenerator = new BLXModelGenerator(false);

    @Before
    public void setUp() throws Exception {
        BooLeXLexer bl = new BooLeXLexer(new ANTLRFileStream("dsl/examples/dlatch.blex"));
        BooLeXParser bp = new BooLeXParser(new CommonTokenStream(bl));

        BooLeXParser.ModuleContext module = bp.module();
        if (bp.getNumberOfSyntaxErrors() > 0)
            throw new Exception("There was a parse error.");

        if (!typeChecker.visit(module))
            throw new Exception("Program contains semantic errors.");

        BLXCircuit mainCircuit = modelGenerator.visit(module);

        List<BLXSocket> inputs = mainCircuit.getInputSockets();
        dlatch = new DLatchRunner(inputs.get(0), inputs.get(1));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDLatch() throws Exception {
        dlatch.start();
    }

    private class DLatchRunner {
        private BLXSocket dataSocket;
        private BLXSocket clockSocket;
        private boolean dataValue;
        private boolean clockValue;
        private BLXEventManager eventManager;

        public DLatchRunner(BLXSocket dataSocket, BLXSocket clockSocket) {
            this.dataSocket = dataSocket;
            this.clockSocket = clockSocket;
            this.dataValue = true;
            this.clockValue = false;
        }

        public void start() {
            this.eventManager = new BLXEventManager(10);

            eventManager.start();
            eventManager.update(dataSocket, dataValue);
            eventManager.update(clockSocket, clockValue);

            System.out.println("Simulating: data = " + dataValue + ", clock = " + clockValue);

            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!clockValue)
                    clockValue = true;
                else {
                    dataValue = !dataValue;
                    clockValue = false;
                }

                eventManager.update(dataSocket, dataValue);
                eventManager.update(clockSocket, clockValue);
            }
            eventManager.stop();
        }
    }
}