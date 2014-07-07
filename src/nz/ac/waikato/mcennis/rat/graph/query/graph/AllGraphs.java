/**
 * AllGraphs
 * Created Jan 30, 2009 - 10:08 pm
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph;

import java.io.IOException;
import nz.ac.waikato.mcennis.rat.graph.query.*;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class AllGraphs implements GraphQuery{

    transient State state = State.UNINITIALIZED;

    public void buildQuery(){
	state = State.READY;
    }
    
    public Collection<Graph> execute(Graph restriction, Collection<Actor> actorList, Collection<Link> linkList) {
        LinkedList<Graph> result = new LinkedList<Graph>();
	addChildren(result,restriction);
    Collections.sort(result);
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

    public void addChildren(LinkedList<Graph> result,Graph parent){
	Collection<Graph> children = parent.getChildren();
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

    public Iterator<Graph> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
