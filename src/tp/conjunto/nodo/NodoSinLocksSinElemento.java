package tp.conjunto.nodo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class NodoSinLocksSinElemento<T> extends NodoSinLocks<T>{

    public NodoSinLocksSinElemento(){}
    public NodoSinLocksSinElemento(AtomicMarkableReference<NodoSinLocks<T>> sucesor){
    	this.sucesor = sucesor;
    }

    @Override public Optional<T> elemento(){return Optional.empty();}
}
