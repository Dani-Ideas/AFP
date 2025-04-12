package fes.aragon.inicio;

import fes.aragon.codigo.Sym;
import fes.aragon.codigo.Lexico;
import fes.aragon.codigo.AutomataPila;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Inicio {
    public static void main(String[] args) {
        BufferedReader buf;
        try {
            buf = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/archivo.txt"));

            Lexico analizador = new Lexico(buf);
            AutomataPila apd = new AutomataPila(analizador);

            if (apd.analizar()) {
                System.out.println("Cadena ACEPTADA");
            } else {
                System.out.println("Cadena RECHAZADA");
            }

        } catch (IOException e) {
            System.err.println("Error al leer archivo: " + e.getMessage());
        }
    }
}