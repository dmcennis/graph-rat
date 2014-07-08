/**
 * ExponentialDistance
 * Created Jan 19, 2009 - 10:01:39 PM
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
 *
 * @author Daniel McEnnis
 */
public class ExponentialDistance implements DistanceFunction{

    double positiveBase = 2.0;
    
    double negativeBase = 1.5;
    
    double positiveExponent = 2.0;
    
    double negativeExponent = 1.1;

    public double getNegativeBase() {
        return negativeBase;
    }

    public void setNegativeBase(double negativeBase) {
        this.negativeBase = negativeBase;
    }

    public double getNegativeExponent() {
        return negativeExponent;
    }

    public void setNegativeExponent(double negativeExponent) {
        this.negativeExponent = negativeExponent;
    }

    public double getPositiveBase() {
        return positiveBase;
    }

    public void setPositiveBase(double positiveBase) {
        this.positiveBase = positiveBase;
    }

    public double getPositiveExponent() {
        return positiveExponent;
    }

    public void setPositiveExponent(double positiveExponent) {
        this.positiveExponent = positiveExponent;
    }
    
    
    public double distance(DataVector inst1, DataVector inst2) {
        inst1.reset();
        inst2.reset();
        double positiveCount = 0.0;
        double negativeCount = 0.0;
        while(inst1.hasNext()){
            if(inst2.getValue(inst1.getCurrentIndex()) != 0.0){
                positiveCount += Math.abs(inst1.getCurrentValue()*inst2.getValue(inst1.getCurrentIndex()));
            }else{
                negativeCount++;
            }
        }
        while(inst2.hasNext()){
            if(inst1.getValue(inst2.getCurrentIndex())!=0.0){
                positiveCount += Math.abs(inst2.getCurrentValue()*inst1.getValue(inst2.getCurrentIndex()));
            }else{
                negativeCount++;
            }
        }
        return ((positiveBase * Math.pow(positiveExponent,positiveCount))-(negativeBase * Math.pow(negativeExponent,negativeCount)));
    }


    public ExponentialDistance prototype(){
        return new ExponentialDistance();
    }
}
