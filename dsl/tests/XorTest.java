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

public class XorTest {
    private XorRunner xorRunner;
    static private BooLeXTypeChecker typeChecker = new BooLeXTypeChecker();
    static private BLXModelGenerator modelGenerator = new BLXModelGenerator(false);

    @Before
    public void setUp() throws Exception {
        BooLeXLexer bl = new BooLeXLexer(new ANTLRFileStream("dsl/examples/xor.blex"));
        BooLeXParser bp = new BooLeXParser(new CommonTokenStream(bl));

        BooLeXParser.ModuleContext module = bp.module();
        if (bp.getNumberOfSyntaxErrors() > 0)
            throw new Exception("There was a parse error.");

        if (!typeChecker.visit(module))
            throw new Exception("Program contains semantic errors.");

        BLXCircuit mainCircuit = modelGenerator.visit(module);

        List<BLXSocket> inputs = mainCircuit.getInputSockets();
        xorRunner = new XorRunner(inputs.get(0), inputs.get(1));
    }

    @Test
    public void testXor() throws Exception {
        try {
            xorRunner.start();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private class XorRunner {
        private BLXSocket aSocket;
        private BLXSocket bSocket;
        private boolean aValue;
        private boolean bValue;
        private BLXEventManager eventManager;

        public XorRunner(BLXSocket aSocket, BLXSocket bSocket) {
            this.aSocket = aSocket;
            this.bSocket = bSocket;
            this.aValue = true;
            this.bValue = false;
        }

        private <K,V> boolean stable(Map<K, V> m1, Map<K, V> m2) {
            if (m1.size() != m2.size())
                return false;
            for (K key: m1.keySet())
                if (!m1.get(key).equals(m2.get(key)))
                    return false;
            return true;
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

        private void printState(Map<String, Boolean> currentValues) {
            String output = "";

            for (Map.Entry<String, Boolean> socket : currentValues.entrySet())
                if (!socket.getKey().equals(""))
                    output += socket.getKey() + ": " + socket.getValue() + ", ";

            if (!output.equals(""))
                System.out.println("[" + output + "]");
        }

        private String error = "";
        private int stableRun = 0;

        public void start() throws Exception {
            this.eventManager = new BLXEventManager(10, components -> {
                currentStates.putAll(getSocketMap(components));
            });

            eventManager.start();

            boolean values[][] = {{true, true}, {true, false}, {false, true}, {false, false}};

            for (int i = 0; i < 4; i++) {
                try {
                    eventManager.update(aSocket, values[i][0]);
                    eventManager.update(bSocket, values[i][1]);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(aSocket.getValue() ^ bSocket.getValue() != currentStates.get("%o1")) {
                    eventManager.stop();
                    throw new AssertionError("XOR computation failed");
                }
            }

            eventManager.stop();
        }
    }
}