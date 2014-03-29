package boolex.typechecker;

import boolex.antlr.BooLeXBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

import static boolex.antlr.BooLeXParser.*;
import static boolex.helpers.ANTLRHelper.flattenExpressionList;
import static boolex.helpers.ANTLRHelper.flattenIdentifierList;

public class BooLeXTypeChecker extends BooLeXBaseVisitor<Boolean> {
    private BooLeXScope scopes = new BooLeXScope();

    @Override
    public Boolean visitCircuitCall(@NotNull CircuitCallContext ctx) {
        String calleeName = ctx.Identifier().toString();
        BooLeXType callee = scopes.getSymbol(calleeName);

        if (callee == null) {
            System.err.println("Error: '" + calleeName + "' is not defined");
            return false;
        } else if (callee.getClass() != CircuitType.class) {
            System.err.println("Error: '" + calleeName + "' is not a circuit");
            return false;
        }

        CircuitType circuit = (CircuitType) callee;

        boolean ok;
        try {
            ok = expressionListLength(ctx.expressionList()) == circuit.getFormals();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            ok = false;
        }

        ok &= visitExpressionList(ctx.expressionList());
        return ok;
    }

    @Override
    public Boolean visitEvaluations(@NotNull EvaluationsContext ctx) {
        scopes.startScope("evaluations");
        boolean ok = true;
        for (ParseTree child : ctx.children) {
            ok &= visit(child);
        }
        scopes.endScope();
        return ok;
    }

    @Override
    public Boolean visitIdentifierList(@NotNull IdentifierListContext ctx) {
        List<String> names = flattenIdentifierList(ctx);
        boolean ok = true;
        for (String name : names) {
            if (scopes.getSymbol(name) != null) {
                System.err.println("Error: Duplicate symbol '" + name + "'");
                ok = false;
            } else {
                try {
                    scopes.insertSymbol(name, new SymbolType());
                } catch (ParseException e) {
                    System.err.println("Error! Failed to insert symbol: " + name);
                    ok = false;
                }
            }
        }
        return ok;
    }

    @Override
    public Boolean visitExpression(@NotNull ExpressionContext ctx) {
        try {
            if (countExpressionOutputs(ctx) < 0)
                return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        if (ctx.factor() != null)
            return visitFactor(ctx.factor());
        else
            for (ExpressionContext subExpression : ctx.expression())
                if (!visitExpression(subExpression))
                    return false;
        return true;
    }

    @Override
    public Boolean visitCircuitIndex(@NotNull CircuitIndexContext ctx) {
        if (!scopes.getOwner().equals("evaluations")) {
            System.err.println("Error! Cannot index a sub-circuit in a circuit definition.");
            return false;
        }

        String calleeName = ctx.Identifier().toString();
        int index = java.lang.Integer.parseInt(ctx.Integer().toString());
        BooLeXType callee = scopes.getSymbol(calleeName);

        if (callee == null) {
            System.err.println("Error! Undefined symbol " + calleeName + ".");
            return false;
        }

        if (!(callee instanceof CircuitType)) {
            System.err.println("Error! Undefined symbol " + calleeName + ".");
            return false;
        }

        if (index > ((CircuitType) callee).getOutputs()) {
            System.err.println("Error! " + calleeName + " does not have " + index + " outputs.");
            return false;
        }
        return true;
    }

    @Override
    public Boolean visitAssignment(@NotNull AssignmentContext ctx) {
        if (!visitExpressionList(ctx.expressionList()))
            return false;

        List<String> identifiers = flattenIdentifierList(ctx.identifierList());

        try {
            if (identifiers.size() != expressionListLength(ctx.expressionList()))
                throw new Exception("Error! Mismatched assignment.");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Boolean visitModule(@NotNull ModuleContext ctx) {
        // Add a new scope on to the list
        scopes.startScope();

        // Get the various circuit and evaluation declarations
        List<CircuitDeclarationContext> circuitDeclarations = ctx.circuitDeclaration();
        List<EvaluationsContext> evaluations = ctx.evaluations();

        boolean ok = true;

        // Check all the circuit declarations
        for (CircuitDeclarationContext circuitDeclaration : circuitDeclarations)
            ok &= visitCircuitDeclaration(circuitDeclaration);

        // Check the circuit evaluations
        for (EvaluationsContext evaluation : evaluations)
            ok &= visitEvaluations(evaluation);

        if(scopes.getSymbol("main") == null) {
            ok = false;
            System.err.println("Error! No 'main' circuit.");
        }

        // Destroy the scope
        scopes.endScope();
        return ok;
    }

    @Override
    public Boolean visitSetCircuit(@NotNull SetCircuitContext ctx) {
        String varName = ctx.Identifier().toString();
        int index = java.lang.Integer.parseInt(ctx.Integer().toString());
        BooLeXType assignee = scopes.getSymbol(varName);

        if (assignee == null || !(assignee instanceof CircuitType)) {
            System.err.println("Error! Circuit '" + varName + "' undefined!");
            return false;
        }

        try {
            if (countExpressionOutputs(ctx.expression()) != 1)
                return false;
            if (index >= ((CircuitType) assignee).getFormals())
                return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        return visitExpression(ctx.expression());
    }

    @Override
    public Boolean visitCircuitDeclaration(@NotNull CircuitDeclarationContext ctx) {
        // Save the circuit's name
        String name = ctx.Identifier().toString();
        // Save the circuit's formals
        List<String> formals = flattenIdentifierList(ctx.identifierList());

        // Construct the circuit
        CircuitType circuitType = new CircuitType(formals.size());

        // Add the circuit to the current scope; if it already exists, quit and return false.
        try {
            scopes.insertSymbol(name, circuitType);
        } catch (ParseException e) {
            System.err.println("Error! " + e.getMessage());
            return false;
        }

        // Start a scope for the circuit declaration
        scopes.startScope(name);

        // Now we check inside the declaration...
        boolean ok = true;

        // Add the locals to the scope. Locals can shadow globals, but we issue a warning
        for (String id : formals) {
            try {
                scopes.insertSymbol(id, new SymbolType());
            } catch (ParseException e) {
                System.err.println("Warning! " + e.getMessage());
            }
        }

        // Check the assignments
        for (AssignmentContext assignmentContext : ctx.assignment()) {
            visitIdentifierList(assignmentContext.identifierList());
        }

        // Check the assignments
        for (AssignmentContext assignmentContext : ctx.assignment()) {
            ok &= visitAssignment(assignmentContext);
        }

        // Check the return statement
        ok &= visitOutStatement(ctx.outStatement());

        // Leaving the circuit declaration
        scopes.endScope();
        return ok;
    }

    @Override
    public Boolean visitPrint(@NotNull PrintContext ctx) {
        return visitExpressionList(ctx.expressionList());
    }

    @Override
    public Boolean visitExpressionList(@NotNull ExpressionListContext ctx) {
        List<ExpressionContext> expressions = flattenExpressionList(ctx);
        boolean ok = true;
        for (ExpressionContext expression : expressions) {
            ok &= visitExpression(expression);
        }
        return ok;
    }

    @Override
    public Boolean visitFactor(@NotNull FactorContext ctx) {
        if (ctx.circuitCall() != null)
            return visitCircuitCall(ctx.circuitCall());
        else if (ctx.circuitIndex() != null)
            return visitCircuitIndex(ctx.circuitIndex());
        else if (ctx.expression() != null)
            return visitExpression(ctx.expression());
        else if (ctx.Identifier() != null) {
            String name = ctx.Identifier().toString();
            boolean symbolExists = scopes.getSymbol(name) != null;
            if(!symbolExists)
                System.err.println("Error: '" + name + "' does not exist.");
            return symbolExists;
        }
        else if (ctx.BooleanValue() != null)
            return true;

        System.err.println("Error! Unhandled case for factor.");
        return false;
    }

    @Override
    public Boolean visitOutStatement(@NotNull OutStatementContext ctx) {
        int numberOfOutputs;
        try {
            numberOfOutputs = expressionListLength(ctx.expressionList());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        ((CircuitType) scopes.getSymbol(scopes.getOwner())).setOutputs(numberOfOutputs);
        return visitExpressionList(ctx.expressionList());
    }

    @Override
    public Boolean visitCreateCircuit(@NotNull CreateCircuitContext ctx) {
        String locName = ctx.Identifier(0).toString();
        String circName = ctx.Identifier(1).toString();

        int nInputs = 0;
        if (ctx.expressionList() != null) {
            try {
                nInputs = expressionListLength(ctx.expressionList());
                if (!visitExpressionList(ctx.expressionList()))
                    return false;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return false;
            }
        }

        BooLeXType circuit = scopes.getSymbol(circName);
        if (circuit == null) {
            System.err.println("Error! No such circuit " + circName);
            return false;
        }

        if (scopes.getSymbol(locName) != null) {
            System.err.println("Error! Redefinition of symbol " + locName);
            return false;
        }

        if (!(circuit instanceof CircuitType)) {
            System.err.println("Error! Symbol '" + circName + "' is not a circuit");
            return false;
        }

        if (((CircuitType) circuit).getFormals() != nInputs) {
            System.err.println("Error! Mismatched number of inputs to " + locName);
            return false;
        }

        try {
            scopes.insertSymbol(locName, circuit);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Boolean visitTerminal(@NotNull TerminalNode node) {
        return true;
    }

    private int factorOutputs(FactorContext ctx) throws ParseException {
        if (ctx.circuitCall() != null) {
            TerminalNode identifier = ctx.circuitCall().Identifier();
            String callee = identifier.toString();
            BooLeXType circuit = scopes.getSymbol(callee);
            if (circuit == null)
                throw new ParseException(
                        callee + "' is not defined",
                        identifier.getSymbol().getLine(),
                        identifier.getSymbol().getCharPositionInLine());
            if (circuit instanceof CircuitType)
                return ((CircuitType) circuit).getOutputs();
            return 1;
        } else if (ctx.circuitIndex() != null) return 1;
        else if (ctx.expression() != null) return countExpressionOutputs(ctx.expression());
        else if (ctx.Identifier() != null) return 1;
        else if (ctx.BooleanValue() != null) return 1;
        return 1;
    }

    private int countExpressionOutputs(ExpressionContext ctx) throws ParseException {
        int number = -1;
        if (ctx.factor() != null)
            number = factorOutputs(ctx.factor());
        else
            for (ExpressionContext subExpression : ctx.expression())
                if (number == -1)
                    number = countExpressionOutputs(subExpression);
                else if (countExpressionOutputs(subExpression) != number)
                    throw new ParseException(
                            "Cannot apply binary operation to more than two elements.",
                            subExpression.getStart().getLine(),
                            subExpression.getStart().getCharPositionInLine());

        return number;
    }

    private int expressionListLength(ExpressionListContext ctx) throws ParseException {
        int size = 0;
        for (ExpressionContext exp : flattenExpressionList(ctx))
            size += countExpressionOutputs(exp);
        return size;
    }
}
