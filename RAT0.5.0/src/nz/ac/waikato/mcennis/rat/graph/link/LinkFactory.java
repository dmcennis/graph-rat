/*

 * LinkFactory.java

 *

 * Created on 1 May 2007, 17:03

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.graph.link;



import java.util.HashMap;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesImplementation;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertyRestriction;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;



/**

 *


 * 

 * Class for generating Links from a Properties map.

 * @author Daniel McEnnis
 */

public class LinkFactory extends AbstractFactory<Link>{

    

    static LinkFactory instance = null;

 
    /**

     * Create a new reference to a singelton LinkFactory object

     * 

     * @return a reference to a LinkFactory

     */

    public static LinkFactory newInstance(){

        if(instance == null){

            instance = new LinkFactory();

        }

        return instance;

    }

    /** Creates a new instance of LinkFactory */

    protected LinkFactory() {
        ParameterInternal param = ParameterFactory.newInstance().create("LinkClass", String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("BasicUserLink");
        properties.add(param);

        param = ParameterFactory.newInstance().create("LinkRelation", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("ListensTo");
        properties.add(param);

        param = ParameterFactory.newInstance().create("DerbyDirectory", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("/tmp/");
        properties.add(param);

        param = ParameterFactory.newInstance().create("Database", String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("LiveJournal");
        properties.add(param);

        map.put("BasicUserLink", new BasicUserLink());
        map.put("DBLink",new DBLink());
    }

    

    /**

     * Creates a new Link based on the 'LinkClass' attribute.  Currently only BasicLink

     * is supported, so this value is ignored.

     * 

     * 'LinkType' sets the link type (relation) of this link before returning.

     * 

     * @param props map of properties for creating a map

     * @return a newly created Link

     */

    public Link create(String relation){
        return create(relation,properties);
    }
    
    public Link create(Properties props){
        return create(null,props);
    }
    
    public Link create(String relation, Properties parameters){
        String linkClass = "";
        if ((parameters.get("ActorClass") != null) && (parameters.get("ActorClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            linkClass = (String) parameters.get("ActorClass").getValue().iterator().next();
        } else {
            linkClass = (String) properties.get("ActorClass").getValue().iterator().next();
        }
        
        Link ret = null;
        if(map.containsKey(linkClass)){
            ret = map.get(linkClass).prototype();
        }else{
            ret = new BasicUserLink();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Link class '"+linkClass+"' not found - assuming BasicUserLink");
        }
        
        ret.init(parameters);
        
        if (relation == null) {
            if ((parameters.get("Relation") != null) && (parameters.get("Relation").getParameterClass().getName().contentEquals(String.class.getName()))) {
                relation = (String) parameters.get("Relation").getValue().iterator().next();
            } else {
                relation = (String) properties.get("Relation").getValue().iterator().next();
            }
        }
        ret.setRelation(relation);
        
        return ret;
    }

    public Parameter getClassParameter(){
        return properties.get("LinkClass");
    }
}

