package org.banana.translator;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.banana.grammar.LispKitLexer;
import org.banana.grammar.LispKitParser;

import java.util.ArrayList;

@Slf4j
@UtilityClass
public class LispKitParserApp {

    public static String parseAndCompileAndRun(String input) {
        AstNode astRoot = parseToAst(input);
        AstList secdCode = new LispCompiler().compile(astRoot, new ArrayList<>());
        String bytecode = secdCode.reconstruct();
        log.info("Bytecode: {}", bytecode);
        AstNode result = new SecdMachine().run(secdCode);
        return "SECD Result: " + result.reconstruct() + "\nCode: " + bytecode;
    }

    public static String parseAndEvaluate(String input) {
        AstNode astRoot = parseToAst(input);
        AstPrinter.print(astRoot);
        AstNode result = new LispEvaluator().evaluate(astRoot, new LispContext());
        return "Result: " + result.reconstruct();
    }

    public static AstNode parseToAst(String input) {
        ParseTree tree = buildConcreteSyntaxTree(input);
        return new LispAstVisitor().visit(tree);
    }

    public static AstNode compileAndRun(String input) {
        AstList secdCode = new LispCompiler().compile(parseToAst(input), new ArrayList<>());
        return new SecdMachine().run(secdCode);
    }

    public static AstNode evaluateToNode(String input) {
        return new LispEvaluator().evaluate(parseToAst(input), new LispContext());
    }

    public static String parseAndGetAstString(String input) {
        AstNode astRoot = parseToAst(input);
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