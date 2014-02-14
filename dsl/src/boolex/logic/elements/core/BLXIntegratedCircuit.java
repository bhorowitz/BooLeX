package boolex.logic.elements.core;

/**
 * Created by dani on 2/10/14.
 */
public class BLXIntegratedCircuit extends BLXGate {
    private BLXTruthTable table;

    public BLXIntegratedCircuit(int numInputs, int numOutputs) {
        super(numInputs, numOutputs);
        this.table = new BLXTruthTable();
    }

    public BLXIntegratedCircuit(BLXSocket[] inputSockets, BLXSocket[] outputSockets) {
        this(inputSockets, outputSockets, null);
    }

    public BLXIntegratedCircuit(BLXSocket[] inputSockets, BLXSocket[] outputSockets, int[] signalOrder) {
        super(inputSockets.length, outputSockets.length);
        this.table = new BLXTruthTable();
        Boolean[][] possibilities = BLXTruthTable.permute(inputSockets.length);
        for (Boolean[] possibility : possibilities) {
            for (int i = 0; i < possibility.length; i++) {
                BLXSocket socket = (signalOrder == null) ? inputSockets[i] : inputSockets[signalOrder[i]];
                Boolean signal   = (signalOrder == null) ? possibility[i]  : possibility[signalOrder[i]];
                socket.signal(signal,true);
            }
            Boolean[] result = new Boolean[outputSockets.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = getOutputSocket(i).getTestValue();
            }
            addRule(possibility,result);
        }
    }

    @Override
    public Boolean[] evaluate(Boolean[] inputValues) {
        inputValues = adjustSize(inputValues, numInputSockets());
        return table.lookup(inputValues);
    }

    public BLXTruthTable getTable() {
        return table;
    }

    public void addRule(Boolean[] inputs, Boolean[] outputs) {
        inputs  = adjustSize(inputs,  numInputSockets());
        outputs = adjustSize(outputs, numOutputSockets());
        table.addRule(inputs, outputs);
    }

    private Boolean[] adjustSize(Boolean[] values, int newSize) {
        if (values.length == newSize)
            return values;
        else {
            Boolean[] adjusted = new Boolean[newSize];
            for (int i = 0; i < values.length && i < newSize; i++) {
                adjusted[i] = values[i];
            }
            return adjusted;
        }
    }
}
