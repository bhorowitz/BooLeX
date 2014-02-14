package boolex.logic.elements.core;

/**
 * Created by dani on 2/10/14.
 */

import java.util.HashMap;
public class BLXTruthTable {
    private HashMap<Boolean[],Boolean[]> table;

    public static Boolean[][] permute(int size) {
        if (size == 1) {
            return new Boolean[][]{{false,true}};
        }
        Boolean[][] table = permute(size - 1);
        Boolean[][] newTable = new Boolean[table.length+1][table[0].length * 2];
        for (int i = 0; i < newTable.length; i++) {
            for (int j = 0; j < newTable[0].length; j++) {
                newTable[i][j] = (i == 0) ? j >= table[0].length : table[i-1][j % table[0].length];
            }
        }
        return newTable;
    }

    public Boolean[] lookup(Boolean... inputs) {
        return table.get(inputs);
    }

    public void addRule(Boolean[] inputs, Boolean[] outputs) {
        table.put(inputs,outputs);
    }
}
