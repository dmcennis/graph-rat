/**
 * ActorByLink
 * Created Jan 6, 2009 - 12:44:35 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.actor;
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

import java.io.IOException;

import org.mcennis.graphrat.query.*;
import java.io.Writer;
import java.util.*;
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

    public SortedSet<Actor> execute(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        TreeSet<Actor> result = new TreeSet<Actor>();
        TreeSet<Actor> core = new TreeSet<Actor>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
            Iterator<Actor> it_actor = null;
            if (actorList == null) {
                it_actor = g.getActorIterator();
            } else {
                SortedSet<Actor> array = g.getActor();
                array.retainAll(actorList);
                it_actor = array.iterator();
            }
            while (it_actor.hasNext()) {
                Actor actor = it_actor.next();
                core.add(actor);
                SortedSet<Link> links = null;
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

    public Iterator<Actor> executeIterator(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
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

        TreeSet<Actor> next = new TreeSet<Actor>();
        boolean remaining = true;
        SortedSet<Link> linkList;
        SortedSet<Actor> actorList;
        Iterator<Actor> actorIterator;
        Iterator<Link> linkIterator;
        Graph g;

        public ActorIterator(Graph g, SortedSet<Actor> a, SortedSet<Link> l) {
            linkList = l;
            actorList = a;
            if(a != null){
                TreeSet<Actor> sortedActor = new TreeSet<Actor>();
                sortedActor.addAll(a);
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
                Actor ret = next.first();
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
