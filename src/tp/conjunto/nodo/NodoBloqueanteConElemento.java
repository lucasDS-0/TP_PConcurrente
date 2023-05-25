package tp.conjunto.nodo;

import java.util.Optional;

public class NodoBloqueanteConElemento<T> extends NodoBloqueante<T>{
    private T elemento;
    
    public NodoBloqueanteConElemento(T elemento,NodoBloqueante<T> sucesor){
        this.elemento = elemento;
        this.sucesor = sucesor;
    }

    @Override public Optional<T> elemento(){return Optional.of(elemento);}
}

