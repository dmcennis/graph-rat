/*
 * AlgorithmMacros - created 4/02/2009 - 10:06:57 PM
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

package org.mcennis.graphrat.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.actor.AndActorQuery;
import org.mcennis.graphrat.query.link.AndLinkQuery;
import org.dynamicfactory.descriptors.Properties;

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
