/**
 * AbstractFactory
 * Created Jan 26, 2009 - 2:56:20 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesImplementation;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;

/**
 *
 * @author Daniel McEnnis
 */
public abstract class AbstractFactory<Type> {
    
    protected HashMap<String,Type> map = new HashMap<String,Type>();
    protected PropertiesInternal properties = new PropertiesImplementation();
    
    public abstract Type create(Properties props);
            
    public void setDefaultProperty(ParameterInternal value) {
        properties.add(value);
    }
    
    public void addDefaultProperty(String type, Object value) throws InvalidObjectTypeException{
        properties.add(type,value);
    }

    public Properties getParameter() {
        return properties;
    }
    
    public Parameter getParameter(String type){
        return properties.get(type);
    }
    
    public void addType(String type,Type prototype){
        if(type == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null property class name added");
        }   
        if(prototype == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "No property obejct provided");
        }
        if((type != null)&&(prototype != null)){
            map.put(type,prototype);
        }
    }
    
    public boolean check(Properties props){
        boolean good = true;
        Iterator<Parameter> collection = props.get().iterator();
        while(collection.hasNext()){
            if(!properties.check(collection.next())){
                return false;
            }
        }
        return true;       
    }
    
    public boolean check(Parameter parameter){
        return properties.check(parameter);
    }
    
    public abstract Parameter getClassParameter();
    
    public Collection<String> getKnownTypes(){
        LinkedList<String> ret = new LinkedList<String>();
        ret.addAll(map.keySet());
        return ret;
    }
}
