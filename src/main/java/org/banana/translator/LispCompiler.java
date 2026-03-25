package org.banana.translator;

import java.util.ArrayList;
import java.util.List;

public class LispCompiler {

    public AstList compile(AstNode root, List<String> rawNames) {
        List<List<String>> names = new ArrayList<>();
        names.add(new ArrayList<>(rawNames));
        AstList code = compileRec(root, names);
        code.addChild(new AstAtomSymbol("STOP"));
        return code;
    }

    private AstList compileRec(AstNode node, List<List<String>> names) {
        AstList code = new AstList();

        // 1. Числа
        if (node instanceof AstAtomNumber) {
            code.addChild(new AstAtomSymbol("LDC"));
            code.addChild(node);
            return code;
        }

        // 2. Переменные
        if (node instanceof AstAtomSymbol sym) {
            String name = sym.getName();
            int[] place = place(names, name);
            if (place == null) {
                code.addChild(new AstAtomSymbol("LDC"));
                code.addChild(new AstAtomSymbol(name));
            } else {
                code.addChild(new AstAtomSymbol("LD"));
                AstList coordinates = new AstList();
                coordinates.addChild(new AstAtomNumber(place[0]));
                coordinates.addChild(new AstAtomNumber(place[1]));
                code.addChild(coordinates);
            }
            return code;
        }


        // 3. Списки
        if (node instanceof AstList listNode) {
            List<AstNode> children = listNode.getChildren();

            // Пустой список -> LDC ()
            if (children.isEmpty()) {
                code.addChild(new AstAtomSymbol("LDC"));
                code.addChild(new AstList());
                return code;
            }

            AstNode head = children.get(0);
            if (head instanceof AstAtomSymbol headSym) {
                String op = headSym.getName().toUpperCase().trim();
                switch (op) {
                    case "QUOTE": {
                        code.addChild(new AstAtomSymbol("LDC"));
                        code.addChild(children.get(1));
                        return code;
                    }
                    // Сначала Правый (2), потом Левый (1).
                    // Это обеспечит порядок на стеке: [Левый, Правый] (Левый на вершине)
                    case "CONS": {
                        code.getChildren().addAll(compileRec(children.get(2), names).getChildren());
                        code.getChildren().addAll(compileRec(children.get(1), names).getChildren());
                        code.addChild(new AstAtomSymbol(op));
                        return code;
                    }

                    // Порядок: Сначала Левый (1), потом Правый (2).
                    // Это даст стек: [Arg2, Arg1] -> ADD снимает v2, потом v1.
                    // (ADD Z 1) -> LD(Z) LDC(1) ADD -> Stack: [1, Z] -> 1+Z
                    case "ADD":
                    case "SUB":
                    case "MUL":
                    case "DIV":
                    case "REM":
                    case "LEQ":
                    case "EQUAL": {
                        code.getChildren().addAll(compileRec(children.get(1), names).getChildren()); // <--- Сначала левый
                        code.getChildren().addAll(compileRec(children.get(2), names).getChildren()); // <--- Потом правый
                        code.addChild(new AstAtomSymbol(op));
                        return code;
                    }
                    case "CAR":
                    case "CDR":
                    case "ATOM": {
                        code.getChildren().addAll(compileRec(children.get(1), names).getChildren());
                        code.addChild(new AstAtomSymbol(op));
                        return code;
                    }
                    case "COND": {
                        code.getChildren().addAll(compileRec(children.get(1), names).getChildren());
                        code.addChild(new AstAtomSymbol("SEL"));
                        AstList trueBranch = compileRec(children.get(2), names);
                        trueBranch.addChild(new AstAtomSymbol("JOIN"));
                        AstList falseBranch = compileRec(children.get(3), names);
                        falseBranch.addChild(new AstAtomSymbol("JOIN"));
                        code.addChild(trueBranch);
                        code.addChild(falseBranch);
                        return code;
                    }
                    case "LAMBDA": {
                        AstList argsList = (AstList) children.get(1);
                        List<String> argNames = new ArrayList<>();
                        for (AstNode a : argsList.getChildren()) argNames.add(((AstAtomSymbol) a).getName());
                        List<List<String>> newNames = new ArrayList<>();
                        newNames.add(argNames);
                        newNames.addAll(names);
                        AstList bodyCode = compileRec(children.get(2), newNames);
                        bodyCode.addChild(new AstAtomSymbol("RTN"));
                        code.addChild(new AstAtomSymbol("LDF"));
                        code.addChild(bodyCode);
                        return code;
                    }
                    case "LET": {
                        AstNode body = children.get(1);
                        List<String> vars = new ArrayList<>();
                        List<AstNode> vals = new ArrayList<>();
                        for (int i = 2; i < children.size(); i++) {
                            AstList pair = (AstList) children.get(i);
                            vars.add(((AstAtomSymbol) pair.getChildren().get(0)).getName());
                            vals.add(pair.getChildren().get(1));
                        }
                        // Этот цикл генерирует стек: [ValN, ..., Val1, NIL] -> [Head, Tail]
                        code.addChild(new AstAtomSymbol("LDC"));
                        code.addChild(new AstList());
                        for (int i = vals.size() - 1; i >= 0; i--) {
                            code.getChildren().addAll(compileRec(vals.get(i), names).getChildren());
                            code.addChild(new AstAtomSymbol("CONS"));
                        }
                        List<List<String>> nNames = new ArrayList<>();
                        nNames.add(vars);
                        nNames.addAll(names);
                        AstList bCode = compileRec(body, nNames);
                        bCode.addChild(new AstAtomSymbol("RTN"));
                        code.addChild(new AstAtomSymbol("LDF"));
                        code.addChild(bCode);
                        code.addChild(new AstAtomSymbol("AP"));
                        return code;
                    }
                    case "LETREC": {
                        AstNode body = children.get(1);
                        List<String> vars = new ArrayList<>();
                        List<AstNode> exprs = new ArrayList<>();
                        for (int i = 2; i < children.size(); i++) {
                            AstList pair = (AstList) children.get(i);
                            vars.add(((AstAtomSymbol) pair.getChildren().get(0)).getName());
                            exprs.add(pair.getChildren().get(1));
                        }
                        code.addChild(new AstAtomSymbol("DUM"));
                        List<List<String>> recNames = new ArrayList<>();
                        recNames.add(vars);
                        recNames.addAll(names);
                        code.addChild(new AstAtomSymbol("LDC"));
                        code.addChild(new AstList());
                        for (int i = exprs.size() - 1; i >= 0; i--) {
                            code.getChildren().addAll(compileRec(exprs.get(i), recNames).getChildren());
                            code.addChild(new AstAtomSymbol("CONS"));
                        }
                        AstList bCode = compileRec(body, recNames);
                        bCode.addChild(new AstAtomSymbol("RTN"));
                        code.addChild(new AstAtomSymbol("LDF"));
                        code.addChild(bCode);
                        code.addChild(new AstAtomSymbol("RAP"));
                        return code;
                    }
                }
            }
            // Universal Application
            code.addChild(new AstAtomSymbol("LDC"));
            code.addChild(new AstList());
            for (int i = children.size() - 1; i >= 1; i--) {
                code.getChildren().addAll(compileRec(children.get(i), names).getChildren());
                code.addChild(new AstAtomSymbol("CONS"));
            }
            code.getChildren().addAll(compileRec(head, names).getChildren());
            code.addChild(new AstAtomSymbol("AP"));
            return code;
        }
        return code;
    }

    private int[] place(List<List<String>> names, String var) {
        for (int b = 0; b < names.size(); b++) {
            List<String> frame = names.get(b);
            for (int m = 0; m < frame.size(); m++) {
                if (frame.get(m).equals(var)) return new int[]{b, m};
            }
        }
        return null;
    }
}