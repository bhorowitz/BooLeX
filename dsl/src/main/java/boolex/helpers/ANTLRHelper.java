package boolex.helpers;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

import static boolex.antlr.BooLeXParser.*;

/**
 * Created by ajr64 on 3/24/14.
 */
public class ANTLRHelper {
    public static List<String> flattenIdentifierList(IdentifierListContext identifierListContext) {
        List<String> names = new ArrayList<>();
        while (identifierListContext != null) {
            names.add(identifierListContext.Identifier().toString());
            identifierListContext = identifierListContext.identifierList();
        }
        return names;
    }

    public static List<ExpressionContext> flattenExpressionList(@NotNull ExpressionListContext ctx) {
        List<ExpressionContext> exps = new ArrayList<>();
        while (ctx != null) {
            exps.add(ctx.expression());
            ctx = ctx.expressionList();
        }
        return exps;
    }
}
