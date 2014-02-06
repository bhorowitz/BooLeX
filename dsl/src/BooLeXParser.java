// Generated from BooLeX.g4 by ANTLR 4.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BooLeXParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, Circuit=2, Out=3, End=4, And=5, Or=6, Not=7, PostNot=8, Xor=9, 
		NAnd=10, Nor=11, XNor=12, BooleanValue=13, Assign=14, LeftParen=15, RightParen=16, 
		LeftBracket=17, RightBracket=18, Identifier=19, Whitespace=20, Newline=21, 
		LineComment=22;
	public static final String[] tokenNames = {
		"<INVALID>", "','", "'circuit'", "'out'", "'end'", "And", "Or", "Not", 
		"'''", "Xor", "'nand'", "'nor'", "'xnor'", "BooleanValue", "'='", "'('", 
		"')'", "'['", "']'", "Identifier", "Whitespace", "Newline", "LineComment"
	};
	public static final int
		RULE_module = 0, RULE_circuitdeclaration = 1, RULE_identifierList = 2, 
		RULE_expressionList = 3, RULE_assignment = 4, RULE_out = 5, RULE_circuitCall = 6, 
		RULE_booleanFactor = 7, RULE_booleanPostNot = 8, RULE_booleanPreNot = 9, 
		RULE_booleanOr = 10, RULE_booleanXor = 11, RULE_booleanExpression = 12;
	public static final String[] ruleNames = {
		"module", "circuitdeclaration", "identifierList", "expressionList", "assignment", 
		"out", "circuitCall", "booleanFactor", "booleanPostNot", "booleanPreNot", 
		"booleanOr", "booleanXor", "booleanExpression"
	};

	@Override
	public String getGrammarFileName() { return "BooLeX.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public BooLeXParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ModuleContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(BooLeXParser.EOF, 0); }
		public CircuitdeclarationContext circuitdeclaration(int i) {
			return getRuleContext(CircuitdeclarationContext.class,i);
		}
		public List<CircuitdeclarationContext> circuitdeclaration() {
			return getRuleContexts(CircuitdeclarationContext.class);
		}
		public ModuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_module; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterModule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitModule(this);
		}
	}

	public final ModuleContext module() throws RecognitionException {
		ModuleContext _localctx = new ModuleContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_module);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Circuit) {
				{
				{
				setState(26); circuitdeclaration();
				}
				}
				setState(31);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(32); match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CircuitdeclarationContext extends ParserRuleContext {
		public OutContext out() {
			return getRuleContext(OutContext.class,0);
		}
		public AssignmentContext assignment(int i) {
			return getRuleContext(AssignmentContext.class,i);
		}
		public TerminalNode LeftParen() { return getToken(BooLeXParser.LeftParen, 0); }
		public List<AssignmentContext> assignment() {
			return getRuleContexts(AssignmentContext.class);
		}
		public TerminalNode End() { return getToken(BooLeXParser.End, 0); }
		public TerminalNode RightParen() { return getToken(BooLeXParser.RightParen, 0); }
		public TerminalNode Circuit() { return getToken(BooLeXParser.Circuit, 0); }
		public TerminalNode Identifier() { return getToken(BooLeXParser.Identifier, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public CircuitdeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_circuitdeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterCircuitdeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitCircuitdeclaration(this);
		}
	}

	public final CircuitdeclarationContext circuitdeclaration() throws RecognitionException {
		CircuitdeclarationContext _localctx = new CircuitdeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_circuitdeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34); match(Circuit);
			setState(35); match(Identifier);
			setState(36); match(LeftParen);
			setState(38);
			_la = _input.LA(1);
			if (_la==Identifier) {
				{
				setState(37); identifierList(0);
				}
			}

			setState(40); match(RightParen);
			setState(44);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Identifier) {
				{
				{
				setState(41); assignment();
				}
				}
				setState(46);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(47); out();
			setState(48); match(End);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierListContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(BooLeXParser.Identifier, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public IdentifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterIdentifierList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitIdentifierList(this);
		}
	}

	public final IdentifierListContext identifierList() throws RecognitionException {
		return identifierList(0);
	}

	private IdentifierListContext identifierList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		IdentifierListContext _localctx = new IdentifierListContext(_ctx, _parentState);
		IdentifierListContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_identifierList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(51); match(Identifier);
			}
			_ctx.stop = _input.LT(-1);
			setState(58);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new IdentifierListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_identifierList);
					setState(53);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(54); match(1);
					setState(55); match(Identifier);
					}
					} 
				}
				setState(60);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitExpressionList(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		return expressionList(0);
	}

	private ExpressionListContext expressionList(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, _parentState);
		ExpressionListContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_expressionList, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(62); booleanExpression();
			}
			_ctx.stop = _input.LT(-1);
			setState(69);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpressionListContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expressionList);
					setState(64);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(65); match(1);
					setState(66); booleanExpression();
					}
					} 
				}
				setState(71);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public TerminalNode Assign() { return getToken(BooLeXParser.Assign, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitAssignment(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72); identifierList(0);
			setState(73); match(Assign);
			setState(74); booleanExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode Out() { return getToken(BooLeXParser.Out, 0); }
		public OutContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_out; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterOut(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitOut(this);
		}
	}

	public final OutContext out() throws RecognitionException {
		OutContext _localctx = new OutContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_out);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76); match(Out);
			setState(77); expressionList(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CircuitCallContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(BooLeXParser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(BooLeXParser.RightParen, 0); }
		public TerminalNode Identifier() { return getToken(BooLeXParser.Identifier, 0); }
		public IdentifierListContext identifierList() {
			return getRuleContext(IdentifierListContext.class,0);
		}
		public CircuitCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_circuitCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterCircuitCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitCircuitCall(this);
		}
	}

	public final CircuitCallContext circuitCall() throws RecognitionException {
		CircuitCallContext _localctx = new CircuitCallContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_circuitCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79); match(Identifier);
			setState(80); match(LeftParen);
			setState(82);
			_la = _input.LA(1);
			if (_la==Identifier) {
				{
				setState(81); identifierList(0);
				}
			}

			setState(84); match(RightParen);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanFactorContext extends ParserRuleContext {
		public CircuitCallContext circuitCall() {
			return getRuleContext(CircuitCallContext.class,0);
		}
		public TerminalNode BooleanValue() { return getToken(BooLeXParser.BooleanValue, 0); }
		public BooleanExpressionContext booleanExpression() {
			return getRuleContext(BooleanExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(BooLeXParser.Identifier, 0); }
		public BooleanFactorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanFactor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterBooleanFactor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitBooleanFactor(this);
		}
	}

	public final BooleanFactorContext booleanFactor() throws RecognitionException {
		BooleanFactorContext _localctx = new BooleanFactorContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_booleanFactor);
		try {
			setState(93);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(86); circuitCall();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(87); match(Identifier);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(88); match(BooleanValue);
				}
				break;

			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(89); match(LeftParen);
				setState(90); booleanExpression();
				setState(91); match(RightParen);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanPostNotContext extends ParserRuleContext {
		public TerminalNode PostNot() { return getToken(BooLeXParser.PostNot, 0); }
		public BooleanFactorContext booleanFactor() {
			return getRuleContext(BooleanFactorContext.class,0);
		}
		public BooleanPostNotContext booleanPostNot() {
			return getRuleContext(BooleanPostNotContext.class,0);
		}
		public BooleanPostNotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanPostNot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterBooleanPostNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitBooleanPostNot(this);
		}
	}

	public final BooleanPostNotContext booleanPostNot() throws RecognitionException {
		return booleanPostNot(0);
	}

	private BooleanPostNotContext booleanPostNot(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		BooleanPostNotContext _localctx = new BooleanPostNotContext(_ctx, _parentState);
		BooleanPostNotContext _prevctx = _localctx;
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_booleanPostNot, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(96); booleanFactor();
			}
			_ctx.stop = _input.LT(-1);
			setState(102);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BooleanPostNotContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_booleanPostNot);
					setState(98);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(99); match(PostNot);
					}
					} 
				}
				setState(104);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class BooleanPreNotContext extends ParserRuleContext {
		public BooleanPreNotContext booleanPreNot() {
			return getRuleContext(BooleanPreNotContext.class,0);
		}
		public TerminalNode Not() { return getToken(BooLeXParser.Not, 0); }
		public BooleanPostNotContext booleanPostNot() {
			return getRuleContext(BooleanPostNotContext.class,0);
		}
		public BooleanPreNotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanPreNot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterBooleanPreNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitBooleanPreNot(this);
		}
	}

	public final BooleanPreNotContext booleanPreNot() throws RecognitionException {
		BooleanPreNotContext _localctx = new BooleanPreNotContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_booleanPreNot);
		try {
			setState(108);
			switch (_input.LA(1)) {
			case BooleanValue:
			case LeftParen:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(105); booleanPostNot(0);
				}
				break;
			case Not:
				enterOuterAlt(_localctx, 2);
				{
				setState(106); match(Not);
				setState(107); booleanPreNot();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanOrContext extends ParserRuleContext {
		public BooleanFactorContext booleanFactor() {
			return getRuleContext(BooleanFactorContext.class,0);
		}
		public BooleanPreNotContext booleanPreNot(int i) {
			return getRuleContext(BooleanPreNotContext.class,i);
		}
		public List<BooleanPreNotContext> booleanPreNot() {
			return getRuleContexts(BooleanPreNotContext.class);
		}
		public TerminalNode Nor() { return getToken(BooLeXParser.Nor, 0); }
		public TerminalNode Or() { return getToken(BooLeXParser.Or, 0); }
		public BooleanOrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanOr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterBooleanOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitBooleanOr(this);
		}
	}

	public final BooleanOrContext booleanOr() throws RecognitionException {
		BooleanOrContext _localctx = new BooleanOrContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_booleanOr);
		try {
			setState(119);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(110); booleanPreNot();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(111); booleanPreNot();
				setState(112); match(Or);
				setState(113); booleanPreNot();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(115); booleanPreNot();
				setState(116); match(Nor);
				setState(117); booleanFactor();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanXorContext extends ParserRuleContext {
		public TerminalNode Xor() { return getToken(BooLeXParser.Xor, 0); }
		public List<BooleanOrContext> booleanOr() {
			return getRuleContexts(BooleanOrContext.class);
		}
		public BooleanOrContext booleanOr(int i) {
			return getRuleContext(BooleanOrContext.class,i);
		}
		public TerminalNode XNor() { return getToken(BooLeXParser.XNor, 0); }
		public BooleanXorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanXor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterBooleanXor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitBooleanXor(this);
		}
	}

	public final BooleanXorContext booleanXor() throws RecognitionException {
		BooleanXorContext _localctx = new BooleanXorContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_booleanXor);
		try {
			setState(130);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(121); booleanOr();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(122); booleanOr();
				setState(123); match(Xor);
				setState(124); booleanOr();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(126); booleanOr();
				setState(127); match(XNor);
				setState(128); booleanOr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanExpressionContext extends ParserRuleContext {
		public TerminalNode And() { return getToken(BooLeXParser.And, 0); }
		public List<BooleanXorContext> booleanXor() {
			return getRuleContexts(BooleanXorContext.class);
		}
		public BooleanXorContext booleanXor(int i) {
			return getRuleContext(BooleanXorContext.class,i);
		}
		public TerminalNode NAnd() { return getToken(BooLeXParser.NAnd, 0); }
		public BooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).enterBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof BooLeXListener ) ((BooLeXListener)listener).exitBooleanExpression(this);
		}
	}

	public final BooleanExpressionContext booleanExpression() throws RecognitionException {
		BooleanExpressionContext _localctx = new BooleanExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_booleanExpression);
		try {
			setState(141);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(132); booleanXor();
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(133); booleanXor();
				setState(134); match(And);
				setState(135); booleanXor();
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(137); booleanXor();
				setState(138); match(NAnd);
				setState(139); booleanXor();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2: return identifierList_sempred((IdentifierListContext)_localctx, predIndex);

		case 3: return expressionList_sempred((ExpressionListContext)_localctx, predIndex);

		case 8: return booleanPostNot_sempred((BooleanPostNotContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expressionList_sempred(ExpressionListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1: return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean booleanPostNot_sempred(BooleanPostNotContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2: return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean identifierList_sempred(IdentifierListContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\30\u0092\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\7\2\36\n\2\f\2\16\2!\13\2\3\2\3\2"+
		"\3\3\3\3\3\3\3\3\5\3)\n\3\3\3\3\3\7\3-\n\3\f\3\16\3\60\13\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4;\n\4\f\4\16\4>\13\4\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\7\5F\n\5\f\5\16\5I\13\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\5"+
		"\bU\n\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t`\n\t\3\n\3\n\3\n\3\n\3"+
		"\n\7\ng\n\n\f\n\16\nj\13\n\3\13\3\13\3\13\5\13o\n\13\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\5\fz\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u0085"+
		"\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\u0090\n\16\3\16"+
		"\2\5\6\b\22\17\2\4\6\b\n\f\16\20\22\24\26\30\32\2\2\u0095\2\37\3\2\2\2"+
		"\4$\3\2\2\2\6\64\3\2\2\2\b?\3\2\2\2\nJ\3\2\2\2\fN\3\2\2\2\16Q\3\2\2\2"+
		"\20_\3\2\2\2\22a\3\2\2\2\24n\3\2\2\2\26y\3\2\2\2\30\u0084\3\2\2\2\32\u008f"+
		"\3\2\2\2\34\36\5\4\3\2\35\34\3\2\2\2\36!\3\2\2\2\37\35\3\2\2\2\37 \3\2"+
		"\2\2 \"\3\2\2\2!\37\3\2\2\2\"#\7\2\2\3#\3\3\2\2\2$%\7\4\2\2%&\7\25\2\2"+
		"&(\7\21\2\2\')\5\6\4\2(\'\3\2\2\2()\3\2\2\2)*\3\2\2\2*.\7\22\2\2+-\5\n"+
		"\6\2,+\3\2\2\2-\60\3\2\2\2.,\3\2\2\2./\3\2\2\2/\61\3\2\2\2\60.\3\2\2\2"+
		"\61\62\5\f\7\2\62\63\7\6\2\2\63\5\3\2\2\2\64\65\b\4\1\2\65\66\7\25\2\2"+
		"\66<\3\2\2\2\678\f\3\2\289\7\3\2\29;\7\25\2\2:\67\3\2\2\2;>\3\2\2\2<:"+
		"\3\2\2\2<=\3\2\2\2=\7\3\2\2\2><\3\2\2\2?@\b\5\1\2@A\5\32\16\2AG\3\2\2"+
		"\2BC\f\3\2\2CD\7\3\2\2DF\5\32\16\2EB\3\2\2\2FI\3\2\2\2GE\3\2\2\2GH\3\2"+
		"\2\2H\t\3\2\2\2IG\3\2\2\2JK\5\6\4\2KL\7\20\2\2LM\5\32\16\2M\13\3\2\2\2"+
		"NO\7\5\2\2OP\5\b\5\2P\r\3\2\2\2QR\7\25\2\2RT\7\21\2\2SU\5\6\4\2TS\3\2"+
		"\2\2TU\3\2\2\2UV\3\2\2\2VW\7\22\2\2W\17\3\2\2\2X`\5\16\b\2Y`\7\25\2\2"+
		"Z`\7\17\2\2[\\\7\21\2\2\\]\5\32\16\2]^\7\22\2\2^`\3\2\2\2_X\3\2\2\2_Y"+
		"\3\2\2\2_Z\3\2\2\2_[\3\2\2\2`\21\3\2\2\2ab\b\n\1\2bc\5\20\t\2ch\3\2\2"+
		"\2de\f\3\2\2eg\7\n\2\2fd\3\2\2\2gj\3\2\2\2hf\3\2\2\2hi\3\2\2\2i\23\3\2"+
		"\2\2jh\3\2\2\2ko\5\22\n\2lm\7\t\2\2mo\5\24\13\2nk\3\2\2\2nl\3\2\2\2o\25"+
		"\3\2\2\2pz\5\24\13\2qr\5\24\13\2rs\7\b\2\2st\5\24\13\2tz\3\2\2\2uv\5\24"+
		"\13\2vw\7\r\2\2wx\5\20\t\2xz\3\2\2\2yp\3\2\2\2yq\3\2\2\2yu\3\2\2\2z\27"+
		"\3\2\2\2{\u0085\5\26\f\2|}\5\26\f\2}~\7\13\2\2~\177\5\26\f\2\177\u0085"+
		"\3\2\2\2\u0080\u0081\5\26\f\2\u0081\u0082\7\16\2\2\u0082\u0083\5\26\f"+
		"\2\u0083\u0085\3\2\2\2\u0084{\3\2\2\2\u0084|\3\2\2\2\u0084\u0080\3\2\2"+
		"\2\u0085\31\3\2\2\2\u0086\u0090\5\30\r\2\u0087\u0088\5\30\r\2\u0088\u0089"+
		"\7\7\2\2\u0089\u008a\5\30\r\2\u008a\u0090\3\2\2\2\u008b\u008c\5\30\r\2"+
		"\u008c\u008d\7\f\2\2\u008d\u008e\5\30\r\2\u008e\u0090\3\2\2\2\u008f\u0086"+
		"\3\2\2\2\u008f\u0087\3\2\2\2\u008f\u008b\3\2\2\2\u0090\33\3\2\2\2\16\37"+
		"(.<GT_hny\u0084\u008f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}