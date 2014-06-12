/*

 * PropertyFactory.java

 *

 * Created on 1 May 2007, 17:03

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */
package nz.ac.waikato.mcennis.rat.graph.property;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.BasicParameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertyRestriction;

/**

 *

 * 

 * Class for creating Property objects

 * @author Daniel McEnnis

 */
public class PropertyFactory extends AbstractFactory<Property>{

    static PropertyFactory instance = null;

    /**
    
     * Create a reference to the singelton PropertyFactory
    
     * 
    
     * @return reference to the PropertyFactory
    
     */
    public static PropertyFactory newInstance() {

        if (instance == null) {

            instance = new PropertyFactory();

        }

        return instance;

    }

    /** Creates a new instance of PropertyFactory */
    public PropertyFactory() {
        ParameterInternal name = new BasicParameter();
        name.setType("PropertyClass");
        name.setParameterClass(String.class);
        PropertyRestriction restrictionPart = new PropertyRestriction();
        restrictionPart.setMinCount(1);
        restrictionPart.setMaxCount(1);
        restrictionPart.setClassType(String.class);
        name.setRestrictions(restrictionPart);
        name.add("BasicProperty");
        properties.add(name);

        name = new BasicParameter();
        name.setType("PropertyValueClass");
        name.setParameterClass(Class.class);
        restrictionPart = new PropertyRestriction();
        restrictionPart.setMinCount(1);
        restrictionPart.setMaxCount(1);
        restrictionPart.setClassType(Class.class);
        name.setRestrictions(restrictionPart);
        name.add(String.class);
        properties.add(name);

        name = new BasicParameter();
        name.setType("PropertyID");
        name.setParameterClass(Class.class);
        restrictionPart = new PropertyRestriction();
        restrictionPart.setMinCount(1);
        restrictionPart.setMaxCount(1);
        restrictionPart.setClassType(String.class);
        name.setRestrictions(restrictionPart);
        name.add("Default");
        properties.add(name);

        name = new BasicParameter();
        name.setType("GraphRoot");
        name.setParameterClass(Class.class);
        restrictionPart = new PropertyRestriction();
        restrictionPart.setMinCount(0);
        restrictionPart.setMaxCount(1);
        restrictionPart.setClassType(Graph.class);
        name.setRestrictions(restrictionPart);
        properties.add(name);
 
        properties.setDefaultRestriction(new PropertyRestriction());

        map.put("BasicProperty", new BasicProperty("Default", String.class));
    }

    public Property create(String id, Class objectType) {
        return create(id, objectType, properties);
    }
    
    public Property create(Properties props){
        return create(null,null,props);
    }
    

    public Property create(String id, Class objectType, Properties props) {
        if (id == null) {
            if ((props.get("Name") != null) && (props.get("Name").getParameterClass().getName().contentEquals("java.lang.String"))) {
                id = (String) props.get("Name").getValue().iterator().next();
            } else {
                id = (String) properties.get("Name").getValue().iterator().next();
            }
        }
        if (objectType == null) {
            objectType = (Class) properties.get("Class").getValue().iterator().next();
            if ((props.get("Class") != null) && (props.get("Class").getParameterClass().getName().contentEquals("java.lang.Class"))) {
                objectType = (Class) props.get("Class").getValue().iterator().next();
            }
        }
        Property ret = null;
        String parameterType = (String) properties.get("PropertyType").getValue().iterator().next();
        if ((props.get("PropertyType") != null) && (props.get("PropertyType").getParameterClass().getName().contentEquals("java.lang.String"))) {
            parameterType = (String) props.get("PropertyType").getValue().iterator().next();
        }
        if (map.containsKey(parameterType)) {
            ret = map.get(parameterType).duplicate();
        } else {
            ret = new BasicProperty(id, objectType);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unknown property type '" + parameterType + "' requested: using BasicProperty as the default");
        }
        return ret;
    }

    public Parameter getClassParameter(){
        return properties.get("PropertyClass");
    }
/**
    
     * Creates a new Property Object. 'PropertyID' is used to set
    
     * the id of the resulting property.  'PropertyClass' is used to set what
    
     * class of objects can be added as values. A new BasicProperty object with the
    
     * given id and given values class is returned.
    
     * 
    
     * @param props map of initialization properties
    
     * @return new Property object
    
     */
}

