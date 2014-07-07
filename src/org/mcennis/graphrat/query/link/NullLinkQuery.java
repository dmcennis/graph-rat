/*
 * NullLinkQuery - created 31/01/2009 - 10:32:18 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.query.link;

import java.util.Collection;
import java.util.Iterator;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.NullQuery;
import org.mcennis.graphrat.query.LinkQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class NullLinkQuery extends NullQuery implements LinkQuery{

    public Collection<Link> execute(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList) {
        return super.execute();
    }

    public Iterator<Link> executeIterator(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList) {
        return super.executeIterator();
    }

    public void buildQuery(){
        super.buildQuery("Link");
    }

    public NullLinkQuery prototype() {
        return new NullLinkQuery();
    }

}
