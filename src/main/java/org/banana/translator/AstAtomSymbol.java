package org.banana.translator;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstAtomSymbol astAtomSymbol){
            return this.getName().equals(astAtomSymbol.getName());
        }
        return false;
    }
}