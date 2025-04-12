package fes.aragon.codigo;
import fes.aragon.codigo.Sym;
import fes.aragon.codigo.Tokens;
import java.util.Stack;

public class AutomataPila {
    private Stack<Integer> pila = new Stack<>();
    private Tokens tokenActual;
    private Lexico analizador;
    private boolean aceptado = false;

    public AutomataPila(Lexico analizador) {
        this.analizador = analizador;
        // Inicializar pila con símbolo inicial
        pila.push(Sym.PUNTOCOMA); // Símbolo de fondo
        pila.push(Sym.LETRA_S);    // Símbolo inicial (definiría Sym.LETRA_S como constante)
    }

    public boolean analizar() {
        siguienteToken();
        while (!pila.isEmpty() && tokenActual != null) {
            int topePila = pila.peek();

            System.out.println("Pila: " + pila + " | Entrada: " + tokenActual.getToken());

            if (topePila == tokenActual.getLexema()) {
                // Coincidencia, sacar de pila y avanzar
                pila.pop();
                siguienteToken();
            } else {
                // Aplicar reglas de producción
                switch (topePila) {
                    case Sym.LETRA_S:
                        if (tokenActual.getLexema() == Sym.LETRA_A ||
                                tokenActual.getLexema() == Sym.LETRA_B) {
                            pila.pop();
                            pila.push(Sym.LETRA_B);
                            pila.push(Sym.LETRA_A);
                        } else {
                            return false;
                        }
                        break;

                    case Sym.LETRA_A:
                        if (tokenActual.getLexema() == Sym.LETRA_A) {
                            pila.pop();
                            pila.push(Sym.LETRA_A);
                        } else {
                            pila.pop(); // A → λ
                        }
                        break;

                    case Sym.LETRA_B:
                        if (tokenActual.getLexema() == Sym.LETRA_B) {
                            pila.pop();
                            pila.push(Sym.LETRA_D);
                            pila.push(Sym.LETRA_C);
                            pila.push(Sym.LETRA_B);
                        } else {
                            return false;
                        }
                        break;

                    case Sym.LETRA_C:
                        if (tokenActual.getLexema() == Sym.LETRA_C) {
                            pila.pop();
                            pila.push(Sym.LETRA_C);
                        } else {
                            pila.pop(); // C → λ
                        }
                        break;

                    default:
                        return false;
                }
            }
        }

        aceptado = pila.isEmpty() && tokenActual.getLexema() == Sym.PUNTOCOMA;
        return aceptado;
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