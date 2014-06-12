/* * UserList.java * * Created on 4 June 2007, 10:06 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph;import java.util.HashSet;import java.util.Iterator;import java.util.LinkedList;import java.util.Properties;import java.util.Set;import java.util.Vector;import java.util.regex.Pattern;import nz.ac.waikato.mcennis.rat.graph.link.Link;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import nz.ac.waikato.mcennis.rat.graph.path.PathSet;import nz.ac.waikato.mcennis.rat.graph.property.Property;import nz.ac.waikato.mcennis.rat.graph.query.Query;/** * Aquires a list of all actors that have been seen. All other operations are no-ops. * FIXME: Specific to "User" actor type (mode). * * @author Daniel McEnnis *  */public class UserList extends ModelShell implements Graph {    public static final long serialVersionUID = 1;        String[] userType = new String[]{"User"};        Vector<Vector<Actor>> actor = new Vector<Vector<Actor>>();;    String name;    /** Creates a new instance of UserList */    public UserList() {        init(null);    }    @Override    public Actor[] getActor() {        LinkedList<Actor> ret = new LinkedList<Actor>();        for(Vector<Actor> vect : actor){            ret.addAll(vect);        }        return ret.toArray(new Actor[]{});    }    @Override    public Actor getActor(String type, String ID) {        return null;    }    @Override    public void add(Actor u) {        for(int i=0;i<userType.length;++i){            if(userType[i].contentEquals(u.getType())){                actor.get(i).add(u);            }        }    }    @Override    public void add(Link link) {    }    @Override    public Link[] getLink() {        return null;    }    @Override    public Link[] getLink(String type) {        return null;    }    @Override    public void remove(Actor u) {        for(Vector<Actor> vect : actor){            vect.remove(u);        }    }    @Override    public void remove(Link ul) {    }    @Override    public void add(Property prop) {    }    @Override    public Property[] getProperty() {        return null;    }    @Override    public Property getProperty(String type) {        return null;    }    @Override    public Actor[] getActor(String type) {        for(int i=0;i<userType.length;++i){            if(userType[i].contentEquals(type)){                return actor.get(i).toArray(new Actor[]{});            }        }        return null;    }    @Override    public void setSubGraph(Query q) {    }    @Override    public Graph[] getSubGraph(Query q) {        return null;    }    @Override    public PathSet[] getPathSet() {        return null;    }    @Override    public PathSet getPathSet(String id) {        return null;    }    @Override    public void add(PathSet pathSet) {    }    @Override    public String[] getLinkTypes() {        return null;    }    @Override    public Link[] getLinkBySource(String type, Actor sourceActor) {        return null;    }    @Override    public Link[] getLinkByDestination(String type, Actor destActor) {        return null;    }    @Override    public Link[] getLink(String type, Actor sourceActor, Actor destActor) {        return null;    }    @Override    public String[] getActorTypes() {        return userType;    }    @Override    public Iterator<Actor> getActorIterator(String type) {        for(int i=0;i<userType.length;++i){            if(userType[i].contentEquals(type)){                return actor.get(i).iterator();            }        }        return null;    }        public void setUserType(String[] types){        if(types != null){            userType = types;            actor = new Vector<Vector<Actor>>();        }    }    @Override    public void setID(String id) {        name = id;    }    @Override    public String getID() {        return name;    }    @Override    public void commit() {    }    @Override    public void add(Graph g) {    }    @Override    public void close() {    }    @Override    public void anonymize() {    }    @Override    public Graph getParent() {        return null;    }    @Override    public Graph[] getChildren() {        return null;    }    @Override    public Graph getChildren(String id) {        return null;    }    @Override    public void addChild(Graph g) {    }    @Override    public Graph getSubGraph(Properties props, Set<Actor> actor) {        return null;    }    @Override    public Graph[] getGraphs(Pattern pattern) {        return new Graph[]{};    }    @Override    public Parameter[] getParameters() {        return new Parameter[]{};    }    @Override    public void init(Properties props) {        ;    }    @Override    public int getActorCount(String mode) {        int ret = 0;        for(int i=0;i<actor.size();++i){            ret =+ actor.get(i).size();        }        return ret;    }}