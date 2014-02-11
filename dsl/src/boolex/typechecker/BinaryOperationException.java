package boolex.typechecker;

/**
 * Created by alex on 2/11/14.
 */
public class BinaryOperationException extends ParseException {
    public BinaryOperationException(String s, int line, int charPositionInLine) {
        super(s, line, charPositionInLine);
    }
}
