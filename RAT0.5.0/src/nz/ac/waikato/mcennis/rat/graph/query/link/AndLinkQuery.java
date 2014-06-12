/**
 * AndActorQuery
 * Created Jan 31, 2009 - 7:11 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.query.link;

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
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.AndQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class AndLinkQuery extends AndQuery implements LinkQuery{
    
    transient Graph g = null;

    transient Collection<Actor> sourceActorList = null;

    transient Collection<Actor> destActorList = null;

    transient Collection<Link> linkList = null;
    
    public Collection<Link> execute(Graph g,Collection<Actor> sourceActorList,Collection<Actor> destActorList, Collection<Link> linkList){
	this.g = g;
	this.sourceActorList = sourceActorList;
	this.destActorList = destActorList;
	this.linkList = linkList;
	Collection<Link> ret = execute(linkList);
	this.g = null;
	this.destActorList = null;
	this.sourceActorList = null;
	this.linkList = null;
	return ret;
    }

    public Iterator<Link> executeIterator(Graph g,Collection<Actor> sourceActorList,Collection<Actor> destActorList, Collection<Link> linkList){
	this.g = g;
	this.sourceActorList = sourceActorList;
	this.destActorList = destActorList;
	this.linkList = linkList;
	Iterator<Link> ret = executeIterator(linkList);
	this.g = null;
	this.destActorList = null;
	this.sourceActorList = null;
	this.linkList = null;
	return ret;
    }

    protected Collection<Link> executeComponent(Object query){
	return ((LinkQuery)query).execute(g,sourceActorList,destActorList,linkList);
    }

    protected Iterator<Link> executeIterator(Object query){
	return ((LinkQuery)query).execute(g,sourceActorList,destActorList,linkList).iterator();
    }

    protected void exportQuery(Object object,Writer writer) throws IOException{
	((LinkQuery)object).exportQuery(writer);
    }

    public void importQuery(Reader reader) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void buildQuery(Collection<LinkQuery> source){
	super.buildQuery(source,"Link");
    }

    
    public AndLinkQuery prototype(){
        return new AndLinkQuery();
    }

}
