/**
 * ParameterFactory
 * Created Jan 26, 2009 - 2:49:40 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.descriptors;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class ParameterFactory extends AbstractFactory<ParameterInternal> {

    private static ParameterFactory instance = null;

    static public ParameterFactory newInstance() {
        if (instance == null) {
            instance = new ParameterFactory();
        }
        return instance;
    }

    private ParameterFactory() {
        super();
        ParameterInternal name = new BasicParameter();
        name.setType("Name");
        name.setParameterClass(String.class);
        PropertyRestriction restriction = new PropertyRestriction();
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        restriction.setClassType(String.class);
        name.setRestrictions(restriction);
        name.add("GenericParameter");
        properties.add(name);
        
        name = new BasicParameter();
        name.setType("ParameterClass");
        name.setParameterClass(String.class);
        restriction = new PropertyRestriction();
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        restriction.setClassType(String.class);
        name.setRestrictions(restriction);
        name.add("BasicParameter");
        properties.add(name);

        name = new BasicParameter();
        name.setType("Class");
        name.setParameterClass(Class.class);
        restriction = new PropertyRestriction();
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        restriction.setClassType(Class.class);
        name.setRestrictions(restriction);
        name.add(String.class);
        properties.add(name);        
        
        name = new BasicParameter();
        name.setType("Description");
        name.setParameterClass(String.class);
        restriction = new PropertyRestriction();
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        restriction.setClassType(String.class);
        name.setRestrictions(restriction);
        name.add("");
        properties.add(name);

        properties.setDefaultRestriction(new PropertyRestriction());
        
        map.put("BasicParameter", new BasicParameter());
    }

    public ParameterInternal create(Properties props) {
        return create(null, null, null, props);
    }

    public ParameterInternal create(String type, Class classType) {
        return create(type, classType, null, properties);
    }

    public ParameterInternal create(String type, Class classType, String description) {
        return create(type, classType, description, properties);
    }

    // TODO: replace reflection with faster operation
    public ParameterInternal create(String type, Class classType, String description, Properties props) {
        if (type == null) {
            if ((props.get("Name") != null) && (props.get("Name").getParameterClass().getName().contentEquals("java.lang.String"))) {
                type = (String) props.get("Name").getValue().iterator().next();
            } else {
                type = (String) properties.get("Name").getValue().iterator().next();
            }
        }

        if (classType == null) {
            if ((props.get("Class") != null) && (props.get("Class").getParameterClass().getName().contentEquals("java.lang.Class"))) {
                classType = (Class) props.get("Class").getValue().iterator().next();
            } else {
                classType = (Class) properties.get("Class").getValue().iterator().next();
            }
        }

        String parameterType = (String) properties.get("ParameterClass").getValue().iterator().next();
        if ((props.get("ParameterClass") != null) && (props.get("ParameterClass").getParameterClass().getName().contentEquals("java.lang.String"))) {
            parameterType = (String) props.get("ParameterClass").getValue().iterator().next();
        }

        ParameterInternal ret = null;
        if (map.containsKey(parameterType)) {
            ret = map.get(parameterType);
        } else {
            ret = new BasicParameter();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Requested Parameter type '" + parameterType + "' does not exist");
        }
        ret.setType(type);
        ret.setParameterClass(classType);
        if (description == null) {
            if ((props.get("Name") != null) && (props.get("Name").getParameterClass().getName().contentEquals("java.lang.String"))) {
                description = (String) props.get("Name").getValue().iterator().next();
            } else {
                description = (String) properties.get("Name").getValue().iterator().next();
            }
        }
        ret.setDescription(description);

        return ret;
    }

    public Parameter getClassParameter(){
        return properties.get("ParameterClass");
    }
}
