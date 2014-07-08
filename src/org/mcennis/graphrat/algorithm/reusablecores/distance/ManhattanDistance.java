/*

 * Created 23/05/2008-15:49:30

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


package org.mcennis.graphrat.algorithm.reusablecores.distance;



import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;



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





    public ManhattanDistance prototype(){

        return new ManhattanDistance();

    }

}

