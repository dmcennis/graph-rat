/*
 * NullLinkQuery - created 31/01/2009 - 10:32:18 PM
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
package org.mcennis.graphrat.query.link;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;

import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.NullQuery;
import org.mcennis.graphrat.query.LinkQuery;

/**
 *
 * @author Daniel McEnnis
 */
public class NullLinkQuery extends NullQuery implements LinkQuery{

    public SortedSet<Link> execute(Graph g, SortedSet<Actor> sourceActorList, SortedSet<Actor> destinationActorList, SortedSet<Link> linkList) {
        return super.execute();
    }

    public Iterator<Link> executeIterator(Graph g, SortedSet<Actor> sourceActorList, SortedSet<Actor> destinationActorList, SortedSet<Link> linkList) {
        return super.executeIterator();
    }

    public void buildQuery(){
        super.buildQuery("Link");
    }

    public NullLinkQuery prototype() {
        return new NullLinkQuery();
    }

}
