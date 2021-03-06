/*
 * UserIDList.java
 *
 * Created on 20 August 2007, 14:50
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
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.path.PathSet;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;

/**
 * Class that records only those users that initiate links.  All other forms of adding
 * a user are disabled.  All other apsects of the graph are no-ops.  Used to create a 
 * subset of all seen users that are represented by a FOAF page (i.e. have been crawled).
 *
 * @author Daniel McEnnis
 * 
 */
public class UserIDList extends ModelShell implements Graph {

    PropertiesInternal properties = PropertiesFactory.newInstance().create();
    
    TreeSet<String> usernames = new TreeSet();

    /** Creates a new instance of UserIDList */
    public UserIDList() {
        ParameterInternal name = ParameterFactory.newInstance().create("GraphID",String.class);
        SyntaxObject restrictionPart = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(restrictionPart);
        name.add("Default");
        properties.add(name);

    }

    @Override
    public void add(Actor u) {



    }

    /**
     * Instead of adding a link, adds the source actor to the list of 'good' actors
     * 
     * @param link link (arc,edge) to be mined
     */
    public void add(Link link) {

        usernames.add(link.getSource().getID());

    }

    @Override
    public void remove(Actor u) {

    }

    @Override
    public void remove(Link ul) {

    }

    /**
     * Returns only actors that are present in the index of 'good' actors.
     */
    public Actor getActor(String type, String ID) {

        if (usernames.contains(ID)) {

            return ActorFactory.newInstance().create(type,ID);

        } else {

            return null;

        }

    }

    @Override
    public SortedSet<Actor> getActor() {

        return new TreeSet<Actor>();

    }

    @Override
    public SortedSet<Actor> getActor(String type) {

        return new TreeSet<Actor>();

    }

    @Override
    public Iterator<Actor> getActorIterator(String type) {

        return new LinkedList<Actor>().iterator();

    }

    @Override
    public SortedSet<String> getActorTypes() {

        return new TreeSet<String>();

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
    public SortedSet<String> getLinkTypes() {

        return new TreeSet<String>();

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
    public void add(Property prop) {

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
    public void setID(String id) {

    }

    @Override
    public String getID() {

        return "UserIDList";

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
        return usernames.size();
    }

    public int compareTo(Object o) {
        return GraphCompare.compareTo(this,o);
    }

    public Iterator<Actor> getActorIterator() {
        return new TreeSet<Actor>().iterator();
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

    public Graph prototype() {
        return new UserIDList();
    }

    public void removeProperty(String ID) {

    }
}

