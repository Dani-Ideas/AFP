package fes.aragon.codigo;

public class ArbolSintactico<E> {
    protected Nodo<E> raiz;
    public ArbolSintactico() {
        raiz = null;
    }
    public Nodo<E> getRaiz() {
        return raiz;
    }
    public void insertar(E dato) {
        Nodo<E> n = raiz;
        Nodo<E> previo = null;
        while (n != null) {
            previo = n;
            if (n.mayor(dato)) {
                n = n.derecho;
            } else {
                n = n.izquierdo;
            }
        }
        if (raiz == null) {
            raiz = new Nodo<E>(dato, "raiz");
        } else if (previo.mayor(dato)) {
            previo.derecho = new Nodo<E>(dato, "derecho,padre=" + previo.dato);
        } else {
            previo.izquierdo = new Nodo<E>(dato, "izquierdo,padre=" + previo.dato);
        }
    }
    public void imprimir(Nodo<E> n) {
        System.out.println(n.dato + " " + n.etiqueta);
    }
    public void preorden(Nodo<E> n) {
        if (n != null) {
            imprimir(n);
            preorden(n.izquierdo);
            preorden(n.derecho);
        }
    }
    public void postorden(Nodo<E> n) {
        if (n != null) {
            postorden(n.izquierdo);
            postorden(n.derecho);
            imprimir(n);
        }
    }
}
