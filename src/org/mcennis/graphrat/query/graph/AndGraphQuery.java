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
package org.mcennis.graphrat.query.graph;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.GraphQuery;
import org.mcennis.graphrat.query.AndQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class AndGraphQuery extends AndQuery implements GraphQuery{
    
    transient Graph g = null;

    transient Collection<Actor> actorList = null;

    transient Collection<Link> linkList = null;
    
    public Collection<Graph> execute(Graph g,Collection<Actor> actorList, Collection<Link> linkList){
	this.g = g;
	this.actorList = actorList;
	this.linkList = linkList;
	Collection<Graph> ret = execute(null);
	this.g = null;
	this.actorList = null;
	this.linkList = null;
	return ret;
    }

    public Iterator<Graph> executeIterator(Graph g,Collection<Actor> actorList, Collection<Link> linkList){
	this.g = g;
	this.actorList = actorList;
	this.linkList = linkList;
	Iterator<Graph> ret = executeIterator(null);
	this.g = null;
	this.actorList = null;
	this.linkList = null;
	return ret;
    }

    protected Collection<Graph> executeComponent(Object query){
	return ((GraphQuery)query).execute(g,actorList,linkList);
    }

    protected Iterator<Graph> executeIterator(Object query){
	return ((GraphQuery)query).execute(g,actorList,linkList).iterator();
    }

    protected void exportQuery(Object object,Writer writer) throws IOException{
	((GraphQuery)object).exportQuery(writer);
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(Collection<GraphQuery> source){
	super.buildQuery(source,"Graph");
    }

    
    public AndGraphQuery prototype(){
        return new AndGraphQuery();
    }
}
