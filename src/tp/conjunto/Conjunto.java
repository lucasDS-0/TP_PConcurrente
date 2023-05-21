package tp.conjunto;

import java.util.Comparator;

/** Interfaz de un Conjunto.*/
public interface Conjunto<T>{
	public boolean agregar(T elemento);
	public boolean remover(T elemento);
	
	public interface Factory<T>{
        public Conjunto<T> armarConjuntoVacio(Comparator<T> comparator);
	}
}
