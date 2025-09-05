package com.turix.TurixCC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;

/** Misma clase y paquete: solo UI mejorada, sin cambiar tu lógica. */
public class Main extends JFrame {

    // Editor y consola
    private final JTextArea inputArea  = new JTextArea(14, 80);
    private final JTextArea outputArea = new JTextArea(10, 80);

    // Estado
    private File currentFile = null;
    private final JLabel statusLabel = new JLabel("Listo");

    // Acciones (para reusar en menú y toolbar)
    private final Action actOpen    = new AbstractAction("Abrir…")   { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O")); }
        @Override public void actionPerformed(ActionEvent e){ onOpenFile(); } };
    private final Action actSaveAs  = new AbstractAction("Guardar…") { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S")); }
        @Override public void actionPerformed(ActionEvent e){ onSaveAs(); } };
    private final Action actCompile = new AbstractAction("Compilar"){ { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5")); }
        @Override public void actionPerformed(ActionEvent e){ onEvaluate(e); } };
    private final Action actClearOut= new AbstractAction("Limpiar"){ { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L")); }
        @Override public void actionPerformed(ActionEvent e){ outputArea.setText(""); setStatus("Consola limpiada"); } };
    private final Action actZoomIn  = new AbstractAction("Zoom +")   { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control EQUALS")); }
        @Override public void actionPerformed(ActionEvent e){ bumpFontSize(1); } };
    private final Action actZoomOut = new AbstractAction("Zoom -")   { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control MINUS")); }
        @Override public void actionPerformed(ActionEvent e){ bumpFontSize(-1); } };
    private final Action actQuit    = new AbstractAction("Salir")    { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q")); }
        @Override public void actionPerformed(ActionEvent e){ dispose(); } };

    public Main() {
        super("Calculadora Turix");

        installNiceLookAndFeel();
        tuneGlobalFonts(13); // tamaño base

        // Áreas de texto: monoespaciadas y con márgenes
        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 14);
        inputArea.setFont(mono);
        outputArea.setFont(mono);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        inputArea.setBorder(new EmptyBorder(8,8,8,8));
        outputArea.setBorder(new EmptyBorder(8,8,8,8));

        // Números de línea para el editor
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setRowHeaderView(new LineNumberView(inputArea));
        JScrollPane outputScroll = new JScrollPane(outputArea);

        // Headers
        JLabel inLabel  = styledLabel("Editor (una expresión por línea)");
        JLabel outLabel = styledLabel("Consola");

        JPanel editorPanel = new JPanel(new BorderLayout(6,6));
        editorPanel.add(inLabel, BorderLayout.NORTH);
        editorPanel.add(inputScroll, BorderLayout.CENTER);

        JPanel consolePanel = new JPanel(new BorderLayout(6,6));
        consolePanel.add(outLabel, BorderLayout.NORTH);
        consolePanel.add(outputScroll, BorderLayout.CENTER);

        // Split vertical: editor arriba, consola abajo
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, consolePanel);
        split.setResizeWeight(0.65);

        // Menú + Toolbar + Status bar
        setJMenuBar(buildMenuBar());
        JToolBar toolbar = buildToolBar();
        JPanel statusBar = buildStatusBar();

        // Atajos: Ctrl+Enter = compilar
        inputArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl ENTER"), "EVALUAR");
        inputArea.getActionMap().put("EVALUAR", actCompile);

        // Layout root
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(new EmptyBorder(10,10,10,10));
        root.add(toolbar, BorderLayout.NORTH);
        root.add(split, BorderLayout.CENTER);
        root.add(statusBar, BorderLayout.SOUTH);

        setContentPane(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }

    /* ---------- Look & Feel / Estilo ---------- */

    private void installNiceLookAndFeel() {
        try {
            // Nimbus si está disponible; si no, sistema
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(laf.getName())) {
                    UIManager.setLookAndFeel(laf.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {}
        // Colores suaves para títulos
        UIManager.put("Label.foreground", new Color(30,30,30));
    }

    private void tuneGlobalFonts(int size) {
        UIDefaults d = UIManager.getLookAndFeelDefaults();
        for (Object k : d.keySet()) {
            Object v = d.get(k);
            if (v instanceof FontUIResource) {
                FontUIResource f = (FontUIResource) v;
                d.put(k, new FontUIResource(f.getFamily(), f.getStyle(), size));
            }
        }
    }

    private JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(l.getFont().deriveFont(Font.BOLD, l.getFont().getSize2D()+1f));
        l.setBorder(new EmptyBorder(2,2,6,2));
        return l;
    }

    private JToolBar buildToolBar() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.add(button(actOpen,"📂Abrir"));
        tb.add(button(actSaveAs,"💾Guardar"));
        tb.addSeparator();
        tb.add(button(actCompile,"Compilar"));
        tb.add(button(actClearOut,"🧹Limpiar"));
        tb.addSeparator();
        tb.add(button(actZoomIn,"Zoom +"));
        tb.add(button(actZoomOut,"Zoom -"));
        return tb;
    }

    private JButton button(Action action, String text) {
        JButton b = new JButton(text);
        b.setToolTipText((String) action.getValue(Action.NAME));
        b.addActionListener(action);
        b.putClientProperty("JButton.buttonType", "roundRect"); // algunos LAFs lo usan
        return b;
    }

    private JPanel buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(6,0,0,0));
        statusLabel.setBorder(new EmptyBorder(2,8,2,8));
        p.add(new JSeparator(), BorderLayout.NORTH);
        p.add(statusLabel, BorderLayout.WEST);
        return p;
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu mArchivo = new JMenu("Archivo");
        mArchivo.add(new JMenuItem(actOpen));
        mArchivo.add(new JMenuItem(actSaveAs));
        mArchivo.addSeparator();
        mArchivo.add(new JMenuItem(actCompile));
        mArchivo.add(new JMenuItem(actClearOut));
        mArchivo.addSeparator();
        mArchivo.add(new JMenuItem(actQuit));

        JMenu mVer = new JMenu("Ver");
        mVer.add(new JMenuItem(actZoomIn));
        mVer.add(new JMenuItem(actZoomOut));

        bar.add(mArchivo);
        bar.add(mVer);
        return bar;
    }

    /* ---------- Acciones de Archivo / Compilar ---------- */

    private void onOpenFile() {
        JFileChooser chooser = new JFileChooser(currentFile != null ? currentFile.getParentFile() : null);
        chooser.setDialogTitle("Abrir archivo");
        chooser.setFileFilter(new FileNameExtensionFilter("Texto (*.txt, *.turix)", "txt", "turix"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                String content = readFile(f);
                inputArea.setText(content);
                inputArea.setCaretPosition(0);
                currentFile = f;
                setTitle("Calculadora Turix — " + f.getName());
                setStatus("Archivo cargado: " + f.getName());
            } catch (IOException ex) {
                showError("No se pudo leer el archivo:\n" + ex.getMessage(), "Error al abrir");
            }
        }
    }

    private void onSaveAs() {
        JFileChooser chooser = new JFileChooser(currentFile != null ? currentFile.getParentFile() : null);
        chooser.setDialogTitle("Guardar como…");
        chooser.setFileFilter(new FileNameExtensionFilter("Texto (*.txt)", "txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (!f.getName().contains(".")) f = new File(f.getParentFile(), f.getName()+".txt");
            try {
                writeFile(f, inputArea.getText());
                currentFile = f;
                setTitle("Calculadora Turix — " + f.getName());
                setStatus("Archivo guardado: " + f.getName());
            } catch (IOException ex) {
                showError("No se pudo guardar el archivo:\n" + ex.getMessage(), "Error al guardar");
            }
        }
    }

    /** Compila/Evalúa el contenido del inputArea línea por línea y escribe en outputArea. */
    private void onEvaluate(ActionEvent e) {
        String text = inputArea.getText();
        if (text == null || text.isBlank()) {
            setStatus("Nada que compilar");
            return;
        }
        outputArea.append("== Compilando/Analizando ==\n");
        String[] lines = text.split("\\R");
        int ok = 0, err = 0;
        for (String line : lines) {
            String expr = line.trim();
            if (expr.isEmpty()) continue;
            try {
                Turix parser = new Turix(new java.io.StringReader(expr));
                parser.Start(); // si Start() devuelve double, puedes imprimir el valor
                outputArea.append(expr + "  ✔ Válida\n");
                ok++;
            } catch (ParseException pe) {
                outputArea.append(expr + "  ✘ Sintaxis: " + pe.getMessage() + "\n");
                err++;
            } catch (TokenMgrError le) {
                outputArea.append(expr + "  ✘ Léxico: " + le.getMessage() + "\n");
                err++;
            } catch (Exception ex) {
                outputArea.append(expr + "  ✘ Error: " + ex.getMessage() + "\n");
                err++;
            }
        }
        outputArea.append(String.format("-- Listo: %d OK, %d errores --%n%n", ok, err));
        setStatus(String.format("Compilación terminada: %d OK, %d errores", ok, err));
    }

    /* ---------- Utilidades ---------- */

    private static String readFile(File f) throws IOException {
        try (InputStream in = new FileInputStream(f)) {
            byte[] data = in.readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        }
    }

    private static void writeFile(File f, String content) throws IOException {
        try (OutputStream out = new FileOutputStream(f)) {
            out.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void setStatus(String msg) { statusLabel.setText(msg); }

    private void bumpFontSize(int delta) {
        changeAreaFont(inputArea, delta);
        changeAreaFont(outputArea, delta);
    }

    private void changeAreaFont(JTextArea area, int delta) {
        Font f = area.getFont();
        int newSize = Math.max(10, f.getSize()+delta);
        area.setFont(f.deriveFont((float)newSize));
    }

    private void showError(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /* ---------- Números de línea (simple) ---------- */

    /** Barra simple de números de línea para el JTextArea del editor. */
    static class LineNumberView extends JComponent {
        private final JTextArea textArea;
        private final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        private final int padding = 6;

        LineNumberView(JTextArea textArea) {
            this.textArea = textArea;
            setFont(font);
            setForeground(new Color(120,120,120));
            setPreferredWidth();
            textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e){ repaint(); setPreferredWidth(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e){ repaint(); setPreferredWidth(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e){ repaint(); setPreferredWidth(); }
            });
            textArea.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override public void componentResized(java.awt.event.ComponentEvent e){ repaint(); }
            });
            textArea.addCaretListener(e -> repaint());
        }

        private void setPreferredWidth() {
            int lines = Math.max(1, textArea.getLineCount());
            int digits = String.valueOf(lines).length();
            int width = padding*2 + getFontMetrics(getFont()).stringWidth("9".repeat(digits));
            setPreferredSize(new Dimension(width, Integer.MAX_VALUE));
            revalidate();
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Rectangle clip = g.getClipBounds();
            g.setColor(new Color(245,245,245));
            g.fillRect(clip.x, clip.y, clip.width, clip.height);

            int base = textArea.getInsets().top;
            try {
                int start = textArea.viewToModel2D(new Point(0, clip.y));
                int end   = textArea.viewToModel2D(new Point(0, clip.y + clip.height));
                javax.swing.text.Element root = textArea.getDocument().getDefaultRootElement();
                int startLine = root.getElementIndex(start);
                int endLine   = root.getElementIndex(end);

                g.setColor(new Color(160,160,160));
                g.setFont(getFont());
                for (int line = startLine; line <= endLine; line++) {
                    Rectangle r = textArea.modelToView2D(root.getElement(line).getStartOffset()).getBounds();
                    int y = r.y + r.height - 4;
                    String num = String.valueOf(line + 1);
                    int x = getPreferredSize().width - getFontMetrics(getFont()).stringWidth(num) - padding;
                    g.drawString(num, x, y);
                }
            } catch (Exception ignored) {}
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignore) {}
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
