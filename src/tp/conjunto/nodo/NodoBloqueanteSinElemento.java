package tp.conjunto.nodo;

import java.util.Optional;
  
public class NodoBloqueanteSinElemento<T> extends NodoBloqueante<T>{

    public NodoBloqueanteSinElemento(){}
    public NodoBloqueanteSinElemento(NodoBloqueante<T> sucesor){this.sucesor = sucesor;}
    @Override public Optional<T>elemento(){return Optional.empty();}
    @Override public int sumaElemento(){return 0;}
}
