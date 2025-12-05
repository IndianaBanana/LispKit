// Generated from java-escape by ANTLR 4.11.1

    package org.banana.grammar;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class LispKitLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		QUOTE=1, CAR=2, CDR=3, CONS=4, ATOM=5, ADD=6, SUB=7, MUL=8, DIV=9, REM=10, 
		LEQ=11, EQUAL=12, COND=13, LAMBDA=14, LET=15, LETREC=16, LPAREN=17, RPAREN=18, 
		NUMBER=19, IDENT=20, WS=21;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"QUOTE", "CAR", "CDR", "CONS", "ATOM", "ADD", "SUB", "MUL", "DIV", "REM", 
			"LEQ", "EQUAL", "COND", "LAMBDA", "LET", "LETREC", "LPAREN", "RPAREN", 
			"NUMBER", "IDENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "QUOTE", "CAR", "CDR", "CONS", "ATOM", "ADD", "SUB", "MUL", "DIV", 
			"REM", "LEQ", "EQUAL", "COND", "LAMBDA", "LET", "LETREC", "LPAREN", "RPAREN", 
			"NUMBER", "IDENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LispKitLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LispKit.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0015\u0092\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0003\u0012~\b\u0012\u0001\u0012\u0004\u0012"+
		"\u0081\b\u0012\u000b\u0012\f\u0012\u0082\u0001\u0013\u0001\u0013\u0005"+
		"\u0013\u0087\b\u0013\n\u0013\f\u0013\u008a\t\u0013\u0001\u0014\u0004\u0014"+
		"\u008d\b\u0014\u000b\u0014\f\u0014\u008e\u0001\u0014\u0001\u0014\u0000"+
		"\u0000\u0015\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b"+
		"\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015\u0001"+
		"\u0000\u0014\u0002\u0000QQqq\u0002\u0000UUuu\u0002\u0000OOoo\u0002\u0000"+
		"TTtt\u0002\u0000EEee\u0002\u0000CCcc\u0002\u0000AAaa\u0002\u0000RRrr\u0002"+
		"\u0000DDdd\u0002\u0000NNnn\u0002\u0000SSss\u0002\u0000MMmm\u0002\u0000"+
		"BBbb\u0002\u0000LLll\u0002\u0000IIii\u0002\u0000VVvv\u0001\u000009\u0002"+
		"\u0000AZaz\u0003\u000009AZaz\u0003\u0000\t\n\r\r  \u0095\u0000\u0001\u0001"+
		"\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"+
		"\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000"+
		"\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"+
		"\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"+
		"\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"+
		"\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000"+
		"\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000"+
		"\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000"+
		"\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'"+
		"\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0001+\u0001\u0000"+
		"\u0000\u0000\u00031\u0001\u0000\u0000\u0000\u00055\u0001\u0000\u0000\u0000"+
		"\u00079\u0001\u0000\u0000\u0000\t>\u0001\u0000\u0000\u0000\u000bC\u0001"+
		"\u0000\u0000\u0000\rG\u0001\u0000\u0000\u0000\u000fK\u0001\u0000\u0000"+
		"\u0000\u0011O\u0001\u0000\u0000\u0000\u0013S\u0001\u0000\u0000\u0000\u0015"+
		"W\u0001\u0000\u0000\u0000\u0017[\u0001\u0000\u0000\u0000\u0019a\u0001"+
		"\u0000\u0000\u0000\u001bf\u0001\u0000\u0000\u0000\u001dm\u0001\u0000\u0000"+
		"\u0000\u001fq\u0001\u0000\u0000\u0000!x\u0001\u0000\u0000\u0000#z\u0001"+
		"\u0000\u0000\u0000%}\u0001\u0000\u0000\u0000\'\u0084\u0001\u0000\u0000"+
		"\u0000)\u008c\u0001\u0000\u0000\u0000+,\u0007\u0000\u0000\u0000,-\u0007"+
		"\u0001\u0000\u0000-.\u0007\u0002\u0000\u0000./\u0007\u0003\u0000\u0000"+
		"/0\u0007\u0004\u0000\u00000\u0002\u0001\u0000\u0000\u000012\u0007\u0005"+
		"\u0000\u000023\u0007\u0006\u0000\u000034\u0007\u0007\u0000\u00004\u0004"+
		"\u0001\u0000\u0000\u000056\u0007\u0005\u0000\u000067\u0007\b\u0000\u0000"+
		"78\u0007\u0007\u0000\u00008\u0006\u0001\u0000\u0000\u00009:\u0007\u0005"+
		"\u0000\u0000:;\u0007\u0002\u0000\u0000;<\u0007\t\u0000\u0000<=\u0007\n"+
		"\u0000\u0000=\b\u0001\u0000\u0000\u0000>?\u0007\u0006\u0000\u0000?@\u0007"+
		"\u0003\u0000\u0000@A\u0007\u0002\u0000\u0000AB\u0007\u000b\u0000\u0000"+
		"B\n\u0001\u0000\u0000\u0000CD\u0007\u0006\u0000\u0000DE\u0007\b\u0000"+
		"\u0000EF\u0007\b\u0000\u0000F\f\u0001\u0000\u0000\u0000GH\u0007\n\u0000"+
		"\u0000HI\u0007\u0001\u0000\u0000IJ\u0007\f\u0000\u0000J\u000e\u0001\u0000"+
		"\u0000\u0000KL\u0007\u000b\u0000\u0000LM\u0007\u0001\u0000\u0000MN\u0007"+
		"\r\u0000\u0000N\u0010\u0001\u0000\u0000\u0000OP\u0007\b\u0000\u0000PQ"+
		"\u0007\u000e\u0000\u0000QR\u0007\u000f\u0000\u0000R\u0012\u0001\u0000"+
		"\u0000\u0000ST\u0007\u0007\u0000\u0000TU\u0007\u0004\u0000\u0000UV\u0007"+
		"\u000b\u0000\u0000V\u0014\u0001\u0000\u0000\u0000WX\u0007\r\u0000\u0000"+
		"XY\u0007\u0004\u0000\u0000YZ\u0007\u0000\u0000\u0000Z\u0016\u0001\u0000"+
		"\u0000\u0000[\\\u0007\u0004\u0000\u0000\\]\u0007\u0000\u0000\u0000]^\u0007"+
		"\u0001\u0000\u0000^_\u0007\u0006\u0000\u0000_`\u0007\r\u0000\u0000`\u0018"+
		"\u0001\u0000\u0000\u0000ab\u0007\u0005\u0000\u0000bc\u0007\u0002\u0000"+
		"\u0000cd\u0007\t\u0000\u0000de\u0007\b\u0000\u0000e\u001a\u0001\u0000"+
		"\u0000\u0000fg\u0007\r\u0000\u0000gh\u0007\u0006\u0000\u0000hi\u0007\u000b"+
		"\u0000\u0000ij\u0007\f\u0000\u0000jk\u0007\b\u0000\u0000kl\u0007\u0006"+
		"\u0000\u0000l\u001c\u0001\u0000\u0000\u0000mn\u0007\r\u0000\u0000no\u0007"+
		"\u0004\u0000\u0000op\u0007\u0003\u0000\u0000p\u001e\u0001\u0000\u0000"+
		"\u0000qr\u0007\r\u0000\u0000rs\u0007\u0004\u0000\u0000st\u0007\u0003\u0000"+
		"\u0000tu\u0007\u0007\u0000\u0000uv\u0007\u0004\u0000\u0000vw\u0007\u0005"+
		"\u0000\u0000w \u0001\u0000\u0000\u0000xy\u0005(\u0000\u0000y\"\u0001\u0000"+
		"\u0000\u0000z{\u0005)\u0000\u0000{$\u0001\u0000\u0000\u0000|~\u0005-\u0000"+
		"\u0000}|\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~\u0080\u0001"+
		"\u0000\u0000\u0000\u007f\u0081\u0007\u0010\u0000\u0000\u0080\u007f\u0001"+
		"\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0080\u0001"+
		"\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000\u0083&\u0001\u0000"+
		"\u0000\u0000\u0084\u0088\u0007\u0011\u0000\u0000\u0085\u0087\u0007\u0012"+
		"\u0000\u0000\u0086\u0085\u0001\u0000\u0000\u0000\u0087\u008a\u0001\u0000"+
		"\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0088\u0089\u0001\u0000"+
		"\u0000\u0000\u0089(\u0001\u0000\u0000\u0000\u008a\u0088\u0001\u0000\u0000"+
		"\u0000\u008b\u008d\u0007\u0013\u0000\u0000\u008c\u008b\u0001\u0000\u0000"+
		"\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000"+
		"\u0000\u008e\u008f\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000"+
		"\u0000\u0090\u0091\u0006\u0014\u0000\u0000\u0091*\u0001\u0000\u0000\u0000"+
		"\u0005\u0000}\u0082\u0088\u008e\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}