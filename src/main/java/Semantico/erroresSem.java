/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Semantico;

import java.util.ArrayList;

/**
 *
 * @author stare
 */
public class erroresSem {
    private static ArrayList<String> erroresSem = new ArrayList<>();

    public static void addError(String msg) {
        erroresSem.add(msg);
    }

    public static ArrayList<String> getErrores() {
        return erroresSem;
    }

    public static void resetErrores() {
        erroresSem.clear();
    }
}