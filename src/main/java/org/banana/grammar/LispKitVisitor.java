// Generated from java-escape by ANTLR 4.11.1

    package org.banana.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LispKitParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LispKitVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LispKitParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(LispKitParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispKitParser#sExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSExpr(LispKitParser.SExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispKitParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(LispKitParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispKitParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(LispKitParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link LispKitParser#symbolic_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbolic_atom(LispKitParser.Symbolic_atomContext ctx);
}