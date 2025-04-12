package fes.aragon.codigo;

import java.io.IOException;
import java.util.Stack;

public class AutomataPila {
    private Stack<Integer> pila = new Stack<>();
    private Tokens tokenActual;
    private Lexico analizador;
    private boolean aceptado = false;

    public AutomataPila(Lexico analizador) {
        this.analizador = analizador;
        // Inicializar pila con símbolo inicial (S) y símbolo de fondo (;)
        pila.push(Sym.PUNTOCOMA); // Símbolo de fondo de pila
        pila.push(Sym.PILA_S);     // Símbolo inicial S (para la pila)
    }

    public boolean analizar() {
        siguienteToken();
        while (!pila.isEmpty() && tokenActual != null) {
            int topePila = pila.peek();

            System.out.println("Pila: " + representarPila() + " | Entrada: " +
                    (tokenActual != null ? tokenActual.getToken() : "EOF"));

            if (topePila == tokenActual.getLexema()) {
                // Coincidencia entre tope de pila y token actual
                pila.pop();
                siguienteToken();
            } else {
                // Aplicar reglas de producción
                switch (topePila) {
                    case Sym.PILA_S: // S → AB
                        if (tokenActual.getLexema() == Sym.LETRA_A ||
                                tokenActual.getLexema() == Sym.LETRA_B) {
                            pila.pop(); // Sacar S
                            pila.push(Sym.PILA_B); // Primero B
                            pila.push(Sym.PILA_A); // Luego A
                        } else {
                            return false; // Error sintáctico
                        }
                        break;

                    case Sym.PILA_A: // A → a | λ
                        if (tokenActual.getLexema() == Sym.LETRA_A) {
                            pila.pop(); // Sacar A
                            pila.push(Sym.LETRA_A); // Poner a (token)
                        } else {
                            pila.pop(); // A → λ (epsilon)
                        }
                        break;

                    case Sym.PILA_B: // B → bCd
                        if (tokenActual.getLexema() == Sym.LETRA_B) {
                            pila.pop(); // Sacar B
                            pila.push(Sym.LETRA_D); // Primero d
                            pila.push(Sym.PILA_C);  // Luego C
                            pila.push(Sym.LETRA_B); // Luego b (token)
                        } else {
                            return false; // Error sintáctico
                        }
                        break;

                    case Sym.PILA_C: // C → c | λ
                        if (tokenActual.getLexema() == Sym.LETRA_C) {
                            pila.pop(); // Sacar C
                            pila.push(Sym.LETRA_C); // Poner c (token)
                        } else {
                            pila.pop(); // C → λ (epsilon)
                        }
                        break;

                    default:
                        return false; // Símbolo inesperado en la pila
                }
            }
        }

        // Aceptar si la pila está vacía (solo quedó el ; de fondo) y llegamos al ;
        aceptado = pila.isEmpty() ||
                (pila.size() == 1 && pila.peek() == Sym.PUNTOCOMA &&
                        tokenActual.getLexema() == Sym.PUNTOCOMA);
        return aceptado;
    }

    private String representarPila() {
        StringBuilder sb = new StringBuilder();
        for (int simbolo : pila) {
            switch (simbolo) {
                case Sym.PILA_S: sb.append("S"); break;
                case Sym.PILA_A: sb.append("A"); break;
                case Sym.PILA_B: sb.append("B"); break;
                case Sym.PILA_C: sb.append("C"); break;
                case Sym.PILA_D: sb.append("D"); break;
                case Sym.PUNTOCOMA: sb.append(";"); break;
                default: sb.append((char) simbolo);
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
            e.printStackTrace();
        }
    }
}