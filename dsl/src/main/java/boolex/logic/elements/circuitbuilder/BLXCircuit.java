package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXConstantSocket;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignalReceiver;

import java.util.*;

/**
 * A BLXCircuit is a building tool for logic circuits comprised of BLXSockets and
 * BLXGates.  As the parse tree is visited, a large BLXCircuit is built at each
 * root node by connecting the BLXCircuits returned from its branches.
 *
 * @author Dani Dickstein
 */
public class BLXCircuit {

    private List<BLXSocket> outputSockets;
    private List<BLXSocket> inputSockets;
    private final BLXConstantSocket trueSocket = new BLXConstantSocket(true);
    private final BLXConstantSocket falseSocket = new BLXConstantSocket(false);

    /**
     * Constructor for a fresh BLXCircuit
     * @param socketId The id for the initial socket of this circuit
     * @param defaultValue The default value for the sockets in this circuit
     */
    public BLXCircuit(String socketId, Boolean defaultValue) {
        outputSockets = new ArrayList<>();
        inputSockets = new ArrayList<>();
        BLXSocket coreInputSocket = new BLXSocket(socketId, defaultValue);
        BLXSocket coreOutputSocket = new BLXSocket(socketId, defaultValue);
        coreInputSocket.addTarget(coreOutputSocket);
        inputSockets.add(coreInputSocket);
        outputSockets.add(coreOutputSocket);
    }

    /**
     * Constructor for a BLXCircuit by joining together pre-existing sockets
     * @param inputSockets The input sockets for the new circuit
     * @param outputSockets The output sockets for the new circuit
     * @param trueTargets The signal receivers in this circuit to be set to a constant 1
     * @param falseTargets The signal receivers in this circuit to be set to a constant 0
     */
    public BLXCircuit(List<BLXSocket> inputSockets, List<BLXSocket> outputSockets,
                      Set<BLXSignalReceiver> trueTargets, Set<BLXSignalReceiver> falseTargets) {
        this.outputSockets = outputSockets;
        this.inputSockets  = inputSockets;
        if (trueTargets != null)
            trueSocket.addTargets(trueTargets);
        if (falseTargets != null)
            falseSocket.addTargets(falseTargets);
    }

    /**
     * Hook up an input socket to a constant value
     * @param constValue The constant value
     * @param inputSocketIndex The input socket
     */
    public void driveInputByConstant(boolean constValue, int inputSocketIndex) {
        if (inputSocketIndex >= 0 && inputSocketIndex < inputSockets.size()) {
            BLXConstantSocket driver = constValue ? trueSocket : falseSocket;
            driver.addTargets(inputSockets.get(inputSocketIndex).getTargets());
            inputSockets.remove(inputSocketIndex);
        }
    }

    /**
     * Get the list of output sockets for this circuit
     * @return The list of output sockets
     */
    public List<BLXSocket> getOutputSockets() {
        return outputSockets;
    }

    /**
     * Get the list of input sockets for this circuit
     * @return The list of input sockets
     */
    public List<BLXSocket> getInputSockets() {
        return inputSockets;
    }

    /**
     * Get the true socket for this circuit
     * @return The true socket for this circuit
     */
    public BLXSocket getTrueSocket() {
        return trueSocket;
    }

    /**
     * Get the false socket for this circuit
     * @return The false socket for this circuit
     */
    public BLXSocket getFalseSocket() {
        return falseSocket;
    }
}
