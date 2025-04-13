package fes.aragon.inicio;

import fes.aragon.codigo.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Inicio {
    public static void main(String[] args) {
        try {
            BufferedReader buf = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/archivo.txt"));
            Lexico analizador = new Lexico(buf);
            Stack<String> pila = new Stack<>();
            SiguienteToken tokenHandler = new SiguienteToken(analizador);
            Axiomas axiomas = new Axiomas(pila, tokenHandler);
            Derivador derivador = new Derivador(pila, axiomas.getProducciones(), tokenHandler);
            axiomas.S();
            derivador.analizar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
