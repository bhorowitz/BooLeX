package boolex.logic.elements.circuitbuilder;

import boolex.antlr.BooLeXBaseVisitor;
import boolex.logic.elements.core.BLXSocket;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.stream.Collectors;

import static boolex.antlr.BooLeXParser.*;
import static boolex.helpers.ANTLRHelper.flattenExpressionList;
import static boolex.helpers.ANTLRHelper.flattenIdentifierList;

/**
 * Created by ajr64 on 3/24/14.
 */
public class BLXModelGenerator extends BooLeXBaseVisitor<BLXCircuit> {
    Map<String, CircuitDeclarationContext> knownCircuits = new HashMap<>();
    Map<String, BLXCircuit> currentScope;
    boolean initializeToFalse;

    private int nameIncrement = 0;

    private String getNextName() {
        nameIncrement++;
        return "%o" + nameIncrement;
    }

    private String getLastName() {
        return "%o" + nameIncrement;
    }


    public BLXModelGenerator(boolean initializeToFalse) {
        super();
        this.initializeToFalse = initializeToFalse;
    }

    @Override
    public BLXCircuit visitCircuitCall(@NotNull CircuitCallContext ctx) {
        String circuitName = ctx.Identifier().toString();
        CircuitDeclarationContext resolvedCircuit = knownCircuits.get(circuitName);

        List<String> formals = flattenIdentifierList(resolvedCircuit.identifierList());
        List<ExpressionContext> argumentList = flattenExpressionList(ctx.expressionList());

        Map<String, BLXSocket> inputSocketMap = new HashMap<>();

        int i = 0;
        for (ExpressionContext argumentExpression : argumentList) {
            BLXCircuit argument = visitExpression(argumentExpression);
            assert(argument != null);

            for (BLXSocket outputSocket : argument.getMarkedOutputSockets().values()) {
                inputSocketMap.put(formals.get(i), outputSocket);
                i++;
            }
        }

        BLXCircuit subCircuit = visitCircuitDeclaration(resolvedCircuit);
        subCircuit.input(inputSocketMap);
        return subCircuit;
    }

    @Override
    public BLXCircuit visitExpression(@NotNull ExpressionContext ctx) {
        String nameToUnmark = getLastName();
        if(ctx.factor() != null)
            return visitFactor(ctx.factor());
        if(ctx.PostNot() != null || ctx.Not() != null)
            return visitExpression(ctx.expression(0)).not(getNextName()).remarkOutputSocket(nameToUnmark, getLastName());
        if(ctx.And() != null)
            return visitExpression(ctx.expression(0)).and(visitExpression(ctx.expression(1)), getNextName()).remarkOutputSocket(nameToUnmark, getLastName());
        if(ctx.NAnd() != null)
            return visitExpression(ctx.expression(0)).nand(visitExpression(ctx.expression(1)), getNextName()).remarkOutputSocket(nameToUnmark, getLastName());
        if(ctx.Or() != null)
            return visitExpression(ctx.expression(0)).or(visitExpression(ctx.expression(1)), getNextName()).remarkOutputSocket(nameToUnmark, getLastName());
        if(ctx.Nor() != null)
            return visitExpression(ctx.expression(0)).nor(visitExpression(ctx.expression(1)), getNextName()).remarkOutputSocket(nameToUnmark, getLastName());
        if(ctx.Xor() != null)
            return visitExpression(ctx.expression(0)).xor(visitExpression(ctx.expression(1)), getNextName()).remarkOutputSocket(nameToUnmark, getLastName());
        if(ctx.XNor() != null)
            return visitExpression(ctx.expression(0)).xnor(visitExpression(ctx.expression(1)), getNextName()).remarkOutputSocket(nameToUnmark, getLastName());
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
            return new BLXCircuit(getNextName(), initializeToFalse).input(value, getLastName()).markOutputSocket(getLastName());
        }
        if(ctx.Identifier() != null)
            return currentScope.get(ctx.Identifier().toString());
        System.err.println("Error! Your computer is broken, please buy a new one.");
        return null;
    }

    @Override
    public BLXCircuit visitTerminal(@NotNull TerminalNode node) {
        return super.visitTerminal(node);
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

        List<String> formals = flattenIdentifierList(ctx.identifierList());
        formals.forEach(formal -> currentScope.put(formal, new BLXCircuit(formal, initializeToFalse)));

        for (AssignmentContext assignment : ctx.assignment()) {
            List<String> names = flattenIdentifierList(assignment.identifierList());
            int i = 0;
            for (ExpressionContext expression : flattenExpressionList(assignment.expressionList())) {
                BLXCircuit expressionCircuit = visitExpression(expression);
                for (BLXSocket blxSocket : expressionCircuit.getMarkedOutputSockets().values()) {
                    currentScope.put(names.get(i), expressionCircuit);
                    i++;
                }
            }
        }

        List<BLXCircuit> outputs = flattenExpressionList(ctx.outStatement().expressionList())
                .stream().map(this::visitExpression).collect(Collectors.toList());

        BLXCircuit finalCircuit = outputs.get(0);
        outputs.remove(0);

        outputs.forEach(finalCircuit::consume);

        currentScope = previousScope;
        return finalCircuit;
    }
}
