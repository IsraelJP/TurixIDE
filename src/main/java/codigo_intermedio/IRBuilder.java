package codigo_intermedio;

import java.util.ArrayList;
import java.util.List;

public final class IRBuilder {
    private static final String NL = System.lineSeparator();
    private static final IRBuilder INSTANCE = new IRBuilder();

    private final List<String> lines = new ArrayList<>();

    private IRBuilder() {}

    public static IRBuilder get() {
        return INSTANCE;
    }

    public void clear() {
        lines.clear();
    }

    public void emitRaw(String s) {
        if (s != null && !s.isEmpty()) {
            lines.add(s);
        }
    }

    public void emitHeader(String title) {
        lines.add("==== " + title + " ====");
    }

    public void emitAssignment(String id, String infija, String postfija, String eval) {
        lines.add("ASIGNACIÓN");
        lines.add("  id       : " + id);
        lines.add("  infija   : " + infija);
        lines.add("  postfija : " + postfija);
        if (eval != null) lines.add("  eval     : " + eval);
        lines.add("");
    }

    public void emitExpression(String infija, String postfija, String eval) {
        lines.add("EXPRESIÓN");
        lines.add("  infija   : " + infija);
        lines.add("  postfija : " + postfija);
        if (eval != null) lines.add("  eval     : " + eval);
        lines.add("");
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();
        for (String l : lines) sb.append(l).append(NL);
        return sb.toString();
    }
}
