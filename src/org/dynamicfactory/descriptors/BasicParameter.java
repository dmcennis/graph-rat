/*
 * BasicParameter.java
 *
 * Created on 14 September 2007, 13:55
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.dynamicfactory.descriptors;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

/**
 *
 * 
 * Base implementation of all the parameter interfaces.
 * @author Daniel McEnnis
 */
public class BasicParameter implements ParameterInternal {

    private SyntaxObject restrictions;
    private boolean structural;
    private String description;
    private Property value;

    /** Creates a new instance of BasicParameter */
    public BasicParameter() {
        value = PropertyFactory.newInstance().create("Default", Object.class);
    }

    @Override
    public void setStructural(boolean b) {

        structural = b;

    }

    @Override
    public boolean isStructural() {

        return structural;

    }

    @Override
    public void set(Property o) {

        if (o.getType().contentEquals(value.getType()) && o.getPropertyClass().equals(value.getPropertyClass())) {
            if (restrictions.check(o)) {
                value = o;
            }
        }

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        if (d != null) {
            description = d;
        } else {
            Logger.getLogger(d).log(Level.WARNING, "Null description passed to parameter " + value.getType());
        }
    }

    public void setType(String name) {
        value.setType(name);
    }

    public void setRestrictions(SyntaxObject syntax) {
        restrictions = syntax;
    }

    public SyntaxObject getRestrictions() {
        return restrictions;
    }

    public void add(Object o) {
        if (restrictions.check(value, o)) {
            try {
                value.add(o);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(BasicParameter.class.getName()).log(Level.SEVERE, "Uncaught Invalid object type after cleared by syntax checker", ex);
            }
        }
    }

    public String getType() {
        return value.getType();
    }

    public List<Object> getValue() {
        return value.getValue();
    }

    public Class getParameterClass() {
        return value.getPropertyClass();
    }

    public void setParameterClass(Class type) {
        value.setClass(type);
    }

    public boolean check(Property property) {
        return restrictions.check(property);
    }

    public boolean check(String type, Object value) {
        if (this.value.getType().contentEquals(type)) {
            return restrictions.check(this.value, value);
        }
        return false;
    }

    public boolean check(Parameter type) {
        if (this.value.getType().contentEquals(type.getType())) {
            return restrictions.check(type);
        } else {
            return false;
        }
    }

    public void clear() {
        String type = value.getType();
        Class classType = value.getPropertyClass();
        value = PropertyFactory.newInstance().create(type, classType);
    }

    public void add(List value) {
        if (restrictions.check(this.value, value)) {
            Iterator it = value.iterator();
            try {
                while (it.hasNext()) {
                    this.value.add(it.next());
                }
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(BasicParameter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean check(String type, List value) {
        return restrictions.check(type,value);
    }
    
    public ParameterInternal duplicate(){
        BasicParameter ret = new BasicParameter();
        ret.value = PropertyFactory.newInstance().create(this.value.getType(),this.value.getPropertyClass());
        Iterator it  = this.value.getValue().iterator();
        while(it.hasNext()){
            try {
                ret.value.add(it.next());
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(BasicParameter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ret.structural = this.structural;
        ret.description = this.description;
        ret.restrictions = this.restrictions.duplicate();
        return ret;
    }
    
    public Object get(){
        if(this.value.getValue().size()>0){
            return this.value.getValue().get(0);
        }
        return null;
    }

    public void set(Object value) {
        this.clear();
        this.add(value);
    }

    public void set(List value) {
        this.clear();
        this.add(value);
    }
}

