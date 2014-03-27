package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXConstantSocket;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.*;

/**
 * Created by ajr64 on 3/26/14.
 */
public class BLXCircuit {

    private List<BLXSocket> outputSockets;
    private List<BLXSocket> inputSockets;
    private final BLXConstantSocket trueSocket = new BLXConstantSocket(true);
    private final BLXConstantSocket falseSocket = new BLXConstantSocket(false);

    public BLXCircuit(String socketId, Boolean defaultValue) {
        outputSockets = new ArrayList<>();
        inputSockets = new ArrayList<>();
        BLXSocket coreInputSocket = new BLXSocket(socketId, defaultValue);
        BLXSocket coreOutputSocket = new BLXSocket(socketId, defaultValue);
        coreInputSocket.addTarget(coreOutputSocket);
        inputSockets.add(coreInputSocket);
        outputSockets.add(coreOutputSocket);
    }

    public void driveInputByConstant(boolean constValue, int inputSocketIndex) {
        if (inputSocketIndex >= 0 && inputSocketIndex < inputSockets.size()) {
            BLXConstantSocket driver = constValue ? trueSocket : falseSocket;
            driver.addTargets(inputSockets.get(inputSocketIndex).getTargets());
            inputSockets.remove(inputSocketIndex);
        }
    }

    public BLXCircuit(List<BLXSocket> inputSockets, List<BLXSocket> outputSockets,
                      Set<BLXSignalReceiver> trueTargets, Set<BLXSignalReceiver> falseTargets) {
        this.outputSockets = outputSockets;
        this.inputSockets  = inputSockets;
        if (trueTargets != null)
            trueSocket.addTargets(trueTargets);
        if (falseTargets != null)
            falseSocket.addTargets(falseTargets);
    }

    public List<BLXSocket> getOutputSockets() {
        return outputSockets;
    }

    public List<BLXSocket> getInputSockets() {
        return inputSockets;
    }

    public BLXSocket getTrueSocket() {
        return trueSocket;
    }

    public BLXSocket getFalseSocket() {
        return falseSocket;
    }
}
