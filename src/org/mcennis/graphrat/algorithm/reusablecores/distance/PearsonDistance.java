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

 * Adapted from Anna Huang's Pearson distance metric for Weka by Daniel McEnnis

 * @author Anna Huang

 */

public class PearsonDistance implements DistanceFunction {



    @Override

    public double distance(DataVector inst1, DataVector inst2) {



        double dist = 0.0, product = 0.0, squarSumA = 0.0, squarSumB = 0.0, tfA = 0.0, tfB = 0.0;

        int size = 0;

//		for(int v = 0; v < inst1.numValues(); v++) {

        inst1.reset();

        while (inst1.hasNext()) {

            inst1.next();

            double temp = inst1.getCurrentValue() * inst2.getValue(inst1.getCurrentIndex());

            if (temp == 0.0) {

                size--;

            }

            product += temp;



            squarSumA += inst1.getCurrentValue() * inst1.getCurrentValue();



            tfA += inst1.getCurrentValue();

        }



//		for(int v = 0; v < inst2.numValues(); v++) {

        inst2.reset();

        while (inst2.hasNext()) {

            inst2.next();

            squarSumB += inst2.getCurrentValue() * inst2.getCurrentValue();



            tfB += inst2.getCurrentValue();

        }



        int m = inst1.size() + inst2.size() + size;//   inst1.classIndex() == -1) ? inst1.numAttributes() : inst1.numAttributes() -1;



        dist = m * product - tfA * tfB;



        dist /= Math.sqrt((m * squarSumA - tfA * tfA) * (m * squarSumB - tfB * tfB));



        return dist;

    }



    public PearsonDistance prototype(){

        return new PearsonDistance();

    }

}

