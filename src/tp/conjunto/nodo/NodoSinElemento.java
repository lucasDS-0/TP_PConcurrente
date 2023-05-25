package tp.conjunto.nodo;

import java.util.Optional;
  
public class NodoSinElemento<T> extends Nodo<T>{

    public NodoSinElemento(){
        this.sucesor = Optional.empty();
    }
    @Override public Optional<T> elemento(){return Optional.empty();}
}
