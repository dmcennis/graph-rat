/**
 * LinkByProperty
 * Created Jan 5, 2009 - 10:09:10 PM
 * Copyright Daniel McEnnis, see license.txt
 */
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
package org.mcennis.graphrat.query.link;

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
import org.dynamicfactory.property.Property;
import org.dynamicfactory.propertyQuery.NullPropertyQuery;
import org.dynamicfactory.propertyQuery.PropertyQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class LinkByProperty implements LinkQuery {

    PropertyQuery query = new NullPropertyQuery();
    String propertyID = "";
    boolean not = false;
    transient State state = State.UNINITIALIZED;

    public void buildQuery(String propertyID, boolean not, PropertyQuery query) {
        this.propertyID = propertyID;
        if (propertyID == null) {
            this.propertyID = "";
        }
        this.not = not;
        this.query = query;
        if (query == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null query is not permitted - inserting NullPropertyQuery");
            query = new NullPropertyQuery();
        }
    }

    public Collection<Link> execute(Graph g,Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList) {
        HashSet<Link> result = new HashSet<Link>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
	
	Iterator<Link> it_link = null;
	Iterator<Link> array = g.getLinkIterator();
	LinkedList<Link> list = new LinkedList<Link>();
	while (array.hasNext()) {
	    Link l = array.next();
	    if ((linkList == null) || (linkList.contains(l))) {
            if((sourceActorList != null)&&(destinationActorList == null)){
                if(sourceActorList.contains(l.getSource())){
                    list.add(l);
                }
            }else if((sourceActorList == null)&&(destinationActorList!=null)){
                if(destinationActorList.contains(l.getDestination())){
                    list.add(l);
                }
            }else if((sourceActorList != null)&&(destinationActorList!=null)){
                if(sourceActorList.contains(l.getSource())&&destinationActorList.contains(l.getDestination())){
                    list.add(l);
                }
            }else{
                list.add(l);
            }
	    }
	}
	
	it_link = list.iterator();
	
	while (it_link.hasNext()) {
	    Link link = it_link.next();
	    Property test = link.getProperty(propertyID);
	    if (test != null) {
		if (query.execute(test)) {
		    if (!not) {
			result.add(link);
		    }
		} else if (not) {
		    result.add(link);
		}
	    } else if (not) {
		result.add(link);
	    }
	}
        return result;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<LinkByProperty>\n");
        if (not) {
            writer.append("\t<Not/>\n");
        }
        writer.append("\t<PropertyID>").append(propertyID).append("</PropertyID>\n");
        query.exportQuery(writer);
        writer.append("</LinkByProperty>\n");
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            LinkByProperty right = (LinkByProperty) o;
            if (propertyID.compareTo(right.propertyID) != 0) {
                return propertyID.compareTo(right.propertyID);
            }
            if (!not && right.not) {
                return -1;
            }
            if (not && right.not) {
                return 1;
            }
            return query.compareTo(right.query);
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public LinkByProperty prototype() {
        return new LinkByProperty();
    }

    public Iterator<Link> executeIterator(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList) {
        if(!not){
            return new LinkIterator(g,linkList);
        }else{
            XorLinkQuery xor = (XorLinkQuery)LinkQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<LinkQuery> list = new LinkedList<LinkQuery>();
            LinkByRelation all = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            all.buildQuery(".*",false);
            list.add(all);
            LinkByProperty link = this.prototype();
            link.propertyID=propertyID;
            link.query=query;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, sourceActorList,destinationActorList, linkList);
        }
    }
    public class LinkIterator implements Iterator<Link> {

        Iterator<Link> it;
        Link next = null;
        boolean remaining = true;

        public LinkIterator(Graph g, Collection<Link> link) {
            if (link != null) {
                LinkedList<Link> actor = new LinkedList<Link>();
                actor.addAll(link);
                Collections.sort(actor);
                it = actor.iterator();
            } else {
                it = g.getLinkIterator();
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

        public Link next() {
            hasNext();
            Link ret = next;
            next = null;
            return ret;
        }

        public void remove() {
            ;
        }
    }
}
