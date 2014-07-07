/*

 * Created 23/05/2008-15:49:39

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

