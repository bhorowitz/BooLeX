import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BooLeXLexer bl = new BooLeXLexer(new ANTLRFileStream("examples\\adder.blex"));
            BooLeXParser bp = new BooLeXParser(new CommonTokenStream(bl));

            BooLeXParser.ModuleContext module = bp.module();
            TypeCheckingVisitor tcv = new TypeCheckingVisitor();
            Boolean programCorrect = tcv.visit(module);
            if(programCorrect) {
                System.out.println("Program is valid. Continuing...");
            } else {
                System.out.println("Error! Program is invalid. Cannot continue.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
