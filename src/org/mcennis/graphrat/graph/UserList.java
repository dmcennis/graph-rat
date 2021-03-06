/*
 * UserList.java
 *
 * Created on 4 June 2007, 10:06
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcennis.graphrat.graph;

import java.util.*;

import org.dynamicfactory.descriptors.Properties;
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
    public SortedSet<Actor> getActor() {

        TreeSet<Actor> ret = new TreeSet<Actor>();
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
    public SortedSet<Link> getLink() {

        return new TreeSet<Link>();

    }

    @Override
    public SortedSet<Link> getLink(String type) {

        return new TreeSet<Link>();

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
    public SortedSet<Actor> getActor(String type) {
        if(actor.containsKey(type)){
            TreeSet<Actor> ret = new TreeSet<Actor>();
            ret.addAll(actor.get(type));
            return ret;
        }else{
            return new TreeSet<Actor>();
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
    public SortedSet<String> getLinkTypes() {

        return new TreeSet<String>();

    }

    @Override
    public SortedSet<Link> getLinkBySource(String type, Actor sourceActor) {

        return new TreeSet<Link>();

    }

    @Override
    public SortedSet<Link> getLinkByDestination(String type, Actor destActor) {

        return new TreeSet<Link>();

    }

    @Override
    public SortedSet<Link> getLink(String type, Actor sourceActor, Actor destActor) {

        return new TreeSet<Link>();

    }

    @Override
    public SortedSet<String> getActorTypes() {
        TreeSet<String> ret = new TreeSet<String>();
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
    public SortedSet<Graph> getChildren() {

        return new TreeSet<Graph>();

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
        return new TreeSet<Link>().iterator();
    }

    public Iterator<Link> getLinkIterator(String type) {
        return new TreeSet<Link>().iterator();
    }

    public Iterator<Link> getLinkBySourceIterator(String type, Actor sourceActor) {
        return new TreeSet<Link>().iterator();
    }

    public Iterator<Link> getLinkByDesinationIterator(String type, Actor destActor) {
        return new TreeSet<Link>().iterator();
    }

    public Iterator<Link> getLinkIterator(String type, Actor sourceActor, Actor destActor) {
        return new TreeSet<Link>().iterator();
    }

    public Iterator<Property> getPropertyIterator() {
        return new LinkedList<Property>().iterator();
    }

    public Iterator<PathSet> getPathSetIterator() {
        return new LinkedList<PathSet>().iterator();
    }

    public Iterator<Graph> getChildrenIterator() {
        return new TreeSet<Graph>().iterator();
    }

    public Parameter getParameter(String name) {
        return properties.get(name);
    }

    public void removeProperty(String ID) {

    }
}

