/**
 * DataVectorXMLFactory
 * Created Jan 17, 2009 - 10:01:36 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.reusablecores.datavector.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;

/**
 *
 * @author Daniel McEnnis
 */
public class DataVectorXMLFactory extends AbstractFactory<DataVectorXML>{
    static DataVectorXMLFactory instance= null;

    public static DataVectorXMLFactory newInstance(){
        if(instance==null){
            instance = new DataVectorXMLFactory();
        }
        return instance;
    }
    
    DataVectorXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("DataVectorXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("DoubleArrayVector");
        properties.add(name);
        
        map.put("DoubleArrayVector",new DoubleArrayVectorXML());
        map.put("InstanceVector",new InstanceVectorXML());
        map.put("MapVector",new MapVectorXML());
    }
    
    public DataVectorXML create(String name){
        return create(name,properties);
    }
    
    public DataVectorXML create(Properties props){
        return create(null,props);
    }
    
    public DataVectorXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("DataVectorXMLClass") != null) && (parameters.get("DataVectorXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("DataVectorXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("DataVectorXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (DataVectorXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"DataVectorXML class '"+name+"' not found - assuming DoubleArrayVector");
            return new DoubleArrayVectorXML();
        }
    }
    public Parameter getClassParameter(){
        return properties.get("DataVectorXMLClass");
    }
}
