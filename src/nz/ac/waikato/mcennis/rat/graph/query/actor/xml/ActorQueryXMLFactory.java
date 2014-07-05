/**
 * ActorQueryXMLFactory
 * Created Jan 26, 2009 - 10:46:51 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.actor.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryXML;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorQueryXMLFactory extends AbstractFactory<ActorQueryXML>{

        static ActorQueryXMLFactory instance = null;
    
    static public ActorQueryXMLFactory newInstance(){
        if(instance == null){
            instance = new ActorQueryXMLFactory();
        }
        return instance;
    }
    
    private ActorQueryXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("ActorQueryXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("ActorByLink",new ActorByLinkXML());
        map.put("ActorByMode",new ActorByModeXML());
        map.put("ActorByProperty",new ActorByPropertyXML());
        map.put("AndQuery",new AndActorQueryXML());
        map.put("OrQuery",new OrActorQueryXML());
        map.put("XorQuery",new XorActorQueryXML());
        map.put("NullQuery",new NullActorQueryXML());
        
    }

    @Override
    public ActorQueryXML create(Properties props) {
        return create(null,props);
    }
    
    public ActorQueryXML create(String name){
        return create(name,properties);
    }
    
    public ActorQueryXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("ActorQueryXMLClass") != null) && (parameters.get("ActorQueryXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("ActorQueryXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("ActorQueryXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (ActorQueryXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming NullQueryXML<Actor>");
            return new NullActorQueryXML();
        }
    }
    
    public Parameter getClassParameter(){
        return properties.get("ActorQueryXMLClass");
    }
}
