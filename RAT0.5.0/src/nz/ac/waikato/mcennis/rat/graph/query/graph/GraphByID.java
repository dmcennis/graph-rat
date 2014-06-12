/**
 * GraphByID
 * Created Jan 10, 2009 - 9:58:53 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.graph;

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.XMLParserObject.State;
import nz.ac.waikato.mcennis.rat.graph.query.GraphQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphByID implements GraphQuery {

    Pattern pattern = Pattern.compile(".*");
    
    transient State state = State.UNINITIALIZED;

    public Collection<Graph> execute(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        HashSet<Graph> result = new HashSet<Graph>();
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

    protected Collection<Graph> getGraphSet(Graph g) {
        LinkedList<Graph> result = new LinkedList<Graph>();
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

    public Iterator<Graph> executeIterator(Graph g, Collection<Actor> actorList, Collection<Link> linkList) {
        return execute(g,actorList,linkList).iterator();
    }
}
