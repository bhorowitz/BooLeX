/**
 * Created by Alex Reinking on 2/9/14.
 * This file is a part of BooLeX
 */
public class Symbol {
    private String name = "";
    private Type type = null;

    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol = (Symbol) o;

        if (type != symbol.type) return false;
        if (name != null ? !name.equals(symbol.name) : symbol.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public enum Type {
        Local,
        Argument
    }
}
