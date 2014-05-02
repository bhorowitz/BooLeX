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

public class DLatchTest {
    private DLatchRunner dLatch;
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
        dLatch = new DLatchRunner(inputs.get(0), inputs.get(1));
    }

    @Test
    public void testDLatch() throws Exception {
        try {
            dLatch.start();
        } catch (Exception e) {
            fail(e.getMessage());
        }
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
        private Map<String, Boolean> lastStates = null;

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
                if(lastStates == null) {
                    lastStates = new TreeMap<>(currentStates);
                    return;
                }

                boolean isStable = stable(lastStates, currentStates);
                if(isStable)
                    stableRun++;
                if(isStable && stableRun == 25) {
                    // testing invariants
                    if(currentStates.get("d") && currentStates.get("clk"))
                        if (!currentStates.get("%o1") || currentStates.get("%o2"))
                            error = "d = true and clk = true should => o1 = true and o2 = false.";

                    System.out.print("Stabilized! ");
                    printState(currentStates);
                    stableRun = 0;
                }

                lastStates = new TreeMap<>(currentStates);
            });

            eventManager.start();
            eventManager.update(dataSocket, dataValue);
            eventManager.update(clockSocket, clockValue);

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

                if(!error.equals("")) {
                    eventManager.stop();
                    throw new AssertionError(error);
                }

                eventManager.update(dataSocket, dataValue);
                eventManager.update(clockSocket, clockValue);
            }
            eventManager.stop();
        }
    }
}