package codigo_intermedio;

import java.util.*;
import arbol.infijaPostfija;

public final class QuadGenerator {

    private static final Set<String> OPS = new HashSet<>(Arrays.asList(
            "+", "-", "*", "/", "%", "^", "="
    ));
    private QuadGenerator(){}

    public static class Result {
        public final List<Quadruple> quads;
        public final String postfix;
        public final Double eval;

        // NUEVOS/Necesarios para impresión:
        public final List<infijaPostfija.TraceStep> infixToPostfixTrace;
        public final List<String> evalStackTrace;

        private Result(List<Quadruple> quads, String postfix, Double eval,
                       List<infijaPostfija.TraceStep> infixToPostfixTrace,
                       List<String> evalStackTrace) {
            this.quads = quads;
            this.postfix = postfix;
            this.eval = eval;
            this.infixToPostfixTrace = infixToPostfixTrace;
            this.evalStackTrace = evalStackTrace;
        }
    }

    public static Result fromAssignment(String target, String infixExpr, Map<String, Double> variables) {
        // 1) Infija -> Postfija con traza
        infijaPostfija.PostfixTrace tr = infijaPostfija.convertirConTraza(infixExpr);
        String postfix = tr.postfix;

        // 2) Generar cuádruplas y traza de pila de evaluación
        List<String> evalTrace = new ArrayList<>();
        List<Quadruple> quads = buildQuadsFromPostfix(postfix, target, evalTrace, variables);

        // 3) Evaluación estática (si aplica)
        Double eval = tryEvalPostfix(postfix, variables);

        return new Result(quads, postfix, eval, tr.steps, evalTrace);
    }

    private static List<Quadruple> buildQuadsFromPostfix(String postfix, String targetVar,
                                                        List<String> evalTrace,
                                                        Map<String, Double> variables) {
        List<Quadruple> quads = new ArrayList<>();
        if (postfix == null || postfix.isBlank()) return quads;

        Deque<String> stack = new ArrayDeque<>();
        int tempCounter = 1;
        int paso = 1;

        for (String tk : postfix.trim().split("\\s+")) {
            if (tk.isEmpty()) continue;
            if (isOperator(tk)) {
                String b = stack.pop();
                String a = stack.pop();
                String t = "t" + (tempCounter++);
                quads.add(new Quadruple(t, a, tk, b));
                stack.push(t);
                evalTrace.add(String.format("Paso %02d: aplicar '%s' => push(%s) | pila=%s", paso++, tk, t, stack));
            } else {
                String resolved = resolveOperand(tk, variables);
                stack.push(resolved);
                String shown = tk.equals(resolved) ? tk : tk + "->" + resolved;
                evalTrace.add(String.format("Paso %02d: push(%s) | pila=%s", paso++, shown, stack));
            }
        }

        String top = stack.isEmpty() ? "" : stack.pop();
        quads.add(new Quadruple(targetVar, top, "=", ""));
        evalTrace.add(String.format("Paso %02d: asignar %s = %s", paso, targetVar, top));
        return quads;
    }

    private static boolean isOperator(String s) { return OPS.contains(s); }

    private static String resolveOperand(String token, Map<String, Double> variables) {
        if (variables == null || token == null || token.isBlank()) return token;
        Double value = variables.get(token);
        if (value == null) return token;
        return formatNumber(value);
    }

    private static String formatNumber(Double value) {
        if (value == null) return "";
        if (value.isNaN() || value.isInfinite()) {
            return value.toString();
        }
        double dbl = value;
        if (Math.floor(dbl) == dbl) {
            long asLong = (long) dbl;
            return Long.toString(asLong);
        }
        String str = Double.toString(dbl);
        if (str.contains("E") || str.contains("e")) {
            return str;
        }
        if (str.indexOf('.') >= 0) {
            while (str.endsWith("0")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.endsWith(".")) {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    private static Double tryEvalPostfix(String postfix, Map<String, Double> vars) {
        if (postfix == null || postfix.isBlank()) return null;
        try {
            Deque<Double> st = new ArrayDeque<>();
            for (String tk : postfix.trim().split("\\s+")) {
                if (OPS.contains(tk) && !tk.equals("=")) {
                    double b = st.pop(), a = st.pop();
                    switch (tk) {
                        case "+": st.push(a + b); break;
                        case "-": st.push(a - b); break;
                        case "*": st.push(a * b); break;
                        case "/": st.push(a / b); break;
                        case "%": st.push(a % b); break;
                        case "^": st.push(Math.pow(a, b)); break;
                    }
                } else {
                    try {
                        st.push(Double.valueOf(tk));
                    } catch (NumberFormatException e) {
                        if (vars != null && vars.containsKey(tk)) st.push(vars.get(tk));
                        else return null;
                    }
                }
            }
            return (st.size() == 1) ? st.peek() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
