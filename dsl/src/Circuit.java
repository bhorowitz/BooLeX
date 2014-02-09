import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alex Reinking on 2/9/14.
 * This file is a part of BooLeX
 */
public class Circuit  {
    private String name = "";
    private List<Symbol> arguments = new LinkedList<>();
    private List<Symbol> locals = new LinkedList<>();

    public Circuit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArgument(Symbol sym) {
        arguments.add(sym);
    }

    public void addAllArguments(List<Symbol> syms) {
        arguments.addAll(syms);
    }

    public void addLocal(Symbol sym) throws Exception {
        if(getSymbol(sym.getName()) != null)
            throw new Exception("Error: symbol " + sym.getName() + " already exists.");
        locals.add(sym);
    }

    public void allAllLocals(List<Symbol> syms) {
        locals.addAll(syms);
    }

    public Symbol getSymbol(String name) {
        for (Symbol argument : arguments) {
            if(argument.getName().equals(name))
                return argument;
        }
        for (Symbol local : locals) {
            if(local.getName().equals(name))
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
}
