package fes.aragon.codigo;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class Axiomas {
    private final Stack<String> pila;
    private final SiguienteToken tokenHandler;
    private final Map<String, Function<Tokens, String>> producciones = new HashMap<>();

    public Axiomas(Stack<String> pila, SiguienteToken tokenHandler) {
        this.pila = pila;
        this.tokenHandler = tokenHandler;
        inicializarProducciones();
    }

    public Map<String, Function<Tokens, String>> getProducciones() {
        return producciones;
    }
    /* Este es el axioma inicial, presupone que tiene punto y coma pero si se comprueva con el derivador
    * */
    public void S() {
        pila.push(";");
        pila.push("S");
        tokenHandler.siguiente();
    }

    private void inicializarProducciones() {
        producciones.put("A", t -> {
            if (t.getLexema() == Sym.A) {
                System.out.println("✅ Producción aplicada: A := a");
                return "a";
            } else if (t.getLexema() == Sym.B) {
                System.out.println("✅ Producción aplicada: A := λ (epsilon)");
                return "";
            } else {
                tokenHandler.setError("❌ Error en A: Se esperaba 'a' o 'b'");
                return "";
            }
        });

        producciones.put("B", t -> {
            if (t.getLexema() == Sym.B) {
                System.out.println("✅ Producción aplicada: B := bCd");
                return "bCd";
            } else {
                tokenHandler.setError("❌ Error en B: Se esperaba 'b'");
                return "";
            }
        });

        producciones.put("C", t -> {
            if (t.getLexema() == Sym.C) {
                System.out.println("✅ Producción aplicada: C := c");
                return "c";
            } else if (t.getLexema() == Sym.D) {
                System.out.println("✅ Producción aplicada: C := λ (epsilon)");
                return "";
            } else {
                tokenHandler.setError("❌ Error en C: Se esperaba 'c' o 'd'");
                return "";
            }
        });

        producciones.put("S", t -> {
            System.out.println("✅ Producción aplicada: S := AB");
            return "AB";
        });
    }
}
