/**
 * AndActorQuery
 * Created Jan 31, 2009 - 7:11 PM
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
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.OrQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class OrLinkQuery extends OrQuery implements LinkQuery{
    
    transient Graph g = null;

    transient Collection<Actor> sourceActorList = null;

    transient Collection<Actor> destActorList = null;

    transient Collection<Link> linkList = null;
    
    public Collection<Link> execute(Graph g,Collection<Actor> sourceActorList,Collection<Actor> destActorList, Collection<Link> linkList){
	this.g = g;
	this.sourceActorList = sourceActorList;
	this.destActorList = destActorList;
	this.linkList = linkList;
	Collection<Link> ret = execute(linkList);
	this.g = null;
	this.destActorList = null;
	this.sourceActorList = null;
	this.linkList = null;
	return ret;
    }

    public Iterator<Link> executeIterator(Graph g,Collection<Actor> sourceActorList, Collection<Actor> destActorList,Collection<Link> linkList){
	this.g = g;
	this.sourceActorList = sourceActorList;
	this.destActorList = destActorList;
	this.linkList = linkList;
	Iterator<Link> ret = executeIterator(linkList);
	this.g = null;
	this.destActorList = null;
	this.sourceActorList = null;
	this.linkList = null;
	return ret;
    }

    protected Collection<Link> executeComponent(Object query){
	return ((LinkQuery)query).execute(g,sourceActorList,destActorList,linkList);
    }

    protected Iterator<Link> executeIterator(Object query){
	return ((LinkQuery)query).execute(g,sourceActorList,destActorList,linkList).iterator();
    }

    protected void exportQuery(Object object,Writer writer) throws IOException{
	((LinkQuery)object).exportQuery(writer);
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(Collection<LinkQuery> source){
	super.buildQuery(source,"Link");
    }

    
    public OrLinkQuery prototype(){
        return new OrLinkQuery();
    }
}
