package tp;

import tp.conjunto.*;
import tp.hilos.*;

import java.util.Comparator;

public class Main{

    private static int cantidadDeHilos = 60;
    private static int cantidadMaxDeOperaciones = 300;
    private static int cantidadDeOperacionesPorHilo = cantidadMaxDeOperaciones/cantidadDeHilos;
        
	public static void main (String[] args){
   	    System.out.println("Hola");
        Comparator<String> comparador = Comparator.comparing(String::toString);
        //_manipularParaProbar(new ConjuntoSinConcurrencia.Factory<String>(),comparador);
        //_manipularParaProbar(new ConjuntoGranularidadGruesa.Factory<String>(),comparador);
        //_manipularParaProbar(new ConjuntoGranularidadFina.Factory<String>(),comparador);
        //_manipularParaProbar(new ConjuntoSincronizacionOptimista.Factory<String>(),comparador);
        //_manipularParaProbar(new ConjuntoSinLocks.Factory<String>(),comparador);
		System.out.println("Chau");
		
        Conjunto.Factory<String> f = new ConjuntoGranularidadFina.Factory<String>();
		
		int cantidadAgregar = cantidadDeHilos/3;
		int cantidadContiene = cantidadDeHilos/3;
		int cantidadRemover = cantidadDeHilos/3;
		
		System.out.println("CreacionHilos::INI");
	    Conjunto<String> c = f.armarConjuntoVacio(comparador);
		
		for(int i = 0; i < cantidadAgregar; i++) {
		    new HiloAgregar<String>(c, "A", cantidadDeOperacionesPorHilo).start();
		}
		
		for(int i = 0; i < cantidadContiene; i++) {
		    new HiloContiene<String>(c, "A", cantidadDeOperacionesPorHilo).start();
		}
		
		for(int i = 0; i < cantidadRemover; i++) {
		    new HiloRemover<String>(c, "A", cantidadDeOperacionesPorHilo).start();
		}
		
		System.out.println("CreacionHilos::FIN");
	}
	
	
	
	
	private static void _manipularParaProbar(Conjunto.Factory<String> f,Comparator<String> comparador){
	   	System.out.println("manipularParaProbar::INI");
	    Conjunto<String> c = f.armarConjuntoVacio(comparador);
	    System.out.println("manipularParaProbar::c.class: " + c.getClass().getName());
		c.agregar("C"); System.out.println(c);    // C
	    c.agregar("B"); System.out.println(c);    // B,C
   	    c.agregar("A"); System.out.println(c);    // A,B,C
   	    c.agregar("A"); System.out.println(c);    // A,B,C
	    c.remover("B"); System.out.println(c);    // A,C
	    c.agregar("D"); System.out.println(c);    // A,C,D
	    c.agregar("E"); System.out.println(c);    // A,C,D,E
	    c.remover("A"); System.out.println(c);    // C,D,E
	    c.remover("X"); System.out.println(c);    // C,D,E
	    c.remover("E"); System.out.println(c);    // C,D
	    System.out.println(c.contiene("A"));      // false
	    System.out.println(c.contiene("B"));      // false
	    System.out.println(c.contiene("C"));      // true
	    System.out.println(c.contiene("D"));      // true
	    System.out.println(c.contiene("E"));      // false
	    System.out.println(c.contiene("X"));      // false
	    System.out.println("manipularParaProbar::FIN");
	}


}
