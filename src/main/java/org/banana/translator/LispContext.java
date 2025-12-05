package org.banana.translator;

import java.util.HashMap;
import java.util.Map;

public class LispContext {
    // Это "текущий лист бумаги" — локальные переменные этого уровня
    private final Map<String, AstNode> values = new HashMap<>();

    // Это ссылка на "предыдущий лист" (хвост списка)
    // Если parent == null, значит это глобальный уровень
    private final LispContext parent;

    // Конструктор для Глобального контекста (самый верхний уровень)
    public LispContext() {
        this.parent = null;
    }

    // Конструктор для Локального контекста (создается при входе в функцию/LET)
    public LispContext(LispContext parent) {
        this.parent = parent;
    }

    public AstNode get(String name) {
        // 1. Сначала ищем у себя (локально)
        if (values.containsKey(name)) {
            return values.get(name);
        }
        
        // 2. Если не нашли — спрашиваем у родителя (рекурсия)
        if (parent != null) {
            return parent.get(name);
        }

        // 3. Если родителя нет, а переменную не нашли — значит её не существует
        throw new RuntimeException("Undefined variable: " + name);
    }

    // Метод для записи переменной в ТЕКУЩИЙ уровень
    public void define(String name, AstNode value) {
        values.put(name, value);
    }
    
    @Override
    public String toString() {
        return "Context{keys=" + values.keySet() + ", parent=" + (parent != null ? "exist" : "null") + "}";
    }
}