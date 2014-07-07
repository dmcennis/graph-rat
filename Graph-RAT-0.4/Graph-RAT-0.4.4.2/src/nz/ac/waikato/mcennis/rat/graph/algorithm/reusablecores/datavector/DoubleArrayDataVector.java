/*
 * Created 21/05/2008-16:11:17
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.reusablecores.datavector;

import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;
import org.mcennis.graphrat.algorithm.reusablecores.distance.*;

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
    public double getValue(Object index) {
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
    public Object getCurrentIndex() {
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

}
