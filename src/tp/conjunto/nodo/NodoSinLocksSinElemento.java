package tp.conjunto.nodo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class NodoSinLocksSinElemento<T> extends NodoSinLocks<T>{

    public NodoSinLocksSinElemento(){}
    public NodoSinLocksSinElemento(AtomicMarkableReference<NodoSinLocksSinElemento<T>> sucesor){
    	this.sucesor.set(sucesor.getReference(), false);
    }

    @Override public Optional<T> elemento(){return Optional.empty();}
    @Override public int sumaElemento(){return 0;}
}
