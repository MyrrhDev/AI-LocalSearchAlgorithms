/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bicingoptimiser;

import IA.Bicing.Estaciones;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Josep Clotet Ginovart, Fernando Marimon Llopis, Mayra Pastor Valdivia
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        int maxFurgos;
        int numEstaciones;
        int numBicis;
        int demanda;
        int seed;
        int boolHill;
        int boolHeurSimple;
        int boolIniSimple;
        int boolProporcionalidad;
        
        Scanner in = new Scanner(System.in);
        System.out.println("Rush hour? (Sí: 1, No: 0): ");
        demanda = in.nextInt();
        System.out.println("Proporcionalidad entre bicis-estacions-furgonetas? (Sí: 1, No: 0): ");
        boolProporcionalidad = in.nextInt();
        System.out.println("Num Estaciones: ");    
        numEstaciones = in.nextInt();
        if(boolProporcionalidad==1){
            numBicis = 50*numEstaciones;
            maxFurgos = numEstaciones/5;
        }
        else{
            System.out.println("Num Bicis: ");    
            numBicis = in.nextInt();
            System.out.println("Num Furgos: "); 
            maxFurgos = in.nextInt();
        }
 
        System.out.println("Seed: "); 
        seed = in.nextInt();
        
        System.out.println("Hill Climbing: 1, Simulated Annealing: 2: ");
        boolHill = in.nextInt();
        
        System.out.println("Heurístico Simple: 1, Heurístico Complejo: 2: ");
        boolHeurSimple = in.nextInt();
        
        System.out.println("Estado inicial vacío: 1, Estado inicial lleno: 2: ");
        boolIniSimple = in.nextInt();
        
        Estaciones estaciones = new Estaciones(numEstaciones, numBicis, demanda, seed);
        BicingOptimiserState estado = new BicingOptimiserState(estaciones, maxFurgos);
        
        if(boolIniSimple==1)
            estado.solucionInicialSimple();
        else
            estado.solucionInicialCompleja();
        
        BicingOptimiserState q;
        Problem prob;
        
        try{
            if(boolHeurSimple==1){
                if(boolHill==1)
                    prob = new Problem(estado, new BicingSuccessorsHillClimbing(), new BicingOptimiserGoalTest(), new BicingHeuristicIngresos());
                else
                    prob = new Problem(estado, new BicingSuccessorsSimAnnealing(), new BicingOptimiserGoalTest(), new BicingHeuristicIngresos());
            }
            else{
                if(boolHill==1)
                    prob = new Problem(estado, new BicingSuccessorsHillClimbing(), new BicingOptimiserGoalTest(), new BicingHeuristicCosteIngresos());
                else
                    prob = new Problem(estado, new BicingSuccessorsSimAnnealing(), new BicingOptimiserGoalTest(), new BicingHeuristicCosteIngresos());
            }
            
            Search search;
            if(boolHill==1)
                search= new HillClimbingSearch();
            else
                search = new SimulatedAnnealingSearch(2000,100,2,0.0001);
            
            SearchAgent agent = new SearchAgent(prob,search);
            
            if(boolHill==1){
                printActions(agent.getActions());
                System.out.println("");
                printInstrumentation(agent.getInstrumentation());
                System.out.println("");
            }
            
            q = (BicingOptimiserState) search.getGoalState();
            System.out.println("==========================================================");
            System.out.println("");
            System.out.println("SOLUCIÓN:");
            System.out.println("");
            System.out.println("Ingresos: "+q.getIngresos());
            System.out.println("Costes: "+q.calcularCosteDistancia());
            System.out.println("Ingresos - Costes: "+(q.getIngresos()-q.calcularCosteDistancia()) );
            System.out.println("");

            //System.out.print(((BicingOptimiserState) search.getGoalState()).toString());
            System.out.println("=========================================================="); 
            System.out.println(q.toString());
        
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
     private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }
            
}
