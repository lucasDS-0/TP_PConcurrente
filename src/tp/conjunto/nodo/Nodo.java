package tp.conjunto.nodo;

import java.util.Optional;

public abstract class Nodo<T>{
    protected Optional<Nodo<T>> sucesor;
    
    public abstract Optional<T> elemento();
    
    public Optional<Nodo<T>> sucesor(){return sucesor;}
    public void nuevoSucesor(Optional<Nodo<T>> nuevoSucesor){this.sucesor = nuevoSucesor;}
}

