package org.banana.translator;

import org.banana.grammar.LispKitBaseVisitor;
import org.banana.grammar.LispKitParser.AtomContext;
import org.banana.grammar.LispKitParser.ListContext;
import org.banana.grammar.LispKitParser.ProgramContext;
import org.banana.grammar.LispKitParser.SExprContext;

public class LispAstVisitor extends LispKitBaseVisitor<AstNode> {

    @Override
    public AstNode visitProgram(ProgramContext ctx) {
        return visit(ctx.sExpr());
    }

    @Override
    public AstNode visitSExpr(SExprContext ctx) {
        if (ctx.atom() != null) {
            return visit(ctx.atom());
        } else if (ctx.list() != null) {
            return visit(ctx.list());
        }
        return null;
    }

    @Override
    public AstNode visitList(ListContext ctx) {
        AstList listNode = new AstList();
        for (SExprContext sExprCtx : ctx.sExpr()) {
            listNode.addChild(visit(sExprCtx));
        }
        return listNode;
    }

    @Override
    public AstNode visitAtom(AtomContext ctx) {

        if (ctx.NUMBER() != null) {
            int val = Integer.parseInt(ctx.NUMBER().getText());
            return new AstAtomNumber(val);
        } else {
            return new AstAtomSymbol(ctx.getText());
        }
    }
}