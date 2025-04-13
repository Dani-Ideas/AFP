package fes.aragon.codigo;

import java.io.IOException;

public class SiguienteToken {
    private final Lexico analizador;
    private Tokens tokenActual;
    private boolean error = false;

    public SiguienteToken(Lexico analizador) {
        this.analizador = analizador;
    }

    public void siguiente() {
        try {
            tokenActual = analizador.yylex();
            if (tokenActual == null) {
                tokenActual = new Tokens("EOF", Sym.EOF, 0, 0);
                throw new IOException("Fin Archivo");
            }
            System.out.print("🔍 Token leído: " + tokenActual.getLexema() + " (" + tokenActual.getToken() + ")   ");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Tokens getTokenActual() {
        return tokenActual;
    }

    public void setError(String mensaje) {
        error = true;
        System.out.println(mensaje);
    }

    public boolean hayError() {
        return error;
    }
}
