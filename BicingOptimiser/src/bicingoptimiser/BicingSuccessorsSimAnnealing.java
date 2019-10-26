/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bicingoptimiser;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Josep Clotet Ginovart, Fernando Marimon Llopis, Mayra Pastor Valdivia
 */
public class BicingSuccessorsSimAnnealing implements SuccessorFunction{
    @Override
    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        BicingOptimiserState estado = (BicingOptimiserState) aState;
        //BicingHeuristicIngresos ingresos = new BicingHeuristicIngresos();

        Random myRandom = new Random();
        //int lucky = myRandom.nextInt(4);
        for (int i = 0; i < 4; i++) {
            int lucky = myRandom.nextInt(estado.furgosLength());
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            if(newEstado.anadirBicicletaMismoCoste(lucky)) {
                StringBuffer S = new StringBuffer();
                S.append("anadidas bicis sin pasar de nivel a la furgo/paradaOrigen " + lucky + "\n");//Para saber que parada hacer resta de bicis entre estado y newEstudia
                retVal.add(new Successor(S.toString(), newEstado));
            }
        }

        for (int i = 0; i < 4; i++) {
            int lucky = myRandom.nextInt(estado.furgosLength());
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            if(newEstado.anadirBicicletaNextCoste(lucky)) {
                StringBuffer S = new StringBuffer();
                S.append("anadidas bicis pasando de nivel a la furgo/paradaOrigen " + lucky + "\n");//Para saber que parada hacer resta de bicis entre estado y newEstudia
                retVal.add(new Successor(S.toString(), newEstado));
            }
        }

        int rand = myRandom.nextInt(estado.origenesDispLength());
        for(int j = 0; j < 4; j++){
            int lucky2 = myRandom.nextInt(estado.furgosLength());
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            if(newEstado.anadirParada(rand, lucky2)) {
               StringBuffer S = new StringBuffer();
                S.append("estacion " + lucky2 + " anadida al trayecto que sale de la estacion " + rand + "\n");
                retVal.add(new Successor(S.toString(), newEstado)); 
            }
        }

        for(int i = 0; i < 4; i++) {
            int lucky = myRandom.nextInt(estado.furgosLength());
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            if(newEstado.permutarParadas(lucky)) {
                StringBuffer S = new StringBuffer();
                S.append("se han permutado las paradas del trayecto con origen en la estacion " + lucky + "\n");
                retVal.add(new Successor(S.toString(), newEstado));
            }
        }
      
        return retVal;
    }
}
