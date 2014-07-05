/**
 * ActorQueryFactory
 * Created Jan 26, 2009 - 8:29:21 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByLink;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByProperty;
import nz.ac.waikato.mcennis.rat.graph.query.actor.AndActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.NullActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.OrActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.XorActorQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorQueryFactory extends AbstractFactory<ActorQuery>{

    static ActorQueryFactory instance = null;
    
    public static ActorQueryFactory newInstance(){
        if(instance == null){
            instance = new ActorQueryFactory();
        }
        return instance;
    }
    
    private ActorQueryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("QueryClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("AndQuery",new AndActorQuery());
        map.put("NullQuery",new NullActorQuery());
        map.put("OrQuery",new OrActorQuery());
        map.put("XorQuery",new XorActorQuery());
        map.put("ActorByMode",new ActorByMode());
        map.put("ActorByLink",new ActorByLink());
        map.put("ActorByProperty",new ActorByProperty());
    }
    
    @Override
    public ActorQuery create(Properties props) {
        return create(null,props);
    }
    
    public ActorQuery create(String id){
        return create(id,properties);
    }
    
    public ActorQuery create(String id, Properties parameters){
        String classType = "";
        if ((parameters.get("QueryClass") != null) && (parameters.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("QueryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("QueryClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"ActoQuery class '"+classType+"' nbot found - assuming NullQuery<Actor>");
            return new NullActorQuery();
        }
    }

    public Parameter getClassParameter(){
        return properties.get("QueryClass");
    }
}
