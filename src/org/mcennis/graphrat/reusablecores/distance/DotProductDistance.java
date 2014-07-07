/*

 * Created 23/05/2008-15:49:39

 * Copyright Daniel McEnnis, see license.txt

 */



package org.mcennis.graphrat.reusablecores.distance;



import org.mcennis.graphrat.reusablecores.datavector.DataVector;



/**

 * distance measure of dot-product of the two vectors as vectors in a Euclidean space.

 * @author Daniel McEnnis

 */

public class DotProductDistance implements DistanceFunction{



    @Override

    public double distance(DataVector inst1, DataVector inst2) {

		double product = 0.0;

//		for(int v=0; v<inst1.numValues(); v++){

                inst1.reset();

                while(inst1.hasNext()){

                        inst1.next();

			product += inst1.getCurrentValue() * inst2.getValue(inst1.getCurrentIndex());

		}

//		for(int v=0; v<inst2.numValues(); v++) {

		

		return product;

    }





    public DotProductDistance prototype(){

        return new DotProductDistance();

    }

}

