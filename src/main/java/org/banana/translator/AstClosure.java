package org.banana.translator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class AstClosure implements AstNode {
    private final List<String> argNames; // Имена аргументов (x1...xk)
    private final AstNode body;          // Тело функции
    private final LispContext context;   // Контекст создания (Snapshot)

    @Override
    public String reconstruct() {
        return "Closure(...)";
    }

    @Override
    public String toString() {
        return "Closure(args=" + argNames + ")";
    }


}