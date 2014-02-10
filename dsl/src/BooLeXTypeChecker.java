import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/*
 * TODO: Make error messages more informative.
 */

public class BooLeXTypeChecker extends BooLeXBaseVisitor<Boolean> {
    private HashMap<String, Circuit> knownCircuits = new HashMap<>();

    private List<String> extractIdentifiers(BooLeXParser.IdentifierListContext ctx) {
        List<String> identifiers = new LinkedList<>();

        while (ctx != null) {
            identifiers.add(ctx.Identifier().toString());
            ctx = ctx.identifierList();
        }

        return identifiers;
    }

    private int expressionListLength(BooLeXParser.ExpressionListContext ctx) {
        int size = 0;

        while (ctx != null) {
            size++;
            ctx = ctx.expressionList();
        }

        return size;
    }

    private Circuit getCircuit(ParserRuleContext ctx) {
        if (ctx.getParent() == null)
            return null;
        else if (ctx instanceof BooLeXParser.CircuitDeclarationContext) {
            BooLeXParser.CircuitDeclarationContext circuitDeclarationContext = (BooLeXParser.CircuitDeclarationContext) ctx;
            Circuit ret = knownCircuits.get(circuitDeclarationContext.Identifier().toString());
            if (ret == null)
                System.err.println("Impossible!");
            return ret;
        } else
            return getCircuit(ctx.getParent());
    }

    @Override
    public Boolean visitCircuitDeclaration(@NotNull BooLeXParser.CircuitDeclarationContext ctx) {
        // Check that the circuit has not yet been defined.
        String circuitName = ctx.Identifier().toString();
        if (knownCircuits.get(circuitName) != null) {
            System.err.println("Circuit " + circuitName + " already exists.");
            return false;
        }

        Circuit circuit = new Circuit(circuitName);
        circuit.addAllArguments(extractIdentifiers(ctx.identifierList()), Symbol.Type.Argument);

        // Add the names of the inputs to the list of local circuit variables.
        knownCircuits.put(circuitName, circuit);

        // Check all of the assignments inside.
        List<BooLeXParser.AssignmentContext> assignment = ctx.assignment();
        for (BooLeXParser.AssignmentContext assignmentContext : assignment)
            if (!visitAssignment(assignmentContext))
                return false;

        // At this point, the only thing left unchecked is the output declaration.
        return visitOutStatement(ctx.outStatement());
    }

    @Override
    public Boolean visitCircuitCall(@NotNull BooLeXParser.CircuitCallContext ctx) {
        String callee = ctx.Identifier().toString();

        Circuit target= knownCircuits.get(callee);

        if (target == null) {
            System.err.println("Invalid call to " + callee);
            return false;
        }

        if(target.getNumberOfArguments() != expressionListLength(ctx.expressionList())) {
            System.err.println("Too few arguments to " + callee);
            return false;
        }

        return visitExpressionList(ctx.expressionList());
    }

    @Override
    public Boolean visitExpressionList(@NotNull BooLeXParser.ExpressionListContext ctx) {
        // An expression list is valid if ALL of its children are valid
        if (ctx.expressionList() != null) {
            return visitExpression(ctx.expression()) && visitExpressionList(ctx.expressionList());
        } else {
            return visitExpression(ctx.expression());
        }
    }

//    @Override
//    public Boolean visitIdentifierList(@NotNull BooLeXParser.IdentifierListContext ctx) {
//        // an identifier list on its own can't be invalid.
//        return true;
//    }

    @Override
    public Boolean visitExpression(@NotNull BooLeXParser.ExpressionContext ctx) {
        // An expression is either a factor or a composition of expressions
        // via nots, ands, ors, etc.
        if (ctx.factor() != null)
            return visitFactor(ctx.factor());
        else
            for (BooLeXParser.ExpressionContext subExpression : ctx.expression())
                if (!visitExpression(subExpression))
                    return false;
        return true;
    }

    @Override
    public Boolean visitAssignment(@NotNull BooLeXParser.AssignmentContext ctx) {
        // TODO: Implement enough of a symbol table to ensure that assignments do not under- or over- assign.
        if (!visitExpressionList(ctx.expressionList()))
            return false;

        Circuit circuit = getCircuit(ctx);
        if (circuit == null) {
            System.err.println("Stray assignment! How did this get past the parser?");
            return false;
        }

        List<String> identifiers = extractIdentifiers(ctx.identifierList());
        circuit.addAllLocals(identifiers, Symbol.Type.Local);
        return true;
    }

    @Override
    public Boolean visitModule(@NotNull BooLeXParser.ModuleContext ctx) {
        // Module is top-level, so we reset the state for this run
        knownCircuits = new HashMap<>();

        for (BooLeXParser.CircuitDeclarationContext circuitDeclarationContext : ctx.circuitDeclaration()) {
            Boolean circuitOk = visitCircuitDeclaration(circuitDeclarationContext);
            if (!circuitOk)
                return false;
        }
        return true;
    }

    @Override
    public Boolean visitFactor(@NotNull BooLeXParser.FactorContext ctx) {
        if (ctx.circuitCall() != null)
            return visitCircuitCall(ctx.circuitCall());
        else if (ctx.expression() != null)
            return visitExpression(ctx.expression());
        else if (ctx.Identifier() != null) {
            Symbol symbol = getCircuit(ctx).getSymbol(ctx.Identifier().toString());
            return symbol != null;
        } else if (ctx.BooleanValue() != null)
            return true;

        System.err.println("Unhandled case for factor!!!");
        return false;
    }

    @Override
    public Boolean visitOutStatement(@NotNull BooLeXParser.OutStatementContext ctx) {
        // An out statement is only as good as the expressions making up its outputs.
        getCircuit(ctx).setNumberOfOutputs(expressionListLength(ctx.expressionList()));
        return visitExpressionList(ctx.expressionList());
    }
}
