package Semantico;

import com.turix.TurixCC.Token;
import com.turix.TurixCC.TurixConstants;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Semántica de asignaciones y tabla de símbolos.
 * - Usa checkAsing(Token, String) para validar con la EXPRESIÓN COMPLETA (exprBufGet()).
 * - Mantiene checkAsing(Token, Token) para compatibilidad (literales/ident simples).
 */
public class TokenAsignaciones {

    public static boolean banInicio = false;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Hashtable tabla = new Hashtable();

    // Listas legacy (compatibilidad)
    private static final ArrayList<Integer> intComp  = new ArrayList<>();
    private static final ArrayList<Integer> floatComp= new ArrayList<>();
    private static final ArrayList<Integer> strComp  = new ArrayList<>();
    private static final ArrayList<Integer> boolComp = new ArrayList<>();

    /** Inicializa compatibilidades básicas. Llámalo al iniciar el parser. */
    public static void SetTables() {
        intComp.clear(); floatComp.clear(); strComp.clear(); boolComp.clear();

        // INT compatibles
        intComp.add(TurixConstants.INT);
        intComp.add(TurixConstants.NUM); // literal entero

        // FLOAT/DOUBLE compatibles
        floatComp.add(TurixConstants.FLOAT);
        // si usas DOUBLE como tipo, trátalo como float para compatibilidad
        if (hasConst("DOUBLE")) floatComp.add(TurixConstants.DOUBLE);
        floatComp.add(TurixConstants.NUM);
        floatComp.add(TurixConstants.NUM_DEC);

        // STRING compatibles
        strComp.add(TurixConstants.STRING);
        if (hasConst("STRING_LITERAL")) strComp.add(TurixConstants.STRING_LITERAL);

        // BOOL compatibles
        boolComp.add(TurixConstants.BOOL);
        if (hasConst("TRUE"))  boolComp.add(TurixConstants.TRUE);
        if (hasConst("FALSE")) boolComp.add(TurixConstants.FALSE);
    }

    // Utilidad: algunas gramáticas no generan ciertos constants (evita NPEs)
    private static boolean hasConst(String name) {
        try {
            TurixConstants.class.getField(name);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    /** Inserta símbolo; si se da valor (token simple), valida legacy. */
    public static void InsertarSimbolo(Token identificador, int tipoDato, Token valor){
        tabla.put(identificador.image, tipoDato);
        if (valor != null) {
            // Validación simple por token (mejor usar la versión con String)
            checkAsing(identificador, valor);
        }
    }

    // =========================================================
    // 1) NUEVO: Validación con EXPRESIÓN COMPLETA (String)
    // =========================================================
    public static void checkAsing(Token TokenIzq, String exprCompleta){
        Integer tipoL = (Integer) tabla.get(TokenIzq.image);
        if (tipoL == null) {
            erroresSem.addError("Error: el identificador " + TokenIzq.image +
                    " no ha sido declarado \r\nLinea: " + TokenIzq.beginLine);
            return;
        }

        int tipoR = tipoDominanteDeExpr(exprCompleta);

        // Normalizamos: trata DOUBLE como FLOAT para reglas de conversión
        if (tipoR == valOrDefault("DOUBLE", -1)) tipoR = TurixConstants.FLOAT;
        if (tipoL == valOrDefault("DOUBLE", -1)) tipoL = TurixConstants.FLOAT;

        if (tipoL == TurixConstants.INT) {
            if (tipoR != TurixConstants.INT) {
                erroresSem.addError("Error: no se puede convertir la expresión (" + exprCompleta +
                        ") a Int \r\nLinea: " + TokenIzq.beginLine);
            }
            return;
        }

        if (tipoL == TurixConstants.FLOAT) {
            if (!(tipoR == TurixConstants.INT || tipoR == TurixConstants.FLOAT)) {
                erroresSem.addError("Error: no se puede convertir la expresión (" + exprCompleta +
                        ") a Float \r\nLinea: " + TokenIzq.beginLine);
            }
            return;
        }

        if (tipoL == TurixConstants.STRING) {
            if (tipoR != TurixConstants.STRING) {
                erroresSem.addError("Error: no se puede convertir la expresión (" + exprCompleta +
                        ") a String \r\nLinea: " + TokenIzq.beginLine);
            }
            return;
        }

        if (tipoL == TurixConstants.BOOL) {
            if (tipoR != TurixConstants.BOOL) {
                erroresSem.addError("Error: no se puede convertir la expresión (" + exprCompleta +
                        ") a Bool \r\nLinea: " + TokenIzq.beginLine);
            }
            return;
        }

        // Otros tipos: sin restricción específica por ahora
    }

    private static int valOrDefault(String field, int def) {
        try {
            return TurixConstants.class.getField(field).getInt(null);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * Inferencia *ligera* del tipo dominante de una expresión INFija (string).
     * Reglas:
     *  - Si contiene comillas -> STRING
     *  - Si contiene true/false u operadores lógicos (&&, ||, !) -> BOOL
     *  - Si hay algún número con punto decimal -> FLOAT
     *  - Si hay identificadores, elevamos según su tipo en la tabla: STRING > FLOAT > BOOL > INT
     *  - Por defecto -> INT
     */
    private static int tipoDominanteDeExpr(String expr) {
        if (expr == null) return TurixConstants.INT;
        String s = expr.trim();

        // STRING
        if (s.contains("\"")) return TurixConstants.STRING;

        // BOOL
        if (s.matches(".*\\b(true|false)\\b.*") || s.matches(".*(&&|\\|\\||!).*")) {
            return TurixConstants.BOOL;
        }

        // FLOAT (o DOUBLE)
        if (s.matches(".*\\d+\\.\\d+.*")) {
            return TurixConstants.FLOAT;
        }

        // Identificadores: elevamos
        Matcher m = Pattern.compile("\\b[_a-zA-Z][_a-zA-Z0-9]*\\b").matcher(s);
        int dom = TurixConstants.INT;
        while (m.find()) {
            String id = m.group();

            // Ignorar algunas palabras reservadas conocidas
            if ("sqrt pow cos sin log Int Float String Bool Character var let if else while for switch return print readLine true false".contains(id))
                continue;

            Object t = tabla.get(id);
            if (t instanceof Integer) {
                int tk = (Integer) t;
                if (tk == TurixConstants.STRING) return TurixConstants.STRING; // domina todo
                if (tk == TurixConstants.FLOAT || tk == valOrDefault("DOUBLE", -1)) dom = TurixConstants.FLOAT;
                if (tk == TurixConstants.BOOL && dom == TurixConstants.INT) dom = TurixConstants.BOOL;
            }
        }

        return dom; // INT si nada elevó
    }

    // =========================================================
    // 2) LEGACY: Validación con un solo Token (mantener compat.)
    // =========================================================
    public static void checkAsing(Token TokenIzq, Token TokenAsig){
        Integer tipoIdent = (Integer) tabla.get(TokenIzq.image);
        if (tipoIdent == null) {
            banInicio = true;
            erroresSem.addError("El identificador " + TokenIzq.image + " no ha sido declarado " +
                    " \r\nLinea: " + TokenIzq.beginLine);
            return;
        }
        if (TokenAsig == null) return;

        int tipoAsig = deduceTipoFromToken(TokenAsig);

        // Trata DOUBLE como FLOAT
        if (tipoIdent == valOrDefault("DOUBLE", -1)) tipoIdent = TurixConstants.FLOAT;
        if (tipoAsig  == valOrDefault("DOUBLE", -1)) tipoAsig  = TurixConstants.FLOAT;

        if (tipoIdent == TurixConstants.INT) {
            if (tipoAsig != TurixConstants.INT) {
                erroresSem.addError("Error: No se puede convertir " + TokenAsig.image + " a Int \r\nLinea: " + TokenIzq.beginLine);
            }
        } else if (tipoIdent == TurixConstants.FLOAT) {
            if (!(tipoAsig == TurixConstants.INT || tipoAsig == TurixConstants.FLOAT)) {
                erroresSem.addError("Error: No se puede convertir " + TokenAsig.image + " a Float \r\nLinea: " + TokenIzq.beginLine);
            }
        } else if (tipoIdent == TurixConstants.STRING) {
            if (tipoAsig != TurixConstants.STRING) {
                erroresSem.addError("Error: No se puede convertir " + TokenAsig.image + " a String \r\nLinea: " + TokenIzq.beginLine);
            }
        } else if (tipoIdent == TurixConstants.BOOL) {
            if (tipoAsig != TurixConstants.BOOL) {
                erroresSem.addError("Error: No se puede convertir " + TokenAsig.image + " a Bool \r\nLinea: " + TokenIzq.beginLine);
            }
        }
    }

    /** Deducción básica desde un Token concreto (legacy). */
    private static int deduceTipoFromToken(Token t) {
        if (t == null) return TurixConstants.INT;

        switch (t.kind) {
            case TurixConstants.NUM:              return TurixConstants.INT;
            case TurixConstants.NUM_DEC:          return TurixConstants.FLOAT;
            case TurixConstants.STRING_LITERAL:   return TurixConstants.STRING;
            case TurixConstants.TRUE:
            case TurixConstants.FALSE:
                return TurixConstants.BOOL;

            case TurixConstants.IDENT:
                Object v = tabla.get(t.image);
                if (v instanceof Integer) return (Integer) v;
                erroresSem.addError("Error: El identificador " + t.image +
                        " no ha sido declarado\r\nLinea: " + t.beginLine);
                return TurixConstants.INT;

            // Si tienes token DOUBLE distinto, mapealo a FLOAT
            default:
                if (t.kind == valOrDefault("DOUBLE", -1)) return TurixConstants.FLOAT;
                return TurixConstants.INT;
        }
    }

    /** Verifica si un identificador está declarado (usado por el parser). */
    public static String checkVariable(Token checkTok){
        try {
            Integer tipo = (Integer) tabla.get(checkTok.image);
            if (tipo == null) throw new RuntimeException();
            return " ";
        } catch (Exception e){
            return "Error: El identificador "+checkTok.image+" no ha sido declarado.\r\nLinea: "+checkTok.beginLine;
        }
    }
}
