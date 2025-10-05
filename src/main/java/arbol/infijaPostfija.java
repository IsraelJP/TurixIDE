package arbol;

import java.util.Stack;

public class infijaPostfija {

    public static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    public static String convertir(String expression) {
        Stack<String> stack = new Stack<>();
        StringBuilder output = new StringBuilder();

        String[] tokens = expression.trim().split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*") || token.matches("\\d+(\\.\\d+)?")) {
                // Identificador o número
                output.append(token).append(" ");
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.append(stack.pop()).append(" ");
                }
                stack.pop();
            } else if (token.matches("[+\\-*/%]")) {
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        precedence(stack.peek().charAt(0)) >= precedence(token.charAt(0))) {
                    output.append(stack.pop()).append(" ");
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }

        return output.toString().trim();
    }
}
