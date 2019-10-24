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
import java.util.Collections;
import java.util.Comparator;
/**
 *
 * @author Josep Clotet Ginovart, Fernando Marimon Llopis, Mayra Pastor Valdivia
 */
public class BicingOptimiserState {

    private ArrayList[] furgos; //La misma posicion de la furgoneta es el Id de la estacion de origen
    private ArrayList<Integer> origenesDisponibles;//El valor del integer es el Id de una estación excedentaria con 0 o 1 parada
    private ArrayList<Pair> balances;
    public static int maxBicisFurgo = 30;
    private Estaciones estaciones;
   
    public BicingOptimiserState(Estaciones estaciones){ //G3N3R4D0R V4C10
        
        furgos = new ArrayList[estaciones.size()];
        origenesDisponibles = new ArrayList<Integer>();
        balances = new ArrayList<Pair>(estaciones.size());
        this.estaciones = estaciones;
        
        for(int i = 0; i<furgos.length; i++){
            furgos[i] = new ArrayList<Pair>();
        }
    }
    
    public void solucionInicialSimple(){
        rellenarBalances();
    }
    
    public void solucionInicialCompleja(){
        rellenarBalances();
        //TODO
    }
    
    private void rellenarBalances(){
        for(int i = 0; i < estaciones.size(); i++) { //BALANCES
            int balance = min(30, min(estaciones.get(i).getNumBicicletasNoUsadas(), estaciones.get(i).getNumBicicletasNext()-estaciones.get(i).getDemanda()));
            balances.set(i, new Pair(i,balance));
        }
        
        Collections.sort(balances, new Comparator<Pair>(){
            @Override
            public int compare(Pair balance1, Pair balance2){
                return ((Integer)balance2.getSecond()).compareTo((Integer)balance1.getSecond()); //descending order
            }
        });
        int g = 0;
        while((Integer)balances.get(g).getSecond() > 0) {
            origenesDisponibles.add((Integer)balances.get(g).getFirst());
            ++g;
        }
    }
    
    //--------------------------GETTERS-----------------------------------
    
    public int furgosLength() {
        return furgos.length;
    }
    
    public int origenesDispLength() {
        return origenesDisponibles.size();
    }
    
    public int balancesLength() {
        return balances.size();
    }
    
    public int getIdOrigenDisp(int i){
        return origenesDisponibles.get(i);
    }
    
    //--------------------------OPERADORES-------------------------------
    
    public boolean anadirBicicletaMismoCoste(int idFurgo){
        if(furgos[idFurgo] == null) return false;
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
        if(furgos[idFurgo] == null) return false;
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
    
    public boolean anadirParada(int idDisponible, int idNuevaParada) {
        Integer idFurgo = origenesDisponibles.get(idDisponible);
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
    
        furgos[idFurgo].add(new Pair(idNuevaParada,0)); //Caso que tiene una parada
        if(!anadirBicicletaNextCoste(idFurgo)) {
            furgos[idFurgo].remove(1);
            return false;
        }
        origenesDisponibles.remove(idDisponible);
        return true;
    }
    
    public boolean permutarParadas(int idFurgo) {
        if(furgos[idFurgo] == null) return false;
        if(furgos[idFurgo].size() != 3) return false;
        Pair aux = (Pair)furgos[idFurgo].get(1);
        furgos[idFurgo].set(1,furgos[idFurgo].get(2));
        furgos[idFurgo].set(2,aux);
        return true;
    }
    
    //--------------------------CLONADORA--------------------------------------
    
    public BicingOptimiserState(BicingOptimiserState estadoACopiar){ //Clonador
        this.origenesDisponibles = new ArrayList();
        for(int i = 0; i < estadoACopiar.origenesDisponibles.size(); i++) {
            this.origenesDisponibles.add(new Integer(estadoACopiar.origenesDisponibles.get(i)));
        }
        this.balances = new ArrayList();
        for(int i = 0; i < estadoACopiar.balances.size(); i++) {
            this.balances.add(new Pair(new Integer((Integer)((Pair)estadoACopiar.balances.get(i)).getFirst()),new Integer((Integer)((Pair)estadoACopiar.balances.get(i)).getSecond())));
        }
        this.furgos = new ArrayList[estadoACopiar.furgos.length];
        for(int i = 0; i < estadoACopiar.furgos.length; i++) {
            this.furgos[i] = new ArrayList();
            for(int j = 0; j < estadoACopiar.furgos[i].size(); j++) {
                this.furgos[i].add(new Pair(new Integer((Integer)((Pair)estadoACopiar.furgos[i].get(j)).getFirst()),new Integer((Integer)((Pair)estadoACopiar.furgos[i].get(j)).getSecond())));
            }
        }
        this.estaciones = estadoACopiar.estaciones;
    }
   
    
}
