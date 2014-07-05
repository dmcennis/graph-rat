/**
 * PropertiesFactory
 * Created Jan 26, 2009 - 2:45:51 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.descriptors;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertiesFactory extends AbstractFactory<PropertiesInternal>{

    private static PropertiesFactory instance = null;
    
    static public PropertiesFactory newInstance(){
        if(instance==null){
            instance = new PropertiesFactory();
        }
        return instance;
    }
        
    private PropertiesFactory(){
        ParameterInternal type = new BasicParameter();
        type.setType("PropertiesClass");
        type.setParameterClass(String.class);
        PropertyRestriction restriction = new PropertyRestriction();
        restriction.setClassType(String.class);
        restriction.setMaxCount(1);
        restriction.setMinCount(1);
        type.setRestrictions(restriction);
        type.add("PropertiesImplementation");
        properties.add(type);
        
        properties.setDefaultRestriction(new PropertyRestriction());
        
        map.put("PropertiesImplementation", new PropertiesImplementation());
    }
    
    public PropertiesInternal create(){
        return create(properties);
    }
    
    public PropertiesInternal create(Properties props){
        String type = (String) props.get("PropertiesClass").getValue().iterator().next();
        PropertiesInternal ret = map.get(type);
        if(ret != null){
            return ret.duplicate();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Properties type '"+type+"' does not exist -  assuming PropertiesImplementation");
            return new PropertiesImplementation();
        }
    }

    public Parameter getClassParameter(){
        return properties.get("PropertiesClass");
    }
}
