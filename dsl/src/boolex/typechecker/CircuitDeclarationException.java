package boolex.typechecker;

/**
 * Created by alex on 2/11/14.
 */
public class CircuitDeclarationException extends ParseException {
    public CircuitDeclarationException(String symbolName, int line, int pos) {
        super("Circuit " + symbolName + " not defined!", line, pos);
    }
}
