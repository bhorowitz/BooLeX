import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import boolex.typechecker.BooLeXTypeChecker;
import boolex.antlr.*;

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

            Boolean programCorrect = typeChecker.visit(module);

            if (!programCorrect)
                throw new Exception("Program contains semantic errors.");

            System.out.println("Program is valid. Continuing...");
        } catch (IOException e) {
            System.err.println("Error! Could not open example program.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
