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
        NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
        nodoAnt.bloquear();
        try{
            NodoBloqueante<T> nodoAct = nodoAnt.sucesor();
            nodoAct.bloquear();
            try{
                while(true){
                    if(!nodoAct.elemento().isPresent()) break;
                    T elementoAct = nodoAct.elemento().get();
                    if(comparator.compare(elementoAct,elementoNuevo)>0) break;
                    else if(comparator.compare(elementoAct,elementoNuevo)==0) return false;
                    
                    nodoAnt.desbloquear();
                    nodoAnt = nodoAct;
                    nodoAct = nodoAnt.sucesor();        
                    nodoAct.bloquear();
                }
            }finally{nodoAct.desbloquear();}            
            _intercalarElemento(elementoNuevo,nodoAnt,nodoAct);
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
            NodoBloqueante<T> nodoAct = nodoAnt.sucesor();
            nodoAct.bloquear();
            try{
                while(true){
                    if(!nodoAct.elemento().isPresent()) return false;
                    T elementoAct = nodoAct.elemento().get();
                    if(comparator.compare(elementoAct,elementoARemover)>0)return false;
                    if(comparator.compare(elementoAct,elementoARemover)==0)
                        {nodoAnt.nuevoSucesor(nodoAct.sucesor()); return true;}
                    nodoAnt.desbloquear();
                    nodoAnt = nodoAct;
                    nodoAct = nodoAnt.sucesor();        
                    nodoAct.bloquear();
                }
            }finally{nodoAct.desbloquear();}            
        }finally{nodoAnt.desbloquear();}	    
	}

    @Override public boolean contiene(T elementoAVerificar){
        NodoBloqueante<T> nodoAnt = this.marcaDeInicio;
        nodoAnt.bloquear();
        try{
            NodoBloqueante<T> nodoAct = nodoAnt.sucesor();
            nodoAct.bloquear();
            try{
                if(!nodoAct.elemento().isPresent()) return false;
                T elementoAct = nodoAct.elemento().get();
                while(comparator.compare(elementoAct,elementoAVerificar)<0){
                    nodoAnt.desbloquear();
                    nodoAnt = nodoAct;
                    nodoAct = nodoAnt.sucesor();        
                    nodoAct.bloquear();
                    if(!nodoAct.elemento().isPresent()) return false;
                    elementoAct = nodoAct.elemento().get();
                }
                return (comparator.compare(elementoAct,elementoAVerificar)==0);
            }finally{nodoAct.desbloquear();}
        }finally{nodoAnt.desbloquear();}
    }

}
