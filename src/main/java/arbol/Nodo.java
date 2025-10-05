package arbol;
        
public class Nodo {
    private Object valor;  // double para operandos, char para operadores
    private Nodo izquierdo;
    private Nodo derecho;
    public Nodo(Object valor) {
        this.valor = valor;
        this.izquierdo = null;
        this.derecho = null;
    }
    // Getters y setters
    public Object getValor() {
        return valor;
    }
    public void setValor(Object valor) {
        this.valor = valor;
    }
    public Nodo getIzquierdo() {
        return izquierdo;
    }
    public void setIzquierdo(Nodo izquierdo) {
        this.izquierdo = izquierdo;
    }
    public Nodo getDerecho() {
        return derecho;
    }
    public void setDerecho(Nodo derecho) {
        this.derecho = derecho;
    }
    // Método para verificar si es operador
    public boolean esOperador() {
        return valor instanceof Character;
    }
    // Método para obtener como double (si es operando)
    public double getComoDouble() {
        return (double) valor;
    }
}