/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package arbol;

/**
 *
 * @author stare
 */
public class Main {

    public static void main(String[] args) {
        infijaPostfija expPost=new infijaPostfija();
        // Expresión postfija: 3 + 4 * 2 = 3 + (4*2) = 11
        String expresionPostfija = expPost.convertir("3+4* 2");
        System.out.println(expresionPostfija);
        ArbolExpresion arbol = new ArbolExpresion(expresionPostfija);
        double resultado = arbol.evaluar();
        
        System.out.println("Expresión: " + expresionPostfija);
        System.out.println("Resultado: " + resultado);  // Salida: 11.0
        
        // Otro ejemplo: 5 - 3 * 2 = 5 - (3*2) = -1
        String otraExpresion = "5 3 2 * -";
        ArbolExpresion arbol2 = new ArbolExpresion(otraExpresion);
        System.out.println("Resultado de '" + otraExpresion + "': " + arbol2.evaluar());  // -1.0
    }
}

