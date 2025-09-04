package com.turix.TurixCC;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Calculadora Turix. Escribe una expresión y presiona Enter.");
        System.out.println("Vacío o Ctrl+D para salir.");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print(">> ");
                System.out.flush(); // asegura que el prompt se vea al instante
                String line = br.readLine();
                if (line == null || line.trim().isEmpty()) {
                    System.out.println("Hasta luego.");
                    break;
                }
                try {
                    Turix parser = new Turix(new StringReader(line));
                    parser.Start();  // tu Start actual solo valida; si luego haces que regrese un valor, aquí lo imprimes
                    System.out.println("✔ Expresión válida");
                } catch (ParseException pe) {
                    System.out.println("✘ Error de sintaxis: " + pe.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        }
    }
}
