package com.turix.TurixCC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import Semantico.*;

public class Main extends JFrame {

    // Editor + salidas
    private final JTextArea inputArea  = new JTextArea(14, 80);
    private final JTextArea lexArea    = new JTextArea(10, 80);
    private final JTextArea synArea    = new JTextArea(10, 80);
    private final JTextArea semArea    = new JTextArea(10, 80);

    // Estado
    private File currentFile = null;
    private final JLabel statusLabel = new JLabel("Listo");

    // Acciones
    private final Action actOpen    = new AbstractAction("Abrir…")   { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O")); }
        @Override public void actionPerformed(ActionEvent e){ onOpenFile(); } };
    private final Action actSaveAs  = new AbstractAction("Guardar…") { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S")); }
        @Override public void actionPerformed(ActionEvent e){ onSaveAs(); } };
    private final Action actCompile = new AbstractAction("Compilar"){ { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5")); }
        @Override public void actionPerformed(ActionEvent e){ onEvaluate(e); } };
    private final Action actClearOut= new AbstractAction("Limpiar"){ { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L")); }
        @Override public void actionPerformed(ActionEvent e){
            lexArea.setText("");
            synArea.setText("");
            setStatus("Pantallas limpiadas");
        } };
    private final Action actZoomIn  = new AbstractAction("Zoom +")   { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control EQUALS")); }
        @Override public void actionPerformed(ActionEvent e){ bumpFontSize(1); } };
    private final Action actZoomOut = new AbstractAction("Zoom -")   { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control MINUS")); }
        @Override public void actionPerformed(ActionEvent e){ bumpFontSize(-1); } };
    private final Action actQuit    = new AbstractAction("Salir")    { { putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q")); }
        @Override public void actionPerformed(ActionEvent e){ dispose(); } };

    public Main() {
        super("Compilador Turix");

        installNiceLookAndFeel();
        tuneGlobalFonts(13);

        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 14);
        inputArea.setFont(mono);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(new EmptyBorder(8,8,8,8));

        lexArea.setFont(mono);
        synArea.setFont(mono);
        semArea.setFont(mono);
        lexArea.setEditable(true);
        synArea.setEditable(true);
        semArea.setEditable(true);
        lexArea.setBorder(new EmptyBorder(8,8,8,8));
        synArea.setBorder(new EmptyBorder(8,8,8,8));
        semArea.setBorder(new EmptyBorder(8,8,8,8));
        // Scrolls
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setRowHeaderView(new LineNumberView(inputArea));
        JScrollPane lexScroll = new JScrollPane(lexArea);
        JScrollPane synScroll = new JScrollPane(synArea);
        JScrollPane semScroll = new JScrollPane(semArea);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Léxico", lexScroll);
        tabs.addTab("Sintáctico", synScroll);
        tabs.addTab("Semántico", semScroll);
        // Editor panel
        JPanel editorPanel = new JPanel(new BorderLayout(6,6));
       // Línea donde creas el label superior del editor:
        editorPanel.add(styledLabel("Editor (programa completo; Ctrl+Enter para compilar)"), BorderLayout.NORTH);
    
        editorPanel.add(inputScroll, BorderLayout.CENTER);

        // Split
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorPanel, tabs);
        split.setResizeWeight(0.5);

        // Menú + Toolbar + Status
        setJMenuBar(buildMenuBar());
        JToolBar toolbar = buildToolBar();
        JPanel statusBar = buildStatusBar();

        // Atajo Ctrl+Enter
        inputArea.getInputMap().put(KeyStroke.getKeyStroke("ctrl ENTER"), "EVALUAR");
        inputArea.getActionMap().put("EVALUAR", actCompile);

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

    /* ---------- Estilo ---------- */

    private void installNiceLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(laf.getName())) {
                    UIManager.setLookAndFeel(laf.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {}
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

    /* ---------- Archivo / Evaluar ---------- */

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
                setTitle("Compilador Turix — " + f.getName());
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
                setTitle("Compilador Turix — " + f.getName());
                setStatus("Archivo guardado: " + f.getName());
            } catch (IOException ex) {
                showError("No se pudo guardar el archivo:\n" + ex.getMessage(), "Error al guardar");
            }
        }
    }

    /** Compila/Evalúa el contenido completo y separa errores en Léxico / Sintáctico. */
    private void onEvaluate(ActionEvent e) {
        TokenAsignaciones.tabla.clear();
        String text = inputArea.getText();
        if (text == null || text.isBlank()) {
            setStatus("Nada que compilar");
            return;
        }

        lexArea.setText("== Análisis Léxico ==\n");
        synArea.setText("== Análisis Sintáctico ==\n");
        semArea.setText("== Análisis Semántico ==\n");
        TokenAsignaciones.SetTables();
        int erroresLex = 0;
        int erroresSin = 0;

        // ---------- LÉXICO ----------
        SimpleCharStream scsLex = new SimpleCharStream(new StringReader(text), 1, 1);
        TurixTokenManager lex = new TurixTokenManager(scsLex);

        Token t = null;

        while (true) {
        try {
            t = lex.getNextToken();
            if (t.kind == TurixConstants.EOF) break;

            if (t.kind == TurixConstants.ERROR_IDENT || t.kind == TurixConstants.ERROR_IDENT || t.kind == TurixConstants.ERROROPERA
                    || t.kind == TurixConstants.ERROR) {
                erroresLex++;
                lexArea.append(String.format(
                    "✘ Léxico: Error en la línea %d, col %d. Token inválido => '%s'%n",
                    t.beginLine, t.beginColumn, t.image
                ));
                continue; // no lo mostramos como token válido
            }

            // Solo mostramos tokens válidos
            lexArea.append(String.format(
                "Línea %d, Col %d: Token %-18s => '%s'%n",
                t.beginLine, t.beginColumn, TokenCase.getTokenNombre(t.kind), t.image
            ));

        } catch (TokenMgrError tme) {
            erroresLex++;
            lexArea.append("✘ Léxico: " + tme.getMessage() + "\n");
            try { scsLex.readChar(); } catch (IOException ex) { break; }
        }
    }

        if (erroresLex == 0) lexArea.append("✔ Sin errores léxicos\n");
        else lexArea.append(String.format("✘ Total de errores léxicos: %d%n", erroresLex));

        // ---------- SINTÁCTICO ----------
        try {
            Turix parser = new Turix(new StringReader(text));
            boolean continuar = true;

            while (continuar) {
                try {
                    parser.Start(); // intentamos parsear
                    continuar = false;
                } catch (ParseException pe) {
                    erroresSin++;
                    synArea.append("✘ Sintaxis: " + pe.getMessage() + "\n");

                    // Avanzamos un token para no quedar atrapados
                    Token next = parser.getToken(1); 
                    if (next.kind == TurixConstants.EOF) {
                        continuar = false;
                    } else {
                        parser.token = parser.getToken(2);
                    }
                } catch (TokenMgrError tme) {
                    // Si el parser encuentra un error léxico dentro, avanzamos
                    try { scsLex.readChar(); } catch (IOException ex) { continuar = false; }
                }
            }

        } catch (Exception ex) {
            erroresSin++;
            synArea.append("✘ Error inesperado: " + ex.getMessage() + "\n");
        }
        //---------SEMÁNTICO-------
        erroresSem.resetErrores(); // limpiar antes
        try {
            Turix parser = new Turix(new StringReader(text));
            parser.Start(); 
            } catch (Exception ex) {
                // ya lo manejaste en sintaxis
            }
        if (erroresSem.getErrores().isEmpty()) {
            semArea.append("✔ Sin errores semánticos\n");
        } else {
            for (String err :erroresSem.getErrores()) {
                semArea.append("✘ Semántico: " + err + "\n");
            }
            semArea.append("✘ Total de errores semánticos: " +
                           erroresSem.getErrores().size() + "\n");
        }
        if (erroresSin == 0) synArea.append("✔ Sin errores Sintácticos\n");
        else synArea.append(String.format("✘ Total de errores sintácticos: %d%n", erroresSin));

        setStatus(String.format(
            "Compilación terminada: %d errores léxicos, %d errores sintácticos",
            erroresLex, erroresSin));
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
        changeAreaFont(lexArea, delta);
        changeAreaFont(synArea, delta);
        changeAreaFont(semArea, delta);
    }

    private void changeAreaFont(JTextArea area, int delta) {
        Font f = area.getFont();
        int newSize = Math.max(10, f.getSize()+delta);
        area.setFont(f.deriveFont((float)newSize));
    }

    private void showError(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /* ---------- Números de línea ---------- */

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
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
