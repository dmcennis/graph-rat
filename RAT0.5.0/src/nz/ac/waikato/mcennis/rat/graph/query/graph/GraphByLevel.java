/**
 * GraphByLevel
 * Created Jan 10, 2009 - 9:59:15 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByLevel implements GraphQuery {

    int level = 1;

    public Iterator<Graph> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        return execute(g,actorList,linkList).iterator();
    }

    public enum Operation {

        EQ, LT, GT, LTE, GTE, NE
    };
    Operation op = Operation.EQ;
    transient State state = State.UNINITIALIZED;

    public Collection<Graph> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        LinkedList<Graph> result = new LinkedList<Graph>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        traverseChildren(g, result, 0);
        Collections.sort(result);
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

    private void traverseChildren(Graph g, LinkedList<Graph> result, int count) {
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

    private void operate(Graph graph, LinkedList<Graph> result, int count) {
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
