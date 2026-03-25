package org.banana.translator;

import java.util.*;

public class SecdMachine {

    private static class DumpEntry {
        List<AstNode> s; AstList e; List<AstNode> c;
        DumpEntry(List<AstNode> s, AstList e, List<AstNode> c) {
            this.s = s; this.e = e; this.c = c;
        }
    }

    public AstNode run(AstList secdCode) {
        List<AstNode> S = new ArrayList<>();
        AstList E = new AstList();
        List<AstNode> C = new ArrayList<>(secdCode.getChildren());
        Deque<DumpEntry> D = new ArrayDeque<>();

        while (!C.isEmpty()) {
            AstNode instr = C.remove(0);
            if (!(instr instanceof AstAtomSymbol sym)) continue;
            String op = sym.getName().toUpperCase().trim();

            switch (op) {
                case "LDC": {
                    if (C.isEmpty()) throw new RuntimeException("LDC arg missing");
                    S.add(0, C.remove(0));
                    break;
                }
                case "LD": {
                    AstList pos = (AstList) C.remove(0);
                    int b = ((AstAtomNumber) pos.getChildren().get(0)).getValue();
                    int m = ((AstAtomNumber) pos.getChildren().get(1)).getValue();
                    AstList frame = (AstList) E.getChildren().get(b);
                    S.add(0, frame.getChildren().get(m));
                    break;
                }
                case "LDF": {
                    AstList closure = new AstList();
                    closure.addChild(C.remove(0));
                    closure.addChild(E);
                    S.add(0, closure);
                    break;
                }
                case "DUM": {
                    E.getChildren().add(0, new AstList());
                    break;
                }
                case "RAP": {
                    AstList closure = (AstList) S.remove(0);
                    AstList args = (AstList) S.remove(0);
                    AstList closureEnv = (AstList) closure.getChildren().get(1);
                    closureEnv.getChildren().set(0, args);
                    D.push(new DumpEntry(new ArrayList<>(S), E, new ArrayList<>(C)));
                    S.clear();
                    E = closureEnv;
                    C = new ArrayList<>(((AstList) closure.getChildren().get(0)).getChildren());
                    break;
                }
                case "AP": {
                    AstList closure = (AstList) S.remove(0);
                    AstList args = (AstList) S.remove(0);
                    D.push(new DumpEntry(new ArrayList<>(S), E, new ArrayList<>(C)));
                    S.clear();
                    AstList nEnv = new AstList();
                    nEnv.addChild(args);
                    nEnv.getChildren().addAll(((AstList) closure.getChildren().get(1)).getChildren());
                    E = nEnv;
                    C = new ArrayList<>(((AstList) closure.getChildren().get(0)).getChildren());
                    break;
                }

                case "RTN": {
                    AstNode res = S.remove(0);
                    DumpEntry d = D.pop();
                    S = d.s; E = d.e; C = d.c;
                    S.add(0, res);
                    break;
                }
                case "SEL": {
                    AstNode bT = C.remove(0);
                    AstNode bF = C.remove(0);
                    boolean cond = ((AstAtomSymbol) S.remove(0)).getName().equals("TRUE");
                    D.push(new DumpEntry(new ArrayList<>(S), E, new ArrayList<>(C)));
                    C = new ArrayList<>(((AstList)(cond ? bT : bF)).getChildren());
                    break;
                }
                case "JOIN": {
                    C = D.pop().c;
                    break;
                }
                // ВАЖНО: Исправлен порядок. На стеке [Head, Tail].
                case "CONS": {
                    AstNode head = S.remove(0); // Вершина - это Head (левый операнд)
                    AstNode tail = S.remove(0); // Под ней - Tail (правый операнд)

                    if (!(tail instanceof AstList)) throw new RuntimeException("CONS: tail must be a list");

                    AstList nl = new AstList();
                    nl.addChild(head);
                    nl.getChildren().addAll(((AstList) tail).getChildren());
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
                        for(int i=1; i<l.getChildren().size(); i++) res.addChild(l.getChildren().get(i));
                    }
                    S.add(0, res);
                    break;
                }
                case "ADD": binOp(S, Integer::sum); break;
                case "SUB": binOp(S, (x, y) -> x - y); break;
                case "MUL": binOp(S, (x, y) -> x * y); break;
                case "DIV": binOp(S, (x, y) -> x / y); break;
                case "REM": binOp(S, (x, y) -> x % y); break;
                case "LEQ": binComp(S, (x, y) -> x <= y); break;

                case "EQUAL": {
                    AstNode v1 = S.remove(0); // v1 теперь на вершине
                    AstNode v2 = S.remove(0);
                    S.add(0, new AstAtomSymbol(v1.equals(v2) ? "TRUE" : "FALSE"));
                    break;
                }
                case "ATOM": {
                    AstNode n = S.remove(0);
                    boolean isAtom = !(n instanceof AstList);
                    S.add(0, new AstAtomSymbol(isAtom ? "TRUE" : "FALSE"));
                    break;
                }
                case "STOP": return S.get(0);
                default: throw new RuntimeException("Unknown OP: " + op);
            }
        }
        return S.isEmpty() ? new AstList() : S.get(0);
    }

    // ВАЖНО: Исправлен порядок арифметики для согласованности с CONS
    private void binOp(List<AstNode> S, java.util.function.IntBinaryOperator f) {
        int v2 = ((AstAtomNumber) S.remove(0)).getValue();
        int v1 = ((AstAtomNumber) S.remove(0)).getValue();
        S.add(0, new AstAtomNumber(f.applyAsInt(v1, v2)));
    }

    private void binComp(List<AstNode> S, java.util.function.BiPredicate<Integer, Integer> f) {
        int v2 = ((AstAtomNumber) S.remove(0)).getValue();
        int v1 = ((AstAtomNumber) S.remove(0)).getValue();
        S.add(0, new AstAtomSymbol(f.test(v1, v2) ? "TRUE" : "FALSE"));
    }
}