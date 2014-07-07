/*
 * Created 23/05/2008-15:39:09
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.reusablecores.distance;

import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;

/**
 *
 * Euclidean distance measure as defined in Wikipedia
 * @author Daniel McEnnis
  */
public class EuclideanDistance implements DistanceFunction{

    @Override
    public double distance(DataVector inst1, DataVector inst2) {
        double ret = 0.0;
        inst2.reset();
        while(inst2.hasNext()){
            inst2.next();
            ret += Math.pow(inst2.getCurrentValue()-inst1.getValue(inst2.getCurrentIndex()),2);
        }
        inst1.reset();
        while(inst1.hasNext()){
            inst1.next();
            if(inst2.getValue(inst1.getCurrentIndex())== 0.0){
                ret += Math.pow(inst1.getCurrentValue(),2);
            }
        }
        return Math.sqrt(ret);
    }

}
