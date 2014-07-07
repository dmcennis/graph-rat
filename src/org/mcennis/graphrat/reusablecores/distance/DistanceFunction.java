/**

 * Copyright Daniel McEnnis, see license.txt

 */
/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
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

