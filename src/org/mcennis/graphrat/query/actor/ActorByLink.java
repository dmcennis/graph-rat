/**
 * ActorByLink
 * Created Jan 6, 2009 - 12:44:35 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.actor;

import java.io.IOException;

import org.mcennis.graphrat.query.*;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.link.NullLinkQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByLink implements ActorQuery {

    LinkQuery query = new NullLinkQuery();
    LinkEnd restriction = LinkEnd.SOURCE;
    boolean not = false;
    transient State state = State.UNINITIALIZED;

    public Collection<Actor> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        HashSet<Actor> result = new HashSet<Actor>();
        LinkedList<Actor> core = new LinkedList<Actor>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
            Iterator<Actor> it_actor = null;
            if (actorList == null) {
                it_actor = g.getActorIterator();
            } else {
                Collection<Actor> array = g.getActor();
                array.retainAll(actorList);
                it_actor = array.iterator();
            }
            while (it_actor.hasNext()) {
                Actor actor = it_actor.next();
                core.add(actor);
                Collection<Link> links = null;
                if(restriction == LinkEnd.SOURCE){
                    links = query.execute(g, core, null, linkList);
                }else if(restriction == LinkEnd.DESTINATION){
                    links = query.execute(g,null,core,linkList);
                }else{
                    links = query.execute(g, core, core, linkList);
                }
                if (links.size() > 0) {
                    if (!not) {
                        result.add(actor);
                    }
                } else if (not) {
                    result.add(actor);
                }
                core.clear();
            }
        return result;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<ActorByLink>\n");
        if (not) {
            writer.append("\t<Not/>\n");
        }
        writer.append("\t<LinkEnd>");
        switch(restriction){
            case SOURCE:
                writer.append("SOURCE");
                break;
            case DESTINATION:
                writer.append("DESTINATION");
                break;
            case ALL:
                writer.append("ALL");
                break;
        }
        writer.append("</LinkEnd>\n");
        query.exportQuery(writer);
        writer.append("</ActorByLink>\n");
    }

    public void buildQuery(LinkEnd mode, boolean not, LinkQuery query) {
        state = State.LOADING;
        this.restriction = mode;
        if (mode == null) {
            this.restriction = LinkEnd.SOURCE;
        }
        this.not = not;
        if (query != null) {
            this.query = query;
        } else {
            this.query = new NullLinkQuery();
            ((NullLinkQuery) this.query).buildQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "null value for query,  NullQuery inserted.");
        }
        state = State.READY;
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            ActorByLink right = (ActorByLink) o;
            if (restriction.compareTo(right.restriction) != 0) {
                return restriction.compareTo(right.restriction);
            }
            if (!not && right.not) {
                return 1;
            }
            if (not && right.not) {
                return -1;
            }
            return query.compareTo(right.query);
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public ActorByLink prototype() {
        return new ActorByLink();
    }

    public Iterator<Actor> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        if(!not){
            return new ActorIterator(g,actorList,linkList);
        }else{
            XorActorQuery xor = (XorActorQuery)ActorQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
            ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
            mode.buildQuery(".*", ".*", false);
            list.add(mode);
            ActorByLink link = this.prototype();
            link.query=query;
            link.restriction=restriction;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, actorList, linkList);
        }
    }


    public class ActorIterator implements Iterator<Actor> {

        LinkedList<Actor> next = new LinkedList<Actor>();
        boolean remaining = true;
        Collection<Link> linkList;
        Collection<Actor> actorList;
        Iterator<Actor> actorIterator;
        Iterator<Link> linkIterator;
        Graph g;

        public ActorIterator(Graph g, Collection<Actor> a, Collection<Link> l) {
            linkList = l;
            actorList = a;
            if(a != null){
                LinkedList<Actor> sortedActor = new LinkedList<Actor>();
                sortedActor.addAll(a);
                Collections.sort( sortedActor);
                actorIterator = sortedActor.iterator();
            }else{
                actorIterator = (Iterator<Actor>) g.getActorIterator();
            }
            this.g = g;
            this.hasNext();
        }

        public boolean hasNext() {
            if(remaining){
                if(next.size()>0){
                    return true;
                }
                Iterator<Link> link = null;
                if(restriction == LinkEnd.SOURCE){
                    link = query.executeIterator(g, next, null, linkList);
                }else if(restriction == LinkEnd.DESTINATION){
                    link = query.executeIterator(g, next, null, linkList);
                }else{
                    link = query.executeIterator(g, next, next, linkList);
                }
                while(!link.hasNext()&&actorIterator.hasNext()){
                    next.clear();
                    next.add(actorIterator.next());
                    if(restriction == LinkEnd.SOURCE){
                        link = query.executeIterator(g, next, null, linkList);
                    }else if(restriction == LinkEnd.DESTINATION){
                        link = query.executeIterator(g, next, null, linkList);
                    }else{
                        link = query.executeIterator(g, next, next, linkList);
                    }
                }
                if(link.hasNext()){
                    return true;
                }else{
                    remaining = false;
                    return false;
                }

            }else{
                return false;
            }
        }

        public Actor next() {
            if(next.size()>0){
                Actor ret = next.get(0);
                next.clear();
                return ret;
            }else{
                return null;
            }
        }

        public void remove() {
            ;
        }
    }
}
