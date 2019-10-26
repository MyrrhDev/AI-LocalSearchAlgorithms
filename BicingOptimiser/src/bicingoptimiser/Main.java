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

/**
 *
 * @author Josep Clotet Ginovart, Fernando Marimon Llopis, Mayra Pastor Valdivia
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        int maxFlagos = 20;
        int numEstaciones = 50;
        int numBicis = 3000;
        int demanda = 1;
        int seed = 1234;
        
        numEstaciones;
        
        
        
        Estaciones estaciones = new Estaciones(numEstaciones, numBicis, demanda, seed);
        BicingOptimiserState estado = new BicingOptimiserState(estaciones, maxFlagos);
        //estado.solucionInicialSimple();
        estado.solucionInicialCompleja();
        BicingOptimiserState q;
        try{
            Problem prob = new Problem(estado, new BicingSuccessorsHillClimbing(), new BicingOptimiserGoalTest(), new BicingHeuristicCosteIngresos());
            //Problem prob = new Problem(estado, new BicingSuccessorsSimAnnealing(), new BicingOptimiserGoalTest(), new BicingHeuristicCosteIngresos());
            Search search = new HillClimbingSearch();
            //Search search = new SimulatedAnnealingSearch(2000,100,2,0.0001);
            SearchAgent agent = new SearchAgent(prob,search);
            printActions(agent.getActions());
            System.out.println("");
            printInstrumentation(agent.getInstrumentation());
            System.out.println("");
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
