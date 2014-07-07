/**
 * GraphByLink
 * Created Jan 5, 2009 - 7:47:41 PM
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
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.GraphQuery;
import org.mcennis.graphrat.query.GraphQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.link.NullLinkQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByLink implements GraphQuery{

    GraphQuery graphQuery = new AllGraphs();
    
    LinkQuery query = new NullLinkQuery();

    LinkEnd end = LinkEnd.SOURCE;
    boolean not = false;
    
    transient State state = State.UNINITIALIZED;

    public void buildQuery(LinkEnd end, boolean not, GraphQuery graphQuery, LinkQuery query){
        this.not = not;
        if(end == null){
            this.end = LinkEnd.SOURCE;
        }else{
            this.end=end;
        }
        if(graphQuery != null){
            this.graphQuery = graphQuery;
        }else{
            this.graphQuery = new AllGraphs();
            ((AllGraphs)this.query).buildQuery();
        }

        if(query != null){
            this.query = query;
        }else{
            this.query = new NullLinkQuery();
            ((NullLinkQuery)this.query).buildQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "null value for query,  NullQuery inserted.");
        }
    }
    
    public Collection<Graph> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        LinkedList<Graph> result = new LinkedList<Graph>();
        if(g == null){
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        Iterator<Graph> it = (graphQuery.executeIterator(g,actorList,linkList));
        while(it.hasNext()){
            Graph graph = it.next();
            Collection<Link> links = null;
            if(end==LinkEnd.SOURCE){
                links = query.execute(graph, actorList,null,linkList);
            }else if(end==LinkEnd.DESTINATION){
                links = query.execute(graph, null,actorList,linkList);
            }else{
                links = query.execute(graph, actorList,actorList,linkList);
            }
            if(links.size()>0){
                if(!not){
                    result.add(graph);
                }
            }else if(not){
                result.add(graph);
            }                  
        }
        return result;
    }

    public void exportQuery(Writer writer) throws IOException{
        writer.append("<GraphByLink>\n");
        if(not){
            writer.append("\t<Not/>\n");
        }
	graphQuery.exportQuery(writer);
        query.exportQuery(writer);
        writer.append("</GraphByLink>\n");
    }

    public int compareTo(Object o) {
        if(o.getClass().getName().contentEquals(this.getClass().getName())){
            GraphByLink right = (GraphByLink)o;
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

    public GraphByLink prototype(){
        return new GraphByLink();
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
            GraphByLink link = this.prototype();
            link.graphQuery=graphQuery;
                link.query=query;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, actorList, linkList);
        }
    }
    public class GraphIterator implements Iterator<Graph> {

        Graph next = null;
        Iterator<Graph> it;
        Collection<Actor> a;
        Collection<Link> l;
        boolean remaining = true;

        public GraphIterator(Graph g, Collection<Actor> actorList,Collection<Link>linkList) {
            a = actorList;
            l = linkList;
            it = graphQuery.executeIterator(g, actorList, linkList);
        }

        public boolean hasNext() {
            if(remaining){
                if(next ==null){
                    while((next==null)&&(it.hasNext())){
                        next = it.next();
                        if(!checkLinks(next,a,l).hasNext()){
                            next = null;
                        }
                    }
                    remaining = false;
                    return false;
                }
                return true;
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

        protected Iterator<Link> checkLinks(Graph g, Collection<Actor> actorList,Collection<Link>linkList){
            if(end==LinkEnd.SOURCE){
                return query.executeIterator(g, actorList, null, linkList);
            }else if(end == LinkEnd.DESTINATION){
                return query.executeIterator(g, null, actorList, linkList);
            }else{
                return query.executeIterator(g, actorList, actorList, linkList);
            }
        }
    }
}
