/*
 * MemGraph.java
 *
 * Created on 1 May 2007, 15:01
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Iterator;
import java.util.LinkedList;

import nz.ac.waikato.mcennis.rat.graph.link.Link;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.path.PathSet;
import nz.ac.waikato.mcennis.rat.graph.path.Path;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyXMLFactory;
import org.dynamicfactory.property.xml.PropertyXML;

/**
 * A graph object that is held entirely in memory
 *
 * @see nz.ac.waikato.mcennis.arm.graph.Graph
 *
 * @author Daniel McEnnis
 *
 */
public class MemGraph extends ModelShell implements Graph, java.io.Serializable {

    static final long serialVersionUID = 6;
    /**
     * Map of actor ids to user objects by type of actor
     */
    TreeMap<String, TreeMap<String, Actor>> actor;
    /**
     * three level object.  First level is a TreeMap of userlinks based on type.
     * Second level TreeMap is of source user ids.  The third level is a vector
     * of all userlinks that map to the same type and source user.
     *
     * It is assumed that types will enforce their own restrictions on the type
     * of each actor in the link.
     */
    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>>> links;
    TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>>> invertedLinks;

//    GraphIndex index = new GraphIndex();
    PropertiesInternal parameters = PropertiesFactory.newInstance().create();
    TreeMap<String, Property> properties = new TreeMap<String, Property>();
    TreeMap<String, PathSet> pathSets;
    Graph parent = null;
    LinkedList<Graph> children = new LinkedList<Graph>();
    String name;
    UserIDList mapped = null;
    Writer outs = null;
    boolean gzip = true;
    ParameterInternal[] parameter = new ParameterInternal[3];

    /** Creates a new instance of MemGraph */
    public MemGraph() {
        actor = new TreeMap<String, TreeMap<String, Actor>>();
        links = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>>>();
        invertedLinks = new TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>>>();
        ParameterInternal param = ParameterFactory.newInstance().create("GraphID", String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("Root");
        parameters.add(param);

        param = ParameterFactory.newInstance().create("GraphOutput", Writer.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(0, 1, null, Writer.class);
        param.setRestrictions(restrictionPart);
        parameters.add(param);

        param = ParameterFactory.newInstance().create("Compression", Boolean.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        param.setRestrictions(restrictionPart);
        param.add(true);
        parameters.add(param);


        pathSets = new TreeMap<String, PathSet>();
        init(null);
    }

    @Override
    public void add(Link link) {
        if ((mapped == null) || (mapped.getActor(link.getDestination().getMode(), link.getDestination().getID()) != null)) {
            String type = link.getRelation();
            Actor source = link.getSource();
            Actor dest = link.getDestination();
            //            if((source != null)&&(dest != null)){
            if (!links.containsKey(type)) {
                links.put(type, new TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>>());
            }
            if (!invertedLinks.containsKey(type)) {
                invertedLinks.put(type, new TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>>());
            }
            TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>> typeLink = links.get(type);
            TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>> typeInvertedLink = invertedLinks.get(type);
            if (!typeLink.containsKey(source.getMode())) {
                typeLink.put(source.getMode(), new TreeMap<String, TreeMap<Actor,Link>>());
            }
            if (!typeInvertedLink.containsKey(dest.getMode())) {
                typeInvertedLink.put(dest.getMode(), new TreeMap<String, TreeMap<Actor,Link>>());
            }
            TreeMap<String, TreeMap<Actor,Link>> idLink = typeLink.get(source.getMode());
            TreeMap<String, TreeMap<Actor,Link>> idInvertedLink = typeInvertedLink.get(dest.getMode());
            if (!idLink.containsKey(source.getID())) {
                idLink.put(source.getID(), new TreeMap<Actor,Link>());
            }
            if (!idInvertedLink.containsKey(dest.getID())) {
                idInvertedLink.put(dest.getID(), new TreeMap<Actor,Link>());
            }
            idLink.get(source.getID()).put(link.getDestination(),link);
            idInvertedLink.get(dest.getID()).put(link.getSource(),link);
            if (parent != null) {
                parent.add(link);
            }
        }
    }

    @Override
    public void add(Actor u) {
        if ((mapped == null) || (mapped.getActor(u.getMode(), u.getID()) != null)) {
            if (!actor.containsKey(u.getMode())) {
                actor.put(u.getMode(), new TreeMap<String, Actor>());
            }
            TreeMap<String, Actor> entry = actor.get(u.getMode());
            entry.put(u.getID(), u);
            if (parent != null) {
                parent.add(u);
            }
        }
//        index.addActor(u, this);
    }

    @Override
    public Actor getActor(String type, String ID) {
        if (actor.get(type) != null) {
            return actor.get(type).get(ID);
        } else {
            return null;
        }
    }

    @Override
    public List<Actor> getActor(String type) {
        TreeMap<String, Actor> actorClass = actor.get(type);
        LinkedList<Actor> ret = new LinkedList<Actor>();
        ret.addAll(actor.get(type).values());
        return ret;
    }

    /**
     * Serializes MemGraphs to an XML file
     * @param output output stream to serialize into
     * @param isInnerGraph is this the root graph (requiring headers) or an inner
     * graph.
     * @throws java.io.IOException
     */
    public void outputXML(Writer output, boolean isInnerGraph) throws IOException {
        if (!isInnerGraph) {/*
            output.append("<?xml version=\"1.0\"?>\n");
            //            output.append("<!DOCTYPE dataObject [\n");
            //            output.append("  <!ELEMENT dataObject (graph)>\n");
            //            output.append("  <!ELEMENT graph (graphClass,graphName,graphProperties*,pathSet*,user*,userLink*,graph*)>\n");
            //            output.append("  <!ELEMENT graphClass (#PCDATA)>\n");
            //            output.append("  <!ELEMENT graphName (#PCDATA)>\n");
            //            output.append("  <!ELEMENT graphProperties (gClass,gValueClass,gType,gValue+)>\n");
            //            output.append("  <!ELEMENT gClass (#PCDATA)>\n");
            //            output.append("  <!ELEMENT gValueClass (#PCDATA)>\n");
            //            output.append("  <!ELEMENT gType (#PCDATA)>\n");
            //            output.append("  <!ELEMENT gValue (#PCDATA)>\n");
            //            output.append("  <!ELEMENT pathSet (path+)>\n");
            //            output.append("  <!ELEMENT path (actor+)>\n");
            //            output.append("  <!ELEMENT actor (actorType,actorID)>\n");
            //            output.append("  <!ELEMENT actorType (#PCDATA)>\n");
            //            output.append("  <!ELEMENT actorID (#PCDATA)>\n");
            //            output.append("  <!ELEMENT user (userClass,ID,properties*,page*)>\n");
            //            output.append("  <!ELEMENT ID (#PCDATA)>\n");
            //            output.append("  <!ELEMENT properties (propertiesClass,valueClass,type,value*)>\n");
            //            output.append("  <!ELEMENT type (#PCDATA)>\n");
            //            output.append("  <!ELEMENT value (#PCDATA)>\n");
            //            output.append("  <!ELEMENT valueClass (#PCDATA)>\n");
            //            output.append("  <!ELEMENT userLink (uClass,uStrength,uSourceType,uSourceID,uDestinationType,uDestinationID,uProperties)>\n");
            //            output.append("  <!ELEMENT uClass (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uSourceType (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uSourceID (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uStrength (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uDestinationType (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uDestinationID (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uProperties (uPropertiesClass,uValueClass,uPropertiesType,uPropertiesValue+)>\n");
            //            output.append("  <!ELEMENT uPropertiesClass (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uPropertiesValueClass (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uPropertiesType (#PCDATA)>\n");
            //            output.append("  <!ELEMENT uPropertiesValue (#PCDATA)>\n");
            //            output.append("  <!ELEMENT page (#PCDATA)>\n");
            //            output.append("]>\n");
            output.append("\n");*/
            output.append("<DataObject>\n");
        }
        output.append("<Graph>\n");
        output.append("\t<ID>").append(this.name).append("</ID>\n");

        Iterator<Property> graphPropertyIt = this.getPropertyIterator();
        while (graphPropertyIt.hasNext()) {
            Property prop = graphPropertyIt.next();
            PropertyXML propertyXML = PropertyXMLFactory.newInstance().create(prop.getClass().getSimpleName());
            propertyXML.setProperty(prop);
            propertyXML.export(output);
        }

        Iterator<PathSet> pathSet = this.getPathSetIterator();
        while (pathSet.hasNext()) {
            output.append("\t<PathSet>\n");
            Path[] paths = pathSet.next().getPath();
            for (int j = 0; j < paths.length; ++j) {
                output.append("\t\t<Path>\n");
                Actor[] actor = paths[j].getPath();
                for (int k = 0; k < actor.length; ++k) {
                    output.append("\t\t\t<Actor>\n");
                    output.append("\t\t\t\t<Mode>").append(actor[k].getMode()).append("</Mode>\n");
                    output.append("\t\t\t\t<ID>").append(escapeXML(actor[k].getID())).append("</ID>\n");
                    output.append("\t\t\t</Actor>\n");
                }
                output.append("\t\t</Path>\n");
            }
            output.append("\t</PathSet>\n");
        }

        Iterator<Actor> user = this.getActorIterator();
        while (user.hasNext()) {
            Actor u = user.next();
            output.append("\t<Actor>\n");
            output.append("\t\t<Mode>").append(u.getMode()).append("</Mode>\n");
            output.append("\t\t<ID>").append(escapeXML(u.getID())).append("</ID>").append("\n");
            if (!isInnerGraph) {
                List<Property> p = u.getProperty();
                Iterator<Property> props = p.iterator();
                while (props.hasNext()) {
                    Property property = props.next();
                    PropertyXML propertyXML = PropertyXMLFactory.newInstance().create(props.getClass().getSimpleName());
                    propertyXML.setProperty(property);
                    propertyXML.export(output);
                }
            }
            output.append("\t</Actor>\n");
        }

        Iterator<Link> userLinksIt = this.getLinkIterator();
        while (userLinksIt.hasNext()) {
            Link userLinks = userLinksIt.next();
            output.append("\t<Link>\n");
            output.append("\t\t<Relation>").append(escapeXML(userLinks.getRelation())).append("</Relation>\n");
            output.append("\t\t<Strength>").append(Double.toString(userLinks.getStrength())).append("</Strength>\n");
            output.append("\t\t<SourceType>").append(userLinks.getSource().getMode()).append("</SourceType>\n");
            output.append("\t\t<SourceID>").append(escapeXML(userLinks.getSource().getID())).append("</SourceID>\n");
            output.append("\t\t<DestinationType>").append(userLinks.getDestination().getMode()).append("</DestinationType>\n");
            output.append("\t\t<DestinationID>").append(escapeXML(userLinks.getDestination().getID())).append("</DestinationID>\n");
            if (!isInnerGraph) {
                List<Property> propertiesArray = userLinks.getProperty();
                Iterator<Property> p = propertiesArray.iterator();
                while (p.hasNext()) {
                    Property prop = p.next();
                    PropertyXML propertyXML = PropertyXMLFactory.newInstance().create(prop.getClass().getSimpleName());
                    propertyXML.setProperty(prop);
                    propertyXML.export(output);
                }
            }
            output.append("\t</Link>\n");
        }

        for (Graph i : children) {
            ((MemGraph) i).outputXML(output, true);
        }

        output.append("</Graph>\n");
        if (!isInnerGraph) {
            output.append("</DataObject>\n");
        }
    }

    @Override
    public List<Actor> getActor() {
        Vector<Actor> ret = new Vector<Actor>();
        Iterator<TreeMap<String, Actor>> it = actor.values().iterator();
        while (it.hasNext()) {
            ret.addAll(it.next().values());
        }
        return ret;
    }
    //    public void transfer(MemGraph mG){
    //        user = new java.util.TreeMap<String,User>();
    //        User[] mgUser = mG.getUser();
    //        for(int i=0;i<mgUser.length;++i){
    //            BasicUser u = new BasicUser(mgUser[i].getID());
    //            u.load((BasicUser)mgUser[i]);
    //            user.put(u.getID(),u);
    //        }
    //        artist = mG.artist;
    //        userLinks = mG.userLinks;
    //        artistLinks = mG.artistLinks;
    //        algorithm = mG.algorithm;
    //    

    @Override
    public List<Link> getLinkBySource(String type, Actor source) {
        if (links.containsKey(type) && links.get(type).containsKey(source.getMode()) && (links.get(type).get(source.getMode()).containsKey(source.getID()))) {
            LinkedList<Link> ret = new LinkedList<Link>();
            ret.addAll(links.get(type).get(source.getMode()).get(source.getID()).values());
            return ret;
        } else {
            return new LinkedList<Link>();
        }
    }

    @Override
    public List<Link> getLinkByDestination(String type, Actor dest) {
        if (invertedLinks.containsKey(type) && invertedLinks.get(type).containsKey(dest.getMode()) && (invertedLinks.get(type).get(dest.getMode()).containsKey(dest.getID()))) {
            LinkedList<Link> ret = new LinkedList<Link>();
            ret.addAll(invertedLinks.get(type).get(dest.getMode()).get(dest.getID()).values());
            return ret;
        } else {
            return new LinkedList<Link>();
        }
    }

    @Override
    public List<Link> getLink(String type, Actor source, Actor dest) {
        if (links.containsKey(type) && links.get(type).containsKey(source.getMode()) && links.get(type).get(source.getMode()).containsKey(source.getID())) {
            TreeMap<Actor,Link> base = links.get(type).get(source.getMode()).get(source.getID());
            Vector<Link> ret = new Vector<Link>(base.size());
            ret.add(base.get(dest));
            return ret;
        } else {
            return new LinkedList<Link>();
        }
    }

    @Override
    public List<Link> getLink() {
        Vector<Link> ret = new Vector<Link>();
        Iterator<TreeMap<String, TreeMap<String, TreeMap<Actor,Link>>>> it = links.values().iterator();
        while (it.hasNext()) {
            Iterator<TreeMap<String, TreeMap<Actor,Link>>> type = it.next().values().iterator();
            while (type.hasNext()) {
                Iterator<TreeMap<Actor,Link>> entry = type.next().values().iterator();
                while (entry.hasNext()) {
                    ret.addAll(entry.next().values());
                }
            }
        }
        return ret;
    }

    @Override
    public List<Link> getLink(String type) {
        if (links.containsKey(type)) {
            Iterator<TreeMap<String, TreeMap<Actor,Link>>> typeLevel = links.get(type).values().iterator();
            Vector<Link> ret = new Vector<Link>();
            while (typeLevel.hasNext()) {
                Iterator<TreeMap<Actor,Link>> idLevel = typeLevel.next().values().iterator();
                while (idLevel.hasNext()) {
                    ret.addAll(idLevel.next().values());
                }
            }
            return ret;
        } else {
            return new LinkedList<Link>();
        }
    }

    @Override
    public void remove(Actor u) {
        TreeMap<String, Actor> a = actor.get(u.getMode());
        a.remove(u.getID());
        Iterator<String> sourceLinkType = links.keySet().iterator();
        Iterator<String> destLinkType = invertedLinks.keySet().iterator();
        Vector<String> linkSourceType = new Vector<String>();
        Vector<String> linkDestType = new Vector<String>();
        Vector<String> sourceIDs = new Vector<String>();
        Vector<String> destIDs = new Vector<String>();
        Vector<String> sourceTypes = new Vector<String>();
        Vector<String> destTypes = new Vector<String>();

        //
        // Remove indexed-by-source links where id is source
        // identify all vectors/types for inverted-indexed links
        //
        while (sourceLinkType.hasNext()) {
            String sourceLinkKey = sourceLinkType.next();
            if (links.get(sourceLinkKey).containsKey(u.getMode()) && (links.get(sourceLinkKey).get(u.getMode()).containsKey(u.getID()))) {
                TreeMap<String, TreeMap<Actor,Link>> forward = links.get(sourceLinkKey).get(u.getMode());
                TreeMap<Actor,Link> vectForward = forward.get(u.getID());
                for (int i = 0; i < vectForward.size(); ++i) {
                    //                    System.out.println("Forward: " + key + " - "+ vectForward.get(i).getDestination().getID());
                    linkDestType.add(sourceLinkKey);
                    destTypes.add(vectForward.get(i).getDestination().getMode());
                    destIDs.add(vectForward.get(i).getDestination().getID());
                }
                links.get(sourceLinkKey).get(u.getMode()).remove(u.getID());

            }
        }

        //
        // Remove indexed-by-destination links where id is source
        // identify all vector/types for source-indexed links
        //
        while (destLinkType.hasNext()) {
            String key = destLinkType.next();
            if ((invertedLinks.get(key).containsKey(u.getMode())) && (invertedLinks.get(key).get(u.getMode()).containsKey(u.getID()))) {
                TreeMap<String, TreeMap<Actor,Link>> backward = invertedLinks.get(key).get(u.getMode());
                TreeMap<Actor,Link> vectBackward = backward.get(u.getID());
                for (int i = 0; i < vectBackward.size(); ++i) {
                    //                    System.out.println("Backward: "+key + " - "+ vectBackward.get(i).getSource().getID());
                    linkSourceType.add(key);
                    sourceTypes.add(vectBackward.get(i).getSource().getMode());
                    sourceIDs.add(vectBackward.get(i).getSource().getID());
                }
                invertedLinks.get(key).get(u.getMode()).remove(u.getID());
            }
        }

        //
        // Remove all destination-indexed links with this actor as source
        //
        for (int i = 0; i < destIDs.size(); ++i) {
            TreeMap<String, TreeMap<Actor,Link>> map = invertedLinks.get(linkDestType.get(i)).get(destTypes.get(i));
            map.get(destIDs.get(i)).remove(u);
        }

        //
        // Remove all source-indexed links with this actor as destination
        //
        for (int i = 0; i < sourceIDs.size(); ++i) {
            String type = sourceTypes.get(i);
            TreeMap<String, TreeMap<Actor,Link>> map = links.get(linkSourceType.get(i)).get(sourceTypes.get(i));
            map.get(sourceIDs.get(i)).remove(u);
        }
    }
    /*
     *
     * Remove a link.  Each link is listed twice - by source and by destination
     */

    public void remove(Link ul) {
        links.get(ul.getRelation()).get(ul.getSource().getMode()).get(ul.getSource().getID()).remove(ul.getDestination());
        invertedLinks.get(ul.getRelation()).get(ul.getDestination().getMode()).get(ul.getDestination().getID()).remove(ul.getSource());
    }

    @Override
    public List<Property> getProperty() {
        LinkedList<Property> ret = new LinkedList<Property>();
        ret.addAll(properties.values());
        return ret;
    }

    @Override
    public Property getProperty(String type) {
        return properties.get(type);
    }

    @Override
    public void add(Property prop) {
        properties.put(prop.getType(), prop);
    }

    @Override
    public List<PathSet> getPathSet() {
        LinkedList<PathSet> ret = new LinkedList<PathSet>();
        ret.addAll(pathSets.values());
        return ret;
    }

    @Override
    public void add(PathSet pathSet) {
        pathSets.put(pathSet.getType(), pathSet);
    }

    @Override
    public PathSet getPathSet(String id) {
        return pathSets.get(id);
    }

    @Override
    public List<String> getLinkTypes() {
        LinkedList<String> ret = new LinkedList<String>();
        ret.addAll(links.keySet());
        return ret;
    }

    @Override
    public List<String> getActorTypes() {
        LinkedList<String> ret = new LinkedList<String>();
        ret.addAll(actor.keySet());
        return ret;
    }

    @Override
    public void setID(String id) {
        name = id;
    }

    @Override
    public String getID() {
        return name;
    }

    @Override
    public Iterator<Actor> getActorIterator(String type) {
        TreeMap<String, Actor> map = actor.get(type);
        if (map != null) {
            LinkedList<Actor> ret = new LinkedList<Actor>();
            ret.addAll(map.values());
            return ret.iterator();
        } else {
            return null;
        }
    }

    @Override
    public void add(Graph uid) {
        if (uid instanceof UserIDList) {
            mapped = (UserIDList) uid;
        } else if (uid == null) {
            mapped = null;
        }
    }

    @Override
    public void commit() {
        ;//intentionally null
    }

    @Override
    public void close() {
        if (outs != null) {
            try {
                System.out.println("Writing Graph");
                outputXML(outs, false);
                outs.flush();
                outs.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Set the output stream to serialize into when closed
     * @param w target output stream
     */
    public void setWriter(Writer w) {
        outs = w;
    }

    /**
     * Encode the string so that it is XML safe
     * @param base string to encode
     * @return escaped string
     */
    public String escapeXML(String base) {
        String ret = base.replaceAll("&", "&amp;");
        ret = ret.replaceAll("<", "&lt;");
        ret = ret.replaceAll(">", "&gt;");
        ret = ret.replaceAll("[^a-zA-Z0-9-!\"#$%&'()*+,./:;<=>?@\\[\\]\\^_`{|}~\\s]", "");
        return ret;
    }

    @Override
    public void anonymize() {
        List<Actor> a = new LinkedList<Actor>();
        a.addAll(this.getActor());
        Iterator<Actor> aIt = a.iterator();
        int count = 0;
        while (aIt.hasNext()) {
            aIt.next().setID(Integer.toString(count++));
        }
        List<Link> l = new LinkedList<Link>();
        l.addAll(this.getLink());
        this.actor.clear();
        this.links.clear();
        this.invertedLinks.clear();
        aIt = a.iterator();
        while (aIt.hasNext()) {
            this.add(aIt.next());
        }
        Iterator<Link> lIt = l.iterator();
        while (lIt.hasNext()) {
            this.add(lIt.next());
        }
    }

    @Override
    public Graph getParent() {
        return parent;
    }

    @Override
    public List<Graph> getChildren() {
        return children;
    }

    @Override
    public Graph getChildren(String id) {
        Iterator<Graph> it = children.iterator();
        while (it.hasNext()) {
            Graph child = it.next();
            if (child.getID().contentEquals(id)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public void addChild(Graph g) {
        children.add(g);
    }

    protected LinkedList<Link> getLinkBySource(Actor a) {
        LinkedList<Link> ret = new LinkedList<Link>();
        if (a != null) {
            Iterator<String> linkTypes = links.keySet().iterator();
            while (linkTypes.hasNext()) {
                String type = linkTypes.next();
                if (links.get(type).get(a.getMode()) != null) {
                    TreeMap<Actor,Link> vector = links.get(type).get(a.getMode()).get(a.getID());
                    if (vector != null) {
                        ret.addAll(vector.values());
                    }
                }
            }
        } else {
            System.err.println("Actor in getLinkBySource is null");
        }
        return ret;
    }

    protected LinkedList<Link> getLinkByDestination(Actor a) {
        LinkedList<Link> ret = new LinkedList<Link>();
        if (a != null) {
            Iterator<String> linkTypes = invertedLinks.keySet().iterator();
            while (linkTypes.hasNext()) {
                String type = linkTypes.next();
                if (invertedLinks.get(type).get(a.getMode()) != null) {
                    TreeMap<Actor,Link> vector = invertedLinks.get(type).get(a.getMode()).get(a.getID());
                    if (vector != null) {
                        ret.addAll(vector.values());
                    }
                }
            }
        } else {
            System.err.println("Actor in getLinkBySource is null");
        }
        return ret;
    }

    @Override
    public int getActorCount(String type) {
        if (actor.get(type) != null) {
            return actor.get(type).size();
        } else {
            return 0;
        }
    }

    @Override
    public Properties getParameter() {
        return parameters;
    }

    public Parameter getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public void init(Properties map) {
        if (parameters.check(map)) {
            Iterator<Parameter> it = map.get().iterator();
            while (it.hasNext()) {
                Parameter param = it.next();
                if (parameters.get(param.getType()) != null) {
                    parameters.replace(param);
                } else {
                    parameters.add((ParameterInternal) param);
                }
            }
        }

    }

    public int compareTo(Object o) {
        return GraphCompare.compareTo(this, o);
    }

    public Graph prototype() {
        return new MemGraph();
    }

    public Iterator<Actor> getActorIterator() {
        return getActor().iterator();
    }

    public Iterator<Link> getLinkIterator() {
        return getLink().iterator();
    }

    public Iterator<Link> getLinkIterator(String type) {
        return getLink(type).iterator();
    }

    public Iterator<Link> getLinkBySourceIterator(String type, Actor sourceActor) {
        return getLinkBySource(type, sourceActor).iterator();
    }

    public Iterator<Link> getLinkByDesinationIterator(String type, Actor destActor) {
        return getLinkByDestination(type, destActor).iterator();
    }

    public Iterator<Link> getLinkIterator(String type, Actor sourceActor, Actor destActor) {
        return getLink(type, sourceActor, destActor).iterator();
    }

    public Iterator<Property> getPropertyIterator() {
        return properties.values().iterator();
    }

    public Iterator<PathSet> getPathSetIterator() {
        return pathSets.values().iterator();
    }

    public Iterator<Graph> getChildrenIterator() {
        return children.iterator();
    }

    public void removeProperty(String id){
        properties.remove(id);
    }
}

