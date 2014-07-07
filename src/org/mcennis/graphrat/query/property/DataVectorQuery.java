/**
 * DataVectorQuery
 * Created Jan 5, 2009 - 11:47:45 PM
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
package org.mcennis.graphrat.query.property;

import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.reusablecores.InstanceManipulation;
import org.mcennis.graphrat.reusablecores.datavector.DataVector;
import org.mcennis.graphrat.reusablecores.datavector.DoubleArrayDataVector;
import org.mcennis.graphrat.reusablecores.datavector.InstanceDataVector;
import org.mcennis.graphrat.reusablecores.distance.CosineDistance;
import org.mcennis.graphrat.reusablecores.distance.DistanceFunction;
import org.dynamicfactory.propertyQuery.Query.State;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.propertyQuery.NumericQuery;
import org.dynamicfactory.propertyQuery.PropertyQuery;
import weka.core.Instance;

/**
 *
 * @author Daniel McEnnis
 */
public class DataVectorQuery implements PropertyQuery {

    boolean not = false;
    
    DataVector right;
    
    DistanceFunction comparison;
    
    NumericQuery numericQuery;
    
    transient State state = State.UNINITIALIZED;
    
    public boolean execute(Property property) {
        DataVector left = null;
        //TODO: make a factory call instead of a reflection call
        if(property.getPropertyClass().isInstance("org.mcennis.graphrat.algorithm.reusablecores.datavector.Datavector")){
            if(property.getValue().size() > 0){
                left = (DataVector)property.getValue().iterator().next();
            }
        }else{
            LinkedList<Instance> instance = InstanceManipulation.propertyToInstance(property);
            if(instance.size() > 0){
                left = new InstanceDataVector(instance.getFirst());
            }
        }
        if(left == null){
            return not;
        }
        double value = comparison.distance(left, right);
        
        boolean result = numericQuery.execute(value);
        
        if(not){
            return !result;
        }else{
            return result;
        }
    }

    public void exportQuery(Writer writer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(DistanceFunction comparison, boolean not, NumericQuery numericQuery, DataVector right) {
        state = State.LOADING;
        if(comparison == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "comparison function is null, using Cosine as default");
            comparison = new CosineDistance();
        }else{
            this.comparison = comparison;
        }
        this.not = not;
        if(numericQuery == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "numeric query is null, using default values");
            numericQuery = new NumericQuery();
        }else{
            this.numericQuery = numericQuery;
        }
        if(right == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "comparison vector is null, using a zero-dimensional vector");
            right = new DoubleArrayDataVector(new double[]{});
        }else{
            this.right = right;
        }
        state = State.READY;
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            DataVectorQuery r = (DataVectorQuery)o;
            if(this.comparison.getClass().getName().compareTo(r.comparison.getClass().getName())!=0){
                return this.comparison.getClass().getName().compareTo(r.comparison.getClass().getName());
            }
            int val = numericQuery.compareTo(r.numericQuery);
            if(val != 0){
                return val;
            }
            if(! not && r.not){
                return -1;
            }
            if(not && r.not){
                return 1;
            }
            return right.compareTo(r);
        }else{
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public DataVectorQuery prototype() {
        return new DataVectorQuery();
    }
}
