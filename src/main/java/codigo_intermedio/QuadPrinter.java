package codigo_intermedio;

import java.util.List;

public final class QuadPrinter {

    private QuadPrinter() {}

    public static String formatBlock(int index, String targetVar, String infix, QuadGenerator.Result res) {
        String NL = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        // Encabezado
        sb.append("--------------------------------------------------").append(NL);
        sb.append("              Código intermedio #").append(index).append(NL);
        sb.append("--------------------------------------------------").append(NL);
        sb.append(NL);
        sb.append("  ").append(targetVar).append(" = ").append(infix).append(";").append(NL);
        sb.append(NL);

        // Tabla
        String head = String.format("%-12s | %-12s | %-8s | %-12s",
                "Resultado", "Operando 1", "Operador", "Operando 2");
        sb.append(head).append(NL);
        sb.append(repeat('-', head.length())).append(NL);

        List<Quadruple> qs = res.quads;
        for (int i = 0; i < qs.size(); i++) {
            Quadruple q = qs.get(i);
            // Igual que tu ejemplo: las operaciones intermedias con temporales,
            // y la última fila es la asignación "a = temporalN"
            String row = String.format("%-12s | %-12s | %-8s | %-12s",
                    q.result, safe(q.arg1), safe(q.op), safe(q.arg2));
            sb.append(row).append(NL);
        }

        sb.append(NL);
        if (res.eval != null) {
            // Mostrar reducción final si se puede evaluar
            String pretty = (Math.floor(res.eval) == res.eval) ?
                    String.valueOf(res.eval.intValue()) : res.eval.toString();
            sb.append(targetVar).append(" := ").append(pretty).append(NL);
        } else {
            // No evaluable estáticamente
            sb.append(targetVar).append(" := ").append("<no evaluable en compilación>").append(NL);
        }

        return sb.toString();
    }

    private static String repeat(char c, int n) {
        StringBuilder s = new StringBuilder(n);
        for (int i = 0; i < n; i++) s.append(c);
        return s.toString();
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
