package org.banana.translator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntBinaryOperator;

/**
 * Created by Banana on 05.12.2025
 */
public class LispEvaluator {

    public AstNode evaluate(AstNode node, LispContext context) {
        if (node instanceof AstAtomNumber) return node;
        else if (node instanceof AstAtomSymbol astAtomSymbol) {
//            String name = astAtomSymbol.getName().toUpperCase();
//            if (name.equals("TRUE") || name.equals("FALSE") || name.equals("NIL")) {
//                return node;
//            }
            return context.get(astAtomSymbol.getName());
        } else if (node instanceof AstList list) {
            if (list.getChildren().isEmpty()) {
                return list;
            }
            AstNode head = list.getChildren().get(0);
            if (head instanceof AstAtomSymbol) {
                String name = ((AstAtomSymbol) head).getName().toUpperCase();
                try {
                    FunctionName functionName = FunctionName.valueOf(name);
                    List<AstNode> args = list.getChildren().subList(1, list.getChildren().size());
                    switch (functionName) {
                        case ATOM:
                            return evaluateAtom(args, context);
                        case QUOTE:
                            return evaluateQuote(args);
                        case ADD:
                            return evaluateMath(args, context, Integer::sum);
                        case SUB:
                            return evaluateMath(args, context, (a, b) -> a - b);
                        case MUL:
                            return evaluateMath(args, context, (a, b) -> a * b);
                        case DIV:
                            return evaluateMath(args, context, (a, b) -> a / b);
                        case REM:
                            return evaluateMath(args, context, (a, b) -> a % b);
                        case CAR:
                            return evaluateCar(args, context);
                        case CDR:
                            return evaluateCdr(args, context);
                        case CONS:
                            return evaluateCons(args, context);
                        case LEQ:
                            return evaluateLeq(args, context);
                        case EQUAL:
                            return evaluateEqual(args, context);
                        case COND:
                            return evaluateCond(args, context);
                        case LAMBDA:
                            return evaluateLambda(list.getChildren(), context);
                        case LET:
                            return evaluateLet(list.getChildren(), context);
                        case LETREC:
                            return evaluateLetrec(list.getChildren(), context);
                    }
                } catch (IllegalArgumentException ex) {}
            }
            return evaluateApplication(list, context);
        }
        throw new RuntimeException("Unknown AST node: " + node);
    }

    private AstNode evaluateAtom(List<AstNode> args, LispContext context) {
        if (args.size() != 1) throw new RuntimeException("ATOM expects 1 argument");
        AstNode res = evaluate(args.get(0), context);
        boolean isAtom = (res instanceof AstAtomNumber) || (res instanceof AstAtomSymbol);
        return new AstAtomSymbol(isAtom ? "TRUE" : "FALSE");
    }

    private AstNode evaluateQuote(List<AstNode> args) {
        if (args.size() != 1) throw new RuntimeException("QUOTE expects 1 argument");
        return args.get(0);
    }

    private AstNode evaluateMath(List<AstNode> args, LispContext context, IntBinaryOperator operator) {
        if (args.size() != 2) throw new RuntimeException("Math operator expects 2 arguments");

        AstNode val1 = evaluate(args.get(0), context);
        AstNode val2 = evaluate(args.get(1), context);

        if (val1 instanceof AstAtomNumber x && val2 instanceof AstAtomNumber y) {
            int result = operator.applyAsInt(x.getValue(), y.getValue());
            return new AstAtomNumber(result);
        } else {
            throw new RuntimeException("Math arguments must be numbers");
        }
    }

    /**
     * Возвращает голову списка, то есть get(0)
     *
     * @param args    список аргументов функции CAR (список)
     * @param context контекст интерпретации
     * @return первый элемент списка
     */
    private AstNode evaluateCar(List<AstNode> args, LispContext context) {
        if (args.size() != 1) throw new RuntimeException("CAR expects 1 argument");
        AstNode evalRes = evaluate(args.get(0), context);

        if (evalRes instanceof AstList list) {
            List<AstNode> children = list.getChildren();
            if (children.isEmpty()) return new AstList();
            return children.get(0);
        } else {
            throw new RuntimeException("CAR expects a list");
        }
    }

    /**
     * Возвращает хвост списка, то есть sublist(1, list.size()).
     *
     * @param args    список аргументов функции CDR (список)
     * @param context контекст интерпретации
     * @return первый элемент списка
     */
    private AstNode evaluateCdr(List<AstNode> args, LispContext context) {
        if (args.size() != 1) throw new RuntimeException("CDR expects 1 argument");
        AstNode evalRes = evaluate(args.get(0), context);

        if (evalRes instanceof AstList list) {
            List<AstNode> children = list.getChildren();
            if (children.isEmpty()) return new AstList();
            AstList tail = new AstList();
            // Копируем все элементы, кроме первого
            for (int i = 1; i < children.size(); i++) tail.addChild(children.get(i));
            return tail;
        } else {
            throw new RuntimeException("CDR expects a list");
        }
    }

    private AstNode evaluateCons(List<AstNode> args, LispContext context) {
        if (args.size() != 2) throw new RuntimeException("CONS Expects 2 arguments");
        AstNode head = evaluate(args.get(0), context);
        AstNode tail = evaluate(args.get(1), context);

        if (!(tail instanceof AstList)) throw new RuntimeException("Second argument of CONS must be a list");

        AstList newList = new AstList();
        newList.addChild(head);
        newList.getChildren().addAll(((AstList) tail).getChildren());
        return newList;
    }

    private AstNode evaluateLeq(List<AstNode> args, LispContext context) {
        if (args.size() != 2) throw new RuntimeException("LEQ expects 2 arguments");
        AstNode a = evaluate(args.get(0), context);
        AstNode b = evaluate(args.get(1), context);

        if (!(a instanceof AstAtomNumber && b instanceof AstAtomNumber))
            throw new RuntimeException("LEQ expects numbers");
        boolean leq = ((AstAtomNumber) a).getValue() <= ((AstAtomNumber) b).getValue();
        return new AstAtomSymbol(leq ? "TRUE" : "FALSE");
    }

    private AstNode evaluateEqual(List<AstNode> args, LispContext context) {
        if (args.size() != 2) throw new RuntimeException("EQUAL expects 2 arguments");
        AstNode a = evaluate(args.get(0), context);
        AstNode b = evaluate(args.get(1), context);

        boolean equal = a.equals(b);
        return new AstAtomSymbol(equal ? "TRUE" : "FALSE");
    }

    /**
     * Сначала вычисляется выражение e1. Если оно не является
     * булевой функцией, то возникает ошибка.
     * Если вычисление условия e1 ложно, то вычисляется выражение e3
     * и его значение возвращается в качестве результата вычисления
     * условного выражения. В противном случае вычисляется
     * выражение e2.
     * Из правила перехода видно, что все вычисления выполняются в
     * текущем окружении.
     */
    private AstNode evaluateCond(List<AstNode> args, LispContext context) {
        System.out.println(args);
        if (args.size() != 3) throw new RuntimeException("COND expects 3 arguments");
        AstNode evalCondition = evaluate(args.get(0), context);
        if (!(evalCondition instanceof AstAtomSymbol symbolEvalCondition
              && (symbolEvalCondition.getName().equals("TRUE")
                  || symbolEvalCondition.getName().equals("FALSE")))) {
            throw new RuntimeException("1st arguments in COND should be boolean value");
        }
        boolean bool = Boolean.parseBoolean(symbolEvalCondition.getName());
        if (bool) {
            return evaluate(args.get(1), context);
        } else {
            return evaluate(args.get(2), context);
        }
    }

    private AstNode evaluateLambda(List<AstNode> listNodes, LispContext context) {
        // Синтаксис: (LAMBDA (arg1 arg2) body)
        // listNodes[0] = LAMBDA
        // listNodes[1] = (arg1 arg2)
        // listNodes[2] = body

        if (listNodes.size() != 3) throw new RuntimeException("LAMBDA expects args list and body");

        AstNode argsNode = listNodes.get(1);
        if (!(argsNode instanceof AstList argsList)) {
            throw new RuntimeException("LAMBDA arguments must be a list");
        }

        List<String> argNames = new ArrayList<>();
        for (AstNode arg : argsList.getChildren()) {
            if (arg instanceof AstAtomSymbol sym) {
                argNames.add(sym.getName());
            } else {
                throw new RuntimeException("Arg names must be symbols");
            }
        }

        AstNode body = listNodes.get(2);

        // Возвращаем Замыкание. Важно передать ТЕКУЩИЙ context.
        return new AstClosure(argNames, body, context);
    }

    private AstNode evaluateLet(List<AstNode> listNodes, LispContext context) {
        // listNodes[0] = LET
        // listNodes[1] = body (выражение для вычисления)
        // listNodes[2...] = пары (var val)

        AstNode body = listNodes.get(1);

        // 1. Создаем локальный контекст, родителем которого является текущий
        LispContext localContext = new LispContext(context);

        // 2. Проходим по определениям
        for (int i = 2; i < listNodes.size(); i++) {
            AstNode pairNode = listNodes.get(i);
            // Проверка структуры (var val)
            if (!(pairNode instanceof AstList pair) || pair.getChildren().size() != 2) {
                throw new RuntimeException("LET binding must be (var val)");
            }

            String varName = ((AstAtomSymbol) pair.getChildren().get(0)).getName();
            AstNode valExpr = pair.getChildren().get(1);

            // ВАЖНО: Значение вычисляем во ВНЕШНЕМ (исходном) контексте!
            // (Лекция 7, слайд 18, формула 17: аргументы e1...ek вычисляются в сигма)
            AstNode valEvaluated = evaluate(valExpr, context);

            localContext.define(varName, valEvaluated);
        }

        // 3. Вычисляем тело в локальном контексте
        return evaluate(body, localContext);
    }

    private AstNode evaluateLetrec(List<AstNode> listNodes, LispContext context) {

        AstNode body = listNodes.get(1);

        LispContext recursiveContext = new LispContext(context);

        // 2. Обрабатываем определения
        for (int i = 2; i < listNodes.size(); i++) {
            AstList pair = (AstList) listNodes.get(i);
            String varName = ((AstAtomSymbol) pair.getChildren().get(0)).getName();
            AstNode valExpr = pair.getChildren().get(1);

            // ВАЖНО: Вычисляем значение сразу в recursiveContext.
            // Обычно valExpr здесь - это LAMBDA.
            // Внутри evaluateLambda создастся AstClosure, который сохранит ссылку на recursiveContext.
            // Так как Java передает ссылки на объекты, когда мы позже сделаем define,
            // замыкание "увидит" обновленный контекст.
            AstNode valEvaluated = evaluate(valExpr, recursiveContext);

            recursiveContext.define(varName, valEvaluated);
        }

        return evaluate(body, recursiveContext);
    }

    private AstNode evaluateApplication(AstList list, LispContext context) {
        List<AstNode> nodes = list.getChildren();

        // 1. Вычисляем первый элемент. Это должно вернуть:
        //    - AstClosure (если это пользовательская функция)
        AstNode evalHead = evaluate(nodes.get(0), context);

        if (!(evalHead instanceof AstClosure closure)) {
            throw new RuntimeException("Attempt to call non-function: " + evalHead);
        }

        List<AstNode> evaluatedArgs = new ArrayList<>();
        for (int i = 1; i < nodes.size(); i++) {
            evaluatedArgs.add(evaluate(nodes.get(i), context));
        }

        if (evaluatedArgs.size() != closure.getArgNames().size()) {
            throw new RuntimeException("Wrong argument count");
        }

        LispContext functionContext = new LispContext(closure.getContext());

        for (int i = 0; i < evaluatedArgs.size(); i++) {
            functionContext.define(closure.getArgNames().get(i), evaluatedArgs.get(i));
        }

        return evaluate(closure.getBody(), functionContext);
    }
}
