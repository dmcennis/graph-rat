/*
 * Clique.java
 *
 * Created on 20 July 2007, 20:54
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph;

import java.util.List;

import java.util.HashSet;

import java.util.Iterator;

import java.util.LinkedList;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import java.util.regex.Pattern;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.path.PathSet;

import nz.ac.waikato.mcennis.rat.graph.property.Property;

import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.Query;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByActor;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.AndLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;

/**
 * Class for describing a completely connected subgraph.  No link information is
 * kept - only the actors are stored. Unlike more general graphs, this graph 
 * class can only be a leaf graph and must have a parent graph.  The graph is 
 * populated by checking against the parent to see if clique-conditions still 
 * holds
 *
 * @author Daniel McEnnis
 * 
 */
public class Clique extends ModelShell implements Graph, Comparable {

    Graph parent = null;
    String relation;
    String actorType;
    String name;
    TreeSet<Actor> intersection;
    TreeMap<String, Actor> map;
    TreeSet<Actor> actors = new TreeSet<Actor>();
    TreeMap<String, Property> property;
    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    /** Creates a new instance of Clique */
    public Clique() {
        map = new TreeMap<String, Actor>();
        intersection = new TreeSet<Actor>();
        property = new TreeMap<String, Property>();
        ParameterInternal param = ParameterFactory.newInstance().create("GraphID", String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(restrictionPart);
        param.add("Root");
        properties.add(param);
    }

    /**
     * Adds an actor to the clique if
     * <ul><li>The parent is defined
     * <li>The parent contains the actor
     * <li>The actor is pointed to by all previous actors
     * <li>The actor points to every existing actor in the clique
     * </ul>
     * 
     * @param u actor to be (potentially) added to this clique.
     */
    public void add(Actor u) {

        if ((parent != null) && (map.size() == 0)) {

            start(u);

        } else {

            internalExpand(u);

        }

    }

    /**
     * Intentionally a null operation - links are added implicitly by add(Actor)
     * @param link ignored
     */
    public void add(Link link) {

        ;// intentionally null

    }

    /**
     * FIXME: remove actor currently does nothing
     */
    public void remove(Actor u) {

        ;

    }

    /**
     * Intentionally a null operation - links are added implicitly by add(Actor)
     * so removing them makes no sense (unless an actor is removed)
     * @param ul ignored
     */
    public void remove(Link ul) {

        ;// intentioanlly null

    }

    @Override
    public Actor getActor(String type, String ID) {

        return map.get(ID);

    }

    @Override
    public List<Actor> getActor() {
        LinkedList<Actor> ret = new LinkedList<Actor>();
        ret.addAll(map.values());
        return ret;
    }

    @Override
    public List<Actor> getActor(String type) {
        LinkedList<Actor> ret = new LinkedList<Actor>();
        ret.addAll(map.values());
        return ret;

    }

    @Override
    public List<String> getActorTypes() {
        LinkedList<String> ret = new LinkedList<String>();
        ret.add(actorType);
        return ret;
    }

    /**
     * Null operation - links are implicit, not explicitly defined.
     */
    public List<Link> getLink() {

        return new LinkedList<Link>();

    }

    /**
     * Null operation - links are implicit, not explicitly defined.
     */
    public List<Link> getLink(String type) {

        return new LinkedList<Link>();

    }

    /**
     * Null operation - links are implicit, not explicitly defined.
     */
    public List<Link> getLinkBySource(String type, Actor sourceActor) {

        return new LinkedList<Link>();

    }

    /**
     * Null operation - links are implicit, not explicitly defined.
     */
    public List<Link> getLinkByDestination(String type, Actor destActor) {

        return new LinkedList<Link>();

    }

    /**
     * Null operation - links are implicit, not explicitly defined.
     */
    public List<Link> getLink(String type, Actor sourceActor, Actor destActor) {

        return new LinkedList<Link>();

    }

    /**
     * Null operation - links are implicit, not explicitly defined.
     */
    public List<String> getLinkTypes() {
        LinkedList<String> ret = new LinkedList<String>();
        ret.add(relation);
        return ret;

    }

    /**
     * Intentionally null since this class can not have child graphs
     * @param q ignored
     */
    public void setSubGraph(Query q) {

        ;// intentionally null - leaf node

    }

    @Override
    public List<Property> getProperty() {
        LinkedList<Property> ret = new LinkedList<Property>();
        ret.addAll(property.values());
        return ret;
    }

    @Override
    public Property getProperty(String type) {

        return property.get(type);

    }

    @Override
    public void add(Property prop) {

        property.put(prop.getType(), prop);

    }

    /**
     * Intentionally a null operation - paths do no make much sense on a clique
     */
    public List<PathSet> getPathSet() {

        return new LinkedList<PathSet>();

    }

    /**
     * Intentionally a null operation - paths do no make much sense on a clique
     * @param id ignored
     */
    public PathSet getPathSet(String id) {

        return null;

    }

    /**
     * Intentionally a null operation - paths do no make much sense on a clique
     * @param pathSet ignored
     */
    public void add(PathSet pathSet) {

        ;// intentionally empty

    }

    /**
     * Establishes which link type (relation) determines whether an acotr (node)
     * belongs in a clique or not
     * @param rel name of the link type (relation)
     */
    public void setRelation(String rel) {

        relation = rel;

    }

    @Override
    public void setID(String id) {

        properties.get("GraphID").clear();
        properties.get("GraphID").add(id);

    }

    @Override
    public String getID() {

        return (String) (properties.get("GraphID").getValue().iterator().next());

    }

    /**
     * Initializes a clique - add an actor and initialize the intersection
     * HashSet to all destinations this object links to (by keeping a set of all
     * actors that are linked to by all actors in a clique, determining whether
     * a new actor belongs is O(n) with the size of the clique for actors in the
     * set and constant for those not.)
     * 
     * @param u actor that initializes this clique - technically not a clique until
     * the second actor is added.
     */
    protected void start(Actor u) {

        if (parent != null) {

            Iterator<Link> l = parent.getLinkBySource(relation, u).iterator();
            if (l.hasNext()) {
                map.put(u.getID(), u);
                actors.add(u);
            }
            while (l.hasNext()) {
                intersection.add(l.next().getDestination());
            }

        }

    }

    /**
     * Adds a new actor to the graph if the new graph will still be a clique
     * 
     * @param u actor to be added
     */
    protected void internalExpand(Actor u) {

        if (parent != null) {

            if ((!map.containsKey(u.getID())) && (intersection.contains(u))) {

                if (checkLinks(u)) {

                    map.put(u.getID(), u);

                    actors.add(u);

                    Iterator<Actor> l = intersection.iterator();

//                    for (int i = 0; i < l.length; ++i) {
                    while (l.hasNext()) {
                        Actor a = l.next();
                        if (parent.getLink(relation, u, a) == null) {
                            intersection.remove(a);
                        }

                    }

                }

            }

        }

    }

    /**
     * Determines if the actor to be added links to all elements of the clique
     * @param u actor to be checked
     * @return whether or not this actor links to all other actors in the clique
     */
    protected boolean checkLinks(Actor u) {

        boolean ret = true;

        Iterator<Actor> it = actors.iterator();

        while (it.hasNext()) {

            if (parent.getLink(relation, u, it.next()) == null) {

                return false;

            }

        }

        return true;

    }

    /**
     * CompareTo operator for Cliques. Throws a ClassCastException if the object
     * compared is not a Clique
     * 
     * Checks:
     * <ol><li>sort Actors - first clique with a smaller (by compareTo) actor is
     * is smaller by compareTo.
     * <li>sort Properties - firts clique with a smaller (by compareTo) property is
     * smaller by compareTo.
     * <li>return 0
     * </ol>
     * 
     * @param o Clique to be compared against
     */
    public int compareTo(Object o) {
        return GraphCompare.compareTo(this, o);
    }

    /**
     * Equals iff compareTo=0.  Does not throw class cast exception.
     * 
     * @param obj object to check for equality
     */
    public boolean equals(Object obj) {

        if (obj instanceof Clique) {

            if (this.compareTo(obj) == 0) {

                return true;

            } else {

                return false;

            }

        } else {

            return false;

        }

    }

    /**
     * Given this clique (and the graph) - generate all cliques of one bigger 
     * size that have this clique as a subclique where the new actor is greater
     * (by compareTo) than any other actor in the clique.  Returns null if no 
     * such cliques exist.
     * 
     * 
     * @param g parent graph
     * @return HashSet containing cliques or null
     */
    public HashSet<Clique> expand(Graph g) {

        HashSet<Clique> ret = new HashSet<Clique>();

        Iterator<Actor> it = intersection.iterator();

        String[] cliqueMembers = map.keySet().toArray(new String[]{});

        java.util.Arrays.sort(cliqueMembers);

        while (it.hasNext()) {

            Actor next = it.next();

            List<Link> l = g.getLinkBySource(relation, next);
            Iterator<Link> lIt = l.iterator();
            String[] dest = new String[l.size()];
            int count = 0;
            while (lIt.hasNext()) {
                dest[count++] = lIt.next().getDestination().getID();
            }

            java.util.Arrays.sort(dest);

            int i = 0;

            int j = 0;

            while ((i < dest.length) && (j < cliqueMembers.length)) {

                if (dest[i].compareTo(cliqueMembers[j]) < 0) {

                    ++i;

                } else if (dest[i].compareTo(cliqueMembers[j]) > 0) {

                    break;

                } else {

                    ++i;

                    ++j;

                }

            }

            if (j == cliqueMembers.length) {

                Clique c = this.duplicate();

                c.add(map.get(next));

                ret.add(c);

            }
        }

        if (ret.size() > 0) {

            return ret;

        } else {

            return null;

        }

    }

    /**
     * Create a duplicate Clique that is equal to the original
     * 
     * @return duplicate clique
     */
    public Clique duplicate() {

        Clique ret = new Clique();

        ret.map = (TreeMap<String, Actor>) map.clone();

        ret.property = (TreeMap<String, Property>) property.clone();

        ret.relation = relation;

        ret.name = name;

        ret.intersection = (TreeSet<Actor>) intersection.clone();

        return ret;

    }

    /**
     * FIXME: actor iterator not implemented yet.
     */
    public Iterator<Actor> getActorIterator(String type) {

        //TODO: write getActorIterator

        return actors.iterator();

    }

    /**
     * Establishes the type (mode) of actor this clique pulls from
     * 
     * @param type name of the type (mode) of actor.
     */
    public void setActorType(String type) {

        actorType = type;

    }

    @Override
    public void commit() {

        ;//intentionally null

    }

    @Override
    public void add(Graph g) {

        ;// intentionally null

    }

    @Override
    public void close() {
    }

    /**
     * FIXME: anonymizer not implemented
     */
    public void anonymize() {
    }

    @Override
    public Graph getParent() {

        return parent;

    }

    /**
     * Leaf graph only, so intentionally null
     */
    public List<Graph> getChildren() {

        return new LinkedList<Graph>();

    }

    /**
     * Leaf graph only, so intentionally null
     */
    public Graph getChildren(String id) {

        return null;

    }

    /**
     * Leaf graph only, so intentionally null
     */
    public void addChild(Graph g) {
    }

    /**
     * Identical to add(Actor u) except that this Clique is unchanged and a new 
     * Clique is returned. If the new group of actors is not a clique, return 
     * null.
     * 
     * @param u Actor to be added
     * @return new clique containing this actor
     */
    public Clique expand(Actor u) {

        if ((parent != null) && (intersection.contains(u) && checkLinks(u))) {

            Clique ret = new Clique();

            ret.setID(this.getID());

            ret.setActorType(actorType);

            ret.setRelation(relation);

            if (properties.get("LinkFilter").getValue().size() > 0) {
                ret.properties.add("LinkFilter", properties.get("LinkFilter").get());
            }

            ret.map = (TreeMap<String, Actor>) this.map.clone();

            ret.intersection = (TreeSet<Actor>) this.intersection.clone();

            ret.actors = (TreeSet<Actor>) this.actors.clone();

            ret.internalExpand(u);

            return ret;

        } else {

            return null;

        }

    }

    /**
     * Returns an unsorted array of actors that every member of the clique 
     * points to.  If this is none - return null
     * 
     * @return array of actors
     */
    public List<Actor> getIntersection() {
        LinkedList<Actor> ret = new LinkedList<Actor>();
        ret.addAll(intersection);
        return ret;
    }

    /**
     * Return the largest (by compareTo) actor in this clique. Returns null if
     * this clique si empty.
     * 
     * @return largest actor;
     */
    public Actor getMaxActor() {

        return actors.last();

    }

    /**
     * Intentionally null since this graph can have no children.
     */
    public Graph getSubGraph(Properties props, Set<Actor> actor) {

        return null;

    }

    public Graph[] getGraphs(Pattern pattern) {
        if (pattern.matcher(name).matches()) {
            return new Graph[]{this};
        } else {
            return new Graph[]{};
        }
    }

    @Override
    public Properties getParameter() {
        return properties;
    }

    @Override
    public void init(Properties props) {
        if (properties.check(props)) {
            Iterator<Parameter> it = props.get().iterator();
            while (it.hasNext()) {
                Parameter param = it.next();
                if (properties.get(param.getType()) != null) {
                    properties.replace(param);
                } else {
                    properties.add((ParameterInternal) param);
                }
            }
        }
    }

    @Override
    public int getActorCount(String mode) {
        return actors.size();
    }

    public Graph transform() {
        Properties settings = GraphFactory.newInstance().getParameter().duplicate();
        settings.get("GraphID").clear();
        settings.get("GraphID").add(properties.get("GraphID").getValue());
        Graph g = GraphFactory.newInstance().create(settings);

        Iterator<Actor> aIt = map.values().iterator();
        while (aIt.hasNext()) {
            g.add(aIt.next());
        }

        ActorByMode actorQuery = new ActorByMode();
        actorQuery.buildQuery(actorType, ".*",false);
        LinkByRelation relationQuery = new LinkByRelation();
        relationQuery.buildQuery(relation, false);
        LinkByActor linkQuery = new LinkByActor();
        linkQuery.buildQuery(false, actorQuery, actorQuery, LinkQuery.SetOperation.AND);
        LinkedList<LinkQuery> pieces = new LinkedList<LinkQuery>();
        pieces.add(linkQuery);
        pieces.add(relationQuery);

        AndLinkQuery and = new AndLinkQuery();
        and.buildQuery(pieces);
        Iterator<Link> link = (and.executeIterator(parent, null, null, null));
        while (link.hasNext()) {
            g.add(link.next());
        }
        return g;
    }

    public Iterator<Actor> getActorIterator() {
        return actors.iterator();
    }

    public Iterator<Link> getLinkIterator() {
        return new LinkedList<Link>().iterator();
    }

    public Iterator<Link> getLinkIterator(String type) {
        return new LinkedList<Link>().iterator();
    }

    public Iterator<Link> getLinkBySourceIterator(String type, Actor sourceActor) {
        return new LinkedList<Link>().iterator();
    }

    public Iterator<Link> getLinkByDesinationIterator(String type, Actor destActor) {
        return new LinkedList<Link>().iterator();
    }

    public Iterator<Link> getLinkIterator(String type, Actor sourceActor, Actor destActor) {
        return new LinkedList<Link>().iterator();
    }

    public Iterator<Property> getPropertyIterator() {
        return property.values().iterator();
    }

    public Iterator<PathSet> getPathSetIterator() {
        return new LinkedList<PathSet>().iterator();
    }

    public Iterator<Graph> getChildrenIterator() {
        return new LinkedList<Graph>().iterator();
    }

    public Parameter getParameter(String name) {
        return properties.get(name);
    }

    public Graph prototype() {
        return new Clique();
    }

    public void removeProperty(String ID) {
        property.remove(ID);
    }
}

