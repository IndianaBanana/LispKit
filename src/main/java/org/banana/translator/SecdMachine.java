package org.banana.translator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SecdMachine {

    private static AstList expectList(AstNode node, String context) {
        if (!(node instanceof AstList l))
            throw new RuntimeException(context + ": expected list, got " + node);
        return l;
    }

    private static AstAtomNumber expectNumber(AstNode node, String context) {
        if (!(node instanceof AstAtomNumber n))
            throw new RuntimeException(context + ": expected number, got " + node);
        return n;
    }

    public AstNode run(AstList secdCode) {
        List<AstNode> S = new ArrayList<>();
        AstList E = new AstList();
        Deque<AstNode> C = new ArrayDeque<>(secdCode.getChildren());
        Deque<DumpEntry> D = new ArrayDeque<>();

        while (!C.isEmpty()) {
            AstNode instr = C.removeFirst();
            if (!(instr instanceof AstAtomSymbol sym)) continue;
            String op = sym.getName().toUpperCase().trim();

            switch (op) {
                case "LDC": {
                    if (C.isEmpty()) throw new RuntimeException("LDC: missing argument");
                    S.add(0, C.removeFirst());
                    break;
                }
                case "LD": {
                    AstList pos = expectList(C.removeFirst(), "LD position");
                    int b = expectNumber(pos.getChildren().get(0), "LD frame index").getValue();
                    int m = expectNumber(pos.getChildren().get(1), "LD slot index").getValue();
                    AstList frame = expectList(E.getChildren().get(b), "LD frame");
                    S.add(0, frame.getChildren().get(m));
                    break;
                }
                case "LDF": {
                    AstList closure = new AstList();
                    closure.addChild(C.removeFirst());
                    closure.addChild(E);
                    S.add(0, closure);
                    break;
                }
                case "DUM": {
                    E.getChildren().add(0, new AstList());
                    break;
                }
                case "RAP": {
                    AstList closure = expectList(S.remove(0), "RAP closure");
                    AstList args = expectList(S.remove(0), "RAP args");
                    AstList closureEnv = expectList(closure.getChildren().get(1), "RAP closure env");
                    closureEnv.getChildren().set(0, args);
                    D.push(new DumpEntry(new ArrayList<>(S), E, C));
                    S.clear();
                    E = closureEnv;
                    C = new ArrayDeque<>(expectList(closure.getChildren().get(0), "RAP body").getChildren());
                    break;
                }
                case "AP": {
                    AstList closure = expectList(S.remove(0), "AP closure");
                    AstList args = expectList(S.remove(0), "AP args");
                    D.push(new DumpEntry(new ArrayList<>(S), E, C));
                    S.clear();
                    AstList nEnv = new AstList();
                    nEnv.addChild(args);
                    nEnv.getChildren().addAll(expectList(closure.getChildren().get(1), "AP closure env").getChildren());
                    E = nEnv;
                    C = new ArrayDeque<>(expectList(closure.getChildren().get(0), "AP body").getChildren());
                    break;
                }
                case "RTN": {
                    AstNode res = S.remove(0);
                    DumpEntry d = D.pop();
                    S = d.s;
                    E = d.e;
                    C = d.c;
                    S.add(0, res);
                    break;
                }
                case "SEL": {
                    AstNode bT = C.removeFirst();
                    AstNode bF = C.removeFirst();
                    AstNode top = S.remove(0);
                    if (!(top instanceof AstAtomSymbol cond))
                        throw new RuntimeException("SEL: condition must be TRUE or FALSE, got: " + top);
                    boolean branch = cond.getName().equals("TRUE");
                    D.push(new DumpEntry(new ArrayList<>(S), E, C));
                    C = new ArrayDeque<>(expectList(branch ? bT : bF, "SEL branch").getChildren());
                    break;
                }
                case "JOIN": {
                    C = D.pop().c;
                    break;
                }
                case "CONS": {
                    AstNode head = S.remove(0);
                    AstList tail = expectList(S.remove(0), "CONS tail");
                    AstList nl = new AstList();
                    nl.addChild(head);
                    nl.getChildren().addAll(tail.getChildren());
                    S.add(0, nl);
                    break;
                }
                case "CAR": {
                    AstNode node = S.remove(0);
                    if (node instanceof AstList l && !l.getChildren().isEmpty()) {
                        S.add(0, l.getChildren().get(0));
                    } else {
                        S.add(0, new AstList());
                    }
                    break;
                }
                case "CDR": {
                    AstNode node = S.remove(0);
                    AstList res = new AstList();
                    if (node instanceof AstList l && l.getChildren().size() > 1) {
                        for (int i = 1; i < l.getChildren().size(); i++) res.addChild(l.getChildren().get(i));
                    }
                    S.add(0, res);
                    break;
                }
                case "ADD":
                    binOp(S, Integer::sum);
                    break;
                case "SUB":
                    binOp(S, (x, y) -> x - y);
                    break;
                case "MUL":
                    binOp(S, (x, y) -> x * y);
                    break;
                case "DIV":
                    binOp(S, (x, y) -> x / y);
                    break;
                case "REM":
                    binOp(S, (x, y) -> x % y);
                    break;
                case "LEQ":
                    binComp(S, (x, y) -> x <= y);
                    break;
                case "EQUAL": {
                    AstNode v1 = S.remove(0);
                    AstNode v2 = S.remove(0);
                    S.add(0, new AstAtomSymbol(v1.equals(v2) ? "TRUE" : "FALSE"));
                    break;
                }
                case "ATOM": {
                    AstNode n = S.remove(0);
                    S.add(0, new AstAtomSymbol(!(n instanceof AstList) ? "TRUE" : "FALSE"));
                    break;
                }
                case "STOP":
                    return S.get(0);
                default:
                    throw new RuntimeException("Unknown opcode: " + op);
            }
        }
        return S.isEmpty() ? new AstList() : S.get(0);
    }

    private void binOp(List<AstNode> S, java.util.function.IntBinaryOperator f) {
        int v2 = expectNumber(S.remove(0), "binary op right operand").getValue();
        int v1 = expectNumber(S.remove(0), "binary op left operand").getValue();
        S.add(0, new AstAtomNumber(f.applyAsInt(v1, v2)));
    }

    private void binComp(List<AstNode> S, java.util.function.BiPredicate<Integer, Integer> f) {
        int v2 = expectNumber(S.remove(0), "comparison right operand").getValue();
        int v1 = expectNumber(S.remove(0), "comparison left operand").getValue();
        S.add(0, new AstAtomSymbol(f.test(v1, v2) ? "TRUE" : "FALSE"));
    }

    private static class DumpEntry {

        List<AstNode> s;
        AstList e;
        Deque<AstNode> c;

        DumpEntry(List<AstNode> s, AstList e, Deque<AstNode> c) {
            this.s = s;
            this.e = e;
            this.c = new ArrayDeque<>(c);
        }
    }
}
