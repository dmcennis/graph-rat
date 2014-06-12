/* * UserFactory.java * * Created on 1 May 2007, 17:03 * * copyright Daniel McEnnis - published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.actor;import java.util.Properties;/** * *  * Class for creating Actors. * @author Daniel McEnnis */public class ActorFactory {        static ActorFactory instance = null;        /**     * Return a singleton of this object, creating it if needed     * @return static singelton factory     */    public static ActorFactory newInstance(){        if(instance == null){            instance = new ActorFactory();        }        return instance;    }        /** Creates a new instance of UserFactory */    ActorFactory() {    }        /**     * Actors are created using the 'ActorClass' property to determine the class     * of the actor to use.     * <ul><li>'DBActor' - creates a DBActor Actor.     * <li>anything else - creates a BasicUser.     * </ul><br>     * <br>     * Parameters for all actors     * <ul><li>'ActorType' - type (mode) of the created Actor     * <li>'ActorID' - ID of the created Actor     * </ul>     *      * @param props parameters for Actors     * @return newly created Actor     */    public Actor create(Properties props){        Actor n = null;        if((props!=null)&&("DBActor".equalsIgnoreCase(props.getProperty("ActorClass")))){            n = new DBActor();            n.setID(props.getProperty("ActorID"));            if(props.getProperty("ActorType")!=null){                n.setType(props.getProperty("ActorType"));            }else{                n.setType("User");            }        }else if((props!=null)&&(props.getProperty("ActorType")!=null)){            n = new BasicUser(props.getProperty("ActorID"));            n.setType(props.getProperty("ActorType"));        }else{            n = new BasicUser(props.getProperty("ActorID"));            n.setType("User");        }        return n;    }    }