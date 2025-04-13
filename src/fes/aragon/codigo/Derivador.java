package fes.aragon.codigo;

import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class Derivador {
    private final Stack<String> pila;
    private final Map<String, Function<Tokens, String>> producciones;
    private final SiguienteToken tokenHandler;

    public Derivador(Stack<String> pila, Map<String, Function<Tokens, String>> producciones, SiguienteToken tokenHandler) {
        this.pila = pila;
        this.producciones = producciones;
        this.tokenHandler = tokenHandler;
    }

    public void analizar() {
        while (!pila.isEmpty() && !tokenHandler.hayError()) {
            String cima = pila.peek();

            if (producciones.containsKey(cima)) {
                pila.pop();
                String resultado = producciones.get(cima).apply(tokenHandler.getTokenActual());
                for (int i = resultado.length() - 1; i >= 0; i--) {
                    pila.push(String.valueOf(resultado.charAt(i)));
                }

            } else if (cima.equals(";")) {
                if (tokenHandler.getTokenActual().getLexema() == Sym.PUNTOCOMA) {
                    pila.pop();
                    System.out.println("✔️ Punto y coma encontrado.");
                    tokenHandler.siguiente();
                } else {
                    tokenHandler.setError("❌ Error: Se esperaba ';'");
                }

            } else {
                if (cima.equals(tokenHandler.getTokenActual().getLexemaNombre())) {
                    pila.pop();
                    tokenHandler.siguiente();
                } else {
                    tokenHandler.setError("❌ Error: Se esperaba '" + cima + "'");
                }
            }
        }
    }
}
