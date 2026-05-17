package org.banana.translator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LispEvaluatorTest {

    private String eval(String input) {
        return LispKitParserApp.evaluateToNode(input).reconstruct();
    }

    // --- Arithmetic ---

    @Test
    void add() {
        assertEquals("5", eval("(add 2 3)"));
    }

    @Test
    void sub() {
        assertEquals("1", eval("(sub 3 2)"));
    }

    @Test
    void mul() {
        assertEquals("6", eval("(mul 2 3)"));
    }

    @Test
    void div() {
        assertEquals("3", eval("(div 9 3)"));
    }

    @Test
    void rem() {
        assertEquals("1", eval("(rem 7 3)"));
    }

    @Test
    void nestedArithmetic() {
        assertEquals("10", eval("(add (mul 2 3) (sub 5 1))"));
    }

    // --- Comparison ---

    @Test
    void leqTrue() {
        assertEquals("TRUE", eval("(leq 2 3)"));
    }

    @Test
    void leqEqual() {
        assertEquals("TRUE", eval("(leq 3 3)"));
    }

    @Test
    void leqFalse() {
        assertEquals("FALSE", eval("(leq 4 3)"));
    }

    @Test
    void equalNumbers() {
        assertEquals("TRUE", eval("(equal 5 5)"));
    }

    @Test
    void equalNumbersFalse() {
        assertEquals("FALSE", eval("(equal 5 6)"));
    }

    // --- Quote ---

    @Test
    void quoteSymbol() {
        assertEquals("hello", eval("(quote hello)"));
    }

    @Test
    void quoteList() {
        assertEquals("(1 2 3)", eval("(quote (1 2 3))"));
    }

    // --- List operations ---

    @Test
    void car() {
        assertEquals("1", eval("(car (quote (1 2 3)))"));
    }

    @Test
    void cdr() {
        assertEquals("(2 3)", eval("(cdr (quote (1 2 3)))"));
    }

    @Test
    void cons() {
        assertEquals("(1 2 3)", eval("(cons 1 (quote (2 3)))"));
    }

    @Test
    void atomOnNumber() {
        assertEquals("TRUE", eval("(atom 5)"));
    }

    @Test
    void atomOnList() {
        assertEquals("FALSE", eval("(atom (quote (1 2)))"));
    }

    // --- Cond ---

    @Test
    void condTrue() {
        assertEquals("1", eval("(cond (leq 1 2) 1 2)"));
    }

    @Test
    void condFalse() {
        assertEquals("2", eval("(cond (leq 3 2) 1 2)"));
    }

    // --- Lambda & application ---

    @Test
    void lambdaApply() {
        assertEquals("7", eval("((lambda (x) (add x 2)) 5)"));
    }

    @Test
    void lambdaMultiArg() {
        assertEquals("5", eval("((lambda (x y) (add x y)) 2 3)"));
    }

    // --- Let ---

    @Test
    void let() {
        assertEquals("5", eval("(let (add x y) (x (quote 2)) (y (quote 3)))"));
    }

    @Test
    void letNested() {
        assertEquals("10", eval("(let (mul a b) (a (quote 2)) (b (quote 5)))"));
    }

    // --- Letrec (recursion) ---

    @Test
    void letrecFactorial() {
        String expr = "(letrec (fact (quote 5)) (fact (lambda (n) (cond (leq n 1) 1 (mul n (fact (sub n 1)))))))";
        assertEquals("120", eval(expr));
    }

    @Test
    void letrecFibonacci() {
        String expr = "(letrec (fib (quote 6)) (fib (lambda (n) (cond (leq n 1) n (add (fib (sub n 1)) (fib (sub n 2)))))))";
        assertEquals("8", eval(expr));
    }

    // --- Error cases ---

    @Test
    void undefinedVariableThrows() {
        assertThrows(RuntimeException.class, () -> eval("x"));
    }

    @Test
    void divisionByZeroThrows() {
        assertThrows(ArithmeticException.class, () -> eval("(div 1 0)"));
    }

    @Test
    void callNonFunctionThrows() {
        assertThrows(RuntimeException.class, () -> eval("(5 2)"));
    }
}
