package org.banana.translator;

import java.util.List;

public class LispEvaluator {

    // Главный метод: принимает узел и контекст, возвращает вычисленный узел
    public AstNode eval(AstNode node, LispContext context) {
        
        // 1. Если это ЧИСЛО — возвращаем как есть (оно уже вычислено)
        if (node instanceof AstAtomNumber) {
            return node;
        }

        // 2. Если это СИМВОЛ (переменная) — ищем в контексте
        if (node instanceof AstAtomSymbol) {
            String name = ((AstAtomSymbol) node).getName();
            // Ищем значение переменной (например, "x" -> 10)
            return context.get(name);
        }

        // 3. Если это СПИСОК — это вызов функции или специальной формы
        if (node instanceof AstList list) {
            if (list.getChildren().isEmpty()) {
                return list; // Пустой список () возвращаем как есть
            }

            // Первый элемент списка — это команда (например, ADD, QUOTE, LET...)
            AstNode head = list.getChildren().get(0);
            
            // Проверяем, что голова — это символ
            if (!(head instanceof AstAtomSymbol)) {
                 // В будущем здесь будет обработка вызова лямбд: ((LAMBDA ...) arg)
                 throw new RuntimeException("Function name must be a symbol: " + head);
            }

            String functionName = ((AstAtomSymbol) head).getName().toUpperCase();
            List<AstNode> args = list.getChildren().subList(1, list.getChildren().size());

            // --- МАРШРУТИЗАЦИЯ (DISPATCH) ---
            switch (functionName) {
                // Базовые операторы
                case "QUOTE": return evalQuote(args);
                case "ADD":   return evalMath(args, context, Integer::sum);
                case "SUB":   return evalMath(args, context, (a, b) -> a - b);
                case "MUL":   return evalMath(args, context, (a, b) -> a * b);
                case "DIV":   return evalMath(args, context, (a, b) -> a / b);
                case "REM":   return evalMath(args, context, (a, b) -> a % b);
                
                // Списки
                case "CAR":   return evalCar(args, context);
                case "CDR":   return evalCdr(args, context);
                case "CONS":  return evalCons(args, context);
                
                // Логика
                case "ATOM":  return evalAtom(args, context);
                case "EQUAL": return evalEqual(args, context);
                case "LEQ":   return evalLeq(args, context);
                case "COND":  return evalCond(args, context);

                // Сложные формы (реализуем позже или частично)
                case "LET":   return evalLet(args, context);
                
                default:
                    // Если функция не встроенная, ищем её в контексте (для LAMBDA)
                    throw new RuntimeException("Unknown function: " + functionName);
            }
        }

        return node;
    }

    // --- РЕАЛИЗАЦИЯ ОПЕРАТОРОВ ---

    // (QUOTE X) -> возвращает X без вычисления
    private AstNode evalQuote(List<AstNode> args) {
        if (args.size() != 1) throw new RuntimeException("QUOTE expects 1 argument");
        return args.get(0);
    }

    // Математика: вычисляем аргументы и применяем операцию
    private AstNode evalMath(List<AstNode> args, LispContext ctx, java.util.function.IntBinaryOperator op) {
        if (args.size() != 2) throw new RuntimeException("Math op expects 2 arguments");

        // Сначала ВЫЧИСЛЯЕМ аргументы (рекурсия)
        AstNode val1 = eval(args.get(0), ctx);
        AstNode val2 = eval(args.get(1), ctx);

        if (val1 instanceof AstAtomNumber && val2 instanceof AstAtomNumber) {
            int res = op.applyAsInt(((AstAtomNumber) val1).getValue(), ((AstAtomNumber) val2).getValue());
            return new AstAtomNumber(res);
        }
        throw new RuntimeException("Math arguments must be numbers");
    }

    // (CAR list) -> голова списка
    private AstNode evalCar(List<AstNode> args, LispContext ctx) {
        if (args.size() != 1) throw new RuntimeException("CAR expects 1 argument");
        AstNode evalRes = eval(args.get(0), ctx); // вычисляем аргумент

        if (evalRes instanceof AstList) {
            List<AstNode> children = ((AstList) evalRes).getChildren();
            if (children.isEmpty()) throw new RuntimeException("Cannot CAR empty list");
            return children.get(0);
        }
        throw new RuntimeException("CAR expects a list");
    }

    // (CDR list) -> хвост списка
    private AstNode evalCdr(List<AstNode> args, LispContext ctx) {
        if (args.size() != 1) throw new RuntimeException("CDR expects 1 argument");
        AstNode evalRes = eval(args.get(0), ctx);

        if (evalRes instanceof AstList) {
            List<AstNode> children = ((AstList) evalRes).getChildren();
            if (children.isEmpty()) throw new RuntimeException("Cannot CDR empty list");
            
            AstList tail = new AstList();
            // Копируем все элементы, кроме первого
            for (int i = 1; i < children.size(); i++) {
                tail.addChild(children.get(i));
            }
            return tail;
        }
        throw new RuntimeException("CDR expects a list");
    }

    // (CONS head tail)
    private AstNode evalCons(List<AstNode> args, LispContext ctx) {
        if (args.size() != 2) throw new RuntimeException("CONS expects 2 arguments");
        AstNode head = eval(args.get(0), ctx);
        AstNode tail = eval(args.get(1), ctx);

        if (!(tail instanceof AstList)) throw new RuntimeException("Second argument of CONS must be a list");

        AstList newList = new AstList();
        newList.addChild(head);
        newList.getChildren().addAll(((AstList) tail).getChildren());
        return newList;
    }
    
    // (ATOM x) -> TRUE/FALSE
    private AstNode evalAtom(List<AstNode> args, LispContext ctx) {
         if (args.size() != 1) throw new RuntimeException("ATOM expects 1 argument");
         AstNode res = eval(args.get(0), ctx);
         boolean isAtom = (res instanceof AstAtomNumber) || (res instanceof AstAtomSymbol);
         return new AstAtomSymbol(isAtom ? "TRUE" : "FALSE");
    }
    
    private AstNode evalEqual(List<AstNode> args, LispContext ctx) {
        if (args.size() != 2) throw new RuntimeException("EQUAL expects 2 arguments");
        AstNode a = eval(args.get(0), ctx);
        AstNode b = eval(args.get(1), ctx);
        // Упрощенное сравнение через reconstruct (строковое представление)
        boolean eq = a.reconstruct().equals(b.reconstruct());
        return new AstAtomSymbol(eq ? "TRUE" : "FALSE");
    }

    private AstNode evalLeq(List<AstNode> args, LispContext ctx) {
        if (args.size() != 2) throw new RuntimeException("LEQ expects 2 arguments");
        AstNode a = eval(args.get(0), ctx);
        AstNode b = eval(args.get(1), ctx);
        if (a instanceof AstAtomNumber && b instanceof AstAtomNumber) {
            boolean leq = ((AstAtomNumber) a).getValue() <= ((AstAtomNumber) b).getValue();
            return new AstAtomSymbol(leq ? "TRUE" : "FALSE");
        }
        throw new RuntimeException("LEQ expects numbers");
    }

    // (COND (cond1 val1) (cond2 val2) ...)
    private AstNode evalCond(List<AstNode> args, LispContext ctx) {
        for (AstNode arg : args) {
            if (!(arg instanceof AstList)) throw new RuntimeException("COND branches must be lists");
            AstList branch = (AstList) arg;
            if (branch.getChildren().size() != 2) throw new RuntimeException("COND branch needs 2 elements");

            AstNode condition = branch.getChildren().get(0);
            AstNode value = branch.getChildren().get(1);

            AstNode conditionResult = eval(condition, ctx);
            // Считаем истиной всё, что не "FALSE" и не "NIL" (как пример)
            String resStr = conditionResult instanceof AstAtomSymbol ? ((AstAtomSymbol) conditionResult).getName() : "";
            
            if (!resStr.equals("FALSE") && !resStr.equals("NIL")) {
                return eval(value, ctx);
            }
        }
        throw new RuntimeException("No condition in COND was true");
    }

    // (LET ((x 1) (y 2)) body)
    private AstNode evalLet(List<AstNode> args, LispContext ctx) {
        // Мы разбирали это выше.
        // args.get(0) -> список связываний
        // args.get(1) -> тело
        if (args.size() < 2) throw new RuntimeException("LET expects bindings and body");
        
        AstList bindings = (AstList) args.get(0);
        AstNode body = args.get(1);
        
        LispContext localCtx = new LispContext(ctx);
        
        for (AstNode b : bindings.getChildren()) {
            AstList pair = (AstList) b;
            String varName = ((AstAtomSymbol) pair.getChildren().get(0)).getName();
            AstNode valExpr = pair.getChildren().get(1);
            
            AstNode evaluatedVal = eval(valExpr, ctx); // вычисляем в родительском!
            localCtx.define(varName, evaluatedVal);
        }
        
        return eval(body, localCtx);
    }
}