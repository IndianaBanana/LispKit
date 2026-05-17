package org.banana.translator;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class AstAtomSymbol implements AstNode {

    private final String name;

    public AstAtomSymbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String reconstruct() {
        return name;
    }

    @Override
    public String toString() {
        return "Sym(" + name + ")";
    }
}