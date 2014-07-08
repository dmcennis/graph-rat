/*

 * Created 21/05/2008-16:11:17

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


package org.mcennis.graphrat.algorithm.reusablecores.datavector;



/**

 * Class cretaing a data vector backed by a double array.  

 * @author Daniel McEnnis

 */

public class DoubleArrayDataVector implements DataVector{



    double[] source;

    

    int index = -1;

    

    int size=0;

    

    /**

     * Create a data vector from the given double array.  The array is stored by 

     * reference, not value.

     * @param source source data array

     */

    public DoubleArrayDataVector(double[] source){

        this.source = source;

        size = this.source.length;

    }

    

    @Override

    public int size() {

        return size;

    }

    

    @Override

    public void setSize(int s){

        size = s;

    }



    @Override

    public double getValue(Comparable index) {

        if(index.getClass().getName().contentEquals("java.lang.Integer")){

            return source[((Integer)index).intValue()];

        }else{

            return Double.NaN;

        }

    }



    @Override

    public void reset() {

        index = -1;

    }



    @Override

    public double getCurrentValue() {

        return source[index];

    }



    @Override

    public Comparable getCurrentIndex() {

        return index;

    }



    @Override

    public void next() {

        index++;

    }



    @Override

    public boolean hasNext() {

        if(index < source.length-1){

            return true;        

        }else{

            return false;

        }

    }



    public int compareTo(Object o) {

        if(this.getClass().getName().compareTo(o.getClass().getName())==0){

            DoubleArrayDataVector right = (DoubleArrayDataVector)o;

            if(this.size - right.size != 0){

                return this.size - right.size;

            }

            for(int i=0;i<size;++i){

                    if(source[i] - right.source[i] >0){

                        return -1;

                    }else if(source[i] - right.source[i] < 0){

                        return 1;

                    }

            }

            return index - right.index;

        }else{

            return this.getClass().getName().compareTo(o.getClass().getName());

        }

    }



}

