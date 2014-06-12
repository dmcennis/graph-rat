/**
 * NullActorQuery
 * Created Jan 11, 2009 - 3:04:51 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;

/**
 *
 * @author Daniel McEnnis
 */
public class NullQuery<Type extends Comparable> {

    State state = State.UNINITIALIZED;
    
    String type = "Graph";

    boolean passAll = false;

    protected Collection<Type> execute() {
        return new LinkedList<Type>();
    }

    protected Iterator<Type> executeIterator(){
        return new LinkedList<Type>().iterator();
    }

    public void exportQuery(Writer writer) throws IOException{
        writer.append("<NullQuery Class=\""+type+"\"/>\n");
    }

    public int compareTo(Object o) {
        return this.getClass().getName().compareTo(o.getClass().getName());
    }
    
    protected void buildQuery(String type){
        this.type = type;
        state = State.READY;
    }

   public State buildingStatus() {
        return state;
    }
    
}   
