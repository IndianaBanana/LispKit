package org.banana.translator;

public class AstAtomNumber implements AstNode {
    private final int value;

    public AstAtomNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String reconstruct() {
        return String.valueOf(value);
    }
    
    @Override
    public String toString() {
        return "Num(" + value + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AstAtomNumber astAtomNumber){
            return this.getValue() == astAtomNumber.getValue();
        }
        return false;
    }
}