/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bicingoptimiser;

import IA.Bicing.Estaciones;
import aima.util.Pair;
import static java.lang.Integer.min;
import java.util.ArrayList;

/**
 *
 * @author Josep Clotet Ginovart, Fernando Marimon Llopis, Mayra Pastor Valdivia
 */
public class BicingOptimiserState {

    private ArrayList[] furgos;
    private ArrayList<Boolean> origenes; //Puede estar solo en constructora
    private ArrayList<Pair> balances;
    public static int maxBicisFurgo = 30;
   
    public BicingOptimiserState(Estaciones estaciones){
        
        furgos = new ArrayList[estaciones.size()];
        origenes = new ArrayList<Boolean>(estaciones.size());
        balances = new ArrayList<Pair>(estaciones.size());
        
        for(int i=0; i<furgos.length; i++){
            furgos[i] = new ArrayList<Pair>();
        }
        /*
        furgos[0].add(new Pair(1,30));
        furgos[0].add(new Pair(2,2));
        furgos[0].add(new Pair(3,3));
        furgos[0].get(2);*/
        
    }
    
    public boolean anadirBicicletaMismoCoste(int idFurgo, int parada){
        Integer numBicisActual = (Integer)((Pair)furgos[idFurgo].get(0)).getSecond();
        if(numBicisActual%10 == 0) return false;
        Integer idParadaOrigen = (Integer)((Pair)furgos[idFurgo].get(0)).getFirst();
        Integer bicisDisponibles = (Integer)((Pair)balances.stream().filter(estacion -> idParadaOrigen.equals(estacion.getFirst()))).getSecond();
        Integer bicis = min(bicisDisponibles,(maxBicisFurgo-numBicisActual)%10);
        
        for(int i = 1; i < furgos[idFurgo].size(); i++) {
            Integer idParadaDestino = (Integer)((Pair)furgos[idFurgo].get(i)).getFirst();
            Integer demanda = (Integer)((Pair)balances.stream().filter(estacion -> idParadaDestino.equals(estacion.getFirst()))).getSecond();
            if (demanda < 0) {
                Integer bicisfinal = min(-demanda,bicis);
                demanda += bicisfinal;
                bicisDisponibles -= bicisfinal;
                return true;
            }
        }
        return false;
            
    }
    public void anadirBicicletaNextCoste(int idFurgo ){};
            
    
    public BicingOptimiserState(BicingOptimiserState estadoACopiar){
        
    }
   
    
}
