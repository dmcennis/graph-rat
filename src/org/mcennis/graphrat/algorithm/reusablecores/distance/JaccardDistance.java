/**

 * Copyright Daniel McEnnis, Anna Huang, see license.txt

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

package org.mcennis.graphrat.algorithm.reusablecores.distance;



import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;



/**

 * Jaccard Distance function adapted from Anna Huang's Weka Distance function by Daniel McEnnis

 * 

 * @author Anna Huang

 */

public class JaccardDistance implements DistanceFunction {



    @Override

	public double distance(DataVector inst1, DataVector inst2) {

		

		double dist = 0.0, product = 0.0, lengthA = 0.0, lengthB = 0.0;

		

//		for(int v = 0; v < inst1.numValues(); v++){

                inst1.reset();

                while(inst1.hasNext()){

                    inst1.next();

			

			product += inst1.getCurrentValue() * inst2.getValue(inst1.getCurrentIndex());

			

			lengthA += inst1.getCurrentValue() * inst1.getCurrentValue();

		}

		

//		for(int v = 0; v < inst2.numValues(); v++) {

                inst2.reset();

                while(inst2.hasNext()){

			inst2.next();

			

			lengthB += inst2.getCurrentValue() * inst2.getCurrentValue();

		}

		

		dist = product / (lengthA + lengthB - product);

				

		return dist;

	}



    public JaccardDistance prototype(){

        return new JaccardDistance();

    }

}

