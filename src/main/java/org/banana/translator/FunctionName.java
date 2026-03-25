package org.banana.translator;

import lombok.Getter;

/**
 * Created by Banana on 05.12.2025
 */
public enum FunctionName {
    QUOTE("QUOTE"),
    ADD("ADD"),
    SUB("SUB"),
    MUL("MUL"),
    DIV("DIV"),
    CAR("CAR"),
    CDR("CDR"),
    CONS("CONS"),
    ATOM("ATOM"),
    REM("REM"),
    LEQ("LEQ"),
    EQUAL("EQUAL"),
    COND("COND"),
    LAMBDA("LAMBDA"),
    LET("LET"),
    LETREC("LETREC");
    @Getter
    private final String description;

    FunctionName(String description){
        this.description = description;
    }

}
