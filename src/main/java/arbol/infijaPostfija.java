package arbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class infijaPostfija {

    /** Un paso de traza del shunting-yard. */
    public static class TraceStep {
        public final int step;
        public final String token;
        public final String action;
        public final String stackSnapshot;
        public final String outputSnapshot;

        public TraceStep(int step, String token, String action, String stackSnapshot, String outputSnapshot) {
            this.step = step;
            this.token = token;
            this.action = action;
            this.stackSnapshot = stackSnapshot;
            this.outputSnapshot = outputSnapshot;
        }
    }

    /** Resultado con postfija + pasos. */
    public static class PostfixTrace {
        public final String postfix;
        public final List<TraceStep> steps;

        public PostfixTrace(String postfix, List<TraceStep> steps) {
            this.postfix = postfix;
            this.steps = steps;
        }
    }

    public static int precedence(char op) {
        switch (op) {
            case '+':
            case '-': return 1;
            case '*':
            case '/':
            case '%': return 2;
            case '^': return 3;
            default:  return 0;
        }
    }

    /** Conserva tu API actual. */
    public static String convertir(String expression) {
        return convertirConTraza(expression).postfix;
    }

    /** Conversión a postfija con traza (tokens separados por espacios). */
    public static PostfixTrace convertirConTraza(String expression) {
        Stack<String> stack = new Stack<>();
        StringBuilder out = new StringBuilder();
        List<TraceStep> steps = new ArrayList<>();
        int step = 1;

        String[] tokens = expression.trim().split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isOperand(token)) {
                out.append(token).append(" ");
                steps.add(new TraceStep(step++, token, "emitir operando", fmtStack(stack), out.toString().trim()));
            } else if (token.equals("(")) {
                stack.push(token);
                steps.add(new TraceStep(step++, token, "apilar '('", fmtStack(stack), out.toString().trim()));
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    out.append(stack.pop()).append(" ");
                    steps.add(new TraceStep(step, token, "desapilar operador", fmtStack(stack), out.toString().trim()));
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) stack.pop(); // descartar '('
                steps.add(new TraceStep(step++, token, "descartar ')'", fmtStack(stack), out.toString().trim()));
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        precedence(stack.peek().charAt(0)) >= precedence(token.charAt(0))) {
                    out.append(stack.pop()).append(" ");
                    steps.add(new TraceStep(step, token, "desapilar por precedencia", fmtStack(stack), out.toString().trim()));
                }
                stack.push(token);
                steps.add(new TraceStep(step++, token, "apilar operador", fmtStack(stack), out.toString().trim()));
            } else {
                steps.add(new TraceStep(step++, token, "token desconocido/ignorado", fmtStack(stack), out.toString().trim()));
            }
        }

        while (!stack.isEmpty()) {
            String op = stack.pop();
            if (!"(".equals(op) && !")".equals(op)) {
                out.append(op).append(" ");
            }
            steps.add(new TraceStep(step++, "<fin>", "vaciar pila", fmtStack(stack), out.toString().trim()));
        }

        return new PostfixTrace(out.toString().trim(), steps);
    }

    private static boolean isOperand(String token) {
        return token.matches("[a-zA-Z_][a-zA-Z0-9_]*") ||
               token.matches("\\d+(\\.\\d+)?") ||
               token.equalsIgnoreCase("true") ||
               token.equalsIgnoreCase("false");
    }

    private static boolean isOperator(String token) {
        return token.matches("[+\\-*/%\\^]");
    }

    /** Formatea la pila así: "[ +, * ]" */
    private static String fmtStack(Stack<String> st) {
        if (st.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < st.size(); i++) {
            sb.append(st.get(i));
            if (i < st.size() - 1) sb.append(", ");
            else sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
