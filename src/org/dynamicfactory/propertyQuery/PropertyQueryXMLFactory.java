/**
 * PropertyQueryXMLFactory
 * Created Jan 26, 2009 - 11:10:44 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.propertyQuery;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyQueryXMLFactory extends AbstractFactory<PropertyQueryXML>{

        static PropertyQueryXMLFactory instance = null;
    
    static public PropertyQueryXMLFactory newInstance(){
        if(instance == null){
            instance = new PropertyQueryXMLFactory();
        }
        return instance;
    }
    
    private PropertyQueryXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("PropertyQueryXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("NullPropertyQuery");
        properties.add(name);
        
        map.put("NullPropertyQuery",new NullPropertyQueryXML());
        map.put("DataVectorQuery",new DataVectorQueryXML());
        map.put("NumericQuery",new NumericQueryXML());
        map.put("StringQuery",new StringQueryXML());
        
    }

    @Override
    public PropertyQueryXML create(Properties props) {
        return create(null,props);
    }
    
    public PropertyQueryXML create(String name){
        return create(name,properties);
    }
    
    public PropertyQueryXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("PropertyQueryXMLClass") != null) && (parameters.get("PropertyQueryXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("PropertyQueryXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("PropertyQueryXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (PropertyQueryXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming NullPropertyQueryXML");
            return new NullPropertyQueryXML();
        }
    }
    
    public Parameter getClassParameter(){
        return properties.get("PropertyQueryXMLClass");
    }
}
