/**
 * AllGraphs
 * Created Jan 30, 2009 - 10:08 pm
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
import org.mcennis.graphrat.query.*;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;

/**
 *
 * @author Daniel McEnnis
 */
public class AllGraphs implements GraphQuery{

    transient State state = State.UNINITIALIZED;

    public void buildQuery(){
	state = State.READY;
    }
    
    public SortedSet<Graph> execute(Graph restriction, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        TreeSet<Graph> result = new TreeSet<Graph>();
	    addChildren(result,restriction);
         return result;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<AllGraphs/>\n");
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            return 0;
	}else{
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public void addChildren(SortedSet<Graph> result,Graph parent){
	SortedSet<Graph> children = parent.getChildren();
	Iterator<Graph> it = children.iterator();
	addChildren(result,it.next());
	result.add(parent);
    }

    public State buildingStatus() {
        return state;
    }

    public AllGraphs prototype(){
        return new AllGraphs();
    }

    public Iterator<Graph> executeIterator(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
