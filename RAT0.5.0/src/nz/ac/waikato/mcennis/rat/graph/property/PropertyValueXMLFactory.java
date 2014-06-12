/**
 * PropertyValueXMLFactory
 * Created Jan 26, 2009 - 9:22:39 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.property;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.property.xml.AssociativeMiningItemsXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.AttributeXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.DoubleXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.FileXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.InstanceXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.InstancesXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.IntegerXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.LongXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.PropertyValueXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.StringXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.URLXML;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyValueXMLFactory extends AbstractFactory<PropertyValueXML>{
    static PropertyValueXMLFactory instance = null;

    public static PropertyValueXMLFactory newInstance(){
        if(instance == null){
            instance = new PropertyValueXMLFactory();
        }
        return instance;
    }
    
    private PropertyValueXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("ValueClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("String");
        properties.add(name);
        
        map.put("AssociativeMiningItems",new AssociativeMiningItemsXML());
        map.put("Attribute",new AttributeXML());
        map.put("Double",new DoubleXML());
        map.put("File",new FileXML());
        map.put("Instance",new InstanceXML());
        map.put("Instances",new InstancesXML());
        map.put("Integer",new IntegerXML());
        map.put("Long",new LongXML());
        map.put("String",new StringXML());
        map.put("URL",new URLXML());
    }
    
    public PropertyValueXML create(Class classType){
        return create(classType.getSimpleName(),properties);
    }
    
    public PropertyValueXML create(String classType){
        return create(classType,properties);
    }
    
    public PropertyValueXML create(String classType, Properties props){
        if(classType==null){
        if ((props.get("ValueClass") != null) && (props.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) props.get("ValueClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("ValueClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(classType)){
            return map.get(classType).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"PropertyXML class '"+classType+"' not found - assuming StringXML");
            return new StringXML();
        }
    }

    @Override
    public PropertyValueXML create(Properties props) {
        return create(null,props);
    }

    public Parameter getClassParameter(){
        return properties.get("ValueClass");
    }
}
