/**
 * GraphByLevel
 * Created Jan 10, 2009 - 9:59:15 PM
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.GraphQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByLevel implements GraphQuery {

    int level = 1;

    public Iterator<Graph> executeIterator(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        return execute(g,actorList,linkList).iterator();
    }

    public enum Operation {

        EQ, LT, GT, LTE, GTE, NE
    };
    Operation op = Operation.EQ;
    transient State state = State.UNINITIALIZED;

    public SortedSet<Graph> execute(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        TreeSet<Graph> result = new TreeSet<Graph>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        traverseChildren(g, result, 0);
        return result;
    }

    public void buildQuery(int level, Operation op) {
        this.level = level;
        if (op != null) {
            this.op = op;
        } else {
            this.op = Operation.EQ;
        }
    }

    private void traverseChildren(Graph g, TreeSet<Graph> result, int count) {
        Iterator<Graph> children = g.getChildren().iterator();
        while (children.hasNext()) {
            Graph child = children.next();
            operate(child, result, count + 1);
            traverseChildren(child, result, count + 1);
        }

    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<GraphByLevel>\n");
        writer.append("\t<Level>").append(Integer.toString(level)).append("</Level>\n");
        writer.append("\t<Operation>");
        switch (op) {
            case EQ:
                writer.append("EQ");
                break;
            case GT:
                writer.append("GT");
                break;
            case LT:
                writer.append("LT");
                break;
            case GTE:
                writer.append("GTE");
                break;
            case LTE:
                writer.append("LTE");
                break;
            case NE:
                writer.append("NE");
                break;
        }
        writer.append("</Operation>\n");
        writer.append("</GraphByLevel>\n");
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            GraphByLevel gbe = (GraphByLevel) o;
            if (this.level == gbe.level) {
                return op.compareTo(gbe.op);
            } else {
                return level - gbe.level;
            }
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    private void operate(Graph graph, TreeSet<Graph> result, int count) {
        switch (op) {
            case EQ:
                if (count == level) {
                    result.add(graph);
                }
                break;
            case LT:
                if (count < level) {
                    result.add(graph);
                }
                break;
            case GT:
                if (count > level) {
                    result.add(graph);
                }
                break;
            case LTE:
                if (count <= level) {
                    result.add(graph);
                }
                break;
            case GTE:
                if (count >= level) {
                    result.add(graph);
                }
                break;
            case NE:
                if (count != level) {
                    result.add(graph);
                }
                break;
        }
    }

    public State buildingStatus() {
        return state;
    }

    public GraphByLevel prototype() {
        return new GraphByLevel();
    }
    public class GraphIterator<Graph> implements Iterator<Graph> {

        Graph next = null;
        boolean remaining = true;

        public GraphIterator() {
        }

        public boolean hasNext() {
            if(remaining){
                return true;
            }else{
                return false;
            }
        }

        public Graph next() {
            return null;
        }

        public void remove() {
            ;
        }
    }
}
