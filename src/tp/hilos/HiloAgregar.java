package tp.hilos;

import tp.conjunto.*;

public class HiloAgregar<T> extends Thread {
    private T elemento;
    private int operaciones;
    private Conjunto<T> c;

    public HiloAgregar(Conjunto<T> conjunto, T elemento, int n) {
        this.elemento = elemento;
        this.operaciones = n;
        this.c = conjunto;
    }

    public void run() {
        while (operaciones > 0){
            c.agregar(elemento);
            operaciones--;
        }
    }
  
}
