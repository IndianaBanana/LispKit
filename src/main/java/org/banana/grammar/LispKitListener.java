// Generated from java-escape by ANTLR 4.11.1

    package org.banana.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LispKitParser}.
 */
public interface LispKitListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LispKitParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(LispKitParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispKitParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(LispKitParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispKitParser#sExpr}.
	 * @param ctx the parse tree
	 */
	void enterSExpr(LispKitParser.SExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispKitParser#sExpr}.
	 * @param ctx the parse tree
	 */
	void exitSExpr(LispKitParser.SExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispKitParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(LispKitParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispKitParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(LispKitParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispKitParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(LispKitParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispKitParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(LispKitParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link LispKitParser#symbolic_atom}.
	 * @param ctx the parse tree
	 */
	void enterSymbolic_atom(LispKitParser.Symbolic_atomContext ctx);
	/**
	 * Exit a parse tree produced by {@link LispKitParser#symbolic_atom}.
	 * @param ctx the parse tree
	 */
	void exitSymbolic_atom(LispKitParser.Symbolic_atomContext ctx);
}