/**
 * GraphByProperty
 * Created Jan 6, 2009 - 12:35:20 AM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph;

import java.io.IOException;
import nz.ac.waikato.mcennis.rat.graph.query.*;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.NullPropertyQuery;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByProperty implements GraphQuery {

    GraphQuery graphQuery = new AllGraphs();
    PropertyQuery query = new NullPropertyQuery();
    String propertyID = "";
    boolean not = false;
    transient State state = State.UNINITIALIZED;

    public void buildQuery(String propertyID, boolean not, GraphQuery graphQuery, PropertyQuery query) {
        if (propertyID != null) {
            this.propertyID = propertyID;
        }
        this.not = not;
        if (graphQuery != null) {
            this.graphQuery = graphQuery;
        } else {
            this.graphQuery = new AllGraphs();
            ((AllGraphs) this.query).buildQuery();
        }
        if (query != null) {
            this.query = query;
        } else {
            this.query = new NullPropertyQuery();
            ((NullPropertyQuery) this.query).buildQuery(false);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "null value for query,  NullQuery inserted.");
        }
    }

    public Collection<Graph> execute(Graph restriction, Collection<Actor> actorList, Collection<Link> linkList) {
        LinkedList<Graph> result = new LinkedList<Graph>();
        if (restriction == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        Collection<Graph> testSource = graphQuery.execute(restriction, actorList, linkList);
        Iterator<Graph> testSourceIt = testSource.iterator();
        while (testSourceIt.hasNext()) {
            Property test = testSourceIt.next().getProperty(propertyID);
            if (test != null) {
                if (query.execute(test)) {
                    if (!not) {
                        result.add(restriction);
                    }
                } else if (not) {
                    result.add(restriction);
                }
            } else if (not) {
                result.add(restriction);
            }
        }
        return result;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<GraphByProperty>\n");
        if (not) {
            writer.append("\t<Not/>\n");
        }
        writer.append("<PropertyID>").append(propertyID).append("</PropertyID>\n");
        graphQuery.exportQuery(writer);
        query.exportQuery(writer);
        writer.append("</GraphByProperty>\n");
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            GraphByProperty right = (GraphByProperty) o;
            if (propertyID.compareTo(right.propertyID) != 0) {
                return propertyID.compareTo(right.propertyID);
            }
            if (not && !right.not) {
                return -1;
            }
            if (!not && right.not) {
                return 1;
            }
            if (graphQuery.compareTo(right.graphQuery) != 0) {
                return graphQuery.compareTo(right.graphQuery);
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

    public GraphByProperty prototype() {
        return new GraphByProperty();
    }

    public Iterator<Graph> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        if(!not){
            return new GraphIterator(g,actorList,linkList);
        }else{
            XorGraphQuery xor = (XorGraphQuery)GraphQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<GraphQuery> list = new LinkedList<GraphQuery>();
            AllGraphs all = (AllGraphs)GraphQueryFactory.newInstance().create("AllGraphs");
            all.buildQuery();
            list.add(all);
            GraphByProperty link = this.prototype();
            link.graphQuery=graphQuery;
                link.query=query;
                link.propertyID=propertyID;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, actorList, linkList);
        }
    }

    public class GraphIterator implements Iterator<Graph> {

        Iterator<Graph> it;
        Graph next = null;
        Collection<Actor> a;
        Collection<Link> l;
        boolean remaining = true;

        public GraphIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
            it = graphQuery.executeIterator(g, actorList, linkList);
            a = actorList;
            l = linkList;
        }

        public boolean hasNext() {
            if (remaining) {
                if (next == null) {
                    while ((it.hasNext())) {
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

        public Graph next() {
            hasNext();
            Graph ret = next;
            next = null;
            return ret;
        }

        public void remove() {
            ;
        }
    }
}
