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
        siguienteToken(); // Primer token
        Derivador(); // Aquí controlamos todo el análisis
    }


    private String A() {
        System.out.print("🔍 Analizando A con token: " + tokens.getLexema() + " (" + tokens.getToken() + ")  ");
        if (tokens.getLexema() == Sym.A) {
            System.out.println("✅ Producción aplicada: A := a");
            return "a";
        } else if (tokens.getLexema() == Sym.B) {
            System.out.println("✅ Producción aplicada: A := λ (epsilon)");
            return ""; // Epsilon
        } else {
            error = true;
            System.out.println("❌ Error en A: Se esperaba 'a' o 'b'");
            return "";
        }
    }
    private String B() {
        if(tokens.getLexema() == Sym.B) {
            System.out.println("✅ Producción aplicada: B := bCd");
            return "bCd";
        } else {
            error = true;
            System.out.println("error en B-> se espera 'b' y solo 'b'");
            return "";
        }
    }
    private String C() {
        if(tokens.getLexema() == Sym.C) {
            System.out.println("✅ Producción aplicada: C:=c");
            return "c";
        } else if (tokens.getLexema() == Sym.D) {
            System.out.println("✅ Producción aplicada: C:=λ (epsilon)");
            return ""; // Epsilon
        } else {
            error = true;
            System.out.println("error en C-> se espera 'c' o 'd'");
            return "";
        }
    }
    private void Derivador() {
        while (!pila.isEmpty() && !error) {
            String cima = pila.peek(), resultado; // Revisamos el símbolo en la cima de la pila

            switch (cima) {
                case "A":
                    pila.pop(); // Retiramos "A" de la pila
                    resultado = A(); // Aplicamos la producción para A
                    if (!resultado.equals("")) {
                        pila.push(resultado); // Insertamos el resultado (por ejemplo, 'a')
                    }
                    break;

                case "B":
                    pila.pop(); // Retiramos "B" de la pila
                    resultado=B();
                    if (!resultado.isEmpty()) {
                        for (int i = resultado.length() - 1; i >= 0; i--) {
                            pila.push(String.valueOf(resultado.charAt(i)));
                        }
                    }
                    break;
                case "C":
                    pila.pop(); // Retiramos "C" de la pila
                    resultado = C();
                    if (!resultado.equals("")) {
                        pila.push(resultado);
                    }
                    break;
                case "S":
                    pila.pop(); // quitamos S
                    pila.push("B");
                    pila.push("A");
                    System.out.println("✅ Producción aplicada: S := AB");
                    break;

                case ";":
                    if (tokens.getLexema() == Sym.PUNTOCOMA) {
                        pila.pop(); // Consumimos el punto y coma de la pila
                        System.out.println("✔️ Punto y coma encontrado.");
                        siguienteToken(); // Avanzamos al siguiente token para continuar con la derivación a derecha
                        // Continuar vaciando la pila y tokens
                    } else {
                        error = true;
                        System.out.println("❌ Error: Se esperaba ';' al final de la expresión.");
                        break;
                    }
                    break;

                default:
                    // Si es un terminal esperado, lo comparamos con el token actual
                    if (cima.equals(tokens.getLexemaNombre())) {
                        pila.pop(); // Consumimos de la pila
                        siguienteToken(); // Avanzamos al siguiente token
                    } else {
                        error = true;
                        System.out.println("❌ Error: Token inesperado. Se esperaba '" + cima + "'");
                    }
                    break;
            }

        }
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
            System.out.print("🔍 Token leído: " + tokens.getLexema() + " (" + tokens.getToken() + ")   ");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
