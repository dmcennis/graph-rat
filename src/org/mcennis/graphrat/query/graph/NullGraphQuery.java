/*
 * NullGraphQuery - created 31/01/2009 - 10:27:27 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.query.graph;

import java.util.Collection;
import java.util.Iterator;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.GraphQuery;
import org.mcennis.graphrat.query.NullQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class NullGraphQuery extends NullQuery implements GraphQuery{

    public Collection<Graph> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        return super.execute();
    }

    public Iterator<Graph> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        return super.executeIterator();
    }

    public void buildQuery(){
        super.buildQuery("Graph");
    }

    public NullGraphQuery prototype() {
        return new NullGraphQuery();
    }

}
