// Generated from BooLeX.g4 by ANTLR 4.2
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link BooLeXParser}.
 */
public interface BooLeXListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link BooLeXParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(@NotNull BooLeXParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(@NotNull BooLeXParser.AssignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#module}.
	 * @param ctx the parse tree
	 */
	void enterModule(@NotNull BooLeXParser.ModuleContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#module}.
	 * @param ctx the parse tree
	 */
	void exitModule(@NotNull BooLeXParser.ModuleContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#booleanXor}.
	 * @param ctx the parse tree
	 */
	void enterBooleanXor(@NotNull BooLeXParser.BooleanXorContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#booleanXor}.
	 * @param ctx the parse tree
	 */
	void exitBooleanXor(@NotNull BooLeXParser.BooleanXorContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(@NotNull BooLeXParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(@NotNull BooLeXParser.ExpressionListContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#booleanPostNot}.
	 * @param ctx the parse tree
	 */
	void enterBooleanPostNot(@NotNull BooLeXParser.BooleanPostNotContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#booleanPostNot}.
	 * @param ctx the parse tree
	 */
	void exitBooleanPostNot(@NotNull BooLeXParser.BooleanPostNotContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#booleanOr}.
	 * @param ctx the parse tree
	 */
	void enterBooleanOr(@NotNull BooLeXParser.BooleanOrContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#booleanOr}.
	 * @param ctx the parse tree
	 */
	void exitBooleanOr(@NotNull BooLeXParser.BooleanOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#booleanPreNot}.
	 * @param ctx the parse tree
	 */
	void enterBooleanPreNot(@NotNull BooLeXParser.BooleanPreNotContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#booleanPreNot}.
	 * @param ctx the parse tree
	 */
	void exitBooleanPreNot(@NotNull BooLeXParser.BooleanPreNotContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void enterBooleanExpression(@NotNull BooLeXParser.BooleanExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#booleanExpression}.
	 * @param ctx the parse tree
	 */
	void exitBooleanExpression(@NotNull BooLeXParser.BooleanExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#booleanFactor}.
	 * @param ctx the parse tree
	 */
	void enterBooleanFactor(@NotNull BooLeXParser.BooleanFactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#booleanFactor}.
	 * @param ctx the parse tree
	 */
	void exitBooleanFactor(@NotNull BooLeXParser.BooleanFactorContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(@NotNull BooLeXParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(@NotNull BooLeXParser.IdentifierListContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#circuitdeclaration}.
	 * @param ctx the parse tree
	 */
	void enterCircuitdeclaration(@NotNull BooLeXParser.CircuitdeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#circuitdeclaration}.
	 * @param ctx the parse tree
	 */
	void exitCircuitdeclaration(@NotNull BooLeXParser.CircuitdeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#circuitCall}.
	 * @param ctx the parse tree
	 */
	void enterCircuitCall(@NotNull BooLeXParser.CircuitCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#circuitCall}.
	 * @param ctx the parse tree
	 */
	void exitCircuitCall(@NotNull BooLeXParser.CircuitCallContext ctx);

	/**
	 * Enter a parse tree produced by {@link BooLeXParser#out}.
	 * @param ctx the parse tree
	 */
	void enterOut(@NotNull BooLeXParser.OutContext ctx);
	/**
	 * Exit a parse tree produced by {@link BooLeXParser#out}.
	 * @param ctx the parse tree
	 */
	void exitOut(@NotNull BooLeXParser.OutContext ctx);
}