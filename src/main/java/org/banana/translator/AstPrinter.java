package org.banana.translator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class AstPrinter {

    public static void print(AstNode root) {
        log.info("---------- AST DEBUG START ----------");
        printRecursive(root, 0);
        log.info("----------- AST DEBUG END -----------");
    }

    private static void printRecursive(AstNode node, int depth) {
        String indent = StringUtils.repeat("  ", depth);

        if (node instanceof AstList list) {
            log.info("{}📂 List (size={})", indent, list.getChildren().size());
            
            for (AstNode child : list.getChildren()) {
                printRecursive(child, depth + 1);
            }

        } else if (node instanceof AstAtomNumber) {
            AstAtomNumber num = (AstAtomNumber) node;
            log.info("{}🔢 Number: {}", indent, num.getValue());

        } else if (node instanceof AstAtomSymbol) {
            AstAtomSymbol sym = (AstAtomSymbol) node;
            // Выводим символ
            log.info("{}🔤 Symbol: {}", indent, sym.getName());
        }
    }
}