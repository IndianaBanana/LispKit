package org.banana.translator;

import com.formdev.flatlaf.FlatLightLaf;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.AutoCompletionListener; // ДОБАВЛЕНО: Правильный импорт для слушателя
import org.fife.ui.autocomplete.AutoCompletionEvent; // ДОБАВЛЕНО: Импорт для события
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.autocomplete.ShorthandCompletion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
public class LispKitGui {

    private static RSyntaxTextArea inputArea;
    private static JTextArea outputArea;
    private static JScrollPane outputScroll;
    private static JRadioButton evalRadio;
    private static JRadioButton compileRadio;
    private static JButton toggleOutputBtn;
    private static AutoCompletion ac; // Сделали static для доступа в listener
    private static CompletionProvider provider; // Для проверки completions в listener

    public static void main(String[] args) {
        // Установка FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(LispKitGui::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("LispKit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        // Layout: Вертикальный
        JPanel mainPanel = new JPanel(new net.miginfocom.swing.MigLayout(
                "fill, insets 10", "[grow]", "[][grow][][][grow]"
        ));

        // Title
        JLabel title = new JLabel("LispKit", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 16));
        mainPanel.add(title, "span, wrap, center");

        // Input editor
        inputArea = new RSyntaxTextArea(20, 60);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LISP);

        // Настройка автодополнения
        provider = createCompletionProvider();
        ac = new AutoCompletion(provider);
        ac.setAutoCompleteEnabled(true);
        ac.setAutoActivationEnabled(true);
        ac.setAutoActivationDelay(0); // Мгновенно
        ac.setAutoCompleteSingleChoices(false);
        ac.setShowDescWindow(true);
        ac.setChoicesWindowSize(200, 200);
        ac.setTriggerKey(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_DOWN_MASK));

        // Отладка: Listener для логирования событий автодополнения (исправлено на правильные методы)
        ac.addAutoCompletionListener(new AutoCompletionListener() {
            @Override
            public void autoCompleteUpdate(AutoCompletionEvent autoCompletionEvent) {

            }
        });

        ac.install(inputArea);

        // ДОБАВЛЕНО: "Тупой" KeyListener для 100% автоматического показа popup
        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isLetter(keyChar)) { // Только на буквах
                    try {
                        // Получаем текущий текст и позицию курсора
                        int caretPos = inputArea.getCaretPosition();
                        String text = inputArea.getText(0, caretPos);
                        // Находим последнее слово (до пробела/скобки)
                        int wordStart = Math.max(text.lastIndexOf(' '), text.lastIndexOf('(')) + 1;
                        String currentWord = text.substring(wordStart).trim();

                        // Если слово не пустое и есть completions — показываем popup вручную
                        if (currentWord.length() >= 1) {
                            List<?> completions = provider.getCompletions(inputArea);
                            if (!completions.isEmpty()) {
                                ac.doCompletion(); // Форсируем показ
//                                log.info("KeyListener: Forced popup for word '" + currentWord + "'");
                            }
                        }
                    } catch (Exception ex) {
                        log.error("Error in KeyListener: " + ex.getMessage());
                    }
                }
            }
        });

        RTextScrollPane inputScroll = new RTextScrollPane(inputArea);
        inputScroll.setLineNumbersEnabled(true);
        mainPanel.add(inputScroll, "cell 0 1, grow, wrap");

        // Радио-панель
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel modeLabel = new JLabel("Mode:");
        modeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        evalRadio = new JRadioButton("Parse and Evaluate");
        compileRadio = new JRadioButton("Parse, Compile and Run");
        compileRadio.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(evalRadio);
        group.add(compileRadio);
        radioPanel.add(modeLabel);
        radioPanel.add(evalRadio);
        radioPanel.add(compileRadio);
        mainPanel.add(radioPanel, "cell 0 2, center, wrap");

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton checkBtn = new JButton("Evaluate");
        checkBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        checkBtn.setBackground(new Color(30, 144, 255));
        checkBtn.setForeground(Color.WHITE);
        buttonPanel.add(checkBtn);

        toggleOutputBtn = new JButton("Hide Output");
        toggleOutputBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        toggleOutputBtn.setBackground(new Color(100, 100, 100));
        toggleOutputBtn.setForeground(Color.WHITE);
        buttonPanel.add(toggleOutputBtn);

        mainPanel.add(buttonPanel, "cell 0 3, center, wrap");

        // Output
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(245, 245, 245));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        outputScroll = new JScrollPane(outputArea);
        mainPanel.add(outputScroll, "cell 0 4, grow, wrap");

        // Action Evaluate
        checkBtn.addActionListener(e -> {
            String code = inputArea.getText().trim();
            if (code.isEmpty()) {
                outputArea.setText("ERROR: Empty input");
                outputArea.setForeground(Color.RED);
                return;
            }

            try {
                String evaluationResult;
                if (evalRadio.isSelected()) {
                    evaluationResult = LispKitParserApp.parseAndEvaluate(code);
                } else {
                    evaluationResult = LispKitParserApp.parseAndCompileAndRun(code);
                }

                outputArea.setText("EVALUATION RESULT:\n" + evaluationResult);
                outputArea.setForeground(new Color(0, 0, 139));
            } catch (RuntimeException ex) {
                outputArea.setText("RUNTIME ERROR:\n" + ex.getMessage());
                outputArea.setForeground(Color.RED);
                ex.printStackTrace();
            }
        });

        // Action Toggle
        toggleOutputBtn.addActionListener(e -> {
            boolean isVisible = outputScroll.isVisible();
            outputScroll.setVisible(!isVisible);
            toggleOutputBtn.setText(isVisible ? "Show Output" : "Hide Output");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static CompletionProvider createCompletionProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();

        // --- Сложные конструкции (Шаблоны) ---

        // LET: Каркас для локальных переменных
        provider.addCompletion(new ShorthandCompletion(provider, "let",
                "(let (add X Y)\n\t(X (QUOTE 3))\n\t(Y (QUOTE 7))\n)",
                "Локальные привязки (каркас)"));

        // LETREC: Каркас для рекурсивных функций
        provider.addCompletion(new ShorthandCompletion(provider, "letrec",
                "(letrec (\n\t(func (lambda (args) body))\n)\n\t(body)\n)",
                "Рекурсивные привязки (каркас)"));

        // LAMBDA: Каркас функции
        provider.addCompletion(new ShorthandCompletion(provider, "lambda",
                "(lambda (arg1 arg2)\n\t(body)\n)",
                "Анонимная функция"));

        // COND: Каркас условий
        provider.addCompletion(new ShorthandCompletion(provider, "cond",
                "(cond\n\t(condition1 expr1)\n\t(condition2 expr2)\n\t(QUOTE TRUE else_expr)\n)",
                "Условный оператор"));

        // --- Базовые функции (с подсказкой скобок) ---

        // QUOTE
        provider.addCompletion(new ShorthandCompletion(provider, "quote",
                "(quote x)",
                "Цитирует выражение"));

        // Списки
        provider.addCompletion(new ShorthandCompletion(provider, "cons",
                "(cons head tail)", "Создаёт пару"));
        provider.addCompletion(new ShorthandCompletion(provider, "car",
                "(car list)", "Первый элемент списка"));
        provider.addCompletion(new ShorthandCompletion(provider, "cdr",
                "(cdr list)", "Хвост списка"));
        provider.addCompletion(new ShorthandCompletion(provider, "atom",
                "(atom x)", "Проверка на атом"));

        // Арифметика и сравнение
        provider.addCompletion(new ShorthandCompletion(provider, "add",
                "(add x y)", "Сложение"));
        provider.addCompletion(new ShorthandCompletion(provider, "sub",
                "(sub x y)", "Вычитание"));
        provider.addCompletion(new ShorthandCompletion(provider, "mul",
                "(mul x y)", "Умножение"));
        provider.addCompletion(new ShorthandCompletion(provider, "div",
                "(div x y)", "Деление"));
        provider.addCompletion(new ShorthandCompletion(provider, "rem",
                "(rem x y)", "Остаток"));
        provider.addCompletion(new ShorthandCompletion(provider, "leq",
                "(leq x y)", "Меньше или равно"));
        provider.addCompletion(new ShorthandCompletion(provider, "equal",
                "(equal x y)", "Равенство"));

        return provider;
    }
}