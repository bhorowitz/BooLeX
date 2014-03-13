package boolex.typechecker;

/**
 * Created by alex on 2/11/14.
 */
public class ParseException extends Exception {
    private int line, pos;

    public ParseException(String message, int line, int pos) {
        super(message);
        this.line = line;
        this.pos = pos;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }
}