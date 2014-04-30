package boolex.helpers;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

import static boolex.antlr.BooLeXParser.*;

/**
 * @author Alex Reinking
 */
public class ANTLRHelper {
    /**
     * This function takes the identifier list tree structure and changes it into a syntacticaly
     * ordered list
     *
     * @param identifierListContext an IdentifierListContext from the parse tree
     * @return the list of identifiers in order
     */
    public static List<String> flattenIdentifierList(IdentifierListContext identifierListContext) {
        List<String> names = new ArrayList<>();
        while (identifierListContext != null) {
            names.add(identifierListContext.Identifier().toString());
            identifierListContext = identifierListContext.identifierList();
        }
        return names;
    }

    /**
     * This function takes an expression list from the parse tree and turns it into an iterable list
     * @param ctx an ExpressionListContext from the parse tree
     * @return the list of expressions in order
     */
    public static List<ExpressionContext> flattenExpressionList(@NotNull ExpressionListContext ctx) {
        List<ExpressionContext> exps = new ArrayList<>();
        while (ctx != null) {
            exps.add(ctx.expression());
            ctx = ctx.expressionList();
        }
        return exps;
    }
}
