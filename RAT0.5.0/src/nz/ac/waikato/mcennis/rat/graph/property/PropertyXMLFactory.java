/*
 * PropertyXMLFactory - created 1/02/2009 - 10:47:49 PM
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
import nz.ac.waikato.mcennis.rat.graph.property.xml.BasicPropertyXML;
import nz.ac.waikato.mcennis.rat.graph.property.xml.PropertyXML;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyXMLFactory extends AbstractFactory<PropertyXML>{

   static PropertyXMLFactory instance = null;
   
   public static PropertyXMLFactory newInstance(){
       if(instance == null){
           instance = new PropertyXMLFactory();
       }
       return instance;
   }
   
   private PropertyXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("ValueClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("String");
        properties.add(name);

        map.put("BasicParameter",new BasicPropertyXML());
   }
    @Override
    public PropertyXML create(Properties props) {
        return create(null,props);
    }

    public PropertyXML create(String name){
        return create(name,properties);
    }

    public PropertyXML create(String name,Properties props){
        if((name != null)|| (properties.check(props))){
            if(name == null){
                if((props != null)&&(props.get("PropertyXMLClass")!=null)){
                    name = (String)props.get("PropertyXMLClass").get();
                }else{
                    name = (String)properties.get("PropertyXMLClass").get();
                }
            }
            if(map.containsKey(name)){
                return map.get(name);
            }else{
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Class type '"+name+"' does not exist - using BasicPropertyXML as a default.");
                return new BasicPropertyXML();
            }
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Null name provided without a replacement in the properties object - using BasicPropertyXML as a default.");
            return new BasicPropertyXML();
        }
    }

    @Override
    public Parameter getClassParameter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
