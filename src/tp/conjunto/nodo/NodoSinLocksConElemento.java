package tp.conjunto.nodo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class NodoSinLocksConElemento<T> extends NodoSinLocks<T>{
    private T elemento;
    
    public NodoSinLocksConElemento(T elemento,AtomicMarkableReference<NodoSinLocks<T>> sucesor){
        this.elemento = elemento;
        this.sucesor.set(sucesor.getReference(), false);
    }

    @Override public Optional<T> elemento(){return Optional.of(elemento);}
}
