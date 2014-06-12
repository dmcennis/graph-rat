/*
 * Created 23/05/2008-15:49:30
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance;

import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DataVector;

/**
 * Manhattan Distance measure as defined in Wikipedia
 * @author Daniel McEnnis
 */
public class ManhattanDistance implements DistanceFunction{

    @Override
    public double distance(DataVector inst1, DataVector inst2) {
        double ret = 0.0;
        inst2.reset();
        while(inst2.hasNext()){
            inst2.next();
            ret += Math.abs(inst1.getValue(inst2.getCurrentIndex())-inst2.getCurrentValue());
        }
        inst1.reset();
        while(inst1.hasNext()){
            inst1.next();
            if(inst2.getValue(inst1.getCurrentIndex())== 0.0){
                ret += Math.abs(inst1.getCurrentValue());
            }
        }
        return ret;
    }

}
