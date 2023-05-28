package tp;

import tp.conjunto.*;
import tp.hilos.*;

import java.io.PrintWriter;
import java.util.Comparator;

public class Main{
   
	public static void main (String[] args){
	    // Cantidad de Hilos Totales
	    int cHT; // cDH = cA + cR + cC
	    // Cantidad Operaciones Totales
        int cOT;;
        // Cantidad de Operaciones por Hilo
        int cOH;// = cOT/cHT;
        // Cantidad de hilos que Agregan
        int cA;
        // Cantidad de hilos que Remueven
        int cR;
        // Cantidad de hilos que revisan pertenencia (Contiene)
        int cC;

        Comparator<String> comparador = Comparator.comparing(String::toString);
		
		long start;
		long end;
		long lapseGF = 0;
		long lapseSO = 0;
		long lapseSL = 0;

        Conjunto.Factory<String> gf;
        Conjunto.Factory<String> so;
        Conjunto.Factory<String> sl;
		
        // Experimento 1
        
        gf = new ConjuntoGranularidadFina.Factory<String>();
        so = new ConjuntoSincronizacionOptimista.Factory<String>();
        sl = new ConjuntoSinLocks.Factory<String>();
        
        cHT = 10000;
        cOT = 10000;
        cOH = cOT/cHT;      
						
	    System.out.println("Experimento 1 :: INI");
	    
	    try{
	        
	        int[][] porcentajes = 
                {{20,20,60}, {20,30,50},
                 {20,40,40}, {20,50,30},
                 {20,60,20}, {20,70,10}}; // {cC, cA, cR}
	    
		    PrintWriter output1 = new PrintWriter("experimento_1.csv");
	        Conjunto<String> cgf = gf.armarConjuntoVacio(comparador);
	        Conjunto<String> cso = so.armarConjuntoVacio(comparador);
	        Conjunto<String> csl = sl.armarConjuntoVacio(comparador);	        	        
		    
		    output1.printf("GF, SO, SL, Hilos, Operaciones, Agregar, Contiene, Remover\n");
		    
		    for (int i = 0; i < porcentajes.length; i++) {
                cC = (cHT * porcentajes[i][0]) / 100;
                cA = (cHT * porcentajes[i][1]) / 100;
                cR = (cHT * porcentajes[i][2]) / 100;
                
                for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(cgf, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(cgf, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(cgf, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseGF += end - start;
		        }
		        
		        lapseGF = lapseGF/5;
		        
                for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(cso, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(cso, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(cso, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseSO += end - start;
		        }
		        
		        lapseSO = lapseSO/5;
		        
		        for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(csl, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(csl, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(csl, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseSL += end - start;
		        }
		        
		        lapseSL = lapseSL/5;
		        
                output1.printf("%d(ms), %d(ms), %d(ms), %d, %d, %d, %d, %d\n", 
                            lapseGF, lapseSO, lapseSL, cHT, cOT, cA, cC, cR);
            }
            output1.close();
		}catch(Exception e) {e.getStackTrace();}
        System.out.println("Experimento 1 :: FIN");
        
        // Experimento 2
        
        gf = new ConjuntoGranularidadFina.Factory<String>();
        so = new ConjuntoSincronizacionOptimista.Factory<String>();
        sl = new ConjuntoSinLocks.Factory<String>();
        
        cOT = 10000;
				
		lapseGF = 0;
		lapseSO = 0;
		lapseSL = 0;
		
	    System.out.println("Experimento 2 :: INI");
	    
	    try{
	    
	        int[] porcentajes = {20,40,40}; // {cC, cA, cR}
            int[] lcHT = {100,1000, 5000, 10000, 100000};
	    
		    PrintWriter output2 = new PrintWriter("experimento_2.csv");
	        Conjunto<String> cgf = gf.armarConjuntoVacio(comparador);
	        Conjunto<String> cso = so.armarConjuntoVacio(comparador);
	        Conjunto<String> csl = sl.armarConjuntoVacio(comparador);	        	        
		    
		    output2.printf("GF, SO, SL, Hilos, Operaciones, Agregar, Contiene, Remover\n");
		    
		    for (int i = 0; i < lcHT.length; i++) {
		        cHT = lcHT[i];
		        cOH = cOT/cHT;
                cC = (cHT * porcentajes[0]) / 100;
                cA = (cHT * porcentajes[1]) / 100;
                cR = (cHT * porcentajes[2]) / 100;
                
                for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(cgf, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(cgf, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(cgf, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseGF += end - start;
		        }
		        
		        lapseGF = lapseGF/5;
		        
		        for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(cso, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(cso, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(cso, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseSO += end - start;
		        }
		        
		        lapseSO = lapseSO/5;
		        
		        for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(csl, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(csl, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(csl, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseSL += end - start;
		        }
		        
		        lapseSL = lapseSL/5;
		        
		        output2.printf("%d(ms), %d(ms), %d(ms), %d, %d, %d, %d, %d\n", 
                            lapseGF, lapseSO, lapseSL, cHT, cOT, cA, cC, cR);
            }
            output2.close();
		}catch(Exception e) {e.getStackTrace();}
        System.out.println("Experimento 2 :: FIN");
        
        // Experimento 3
        gf = new ConjuntoGranularidadFina.Factory<String>();
        so = new ConjuntoSincronizacionOptimista.Factory<String>();
        sl = new ConjuntoSinLocks.Factory<String>();
        
        cOH = 10;     
				
		lapseGF = 0;
		lapseSO = 0;
		lapseSL = 0;
		
	    System.out.println("Experimento 3 :: INI");
	    
	    try{
	        
            int[] lcHT = {100, 500, 1000, 5000, 10000};
            int[] porcentajes = {20,40,40}; // {cC, cA, cR}   
	    
		    PrintWriter output3 = new PrintWriter("experimento_3.csv");
	        Conjunto<String> cgf = gf.armarConjuntoVacio(comparador);
	        Conjunto<String> cso = so.armarConjuntoVacio(comparador);
	        Conjunto<String> csl = sl.armarConjuntoVacio(comparador);	        	        
		    
		    output3.printf("GF, SO, SL, Hilos, Operaciones, Agregar, Contiene, Remover\n");
		    
		    for (int i = 0; i < lcHT.length; i++) {
		        cHT = lcHT[i];
		        cOT = cOH * cHT;
                cC = (cHT * porcentajes[0]) / 100;
                cA = (cHT * porcentajes[1]) / 100;
                cR = (cHT * porcentajes[2]) / 100;
                
                for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(cgf, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(cgf, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(cgf, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseGF += end - start;
		        }
		        
		        lapseGF = lapseGF/5;
		        
		        for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(cso, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(cso, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(cso, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseSO += end - start;
		        }
		        
		        lapseSO = lapseSO/5;
		        
		        for (int p = 0; p < 5; p++) { // promedio
           		    start = System.currentTimeMillis();
		            
		            for(int j = 0; j < cA; j++) {
		                new HiloAgregar<String>(csl, "A", cOH).start();
		            }
		            for(int j = 0; j < cC; j++) {
		                new HiloContiene<String>(csl, "A", cOH).start();
		            }
		            for(int j = 0; j < cR; j++) {
		                new HiloRemover<String>(csl, "A", cOH).start();
		            }
		            
		            end = System.currentTimeMillis();
		            lapseSL += end - start;
		        }
		        
		        lapseSL = lapseSL/5;
		        
		        output3.printf("%d(ms), %d(ms), %d(ms), %d, %d, %d, %d, %d\n", 
                            lapseGF, lapseSO, lapseSL, cHT, cOT, cA, cC, cR);
            }
            output3.close();
		}catch(Exception e) {e.getStackTrace();}
        System.out.println("Experimento 3 :: FIN");
	}
	
	
	/*
	
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

    */

}
