/**
 * PropertyQueryFactory
 * Created Jan 26, 2009 - 9:01:40 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.DataVectorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.NullPropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.NumericQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.StringQuery;
/**
 *
 * @author Daniel McEnnis
 */
public class PropertyQueryFactory extends AbstractFactory<PropertyQuery>{

    static PropertyQueryFactory instance = null;
    
    public static PropertyQueryFactory newInstance(){
        if(instance == null){
            instance = new PropertyQueryFactory();
        }
        return instance;
    }
    
    private PropertyQueryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("QueryClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("NullPropertyQuery");
        properties.add(name);
        
        map.put("DataVectorQuery",new DataVectorQuery());
        map.put("NullPropertyQuery",new NullPropertyQuery());
        map.put("NumericQuery",new NumericQuery());
        map.put("StringQuery",new StringQuery());
    }
    
    @Override
    public PropertyQuery create(Properties props) {
        return create(null,props);
    }
    
    public PropertyQuery create(String id){
        return create(id,properties);
    }
    
    public PropertyQuery create(String id, Properties parameters){
        String classType = "";
        if ((parameters.get("QueryClass") != null) && (parameters.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("QueryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("QueryClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"PropertyQuery class '"+classType+"' not found - assuming NullPropertyQuery");
            return new NullPropertyQuery();
        }
    }
    public Parameter getClassParameter(){
        return properties.get("QueryClass");
    }
}
