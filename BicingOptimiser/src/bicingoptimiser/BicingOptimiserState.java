/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bicingoptimiser;

import IA.Bicing.Estaciones;
import static java.lang.Integer.min;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
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
    private int numFurgonetas;
    private int maxFurgos;
    private int distanciaRecorrida;
   
    public BicingOptimiserState(Estaciones estaciones, int maxFurgonetas){ //G3N3R4D0R V4C10
        
        furgos = new ArrayList[estaciones.size()];
        origenesDisponibles = new ArrayList<Integer>();
        balances = new ArrayList<Pair>();
        this.estaciones = estaciones;
        this.maxFurgos = maxFurgonetas;
        numFurgonetas = 0;
        distanciaRecorrida = 0;
        for(int i = 0; i<furgos.length; i++){
            furgos[i] = new ArrayList<Pair>();
        }
    }
    
    public void solucionInicialSimple(){
        rellenarBalances();
    }
    
    public void solucionInicialCompleja(){
        rellenarBalances();
        
        //show must go on:
        boolean checkDeficit = true;
        for(int i = 0; checkDeficit && i < origenesDisponibles.size(); i++){
            int posIdDestino = balances.size() - (i+1);
            int idDestino = balances.get(posIdDestino).getFirst();
            if(balances.get(posIdDestino).getSecond() < 0) {
                anadirParada(i,idDestino,true);
            }
            else
                checkDeficit = false;
        }
    }
    
    private void rellenarBalances(){
        for(int i = 0; i < estaciones.size(); i++) { //BALANCES
            int balance = min(30, min(estaciones.get(i).getNumBicicletasNoUsadas(), estaciones.get(i).getNumBicicletasNext()-estaciones.get(i).getDemanda()));
            balances.add(new Pair(i,balance));
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
        if(furgos[idFurgo].size() == 0) return false;
        Integer numBicisActual = ((Pair)furgos[idFurgo].get(0)).getSecond();
        if(numBicisActual%10 == 0) return false;
        Integer idParadaOrigen = ((Pair)furgos[idFurgo].get(0)).getFirst();
        Pair x = balances.stream().filter(estacion -> idParadaOrigen.equals(estacion.getFirst())).findFirst().get();
        Integer bicisDisponibles = x.getSecond();
        Integer bicis = min(bicisDisponibles,(maxBicisFurgo-numBicisActual)%10);
        if(bicis > 0) {
            for (int i = 1; i < furgos[idFurgo].size(); i++) {
                Integer idParadaDestino = ((Pair) furgos[idFurgo].get(i)).getFirst();
                Pair y = balances.stream().filter(estacion -> idParadaDestino.equals(estacion.getFirst())).findFirst().get();
                Integer demanda = y.getSecond();
                if (demanda < 0) {
                    Integer bicisfinal = min(-demanda, bicis);
                    y.setSecond(demanda + bicisfinal);//Balance destino
                    x.setSecond(bicisDisponibles - bicisfinal); //Balance origen
                    ((Pair) furgos[idFurgo].get(0)).setSecond(numBicisActual + bicisfinal);   //Furgos origen
                    ((Pair) furgos[idFurgo].get(i)).setSecond(((Pair) furgos[idFurgo].get(i)).getSecond() + bicisfinal); //Furgos destino
                    return true;
                }
            }
        }
        return false;    
    }
    
    public boolean anadirBicicletaNextCoste(int idFurgo){
        if(furgos[idFurgo].size() == 0) return false;
        Integer numBicisActual = ((Pair)furgos[idFurgo].get(0)).getSecond();
        if(numBicisActual >= maxBicisFurgo) return false;
        Integer idParadaOrigen = ((Pair)furgos[idFurgo].get(0)).getFirst();
        Pair x = balances.stream().filter(estacion -> idParadaOrigen.equals(estacion.getFirst())).findFirst().get(); //Buscar en balances la estacion idParadaOrigen
        Integer bicisDisponibles = x.getSecond(); //Cuantas bicicletas puede dar la estacion idParadaOrigen
        Integer nivelNext = 1+((numBicisActual+9)/10);
        Integer bicis = min(bicisDisponibles,(10*nivelNext-numBicisActual));
        if(bicis > 0) {
            for (int i = 1; i < furgos[idFurgo].size(); i++) {
                Integer idParadaDestino = ((Pair) furgos[idFurgo].get(i)).getFirst();
                Pair y = balances.stream().filter(estacion -> idParadaDestino.equals(estacion.getFirst())).findFirst().get();
                Integer demanda = y.getSecond(); //balance
                if (demanda < 0) {
                    Integer bicisfinal = min(-demanda, bicis);
                    y.setSecond(y.getSecond() + bicisfinal);          //Balance destino
                    x.setSecond(x.getSecond() - bicisfinal);          //Balance origen
                    ((Pair) furgos[idFurgo].get(0)).setSecond(((Pair) furgos[idFurgo].get(0)).getSecond() + bicisfinal);    //Furgos origen
                    ((Pair) furgos[idFurgo].get(i)).setSecond(((Pair) furgos[idFurgo].get(i)).getSecond() + bicisfinal);    //Furgos destino
                    return true;
                }
            }
        }
        return false; 
    }
    
    public boolean anadirParada(int idDisponible, int idNuevaParada, boolean NextCoste) {
        boolean x;
        Integer idFurgo = origenesDisponibles.get(idDisponible);
        if(furgos[idFurgo].size() == 0){ //Caso que no tiene ninguna parada
            furgos[idFurgo].add(new Pair(idFurgo,0));
            furgos[idFurgo].add(new Pair(idNuevaParada,0));
            if(NextCoste) x = !anadirBicicletaNextCoste(idFurgo);
            else x = !anadirBicicletaMismoCoste(idFurgo);
            if(!(numFurgonetas < maxFurgos) || x){
                furgos[idFurgo].remove(1);
                furgos[idFurgo].remove(0);
                return false;
            }
            ++numFurgonetas;
            return true;
        }
        furgos[idFurgo].add(new Pair(idNuevaParada,0)); //Caso que tiene una parada
        if(NextCoste) x = !anadirBicicletaNextCoste(idFurgo);
        else x = !anadirBicicletaMismoCoste(idFurgo);
        if(x || ((Pair)furgos[idFurgo].get(2)).getSecond() == 0) {
            furgos[idFurgo].remove(2);
            return false;
        }
        origenesDisponibles.remove(idDisponible);
        return true;
    }



    public boolean permutarParadas(int idFurgo) {
        if(furgos[idFurgo].size() == 0) return false;
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
        this.numFurgonetas = estadoACopiar.numFurgonetas;
        this.maxFurgos = estadoACopiar.maxFurgos;
    }
   
    //--------------------------Heuristico--------------------------------------

    public double getIngresos() {
        double ingresos = 0;
        
        for(int i = 0; i < furgos.length; ++i) {
            if (furgos[i].size() != 0) {
                ingresos += (Integer) ((Pair) furgos[i].get(0)).getSecond();
            }
        }
        return ingresos;
    }
    
    public double calcularCosteDistancia() {
      
        double coste = 0;
        distanciaRecorrida = 0;
        for(int i = 0; i < furgos.length; ++i) {
            double distancia = 0;
            if(furgos[i].size() != 0) {
                for(int j = 0; j < furgos[i].size()-1; j++) {
                    int origen = (Integer) ((Pair) furgos[i].get(j)).getFirst();
                    int parada = (Integer) ((Pair) furgos[i].get(j+1)).getFirst();
                    
                    int x = 0;  if(j == 1) x = 2; //para tener en cuenta que de 1a a 2a parada llevas menos bicis
                    distancia = abs(estaciones.get(parada).getCoordX() - estaciones.get(origen).getCoordX());
                    distancia += abs(estaciones.get(parada).getCoordY() - estaciones.get(origen).getCoordY());
                    distanciaRecorrida += distancia;
                    coste += (((Integer) ((Pair) furgos[i].get(x)).getSecond() + 9)/10) * distancia/1000;
                    distancia = 0;
                }
            }
        }
        return coste;
    }
    
    public double excedenteTeorico(){
        double excTeorico = 0;
        for(int i = 0; i < furgos.length; i++){
            Integer idEstacion = new Integer(i);
            if(!furgos[i].isEmpty()) {
                int bicisExcedentes = balances.stream().filter(estacion -> idEstacion.equals(estacion.getFirst())).findFirst().get().getSecond();
                excTeorico += bicisExcedentes;
            }
        }
        return excTeorico;
    }
    
    
   
    public String toString() {
        StringBuffer resultado = new StringBuffer();
        resultado.append("\n");
        resultado.append("Trayectos de las furgonetas:\n");
        resultado.append("\n");
        for (int i = 0; i < furgos.length; i++) {
            if(!furgos[i].isEmpty()) {
                resultado.append("Una furgo sale de la estacion " + i + " con " + ((Pair)furgos[i].get(0)).getSecond() + " bicicletas: \n");
                for(int j = 1; j < furgos[i].size(); ++j) {
                   resultado.append("\tDeja " + ((Pair)furgos[i].get(j)).getSecond() + " bicicletas a la estacion " + ((Pair)furgos[i].get(j)).getFirst() + "\n");
                }
                resultado.append("\n");
            }
        }
        resultado.append("==========================================================\n");
        calcularCosteDistancia();
        resultado.append("\n");
        resultado.append("Las furgonetas han recorrido un total de " + distanciaRecorrida/1000.0 + " kilómetros\n");
        return resultado.toString();
    	
    	
    }
}
