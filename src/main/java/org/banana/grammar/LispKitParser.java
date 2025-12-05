// Generated from java-escape by ANTLR 4.11.1

    package org.banana.grammar;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class LispKitParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		QUOTE=1, CAR=2, CDR=3, CONS=4, ATOM=5, ADD=6, SUB=7, MUL=8, DIV=9, REM=10, 
		LEQ=11, EQUAL=12, COND=13, LAMBDA=14, LET=15, LETREC=16, LPAREN=17, RPAREN=18, 
		NUMBER=19, IDENT=20, WS=21;
	public static final int
		RULE_program = 0, RULE_sExpr = 1, RULE_list = 2, RULE_atom = 3, RULE_symbolic_atom = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "sExpr", "list", "atom", "symbolic_atom"
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

	@Override
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LispKitParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public SExprContext sExpr() {
			return getRuleContext(SExprContext.class,0);
		}
		public TerminalNode EOF() { return getToken(LispKitParser.EOF, 0); }
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispKitVisitor ) return ((LispKitVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			sExpr();
			setState(11);
			match(EOF);
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

	@SuppressWarnings("CheckReturnValue")
	public static class SExprContext extends ParserRuleContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public SExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).enterSExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).exitSExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispKitVisitor ) return ((LispKitVisitor<? extends T>)visitor).visitSExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SExprContext sExpr() throws RecognitionException {
		SExprContext _localctx = new SExprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_sExpr);
		try {
			setState(15);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case QUOTE:
			case CAR:
			case CDR:
			case CONS:
			case ATOM:
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case REM:
			case LEQ:
			case EQUAL:
			case COND:
			case LAMBDA:
			case LET:
			case LETREC:
			case NUMBER:
			case IDENT:
				enterOuterAlt(_localctx, 1);
				{
				setState(13);
				atom();
				}
				break;
			case LPAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(14);
				list();
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

	@SuppressWarnings("CheckReturnValue")
	public static class ListContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(LispKitParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(LispKitParser.RPAREN, 0); }
		public List<SExprContext> sExpr() {
			return getRuleContexts(SExprContext.class);
		}
		public SExprContext sExpr(int i) {
			return getRuleContext(SExprContext.class,i);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispKitVisitor ) return ((LispKitVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			match(LPAREN);
			setState(21);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 1835006L) != 0) {
				{
				{
				setState(18);
				sExpr();
				}
				}
				setState(23);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(24);
			match(RPAREN);
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

	@SuppressWarnings("CheckReturnValue")
	public static class AtomContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(LispKitParser.NUMBER, 0); }
		public Symbolic_atomContext symbolic_atom() {
			return getRuleContext(Symbolic_atomContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispKitVisitor ) return ((LispKitVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_atom);
		try {
			setState(28);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(26);
				match(NUMBER);
				}
				break;
			case QUOTE:
			case CAR:
			case CDR:
			case CONS:
			case ATOM:
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case REM:
			case LEQ:
			case EQUAL:
			case COND:
			case LAMBDA:
			case LET:
			case LETREC:
			case IDENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				symbolic_atom();
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

	@SuppressWarnings("CheckReturnValue")
	public static class Symbolic_atomContext extends ParserRuleContext {
		public TerminalNode QUOTE() { return getToken(LispKitParser.QUOTE, 0); }
		public TerminalNode CAR() { return getToken(LispKitParser.CAR, 0); }
		public TerminalNode CDR() { return getToken(LispKitParser.CDR, 0); }
		public TerminalNode CONS() { return getToken(LispKitParser.CONS, 0); }
		public TerminalNode ATOM() { return getToken(LispKitParser.ATOM, 0); }
		public TerminalNode ADD() { return getToken(LispKitParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(LispKitParser.SUB, 0); }
		public TerminalNode MUL() { return getToken(LispKitParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(LispKitParser.DIV, 0); }
		public TerminalNode REM() { return getToken(LispKitParser.REM, 0); }
		public TerminalNode LEQ() { return getToken(LispKitParser.LEQ, 0); }
		public TerminalNode EQUAL() { return getToken(LispKitParser.EQUAL, 0); }
		public TerminalNode COND() { return getToken(LispKitParser.COND, 0); }
		public TerminalNode LAMBDA() { return getToken(LispKitParser.LAMBDA, 0); }
		public TerminalNode LET() { return getToken(LispKitParser.LET, 0); }
		public TerminalNode LETREC() { return getToken(LispKitParser.LETREC, 0); }
		public TerminalNode IDENT() { return getToken(LispKitParser.IDENT, 0); }
		public Symbolic_atomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_symbolic_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).enterSymbolic_atom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LispKitListener ) ((LispKitListener)listener).exitSymbolic_atom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LispKitVisitor ) return ((LispKitVisitor<? extends T>)visitor).visitSymbolic_atom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Symbolic_atomContext symbolic_atom() throws RecognitionException {
		Symbolic_atomContext _localctx = new Symbolic_atomContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_symbolic_atom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 1179646L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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

	public static final String _serializedATN =
		"\u0004\u0001\u0015!\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u0001\u0010"+
		"\b\u0001\u0001\u0002\u0001\u0002\u0005\u0002\u0014\b\u0002\n\u0002\f\u0002"+
		"\u0017\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0003\u0003"+
		"\u001d\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0000\u0000\u0005\u0000"+
		"\u0002\u0004\u0006\b\u0000\u0001\u0002\u0000\u0001\u0010\u0014\u0014\u001e"+
		"\u0000\n\u0001\u0000\u0000\u0000\u0002\u000f\u0001\u0000\u0000\u0000\u0004"+
		"\u0011\u0001\u0000\u0000\u0000\u0006\u001c\u0001\u0000\u0000\u0000\b\u001e"+
		"\u0001\u0000\u0000\u0000\n\u000b\u0003\u0002\u0001\u0000\u000b\f\u0005"+
		"\u0000\u0000\u0001\f\u0001\u0001\u0000\u0000\u0000\r\u0010\u0003\u0006"+
		"\u0003\u0000\u000e\u0010\u0003\u0004\u0002\u0000\u000f\r\u0001\u0000\u0000"+
		"\u0000\u000f\u000e\u0001\u0000\u0000\u0000\u0010\u0003\u0001\u0000\u0000"+
		"\u0000\u0011\u0015\u0005\u0011\u0000\u0000\u0012\u0014\u0003\u0002\u0001"+
		"\u0000\u0013\u0012\u0001\u0000\u0000\u0000\u0014\u0017\u0001\u0000\u0000"+
		"\u0000\u0015\u0013\u0001\u0000\u0000\u0000\u0015\u0016\u0001\u0000\u0000"+
		"\u0000\u0016\u0018\u0001\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000"+
		"\u0000\u0018\u0019\u0005\u0012\u0000\u0000\u0019\u0005\u0001\u0000\u0000"+
		"\u0000\u001a\u001d\u0005\u0013\u0000\u0000\u001b\u001d\u0003\b\u0004\u0000"+
		"\u001c\u001a\u0001\u0000\u0000\u0000\u001c\u001b\u0001\u0000\u0000\u0000"+
		"\u001d\u0007\u0001\u0000\u0000\u0000\u001e\u001f\u0007\u0000\u0000\u0000"+
		"\u001f\t\u0001\u0000\u0000\u0000\u0003\u000f\u0015\u001c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}