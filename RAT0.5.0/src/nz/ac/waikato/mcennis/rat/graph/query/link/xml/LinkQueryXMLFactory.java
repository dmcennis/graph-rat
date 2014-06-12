/**
 * LinkQueryXMLFactory
 * Created Jan 26, 2009 - 11:06:22 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.link.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.AbstractFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.xml.AndQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.xml.NullQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.xml.OrQueryXML;
import nz.ac.waikato.mcennis.rat.graph.query.xml.XorQueryXML;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkQueryXMLFactory extends AbstractFactory<LinkQueryXML>{

        static LinkQueryXMLFactory instance = null;
    
    static public LinkQueryXMLFactory newInstance(){
        if(instance == null){
            instance = new LinkQueryXMLFactory();
        }
        return instance;
    }
    
    private LinkQueryXMLFactory(){
        ParameterInternal name = ParameterFactory.newInstance().create("LinkQueryXMLClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("NullQuery");
        properties.add(name);
        
        map.put("LinkByActor",new LinkByActorXML());
        map.put("LinkByRelation",new LinkByRelationXML());
        map.put("LinkByProperty",new LinkByPropertyXML());
        map.put("AndQuery",new AndLinkQueryXML());
        map.put("OrQuery",new OrLinkQueryXML());
        map.put("XorQuery",new XorLinkQueryXML());
        map.put("NullQuery",new NullLinkQueryXML());
    }

    @Override
    public LinkQueryXML create(Properties props) {
        return create(null,props);
    }
    
    public LinkQueryXML create(String name){
        return create(name,properties);
    }
    
    public LinkQueryXML create(String name, Properties parameters){
        if(name == null){
        if ((parameters.get("LinkQueryXMLClass") != null) && (parameters.get("LinkQueryXMLClass").getParameterClass().getName().contentEquals(String.class.getName()))) {
            name = (String) parameters.get("LinkQueryXMLClass").getValue().iterator().next();
        } else {
            name = (String) properties.get("LinkQueryXMLClass").getValue().iterator().next();
        }
        }

        if(map.containsKey(name)){
            return (LinkQueryXML)map.get(name).newCopy();
        }else{
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Distance class '"+name+"' not found - assuming NullQueryXML<Link>");
            return new NullLinkQueryXML();
        }
    }
    
    public Parameter getClassParameter(){
        return properties.get("LinkQueryXMLClass");
    }
}
