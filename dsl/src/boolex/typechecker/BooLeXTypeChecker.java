package boolex.typechecker;

import boolex.antlr.BooLeXBaseVisitor;
import boolex.antlr.BooLeXParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/*
 * TODO: Make error messages more informative.
 */

public class BooLeXTypeChecker extends BooLeXBaseVisitor<Boolean> {
    private HashMap<String, CircuitDeclaration> knownCircuits = new HashMap<>();
    private HashMap<String, CircuitDeclaration> currentEvaluationScope = null;

    // Terminals are without context and so are always valid, since they
    // escaped the parser.
    @Override
    public Boolean visitTerminal(@NotNull TerminalNode node) {
        return true;
    }

    private List<String> extractIdentifiers(BooLeXParser.IdentifierListContext ctx) {
        List<String> identifiers = new LinkedList<>();

        while (ctx != null) {
            identifiers.add(ctx.Identifier().toString());
            ctx = ctx.identifierList();
        }

        return identifiers;
    }

    @Override
    public Boolean visitPrint(@NotNull BooLeXParser.PrintContext ctx) {
        return visitExpressionList(ctx.expressionList());
    }

    @Override
    public Boolean visitEvaluations(@NotNull BooLeXParser.EvaluationsContext ctx) {
        currentEvaluationScope = new HashMap<>();
        for (ParseTree child : ctx.children) {
            Boolean result = visit(child);
            if (!result)
                return false;
        }
        currentEvaluationScope = null;
        return true;
    }

    @Override
    public Boolean visitSetCircuit(@NotNull BooLeXParser.SetCircuitContext ctx) {
        String varName = ctx.Identifier().toString();
        int index = Integer.parseInt(ctx.Integer().toString());
        if (!currentEvaluationScope.containsKey(varName)) {
            System.err.println("Error! Circuit " + varName + " not yet defined!");
            return false;
        }

        try {
            if (countExpressionOutputs(ctx.expression()) != 1)
                return false;
            if (index >= currentEvaluationScope.get(varName).getNumberOfArguments())
                return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

        return visitExpression(ctx.expression());
    }

    @Override
    public Boolean visitCreateCircuit(@NotNull BooLeXParser.CreateCircuitContext ctx) {
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

        if (knownCircuits.get(circName) == null) {
            System.err.println("Error! No such circuit " + circName);
            return false;
        }

        if (currentEvaluationScope.get(locName) != null) {
            System.err.println("Error! Redefinition of symbol " + locName);
            return false;
        }

        if (knownCircuits.get(circName).getNumberOfArguments() != nInputs) {
            System.err.println("Error! Mismatched number of inputs to " + locName);
            return false;
        }

        currentEvaluationScope.put(locName, knownCircuits.get(circName));
        return true;
    }

    @Override
    public Boolean visitCircuitIndex(@NotNull BooLeXParser.CircuitIndexContext ctx) {
        if (currentEvaluationScope == null) {
            System.err.println("Error! Cannot index a sub-circuit in a circuit definition.");
            return false;
        }

        String locVar = ctx.Identifier().toString();
        int index = Integer.parseInt(ctx.Integer().toString());

        if (currentEvaluationScope.get(locVar) == null) {
            System.err.println("Error! Undefined symbol " + locVar + ".");
            return false;
        }

        if (currentEvaluationScope.get(locVar).getNumberOfArguments() < index) {
            System.err.println("Error! " + locVar + " does not have " + index + " outputs.");
            return false;
        }

        return true;
    }

    private int factorOutputs(BooLeXParser.FactorContext ctx) throws ParseException {
        if (ctx.circuitCall() != null) {
            TerminalNode identifier = ctx.circuitCall().Identifier();
            String callee = identifier.toString();
            CircuitDeclaration circuit = knownCircuits.get(callee);
            if (circuit == null)
                if (currentEvaluationScope != null)
                    circuit = currentEvaluationScope.get(callee);
            if (circuit == null)
                throw new CircuitDeclarationException(callee, identifier.getSymbol().getLine(), identifier.getSymbol().getCharPositionInLine());
            return circuit.getNumberOfOutputs();
        } else if (ctx.circuitIndex() != null) return 1;
        else if (ctx.expression() != null) return countExpressionOutputs(ctx.expression());
        else if (ctx.Identifier() != null) return 1;
        else if (ctx.BooleanValue() != null) return 1;
        return 1;
    }

    private int countExpressionOutputs(BooLeXParser.ExpressionContext ctx) throws ParseException {
        int number = -1;
        if (ctx.factor() != null)
            number = factorOutputs(ctx.factor());
        else
            for (BooLeXParser.ExpressionContext subExpression : ctx.expression())
                if (number == -1)
                    number = countExpressionOutputs(subExpression);
                else if (countExpressionOutputs(subExpression) != number)
                    throw new BinaryOperationException("Cannot apply binary operation to more than two elements.",
                            subExpression.getStart().getLine(), subExpression.getStart().getCharPositionInLine());

        return number;
    }

    private int expressionListLength(BooLeXParser.ExpressionListContext ctx) throws ParseException {
        int size = 0;

        while (ctx != null) {
            size += countExpressionOutputs(ctx.expression());
            ctx = ctx.expressionList();
        }

        return size;
    }

    private CircuitDeclaration getCircuit(ParserRuleContext ctx) {
        if (ctx.getParent() == null)
            return null;
        else if (ctx instanceof BooLeXParser.CircuitDeclarationContext) {
            BooLeXParser.CircuitDeclarationContext circuitDeclarationContext = (BooLeXParser.CircuitDeclarationContext) ctx;
            CircuitDeclaration ret = knownCircuits.get(circuitDeclarationContext.Identifier().toString());
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
            System.err.println("Error! Circuit " + circuitName + " already exists.");
            return false;
        }

        CircuitDeclaration circuit = new CircuitDeclaration(circuitName);
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

        CircuitDeclaration target = knownCircuits.get(callee);

        if (target == null) {
            System.err.println("Error! Missing declaration of circuit " + callee);
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
        CircuitDeclaration circuit = getCircuit(ctx);
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
            if (countExpressionOutputs(ctx) < 0)
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

        CircuitDeclaration circuit = getCircuit(ctx);
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

        for (ParseTree child : ctx.children) {
            if (!visit(child))
                return false;
        }

        return true;
    }

    @Override
    public Boolean visitFactor(@NotNull BooLeXParser.FactorContext ctx) {
        if (ctx.circuitCall() != null)
            return visitCircuitCall(ctx.circuitCall());
        else if (ctx.circuitIndex() != null)
            return visitCircuitIndex(ctx.circuitIndex());
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
