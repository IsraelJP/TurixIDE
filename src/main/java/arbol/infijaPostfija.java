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
        Stack<Character> stack = new Stack<>();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isWhitespace(ch)) continue; // Ignorar espacios

            if (Character.isLetterOrDigit(ch)) {
                output.append(ch).append(" "); // 👈 agrega espacio después de cada operando
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop()).append(" "); // 👈 también aquí
                }
                stack.pop();
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!stack.isEmpty() && stack.peek() != '(' &&
                        precedence(stack.peek()) >= precedence(ch)) {
                    output.append(stack.pop()).append(" "); // 👈 aquí también
                }
                stack.push(ch);
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" "); // 👈 y aquí también
        }

        return output.toString().trim(); // eliminar espacio final
    }
}
