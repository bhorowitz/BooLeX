package boolex.logic.elements.circuitbuilder;

import boolex.antlr.BooLeXBaseVisitor;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static boolex.antlr.BooLeXParser.*;
import static boolex.helpers.ANTLRHelper.flattenExpressionList;
import static boolex.helpers.ANTLRHelper.flattenIdentifierList;

/**
 * Created by ajr64 on 3/24/14.
 */
public class BLXModelGenerator extends BooLeXBaseVisitor<BLXCircuit> {
    private Map<String, CircuitDeclarationContext> knownCircuits = new HashMap<>();
    private Map<String, BLXCircuit> currentScope;
    private BLXCircuitBuilder circuitBuilder;
    private Boolean defaultValue;

    public BLXModelGenerator(boolean initializeToFalse) {
        this.defaultValue = initializeToFalse ? false : null;
        circuitBuilder = new BLXCircuitBuilder(this.defaultValue);
    }

    @Override
    public BLXCircuit visitCircuitCall(@NotNull CircuitCallContext ctx) {
        BLXCircuit arguments = visitExpressionList(ctx.expressionList());
        BLXCircuit subCircuit = visitCircuitDeclaration(knownCircuits.get(ctx.Identifier().toString()));
        return circuitBuilder.chain(arguments, subCircuit);
    }

    @Override
    public BLXCircuit visitExpressionList(@NotNull ExpressionListContext ctx) {

        List<ExpressionContext> expressionContexts = flattenExpressionList(ctx);
        List<BLXCircuit> circuits = expressionContexts.stream().map(this::visitExpression).collect(java.util.stream.Collectors.toList());
        return circuitBuilder.merge(circuits);
    }

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

    @Override
    public BLXCircuit visitFactor(@NotNull FactorContext ctx) {
        if(ctx.expression() != null)
            return visitExpression(ctx.expression());
        if(ctx.circuitCall() != null)
            return visitCircuitCall(ctx.circuitCall());
        if(ctx.BooleanValue() != null) {
            boolean value = ctx.BooleanValue().toString().equals("true") || ctx.BooleanValue().toString().equals("t");
            BLXCircuit constCircuit = new BLXCircuit("true", defaultValue);
            constCircuit.driveInputByConstant(value, 0);
            return constCircuit;
        }
        if(ctx.Identifier() != null) {
            return getOrCreateInScope(ctx.Identifier().toString());
        }
        System.err.println("Error! Your computer is broken, please buy a new one.");
        return null;
    }

    @Override
    public BLXCircuit visitModule(@NotNull ModuleContext ctx) {
        for (CircuitDeclarationContext declarationContext : ctx.circuitDeclaration())
            knownCircuits.put(declarationContext.Identifier().toString(), declarationContext);
        return visitCircuitDeclaration(knownCircuits.get("main"));
    }

    @Override
    public BLXCircuit visitCircuitDeclaration(@NotNull CircuitDeclarationContext ctx) {
        Map<String, BLXCircuit> previousScope = currentScope;
        currentScope = new HashMap<>();

        List<BLXCircuit> arguments = flattenIdentifierList(ctx.identifierList()).stream().map(this::getOrCreateInScope).collect(java.util.stream.Collectors.toList());

        BLXCircuit argumentsCircuit = circuitBuilder.merge(arguments);

        for (AssignmentContext assignment : ctx.assignment()) {
            List<String> lhsNames = flattenIdentifierList(assignment.identifierList());
            List<BLXCircuit> variablesCircuits = lhsNames.stream().map(this::getOrCreateInScope).collect(java.util.stream.Collectors.toList());

            BLXCircuit valuesCircuit = visitExpressionList(assignment.expressionList());
            circuitBuilder.chain(valuesCircuit, circuitBuilder.merge(variablesCircuits));
        }

        BLXCircuit outputCircuit = visitExpressionList(ctx.outStatement().expressionList());
        BLXCircuit finalCircuit = circuitBuilder.buildCircuit(argumentsCircuit, outputCircuit);

        currentScope = previousScope;
        return finalCircuit;
    }

    private BLXCircuit getOrCreateInScope(String name) {
        if(!currentScope.containsKey(name))
            currentScope.put(name, new BLXCircuit(name, defaultValue));
        return currentScope.get(name);
    }
}
