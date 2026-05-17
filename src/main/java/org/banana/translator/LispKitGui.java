package org.banana.translator;

import com.formdev.flatlaf.FlatLightLaf;
import lombok.extern.slf4j.Slf4j;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class LispKitGui {

    private RSyntaxTextArea inputArea;
    private JTextArea outputArea;
    private JScrollPane outputScroll;
    private JRadioButton evalRadio;
    private JRadioButton compileRadio;
    private JButton toggleOutputBtn;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            log.error("Failed to set look and feel", e);
        }
        SwingUtilities.invokeLater(() -> new LispKitGui().createGUI());
    }

    private void createGUI() {
        JFrame frame = new JFrame("LispKit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 800);

        JPanel mainPanel = new JPanel(new net.miginfocom.swing.MigLayout(
                "fill, insets 10", "[grow][220!]", "[][grow 40][][][grow 60]"
        ));

        // Title
        JLabel title = new JLabel("LispKit", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 16));
        mainPanel.add(title, "span 2, wrap, center");

        // Input editor
        inputArea = new RSyntaxTextArea(20, 60);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LISP);
        inputArea.setTabSize(2);

        RTextScrollPane inputScroll = new RTextScrollPane(inputArea);
        inputScroll.setLineNumbersEnabled(true);
        mainPanel.add(inputScroll, "cell 0 1, grow");

        // Reference panel — занимает правую колонку на все строки (1-4)
        mainPanel.add(buildReferencePanel(), "cell 1 1 1 4, growy, top");

        // Mode selector
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel modeLabel = new JLabel("Mode:");
        modeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        evalRadio = new JRadioButton("Interpret");
        compileRadio = new JRadioButton("Compile + Run (SECD)");
        compileRadio.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(evalRadio);
        group.add(compileRadio);
        radioPanel.add(modeLabel);
        radioPanel.add(evalRadio);
        radioPanel.add(compileRadio);
        mainPanel.add(radioPanel, "cell 0 2, center, wrap");

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));

        JButton runBtn = new JButton("Run  (Ctrl+Enter)");
        runBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        runBtn.setBackground(new Color(30, 144, 255));
        runBtn.setForeground(Color.WHITE);
        buttonPanel.add(runBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        buttonPanel.add(clearBtn);

        toggleOutputBtn = new JButton("Hide Output");
        toggleOutputBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        toggleOutputBtn.setBackground(new Color(100, 100, 100));
        toggleOutputBtn.setForeground(Color.WHITE);
        buttonPanel.add(toggleOutputBtn);

        mainPanel.add(buttonPanel, "cell 0 3, center, wrap");

        // Output
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(245, 245, 245));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        outputScroll = new JScrollPane(outputArea);
        mainPanel.add(outputScroll, "cell 0 4, grow, wrap, height 180::");

        // Ctrl+Enter shortcut
        inputArea.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK),
                "run"
        );
        inputArea.getActionMap().put("run", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                runBtn.doClick();
            }
        });

        // Actions
        runBtn.addActionListener(e -> evaluate());
        clearBtn.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
            inputArea.requestFocus();
        });
        toggleOutputBtn.addActionListener(e -> {
            boolean visible = outputScroll.isVisible();
            outputScroll.setVisible(!visible);
            toggleOutputBtn.setText(visible ? "Show Output" : "Hide Output");
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        inputArea.requestFocus();
    }

    private void evaluate() {
        String code = inputArea.getText().trim();
        if (code.isEmpty()) {
            outputArea.setText("ERROR: Empty input");
            outputArea.setForeground(Color.RED);
            return;
        }
        try {
            String result = evalRadio.isSelected()
                    ? LispKitParserApp.parseAndEvaluate(code)
                    : LispKitParserApp.parseAndCompileAndRun(code);
            outputArea.setText(result);
            outputArea.setForeground(new Color(100, 100, 100));
        } catch (RuntimeException ex) {
            outputArea.setText("ERROR: " + ex.getMessage());
            outputArea.setForeground(Color.RED);
            log.error("Evaluation error", ex);
        }
    }

    private JPanel buildReferencePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Quick Reference",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)
        ));

        String ref =
                "── Арифметика ──────────\n" +
                "(add x y)\n" +
                "(sub x y)\n" +
                "(mul x y)\n" +
                "(div x y)\n" +
                "(rem x y)\n" +
                "\n── Сравнения ───────────\n" +
                "(leq x y)   → TRUE/FALSE\n" +
                "(equal x y) → TRUE/FALSE\n" +
                "\n── Списки ──────────────\n" +
                "(cons head tail)\n" +
                "(car list)\n" +
                "(cdr list)\n" +
                "(atom x)    → TRUE/FALSE\n" +
                "(quote x)\n" +
                "\n── Управление ──────────\n" +
                "(cond p then else)\n" +
                "\n── Функции ─────────────\n" +
                "(lambda (a b)\n  body)\n" +
                "\n── Привязки ────────────\n" +
                "(let body\n  (x (quote 3))\n  (y (quote 7)))\n" +
                "\n── Рекурсия ────────────\n" +
                "(letrec body\n  (f (lambda (n)\n    ...)))";

        JTextArea refArea = new JTextArea(ref);
        refArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        refArea.setEditable(false);
        refArea.setOpaque(false);
        refArea.setFocusable(false);
        refArea.setWrapStyleWord(false);
        refArea.setLineWrap(false);
        panel.add(refArea);

        return panel;
    }
}
