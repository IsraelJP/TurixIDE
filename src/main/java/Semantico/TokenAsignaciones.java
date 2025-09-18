/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Semantico;

import com.turix.TurixCC.Token;
import com.turix.TurixCC.TurixConstants;
import java.util.ArrayList;
import java.util.Hashtable;

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
        //Variables para almacenar el tipo de dato del identificador 
        //y las asignaciones
        int tipoIdent1;
        int tipoIdent2 ;
        
        //No se ha declarado
        if (TokenIzq.kind!=5 && TokenIzq.kind!=6 ){
            try{
                tipoIdent1=(Integer)tabla.get(TokenIzq.image);
            }catch(Exception e){
                erroresSem.addError("Error el identificador"+TokenIzq.image + "No ha sido declarado \r\nLinea: "+TokenIzq.beginLine) ;
                return; 
            }
        }
        else{
            tipoIdent1=0; 
            System.out.println("Entro aqui variable");
        }
        //Identificador e=pato
        if(TokenAsig.kind== 60){
            try{
                tipoIdent2=(Integer) tabla.get(TokenAsig.image);
            }catch(Exception e){
                erroresSem.addError("Error: El identificador "+TokenAsig.image+"No ha sido declarado\r\nLinea: "+TokenIzq.beginLine) ;
                return; 
            
            }
        }
        else if (TokenAsig.kind== 5  || TokenAsig.kind== 6 || TokenAsig.kind== 22 || TokenAsig.kind==58 || TokenAsig.kind==59 ){
            tipoIdent2=TokenAsig.kind;
        }else{
            tipoIdent2=0;
        }
        
        //INT: Verificar si es entero
        if(tipoIdent1==20){
            if(!intComp.contains(tipoIdent2)){
                    banInicio=true; 
                   erroresSem.addError("Error: No se puede convertir "+TokenAsig.image+" a Entero \r\nLinea: "+TokenIzq.beginLine) ;
                return;  
            }
        }
        //DOUBLE: Verificar si es Double
        else if(tipoIdent1==13){
            if(!decComp.contains(tipoIdent2)){
                banInicio=true; 
                   erroresSem.addError("Error: No se puede convertir "+TokenAsig.image+" a Decimal \r\nLinea: "+TokenIzq.beginLine) ;
                return; 
            }
        }
        //STRING: Verificar si es String
        else if(tipoIdent1==22){
            if(!strComp.contains(tipoIdent2)){
                banInicio=true; 
                   erroresSem.addError( "Error: No se puede convertir "+TokenAsig.image+" a String \r\nLinea: "+TokenIzq.beginLine) ;
                return; 
            }
        }
        //Bool: Verificar si es Bool
        else if(tipoIdent1==23){
            banInicio=true; 
            if(!boolComp.contains(tipoIdent2)){
                   erroresSem.addError("Error: No se puede convertir "+TokenAsig.image+" a Booleano \r\nLinea: "+TokenIzq.beginLine) ;
                return; 
            }
        }
        else{
            banInicio=true; 
            erroresSem.addError("El identificador "+TokenIzq.image +" no ha sido declarado "+" Linea: "+TokenIzq.beginLine) ;
                return; 
        }
        if (TokenAsig == null) {
  
        return;
    }
        
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
    
    public static void checkOperacion(Token izq, Token der, Token op) {
        if (izq == null || der == null || op == null) return;

        switch (op.kind) {
            case TurixConstants.MAS: // +
                if ((izq.kind == TurixConstants.INT && der.kind == TurixConstants.INT) ||
                    (izq.kind == TurixConstants.FLOAT && der.kind == TurixConstants.FLOAT) ||
                    (izq.kind == TurixConstants.STRING && der.kind == TurixConstants.STRING)) {
                    return; // válido
                }
                erroresSem.addError("Error: No se puede aplicar '+' entre " +
                        nombreTipo(izq.kind) + " y " + nombreTipo(der.kind));
                break;

            case TurixConstants.MENOS: // -
            case TurixConstants.MULTIPLICACION:
            case TurixConstants.DIVISION:
                if ((izq.kind == TurixConstants.INT || izq.kind == TurixConstants.FLOAT) &&
                    (der.kind == TurixConstants.INT || der.kind == TurixConstants.FLOAT)) {
                    return; // válido entre numéricos
                }
                erroresSem.addError("Error: Operación '" + op.image + "' inválida entre " +
                        nombreTipo(izq.kind) + " y " + nombreTipo(der.kind));
                break;

            default:
                // otros operadores los puedes ir agregando
                break;
        }
    }
    public static String nombreTipo(int kind) {
        switch (kind) {
            case TurixConstants.INT: return "Int";
            case TurixConstants.FLOAT: return "Float";
            case TurixConstants.STRING: return "String";
            case TurixConstants.BOOL: return "Bool";
            case TurixConstants.DOUBLE: return "Double";
            default: return "Desconocido(" + kind + ")";
        }
    }

}
