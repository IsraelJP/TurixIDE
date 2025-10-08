package codigo_intermedio;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import arbol.infijaPostfija;

public final class QuadGenerator {

    private static final Set<String> OPS = new HashSet<>(Arrays.asList(
        "+", "-", "*", "/", "%", "^"
    ));

    private QuadGenerator() {}

    public static class Result {
        public final List<Quadruple> quads;
        public final String postfix;
        public final Double eval; // null si no es evaluable estáticamente

        private Result(List<Quadruple> quads, String postfix, Double eval) {
            this.quads = quads;
            this.postfix = postfix;
            this.eval = eval;
        }
    }

    public static Result fromAssignment(String target, String infixExpr,  Map<String, Double> variables) {
        // 1) A postfija
        String postfix = safePostfix(infixExpr);

        // 2) Cuádruplas desde postfija con temporales
        List<Quadruple> quads = buildQuadsFromPostfix(postfix, target);

        // 3) Evaluación estática si es posible (todo numérico)
        Double eval = tryEvalPostfix(postfix,variables);

        return new Result(quads, postfix, eval);
    }

    private static String safePostfix(String infix) {
        try {
            return  infijaPostfija.convertir(infix);
        } catch (Exception e) {
            return "<error postfija: " + e.getMessage() + ">";
        }
    }

    private static List<Quadruple> buildQuadsFromPostfix(String postfix, String targetVar) {
        List<Quadruple> quads = new ArrayList<>();
        if (postfix.startsWith("<error")) return quads;

        Deque<String> stack = new ArrayDeque<>();
        int tempCounter = 1;

        String[] tokens = postfix.trim().split("\\s+");
        for (String tk : tokens) {
            if (tk.isEmpty()) continue;
            if (isOperator(tk)) {
                String b = pop(stack);
                String a = pop(stack);
                String temp = "temporal" + tempCounter++;
                quads.add(new Quadruple(temp, a, tk, b));
                stack.push(temp);
            } else {
                stack.push(tk);
            }
        }

        String top = stack.isEmpty() ? "" : stack.pop();
        // asignación final
        quads.add(new Quadruple(targetVar, top, "=", ""));
        return quads;
    }

    private static boolean isOperator(String t) {
        return OPS.contains(t);
    }

    private static String pop(Deque<String> st) {
        return st.isEmpty() ? "" : st.pop();
        // si quieres ser estricto, lanza IllegalStateException si está vacío
    }

    private static Double tryEvalPostfix(String postfix, Map<String, Double> variables) {
    try {
        Deque<Double> st = new ArrayDeque<>();
        String[] tokens = postfix.trim().split("\\s+");
        for (String tk : tokens) {
            if (tk.isEmpty()) continue;

            if (isOperator(tk)) {
                Double b = st.pop();
                Double a = st.pop();
                st.push(apply(a, b, tk));
            } else {
                // Si es número, booleano o variable conocida, úsalo
                try {
                    // ✅ Intentar convertir a número
                    st.push(Double.valueOf(tk));
                } catch (NumberFormatException ex) {

                    // ✅ Aceptar valores booleanos como 1 (true) y 0 (false)
                    if (tk.equalsIgnoreCase("true")) {
                        st.push(1.0);
                    } else if (tk.equalsIgnoreCase("false")) {
                        st.push(0.0);
                    }

                    // ✅ Buscar variables conocidas en el mapa
                    else if (variables != null && variables.containsKey(tk)) {
                        st.push(variables.get(tk));
                    }

                    // ❌ No se puede evaluar si no se conoce el valor
                    else {
                        return null;
                    }
                }
            }
        }

        if (st.size() == 1 && st.peek() != null && Double.isFinite(st.peek())) {
            return st.peek();
        }
    } catch (Exception ignore) {}
    return null;
}


    private static Double apply(Double a, Double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            case "%": return a % b;
            case "^": return Math.pow(a, b);
            default: throw new IllegalArgumentException("Operador no soportado: " + op);
        }
    }
}
