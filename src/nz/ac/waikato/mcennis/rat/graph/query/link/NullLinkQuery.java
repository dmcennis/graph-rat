/*
 * NullLinkQuery - created 31/01/2009 - 10:32:18 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.link;

import java.util.Collection;
import java.util.Iterator;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.query.NullQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;

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
