/*
 * NullGraph.java
 *
 * Created on 3 June 2007, 14:49
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.graph;

import java.util.List;

import java.util.Iterator;
import java.util.LinkedList;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.actor.Actor;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.path.PathSet;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;

/**
 * Class that retains no information and implements null operations on every node.
 * This is useful when one wishes to parse a document without creating the graph.
 *
 * @author Daniel McEnnis
 * 
 */
public class NullGraph extends ModelShell implements Graph {

    public static final long serialVersionUID = 2;
    
    Properties properties = PropertiesFactory.newInstance().create();

    /** Creates a new instance of NullGraph */
    public NullGraph() {
        init(null);
    }

    @Override
    public List<Actor> getActor() {

        return new LinkedList<Actor>();

    }

    @Override
    public void add(Actor u) {

    }

    @Override
    public List<Actor> getActor(String type) {

        return new LinkedList<Actor>();

    }

    @Override
    public Actor getActor(String type, String ID) {

        return null;

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

        return new LinkedList<String>();

    }

    @Override
    public Iterator<Actor> getActorIterator(String type) {

        return new LinkedList<Actor>().iterator();

    }

    @Override
    public void setID(String id) {

    }

    @Override
    public String getID() {

        return "Null";

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
        ;
    }

    @Override
    public int getActorCount(String mode) {
        return 0;
    }

    public int compareTo(Object o) {
        return GraphCompare.compareTo(this,o);
    }

    public Iterator<Actor> getActorIterator() {
       return new LinkedList<Actor>().iterator();
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
        return null;
    }

    public Graph prototype() {
        return new NullGraph();
    }

    public void removeProperty(String ID) {
        
    }

}

