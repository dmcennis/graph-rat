/*

 * UserFactory.java

 *

 * Created on 1 May 2007, 17:03

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */
package nz.ac.waikato.mcennis.rat.graph.actor;

import nz.ac.waikato.mcennis.rat.AbstractFactory;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;

/**

 *


 * 

 * Class for creating Actors.

 * @author Daniel McEnnis
 */
public class ActorFactory extends AbstractFactory<Actor> {

    static ActorFactory instance = null;

    /**
    
     * Return a singleton of this object, creating it if needed
    
     * @return static singelton factory
    
     */
    public static ActorFactory newInstance() {

        if (instance == null) {

            instance = new ActorFactory();

        }

        return instance;

    }

    /** Creates a new instance of UserFactory */
    private ActorFactory() {
        ParameterInternal parameter = ParameterFactory.newInstance().create("ActorClass", String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        parameter.setRestrictions(restrictionPart);
        parameter.add("BasicUser");
        properties.add(parameter);

        parameter = ParameterFactory.newInstance().create("ActorMode", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        parameter.setRestrictions(restrictionPart);
        parameter.add("User");
        properties.add(parameter);

        parameter = ParameterFactory.newInstance().create("ActorID", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        parameter.setRestrictions(restrictionPart);
        parameter.add("Default");
        properties.add(parameter);

        parameter = ParameterFactory.newInstance().create("DerbyDirectory", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        parameter.setRestrictions(restrictionPart);
        parameter.add("/tmp/");
        properties.add(parameter);

        parameter = ParameterFactory.newInstance().create("Database", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        parameter.setRestrictions(restrictionPart);
        parameter.add("LiveJournal");
        properties.add(parameter);

        map.put("BasicActor", new BasicUser("Default"));
        map.put("DBActor", new DBActor());
    }

    public Actor create(String mode, String id) {
        return create(mode, id, properties);
    }

    public Actor create(String mode, String id, Properties parameters) {
        if (mode == null) {
            if ((parameters.get("ActorMode") != null) && (parameters.get("ActorMode").getParameterClass().getName().contentEquals(String.class.getName()))) {
                mode = (String) parameters.get("ActorMode").getValue().iterator().next();
            } else {
                mode = (String) properties.get("ActorMode").getValue().iterator().next();
            }
        }
        if (id == null) {
            if ((parameters.get("ActorID") != null) && (parameters.get("ActorID").getParameterClass().getName().contentEquals(String.class.getName()))) {
                id = (String) parameters.get("ActorID").getValue().iterator().next();
            } else {
                id = (String) properties.get("ActorID").getValue().iterator().next();
            }
        }
        String classType = "";
        if ((parameters.get("ActorClass") != null) && (parameters.get("ActorClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("ActorClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("ActorClass").getValue().iterator().next();
        }

        Actor ret = map.get(classType).prototype();
        ret.init(parameters);
        ret.setMode(mode);
        ret.setID(id);
        return ret;
    }

    public Actor create(Properties props) {
        return create(null, null, props);
    }

    public Parameter getClassParameter(){
        return properties.get("ActorClass");
    }
/**
    
     * Actors are created using the 'ActorClass' property to determine the class
    
     * of the actor to use.
    
     * <ul><li>'DBActor' - creates a DBActor Actor.
    
     * <li>anything else - creates a BasicUser.
    
     * </ul><br>
    
     * <br>
    
     * Parameters for all actors
    
     * <ul><li>'ActorType' - type (mode) of the created Actor
    
     * <li>'ActorID' - ID of the created Actor
    
     * </ul>
    
     * 
    
     * @param props parameters for Actors
    
     * @return newly created Actor
    
     */
}

