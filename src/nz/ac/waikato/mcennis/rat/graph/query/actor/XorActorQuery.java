/**
 * AndActorQuery
 * Created Jan 31, 2009 - 7:11 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.actor;

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
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.XorQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class XorActorQuery extends XorQuery implements ActorQuery{
    
    transient Graph g = null;

    transient Collection<Actor> actorList = null;

    transient Collection<Link> linkList = null;
    
    public Collection<Actor> execute(Graph g,Collection<Actor> actorList, Collection<Link> linkList){
	this.g = g;
	this.actorList = actorList;
	this.linkList = linkList;
	Collection<Actor> ret = execute(actorList);
	this.g = null;
	this.actorList = null;
	this.linkList = null;
	return ret;
    }

    public Iterator<Actor> executeIterator(Graph g,Collection<Actor> actorList, Collection<Link> linkList){
	this.g = g;
	this.actorList = actorList;
	this.linkList = linkList;
	Iterator<Actor> ret = executeIterator(actorList);
	this.g = null;
	this.actorList = null;
	this.linkList = null;
	return ret;
    }

    protected Collection<Actor> executeComponent(Object query){
	return ((ActorQuery)query).execute(g,actorList,linkList);
    }

    protected Iterator<Actor> executeIterator(Object query){
	return ((ActorQuery)query).execute(g,actorList,linkList).iterator();
    }

    protected void exportQuery(Object object,Writer writer) throws IOException{
	((ActorQuery)object).exportQuery(writer);
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(Collection<ActorQuery> source){
	super.buildQuery(source,"Actor");
    }

    
    public XorActorQuery prototype(){
        return new XorActorQuery();
    }
}
