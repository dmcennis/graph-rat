/**
 * LinkByActor
 * Created Jan 10, 2009 - 7:01:57 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.link;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.NullActorQuery;

/**
 * 
 * @author Daniel McEnnis
 */
public class LinkByActor implements LinkQuery {

    ActorQuery sourceActorQuery = new NullActorQuery();
    ActorQuery destinationActorQuery = new NullActorQuery();
    boolean not = false;
    SetOperation op = SetOperation.AND;
    transient State state = State.UNINITIALIZED;

    public void buildQuery(boolean not, ActorQuery sourceQuery, ActorQuery destinationQuery, SetOperation op) {
        this.op = op;
        if (op == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Null value for the type of set operation to perform to combine link queries - assuming AND");
            this.op = SetOperation.AND;
        }

        if ((sourceQuery == null) && (destinationQuery == null)) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "No source actor query when source query is to be performed");
            destinationActorQuery = new NullActorQuery();
        }

        this.sourceActorQuery = sourceQuery;
        this.destinationActorQuery = destinationQuery;

        this.not = not;
    }

    public Collection<Link> execute(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destActorList, Collection<Link> linkList) {
        HashSet<Link> result = new HashSet<Link>();
        LinkedList<Link> linkLimit = new LinkedList<Link>();
        LinkedList<Actor> actorLimit = new LinkedList<Actor>();
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
                boolean add = true;
                if ((sourceActorList != null) && !sourceActorList.contains(l.getSource())) {
                    add = false;
                }
                if ((destActorList != null) && !destActorList.contains(l.getDestination())) {
                    add = false;
                }
                if (add) {
                    list.add(l);
                }
            }
        }

        it_link = list.iterator();

        while (it_link.hasNext()) {
            Link link = it_link.next();
//            if (link.getRelation().contentEquals(relation)) {
                if ((sourceActorQuery != null)&&(destinationActorQuery==null)) {
                    boolean add = true;
                    actorLimit.add(link.getSource());
                   add = (sourceActorQuery.execute(g, actorLimit, null).size() > 0);
                    if(add&&!not){
                        result.add(link);
                    }else if(!add && not){
                        result.add(link);
                    }
               }else if((sourceActorQuery!=null)&&(destinationActorQuery != null)){
                    boolean source = true;
                    boolean destination = true;
                    actorLimit.add(link.getSource());
                    source = (sourceActorQuery.execute(g, actorLimit, null).size() > 0);
                    actorLimit.clear();
                    actorLimit.add(link.getDestination());
                    destination = (destinationActorQuery.execute(g, actorLimit, null).size() > 0);
                    switch (op) {
                        case AND:
                            if (source && destination) {
                                if (!not) {
                                    result.add(link);
                                }
                            } else {
                                if (not) {
                                    result.add(link);
                                }
                            }
                            break;
                        case OR:
                            if (source && destination) {
                                if (!not) {
                                    result.add(link);
                                }
                            } else {
                                if (not) {
                                    result.add(link);
                                }
                            }
                            break;
                        case XOR:
                            if (source || destination) {
                                if ((source && destination) && (not)) {
                                    result.add(link);
                                } else if (!not) {
                                    result.add(link);
                                }
                            } else {
                                if (not) {
                                    result.add(link);
                                }
                            }
                            break;
                    }
                }else{
                    actorLimit.add(link.getSource());
                    boolean add = (destinationActorQuery.execute(g,actorLimit,null).size()>0);
                    if(add && !not){
                        result.add(link);
                    }else if(!add && not){
                        result.add(link);
                    }
                }
                    linkLimit.clear();
                    actorLimit.clear();

        }
        return result;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<LinkByActor>\n");
        if (not) {
            writer.append("\t<Not/>\n");
        }
        if(op != null){
        writer.append("\t<Operation>");
        switch (op) {
            case AND:
                writer.append("And");
                break;
            case OR:
                writer.append("Or");
                break;
            case XOR:
                writer.append("Xor");
                break;
        }
        writer.append("</Operation>\n");
        }
        if (sourceActorQuery != null) {
            writer.append("\t<SourceActorQuery>\n");
            sourceActorQuery.exportQuery(writer);
            writer.append("\t</SourceActorQuery>\n");
        }
        if (destinationActorQuery != null) {
            writer.append("\t<DestinationActorQuery>\n");
            destinationActorQuery.exportQuery(writer);
            writer.append("\t</DestinationActorQuery>\n");
        }
        writer.append("</LinkByActor>\n");
    }

    void outputLinkEnd(Writer writer, LinkEnd end) throws IOException {
        switch (end) {
            case SOURCE:
                writer.append("Source");
                break;
            case DESTINATION:
                writer.append("Destination");
                break;
            case ALL:
                writer.append("All");
                break;
        }
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            LinkByActor right = (LinkByActor) o;
            if (not && !right.not) {
                return -1;
            }
            if (!not && right.not) {
                return 1;
            }
            if ((sourceActorQuery != null) && (right.sourceActorQuery == null)) {
                return -1;
            }
            if ((sourceActorQuery == null) && (right.sourceActorQuery != null)) {
                return 1;
            }

            if (sourceActorQuery.compareTo(right.sourceActorQuery) != 0) {
                return sourceActorQuery.compareTo(right.sourceActorQuery);
            }
            if ((destinationActorQuery != null) && (right.destinationActorQuery == null)) {
                return -1;
            }
            if ((destinationActorQuery == null) && (right.destinationActorQuery != null)) {
                return 1;
            }

            if (destinationActorQuery.compareTo(right.destinationActorQuery) != 0) {
                return destinationActorQuery.compareTo(right.destinationActorQuery);
            }
            return 0;
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public LinkByActor prototype() {
        return new LinkByActor();
    }

    public Iterator<Link> executeIterator(Graph g, Collection<Actor> sourceActorList, Collection<Actor> destinationActorList, Collection<Link> linkList) {
        if(!not){
            return new LinkIterator(g,sourceActorList,destinationActorList,linkList);
        }else{
            XorLinkQuery xor = (XorLinkQuery)LinkQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<LinkQuery> list = new LinkedList<LinkQuery>();
            LinkByRelation all = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            all.buildQuery(".*",false);
            list.add(all);
            LinkByActor link = this.prototype();
            link.destinationActorQuery=destinationActorQuery;
            link.op=op;
            link.sourceActorQuery=sourceActorQuery;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, sourceActorList,destinationActorList, linkList);
        }
    }

    public class LinkIterator implements Iterator<Link> {

        Iterator<Link> link;
        LinkedList<Link> next = new LinkedList<Link>();
        LinkedList<Link> l;
        Collection<Actor> source;
        Collection<Actor> dest;
        LinkedList<Actor> s = new LinkedList<Actor>();
        LinkedList<Actor> d = new LinkedList<Actor>();
        Graph g;
        boolean remaining = true;

        public LinkIterator(Graph g,Collection<Actor> sourceActorList,Collection<Actor> destinationActorList,Collection<Link>linkList) {
            this.g = g;
            source = sourceActorList;
            dest= destinationActorList;
            if(linkList == null){
                link = g.getLinkIterator();
            }else{
                l = new LinkedList<Link>();
                l.addAll(linkList);
                Collections.sort(l);
                link = l.iterator();
            }
        }

        public boolean hasNext() {
            if(remaining){
                if(next.isEmpty()){
                    while((next.isEmpty())&&(link.hasNext())){
                        next.add(link.next());
                        boolean sourceB = true;
                        s.clear();
                        s.add(next.get(0).getSource());
                        s.retainAll(source);
                        d.clear();
                        d.add(next.get(0).getDestination());
                        d.retainAll(dest);
                        if((sourceActorQuery!=null)&&(!sourceActorQuery.executeIterator(g, s, next).hasNext())){
                            sourceB = false;
                        }
                        boolean destB = true;
                        if((destinationActorQuery!=null)&&(!destinationActorQuery.executeIterator(g, d, next).hasNext())){
                            destB = false;
                        }
                        if(!perform(sourceB,destB)){
                            next.clear();
                        }
                    }
                    if(!next.isEmpty()){
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

        public Link next() {
            hasNext();
            Link ret = next.get(0);
            next.clear();
            return ret;
        }

        protected boolean perform(boolean source, boolean dest){
            switch(op){
                case AND:
                    return source && dest;
                case OR:
                    return source || dest;
                case XOR:
                    if(source && dest){
                        return false;
                    }else if(source || dest){
                        return true;
                    }else{
                        return false;
                    }
                default:
                    return false;
            }
        }

        public void remove() {
            ;
        }
    }
}
