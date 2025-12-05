package org.banana.translator;

import java.util.List;
import java.util.function.IntBinaryOperator;

/**
 * Created by Banana on 05.12.2025
 */
public class LispEvaluatorMy {

    public AstNode evaluate(AstNode node, LispContext context) {
        if (node instanceof AstAtomNumber) return node;
        else if (node instanceof AstAtomSymbol astAtomSymbol) {
            return context.get(astAtomSymbol.getName());
        } else if (node instanceof AstList list) {
            if (list.getChildren().isEmpty()) {
                return list;
            }
            AstNode head = list.getChildren().get(0);
            if (!(head instanceof AstAtomSymbol)) {
                // В будущем здесь будет обработка вызова лямбд: ((LAMBDA ...) arg)
                throw new RuntimeException("Function name must be a symbol: " + head);
            }
            FunctionName functionName = FunctionName.valueOf(((AstAtomSymbol) head).getName().toUpperCase());
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
                    evaluateEqual(args, context);
                case COND:
                    evaluateCond(args, context);
                case LAMBDA:
                    break;
                case LET:
                    break;
                case LETREC:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + functionName);
            }
        }

        return null;
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
            if (children.isEmpty()) throw new RuntimeException("Cannot CAR empty list");
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
            if (children.isEmpty()) throw new RuntimeException("Cannot CDR empty list");
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

        if (!(a instanceof AstAtomNumber && b instanceof AstAtomNumber))
            throw new RuntimeException("EQUAL expects numbers");
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
        if (args.size() != 3) throw new RuntimeException("COND expects 3 arguments");
        AstNode evalCondition = evaluate(args.get(0), context);
        if (!(evalCondition instanceof AstAtomSymbol symbolEvalCondition
              && (symbolEvalCondition.getName().equals("TRUE")
                  || symbolEvalCondition.getName().equals("FALSE")))) {
            throw new RuntimeException("1st arguments in COND should be boolean value");
        }
        boolean bool = Boolean.parseBoolean(symbolEvalCondition.getName());
        if (bool){
            return evaluate(args.get(1), context);
        } else {
            return evaluate(args.get(2), context);
        }
    }
}
