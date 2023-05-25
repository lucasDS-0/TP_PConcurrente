package tp.conjunto.nodo;

import java.util.Optional;

public class NodoConElemento<T> extends Nodo<T>{
    private T elemento;
    
    public NodoConElemento(T elemento,Optional<Nodo<T>> sucesor){
        this.elemento = elemento;
        this.sucesor = sucesor;
    }

    @Override public Optional<T> elemento(){return Optional.of(elemento);}
}

