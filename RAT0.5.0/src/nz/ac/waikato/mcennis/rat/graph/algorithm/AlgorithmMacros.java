/*
 * AlgorithmMacros - created 4/02/2009 - 10:06:57 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.AndActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.AndLinkQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class AlgorithmMacros {

    public static Collection<Link> filterLink(Properties param, Graph g, Collection<Link> collection){
	if(param.get("LinkFilter").get()==null){
	    return collection;
	}else{
	    return ((LinkQuery)param.get("LinkFilter").get()).execute(g,null,null,collection);
	}
    }

    public static Iterator<Link> filterLink(Properties param, Graph g, LinkQuery query, Collection<Actor> source, Collection<Actor> dest, Collection<Link> link){
        if(param.get("LinkFilter").get() == null){
            return query.executeIterator(g, source, dest, link);
        }else{
            AndLinkQuery ret = new AndLinkQuery();
            LinkedList<LinkQuery> list = new LinkedList<LinkQuery>();
            list.add((LinkQuery)param.get("LinkFilter").get());
            list.add(query);
            ret.buildQuery(list);
            return ret.executeIterator(g, source, dest, link);
        }
    }

    public static Iterator<Actor> filterActor(Properties param, Graph g, ActorQuery query, Collection<Actor> actor, Collection<Link> link){
        if(param.get("LinkFilter").get() == null){
            return query.executeIterator(g, actor, link);
        }else{
            AndActorQuery ret = new AndActorQuery();
            LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
            list.add((ActorQuery)param.get("ActorFilter").get());
            list.add(query);
            ret.buildQuery(list);
            return ret.executeIterator(g, actor, link);
        }
    }

    public static Collection<Actor> filterActor(Properties param, Graph g, Collection<Actor> collection){
	if(param.get("ActorFilter").get()==null){
	    return collection;
	}else{
        ActorQuery query = (ActorQuery)param.get("ActorFilter").get();
	    return query.execute(g, collection, null);
	}
    }

    public static String getSourceID(Properties properties, Graph g, String id){
        if(properties.get("AppendGraphID")!=null){
            if((Boolean)properties.get("AppendGraphID").get()){
                return id+g.getID();
            }
        }
        return id;
    }

    public static String getDestID(Properties properties, Graph g, String id){
        if(properties.get("AppendGraphID")!=null){
            if((Boolean)properties.get("AppendGraphID").get()){
                return id+g.getID();
            }
        }
        return id;
    }

}
