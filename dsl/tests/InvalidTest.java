import boolex.antlr.BooLeXLexer;
import boolex.antlr.BooLeXParser;
import boolex.logic.elements.circuitbuilder.BLXCircuit;
import boolex.logic.elements.circuitbuilder.BLXModelGenerator;
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

public class InvalidTest {
    private InvalidRunner invalidRunner;
    static private BooLeXTypeChecker typeChecker = new BooLeXTypeChecker();
    static private BLXModelGenerator modelGenerator = new BLXModelGenerator(false);

    @Before
    public void setUp() throws Exception {
        BooLeXLexer bl = new BooLeXLexer(new ANTLRFileStream("dsl/examples/invalid.blex"));
        BooLeXParser bp = new BooLeXParser(new CommonTokenStream(bl));

        BooLeXParser.ModuleContext module = bp.module();
        if (bp.getNumberOfSyntaxErrors() > 0)
            throw new Exception("There was a parse error.");

        if (!typeChecker.visit(module))
            throw new Exception("Program contains semantic errors.");

        BLXCircuit mainCircuit = modelGenerator.visit(module);

        invalidRunner = new InvalidRunner(mainCircuit);
    }

    @Test
    public void testInvalid() throws Exception {
        try {
            invalidRunner.start();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private class InvalidRunner {
        private BLXSocket aSocket;
        private BLXSocket bSocket;
        private BLXSocket cSocket;
        private BLXCircuit invalidCircuit;
        private BLXEventManager eventManager;

        public InvalidRunner(BLXCircuit invalidCircuit) {
            this.aSocket = invalidCircuit.getInputSockets().get(0);
            this.bSocket = invalidCircuit.getInputSockets().get(1);
            this.cSocket = invalidCircuit.getInputSockets().get(2);
            this.invalidCircuit = invalidCircuit;
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

        private Map<String, Boolean> currentStates = new TreeMap<>();

        public void start() throws Exception {
            this.eventManager = new BLXEventManager(10, components -> {
                currentStates.putAll(getSocketMap(components));
                System.out.println(currentStates);
            });

            eventManager.start(invalidCircuit);

            boolean values[][] = {{true, false, false}, {false, true, false}, {false, false, true}};

            for (boolean[] value : values) {
                try {
                    eventManager.update(aSocket, value[0]);
                    eventManager.update(bSocket, value[1]);
                    eventManager.update(cSocket, value[2]);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            eventManager.stop();
        }
    }
}