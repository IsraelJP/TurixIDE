package com.turix.TurixCC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.StringReader;

public class Main extends JFrame {

    private final JTextArea inputArea = new JTextArea(8, 40);
    private final JTextArea outputArea = new JTextArea(10, 40);
    private final JButton evalButton = new JButton("Evaluar");

    public Main() {
        super("Calculadora Turix (GUI)");

        // Áreas
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);

        // Layout
        setLayout(new BorderLayout(10, 10));
        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(new JLabel("Expresiones (una por línea):"), BorderLayout.NORTH);
        top.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(new JLabel("Resultados:"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(evalButton);

        add(top, BorderLayout.NORTH);
        add(actions, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Acción botón
        evalButton.addActionListener(this::onEvaluate);

        // Atajos: Ctrl+Enter para evaluar
        inputArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl ENTER"), "EVALUAR");
        inputArea.getActionMap().put("EVALUAR", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { onEvaluate(null); }
        });

        // Ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void onEvaluate(ActionEvent e) {
        String text = inputArea.getText();
        if (text == null || text.isBlank()) return;

        String[] lines = text.split("\\R");
        for (String line : lines) {
            String expr = line.trim();
            if (expr.isEmpty()) continue;

            try {
                Turix parser = new Turix(new StringReader(expr));
                parser.Start(); // Tu Start actual solo valida
                outputArea.append(expr + "  ✔ Válida\n");
            } catch (ParseException pe) {
                outputArea.append(expr + "  ✘ Error: " + pe.getMessage() + "\n");
            } catch (Exception ex) {
                outputArea.append(expr + "  ✘ Error: " + ex.getMessage() + "\n");
            }
        }
        outputArea.append("\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
