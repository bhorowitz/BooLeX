package boolex.logic.circuitbuilder;

import boolex.antlr.BooLeXBaseVisitor;
import boolex.logic.elements.core.BLXSocket;
import boolex.logic.elements.signals.BLXSignalReceiver;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static boolex.antlr.BooLeXParser.*;
import static boolex.helpers.ANTLRHelper.flattenExpressionList;
import static boolex.helpers.ANTLRHelper.flattenIdentifierList;

/**
 * The BLXModelGenerator is a tree-traversal-based translator from parse trees to
 * our semantic model (c.f. Martin Fowler), the BLXCircuit.
 *
 * @author Alex Reinking and Dani Dickstein
 */
public class BLXModelGenerator extends BooLeXBaseVisitor<BLXCircuit> {
    private Map<String, CircuitDeclarationContext> knownCircuits = new HashMap<>();
    private Map<String, BLXCircuit> currentScope;
    private BLXCircuitBuilder circuitBuilder;
    private Boolean defaultValue;
    private boolean inTopLevel;

    /**
     *
     * @param initializeToFalse Should all the sockets be initialized to false by default? (otherwise null)
     */
    public BLXModelGenerator(boolean initializeToFalse) {
        defaultValue = initializeToFalse ? false : null;
        circuitBuilder = new BLXCircuitBuilder(this.defaultValue);
        inTopLevel = true;
    }

    /**
     * Converts a call to a sub-circuit to an actual circuit
     * @param ctx The parse tree call to the circuit
     * @return a circuit whose inputs are the inputs of the subcircuit's evaluated arguments
     *         and whose outputs are the subcircuit's outputs
     */
    @Override
    public BLXCircuit visitCircuitCall(@NotNull CircuitCallContext ctx) {
        boolean wasInTopLevel = inTopLevel;
        inTopLevel = false;
        BLXCircuit arguments = visitExpressionList(ctx.expressionList());
        BLXCircuit subCircuit = visitCircuitDeclaration(knownCircuits.get(ctx.Identifier().toString()));
        inTopLevel = wasInTopLevel;
        return circuitBuilder.chain(arguments, subCircuit);
    }

    /**
     * Build up a circuit out of a list of expressions - collects the inputs and outputs in-order
     * @param ctx the parsed expression list
     * @return the circuit
     */
    @Override
    public BLXCircuit visitExpressionList(@NotNull ExpressionListContext ctx) {
        List<ExpressionContext> expressionContexts = flattenExpressionList(ctx);
        List<BLXCircuit> circuits = expressionContexts.stream().map(this::visitExpression).collect(java.util.stream.Collectors.toList());
        return circuitBuilder.merge(circuits);
    }

    /**
     * Construct a circiut from a boolean-arithmetic expression
     * @param ctx the combinational expression
     * @return the circuit
     */
    @Override
    public BLXCircuit visitExpression(@NotNull ExpressionContext ctx) {
        if(ctx.factor() != null)
            return visitFactor(ctx.factor());

        ExpressionContext lhs = ctx.expression(0);

        if(ctx.PostNot() != null || ctx.Not() != null)
            return circuitBuilder.not(visitExpression(lhs));

        ExpressionContext rhs = ctx.expression(1);
        if(ctx.And() != null)
            return circuitBuilder.and(visitExpression(lhs), visitExpression(rhs));
        if(ctx.NAnd() != null)
            return circuitBuilder.nand(visitExpression(lhs), visitExpression(rhs));
        if(ctx.Or() != null)
            return circuitBuilder.or(visitExpression(lhs), visitExpression(rhs));
        if(ctx.Nor() != null)
            return circuitBuilder.nor(visitExpression(lhs), visitExpression(rhs));
        if(ctx.Xor() != null)
            return circuitBuilder.xor(visitExpression(lhs), visitExpression(rhs));
        if(ctx.XNor() != null)
            return circuitBuilder.xnor(visitExpression(lhs), visitExpression(rhs));

        System.err.println("Error! Your computer is broken, please buy a new one.");
        return null;
    }

    /**
     * Create a circuit representing an atom (ie. true, false, a named value)
     * @param ctx the parsed factor
     * @return the circuit
     */
    @Override
    public BLXCircuit visitFactor(@NotNull FactorContext ctx) {
        if(ctx.expression() != null)
            return visitExpression(ctx.expression());
        if(ctx.circuitCall() != null)
            return visitCircuitCall(ctx.circuitCall());
        if(ctx.BooleanValue() != null) {
            boolean value = ctx.BooleanValue().toString().equals("true");
            BLXCircuit constCircuit = new BLXCircuit(value ? "true" : "false", defaultValue);
            constCircuit.driveInputByConstant(value, 0);
            return constCircuit;
        }
        if(ctx.Identifier() != null) {
            return getOrCreateInScope(ctx.Identifier().toString());
        }
        System.err.println("Error! Your computer is broken, please buy a new one.");
        return null;
    }

    /**
     * Top-level parser
     * @param ctx the top-level parsed module
     * @return the BLXCircuit for the circuit named "main"
     */
    @Override
    public BLXCircuit visitModule(@NotNull ModuleContext ctx) {
        for (CircuitDeclarationContext declarationContext : ctx.circuitDeclaration())
            knownCircuits.put(declarationContext.Identifier().toString(), declarationContext);
        return visitCircuitDeclaration(knownCircuits.get("main"));
    }

    /**
     * Creates a whole circuit for a given declaration
     * @param ctx the parsed circuit declaration
     * @return the circuit
     */
    @Override
    public BLXCircuit visitCircuitDeclaration(@NotNull CircuitDeclarationContext ctx) {
        Map<String, BLXCircuit> previousScope = currentScope;
        currentScope = new HashMap<>();

        List<BLXCircuit> arguments = flattenIdentifierList(ctx.identifierList()).stream().map(this::getOrCreateInScope).collect(Collectors.toList());

        BLXCircuit argumentsCircuit = circuitBuilder.merge(arguments);

        Set<BLXSignalReceiver> trueTargets = new HashSet<>();
        Set<BLXSignalReceiver> falseTargets = new HashSet<>();

        for (AssignmentContext assignment : ctx.assignment()) {
            List<String> lhsNames = flattenIdentifierList(assignment.identifierList());
            List<BLXCircuit> variablesCircuits = lhsNames.stream().map(this::getOrCreateInScope).collect(Collectors.toList());

            BLXCircuit valuesCircuit = visitExpressionList(assignment.expressionList());
            BLXCircuit assignmentCircuit = circuitBuilder.chain(valuesCircuit, circuitBuilder.merge(variablesCircuits));
            trueTargets.addAll(assignmentCircuit.getTrueSocket().getTargets());
            falseTargets.addAll(assignmentCircuit.getFalseSocket().getTargets());
        }

        BLXCircuit outputCircuit = visitExpressionList(ctx.outStatement().expressionList());

        trueTargets.addAll(outputCircuit.getTrueSocket().getTargets());
        falseTargets.addAll(outputCircuit.getFalseSocket().getTargets());

        List<BLXSocket> outputSockets = outputCircuit.getOutputSockets();

        // automatically assign names if they aren't given already.
        int num = outputSockets.size();
        for (BLXSocket blxSocket : outputSockets) {
            if(inTopLevel && (blxSocket.getId() == null || blxSocket.getId().equals("")))
                blxSocket.setId("%o" + num);
            num--;
        }

        BLXCircuit finalCircuit = circuitBuilder.buildCircuit(argumentsCircuit, outputCircuit, trueTargets, falseTargets);

        currentScope = previousScope;
        return finalCircuit;
    }

    /**
     * Check to see if a symbol with the given name exists. If so, fetch its circuit from the current
     * scope. If not, create and return an empty circuit.
     * @param name the name of the symbol to look up
     * @return the corresponding circuit (might be new)
     */
    private BLXCircuit getOrCreateInScope(String name) {
        if(!currentScope.containsKey(name))
            currentScope.put(name, new BLXCircuit((inTopLevel) ? name : null, defaultValue));
        return currentScope.get(name);
    }
}
