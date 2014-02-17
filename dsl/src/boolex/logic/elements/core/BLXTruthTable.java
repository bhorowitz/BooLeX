package boolex.logic.elements.core;

/**
 * Created by dani on 2/10/14.
 */

import java.util.HashMap;
public class BLXTruthTable {
    private int numInputs;
    private int numOutputs;

    private HashMap<Boolean[],Boolean[]> table;

    public static Boolean[][] generateBooleanTable(int size) {
        if (size == 1) {
            return new Boolean[][]{{false},{true},{null}};
        }
        Boolean[][] table = generateBooleanTable(size - 1);
        Boolean[][] newTable = new Boolean[table.length * 3][table[0].length + 1];
        for (int i = 0; i < newTable.length; i++) {
            for (int j = 0; j < newTable[0].length; j++) {
                if (j == 0) {
                    if (i < newTable.length / 3)
                        newTable[i][j] = false;
                    else if (i < newTable.length * 2 / 3)
                        newTable[i][j] = true;
                    else
                        newTable[i][j] = null;
                }
                else {
                    newTable[i][j] = table[i % table.length][j-1];
                }
            }
        }
        return newTable;
    }

    public BLXTruthTable(int numInputs, int numOutputs) {
        table = new HashMap<>();
        this.numInputs = numInputs;
        this.numOutputs = numOutputs;
    }

    public int getNumInputs() {
        return numInputs;
    }

    public int getNumOutputs() {
        return numOutputs;
    }

    public Boolean[] lookup(Boolean... inputs) {
        return table.get(inputs);
    }

    public void addRule(Boolean[] inputs, Boolean[] outputs) {
        if (inputs.length == getNumInputs() && outputs.length == getNumOutputs()) {
            // if the table contains a more generic form of the rule, don't put it in.
            // if the table contains a more specific form of the rule, replace it with this one.
            table.put(inputs,outputs);
        }
    }

    private Boolean[][] getNullMatches(Boolean[] input) {

    }

    private Boolean[][] permute(Boolean[] array) {
        if (array.length == 1)
            return new Boolean[][]{{array[0]}};
        else {
            Boolean[] partialArray = new Boolean[array.length-1];
            System.arraycopy(array,1,partialArray,0,partialArray.length);
            Boolean[][] partialPermutations = permute(partialArray);
            int numPermutations = partialPermutations.length * array.length;
            Boolean[][] permutations = new Boolean[numPermutations][partialPermutations[0].length+1];
            for (int i = 0; i < permutations.length; i++) {
                for (int j = 0; j < permutations[0].length; j++) {
                    permutations[i] = insert()
                }
            }
        }
    }
}
