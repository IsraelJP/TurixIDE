/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Semantico;

import com.turix.TurixCC.Token;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author stare
 */
public class TokenAsignaciones {
    //Tabla que almacena tokens
    private static Hashtable tabla=new Hashtable();
     //Listas
    private static ArrayList<Integer> intComp = new ArrayList();
    private static ArrayList<Integer> decComp = new ArrayList();
    private static ArrayList<Integer> strComp = new ArrayList();
    private static ArrayList<Integer> boolComp = new ArrayList();

    public static void InsertarSimbolo(Token identificador, int tipoDato){
        //En este método se agrega a la tabla de tokens el identificador que esta siendo 
        //declarado junto con su tipo de dato
        tabla.put(identificador.image, tipoDato);
    }
    
    public static void SetTables(){
        //Inicialización de tablas, almacena el tipo de dato compatible
        
        intComp.add(5);
        intComp.add(19);
        
        decComp.add(5);
        decComp.add(12);
        decComp.add(19);
        decComp.add(20);
        decComp.add(20);
        
        strComp.add(6);
        strComp.add(22);
        strComp.add(24);
        
        boolComp.add(23);
        boolComp.add(58);
        boolComp.add(59);
        
    }
    
   /* public static String checkAsing(Token TokenIzq, Token TokenAsig){
        //Variables para almacenar el tipo de dato del identificador 
        //y las asignaciones
        int tipoIdent1;
        int tipoIdent2 ;
        
        if (TokenIzq.kind!=5){
            try{
                tipoIdent1=(Integer)tabla.get(TokenIzq.image);
            }catch(Exception e){
                return "Error el identificador"+TokenIzq.image + "No ha sido declarado \r\Linea: "+TokenIzq.beginLine;
            }
        }
        
    }*/
}
