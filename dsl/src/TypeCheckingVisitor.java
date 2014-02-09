import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex Reinking on 2/8/14.
 * Created on 2/8/14 for BooLeX
 */
public class TypeCheckingVisitor extends BooLeXBaseVisitor<Boolean> {
    private List<String> knownCircuits;

    public TypeCheckingVisitor() {
        knownCircuits = new ArrayList<>();
    }

    @Override
    public Boolean visitCircuitDeclaration(@NotNull BooLeXParser.CircuitDeclarationContext ctx) {
        knownCircuits.add(ctx.Identifier().toString());
        List<BooLeXParser.AssignmentContext> assignment = ctx.assignment();

        for (BooLeXParser.AssignmentContext assignmentContext : assignment) {
            if (!visitAssignment(assignmentContext)) {
                return false;
            }
        }

        return visitOutStatement(ctx.outStatement());
    }

    @Override
    public Boolean visitCircuitCall(@NotNull BooLeXParser.CircuitCallContext ctx) {
        String callee = ctx.Identifier().toString();
        boolean validCall = knownCircuits.contains(callee);
        if (validCall)
            System.out.println("Valid: " + callee);
        else
            System.out.println("Invalid: " + callee);
        return validCall;
    }

    @Override
    public Boolean visitExpressionList(@NotNull BooLeXParser.ExpressionListContext ctx) {
        if (ctx.expressionList() != null) {
            return visitExpression(ctx.expression()) && visitExpressionList(ctx.expressionList());
        } else {
            return visitExpression(ctx.expression());
        }
    }

    @Override
    public Boolean visitIdentifierList(@NotNull BooLeXParser.IdentifierListContext ctx) {
        return true;
    }

    @Override
    public Boolean visitExpression(@NotNull BooLeXParser.ExpressionContext ctx) {
        if(ctx.factor() != null) return visitFactor(ctx.factor());
        return true;
    }

    @Override
    public Boolean visitAssignment(@NotNull BooLeXParser.AssignmentContext ctx) {
        return visitExpressionList(ctx.expressionList());
    }

    @Override
    public Boolean visitModule(@NotNull BooLeXParser.ModuleContext ctx) {
        for (BooLeXParser.CircuitDeclarationContext circuitDeclarationContext : ctx.circuitDeclaration()) {
            Boolean circuitOk = visitCircuitDeclaration(circuitDeclarationContext);
            if(!circuitOk)
                return false;
        }
        return true;
    }

    @Override
    public Boolean visitFactor(@NotNull BooLeXParser.FactorContext ctx) {
        if(ctx.circuitCall() != null)
            return visitCircuitCall(ctx.circuitCall());
        return true;
    }

    @Override
    public Boolean visitOutStatement(@NotNull BooLeXParser.OutStatementContext ctx) {
        return super.visitOutStatement(ctx);
    }
}
