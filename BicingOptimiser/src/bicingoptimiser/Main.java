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
        int maxFlagos = 5;
        int numEstaciones = 25;
        int numBicis = 1250;
        int demanda = 1;
        int seed = 1234;
        Estaciones estaciones = new Estaciones(numEstaciones, numBicis, demanda, seed);
        BicingOptimiserState estado = new BicingOptimiserState(estaciones, maxFlagos);
        estado.solucionInicialSimple();
        //estado.solucionInicialCompleja();
        BicingOptimiserState q;
        try{
            //Problem prob = new Problem(estado, new BicingSuccessorsHillClimbing(), new BicingOptimiserGoalTest(), new BicingHeuristicIngresos());
            Problem prob = new Problem(estado, new BicingSuccessorsHillClimbing(), new BicingOptimiserGoalTest(), new BicingHeuristicCosteIngresos());
            Search search = new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(prob,search);
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
            q = (BicingOptimiserState) search.getGoalState();
            System.out.println(q.getIngresos());//-q.calcularCosteDistancia());
            //System.out.print(((BicingOptimiserState) search.getGoalState()).toString());
            System.out.println(estado.toString());  
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(estado.toString());
        
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
