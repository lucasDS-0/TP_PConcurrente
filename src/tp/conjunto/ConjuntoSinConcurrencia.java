package tp.conjunto;

import tp.conjunto.nodo.*;

import java.util.Comparator;
import java.util.Optional;

public class ConjuntoSinConcurrencia<T> implements Conjunto<T>{
    
   	private NodoSinElemento<T> marcaDeInicio = new NodoSinElemento<T>();
    private Comparator<T> comparator;
    
    public static class Factory<T> implements Conjunto.Factory<T>{
        @Override public Conjunto<T> armarConjuntoVacio(Comparator<T> comparator){
            return new ConjuntoSinConcurrencia<T>(comparator);
        }
    }

    private ConjuntoSinConcurrencia(Comparator<T> comparator){
        this.comparator = comparator;
    }
	
    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        Nodo<T> nodo = this.marcaDeInicio;
        while(nodo.sucesor().isPresent()){
            nodo = nodo.sucesor().get();
            sb.append(nodo.elemento().get());
        }
        return sb.toString();
    }

	@Override public boolean agregar(T elementoNuevo){
        Nodo<T> nodoAnt = this.marcaDeInicio;
        Optional<Nodo<T>> nodoSucPosible = nodoAnt.sucesor();
        while(nodoSucPosible.isPresent()){
            Nodo<T> nodoSuc = nodoSucPosible.get();
            T elementoSuc = nodoSuc.elemento().get();
            if(comparator.compare(elementoSuc,elementoNuevo)>0) break;
            else if(comparator.compare(elementoSuc,elementoNuevo)==0) return false;
            nodoAnt = nodoSuc;
            nodoSucPosible = nodoAnt.sucesor();        
        }
        nodoAnt.nuevoSucesor(Optional.of(new NodoConElemento<T>(elementoNuevo,nodoSucPosible)));
	    return true;
	}
	
	@Override public boolean remover(T elemento){
	    Nodo<T> nodoAnt = this.marcaDeInicio;
        Optional<Nodo<T>> nodoPosible = nodoAnt.sucesor();
        while(nodoPosible.isPresent()){
            Nodo<T> nodo = nodoPosible.get();
            Optional<T> elementoPosible = nodo.elemento();
            if(elementoPosible.isPresent() && comparator.compare(elementoPosible.get(),elemento)==0){
                nodoAnt.nuevoSucesor(nodo.sucesor());
                return true;
            }
            nodoAnt = nodo;
            nodoPosible = nodoAnt.sucesor();        
        }
	    return false;
	}

    @Override public boolean contiene(T elementoAVerificar){
        return false;
    }

}
