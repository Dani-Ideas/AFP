/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fes.aragon.inicio;

import fes.aragon.codigo.Sym;

import fes.aragon.codigo.Lexico;
import fes.aragon.codigo.Tokens;
import fes.aragon.codigo.AutomataPila;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author MASH
 */
public class Inicio {
    private boolean error = false;
    private Tokens tokens = null;
    private Lexico analizador = null;

    public static void main(String[] args) {
        Inicio ap = new Inicio();
        BufferedReader buf;
        try {
            buf = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/archivo.txt"));
            ap.analizador = new Lexico(buf);
            AutomataPila apd = new AutomataPila(ap.analizador);
            if (apd.analizar()) {
                System.out.println("Cadena ACEPTADA");
            } else {
                System.out.println("Cadena RECHAZADA");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
