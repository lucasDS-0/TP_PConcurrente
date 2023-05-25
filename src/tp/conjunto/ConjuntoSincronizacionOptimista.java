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
        NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
        NodoBloqueante<T> nodoAct = nodoAnt.sucesor();
        while(nodoAct.elemento().isPresent()){
            sb.append(nodoAct.elemento().get());
            nodoAnt = nodoAct;
            nodoAct = nodoAnt.sucesor();
        }
        return sb.toString();
    }

	@Override public boolean agregar(T elementoNuevo){
        while(true){
            NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
            NodoBloqueante<T> nodoAct = nodoAnt.sucesor();
            while(true){
                if(!nodoAct.elemento().isPresent()) break;
                T elementoAct = nodoAct.elemento().get();
                if(comparator.compare(elementoAct,elementoNuevo)>0) break;
                else if(comparator.compare(elementoAct,elementoNuevo)==0) return false;

                nodoAnt = nodoAct;
                nodoAct = nodoAnt.sucesor();        
            }
            nodoAnt.bloquear();
            nodoAct.bloquear();
            try{
                if(_validarAdyacencia(nodoAnt,nodoAct))
                    {_intercalarElemento(elementoNuevo,nodoAnt,nodoAct); return true;}
                else continue;
            }finally{
                nodoAnt.desbloquear();
                nodoAct.desbloquear();
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
            NodoBloqueante<T> nodoAct = nodoAnt.sucesor();
            while(true){
                if(!nodoAct.elemento().isPresent()) return false;
                T elementoAct = nodoAct.elemento().get();
                if(comparator.compare(elementoAct,elementoARemover)>0) return false;
                else if(comparator.compare(elementoAct,elementoARemover)==0) break;

                nodoAnt = nodoAct;
                nodoAct = nodoAnt.sucesor();        
            }
            nodoAnt.bloquear();
            nodoAct.bloquear();
            try{
                if(_validarAdyacencia(nodoAnt,nodoAct))
                    {nodoAnt.nuevoSucesor(nodoAct.sucesor()); return true;}
                else continue;
            }finally{
                nodoAnt.desbloquear();
                nodoAct.desbloquear();
            }
        }
	}

    @Override public boolean contiene(T elementoAVerificar){
        while(true){
            NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
            NodoBloqueante<T> nodoAct = nodoAnt.sucesor();
            T elementoAct = nodoAct.elemento().get();
            while(comparator.compare(elementoAct,elementoAVerificar)<0){
                nodoAnt = nodoAct;
                nodoAct = nodoAnt.sucesor();
                if(!nodoAct.elemento().isPresent()) return false;
                elementoAct = nodoAct.elemento().get();
            }
            try{
                nodoAnt.bloquear();
                nodoAct.bloquear();
                if(_validarAdyacencia(nodoAnt, nodoAct)){
                    elementoAct = nodoAct.elemento().get();
                    return (comparator.compare(elementoAct,elementoAVerificar)==0);
                }
            }finally{
                nodoAnt.desbloquear();
                nodoAct.desbloquear();
            }
        }
    }

}
