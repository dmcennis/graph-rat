/**
 * GraphQueryXMLFactory
 * Created Jan 26, 2009 - 11:00:14 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.dynamicfactory.AbstractFactory;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import nz.ac.waikato.mcennis.rat.graph.query.GraphQueryXML;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphQueryXMLFactory extends AbstractFactory<GraphQueryXML>{

        static GraphQueryXMLFactory instance = null;
    
    static public GraphQueryXMLFactory newInstance(){
        if(instance == null){
            instance = new GraphQueryXMLFactory();
        }
        return instance;
    }
    
    private GraphQueryXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("GraphQueryXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("GraphByLink",new GraphByLinkXML());
        map.put("GraphByActor",new GraphByActorXML());
        map.put("GraphByProperty",new GraphByPropertyXML());
        map.put("GraphByID",new GraphByIDXML());
        map.put("GraphByLevel",new GraphByLevelXML());
        map.put("AndQuery",new AndGraphQueryXML());
        map.put("OrQuery",new OrGraphQueryXML());
        map.put("XorQuery",new XorGraphQueryXML());
        map.put("NullQuery",new NullGraphQueryXML());
        
    }

    @Override
    public GraphQueryXML create(Properties props) {
        return create(null,props);
    }
    
    public GraphQueryXML create(String name){
        return create(name,properties);
    }
    
    public GraphQueryXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("GraphQueryXMLClass") != null) && (parameters.get("GraphQueryXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("GraphQueryXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("GraphQueryXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (GraphQueryXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming NullQueryXML<Graph>");
            return new NullGraphQueryXML();
        }
    }
    public Parameter getClassParameter(){
        return properties.get("GraphQueryXMLClass");
    }
}
