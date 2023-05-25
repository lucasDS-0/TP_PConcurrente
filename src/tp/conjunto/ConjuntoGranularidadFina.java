package tp.conjunto;

import tp.conjunto.nodo.*;

import java.util.Comparator;
import java.util.Optional;

public class ConjuntoGranularidadFina<T> implements Conjunto<T>{
    
   	private NodoBloqueanteSinElemento<T> marcaDeInicio = new NodoBloqueanteSinElemento<T>(new NodoBloqueanteSinElemento<T>());
    private Comparator<T> comparator;
    
    public static class Factory<T> implements Conjunto.Factory<T>{
        @Override public Conjunto<T> armarConjuntoVacio(Comparator<T> comparator){
            return new ConjuntoGranularidadFina<T>(comparator);
        }
    }

    private ConjuntoGranularidadFina(Comparator<T> comparator){
        this.comparator = comparator;
    }
	
    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        NodoBloqueante<T> nodo = this.marcaDeInicio;
        NodoBloqueante<T> nodoSuc = nodo.sucesor();
        while(nodoSuc.elemento().isPresent()){
            sb.append(nodoSuc.elemento().get());
            nodo = nodoSuc;
            nodoSuc = nodo.sucesor();
        }
        return sb.toString();
    }

	@Override public boolean agregar(T elementoNuevo){
        NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
        nodoAnt.bloquear();
        try{
            NodoBloqueante<T> nodoSuc = nodoAnt.sucesor();
            nodoSuc.bloquear();
            try{
                while(true){
                    if(!nodoSuc.elemento().isPresent()) break;
                    T elementoSuc = nodoSuc.elemento().get();
                    if(comparator.compare(elementoSuc,elementoNuevo)>0) break;
                    else if(comparator.compare(elementoSuc,elementoNuevo)==0) return false;
                    
                    nodoAnt.desbloquear();
                    nodoAnt = nodoSuc;
                    nodoSuc = nodoAnt.sucesor();        
                    nodoSuc.bloquear();
                }
            }finally{nodoSuc.desbloquear();}            
            _intercalarElemento(elementoNuevo,nodoAnt,nodoSuc);
            return true;
        }finally{nodoAnt.desbloquear();}	    
	}

	private void _intercalarElemento(T elemento, NodoBloqueante<T> despuesDe,NodoBloqueante<T> antesDe){
	    despuesDe.nuevoSucesor(new NodoBloqueanteConElemento<T>(elemento,antesDe));
	}
	
	@Override public boolean remover(T elementoARemover){
        NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
        nodoAnt.bloquear();
        try{
            NodoBloqueante<T> nodo = nodoAnt.sucesor();
            nodo.bloquear();
            try{
                while(true){
                    if(!nodo.elemento().isPresent()) return false;
                    T elemento = nodo.elemento().get();
                    if(comparator.compare(elemento,elementoARemover)>0)return false;
                    if(comparator.compare(elemento,elementoARemover)==0)
                        {nodoAnt.nuevoSucesor(nodo.sucesor()); return true;}
                    nodoAnt.desbloquear();
                    nodoAnt = nodo;
                    nodo = nodoAnt.sucesor();        
                    nodo.bloquear();
                }
            }finally{nodo.desbloquear();}            
        }finally{nodoAnt.desbloquear();}	    
	}

}
