/*
 * AggregatorXMLFactory - created 2/02/2009 - 5:45:22 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.reusablecores.aggregator.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregatorXMLFactory extends AbstractFactory<AggregatorXML>{

    static AggregatorXMLFactory instance = null;

    public static AggregatorXMLFactory newInstance(){
        if(instance == null){
            instance = new AggregatorXMLFactory();
        }
        return instance;
    }

    private AggregatorXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("AggregatorXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("MeanXML");
        properties.add(name);

        map.put("Concatentation",new ConcatenationXML());
        map.put("FirstItem",new FirstItemXML());
        map.put("Max",new MaxXML());
        map.put("Mean",new MeanXML());
        map.put("Min",new MinXML());
        map.put("Null",new NullXML());
        map.put("Product",new ProductXML());
        map.put("StandardDeviation",new StandardDeviationXML());
        map.put("Sum",new SumXML());
    }

    @Override
    public AggregatorXML create(Properties props) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AggregatorXML create(String name){
        return create(name,properties);
    }

    public AggregatorXML create(String name, Properties props){
        if(properties.check(props)){
            if(name == null){
                name = (String)props.get("AggregatorXMLClass").get();
            }
            if(map.containsKey(name)){
                return map.get(name).newCopy();
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"No aggregatorXML of type '"+name+"' exists - using NullXML instead");
                return new NullXML();
            }
        }else{
                return new NullXML();
        }
    }

    @Override
    public Parameter getClassParameter() {
        return properties.get("AggregatorXMLClass");
    }

}
