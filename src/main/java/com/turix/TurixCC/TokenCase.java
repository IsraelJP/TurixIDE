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
            case TurixConstants.MAS: return "Signo más";
            case TurixConstants.MENOS: return "Signo menos";
            case TurixConstants.DOUBLE: return "Double";
            case TurixConstants.INT: return "Int";
            case TurixConstants.UINT: return "UInt";
            case TurixConstants.FLOAT: return "Float";
            case TurixConstants.STRING: return "String";
            case TurixConstants.BOOL: return "Bool";
            case TurixConstants.CHARACTER: return "Character";
            case TurixConstants.LETTER: return "Letter";
            case TurixConstants.AND: return "And";
            case TurixConstants.NOT: return "Not";
            case TurixConstants.OR: return "Or";
            case TurixConstants.REL_OP: return "Operador Relacional";
            case TurixConstants.DOS_PUN: return "Dos Puntos";
            case TurixConstants.IGUAL: return "Igual";
            case TurixConstants.CLASS: return "Class";
            case TurixConstants.DEINIT: return "Deint";
            case TurixConstants.ENUM: return "Enum";
            case TurixConstants.EXTENSION: return "Extension";
            case TurixConstants.FUNC: return "Func";
            case TurixConstants.IMPORT: return "Import";
            case TurixConstants.INIT: return "Init";
            case TurixConstants.INOUT: return "Inout";
            case TurixConstants.INTERNAL: return "Internal";
            case TurixConstants.LET: return "Let";
            case TurixConstants.OPERATOR: return "Operator";
            case TurixConstants.PRIVATE: return "Private";
            case TurixConstants.PUBLIC: return "Public";
            case TurixConstants.STATIC: return "Static";
            case TurixConstants.STRUCT: return "Struct";
            case TurixConstants.SUBSCRIPT: return "Subscript";
            case TurixConstants.TYPEALIAS: return "Typealias";
            case TurixConstants.VAR: return "Var";
            case TurixConstants.BREAK: return "Break";
            case TurixConstants.CASE: return "Case";
            case TurixConstants.CONTINUE: return "Continue";
            case TurixConstants.DEFAULT_KEYWORD: return "Default";
            case TurixConstants.DO: return "Do";
            case TurixConstants.ELSE: return "Else";
            case TurixConstants.FALLTHROUGH: return "Fallthrough";
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
            case TurixConstants.AS: return "As";
            case TurixConstants.CATCH: return "Catch";
            case TurixConstants.FALSE: return "False";
            case TurixConstants.IS: return "Is";
            case TurixConstants.NIL: return "Nill";
            case TurixConstants.RETHROWS: return "Rethrows";
            case TurixConstants.SUPER: return "Super";
            case TurixConstants.SELF: return "Self ";
            case TurixConstants.SELF_CAP: return "Self opcion 2";
            case TurixConstants.THROW: return "Throw";
            case TurixConstants.THROWS: return "Throws";
            case TurixConstants.TRUE: return "True";
            case TurixConstants.TRY: return "Try";
            case TurixConstants.ASSOCIATIVITY: return "Associativity";
            case TurixConstants.CONVENIENCE: return "Convenience";
            case TurixConstants.DYNAMIC: return "Dynamic";
            case TurixConstants.DIDSET: return "Didset";
            case TurixConstants.FINAL: return "Final";
            case TurixConstants.GET: return "Get";
            case TurixConstants.INFIX: return "Infix";
            case TurixConstants.INDIRECT: return "Indirect";
            case TurixConstants.LAZY: return "Lazy";
            case TurixConstants.LEFT: return "Left";
            case TurixConstants.MUTATING: return "Mutating";
            case TurixConstants.NONE: return "None";
            case TurixConstants.NONMUTATING: return "Nonmutating";
            case TurixConstants.OPTIONAL: return "Optional";
            case TurixConstants.OVERRIDE: return "Override";
            case TurixConstants.POSTFIX: return "Postfix";
            case TurixConstants.PRECEDENCE: return "Precedence";
            case TurixConstants.PREFIX: return "Prefix";
            case TurixConstants.PROTOCOL: return "Protocol";
            case TurixConstants.PROTOCOL_CAP: return "Protocol opcion 2";
            case TurixConstants.REQUIRED: return "Required";
            case TurixConstants.RIGHT: return "Right";
            case TurixConstants.SET: return "Set";
            case TurixConstants.TYPE: return "Type";
            case TurixConstants.UNOWNED: return "Unowned";
            case TurixConstants.WEAK: return "Weak";
            case TurixConstants.WILLSET: return "Willset";
            case TurixConstants.PRINT: return "Print";
            case TurixConstants.IDENT: return "Identificador";
            case TurixConstants.EOF: return "EOF";
            case TurixConstants.ERROR: return "ERROR";
            case TurixConstants.ERROROPERA: return "ERROR";
            case TurixConstants.BOOLEANO_VAL: return "Valor booleano";
            case TurixConstants.MULTIPLICACION: return "Signo multiplicación";
            case TurixConstants.DIVISION: return "Signo división";
            case TurixConstants.MODULO: return "Signo módulo";
            
            default: return "No registrado";
        }
    }
}
