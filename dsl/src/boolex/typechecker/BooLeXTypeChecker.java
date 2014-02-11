package boolex.typechecker;

import boolex.antlr.*;
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

    private int factorOutputs(BooLeXParser.FactorContext ctx) throws Exception {
        if (ctx.circuitCall() != null) {
            Circuit circuit = knownCircuits.get(ctx.circuitCall().Identifier().toString());
            if (circuit == null)
                throw new Exception("Error! boolex.typechecker.Circuit has not yet been declared.");
            return circuit.getNumberOfOutputs();
        } else if (ctx.expression() != null) return expressionOutputs(ctx.expression());
        else if (ctx.Identifier() != null) return 1;
        else if (ctx.BooleanValue() != null) return 1;
        return 1;
    }

    private int expressionOutputs(BooLeXParser.ExpressionContext ctx) throws Exception {
        int number = -1;
        if (ctx.factor() != null)
            number = factorOutputs(ctx.factor());
        else
            for (BooLeXParser.ExpressionContext subExpression : ctx.expression())
                if (number == -1)
                    number = expressionOutputs(subExpression);
                else if (expressionOutputs(subExpression) != number)
                    throw new Exception("Error! Cannot apply binary operation to many elements.");

        return number;
    }

    private int expressionListLength(BooLeXParser.ExpressionListContext ctx) throws Exception {
        int size = 0;

        while (ctx != null) {
            size += expressionOutputs(ctx.expression());
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
                System.err.println("Error! This should be impossible.");
            return ret;
        } else
            return getCircuit(ctx.getParent());
    }

    @Override
    public Boolean visitCircuitDeclaration(@NotNull BooLeXParser.CircuitDeclarationContext ctx) {
        // Check that the circuit has not yet been defined.
        String circuitName = ctx.Identifier().toString();
        if (knownCircuits.get(circuitName) != null) {
            System.err.println("Error! boolex.typechecker.Circuit " + circuitName + " already exists.");
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

        Circuit target = knownCircuits.get(callee);

        if (target == null) {
            System.err.println("Error! Invalid call to " + callee);
            return false;
        }

        try {
            int rhsOuts = expressionListLength(ctx.expressionList());
            if (target.getNumberOfArguments() != rhsOuts)
                throw new Exception("Error! Incorrect number of arguments to " + callee);
        } catch (Exception e) {
            System.err.println(e.getMessage());
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

    @Override
    public Boolean visitIdentifierList(@NotNull BooLeXParser.IdentifierListContext ctx) {
        Circuit circuit = getCircuit(ctx);
        while (ctx != null) {
            String identifier = ctx.Identifier().toString();
            if (circuit.getSymbol(identifier) != null) {
                System.err.println("Error: Redefining symbol \'" + identifier + "\'");
                return false;
            }
            ctx = ctx.identifierList();
        }
        return true;
    }

    @Override
    public Boolean visitExpression(@NotNull BooLeXParser.ExpressionContext ctx) {
        // An expression is either a factor or a composition of expressions
        // via nots, ands, ors, etc.
        try {
            if (expressionOutputs(ctx) < 0)
                return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

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
            System.err.println("Error! Stray assignment: how did this get past the parser?");
            return false;
        }

        if (!visitIdentifierList(ctx.identifierList()))
            return false;

        List<String> identifiers = extractIdentifiers(ctx.identifierList());

        try {
            if (identifiers.size() != expressionListLength(ctx.expressionList()))
                throw new Exception("Error! Mismatched assignment.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

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

        System.err.println("Error! Unhandled case for factor.");
        return false;
    }

    @Override
    public Boolean visitOutStatement(@NotNull BooLeXParser.OutStatementContext ctx) {
        // An out statement is only as good as the expressions making up its outputs.
        int numberOfOutputs;
        try {
            numberOfOutputs = expressionListLength(ctx.expressionList());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        getCircuit(ctx).setNumberOfOutputs(numberOfOutputs);
        return visitExpressionList(ctx.expressionList());
    }
}
