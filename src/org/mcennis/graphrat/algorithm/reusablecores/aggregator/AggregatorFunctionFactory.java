/**
 * Jul 23, 2008-5:42:31 PM
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

package org.mcennis.graphrat.algorithm.reusablecores.aggregator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregatorFunctionFactory extends AbstractFactory<AggregatorFunction>{
    static AggregatorFunctionFactory instance = null;
    
    public static AggregatorFunctionFactory newInstance(){
        if(instance == null){
            instance = new AggregatorFunctionFactory();
        }
        return instance;
    }
    
    private void AggregatorFunctionFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("AggregatorFunctionClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("MeanAggregator");
        properties.add(name);
        
        map.put("ConcatenationAggregator",new ConcatenationAggregator());
        map.put("FirstItemAggregator", new FirstItemAggregator());
        map.put("MaxAggregator", new MaxAggregator());
        map.put("MeanAggregator", new MeanAggregator());
        map.put("MinAggregator", new MinAggregator());
        map.put("NullAggregator", new NullAggregator());
        map.put("ProductAggregator", new ProductAggregator());
        map.put("StandardDeviationAggregator", new StandardDeviationAggregator());
        map.put("SumAggregator", new SumAggregator());
        
    }
    public AggregatorFunction create(String name){
        return create(name,properties);
    }
    
    
    public AggregatorFunction create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("AggregatorFunctionClass") != null) && (parameters.get("AggregatorFunctionClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("AggregatorFunctionClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("AggregatorFunctionClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return map.get(name).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"AggregatorFunction class '"+name+"' not found - assuming MeanAggregator");
            return new MeanAggregator();
        }
    }

    @Override
    public AggregatorFunction create(Properties props) {
        return create(null,props);
    }
    public Parameter getClassParameter(){
        return properties.get("AggregatorFunctionClass");
    }
}
