package boolex.typechecker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class BooLeXScope {
    private LinkedList<HashMap<String, BooLeXType>> scopes;
    private Stack<String> owners;

    public BooLeXScope() {
        scopes = new LinkedList<>();
        owners = new Stack<>();
    }

    public void startScope() {
        scopes.addFirst(new HashMap<String, BooLeXType>());
        owners.push(null);
    }

    public void startScope(String parent) {
        scopes.addFirst(new HashMap<String, BooLeXType>());
        owners.push(parent);
    }

    public void endScope() {
        scopes.removeFirst();
        owners.pop();
    }

    public String getOwner() {
        return owners.peek();
    }

    public BooLeXType getSymbol(String name) {
        for (HashMap<String, BooLeXType> scope : scopes) {
            if (scope.containsKey(name))
                return scope.get(name);
        }
        return null;
    }

    public void insertSymbol(String name, BooLeXType type) throws ParseException {
        if (getSymbol(name) != null)
            throw new ParseException("Duplicated identifier: '" + name + "'", 0, 0);
        scopes.getFirst().put(name, type);
    }
}
