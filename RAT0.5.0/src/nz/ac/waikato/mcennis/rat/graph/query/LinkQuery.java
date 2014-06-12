/*
 * Query.java
 *
 * Created on 31 January 2007, 16:04
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.query;

import java.io.IOException;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import java.util.Collection;
import java.util.Iterator;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;

/**
 * Class for general queries generating subgraphs.  Currently in pre-planning stages
 * This class is a stub to satisfy the graph methods that reference it.
 *
 * @author Daniel McEnnis
 * 
 */
public interface LinkQuery extends Query{
    
    public Collection<Link> execute(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList);
    
    public Iterator<Link> executeIterator(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList);

    public void exportQuery(java.io.Writer writer) throws IOException;
    
    public State buildingStatus();
    
    public LinkQuery prototype();
        
    public enum LinkEnd {SOURCE , DESTINATION, ALL};

    public enum SetOperation {AND, OR, XOR};
}

