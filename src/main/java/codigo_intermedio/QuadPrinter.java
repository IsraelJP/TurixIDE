// package <tu.package.de.codigo_intermedio>;
package codigo_intermedio;

import java.util.Objects;

public class QuadPrinter {

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(c);
        return sb.toString();
    }

    private static String safe(Object o) {
        return (o == null) ? "" : Objects.toString(o);
    }

    /**
     * Versión original que mantengo para compatibilidad.
     */
    public static String formatBlock(int index, String targetVar, String infix, QuadGenerator.Result res) {
        return formatBlock(index, targetVar, infix, res, null);
    }

    /**
     * Nueva sobrecarga que permite forzar el valor evaluado (overrideEval)
     * para el renglón "target := valor".
     */
    public static String formatBlock(int index, String targetVar, String infix,
                                     QuadGenerator.Result res, Double overrideEval) {
        String NL = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append("--------------------------------------------------").append(NL);
        sb.append("              Código intermedio #").append(index).append(NL);
        sb.append("--------------------------------------------------").append(NL);
        sb.append(NL);

        sb.append("  ").append(targetVar).append(" = ").append(infix).append(NL);
        sb.append("  Expresión postfija: ");
        if (res != null && res.postfix != null) {
            sb.append(String.join(" ", res.postfix));
        }
        sb.append(NL).append(NL);
        // ======= Traza shunting-yard (pila paso a paso) =======
        if (res != null && res.infixToPostfixTrace != null && !res.infixToPostfixTrace.isEmpty()) {
            sb.append("Traza shunting-yard (pila paso a paso)").append(NL);
            sb.append(String.format("%-6s | %-10s | %-25s | %-18s | %-18s%s",
                    "Paso", "Token", "Acción", "Pila", "Salida", NL));
            sb.append("------------------------------------------------------------------------------------------").append(NL);

            for (arbol.infijaPostfija.TraceStep st : res.infixToPostfixTrace) {
                sb.append(String.format("%-6d | %-10s | %-25s | %-18s | %-18s%s",
                        st.step,
                        st.token,
                        st.action,
                        st.stackSnapshot,
                        st.outputSnapshot,
                        NL
                ));
            }
            sb.append(NL);
            sb.append("----------------------------------------").append(NL);
        }

        String head = String.format("%-12s | %-12s | %-8s | %-12s",
                "Resultado", "Operando 1", "Operador", "Operando 2");
        sb.append(head).append(NL);
        sb.append(repeat('-', head.length())).append(NL);

        if (res != null && res.quads != null) {
            for (int i = 0; i < res.quads.size(); i++) {
                var q = res.quads.get(i);
                String row = String.format("%-12s | %-12s | %-8s | %-12s",
                        safe(q.result), safe(q.arg1), safe(q.op), safe(q.arg2));
                sb.append(row).append(NL);
            }
        }
        sb.append(NL);

        Double shown = (overrideEval != null) ? overrideEval : (res != null ? res.eval : null);
        if (shown != null) {
            String pretty = (Math.floor(shown) == shown)
                    ? String.valueOf(shown.intValue())
                    : shown.toString();
            sb.append(targetVar).append(" := ").append(pretty).append(NL);
        } else {
            sb.append(targetVar).append(" := ").append("<no evaluable en compilación>").append(NL);
        }

        return sb.toString();
    }
}
