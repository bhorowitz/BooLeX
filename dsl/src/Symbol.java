/**
 * Created by Alex Reinking on 2/9/14.
 * This file is a part of BooLeX
 */
public class Symbol {
    private String name = "";
    private boolean isLocal = false;

    public Symbol(String name, boolean local) {
        this.name = name;
        isLocal = local;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol = (Symbol) o;

        if (isLocal != symbol.isLocal) return false;
        if (name != null ? !name.equals(symbol.name) : symbol.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (isLocal ? 1 : 0);
        return result;
    }
}
