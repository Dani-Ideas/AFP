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
        while (!pila.isEmpty()) {
            int topePila = pila.peek();

            System.out.println("Pila: " + representarPila() +
                    " | Entrada: " + tokenActual.getToken());

            if (esTerminal(topePila)) {
                if (topePila == tokenActual.getLexema()) {
                    pila.pop();
                    siguienteToken();
                } else {
                    error("Se esperaba: '" + simboloToString(topePila) + "'");
                    return false;
                }
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
                        pila.pop();
                        if (tokenActual.getLexema() == Sym.LETRA_A) {
                            pila.push(Sym.LETRA_A);
                        }
                        // A → λ en caso contrario
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
                        pila.pop();
                        if (tokenActual.getLexema() == Sym.LETRA_C) {
                            pila.push(Sym.LETRA_C);
                        }
                        // C → λ en caso contrario
                        break;

                    default:
                        error("Símbolo inesperado en pila: " + simboloToString(topePila));
                        return false;
                }
            }
        }

        return tokenActual.getLexema() == Sym.PUNTOCOMA;
    }

    private boolean esTerminal(int simbolo) {
        return simbolo == Sym.LETRA_A || simbolo == Sym.LETRA_B ||
                simbolo == Sym.LETRA_C || simbolo == Sym.LETRA_D ||
                simbolo == Sym.PUNTOCOMA;
    }

    private String representarPila() {
        StringBuilder sb = new StringBuilder();
        for (int simbolo : pila) {
            sb.append(simboloToString(simbolo));
        }
        return sb.toString();
    }

    private String simboloToString(int simbolo) {
        switch (simbolo) {
            case Sym.PILA_S: return "S";
            case Sym.PILA_A: return "A";
            case Sym.PILA_B: return "B";
            case Sym.PILA_C: return "C";
            case Sym.PUNTOCOMA: return ";";
            case Sym.LETRA_A: return "a";
            case Sym.LETRA_B: return "b";
            case Sym.LETRA_C: return "c";
            case Sym.LETRA_D: return "d";
            default: return "?";
        }
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
