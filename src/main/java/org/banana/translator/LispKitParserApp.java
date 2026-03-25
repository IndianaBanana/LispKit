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
        // 1. Парсинг
        ParseTree tree = buildConcreteSyntaxTree(input);
        LispAstVisitor visitor = new LispAstVisitor();
        AstNode astRoot = visitor.visit(tree);

        // 2. Компиляция
        LispCompiler compiler = new LispCompiler();
        // Начальный контекст имен пустой
        AstList secdCode = compiler.compile(astRoot, new ArrayList<>());

        // Добавляем команду остановки
//        secdCode.addChild(new AstAtomSymbol("STOP"));

        String bytecode = secdCode.reconstruct(); // Для отладки
        log.info(bytecode);
        // 3. Исполнение
        SecdMachine vm = new SecdMachine();
        AstNode result = vm.run(secdCode);

        return "SECD Result: " + result.reconstruct() + "\nCode: " + bytecode;
    }

    public static String parseAndEvaluate(String input) {
        // 1. Построение AST (как и раньше)
        ParseTree tree = buildConcreteSyntaxTree(input);
        LispAstVisitor visitor = new LispAstVisitor();
        AstNode astRoot = visitor.visit(tree);

        // 2. Печать для отладки
        AstPrinter.print(astRoot);

        // 3. Вычисление AST
        LispEvaluator evaluator = new LispEvaluator();
        LispContext globalContext = new LispContext(); // Создаем глобальный контекст

        // ВАЖНО: Вычисление начинается с глобального контекста
        AstNode result = evaluator.evaluate(astRoot, globalContext);

        // 4. Возвращаем результат в виде строки
        return "Result: " + result.reconstruct();
    }

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