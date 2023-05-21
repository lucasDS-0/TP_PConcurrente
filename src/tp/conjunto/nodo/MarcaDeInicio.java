package tp.conjunto.nodo;

import java.util.Optional;
  
public class MarcaDeInicio<T> extends Nodo<T>{

    public MarcaDeInicio(){
        this.sucesor = Optional.empty();
    }
    @Override public Optional<T> elemento(){return Optional.empty();}
    @Override public int sumaElemento(){return 0;}
}
