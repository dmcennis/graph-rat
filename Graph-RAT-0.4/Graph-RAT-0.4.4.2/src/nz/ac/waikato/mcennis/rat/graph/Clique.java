/* * Clique.java * * Created on 20 July 2007, 20:54 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph;import java.util.HashMap;import java.util.HashSet;import java.util.Iterator;import java.util.Properties;import java.util.Set;import java.util.TreeSet;import java.util.regex.Pattern;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import nz.ac.waikato.mcennis.rat.graph.link.Link;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import nz.ac.waikato.mcennis.rat.graph.path.PathSet;import nz.ac.waikato.mcennis.rat.graph.query.Query;/** * Class for describing a completely connected subgraph.  No link information is * kept - only the actors are stored. Unlike more general graphs, this graph  * class can only be a leaf graph and must have a parent graph.  The graph is  * populated by checking against the parent to see if clique-conditions still  * holds * * @author Daniel McEnnis *  */public class Clique extends ModelShell implements Graph, Comparable {    Graph parent = null;    String relation;    String actorType;    String name;    HashSet<Actor> intersection;    HashMap<String, Actor> map;    TreeSet<Actor> actors = new TreeSet<Actor>();    HashMap<String, Property> property;    /** Creates a new instance of Clique */    public Clique() {        map = new HashMap<String, Actor>();        intersection = new HashSet<Actor>();        property = new HashMap<String, Property>();        init(null);    }    /**     * Adds an actor to the clique if     * <ul><li>The parent is defined     * <li>The parent contains the actor     * <li>The actor is pointed to by all previous actors     * <li>The actor points to every existing actor in the clique     * </ul>     *      * @param u actor to be (potentially) added to this clique.     */    public void add(Actor u) {        if ((parent != null) && (map.size() == 0)) {            start(u);        } else {            internalExpand(u);        }    }    /**     * Intentionally a null operation - links are added implicitly by add(Actor)     * @param link ignored     */    public void add(Link link) {        ;// intentionally null    }    /**     * FIXME: remove actor currently does nothing     */    public void remove(Actor u) {        ;    }    /**     * Intentionally a null operation - links are added implicitly by add(Actor)     * so removing them makes no sense (unless an actor is removed)     * @param ul ignored     */    public void remove(Link ul) {        ;// intentioanlly null    }    @Override    public Actor getActor(String type, String ID) {        return map.get(ID);    }    @Override    public Actor[] getActor() {        if ((map.values() != null) && (map.size() > 0)) {            return map.values().toArray(new Actor[]{});        } else {            return null;        }    }    @Override    public Actor[] getActor(String type) {        if ((map.values() != null) && (map.size() > 0)) {            return map.values().toArray(new Actor[]{});        } else {            return null;        }    }    @Override    public String[] getActorTypes() {        return new String[]{actorType};    }    /**     * Null operation - links are implicit, not explicitly defined.     */    public Link[] getLink() {        return null;    }    /**     * Null operation - links are implicit, not explicitly defined.     */    public Link[] getLink(String type) {        return null;    }    /**     * Null operation - links are implicit, not explicitly defined.     */    public Link[] getLinkBySource(String type, Actor sourceActor) {        return null;    }    /**     * Null operation - links are implicit, not explicitly defined.     */    public Link[] getLinkByDestination(String type, Actor destActor) {        return null;    }    /**     * Null operation - links are implicit, not explicitly defined.     */    public Link[] getLink(String type, Actor sourceActor, Actor destActor) {        return null;    }    /**     * Null operation - links are implicit, not explicitly defined.     */    public String[] getLinkTypes() {        return null;    }    @Override    public Graph[] getSubGraph(Query q) {        return null;    }    /**     * Intentionally null since this class can not have child graphs     * @param q ignored     */    public void setSubGraph(Query q) {        ;// intentionally null - leaf node    }    @Override    public Property[] getProperty() {        if (property.values() != null) {            return property.values().toArray(new Property[]{});        } else {            return null;        }    }    @Override    public Property getProperty(String type) {        return property.get(type);    }    @Override    public void add(Property prop) {        property.put(prop.getType(), prop);    }    /**     * Intentionally a null operation - paths do no make much sense on a clique     */    public PathSet[] getPathSet() {        return null;    }    /**     * Intentionally a null operation - paths do no make much sense on a clique     * @param id ignored     */    public PathSet getPathSet(String id) {        return null;    }    /**     * Intentionally a null operation - paths do no make much sense on a clique     * @param pathSet ignored     */    public void add(PathSet pathSet) {        ;// intentionally empty    }    /**     * Establishes which link type (relation) determines whether an acotr (node)     * belongs in a clique or not     * @param rel name of the link type (relation)     */    public void setRelation(String rel) {        relation = rel;    }    @Override    public void setID(String id) {        name = id;    }    @Override    public String getID() {        return name;    }    /**     * Initializes a clique - add an actor and initialize the intersection     * HashSet to all destinations this object links to (by keeping a set of all     * actors that are linked to by all actors in a clique, determining whether     * a new actor belongs is O(n) with the size of the clique for actors in the     * set and constant for those not.)     *      * @param u actor that initializes this clique - technically not a clique until     * the second actor is added.     */    protected void start(Actor u) {        if (parent != null) {            Link[] l = parent.getLinkBySource(relation, u);            if (l != null) {                map.put(u.getID(), u);                actors.add(u);                for (int i = 0; i < l.length; ++i) {                    intersection.add(l[i].getDestination());                }            }        }    }    /**     * Adds a new actor to the graph if the new graph will still be a clique     *      * @param u actor to be added     */    protected void internalExpand(Actor u) {        if (parent != null) {            if ((!map.containsKey(u.getID())) && (intersection.contains(u))) {                if (checkLinks(u)) {                    map.put(u.getID(), u);                    actors.add(u);                    Actor[] l = intersection.toArray(new Actor[]{});                    for (int i = 0; i < l.length; ++i) {                        if (parent.getLink(relation, u, l[i]) == null) {                            intersection.remove(l[i]);                        }                    }                }            }        }    }    /**     * Determines if the actor to be added links to all elements of the clique     * @param u actor to be checked     * @return whether or not this actor links to all other actors in the clique     */    protected boolean checkLinks(Actor u) {        boolean ret = true;        Iterator<Actor> it = actors.iterator();        while (it.hasNext()) {            if (parent.getLink(relation, u, it.next()) == null) {                return false;            }        }        return true;    }    /**     * CompareTo operator for Cliques. Throws a ClassCastException if the object     * compared is not a Clique     *      * Checks:     * <ol><li>sort Actors - first clique with a smaller (by compareTo) actor is     * is smaller by compareTo.     * <li>sort Properties - firts clique with a smaller (by compareTo) property is     * smaller by compareTo.     * <li>return 0     * </ol>     *      * @param o Clique to be compared against     */    public int compareTo(Object o) {        Clique right = (Clique) o;        if (this.relation.contentEquals(right.relation)) {            if (this.name.contentEquals(right.name)) {                if (this.map.size() == right.map.size()) {                    String[] leftActors = this.map.keySet().toArray(new String[]{});                    String[] rightActors = right.map.keySet().toArray(new String[]{});                    java.util.Arrays.sort(leftActors);                    java.util.Arrays.sort(rightActors);                    for (int i = 0; i < leftActors.length; ++i) {                        if (!leftActors[i].contentEquals(rightActors[i])) {                            return leftActors[i].compareTo(rightActors[i]);                        }                    }                    leftActors = null;                    rightActors = null;                    Property[] leftProp = this.getProperty();                    Property[] rightProp = this.getProperty();                    if (leftProp == null) {                        if (rightProp == null) {                            return 0;                        } else {                            return 1;                        }                    } else {                        if (rightProp == null) {                            return -1;                        } else if (leftProp.length != rightProp.length) {                            return leftProp.length - rightProp.length;                        } else {                            java.util.Arrays.sort(leftProp);                            java.util.Arrays.sort(rightProp);                            for (int i = 0; i < leftProp.length; ++i) {                                if (leftProp[i].compareTo(rightProp[i]) != 0) {                                    return leftProp[i].compareTo(rightProp[i]);                                }                            }                        }                    }                    return 0;                } else {                    return this.map.size() - right.map.size();                }            } else {                return this.name.compareTo(right.name);            }        } else {            return this.relation.compareTo(right.relation);        }    }    /**     * Equals iff compareTo=0.  Does not throw class cast exception.     *      * @param obj object to check for equality     */    public boolean equals(Object obj) {        if (obj instanceof Clique) {            if (this.compareTo(obj) == 0) {                return true;            } else {                return false;            }        } else {            return false;        }    }    /**     * Given this clique (and the graph) - generate all cliques of one bigger      * size that have this clique as a subclique where the new actor is greater     * (by compareTo) than any other actor in the clique.  Returns null if no      * such cliques exist.     *      *      * @param g parent graph     * @return HashSet containing cliques or null     */    public HashSet<Clique> expand(Graph g) {        HashSet<Clique> ret = new HashSet<Clique>();        Iterator<Actor> it = intersection.iterator();        String[] cliqueMembers = map.keySet().toArray(new String[]{});        java.util.Arrays.sort(cliqueMembers);        while (it.hasNext()) {            Actor next = it.next();            Link[] l = g.getLinkBySource(relation, next);            if (l != null) {                String[] dest = new String[l.length];                for (int i = 0; i < l.length; ++i) {                    dest[i] = l[i].getDestination().getID();                }                java.util.Arrays.sort(dest);                int i = 0;                int j = 0;                while ((i < dest.length) && (j < cliqueMembers.length)) {                    if (dest[i].compareTo(cliqueMembers[j]) < 0) {                        ++i;                    } else if (dest[i].compareTo(cliqueMembers[j]) > 0) {                        break;                    } else {                        ++i;                        ++j;                    }                }                if (j == cliqueMembers.length) {                    Clique c = this.duplicate();                    c.add(map.get(next));                    ret.add(c);                }            }        }        if (ret.size() > 0) {            return ret;        } else {            return null;        }    }    /**     * Create a duplicate Clique that is equal to the original     *      * @return duplicate clique     */    public Clique duplicate() {        Clique ret = new Clique();        ret.map = (HashMap<String, Actor>) map.clone();        ret.property = (HashMap<String, Property>) property.clone();        ret.relation = relation;        ret.name = name;        ret.intersection = (HashSet<Actor>) intersection.clone();        return ret;    }    /**     * FIXME: actor iterator not implemented yet.     */    public Iterator<Actor> getActorIterator(String type) {        //TODO: write getActorIterator        return null;    }    /**     * Establishes the type (mode) of actor this clique pulls from     *      * @param type name of the type (mode) of actor.     */    public void setActorType(String type) {        actorType = type;    }    @Override    public void commit() {        ;//intentionally null    }    @Override    public void add(Graph g) {        ;// intentionally null    }    @Override    public void close() {    }    /**     * FIXME: anonymizer not implemented     */    public void anonymize() {    }    @Override    public Graph getParent() {        return parent;    }    /**     * Leaf graph only, so intentionally null     */    public Graph[] getChildren() {        return null;    }    /**     * Leaf graph only, so intentionally null     */    public Graph getChildren(String id) {        return null;    }    /**     * Leaf graph only, so intentionally null     */    public void addChild(Graph g) {    }    /**     * Identical to add(Actor u) except that this Clique is unchanged and a new      * Clique is returned. If the new group of actors is not a clique, return      * null.     *      * @param u Actor to be added     * @return new clique containing this actor     */    public Clique expand(Actor u) {        if ((parent != null) && (intersection.contains(u) && checkLinks(u))) {            Clique ret = new Clique();            ret.setID(this.getID());            ret.setActorType(actorType);            ret.setRelation(relation);            ret.map = (HashMap<String, Actor>) this.map.clone();            ret.intersection = (HashSet<Actor>) this.intersection.clone();            ret.actors = (TreeSet<Actor>) this.actors.clone();            ret.internalExpand(u);            return ret;        } else {            return null;        }    }    /**     * Returns an unsorted array of actors that every member of the clique      * points to.  If this is none - return null     *      * @return array of actors     */    public Actor[] getIntersection() {        if (intersection.size() > 0) {            return intersection.toArray(new Actor[]{});        } else {            return null;        }    }    /**     * Return the largest (by compareTo) actor in this clique. Returns null if     * this clique si empty.     *      * @return largest actor;     */    public Actor getMaxActor() {        return actors.last();    }    /**     * Intentionally null since this graph can have no children.     */    public Graph getSubGraph(Properties props, Set<Actor> actor) {        return null;    }    public Graph[] getGraphs(Pattern pattern) {        if(pattern.matcher(name).matches()){            return new Graph[]{this};        }else{            return new Graph[]{};        }    }    @Override    public Parameter[] getParameters() {        return new Parameter[]{};    }    @Override    public void init(Properties props) {        ;    }    @Override    public int getActorCount(String mode) {        return actors.size();    }}