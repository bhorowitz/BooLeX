import boolex.antlr.BooLeXLexer;
import boolex.antlr.BooLeXParser;
import boolex.logic.elements.circuitbuilder.BLXCircuit;
import boolex.logic.elements.circuitbuilder.BLXModelGenerator;
import boolex.logic.elements.core.BLXEventManager;
import boolex.logic.elements.core.BLXSocket;
import boolex.typechecker.BooLeXTypeChecker;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static private final boolean INITIALIZE_TO_FALSE = false; // TODO get this value from the user

    static private BooLeXTypeChecker typeChecker = new BooLeXTypeChecker();
    static private BLXModelGenerator modelGenerator = new BLXModelGenerator(INITIALIZE_TO_FALSE);

    public static void main(String[] args) {
        try {
            BooLeXLexer bl = new BooLeXLexer(new ANTLRFileStream("examples/dlatch.blex"));
            BooLeXParser bp = new BooLeXParser(new CommonTokenStream(bl));

            BooLeXParser.ModuleContext module = bp.module();
            if (bp.getNumberOfSyntaxErrors() > 0)
                throw new Exception("There was a parse error.");

            if (!typeChecker.visit(module))
                throw new Exception("Program contains semantic errors.");

            System.out.println("Program is valid. Continuing...");

            BLXCircuit mainCircuit = modelGenerator.visit(module);
            //---------------------------------------------------
            List<BLXSocket> inputs = mainCircuit.getInputSockets();
            DLatchTest test = new DLatchTest(inputs.get(0), inputs.get(1));
            test.start();
        } catch (IOException e) {
            System.err.println("[error] Could not open example program.");
        } catch (Exception e) {
            System.err.println("[error] " + e.getMessage());
            e.printStackTrace();
        }
    }
}

class DLatchTest {
    private BLXSocket dataSocket;
    private BLXSocket clockSocket;
    private boolean dataValue;
    private boolean clockValue;
    private BLXEventManager eventManager;

    public DLatchTest(BLXSocket dataSocket, BLXSocket clockSocket) {
        Map<BLXSocket, Boolean> initialMap = new HashMap<>();
        this.dataSocket = dataSocket;
        this.clockSocket = clockSocket;
        this.dataValue = true;
        this.clockValue = false;
        initialMap.put(dataSocket, dataValue);
        initialMap.put(clockSocket, clockValue);
        this.eventManager = new BLXEventManager(initialMap, 250);
    }

    public void start() {
        System.out.println("Simulating: data = " + dataValue + ", clock = " + clockValue);
        new Thread(eventManager::start).start();
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
            System.out.println("Simulating: data = " + dataValue + ", clock = " + clockValue);
            new Thread(() -> {
                eventManager.update(dataSocket, dataValue);
                eventManager.update(clockSocket, clockValue);
            }).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(eventManager::stop).start();
    }
}