/*
 * ActorQuery.java
 *
 * Created on 31 January 2007, 16:04
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package org.mcennis.graphrat.query;

import java.io.IOException;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import java.util.Collection;
import java.util.Iterator;

import org.dynamicfactory.propertyQuery.Query;

/**
 * Class for general queries generating subgraphs.  Currently in pre-planning stages
 * This class is a stub to satisfy the graph methods that reference it.
 *
 * @author Daniel McEnnis
 * 
 */
public interface ActorQuery extends Query{
    
    public Collection<Actor> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList);

    public Iterator<Actor> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList);
    
    public void exportQuery(java.io.Writer writer) throws IOException;
    
    public State buildingStatus();
    
    public ActorQuery prototype();
        
}

