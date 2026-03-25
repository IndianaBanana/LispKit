package org.banana.translator;

import lombok.EqualsAndHashCode;

import java.util.Objects;
@EqualsAndHashCode
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

}