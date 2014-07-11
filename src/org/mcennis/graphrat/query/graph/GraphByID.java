/**
 * GraphByID
 * Created Jan 10, 2009 - 9:58:53 PM
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

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.GraphQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByID implements GraphQuery {

    Pattern pattern = Pattern.compile(".*");
    
    transient State state = State.UNINITIALIZED;

    public SortedSet<Graph> execute(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        TreeSet<Graph> result = new TreeSet<Graph>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
	result.addAll(getGraphSet(g));
        return result;
    }
    
    public void buildQuery(Pattern pattern){
        if(pattern != null){
            this.pattern = pattern;
        }else{
            this.pattern = Pattern.compile(".*");
        }
    }

    protected SortedSet<Graph> getGraphSet(Graph g) {
        TreeSet<Graph> result = new TreeSet<Graph>();
        if (pattern.matcher(g.getID()).matches()) {
            result.add(g);
        } else {
            LinkedList<Graph> ret = new LinkedList<Graph>();
            Iterator<Graph> children = g.getChildren().iterator();
            if (children != null) {
                while(children.hasNext()) {
                    result.addAll(getGraphSet(children.next()));
                }
            }
        }
        return result;
    }

    public void exportQuery(Writer writer) throws IOException{
        writer.append("<GraphByID>\n");
        writer.append("\t<Pattern>").append(pattern.toString()).append("</Pattern>\n");
        writer.append("</GraphByID>\n");
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            return pattern.toString().compareTo(((GraphByID)o).pattern.toString());
        }else{
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public GraphByID prototype(){
        return new GraphByID();
    }

    public Iterator<Graph> executeIterator(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        return execute(g,actorList,linkList).iterator();
    }
}
