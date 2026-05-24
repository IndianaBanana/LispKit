# LispKit

Интерпретатор и компилятор подмножества языка Lisp на Java.

Реализует два независимых режима исполнения для одного и того же AST:
1. **Tree-walking interpreter** — рекурсивный обход AST с вычислением на месте.
2. **Compiler + SECD machine** — компиляция в байткод и исполнение на классической абстрактной машине (Stack, Environment, Control, Dump; Peter Landin, 1964).

---

## Возможности языка

| Конструкция       | Пример                                              |
|-------------------|-----------------------------------------------------|
| Арифметика        | `(add 2 3)` `(mul (sub 10 4) 2)`                   |
| Сравнения         | `(leq 3 5)` `(equal x y)`                          |
| Списки            | `(cons 1 (quote (2 3)))` `(car lst)` `(cdr lst)`   |
| Условие           | `(cond (leq x 0) 0 x)`                             |
| Цитирование       | `(quote (a b c))`                                   |
| Лямбда            | `((lambda (x y) (add x y)) 3 4)`                   |
| Локальные имена   | `(let (add a b) (a (quote 3)) (b (quote 7)))`      |
| Рекурсия          | `(letrec (fact (quote 5)) (fact (lambda (n) ...)))` |

---

## Архитектура

```
src/main/
├── antlr4/LispKit.g4          # Грамматика (ANTLR4, case-insensitive keywords)
└── java/org/banana/
    ├── grammar/               # Сгенерированный ANTLR4 парсер
    └── translator/
        ├── LispAstVisitor     # CST → AST (Visitor pattern)
        ├── AstNode / AstList / AstAtomNumber / AstAtomSymbol / AstClosure
        ├── LispEvaluator      # Tree-walking interpreter
        ├── LispCompiler       # AST → SECD bytecode
        ├── SecdMachine        # Исполнение байткода (S, E, C, D регистры)
        ├── LispContext        # Лексическое окружение (цепочка фреймов)
        └── LispKitGui         # Swing GUI с подсветкой синтаксиса
```

### Как работает SECD-машина

Компилятор переводит AST в список инструкций. Машина держит четыре регистра:

- **S** (Stack) — стек операндов
- **E** (Environment) — список фреймов с переменными
- **C** (Control) — очередь инструкций (`ArrayDeque`)
- **D** (Dump) — стек сохранённых состояний (для вызовов функций)

Ключевые опкоды: `LDC`, `LD`, `LDF`, `AP`, `RTN`, `SEL`, `JOIN`, `DUM`, `RAP`, `CONS`, `CAR`, `CDR`, `ATOM`, `STOP`.

---

## Сборка и запуск

Требования: **Java 17+**, **Maven 3.6+**.

```bash
# Сборка (генерирует ANTLR парсер и компилирует)
mvn clean compile

# Запуск тестов (53 теста: interpreter + SECD machine)
mvn test

# Сборка fat JAR с GUI
mvn package

java -jar target/Lisp-1.0-SNAPSHOT.jar
```

---

## Примеры

### let — локальные привязки

Синтаксис: `(let тело (перем1 знач1) (перем2 знач2) ...)`.
Значения вычисляются во **внешнем** контексте, тело — в локальном.

**Простая арифметика с именованными переменными:**
```lisp
(let (add a b)
  (a (quote 3))
  (b (quote 7)))
```
Результат: `10`

**Несколько переменных в теле:**
```lisp
(let (mul (add a b) (sub a b))
  (a (quote 5))
  (b (quote 3)))
```
Результат: `16` — вычисляет `(a+b) * (a-b)`

**Локальная функция через lambda:**
```lisp
(let (double (quote 6))
  (double (lambda (x) (mul x 2))))
```
Результат: `12` — `double` связывается с лямбдой и применяется к `6`

**Максимум двух чисел:**
```lisp
(let (cond (leq a b) b a)
  (a (quote 10))
  (b (quote 7)))
```
Результат: `10`

**Вложенный let — демонстрация лексической области видимости:**
```lisp
(let
  (let (add x z)
    (z (quote 1)))
  (x (quote 5)))
```
Результат: `6` — внутренний `let` видит `x` из внешнего, добавляет свой `z`

---

### letrec — рекурсивные привязки

**Факториал через рекурсию:**
```lisp
(letrec (fact (quote 5))
  (fact (lambda (n)
    (cond (leq n 1)
      1
      (mul n (fact (sub n 1)))))))
```
Результат: `120`

**Числа Фибоначчи:**
```lisp
(letrec (fib (quote 10))
  (fib (lambda (n)
    (cond (leq n 1)
      n
      (add (fib (sub n 1))
           (fib (sub n 2)))))))
```
Результат: `55`

---

## Стек технологий

- Java 17 (pattern matching `instanceof`, sealed-like AST иерархия)
- ANTLR4 — генерация лексера и парсера из грамматики
- Lombok — `@Slf4j`, `@Getter`, `@EqualsAndHashCode`, `@RequiredArgsConstructor`
- SLF4J + Logback — логирование
- Swing + FlatLaf + RSyntaxTextArea + MigLayout — GUI
- JUnit 5 — тесты
- Maven — сборка и зависимости
