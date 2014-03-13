package boolex.logic.elements.circuitbuilder;

import boolex.logic.elements.core.BLXGate;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.standard.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dani on 3/13/14.
 */
public class BLXCircuit {
    class DuplicateIdException extends Exception {
        public DuplicateIdException() {
            super("two different sockets were assigned the same id");
        }
    }

    private CircuitNode node;
    private Map<String,CircuitNode> nodeTable;

    public BLXCircuit(String id) {
        node = new CircuitNode(id);
        nodeTable = new HashMap<>();
        nodeTable.put(id,node);
    }

    public BLXCircuit assignId(String newId) throws DuplicateIdException {
        if (newId != null) {
            if (nodeTable.containsKey(newId))
                throw new DuplicateIdException();
            String oldId = node.getId();
            nodeTable.put(newId, nodeTable.remove(oldId));
            node.setId(newId);
        }
        return this;
    }

    public BLXCircuit initializeSocketsToFalse() {
        for (Map.Entry<String,CircuitNode> entry : nodeTable.entrySet()) {
            entry.getValue().getInputSocket().setValue(false);
            entry.getValue().getOutputSocket().setValue(false);
        }
        return this;
    }

    public BLXCircuit cycle(BLXCircuit inputCircuit) {
        
    }

    public BLXCircuit not() {
        BLXGate notGate = new BLXNotGate();
        notGate.setInputSocket(0, node.getInputSocket());
        String newId = IdGenerator.generate(nodeTable);
        node = new CircuitNode(newId);
        nodeTable.put(newId,node);
        notGate.setOutputSocket(0, node.getOutputSocket());
        return this;
    }

    public BLXCircuit and(String id) {
        return and(new BLXCircuit(id));
    }

    public BLXCircuit or(String id) {
        return or(new BLXCircuit(id));
    }

    public BLXCircuit xor(String id) {
        return xor(new BLXCircuit(id));
    }

    public BLXCircuit nand(String id) {
        return nand(new BLXCircuit(id));
    }

    public BLXCircuit nor(String id) {
        return nor(new BLXCircuit(id));
    }

    public BLXCircuit xnor(String id) {
        return xnor(new BLXCircuit(id));
    }

    public BLXCircuit and(BLXCircuit inputCircuit) {
        return joinWithBinaryGate(inputCircuit, new BLXAndGate());
    }

    public BLXCircuit or(BLXCircuit inputCircuit) {
        return joinWithBinaryGate(inputCircuit, new BLXOrGate());
    }

    public BLXCircuit xor(BLXCircuit inputCircuit) {
        return joinWithBinaryGate(inputCircuit, new BLXXorGate());
    }

    public BLXCircuit nand(BLXCircuit inputCircuit) {
        return joinWithBinaryGate(inputCircuit, new BLXNandGate());
    }

    public BLXCircuit nor(BLXCircuit inputCircuit) {
        return joinWithBinaryGate(inputCircuit, new BLXNorGate());
    }

    public BLXCircuit xnor(BLXCircuit inputCircuit) {
        return joinWithBinaryGate(inputCircuit, new BLXXnorGate());
    }

    private BLXCircuit joinWithBinaryGate(BLXCircuit inputCircuit, BLXGate gate) {
        if (inputCircuit == null || gate == null)
            return null;
        nodeTable.putAll(inputCircuit.nodeTable);
        gate.setInputSocket(0, node.getInputSocket());
        gate.setInputSocket(1, inputCircuit.node.getInputSocket());
        String newId = IdGenerator.generate(nodeTable);
        node = new CircuitNode(newId);
        nodeTable.put(newId,node);
        gate.setOutputSocket(0, node.getOutputSocket());
        return this;
    }
}

class CircuitNode {
    private BLXSocket inputSocket;
    private BLXSocket outputSocket;
    private String id;

    public CircuitNode(String id) {
        setId(id);
    }

    public boolean hasInputSocket() {
        return inputSocket != null;
    }

    public boolean hasOutputSocket() {
        return outputSocket != null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (hasInputSocket())
            this.inputSocket.setId(id);
        if (hasOutputSocket())
            this.outputSocket.setId(id);
    }

    public BLXSocket getInputSocket() {
        if (!hasInputSocket()) {
            inputSocket = new BLXSocket(id);
        }
        if (!hasOutputSocket()) {
            outputSocket = new BLXSocket(id);
            outputSocket.addTarget(inputSocket);
        }
        return inputSocket;
    }

    public BLXSocket getOutputSocket() {
        if (!hasOutputSocket()) {
            outputSocket = new BLXSocket(id);
        }
        return outputSocket;
    }
}

class IdGenerator {
    private static int numIds;

    public static String generate(Map<String,CircuitNode> existingNodes) {
        String newId = "sock"+numIds++;
        while (existingNodes.containsKey(newId)) {
            newId = "sock"+numIds++;
        }
        return newId;
    }
}

/*
 *
 * q.cycle(q.nor(s).nor(r))
 *
 */