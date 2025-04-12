package fes.aragon.codigo;

import java.io.IOException;
import java.util.Stack;

public class AutomataPila {
    private Stack<Integer> pila = new Stack<>();
    private Tokens tokenActual;
    private Lexico analizador;

    public AutomataPila(Lexico analizador) {
        this.analizador = analizador;
        pila.push(Sym.PUNTOCOMA); // Símbolo de fondo
        pila.push(Sym.PILA_S);    // Símbolo inicial
    }

    public boolean analizar() {
        siguienteToken();
        while (!pila.isEmpty() && tokenActual != null) {
            int topePila = pila.peek();

            System.out.println("Pila: " + representarPila() +
                    " | Entrada: " + tokenActual.getToken());

            if (topePila == tokenActual.getLexema()) {
                pila.pop();
                siguienteToken();
            } else {
                switch (topePila) {
                    case Sym.PILA_S:
                        if (tokenActual.getLexema() == Sym.LETRA_A ||
                                tokenActual.getLexema() == Sym.LETRA_B) {
                            pila.pop();
                            pila.push(Sym.PILA_B);
                            pila.push(Sym.PILA_A);
                        } else {
                            error("Se esperaba 'a' o 'b'");
                            return false;
                        }
                        break;

                    case Sym.PILA_A:
                        if (tokenActual.getLexema() == Sym.LETRA_A) {
                            pila.pop();
                            pila.push(Sym.LETRA_A);
                        } else {
                            pila.pop(); // A → λ
                        }
                        break;

                    case Sym.PILA_B:
                        if (tokenActual.getLexema() == Sym.LETRA_B) {
                            pila.pop();
                            pila.push(Sym.LETRA_D);
                            pila.push(Sym.PILA_C);
                            pila.push(Sym.LETRA_B);
                        } else {
                            error("Se esperaba 'b'");
                            return false;
                        }
                        break;

                    case Sym.PILA_C:
                        if (tokenActual.getLexema() == Sym.LETRA_C) {
                            pila.pop();
                            pila.push(Sym.LETRA_C);
                        } else {
                            pila.pop(); // C → λ
                        }
                        break;

                    default:
                        error("Símbolo inesperado en pila: " + topePila);
                        return false;
                }
            }
        }

        return pila.size() == 1 &&
                pila.peek() == Sym.PUNTOCOMA &&
                tokenActual.getLexema() == Sym.PUNTOCOMA;
    }

    private String representarPila() {
        StringBuilder sb = new StringBuilder();
        for (int simbolo : pila) {
            switch (simbolo) {
                case Sym.PILA_S: sb.append("S"); break;
                case Sym.PILA_A: sb.append("A"); break;
                case Sym.PILA_B: sb.append("B"); break;
                case Sym.PILA_C: sb.append("C"); break;
                case Sym.PUNTOCOMA: sb.append(";"); break;
                case Sym.LETRA_A: sb.append("a"); break;
                case Sym.LETRA_B: sb.append("b"); break;
                case Sym.LETRA_C: sb.append("c"); break;
                case Sym.LETRA_D: sb.append("d"); break;
                default: sb.append("?");
            }
        }
        return sb.toString();
    }

    private void siguienteToken() {
        try {
            tokenActual = analizador.yylex();
            if (tokenActual == null) {
                tokenActual = new Tokens(";", Sym.PUNTOCOMA, 0, 0);
            }
        } catch (IOException e) {
            error("Error al leer token: " + e.getMessage());
        }
    }

    private void error(String mensaje) {
        System.err.println("ERROR: " + mensaje);
        if (tokenActual != null) {
            System.err.println("En línea: " + (tokenActual.getLinea() + 1) +
                    ", columna: " + (tokenActual.getColumna() + 1));
        }
    }
}