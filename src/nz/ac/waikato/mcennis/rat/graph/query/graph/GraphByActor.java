/**
 * GraphByActor
 * Created Jan 5, 2009 - 7:47:24 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph;

import nz.ac.waikato.mcennis.rat.graph.query.*;
import java.io.Writer;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import org.dynamicfactory.propertyQuery.Query.State;
import nz.ac.waikato.mcennis.rat.graph.query.actor.NullActorQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByActor implements GraphQuery{

    GraphQuery graphQuery = new AllGraphs();

    ActorQuery query = new NullActorQuery();
    
    boolean not = false;
    
    transient State state = State.UNINITIALIZED;
    
    public Collection<Graph> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        LinkedList<Graph> result = new LinkedList<Graph>();
        if(g == null){
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        Iterator<Graph> it = graphQuery.executeIterator(g,actorList,linkList);
        while(it.hasNext()){
            Graph graph = it.next();
            Collection<Actor> actors = query.execute(graph,actorList,linkList);
            if(actors.size()>0){
                if(!not){
                    result.add(graph);
                }
            }else if(not){
                result.add(graph);
            }
        }
        return result;
    }
    
    public void buildQuery(boolean not, GraphQuery graphQuery, ActorQuery query){
        this.not = not;
        if(graphQuery != null){
            this.graphQuery = graphQuery;
        }else{
            this.graphQuery = new AllGraphs();
            ((AllGraphs)this.query).buildQuery();
        }
        if(query != null){
            this.query = query;
        }else{
            this.query = new NullActorQuery();
            ((NullActorQuery)this.query).buildQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "null value for query,  NullQuery inserted.");
        }
    }

    public void exportQuery(Writer writer) throws IOException{
        writer.append("<GraphByActor>\n");
        if(not){
            writer.append("\t<Not/>\n");
        }
	graphQuery.exportQuery(writer);
        query.exportQuery(writer);
        writer.append("</GraphByActor>\n");
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            GraphByActor right = (GraphByActor)o;
            if(not && !right.not){
                return -1;
            }
            if(!not && right.not){
                return 1;
            }
            if(graphQuery.compareTo(right.graphQuery)!=0){
                return graphQuery.compareTo(right.graphQuery);
            }
            if(query.compareTo(right.query)!=0){
                return query.compareTo(right.query);
            }
            return 0;
        }else{
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public GraphByActor prototype(){
        return new GraphByActor();
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
            GraphByActor link = this.prototype();
            link.graphQuery=graphQuery;
                link.query=query;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, actorList, linkList);
        }
    }

    public class GraphIterator implements Iterator<Graph> {

        Graph next = null;
        boolean remaining = true;
        Iterator<Graph> it;
        Collection<Actor> r;
        Collection<Link> l;

        public GraphIterator(Graph g, Collection<Actor> artistList,Collection linkList) {
            it = graphQuery.executeIterator(g, artistList, linkList);
            r = artistList;
            l=linkList;
        }

        public boolean hasNext() {
            if(remaining){
                if(next == null){
                    while((next == null)&&(it.hasNext())){
                        next = it.next();
                        if(!query.executeIterator(next, r, l).hasNext()){
                            next = null;
                        }
                    }
                    if(next != null){
                        return true;
                    }else{
                        remaining = false;
                        return false;
                    }
                }else{
                    return true;
                }
            }else{
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
