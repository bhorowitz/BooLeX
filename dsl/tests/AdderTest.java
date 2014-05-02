import boolex.antlr.BooLeXLexer;
import boolex.antlr.BooLeXParser;
import boolex.logic.circuitbuilder.BLXCircuit;
import boolex.logic.circuitbuilder.BLXModelGenerator;
import boolex.logic.elements.core.BLXEventManager;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignalReceiver;
import boolex.typechecker.BooLeXTypeChecker;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.fail;

public class AdderTest {
    static private BooLeXTypeChecker typeChecker = new BooLeXTypeChecker();
    static private BLXModelGenerator modelGenerator = new BLXModelGenerator(false);
    private AdderRunner adderRunner;

    @Before
    public void setUp() throws Exception {
        BooLeXLexer bl = new BooLeXLexer(new ANTLRFileStream("dsl/examples/adder.blex"));
        BooLeXParser bp = new BooLeXParser(new CommonTokenStream(bl));

        BooLeXParser.ModuleContext module = bp.module();
        if (bp.getNumberOfSyntaxErrors() > 0)
            throw new Exception("There was a parse error.");

        if (!typeChecker.visit(module))
            throw new Exception("Program contains semantic errors.");

        BLXCircuit mainCircuit = modelGenerator.visit(module);

        List<BLXSocket> inputs = mainCircuit.getInputSockets();
        List<BLXSocket> aInputs = inputs.subList(0, 4);
        List<BLXSocket> bInputs = inputs.subList(4, 8);
        adderRunner = new AdderRunner(aInputs, bInputs);
    }

    @Test
    public void testAdder() throws Exception {
        try {
            adderRunner.start();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private class AdderRunner {
        private List<BLXSocket> aInputs;
        private List<BLXSocket> bInputs;
        private BLXEventManager eventManager;
        private Map<String, Boolean> currentStates = new TreeMap<>();

        public AdderRunner(List<BLXSocket> aInputs, List<BLXSocket> bInputs) {
            this.aInputs = aInputs;
            this.bInputs = bInputs;
        }

        public void start() throws Exception {
            this.eventManager = new BLXEventManager(0, components -> currentStates.putAll(getSocketMap(components)));

            eventManager.start();

            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    try {
                        System.out.print(i + " + " + j + " = ");

                        eventManager.update(aInputs.get(3), (i & 0b0001) != 0);
                        eventManager.update(aInputs.get(2), (i & 0b0010) != 0);
                        eventManager.update(aInputs.get(1), (i & 0b0100) != 0);
                        eventManager.update(aInputs.get(0), (i & 0b1000) != 0);

                        eventManager.update(bInputs.get(3), (j & 0b0001) != 0);
                        eventManager.update(bInputs.get(2), (j & 0b0010) != 0);
                        eventManager.update(bInputs.get(1), (j & 0b0100) != 0);
                        eventManager.update(bInputs.get(0), (j & 0b1000) != 0);

                        Thread.sleep(50);

                        int result = (currentStates.get("%o4") ? 1 : 0) << 3
                                   | (currentStates.get("%o3") ? 1 : 0) << 2
                                   | (currentStates.get("%o2") ? 1 : 0) << 1
                                   | (currentStates.get("%o1") ? 1 : 0);

                        System.out.println(result);
                        if (((i + j) & 0b1111) != result) {
                            eventManager.stop();
                            throw new AssertionError("Bad addition: " + i + " + " + j + " = " + result);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            eventManager.stop();
        }

        private Map<String, Boolean> getSocketMap(Set<BLXSignalReceiver> components) {
            Map<String, Boolean> socketMap = new HashMap<>();
            if (components != null) {
                components.stream().filter(component -> component instanceof BLXSocket).forEach((BLXSignalReceiver component) -> {
                    BLXSocket socket = (BLXSocket) component;
                    if (socket.getId() != null && !socket.getId().equals("_"))
                        socketMap.put(socket.getId(), socket.getValue());
                });
            }
            return socketMap;
        }
    }
}