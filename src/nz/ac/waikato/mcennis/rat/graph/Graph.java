/*
 * Graph.java
 *
 * Created on 1 May 2007, 14:16
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph;

import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.model.Model;
import nz.ac.waikato.mcennis.rat.graph.path.PathSet;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.property.Property;

import java.util.Iterator;

/**
 * Interface representing a social network.  Users are the individuals consuming
 * music, artists are the artists producing music, userlinks are the links between
 * users (with some name or class attached to them), artistlinks are the links between
 * users and artists, and similar artists are the links between different artists.
 *
 * @author Daniel McEnnis
 */
public interface Graph extends java.io.Serializable, nz.ac.waikato.mcennis.rat.parser.ParsedObject, Model, Comparable {

    /**
     * Add a user to the graph.  This replaces the user if a user with the
     * same ID already exists.
     *
     * @param u user to be included in the graph.
     */
    public void add(Actor u);

    /**
     * Adds a userlink to the graph.  There is no limit on the number of links
     * between users or even with the same type, source user, and destination user.
     *
     * @param link description of a named link between two users
     */
    public void add(Link link);

    /**
     * Identifies the given user and removes it and all links involving it from 
     * the graph.
     * 
     * @param u user to be removed from the graph.
     */
    public void remove(Actor u);

    /**
     * remove all user links with the same type, user ID, and artist ID from the graph
     *
     * @param ul link to be removed from the graph.
     */
    public void remove(Link ul);

    /**
     *
     * Retrieve the users that has the given ID.  Returns null if no user is in 
     * the graph with that ID.
     *
     * @param type 
     * @param ID ID string identifying an user
     *
     * @return user object who has this ID or null if it is not present
     */
    public Actor getActor(String type, String ID);

    /**
     * Retrieve an array of all actors of all types.  Returns null if no actors are in the graph.
     *
     * @return array of all users in the graph
     */
    public List<Actor> getActor();

    
    public Iterator<Actor> getActorIterator();
    /**
     * Retrieve an array of all actors of a given type. Returns null if there are no actors of that type in the graph.
     *
     * @param type class of actor to return
     *
     * @return array of actors of the given type
     */
    public List<Actor> getActor(String type);

    /**
     * Create a read only iterator over all actors.
     * 
     * @param type type (mode) of actor to return
     * @return iterator over all actors of the given type
     */
    public Iterator<Actor> getActorIterator(String type);

    /**
     * List all types - returns null if no actors exist.
     * 
     * @return array of all type names in this graph
     */
    public List<String> getActorTypes();

    /**
     * Return all user to user links in this graph in an array. Returns null if no
     * such user link exists.
     *
     * @return array of all userlinks or null
     */
    public List<Link> getLink();
    
    public Iterator<Link> getLinkIterator();

    /**
     * Retrieve an array of all userlinks of the given type from this graph or null
     * if no such links are in this graph. 
     * 
     * @param type 
     * @return array of all user links of a given type or null 
     */
    public List<Link> getLink(String type);
    
    public Iterator<Link> getLinkIterator(String type);

    /**
     * Returns all links (edges, arcs) of type (relation) type that go from the 
     * given actor to any destination. Returns null if no such links exist.
     * 
     * @param type type (relation) of link to return
     * @param sourceActor starting point of the link
     * @return Array of links or null
     */
    public List<Link> getLinkBySource(String type, Actor sourceActor);

    public Iterator<Link> getLinkBySourceIterator(String type,Actor sourceActor);
    
    /**
     * Returns all links (edges, arcs) of type (relation) type that point to
     * the given actor. Returns null if no such links exist
     * 
     * @param type type (relation) of link to return
     * @param destActor actor pointed to by links
     * @return Array of links or null
     */
    public List<Link> getLinkByDestination(String type, Actor destActor);

    public Iterator<Link> getLinkByDesinationIterator(String type,Actor destActor);

    /**
     * Returns all links of the given type (relation) type going from actor sourceActor
     * to actor destActor or null.  This is typically 1 link or null, but the spec does
     * not forbid multiple links of the same type (relation) between two actors.
     * @param type type (relation) of the link to return
     * @param sourceActor starting point of the link
     * @param destActor actor pointed to by links
     * @return Array of links or null
     */
    public List<Link> getLink(String type, Actor sourceActor, Actor destActor);

    public Iterator<Link> getLinkIterator(String type, Actor sourceActor, Actor destActor);

    /**
     * Returns all link types currently in this graph or null if no links are present.
     * @return array of link types
     */
    public List<String> getLinkTypes();

    /**
     * Returns an array of all properties associated with this object or null if
     * none exist
     * @return Array of properties
     */
    public List<Property> getProperty();

    public Iterator<Property> getPropertyIterator();

    public void removeProperty(String ID);

    /**
     * Return a property named by the given string or null if no property by 
     * this name exists
     * 
     * @param type property's name
     * @return Given property
     */
    public Property getProperty(String type);

    /**
     * Add a property to this graph.  If a property by this name already exists, it
     * is overwritten with the given property.
     * 
     * @param prop property to be added.
     */
    public void add(Property prop);

    /**
     * Return all PathSets associated with this graph. Null if no PathSets are in
     * the current graph.
     * 
     * @return Array of PathSets
     */
    public List<PathSet> getPathSet();

    public Iterator<PathSet> getPathSetIterator();
    /**
     * Return the named PathSet.  Returns null if no PathSet of the given ID exists.
     * 
     * @param id ID of the PathSet to return
     * @return PathSet specified by this ID
     */
    public PathSet getPathSet(String id);

    /**
     * Add a PathSet to this graph.  If another PathSet of the same ID exists, it
     * will be overwritten.
     * 
     * @param pathSet PathSet to be added to this graph
     */
    public void add(PathSet pathSet);

    /**
     * Set this graph's ID to this value.  Graph ID's must be unique.
     * @param id graph ID
     */
    public void setID(String id);

    /**
     * Return the ID associated with this Graph
     * @return ID
     */
    public String getID();

    /**
     * Commit the contents to database or storage.  This is a null operation for
     * memory only objects.  (Delaying committing changes to be in batches can 
     * result in a massive decrease in execution time.)
     * 
     */
    public void commit();

    /**
     * Add a graph (g) to the graph.  Will be changed to be a graph helper - 
     * deprecated by addChild(Graph g)
     * @param g graph to be added
     */
    public void add(Graph g);

    /**
     * Close database connections or write to file.  Operations on a graph after
     * close are undefined.
     */
    public void close();

    /**
     * Replaces all actor IDs with an anonymous numeric ID.
     */
    public void anonymize();

    /**
     * Returns the parent graph of this object or null if this is a root graph
     * 
     * @return parent graph
     */
    public Graph getParent();

    /**
     * Returns the children graphs of this object or null if none exist
     * 
     * @return Array of graphs that are children of this graph
     */
    public List<Graph> getChildren();
    
    public Iterator<Graph> getChildrenIterator();

    /**
     * Returns the children graph with the given id or null if there is not a 
     * child with that name.
     * 
     * @param id ID of the graph to return
     * @return Graph with the given ID
     */
    public Graph getChildren(String id);

    /**
     * Set the given graph to be a child of the current graph. NOTE: this does not 
     * verify that all actors and links only reference actors or links in the 
     * parent - this is the responsibility of the caller.
     * 
     * @param g Child Graph
     */
    public void addChild(Graph g);


    /**
     * Obtain a computer-readable description of the parameters and options this
     * graph supports
     * 
     * @return Description of all parameters this graph object supports.
     */
    public Properties getParameter();
    
    public Parameter getParameter(String name);
    
    /**
     * Set the parameters of this graph from the following property object.
     * @param props properties to be set
     */
    public void init(Properties props);
    
    /**
     * Returns the number of actors of the given type
     * @param mode type of actor to query
     * @return int number of actors of the given type
     */
    public int getActorCount(String mode);
    
    public Graph prototype();
    
}

