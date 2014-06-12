/**
 * LinkQueryFactory
 * Created Jan 26, 2009 - 8:52:23 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.query.link.AndLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByActor;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByProperty;
import nz.ac.waikato.mcennis.rat.graph.query.link.NullLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.OrLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.XorLinkQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkQueryFactory extends AbstractFactory<LinkQuery>{

    static LinkQueryFactory instance = null;
    
    public static LinkQueryFactory newInstance(){
        if(instance == null){
            instance = new LinkQueryFactory();
        }
        return instance;
    }
    
    private LinkQueryFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("QueryClass", String.class);
        SyntaxObject check = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(check);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("AndQuery",new AndLinkQuery());
        map.put("NullQuery",new NullLinkQuery());
        map.put("OrQuery",new OrLinkQuery());
        map.put("XorQuery",new XorLinkQuery());
        map.put("LinkByRelation",new LinkByRelation());
        map.put("LinkByActor",new LinkByActor());
        map.put("LinkByProperty",new LinkByProperty());
    }
    
    @Override
    public LinkQuery create(Properties props) {
        return create(null,props);
    }
    
    public LinkQuery create(String id){
        return create(id,properties);
    }
    
    public LinkQuery create(String id, Properties parameters){
        String classType = "";
        if ((parameters.get("QueryClass") != null) && (parameters.get("QueryClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            classType = (String) parameters.get("QueryClass").getValue().iterator().next();
        } else {
            classType = (String) properties.get("QueryClass").getValue().iterator().next();
        }

        if(map.containsKey(classType)){
            return map.get(classType).prototype();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"LinkQuery class '"+classType+"' not found - assuming NullQuery<Link>");
            return new NullLinkQuery();
        }
    }

    public Parameter getClassParameter(){
        return properties.get("QueryClass");
    }
}
