package tp.conjunto;

import tp.conjunto.nodo.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class ConjuntoSinLocks<T> implements Conjunto<T>{
    
   	private AtomicMarkableReference<NodoSinLocksSinElemento<T>> marcaDeInicio = 
    new AtomicMarkableReference<NodoSinLocksSinElemento<T>>(new NodoSinLocksSinElemento<T>(
        new AtomicMarkableReference<NodoSinLocksSinElemento<T>>(new NodoSinLocksSinElemento<T>(), false)), false);

    private Comparator<T> comparator;
    
    public static class Factory<T> implements Conjunto.Factory<T>{
        @Override public Conjunto<T> armarConjuntoVacio(Comparator<T> comparator){
            return new ConjuntoSinLocks<T>(comparator);
        }
    }

    private ConjuntoSinLocks(Comparator<T> comparator){
        this.comparator = comparator;
    }
	
    @Override public String toString(){
        StringBuilder sb = new StringBuilder();
        NodoSinLocks<T> nodoAct = this.marcaDeInicio.getReference();
        NodoSinLocks<T> nodoSuc = nodoAct.sucesor().getReference();
        while(nodoSuc.elemento().isPresent()){
            sb.append(nodoSuc.elemento().get());
            nodoAct = nodoSuc;
            nodoSuc = nodoAct.sucesor().getReference();
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

    public Pair<NodoSinLocks<T>> find(T elemento){
        NodoSinLocks<T> nodoAnt;
        NodoSinLocks<T> nodoAct;
        NodoSinLocks<T> nodoSuc;

        boolean[] marked = {false};
        boolean snip = false;
    
        while(true){
            nodoAnt = this.marcaDeInicio.getReference();
            nodoAct = nodoAnt.sucesor().getReference();
            while(true){
                nodoSuc = nodoAct.sucesor().get(marked);
                while(marked[0]){
                    snip = nodoAnt.sucesor().compareAndSet(nodoAct, nodoSuc, false, false);
                    if (snip) {
                        nodoAct = nodoSuc;
                        nodoSuc = nodoAct.sucesor().get(marked);
                    }
                }
                if (snip){
                    if (comparator.compare(nodoAct.elemento().get(), elemento)>=0){
                        Pair<NodoSinLocks<T>> res = new Pair<NodoSinLocks<T>>(nodoAnt, nodoAct);
                        return res;
                    }
                    nodoAnt = nodoAct;
                    nodoAct = nodoSuc;
                }
            }
        }

    }

	@Override public boolean agregar(T elementoNuevo){
        while(true){
            Pair<NodoSinLocks<T>> par = find(elementoNuevo);
            NodoSinLocks<T> nodoAnt = par.getFst();
            NodoSinLocks<T> nodoAct = par.getSnd();
            if (comparator.compare(nodoAct.elemento().get(), elementoNuevo)==0) {
                return false;
            }else{
                NodoSinLocksConElemento<T> nodoNuevo = 
                new NodoSinLocksConElemento<T>(elementoNuevo, new AtomicMarkableReference<NodoSinLocks<T>>(nodoAct,false));
                if (nodoAnt.sucesor().compareAndSet(nodoAct, nodoNuevo, false, false))
                    return true;
            }
        }
    }
    
	
	@Override public boolean remover(T elementoARemover){
	    boolean snip;
        while(true){
            Pair<NodoSinLocks<T>> par = find(elementoARemover);
            NodoSinLocks<T> nodoAnt = par.getFst();
            NodoSinLocks<T> nodoAct = par.getSnd();
            if (comparator.compare(nodoAct.elemento().get(), elementoARemover)!=0) {
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

}
