package boolex.typechecker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * This class helps manage scopes in the type checker.
 * @author Alex Reinking
 */
public class BooLeXScope {
    private LinkedList<HashMap<String, BooLeXType>> scopes;
    private Stack<String> owners;

    public BooLeXScope() {
        scopes = new LinkedList<>();
        owners = new Stack<>();
    }

    /**
     * Creates a new scope
     */
    public void startScope() {
        scopes.addFirst(new HashMap<>());
        owners.push(null);
    }

    /**
     * Creates a new scope belonging to the circuit named `parent`
     * @param parent the owner of the scope
     */
    public void startScope(String parent) {
        scopes.addFirst(new HashMap<>());
        owners.push(parent);
    }

    /**
     * Destroy the current scope
     */
    public void endScope() {
        scopes.removeFirst();
        owners.pop();
    }

    /**
     * Get the name of the current owner
     * @return the current owner
     */
    public String getOwner() {
        return owners.peek();
    }

    /**
     * Checks to see if we are currently in a scope owned by parent
     * @param parent the owner of the scope we're looking for
     * @return whether we're in that scope
     */
    public boolean inContext(String parent) { return owners.contains(parent); }

    public BooLeXType getSymbol(String name) {
        for (HashMap<String, BooLeXType> scope : scopes) {
            if (scope.containsKey(name))
                return scope.get(name);
        }
        return null;
    }

    /**
     * Adds a symbol to the current scope
     * @param name the name of the symbol
     * @param type the symbol's type
     * @throws ParseException
     */
    public void insertSymbol(String name, BooLeXType type) throws ParseException {
        if (getSymbol(name) != null)
            throw new ParseException("Duplicated identifier: '" + name + "'", 0, 0);
        scopes.getFirst().put(name, type);
    }
}
