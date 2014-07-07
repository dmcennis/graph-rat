/**
 * ActorByProperty
 * Created Jan 5, 2009 - 10:16:19 PM
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
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.propertyQuery.NullPropertyQuery;
import org.dynamicfactory.propertyQuery.PropertyQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByProperty implements ActorQuery {

    PropertyQuery query = new NullPropertyQuery();
    String propertyID = "";
    boolean not = false;
    transient State state = State.UNINITIALIZED;

    public Iterator<Actor> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        if(!not){
            return new ActorIterator(g,actorList);
        }else{
            XorActorQuery xor = (XorActorQuery)ActorQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
            ActorByMode all = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
            all.buildQuery(".*", ".*", false);
            list.add(all);
            ActorByProperty link = this.prototype();
            link.propertyID=propertyID;
                link.query=query;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, actorList, linkList);
        }
    }

    enum AbpState {

        START, PROPERTY_ID, MODE, NOT, QUERY
    };
    transient AbpState internalState = AbpState.START;

    public Collection<Actor> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        HashSet<Actor> result = new HashSet<Actor>();
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
            Property test = actor.getProperty(propertyID);
            if (test != null) {
                if (query.execute(test)) {
                    if (!not) {
                        result.add(actor);
                    }
                } else if (not) {
                    result.add(actor);
                }
            } else if (not) {
                result.add(actor);
            }
        }
        return result;
    }

    public void buildQuery(String propertyID, boolean not, PropertyQuery query) {
        state = State.LOADING;
        this.propertyID = propertyID;
        if (propertyID == null) {
            propertyID = "";
        }
        this.not = not;
        if (query != null) {
            this.query = query;
        } else {
            this.query = new NullPropertyQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "null value for query,  NullQuery inserted.");
        }
        state = State.READY;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<ActorByProperty>\n");
        if (not) {
            writer.append("\t<Not/>\n");
        }
        writer.append("\t<PropertyID>").append(propertyID).append("</PropertyID>\n");
        query.exportQuery(writer);
        writer.append("</ActorByProperty>\n");
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            ActorByProperty right = (ActorByProperty) o;
            if (propertyID.compareTo(right.propertyID) != 0) {
                return propertyID.compareTo(right.propertyID);
            }
            if (not && !right.not) {
                return -1;
            }
            if (!not && right.not) {
                return 1;
            }
            if (query.compareTo(right.query) != 0) {
                return query.compareTo(right.query);
            }
            return 0;
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public ActorByProperty prototype() {
        return new ActorByProperty();
    }

    public class ActorIterator implements Iterator<Actor> {

        Actor next = null;
        Iterator<Actor> it;
        boolean remaining = true;

        public ActorIterator(Graph g, Collection<Actor> actorList) {
            if (actorList != null) {
                LinkedList<Actor> actor = new LinkedList<Actor>();
                actor.addAll(actorList);
                Collections.sort(actor);
                it = actor.iterator();
            } else {
                it = g.getActorIterator();
            }
        }

        public boolean hasNext() {
            if (remaining) {
                if (next == null) {
                    while (it.hasNext()) {
                        next = it.next();
                        if (next.getProperty(propertyID) != null) {
                            if (query.execute(next.getProperty(propertyID))) {
                                return true;
                            }
                        }
                    }
                    remaining = false;
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        public Actor next() {
            hasNext();
            Actor ret = next;
            next = null;
            return ret;
        }

        public void remove() {
            ;
        }
    }
}
