/**

 * Copyright Daniel McEnnis, Anna Huang, see license.txt

 */

package org.mcennis.graphrat.reusablecores.distance;



import org.mcennis.graphrat.reusablecores.datavector.DataVector;



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

