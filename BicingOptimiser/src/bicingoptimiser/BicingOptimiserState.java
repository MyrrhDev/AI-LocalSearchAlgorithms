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
    private ArrayList<Integer> origenes;//El valor del integer es el Id de la estacion, solo excedentes
    private ArrayList<Pair> balances;
    public static int maxBicisFurgo = 30;
    private Estaciones estaciones;
   
    public BicingOptimiserState(Estaciones estaciones){
        
        furgos = new ArrayList[estaciones.size()];
        origenes = new ArrayList<Integer>(estaciones.size());
        balances = new ArrayList<Pair>(estaciones.size());
        this.estaciones = estaciones;
        
        for(int i=0; i<furgos.length; i++){
            furgos[i] = new ArrayList<Pair>();
        }
        /*
        furgos[0].add(new Pair(1,30));
        furgos[0].add(new Pair(2,2));
        furgos[0].add(new Pair(3,3));
        furgos[0].get(2);*/
        
    }
    
    public boolean anadirBicicletaMismoCoste(int idFurgo){
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
                demanda += bicisfinal;          //Balance destino
                bicisDisponibles -= bicisfinal; //Balance origen
                numBicisActual += bicisfinal;   //Furgos origen
                Integer bicisDejadas = (Integer)((Pair)furgos[idFurgo].get(i)).getSecond();
                bicisDejadas += bicisfinal; //Furgos destino
                return true;
            }
        }
        return false;    
    }
    
    public boolean anadirBicicletaNextCoste(int idFurgo){
        Integer numBicisActual = (Integer)((Pair)furgos[idFurgo].get(0)).getSecond();
        if(numBicisActual >= maxBicisFurgo) return false;
        Integer idParadaOrigen = (Integer)((Pair)furgos[idFurgo].get(0)).getFirst();
        Integer bicisDisponibles = (Integer)((Pair)balances.stream().filter(estacion -> idParadaOrigen.equals(estacion.getFirst()))).getSecond();
        Integer nivelNext = 1+((numBicisActual+9)/10);
        Integer bicis = min(bicisDisponibles,(10*nivelNext-numBicisActual));
        
        for(int i = 1; i < furgos[idFurgo].size(); i++) {
            Integer idParadaDestino = (Integer)((Pair)furgos[idFurgo].get(i)).getFirst();
            Integer demanda = (Integer)((Pair)balances.stream().filter(estacion -> idParadaDestino.equals(estacion.getFirst()))).getSecond();
            if (demanda < 0) {
                Integer bicisfinal = min(-demanda,bicis);
                demanda += bicisfinal;          //Balance destino
                bicisDisponibles -= bicisfinal; //Balance origen
                numBicisActual += bicisfinal;   //Furgos origen
                Integer bicisDejadas = (Integer)((Pair)furgos[idFurgo].get(i)).getSecond();
                bicisDejadas += bicisfinal; //Furgos destino
                return true;
            }
        }
        return false; 
    }
    
    public boolean anadirParada(int idFurgo, int idNuevaParada) {
        if(furgos[idFurgo].get(0) == null){ //Caso que no tiene ninguna parada
            furgos[idFurgo].add(new Pair(idFurgo,0));
            furgos[idFurgo].add(new Pair(idNuevaParada,0));
            if(!anadirBicicletaNextCoste(idFurgo)) {
                furgos[idFurgo].remove(1);
                furgos[idFurgo].remove(0);
                return false;
            }
            return true;
        }
        if(furgos[idFurgo].size() > 2) return false; //Caso que tiene dos paradas ya
        
        furgos[idFurgo].add(new Pair(idNuevaParada,0)); //Caso que tiene una parada
        if(!anadirBicicletaNextCoste(idFurgo)) {
            furgos[idFurgo].remove(1);
            return false;
        }
        return true;
    }
    
    public BicingOptimiserState(BicingOptimiserState estadoACopiar){
        
    }
   
    
}
