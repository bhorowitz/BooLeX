import boolex.antlr.BooLeXLexer;
import boolex.antlr.BooLeXParser;
import boolex.typechecker.BooLeXTypeChecker;
import boolex.typechecker.ParseException;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Main {
    static private BooLeXTypeChecker typeChecker = new BooLeXTypeChecker();

    public static void main(String[] args) {
        try {
            BooLeXLexer bl = new BooLeXLexer(new ANTLRFileStream("examples/adder.blex"));
            BooLeXParser bp = new BooLeXParser(new CommonTokenStream(bl));

            BooLeXParser.ModuleContext module = bp.module();
            if (bp.getNumberOfSyntaxErrors() > 0)
                throw new Exception("There was a parse error.");

            if (!typeChecker.visit(module))
                throw new Exception("Program contains semantic errors.");

            System.out.println("Program is valid. Continuing...");
        } catch (IOException e) {
            System.err.println("[error] Could not open example program.");
        } catch (ParseException e) {
            System.err.println("[error] line " + e.getLine() + ":" + e.getPos() + " " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[error] " + e.getMessage());
        }
    }
}
