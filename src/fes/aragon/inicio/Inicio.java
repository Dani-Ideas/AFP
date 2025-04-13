/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fes.aragon.inicio;

import fes.aragon.codigo.Lexico;
import fes.aragon.codigo.Sym;
import fes.aragon.codigo.Tokens;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 *
 * @author MASH
 */
public class Inicio {
    private boolean error = false;
    private Tokens tokens = null;
    private Lexico analizador = null;
    private Stack<String> pila = new Stack<>();

    public static void main(String[] args) {
        Inicio ap = new Inicio();
        BufferedReader buf;
        try {
            buf = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/archivo.txt"));
            ap.analizador = new Lexico(buf);
            //ap.siguienteToken();
            //ap.sentencia();
            ap.S();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void S() {
        pila.push(";");
        pila.push("S");
        siguienteToken();
        if (tokens.getLexema() == Sym.A || tokens.getLexema() == Sym.B) {
            System.out.println("🔍 Token leído: " + tokens.getLexema() + " (" + tokens.getToken() + ") "+"Aplicando axioma: S := AB");
            // Simulamos el comportamiento de la pila como derivación
            pila.pop(); // quitamos S
            pila.push("B"); // metemos B
            pila.push("A"); // metemos A

            A();
            if (!error) {
                B();
            }
        } else {
            error = true;
            System.out.println("Error: Token inválido para el axioma S. Se esperaba 'a' o 'b'");
        }

        if (!error) {
            siguienteToken(); // buscamos el punto y coma
            if (tokens.getLexema() == Sym.PUNTOCOMA) {
                System.out.println("✔️ Punto y coma encontrado.");
                System.out.println("🌀 Contenido de la pila: " + pila);
                System.out.println("📥 Aquí comenzaría la fase de análisis de derecha a izquierda.");
            } else {
                error = true;
                System.out.println("❌ Error: Se esperaba ';' después de la expresión.");
            }
        }
    }


    private void A() {
        System.out.println("🔍 Analizando A con token: " + tokens.getLexema() + " (" + tokens.getToken() + ")");

        if (tokens.getLexema() == Sym.A) {
            pila.pop(); // quitamos A porque lo reemplazamos por 'a'
            System.out.println("✅ Producción aplicada: A := a");
            siguienteToken(); // consumimos 'a'
        } else if (tokens.getLexema() == Sym.B) {
            pila.pop(); // quitamos A porque A := λ
            System.out.println("✅ Producción aplicada: A := λ (epsilon)");
        } else {
            error = true;
            System.out.println("❌ Error en A: Token inesperado. Se esperaba 'a' o 'b'");
        }
    }

    private void B() {
        if(tokens.getLexema() == Sym.ENTERO) {
            siguienteToken();
        } else {
            error = true;
            System.out.println("error en B-> se espera un entero");
        }
    }

    private void sentencia() {
        do {
            asignacion();
            if (tokens.getLexema() != Sym.PUNTOCOMA) {
                errorSintactico();
                siguienteToken();
            }
            if (!this.error) {
                System.out.println("Invalida linea= " + (tokens.getLinea() + 1));
                this.error = true;
            } else {
                System.out.println("Valida  linea= " + (tokens.getLinea() + 1));
            }
            siguienteToken();
        } while (tokens.getLexema() != Sym.EOF);
    }

    private void asignacion() {
        if (tokens.getLexema() == Sym.ID) {
            siguienteToken();
            if (tokens.getLexema() == Sym.IGUAL) {
                siguienteToken();
                expresion();
            } else {
                errorSintactico();
            }
        } else {
            errorSintactico();
        }
    }

    private void expresion() {
        if (tokens.getLexema() == Sym.ID || tokens.getLexema() == Sym.ENTERO) {
            siguienteToken();
            if (tokens.getLexema() == Sym.MAS || tokens.getLexema() == Sym.MENOS) {
                siguienteToken();
                expresion();
            } else {
                if (tokens.getLexema() != Sym.PUNTOCOMA) {
                    errorSintactico();
                }
            }
        } else {
            if (tokens.getLexema() != Sym.PUNTOCOMA) {
                errorSintactico();
            }
        }
    }

    private void errorSintactico() {
        this.error = false;
        //descartar todo hasta encontrar ;
        do {
            System.out.println(tokens.toString());
            if (tokens.getLexema() != Sym.PUNTOCOMA) {
                siguienteToken();
            }
        } while (tokens.getLexema() != Sym.PUNTOCOMA && tokens.getLexema() != Sym.EOF);

    }

    private void siguienteToken() {
        try {
            tokens = analizador.yylex();
            if (tokens == null) {
                tokens = new Tokens("EOF", Sym.EOF, 0, 0);
                throw new IOException("Fin Archivo");
            }
            System.out.println("🔍 Token leído: " + tokens.getLexema() + " (" + tokens.getToken() + ")");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
