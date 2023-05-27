package tp.conjunto;

import tp.conjunto.nodo.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ConjuntoSinLocks<T> implements Conjunto<T>{
    
   	private NodoSinLocks<T> marcaDeInicio = new NodoSinLocks<T>(Integer.MIN_VALUE);
    private NodoSinLocks<T> marcaDeFin = new NodoSinLocks<T>(Integer.MAX_VALUE);

    

    private Comparator<T> comparator;
    
    public static class Factory<T> implements Conjunto.Factory<T>{
        @Override public Conjunto<T> armarConjuntoVacio(Comparator<T> comparator){
            return new ConjuntoSinLocks<T>(comparator);
        }
    }

    private ConjuntoSinLocks(Comparator<T> comparator){
        marcaDeInicio.sucesor = new AtomicMarkableReference<NodoSinLocks<T>>(marcaDeFin, false);
        this.comparator = comparator;
    }
	
    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        NodoSinLocks<T> nodoAnt = this.marcaDeInicio;
        NodoSinLocks<T> nodoAct = nodoAnt.sucesor().getReference();
        while(nodoAct.elemento() != null){
            sb.append(nodoAct.elemento());
            nodoAnt = nodoAct;
            nodoAct = nodoAnt.sucesor().getReference();
        }
        return sb.toString();
    }

    public class Pair<T>{
        private T fst;
        private T snd;

        private Pair(T fst, T snd) {
            this.fst = fst;
            this.snd = snd;
        }

        T getFst(){return this.fst;}
        T getSnd(){return this.snd;}
    }

    public Pair<NodoSinLocks<T>> find(T elemento, int key){
        NodoSinLocks<T> nodoAnt = null;
        NodoSinLocks<T> nodoAct = null;
        NodoSinLocks<T> nodoSuc = null;

        boolean[] marked = {false};
        boolean snip = false;
    
        retry: while(true){
            nodoAnt = this.marcaDeInicio;
            nodoAct = nodoAnt.sucesor().getReference();
            while(true){
                if (nodoAct.key() != Integer.MAX_VALUE) {
                    nodoSuc = nodoAct.sucesor().get(marked);
                    while(marked[0]){
                        snip = nodoAnt.sucesor().compareAndSet(nodoAct, nodoSuc, false, false);
                        if (!snip) continue retry;
                        nodoAct = nodoSuc;
                        nodoSuc = nodoAct.sucesor().get(marked);
                    }
                }
                if (nodoAct.key() >= key){
                    Pair<NodoSinLocks<T>> res = new Pair<NodoSinLocks<T>>(nodoAnt, nodoAct);
                    return res;
                }
                nodoAnt = nodoAct;
                nodoAct = nodoSuc;
            }
        }

    }

	@Override public boolean agregar(T elementoNuevo){
        int key = elementoNuevo.hashCode();
        while(true){
            Pair<NodoSinLocks<T>> par = find(elementoNuevo, key);
            NodoSinLocks<T> nodoAnt = par.getFst();
            NodoSinLocks<T> nodoAct = par.getSnd();
            if (nodoAct.key() == key) {
                return false;
            }else{
                NodoSinLocks<T> nodoNuevo = new NodoSinLocks<T>(key, elementoNuevo);
                nodoNuevo.sucesor = new AtomicMarkableReference<NodoSinLocks<T>>(nodoAct, false);
                if (nodoAnt.sucesor().compareAndSet(nodoAct, nodoNuevo, false, false))
                    return true;
            }
        }
    }
    
	
	@Override public boolean remover(T elementoARemover){
        int key = elementoARemover.hashCode();
	    boolean snip;
        while(true){
            Pair<NodoSinLocks<T>> par = find(elementoARemover, key);
            NodoSinLocks<T> nodoAnt = par.getFst();
            NodoSinLocks<T> nodoAct = par.getSnd();
            if (nodoAct.key() != key) {
                return false;
            }else{
                NodoSinLocks<T> nodoSuc = nodoAct.sucesor().getReference();
                snip = nodoAct.sucesor().attemptMark(nodoSuc, true);
                if (!snip) {continue;}
                nodoAnt.sucesor().compareAndSet(nodoAct, nodoSuc, false, false);
                return true;
            }   
        }
	}

    @Override public boolean contiene(T elementoAVerificar){
        boolean[] marked = {false};
        int key = elementoAVerificar.hashCode();
        NodoSinLocks<T> nodoAct = this.marcaDeInicio;
        while(nodoAct.key() < key){
            nodoAct = nodoAct.sucesor().getReference();
            if(nodoAct.key() == Integer.MAX_VALUE) break;
            NodoSinLocks<T> nodoSuc = nodoAct.sucesor().get(marked);
        }
        return (nodoAct.key() == key && !marked[0]);
    }

}
