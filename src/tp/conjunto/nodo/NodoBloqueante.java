package tp.conjunto.nodo;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class NodoBloqueante<T>{

    protected NodoBloqueante<T> sucesor;
    protected final Lock lock = new ReentrantLock(true);
    
    public void bloquear(){lock.lock();}
    public void desbloquear(){lock.unlock();}
    
    public abstract Optional<T> elemento();
    
    public NodoBloqueante<T> sucesor(){return sucesor;}
    public void nuevoSucesor(NodoBloqueante<T> nuevoSucesor){this.sucesor = nuevoSucesor;}
}

