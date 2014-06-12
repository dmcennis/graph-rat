/*
 * NullGraphQuery - created 31/01/2009 - 10:27:27 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.graph;

import java.util.Collection;
import java.util.Iterator;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;
import nz.ac.waikato.mcennis.rat.graph.query.NullQuery;

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
