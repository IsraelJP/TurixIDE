/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Codigo_Intermedio;

import java.util.ArrayDeque;
import java.util.Deque;
public class ArbolExpresion {
    private Nodo raiz;
    public ArbolExpresion(String expresionPostfija) {
        this.raiz = construirArbol(expresionPostfija);
    }
    private Nodo construirArbol(String expresion) {
        Deque<Nodo> pila = new ArrayDeque<>();
        String[] tokens = expresion.split(" ");  // Asume tokens separados por espacio
        for (String token : tokens) {
            if (esNumero(token)) {
                // Crear nodo operando
                Nodo nodo = new Nodo(Double.parseDouble(token));
                pila.push(nodo);
            } else {
                // Es operador: sacar dos nodos, crear nuevo nodo operador
                Nodo derecho = pila.pop();
                Nodo izquierdo = pila.pop();
                Nodo nodoOperador = new Nodo(token.charAt(0));  // Asume operador de un char
                nodoOperador.setIzquierdo(izquierdo);
                nodoOperador.setDerecho(derecho);
                pila.push(nodoOperador);
            }
        }
        return pila.pop();  // La raíz
    }
    // Método auxiliar para verificar si es número
    private boolean esNumero(String token) {
        try {
             Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public Nodo getRaiz() {
        return raiz;
    }
  // Método público de evaluación que inicia la recursión con profundidad 0
    public double evaluar() {
        System.out.println("Iniciando evaluación del árbol de expresiones...");
        double resultado = evaluarRecursivo(raiz, 0);
        System.out.println("Evaluación completa. Resultado final: " + resultado);
        return resultado;
    }

    // Método recursivo modificado para mostrar el procedimiento
    private double evaluarRecursivo(Nodo nodo, int profundidad) {
        // Crear indentación basada en la profundidad
        String indentacion = "  ".repeat(profundidad);

        if (!nodo.esOperador()) {
            // Es un operando (hoja)
            double valor = nodo.getComoDouble();
            return valor;
        }

        // Es un operador: evaluar hijos primero
        double izquierdo = evaluarRecursivo(nodo.getIzquierdo(), profundidad + 1);
        double derecho = evaluarRecursivo(nodo.getDerecho(), profundidad + 1);
        char operador = (char) nodo.getValor();

        // Calcular resultado
        double resultado;
        switch (operador) {
            case '+':
                resultado = izquierdo + derecho;
                break;
            case '-':
                resultado = izquierdo - derecho;
                break;
            case '*':
                resultado = izquierdo * derecho;
                break;
            case '/':
                if (derecho == 0) {
                    throw new ArithmeticException("División por cero");
                }
                resultado = izquierdo / derecho;
                break;
            default:
                throw new IllegalArgumentException("Operador no soportado: " + operador);
        }

        // Mostrar el paso de este operador
        System.out.println(indentacion + "Evaluando " + izquierdo + " " + operador + " " + derecho + " = " + resultado);
        return resultado;
    }

}