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
            case TurixConstants.NUM: return "Numero";
            case TurixConstants.MAS: return "MAS";
            case TurixConstants.MENOS: return "MENOS";
            case TurixConstants.DOUBLE: return "DOUBLE";
            case TurixConstants.INT: return "INT";
            case TurixConstants.UINT: return "UINT";
            case TurixConstants.FLOAT: return "FLOAT";
            case TurixConstants.STRING: return "STRING";
            case TurixConstants.BOOL: return "BOOL";
            case TurixConstants.CHARACTER: return "CHARACTER";
            case TurixConstants.LETTER: return "LETTER";
            case TurixConstants.AND: return "AND";
            case TurixConstants.NOT: return "NOT";
            case TurixConstants.OR: return "OR";
            case TurixConstants.REL_OP: return "REL_OP";
            case TurixConstants.DOS_PUN: return "DOS_PUN";
            case TurixConstants.IGUAL: return "IGUAL";
            case TurixConstants.CLASS: return "CLASS";
            case TurixConstants.DEINIT: return "DEINIT";
            case TurixConstants.ENUM: return "ENUM";
            case TurixConstants.EXTENSION: return "EXTENSION";
            case TurixConstants.FUNC: return "FUNC";
            case TurixConstants.IMPORT: return "IMPORT";
            case TurixConstants.INIT: return "INIT";
            case TurixConstants.INOUT: return "INOUT";
            case TurixConstants.INTERNAL: return "INTERNAL";
            case TurixConstants.LET: return "LET";
            case TurixConstants.OPERATOR: return "OPERATOR";
            case TurixConstants.PRIVATE: return "PRIVATE";
            case TurixConstants.PUBLIC: return "PUBLIC";
            case TurixConstants.STATIC: return "STATIC";
            case TurixConstants.STRUCT: return "STRUCT";
            case TurixConstants.SUBSCRIPT: return "SUBSCRIPT";
            case TurixConstants.TYPEALIAS: return "TYPEALIAS";
            case TurixConstants.VAR: return "VAR";
            case TurixConstants.BREAK: return "BREAK";
            case TurixConstants.CASE: return "CASE";
            case TurixConstants.CONTINUE: return "CONTINUE";
            case TurixConstants.DEFAULT_KEYWORD: return "DEFAULT_KEYWORD";
            case TurixConstants.DO: return "DO";
            case TurixConstants.ELSE: return "ELSE";
            case TurixConstants.FALLTHROUGH: return "FALLTHROUGH";
            case TurixConstants.FOR: return "FOR";
            case TurixConstants.IF: return "IF";
            case TurixConstants.IN: return "IN";
            case TurixConstants.RETURN: return "RETURN";
            case TurixConstants.SWITCH: return "SWITCH";
            case TurixConstants.WHERE: return "WHERE";
            case TurixConstants.WHILE: return "WHILE";
            case TurixConstants.K_I: return "K_I";
            case TurixConstants.K_F: return "K_F";
            case TurixConstants.PAR_I: return "PAR_I";
            case TurixConstants.PAR_F: return "PAR_F";
            case TurixConstants.COR_I: return "COR_I";
            case TurixConstants.COR_F: return "COR_F";
            case TurixConstants.AS: return "AS";
            case TurixConstants.CATCH: return "CATCH";
            case TurixConstants.FALSE: return "FALSE";
            case TurixConstants.IS: return "IS";
            case TurixConstants.NIL: return "NIL";
            case TurixConstants.RETHROWS: return "RETHROWS";
            case TurixConstants.SUPER: return "SUPER";
            case TurixConstants.SELF: return "SELF";
            case TurixConstants.SELF_CAP: return "SELF_CAP";
            case TurixConstants.THROW: return "THROW";
            case TurixConstants.THROWS: return "THROWS";
            case TurixConstants.TRUE: return "TRUE";
            case TurixConstants.TRY: return "TRY";
            case TurixConstants.ASSOCIATIVITY: return "ASSOCIATIVITY";
            case TurixConstants.CONVENIENCE: return "CONVENIENCE";
            case TurixConstants.DYNAMIC: return "DYNAMIC";
            case TurixConstants.DIDSET: return "DIDSET";
            case TurixConstants.FINAL: return "FINAL";
            case TurixConstants.GET: return "GET";
            case TurixConstants.INFIX: return "INFIX";
            case TurixConstants.INDIRECT: return "INDIRECT";
            case TurixConstants.LAZY: return "LAZY";
            case TurixConstants.LEFT: return "LEFT";
            case TurixConstants.MUTATING: return "MUTATING";
            case TurixConstants.NONE: return "NONE";
            case TurixConstants.NONMUTATING: return "NONMUTATING";
            case TurixConstants.OPTIONAL: return "OPTIONAL";
            case TurixConstants.OVERRIDE: return "OVERRIDE";
            case TurixConstants.POSTFIX: return "POSTFIX";
            case TurixConstants.PRECEDENCE: return "PRECEDENCE";
            case TurixConstants.PREFIX: return "PREFIX";
            case TurixConstants.PROTOCOL: return "PROTOCOL";
            case TurixConstants.PROTOCOL_CAP: return "PROTOCOL_CAP";
            case TurixConstants.REQUIRED: return "REQUIRED";
            case TurixConstants.RIGHT: return "RIGHT";
            case TurixConstants.SET: return "SET";
            case TurixConstants.TYPE: return "TYPE";
            case TurixConstants.UNOWNED: return "UNOWNED";
            case TurixConstants.WEAK: return "WEAK";
            case TurixConstants.WILLSET: return "WILLSET";
            case TurixConstants.PRINT: return "PRINT";
            case TurixConstants.IDENT: return "IDENT";
            case TurixConstants.EOF: return "EOF";
            default: return "ERROR";
        }
    }
}
