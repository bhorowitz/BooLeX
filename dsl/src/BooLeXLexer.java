// Generated from BooLeX.g4 by ANTLR 4.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class BooLeXLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, Circuit=2, Out=3, End=4, And=5, Or=6, Not=7, PostNot=8, Xor=9, 
		NAnd=10, Nor=11, XNor=12, BooleanValue=13, Assign=14, LeftParen=15, RightParen=16, 
		LeftBracket=17, RightBracket=18, Identifier=19, Whitespace=20, Newline=21, 
		LineComment=22;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "'circuit'", "'out'", "'end'", "And", "Or", "Not", "'''", "Xor", 
		"'nand'", "'nor'", "'xnor'", "BooleanValue", "'='", "'('", "')'", "'['", 
		"']'", "Identifier", "Whitespace", "Newline", "LineComment"
	};
	public static final String[] ruleNames = {
		"T__0", "Circuit", "Out", "End", "And", "Or", "Not", "PostNot", "Xor", 
		"NAnd", "Nor", "XNor", "BooleanValue", "Assign", "LeftParen", "RightParen", 
		"LeftBracket", "RightBracket", "Identifier", "Whitespace", "Newline", 
		"LineComment"
	};


	public BooLeXLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "BooLeX.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\30\u009e\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6"+
		"\3\6\5\6F\n\6\3\7\3\7\3\7\5\7K\n\7\3\b\3\b\3\b\3\b\5\bQ\n\b\3\t\3\t\3"+
		"\n\3\n\3\n\3\n\5\nY\n\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\r\3"+
		"\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16"+
		"s\n\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\7\24"+
		"\u0081\n\24\f\24\16\24\u0084\13\24\3\25\6\25\u0087\n\25\r\25\16\25\u0088"+
		"\3\25\3\25\3\26\3\26\5\26\u008f\n\26\3\26\5\26\u0092\n\26\3\26\3\26\3"+
		"\27\3\27\7\27\u0098\n\27\f\27\16\27\u009b\13\27\3\27\3\27\2\2\30\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\24\'\25)\26+\27-\30\3\2\6\5\2C\\aac|\6\2\62;C\\aac|\4\2\13\13"+
		"\"\"\4\2\f\f\17\17\u00a8\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2"+
		"\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\3/\3\2\2\2\5\61\3\2\2\2\79\3\2\2\2\t=\3\2\2\2\13E\3\2\2"+
		"\2\rJ\3\2\2\2\17P\3\2\2\2\21R\3\2\2\2\23X\3\2\2\2\25Z\3\2\2\2\27_\3\2"+
		"\2\2\31c\3\2\2\2\33r\3\2\2\2\35t\3\2\2\2\37v\3\2\2\2!x\3\2\2\2#z\3\2\2"+
		"\2%|\3\2\2\2\'~\3\2\2\2)\u0086\3\2\2\2+\u0091\3\2\2\2-\u0095\3\2\2\2/"+
		"\60\7.\2\2\60\4\3\2\2\2\61\62\7e\2\2\62\63\7k\2\2\63\64\7t\2\2\64\65\7"+
		"e\2\2\65\66\7w\2\2\66\67\7k\2\2\678\7v\2\28\6\3\2\2\29:\7q\2\2:;\7w\2"+
		"\2;<\7v\2\2<\b\3\2\2\2=>\7g\2\2>?\7p\2\2?@\7f\2\2@\n\3\2\2\2AB\7c\2\2"+
		"BC\7p\2\2CF\7f\2\2DF\7,\2\2EA\3\2\2\2ED\3\2\2\2F\f\3\2\2\2GH\7q\2\2HK"+
		"\7t\2\2IK\7-\2\2JG\3\2\2\2JI\3\2\2\2K\16\3\2\2\2LM\7p\2\2MN\7q\2\2NQ\7"+
		"v\2\2OQ\7/\2\2PL\3\2\2\2PO\3\2\2\2Q\20\3\2\2\2RS\7)\2\2S\22\3\2\2\2TU"+
		"\7z\2\2UV\7q\2\2VY\7t\2\2WY\7`\2\2XT\3\2\2\2XW\3\2\2\2Y\24\3\2\2\2Z[\7"+
		"p\2\2[\\\7c\2\2\\]\7p\2\2]^\7f\2\2^\26\3\2\2\2_`\7p\2\2`a\7q\2\2ab\7t"+
		"\2\2b\30\3\2\2\2cd\7z\2\2de\7p\2\2ef\7q\2\2fg\7t\2\2g\32\3\2\2\2hi\7v"+
		"\2\2ij\7t\2\2jk\7w\2\2ks\7g\2\2lm\7h\2\2mn\7c\2\2no\7n\2\2op\7u\2\2ps"+
		"\7g\2\2qs\4\62\63\2rh\3\2\2\2rl\3\2\2\2rq\3\2\2\2s\34\3\2\2\2tu\7?\2\2"+
		"u\36\3\2\2\2vw\7*\2\2w \3\2\2\2xy\7+\2\2y\"\3\2\2\2z{\7]\2\2{$\3\2\2\2"+
		"|}\7_\2\2}&\3\2\2\2~\u0082\t\2\2\2\177\u0081\t\3\2\2\u0080\177\3\2\2\2"+
		"\u0081\u0084\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083(\3"+
		"\2\2\2\u0084\u0082\3\2\2\2\u0085\u0087\t\4\2\2\u0086\u0085\3\2\2\2\u0087"+
		"\u0088\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\3\2"+
		"\2\2\u008a\u008b\b\25\2\2\u008b*\3\2\2\2\u008c\u008e\7\17\2\2\u008d\u008f"+
		"\7\f\2\2\u008e\u008d\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0092\3\2\2\2\u0090"+
		"\u0092\7\f\2\2\u0091\u008c\3\2\2\2\u0091\u0090\3\2\2\2\u0092\u0093\3\2"+
		"\2\2\u0093\u0094\b\26\2\2\u0094,\3\2\2\2\u0095\u0099\7%\2\2\u0096\u0098"+
		"\n\5\2\2\u0097\u0096\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u0097\3\2\2\2\u0099"+
		"\u009a\3\2\2\2\u009a\u009c\3\2\2\2\u009b\u0099\3\2\2\2\u009c\u009d\b\27"+
		"\2\2\u009d.\3\2\2\2\r\2EJPXr\u0082\u0088\u008e\u0091\u0099\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}