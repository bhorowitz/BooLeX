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

//    Let's see if we ever actually need these:

// --Commented out by Inspection START (2/17/14 11:54 AM):
//    public void setLine(int line) {
//        this.line = line;
//    }
// --Commented out by Inspection STOP (2/17/14 11:54 AM)

    public int getPos() {
        return pos;
    }

// --Commented out by Inspection START (2/17/14 11:54 AM):
//    public void setPos(int pos) {
//        this.pos = pos;
//    }
// --Commented out by Inspection STOP (2/17/14 11:54 AM)
}
