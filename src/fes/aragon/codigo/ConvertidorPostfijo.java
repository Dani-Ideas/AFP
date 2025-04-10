package fes.aragon.codigo;
import java.util.Stack;


public class ConvertidorPostfijo {

    public String convertir(String expresion) {
        // Depurar la expresión
        String expr = depurar(expresion);
        String[] arrayInfix = expr.split(" ");

        Stack<String> E = new Stack<>();
        Stack<String> P = new Stack<>();
        Stack<String> S = new Stack<>();

        for (int i = arrayInfix.length - 1; i >= 0; i--) {
            E.push(arrayInfix[i]);
        }

        try {
            while (!E.isEmpty()) {
                switch (pref(E.peek())) {
                    case 1:
                        P.push(E.pop());
                        break;
                    case 3:
                    case 4:
                        while (!P.isEmpty() && pref(P.peek()) >= pref(E.peek())) {
                            S.push(P.pop());
                        }
                        P.push(E.pop());
                        break;
                    case 2:
                        while (!P.peek().equals("(")) {
                            S.push(P.pop());
                        }
                        P.pop();
                        E.pop();
                        break;
                    default:
                        S.push(E.pop());
                }
            }

            // Formatear resultado
            String postfix = S.toString().replaceAll("[\\]\\[,]", "");
            return postfix.trim();

        } catch (Exception ex) {
            return "Error en la expresión: " + ex.getMessage();
        }
    }

    private String depurar(String s) {
        s = s.replaceAll("\\s+", ""); // Elimina espacios
        s = "(" + s + ")";
        String simbols = "+-*/()";
        String str = "";

        for (int i = 0; i < s.length(); i++) {
            if (simbols.contains("" + s.charAt(i))) {
                str += " " + s.charAt(i) + " ";
            } else {
                str += s.charAt(i);
            }
        }
        return str.replaceAll("\\s+", " ").trim();
    }

    private int pref(String op) {
        int prf = 99;
        if (op.equals("^")) prf = 5;
        if (op.equals("*") || op.equals("/")) prf = 4;
        if (op.equals("+") || op.equals("-")) prf = 3;
        if (op.equals(")")) prf = 2;
        if (op.equals("(")) prf = 1;
        return prf;
    }
}
