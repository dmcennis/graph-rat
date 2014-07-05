/**
 * Properties
 * Created Jan 23, 2009 - 1:54:28 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.descriptors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.PropertyFactory;


/**
 *
 * @author Daniel McEnnis
 */
public class PropertiesImplementation implements PropertiesInternal {

    TreeMap<String,ParameterInternal> propertyMap = new TreeMap<String,ParameterInternal>();
    
    SyntaxObject restriction = new PropertyRestriction();
    
    public PropertiesImplementation(){}
    
    public ParameterInternal get(String string){
        return propertyMap.get(string);
    }
    
    public void set(Property value){
        if(value == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null properties not permitted in a Properties object");
        }else if(propertyMap.containsKey(value.getType())){
            ParameterInternal p = propertyMap.get(value.getType());
            if(p.check(value)){
                p.set(value);
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Property '"+value.getType()+"' change vetoed by parameter restrictions");
            }
        }else if(restriction.check(value)){
            ParameterInternal p = new BasicParameter();
            p.setType(value.getType());
            p.setParameterClass(value.getClass());
            p.set(value);
            p.setRestrictions(restriction);
            propertyMap.put(value.getType(), p);
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Property '"+value.getType()+"' change vetoed by default parameter restrictions");
        }
    }
    
    public void add(String type,Object value){
        if(type == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null values not permitted in a Properties object");
        }else if(value == null){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null parameter names not permitted in a Properties object");
        }else if(!propertyMap.containsKey(type)){
            Property prop = PropertyFactory.newInstance().create(type,value.getClass());
            try {
                prop.add(value);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(PropertiesImplementation.class.getName()).log(Level.SEVERE, "After checking class is compatible, incompatible  class added.", ex);
            }
            if(restriction.check(prop)){
                ParameterInternal p = new BasicParameter();
                p.setType(type);
                p.setParameterClass(value.getClass());
                p.set(prop);
                p.setRestrictions(restriction);
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "No parameters of type '"+type+"' exist and the new parameter is vetoed by parameter restrictions");
            }
        }else if(propertyMap.get(type).check(type, value)){
            propertyMap.get(type).add(value);
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Property '"+propertyMap.get(type).getType()+"' change vetoed by property restrictions");
        }
    }
    
    public void add(ParameterInternal parameter){
        propertyMap.put(parameter.getType(),parameter);
    }
    
    public void remove(String type){
        if(type != null){
            propertyMap.remove(type);
        }
    }
    
    public SyntaxObject getDefaultRestriction(){
        return restriction;
    }
    
    public void setDefaultRestriction(SyntaxObject restriction){
        this.restriction = restriction;
    }
    
    public PropertiesImplementation duplicate(){
        PropertiesImplementation ret = new PropertiesImplementation();
        Iterator<String> mapIt =propertyMap.keySet().iterator();
        while(mapIt.hasNext()){
            String key = mapIt.next();
            ret.propertyMap.put(key, propertyMap.get(key).duplicate());
        }
        ret.restriction = this.restriction.duplicate();
        return ret;
    }
    
    public List<Parameter> get(){
        LinkedList<Parameter> ret = new LinkedList<Parameter>();
        ret.addAll(propertyMap.values());
        return ret;
    }
    
    public boolean check(Parameter type){
        if(propertyMap.containsKey(type.getType())){
            return propertyMap.get(type.getType()).check(type);
        }else{
            return restriction.check(type);
        }
    }
    
    public boolean check(Properties props){
        boolean good = true;
        Iterator<Parameter> it = props.get().iterator();
        while(it.hasNext()){
            if(!this.check(it.next())){
                good = false;
            }
        }
        return good;
    }
    
    public void replace(Parameter type){
        propertyMap.put(type.getType(),(ParameterInternal) type);
    }

    public PropertiesInternal merge(Properties right){
        PropertiesInternal ret = this.duplicate();
        Iterator<Parameter> names = right.get().iterator();
        while(names.hasNext()){
            Parameter param = names.next();
            if(ret.get(param.getType())!=null){
                ret.remove(param.getType());
            }
            ret.add(((ParameterInternal)param).duplicate());
        }
        return ret;
    }

    public void add(String type, List value) {
        if(propertyMap.containsKey(type)){
            propertyMap.get(type).add(value);
        }else{
            if(value != null){
                ParameterInternal prop = null;
                if(value.size()>0){
                    prop = ParameterFactory.newInstance().create(type,value.get(0).getClass() );
                }else{
                    prop = ParameterFactory.newInstance().create(type,Object.class);
                }
                if(this.check(prop)){
                    propertyMap.put(type, prop);
                }
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null value list provided, aborting change");
            }
        }
    }

    public void set(String type, Object value) {
        if(propertyMap.containsKey(type)){
            propertyMap.get(type).clear();
            propertyMap.get(type).add(value);
        }else{
            add(type,value);
        }
    }

    public void set(String type, List value) {
        if(propertyMap.containsKey(type)){
            propertyMap.get(type).clear();
            propertyMap.get(type).add(value);
        }else{
            add(type,value);
        }
    }
}


