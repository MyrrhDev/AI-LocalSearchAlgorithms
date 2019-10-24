/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bicingoptimiser;

import aima.search.framework.HeuristicFunction;
/**
 *
 * @author Josep Clotet Ginovart, Fernando Marimon Llopis, Mayra Pastor Valdivia
 */
public class BicingHeuristicIngresos implements HeuristicFunction {
// Aquest valora els costos. verificar que se puede con int la sig. funcion:
		public double getHeuristicValue(Object state) {
			BicingOptimiserState estado = (BicingOptimiserState ) state;
                        return estato.getIngresos();
		}
}
