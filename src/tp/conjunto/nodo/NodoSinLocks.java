package tp.conjunto.nodo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicMarkableReference;

public abstract class NodoSinLocks<T>{

    protected AtomicMarkableReference<NodoSinLocks<T>> sucesor;
        
    public abstract Optional<T> elemento();
    
    public AtomicMarkableReference<NodoSinLocks<T>> sucesor(){return sucesor;}
    public void nuevoSucesor(AtomicMarkableReference<NodoSinLocks<T>> nuevoSucesor){
        this.sucesor.set(nuevoSucesor.getReference(), false);
    }
}
