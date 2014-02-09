import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;

/**
 * Created by Alex Reinking on 2/8/14.
 * Created on 2/8/14 for BooLeX
 *
 * TODO: Make error messages more informative.
 */

public class TypeCheckingVisitor extends BooLeXBaseVisitor<Boolean> {
    private List<String> knownCircuits = new ArrayList<>();
    private HashMap<String, List<String>> circuitLocals = new LinkedHashMap<>();

    private List<String> extractIdentifiers(BooLeXParser.IdentifierListContext ctx) {
        List<String> identifiers = new LinkedList<>();

        while (ctx != null) {
            identifiers.add(ctx.Identifier().toString());
            ctx = ctx.identifierList();
        }

        return identifiers;
    }

    private BooLeXParser.CircuitDeclarationContext getCircuit(ParserRuleContext ctx) {
        if(ctx.getParent() == null)
            return null;
        else if(ctx instanceof BooLeXParser.CircuitDeclarationContext)
            return (BooLeXParser.CircuitDeclarationContext) ctx;
        else
            return getCircuit(ctx.getParent());
    }

    @Override
    public Boolean visitCircuitDeclaration(@NotNull BooLeXParser.CircuitDeclarationContext ctx) {
        // Check that the circuit has not yet been defined.
        String circuitName = ctx.Identifier().toString();
        if (knownCircuits.contains(circuitName)) {
            System.err.println("Circuit " + circuitName + " already exists.");
            return false;
        }
        knownCircuits.add(circuitName);

        // Add the names of the inputs to the list of local circuit variables.
        circuitLocals.put(circuitName, extractIdentifiers(ctx.identifierList()));

        // Check all of the assignments inside.
        List<BooLeXParser.AssignmentContext> assignment = ctx.assignment();
        for (BooLeXParser.AssignmentContext assignmentContext : assignment) {
            if (!visitAssignment(assignmentContext)) {
                System.err.println("Assignment failed in circuit " + circuitName);
                return false;
            }
        }

        // At this point, the only thing left unchecked is the output declaration.
        return visitOutStatement(ctx.outStatement());
    }

    @Override
    public Boolean visitCircuitCall(@NotNull BooLeXParser.CircuitCallContext ctx) {
        String callee = ctx.Identifier().toString();

        BooLeXParser.CircuitDeclarationContext circuit = getCircuit(ctx);
        if(circuit == null) {
            System.err.println("Could not get circuit for node.");
            return false;
        }

        // If the name of the circuit appears among the extant circuits
        // or if the name was passed as an argument / is a local.
        boolean validCall = knownCircuits.contains(callee)
                         || circuitLocals.get(circuit.Identifier().toString())
                                         .contains(callee);

        if(!validCall) {
            System.err.println("Invalid call to " + callee);
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
        // an identifier list on its own can't be invalid.
        return true;
    }

    @Override
    public Boolean visitExpression(@NotNull BooLeXParser.ExpressionContext ctx) {
        // An expression is either a factor or a composition of expressions
        // via nots, ands, ors, etc.
        if (ctx.factor() != null)
            return visitFactor(ctx.factor());
        else {
            for (BooLeXParser.ExpressionContext subExpression : ctx.expression()) {
                if(!visitExpression(subExpression)) {
                    System.err.println("Sub-expression failed!");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Boolean visitAssignment(@NotNull BooLeXParser.AssignmentContext ctx) {
        // TODO: Implement enough of a symbol table to ensure that assignments do not under- or over- assign.
        if(!visitExpressionList(ctx.expressionList())) {
            System.err.println("Bad expression in assignment!");
            return false;
        }

        BooLeXParser.CircuitDeclarationContext circuit = getCircuit(ctx);
        if(circuit == null)
            return false;

        circuitLocals.get(circuit.Identifier().toString()).addAll(extractIdentifiers(ctx.identifierList()));
        return true;
    }

    @Override
    public Boolean visitModule(@NotNull BooLeXParser.ModuleContext ctx) {
        for (BooLeXParser.CircuitDeclarationContext circuitDeclarationContext : ctx.circuitDeclaration()) {
            Boolean circuitOk = visitCircuitDeclaration(circuitDeclarationContext);
            if (!circuitOk) {
                System.err.println("Bad circuit!");
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean visitFactor(@NotNull BooLeXParser.FactorContext ctx) {
        if (ctx.circuitCall() != null)
            return visitCircuitCall(ctx.circuitCall());
        else if(ctx.expression() != null)
            return visitExpression(ctx.expression());
        else if(ctx.Identifier() != null) {
            BooLeXParser.CircuitDeclarationContext circuit = getCircuit(ctx);
            return circuitLocals.get(circuit.Identifier().toString()).contains(ctx.Identifier().toString());
        } else if(ctx.BooleanValue() != null) {
            return true;
        }
        System.err.println("Unhandled case for factor!!!");
        return false;
    }

    @Override
    public Boolean visitOutStatement(@NotNull BooLeXParser.OutStatementContext ctx) {
        // An out statement is only as good as the expressions making up its outputs.
        return visitExpressionList(ctx.expressionList());
    }
}
