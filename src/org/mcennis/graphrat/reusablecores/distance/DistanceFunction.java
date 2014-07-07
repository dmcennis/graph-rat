/**

 * Copyright Daniel McEnnis, see license.txt

 */

package org.mcennis.graphrat.reusablecores.distance;



import org.mcennis.graphrat.reusablecores.datavector.DataVector;


/**

 * Interface abstracting distance functions.  Existance of a distance between 

 * all vectors can be assumed.  No other assumptions about the distance 

 * function should be assumed beyond existance (including symettry of distance measures) unless the

 * specific distance measure defines it.

 * 

 * @author Daniel McEnnis

 */

public interface DistanceFunction {



    /**

     * Return the distance between the two given vectors as a double.

     * @param inst1 left data vector

     * @param inst2 right data vector

     * @return distance by a given metric

     */

    public double distance(DataVector inst1, DataVector inst2);

    

    public DistanceFunction prototype();

}

