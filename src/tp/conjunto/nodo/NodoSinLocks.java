package tp.conjunto.nodo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class NodoSinLocks<T>{

    private int key;
    private T elemento = null;
    public AtomicMarkableReference<NodoSinLocks<T>> sucesor;
        
    public NodoSinLocks(int key){
        this.key = key;
    }

    public NodoSinLocks(int key, T elemento){
        this.key = key;
        this.elemento = elemento;
        this.sucesor = null;
    }

    public int key() {
        return this.key;
    }

    public T elemento(){
        return elemento;
    }
    
    public AtomicMarkableReference<NodoSinLocks<T>> sucesor(){
        return sucesor;
    }
}
