/**
 * Copyright Daniel McEnnis, Anna Huang, see license.txt
 */
package org.mcennis.graphrat.algorithm.reusablecores.distance;

import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;

/**
 * Adapted from Anna Huang's distance metric for Weka by Daniel McEnnis
 * 
 * @author Anna Huang
 */
public class CosineDistance implements DistanceFunction{

        @Override
	public double distance(DataVector inst1, DataVector inst2) {
		
		double dist = 0.0, product = 0.0, lengthA = 0.0, lengthB = 0.0;
//		for(int v=0; v<inst1.numValues(); v++){
                inst1.reset();
                while(inst1.hasNext()){
                        inst1.next();
			product += inst1.getCurrentValue() * inst2.getValue(inst1.getCurrentIndex());
			lengthA += inst1.getCurrentValue() * inst1.getCurrentValue();
		}
//		for(int v=0; v<inst2.numValues(); v++) {
                inst2.reset();
                while(inst2.hasNext()){
                    inst2.next();
			lengthB += inst2.getCurrentValue() * inst2.getCurrentValue();
		}
		lengthA = Math.sqrt(lengthA);
		lengthB = Math.sqrt(lengthB);
		
                if((lengthA ==0.0)||(lengthB == 0.0)){
                    return Double.NaN;
                }
                
		dist = product / (lengthA * lengthB);
//		dist = 1 - dist;
		return dist;
	}
	
}
