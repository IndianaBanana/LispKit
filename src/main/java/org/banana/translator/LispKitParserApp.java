package org.banana.translator;

import lombok.experimental.UtilityClass;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.banana.grammar.LispKitLexer;
import org.banana.grammar.LispKitParser;

@UtilityClass
public class LispKitParserApp {


    public static String parseAndGetAstString(String input) {
        ParseTree tree = buildConcreteSyntaxTree(input);

        LispAstVisitor visitor = new LispAstVisitor();
        AstNode astRoot = visitor.visit(tree);

        AstPrinter.print(astRoot);
        return astRoot.reconstruct();
    }


    private static ParseTree buildConcreteSyntaxTree(String input) {
        LispKitParser parser = initParser(input);
        return parser.program();
    }

    private static LispKitParser initParser(String input) {
        CharStream stream = CharStreams.fromString(input);
        LispKitLexer lexer = new LispKitLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LispKitParser parser = new LispKitParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        return parser;
    }
}