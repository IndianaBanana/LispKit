package org.banana.translator;

import com.formdev.flatlaf.FlatLightLaf;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

public class LispKitGui {

    private static RSyntaxTextArea inputArea;
    private static JTextArea outputArea;

    public static void main(String[] args) {
        // Установка современного FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(LispKitGui::createGUI);
    }

    private static void createGUI() {
        JFrame frame = new JFrame("LispKit Syntax Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        JPanel mainPanel = new JPanel(new net.miginfocom.swing.MigLayout(
                "fill, insets 10", "[grow 70][grow 30]", "[][grow][]"
        ));

        // Title
        JLabel title = new JLabel("LispKit — Lexical and Syntax Analysis", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 16));
        mainPanel.add(title, "span, wrap, center");

        // Input editor
        inputArea = new RSyntaxTextArea(20, 60);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);

        RTextScrollPane inputScroll = new RTextScrollPane(inputArea);
        inputScroll.setLineNumbersEnabled(true);
        mainPanel.add(inputScroll, "cell 0 1, grow");

        // Output area
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(245, 245, 245));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane outputScroll = new JScrollPane(outputArea);
        mainPanel.add(outputScroll, "cell 1 1, grow");

        // Button
        JButton checkBtn = new JButton("Check Syntax");
        checkBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        checkBtn.setBackground(new Color(30, 144, 255));
        checkBtn.setForeground(Color.WHITE);
        mainPanel.add(checkBtn, "cell 0 2 2 1, center, wrap");

        // Button action
        checkBtn.addActionListener(e -> {
            String code = inputArea.getText().trim();
            if (code.isEmpty()) {
                outputArea.setText("ERROR: Empty input");
                outputArea.setForeground(Color.RED);
                return;
            }

            try {
                // Получаем строку, собранную из AST
                String reconstructed = LispKitParserApp.parseAndGetAstString(code);

                outputArea.setText("AST RECONSTRUCTION:\n" + reconstructed);
                // Используем темно-синий цвет, чтобы отличалось от обычного текста
                outputArea.setForeground(new Color(0, 0, 139));
            } catch (RuntimeException ex) {
                outputArea.setText("SYNTAX ERROR:\n" + ex.getMessage());
                outputArea.setForeground(Color.RED);
            }
        });

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
