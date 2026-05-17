package org.banana.translator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecdMachineTest {

    private String run(String input) {
        return LispKitParserApp.compileAndRun(input).reconstruct();
    }

    // --- Arithmetic ---

    @Test
    void add() {
        assertEquals("5", run("(add 2 3)"));
    }

    @Test
    void sub() {
        assertEquals("1", run("(sub 3 2)"));
    }

    @Test
    void mul() {
        assertEquals("6", run("(mul 2 3)"));
    }

    @Test
    void div() {
        assertEquals("3", run("(div 9 3)"));
    }

    @Test
    void rem() {
        assertEquals("1", run("(rem 7 3)"));
    }

    @Test
    void nestedArithmetic() {
        assertEquals("10", run("(add (mul 2 3) (sub 5 1))"));
    }

    // --- Comparison ---

    @Test
    void leqTrue() {
        assertEquals("TRUE", run("(leq 2 3)"));
    }

    @Test
    void leqFalse() {
        assertEquals("FALSE", run("(leq 4 3)"));
    }

    @Test
    void equalTrue() {
        assertEquals("TRUE", run("(equal 5 5)"));
    }

    @Test
    void equalFalse() {
        assertEquals("FALSE", run("(equal 5 6)"));
    }

    // --- Quote ---

    @Test
    void quoteSymbol() {
        assertEquals("hello", run("(quote hello)"));
    }

    @Test
    void quoteList() {
        assertEquals("(1 2 3)", run("(quote (1 2 3))"));
    }

    // --- List operations ---

    @Test
    void car() {
        assertEquals("1", run("(car (quote (1 2 3)))"));
    }

    @Test
    void cdr() {
        assertEquals("(2 3)", run("(cdr (quote (1 2 3)))"));
    }

    @Test
    void cons() {
        assertEquals("(1 2 3)", run("(cons 1 (quote (2 3)))"));
    }

    @Test
    void atomOnNumber() {
        assertEquals("TRUE", run("(atom 5)"));
    }

    @Test
    void atomOnList() {
        assertEquals("FALSE", run("(atom (quote (1 2)))"));
    }

    // --- Cond / SEL ---

    @Test
    void condTrue() {
        assertEquals("1", run("(cond (leq 1 2) 1 2)"));
    }

    @Test
    void condFalse() {
        assertEquals("2", run("(cond (leq 3 2) 1 2)"));
    }

    // --- Lambda / AP ---

    @Test
    void lambdaApply() {
        assertEquals("7", run("((lambda (x) (add x 2)) 5)"));
    }

    @Test
    void lambdaMultiArg() {
        assertEquals("5", run("((lambda (x y) (add x y)) 2 3)"));
    }

    // --- Let ---

    @Test
    void let() {
        assertEquals("5", run("(let (add x y) (x (quote 2)) (y (quote 3)))"));
    }

    // --- Letrec / RAP ---

    @Test
    void letrecFactorial() {
        String expr = "(letrec (fact (quote 5)) (fact (lambda (n) (cond (leq n 1) 1 (mul n (fact (sub n 1)))))))";
        assertEquals("120", run(expr));
    }

    @Test
    void letrecFibonacci() {
        String expr = "(letrec (fib (quote 6)) (fib (lambda (n) (cond (leq n 1) n (add (fib (sub n 1)) (fib (sub n 2)))))))";
        assertEquals("8", run(expr));
    }
}
