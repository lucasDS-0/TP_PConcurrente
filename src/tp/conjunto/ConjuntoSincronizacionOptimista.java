package tp.conjunto;

import tp.conjunto.nodo.*;

import java.util.Comparator;
import java.util.Optional;

public class ConjuntoSincronizacionOptimista<T> implements Conjunto<T>{
    
   	private NodoBloqueanteSinElemento<T> marcaDeInicio = new NodoBloqueanteSinElemento<T>(new NodoBloqueanteSinElemento<T>());
    private Comparator<T> comparator;
    
    public static class Factory<T> implements Conjunto.Factory<T>{
        @Override public Conjunto<T> armarConjuntoVacio(Comparator<T> comparator){
            return new ConjuntoSincronizacionOptimista<T>(comparator);
        }
    }

    private ConjuntoSincronizacionOptimista(Comparator<T> comparator){
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
        while(true){
            NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
            NodoBloqueante<T> nodoSuc = nodoAnt.sucesor();
            while(true){
                if(!nodoSuc.elemento().isPresent()) break;
                T elementoSuc = nodoSuc.elemento().get();
                if(comparator.compare(elementoSuc,elementoNuevo)>0) break;
                else if(comparator.compare(elementoSuc,elementoNuevo)==0) return false;

                nodoAnt = nodoSuc;
                nodoSuc = nodoAnt.sucesor();        
            }
            nodoAnt.bloquear();
            nodoSuc.bloquear();
            try{
                if(_validarAdyacencia(nodoAnt,nodoSuc))
                    {_intercalarElemento(elementoNuevo,nodoAnt,nodoSuc); return true;}
                else continue;
            }finally{
                nodoAnt.desbloquear();
                nodoSuc.desbloquear();
            }
        }
    }
    
    private boolean _validarAdyacencia(NodoBloqueante<T> nodoPre,NodoBloqueante<T> nodoSuc){ 
        NodoBloqueante<T> nodoAux = this.marcaDeInicio;        
        while(true){
            if(nodoAux.sucesor()==null) return false;
            if(nodoAux==nodoPre) return nodoAux.sucesor()==nodoSuc;
            nodoAux = nodoAux.sucesor();        
        }
    }

	private void _intercalarElemento(T elemento, NodoBloqueante<T> despuesDe,NodoBloqueante<T> antesDe){
	    despuesDe.nuevoSucesor(new NodoBloqueanteConElemento<T>(elemento,antesDe));
	}
	
	@Override public boolean remover(T elementoARemover){
	    while(true){
            NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
            NodoBloqueante<T> nodoSuc = nodoAnt.sucesor();
            while(true){
                if(!nodoSuc.elemento().isPresent()) return false;
                T elementoSuc = nodoSuc.elemento().get();
                if(comparator.compare(elementoSuc,elementoARemover)>0) return false;
                else if(comparator.compare(elementoSuc,elementoARemover)==0) break;

                nodoAnt = nodoSuc;
                nodoSuc = nodoAnt.sucesor();        
            }
            nodoAnt.bloquear();
            nodoSuc.bloquear();
            try{
                if(_validarAdyacencia(nodoAnt,nodoSuc))
                    {nodoAnt.nuevoSucesor(nodoSuc.sucesor()); return true;}
                else continue;
            }finally{
                nodoAnt.desbloquear();
                nodoSuc.desbloquear();
            }
        }
	}

}
