/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Semantico;

import com.turix.TurixCC.Token;
import java.util.ArrayList;
import java.util.Hashtable;
import com.turix.TurixCC.TurixConstants;
/**
 *
 * @author stare
 */
public class TokenAsignaciones {
    

    public static boolean banInicio=false; 
    //Tabla que almacena tokens
    public static Hashtable tabla=new Hashtable();
     //Listas
    private static ArrayList<Integer> intComp = new ArrayList();
    private static ArrayList<Integer> decComp = new ArrayList();
    private static ArrayList<Integer> strComp = new ArrayList();
    private static ArrayList<Integer> boolComp = new ArrayList();

    public static void InsertarSimbolo(Token identificador, int tipoDato, Token valor){
        //En este método se agrega a la tabla de tokens el identificador que esta siendo 
        //declarado junto con su tipo de dato int e=2 -> e Int
        
        tabla.put(identificador.image, tipoDato);
        if(valor!=null){
        checkAsing(identificador, valor); 
        }
       
    }
    
    public static void SetTables(){
        //Inicialización de tablas, almacena el tipo de dato compatible
        
        intComp.add(5); //Numero 1233
        intComp.add(20);//int
        
        decComp.add(5);//Numero
        decComp.add(6); //Decimal 12.3
        decComp.add(13);//Double
        decComp.add(20);
        decComp.add(21);
        
        strComp.add(7); //String literal AHUNSNKJKS
        strComp.add(22); //String 
        strComp.add(24);//Char
        
        boolComp.add(23);//Bool
        boolComp.add(58); //False
        boolComp.add(59);//True
        
    }
    //Token izquierdo es tipo
    //Token der resultado

    public static void checkAsing(Token TokenIzq, Token TokenAsig){
        // 0) Guardas defensivas
        if (TokenIzq == null) {
            erroresSem.addError("Asignación inválida: identificador izquierdo nulo.");
            return;
        }
        if (TokenAsig == null) {
            erroresSem.addError(
                "Asignación inválida a '" + TokenIzq.image +
                "': la expresión del lado derecho no se pudo evaluar (es nula). " +
                "Línea: " + TokenIzq.beginLine
            );
            return;
        }

        // 1) Determinar tipo del identificador izquierdo (debe existir en la tabla)
        int tipoIdent1;
        if (TokenIzq.kind != TurixConstants.NUM && TokenIzq.kind != TurixConstants.NUM_DEC) {
            try {
                tipoIdent1 = (Integer) tabla.get(TokenIzq.image);
            } catch (Exception e) {
                erroresSem.addError(
                    "Error: el identificador '" + TokenIzq.image + "' no ha sido declarado. " +
                    "Línea: " + TokenIzq.beginLine
                );
                return;
            }
        } else {
            // Si por alguna razón llega un literal a la izquierda (no debería), lo marcamos inválido
            erroresSem.addError(
                "Asignación inválida: la izquierda debe ser un identificador, no un literal. " +
                "Línea: " + TokenIzq.beginLine
            );
            return;
        }

        // 2) Determinar tipo del lado derecho (identificador o literal/constante)
        int tipoIdent2;
        if (TokenAsig.kind == TurixConstants.IDENT) {
            try {
                tipoIdent2 = (Integer) tabla.get(TokenAsig.image);
            } catch (Exception e) {
                erroresSem.addError(
                    "Error: el identificador '" + TokenAsig.image + "' (lado derecho) no ha sido declarado. " +
                    "Línea: " + TokenIzq.beginLine
                );
                return;
            }
        } else if (
            TokenAsig.kind == TurixConstants.NUM ||
            TokenAsig.kind == TurixConstants.NUM_DEC ||
            TokenAsig.kind == TurixConstants.STRING_LITERAL ||
            TokenAsig.kind == TurixConstants.STRING ||
            TokenAsig.kind == TurixConstants.FALSE ||
            TokenAsig.kind == TurixConstants.TRUE
        ) {
            // Para literales/keywords, su "tipo" lo igualamos al kind del token.
            tipoIdent2 = TokenAsig.kind;
        } else {
            // Si no reconocemos (p.ej. una expresión compuesta), sé conservador.
            // Aquí podrías inferir el tipo de la expresión si implementas propagación de tipos.
            tipoIdent2 = 0; // desconocido
        }

        // 3) Reglas de compatibilidad (ejemplos)
        //   INT (20) acepta NUM y NUM_DEC (según tu diseño puedes limitarlo a NUM)
        if (tipoIdent1 == TurixConstants.INT) {
            if (!(tipoIdent2 == TurixConstants.NUM || tipoIdent2 == TurixConstants.NUM_DEC)) {
                erroresSem.addError(
                    "No se puede asignar '" + TokenAsig.image + "' a Int. Línea: " + TokenIzq.beginLine
                );
                return;
            }
        } else if (tipoIdent1 == TurixConstants.DOUBLE) {
            if (!(tipoIdent2 == TurixConstants.NUM || tipoIdent2 == TurixConstants.NUM_DEC)) {
                erroresSem.addError(
                    "No se puede asignar '" + TokenAsig.image + "' a Double. Línea: " + TokenIzq.beginLine
                );
                return;
            }
        } else if (tipoIdent1 == TurixConstants.STRING) {
            if (!(tipoIdent2 == TurixConstants.STRING_LITERAL || tipoIdent2 == TurixConstants.STRING)) {
                erroresSem.addError(
                    "No se puede asignar '" + TokenAsig.image + "' a String. Línea: " + TokenIzq.beginLine
                );
                return;
            }
        } else if (tipoIdent1 == TurixConstants.BOOL) {
            if (!(tipoIdent2 == TurixConstants.TRUE || tipoIdent2 == TurixConstants.FALSE)) {
                erroresSem.addError(
                    "No se puede asignar '" + TokenAsig.image + "' a Bool. Línea: " + TokenIzq.beginLine
                );
                return;
            }
        }
        // Si todo pasa, la asignación es compatible.
    }

    //Método que verifica un identificador ha sido declarado
    public static String checkVariable(Token checkTok){
        try{
            int tipoIdent1=(Integer)tabla.get(checkTok.image);
            return " ";
        }catch(Exception e){
            return "Error: El indentificador "+checkTok.image+" No ha sido declarado"+" Linea: "+checkTok.beginLine;
        }
    }
}
    


