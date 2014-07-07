/*
 * UserList.java
 *
 * Created on 4 June 2007, 10:06
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.graph;

import java.util.List;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.actor.Actor;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.path.PathSet;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;

/**
 * Aquires a list of all actors that have been seen. All other operations are no-ops.
 * FIXME: Specific to "User" actor type (mode).
 *
 * @author Daniel McEnnis
 * 
 */
public class UserList extends ModelShell implements Graph {

    public static final long serialVersionUID = 1;
    
    TreeMap<String,Vector<Actor>> actor = new TreeMap<String,Vector<Actor>>();
    String name;
    
    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    /** Creates a new instance of UserList */
    public UserList() {
        ParameterInternal name = ParameterFactory.newInstance().create("GraphID",String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("Default");
        properties.add(name);

        name = ParameterFactory.newInstance().create("ActorModeList",String.class);
        restrictionPart = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("User");
        properties.add(name);

       
    }

    @Override
    public List<Actor> getActor() {

        LinkedList<Actor> ret = new LinkedList<Actor>();
        Iterator<Vector<Actor>> it = actor.values().iterator();
        while(it.hasNext()){
            ret.addAll(it.next());
        }
        return ret;

    }

    @Override
    public Actor getActor(String type, String ID) {
        if((properties.get("ActorModeList").getValue().contains(type))){
            for(int i=0;i<actor.get(type).size();++i){
                if(actor.get(type).get(i).getID().contentEquals(ID)){
                    return actor.get(type).get(i);
                }
            }
        }
        return null;
    }

    @Override
    public void add(Actor u) {
        if((properties.get("ActorModeList").getValue().contains(u.getMode()))){
            if(!actor.containsKey(u.getMode())){
                actor.put(u.getMode(),new Vector<Actor>());
            }
            actor.get(u.getMode()).add(u);
        }
    }

    @Override
    public void add(Link link) {

    }

    @Override
    public List<Link> getLink() {

        return new LinkedList<Link>();

    }

    @Override
    public List<Link> getLink(String type) {

        return new LinkedList<Link>();

    }

    @Override
    public void remove(Actor u) {
        actor.get(u.getMode()).remove(u);
    }

    @Override
    public void remove(Link ul) {

    }

    @Override
    public void add(Property prop) {

    }

    @Override
    public List<Property> getProperty() {

        return new LinkedList<Property>();

    }

    @Override
    public Property getProperty(String type) {

        return null;

    }

    @Override
    public List<Actor> getActor(String type) {
        if(actor.containsKey(type)){
            return actor.get(type);
        }else{
            return new LinkedList<Actor>();
        }
    }

    @Override
    public List<PathSet> getPathSet() {

        return new LinkedList<PathSet>();

    }

    @Override
    public PathSet getPathSet(String id) {

        return null;

    }

    @Override
    public void add(PathSet pathSet) {

    }

    @Override
    public List<String> getLinkTypes() {

        return new LinkedList<String>();

    }

    @Override
    public List<Link> getLinkBySource(String type, Actor sourceActor) {

        return new LinkedList<Link>();

    }

    @Override
    public List<Link> getLinkByDestination(String type, Actor destActor) {

        return new LinkedList<Link>();

    }

    @Override
    public List<Link> getLink(String type, Actor sourceActor, Actor destActor) {

        return new LinkedList<Link>();

    }

    @Override
    public List<String> getActorTypes() {
        LinkedList<String> ret = new LinkedList<String>();
        Iterator it = properties.get("ActorModeList").getValue().iterator();
        while(it.hasNext()){
            ret.add((String)it.next());
        }
        return ret;

    }

    @Override
    public Iterator<Actor> getActorIterator(String type) {
        if(actor.containsKey(type)){
            return actor.get(type).iterator();
        }else{
            return (new LinkedList()).iterator();
        }

    }
    
    @Override
    public void setID(String id) {
        properties.get("GraphID").clear();
        properties.get("GraphID").add(id);

    }

    @Override
    public String getID() {

        return (String)properties.get("GraphID").getValue().iterator().next();

    }

    @Override
    public void commit() {

    }

    @Override
    public void add(Graph g) {

    }

    @Override
    public void close() {

    }

    @Override
    public void anonymize() {

    }

    @Override
    public Graph getParent() {

        return null;

    }

    @Override
    public List<Graph> getChildren() {

        return new LinkedList<Graph>();

    }

    @Override
    public Graph getChildren(String id) {

        return null;

    }

    @Override
    public void addChild(Graph g) {

    }

    @Override
    public Properties getParameter() {
        return properties;
    }

    @Override
    public void init(Properties props) {
        if(properties.check(props)){
            Iterator<Parameter> it = props.get().iterator();            
            while(it.hasNext()){
                Parameter param = it.next();
                if(properties.get(param.getType())!=null){
                    properties.replace(param);
                }else{
                    properties.add((ParameterInternal) param);
                }
            }
        }
    }

    @Override
    public int getActorCount(String mode) {
        int ret = 0;
        for(int i=0;i<actor.size();++i){
            ret =+ actor.get(i).size();
        }
        return ret;
    }

    public int compareTo(Object o) {
        return GraphCompare.compareTo(this,o);
    }


    public Graph prototype() {
        return new UserList();
    }

    public Iterator<Actor> getActorIterator() {
        return getActor().iterator();
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
        return new LinkedList<Property>().iterator();
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

    public void removeProperty(String ID) {

    }
}

