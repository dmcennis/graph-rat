/**
 * SyntaxCheckerFactory
 * Created Jan 26, 2009 - 2:48:38 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.dynamicfactory.descriptors;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.query.property.NullPropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class SyntaxCheckerFactory extends AbstractFactory<SyntaxObject> {

    private static SyntaxCheckerFactory instance = null;

    static public SyntaxCheckerFactory newInstance() {
        if (instance == null) {
            instance = new SyntaxCheckerFactory();
        }
        return instance;
    }

    private SyntaxCheckerFactory() {
        ParameterInternal parameter = new BasicParameter();
        parameter.setParameterClass(String.class);
        parameter.setType("SyntaxCheckerClass");
        PropertyRestriction restriction = new PropertyRestriction();
        restriction.setClassType(String.class);
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        parameter.setRestrictions(restriction);
        parameter.add("PropertyRestriction");
        properties.add(parameter);

        parameter = new BasicParameter();
        parameter.setParameterClass(Class.class);
        parameter.setType("ClassType");
        restriction = new PropertyRestriction();
        restriction.setClassType(Class.class);
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        parameter.setRestrictions(restriction);
        parameter.add(Object.class);
        properties.add(parameter);

        parameter = new BasicParameter();
        parameter.setParameterClass(Integer.class);
        parameter.setType("MinCount");
        restriction = new PropertyRestriction();
        restriction.setClassType(Integer.class);
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        parameter.setRestrictions(restriction);
        parameter.add(new Integer(0));
        properties.add(parameter);

        parameter = new BasicParameter();
        parameter.setParameterClass(Integer.class);
        parameter.setType("MaxCount");
        restriction = new PropertyRestriction();
        restriction.setClassType(Integer.class);
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        parameter.setRestrictions(restriction);
        parameter.add(Integer.MAX_VALUE);
        properties.add(parameter);

        parameter = new BasicParameter();
        parameter.setParameterClass(PropertyQuery.class);
        parameter.setType("Test");
        restriction = new PropertyRestriction();
        restriction.setClassType(PropertyQuery.class);
        restriction.setMinCount(1);
        restriction.setMaxCount(1);
        parameter.setRestrictions(restriction);
        NullPropertyQuery query = new NullPropertyQuery();
        query.buildQuery(true);
        parameter.add(query);
        properties.add(parameter);

        properties.setDefaultRestriction(new PropertyRestriction());

        map.put("PropertyRestriction", new PropertyRestriction());
    }

    public SyntaxObject create(int minCount, int maxCount, PropertyQuery query, Class classType) {
        return create(minCount, maxCount, query, classType, properties);
    }

    public SyntaxObject create(int minCount, int maxCount, PropertyQuery query, Class classType, Properties props) {
        String type = (String) properties.get("SyntaxCheckerClass").getValue().iterator().next();
        if ((props.get("SyntaxCheckerClass") != null) && (props.get("SyntaxCheckerClass").getParameterClass().getName().contentEquals("java.lang.String"))) {
            type = (String) props.get("SyntaxCheckerClass").getValue().iterator().next();
        }

        SyntaxObject prototype = map.get(type).duplicate();
        if(prototype == null){
            prototype = new PropertyRestriction();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Unknown SyntaxObject '"+type+"' requested - assuming PropertyRestriction");
        }
        
        if (query == null) {
            query = (PropertyQuery) properties.get("Test").getValue().iterator().next();
            if ((props.get("Test") != null) && (PropertyQuery.class.isAssignableFrom(props.get("Test").getParameterClass()))) {
                query = (PropertyQuery) props.get("Test").getValue().iterator().next();
            }
        }
        prototype.setTest(query);

        if (classType == null) {
            classType = (Class) properties.get("ClassType").getValue().iterator().next();
            if ((props.get("ClassType") != null) && (props.get("ClassType").getParameterClass().getName().contentEquals("java.lang.Class"))) {
                classType = (Class) props.get("ClassType").getValue().iterator().next();
            }
        }
        prototype.setClassType(classType);
        prototype.setMinCount(minCount);
        prototype.setMaxCount(maxCount);

        return prototype;

    }

    @Override
    public SyntaxObject create(Properties props) {
        int minCount = (Integer) properties.get("MinCount").getValue().iterator().next();
        if ((props.get("MinCount") != null) && (props.get("MinCount").getParameterClass().getName().contentEquals("java.lang.Integer"))) {
            minCount = (Integer) props.get("MinCount").getValue().iterator().next();
        }

        int maxCount = (Integer) properties.get("MaxCount").getValue().iterator().next();
        if ((props.get("MaxCount") != null) && (props.get("MaxCount").getParameterClass().getName().contentEquals("java.lang.Integer"))) {
            maxCount = (Integer) props.get("MaxCount").getValue().iterator().next();
        }
        return create(minCount, maxCount, null, null, props);
    }

    public Parameter getClassParameter(){
        return properties.get("SyntaxCheckerClass");
    }
}
