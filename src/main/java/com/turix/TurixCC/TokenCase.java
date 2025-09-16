/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.turix.TurixCC;

/**
 *
 * @author stare
 */
public class TokenCase {
    public static String getTokenNombre(int tipo) {
        switch (tipo) {
            case TurixConstants.NUM: return "Número";
            case TurixConstants.STRING_LITERAL: return "Cadena";
            case TurixConstants.MAS: return "Signo más";
            case TurixConstants.MENOS: return "Signo menos";
            case TurixConstants.DOUBLE: return "Double";
            case TurixConstants.INT: return "Int";
            case TurixConstants.UINT: return "UInt";
            case TurixConstants.FLOAT: return "Float";
            case TurixConstants.STRING: return "String";
            case TurixConstants.BOOL: return "Bool";
            case TurixConstants.CHARACTER: return "Character";
            case TurixConstants.AND: return "And";
            case TurixConstants.NOT: return "Not";
            case TurixConstants.OR: return "Or";
            case TurixConstants.REL_OP: return "Operador Relacional";
            case TurixConstants.DOS_PUN: return "Dos Puntos";
            case TurixConstants.FLECHA: return "Flecha";
            case TurixConstants.COMILLA: return "Comillas";
            case TurixConstants.COMA: return "Coma";
            case TurixConstants.IGUAL: return "Igual";
            case TurixConstants.FUNC: return "Func";
            case TurixConstants.LET: return "Let";
            case TurixConstants.VAR: return "Var";
            case TurixConstants.BREAK: return "Break";
            case TurixConstants.CASE: return "Case";
            case TurixConstants.DEFAULT_KEYWORD: return "Default";
            case TurixConstants.REPEAT: return "Repeat";
            case TurixConstants.ELSE: return "Else";
            case TurixConstants.FOR: return "For";
            case TurixConstants.IF: return "If";
            case TurixConstants.IN: return "In";
            case TurixConstants.RETURN: return "Return";
            case TurixConstants.SWITCH: return "Switch";
            case TurixConstants.WHERE: return "Where";
            case TurixConstants.WHILE: return "While";
            case TurixConstants.K_I: return "Llave de apaertura";
            case TurixConstants.K_F: return "Llave de cierre";
            case TurixConstants.PAR_I: return "Parentesis de apertura";
            case TurixConstants.PAR_F: return "Parentesis de cierre";
            case TurixConstants.COR_I: return "Corchete de apertura";
            case TurixConstants.COR_F: return "Corchete de cerradura";
            case TurixConstants.FALSE: return "False";
            case TurixConstants.TRUE: return "True";
            case TurixConstants.PRINT: return "Print";
            case TurixConstants.IDENT: return "Identificador";
            case TurixConstants.EOF: return "EOF";
            case TurixConstants.ERROR: return "ERROR";
            case TurixConstants.ERROROPERA: return "ERROR";
            case TurixConstants.MULTIPLICACION: return "Signo multiplicación";
            case TurixConstants.DIVISION: return "Signo división";
            case TurixConstants.MODULO: return "Signo módulo";
            case TurixConstants.TERMINATOR: return "Terminator";
            case TurixConstants.SEPARATOR: return "Separator";
            case TurixConstants.READLINE: return "Lectura de linea";
            case TurixConstants.ERROR_IDENT: return "ERROR";
            
            default: return "No registrado";
        }
    }
}
