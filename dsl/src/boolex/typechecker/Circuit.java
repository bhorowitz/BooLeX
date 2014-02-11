package boolex.typechecker;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex Reinking on 2/9/14.
 * This file is a part of BooLeX
 */
public class Circuit {
    private String name = "";
    private List<Symbol> arguments = new LinkedList<>();
    private List<Symbol> locals = new LinkedList<>();
    private int numberOfOutputs = -1;

    public Circuit(String name) {
        this.name = name;
    }

    public void addAllArguments(List<String> syms, Symbol.Type type) {
        for (String sym : syms) {
            arguments.add(new Symbol(sym, type));
        }
    }

    public void addAllLocals(List<String> identifiers, Symbol.Type type) {
        for (String sym : identifiers) {
            locals.add(new Symbol(sym, type));
        }
    }

    public int getNumberOfArguments() {
        return arguments.size();
    }

    public Symbol getSymbol(String name) {
        for (Symbol argument : arguments) {
            if (argument.getName().equals(name))
                return argument;
        }
        for (Symbol local : locals) {
            if (local.getName().equals(name))
                return local;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Circuit circuit = (Circuit) o;

        if (!arguments.equals(circuit.arguments)) return false;
        if (!locals.equals(circuit.locals)) return false;
        if (!name.equals(circuit.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + locals.hashCode();
        return result;
    }

    public int getNumberOfOutputs() {
        return numberOfOutputs;
    }

    public void setNumberOfOutputs(int numberOfOutputs) {
        this.numberOfOutputs = numberOfOutputs;
    }
}
