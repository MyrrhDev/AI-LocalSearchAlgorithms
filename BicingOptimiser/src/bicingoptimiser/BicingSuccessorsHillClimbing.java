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

/**
 *
 * @author Josep Clotet Ginovart, Fernando Marimon Llopis, Mayra Pastor Valdivia
 */
public class BicingSuccessorsHillClimbing implements SuccessorFunction{
    
    @Override
    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        BicingOptimiserState estado = (BicingOptimiserState) aState;
        //BicingHeuristicIngresos ingresos = new BicingHeuristicIngresos();

        
        //permutarParadas(int idFurgo)
        for (int i = 0; i < estado.furgosLength(); i++) {
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            if(newEstado.anadirBicicletaMismoCoste(i)) {
                StringBuffer S = new StringBuffer();
                S.append("anadidas bicis sin pasar de nivel a la furgo/paradaOrigen " + i + "\n");//Para saber que parada hacer resta de bicis entre estado y newEstudia
                retVal.add(new Successor(S.toString(), newEstado));
            }
        }
        
        for (int i = 0; i < estado.furgosLength(); i++) {
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            if(newEstado.anadirBicicletaNextCoste(i)) {
                StringBuffer S = new StringBuffer();
                S.append("anadidas bicis pasando de nivel a la furgo/paradaOrigen " + i + "\n");//Para saber que parada hacer resta de bicis entre estado y newEstudia
                retVal.add(new Successor(S.toString(), newEstado));
            }
        }
        
        for(int i = 0; i < estado.origenesDispLength(); i++){
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            int idDisp = newEstado.getIdOrigenDisp(i);
            for(int j = 0; j < estado.furgosLength(); j++){
                if(newEstado.anadirParada(idDisp, j)) {
                   StringBuffer S = new StringBuffer();
                    S.append("estacion " + j + " anadida al trayecto que sale de la estacion " + idDisp + "\n");
                    retVal.add(new Successor(S.toString(), newEstado)); 
                }
            }
        }
        
        for(int i = 0; i < estado.furgosLength(); i++) {
            BicingOptimiserState newEstado = new BicingOptimiserState(estado);
            if(newEstado.permutarParadas(i)) {
                StringBuffer S = new StringBuffer();
                S.append("se han permutado las paradas del trayecto con origen en la estacion " + i + "\n");
                retVal.add(new Successor(S.toString(), newEstado));
            }
        } 
        
        return retVal;
    }
}
