/*
 * NullActorQuery - created 31/01/2009 - 10:24:13 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.query.actor;

import java.util.Collection;
import java.util.Iterator;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.NullQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class NullActorQuery extends NullQuery implements ActorQuery{

    public Collection<Actor> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        return super.execute();
    }

    public Iterator<Actor> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        return super.executeIterator();
    }

    public void buildQuery(){
        super.buildQuery("Actor");
    }

    public NullActorQuery prototype() {
        return new NullActorQuery();
    }

}
