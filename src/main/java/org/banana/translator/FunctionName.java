package org.banana.translator;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum FunctionName {
    QUOTE, ADD, SUB, MUL, DIV, CAR, CDR, CONS, ATOM, REM, LEQ, EQUAL, COND, LAMBDA, LET, LETREC;

    private static final Map<String, FunctionName> BY_NAME = Arrays.stream(values())
            .collect(Collectors.toMap(Enum::name, f -> f));

    public static Optional<FunctionName> fromString(String name) {
        return Optional.ofNullable(BY_NAME.get(name));
    }
}
