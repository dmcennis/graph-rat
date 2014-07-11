/**
 * ActorByMode
 * Created Jan 10, 2009 - 7:49:27 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.query.actor;
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

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class ActorByMode implements ActorQuery {

    String mode = ".*";
    String id = ".*";
    boolean not = false;
    transient State state = State.UNINITIALIZED;

    public SortedSet<Actor> execute(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        TreeSet<Actor> result = new TreeSet<Actor>();
        if (g == null) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Null graph collection - empty set returned by default");
            return result;
        }
        LinkedList<Actor> actor = new LinkedList<Actor>();
        Iterator<String> modes = g.getActorTypes().iterator();
        while (modes.hasNext()) {
            String modeName = modes.next();
            if (!not && modeName.matches(mode)) {
                actor.addAll(g.getActor(modeName));
            } else if (not && !modeName.matches(mode)) {
                actor.addAll(g.getActor(modeName));
            }
        }
        if (actorList != null) {
            actor.retainAll(actorList);
        }
        Iterator<Actor> it_actor = actor.iterator();
        while (it_actor.hasNext()) {
            Actor a = it_actor.next();
            if (a.getID().matches(id)) {
                if (!not) {
                    result.add(a);
                }
            } else if (not) {
                result.add(a);
            }
        }
        return result;
    }

    public void buildQuery(String mode, String id, boolean not) {
        state = State.LOADING;
        this.mode = mode;
        if (mode == null) {
            this.mode = "";
        }
        this.id = id;
        if(id == null){
            this.id = "";
        }
        this.not = not;
        state = State.READY;
    }

    public void exportQuery(Writer writer) throws IOException {
        writer.append("<ActorByMode>\n");
        if (not) {
            writer.append("<Not/>");
        }
        writer.append("<Mode>").append(mode).append("</Mode>\n");
        writer.append("</ActorByMode>");
    }

    public int compareTo(Object o) {
        if (o.getClass().getName().contentEquals(this.getClass().getName())) {
            ActorByMode right = (ActorByMode) o;
            if (mode.compareTo(right.mode) != 0) {
                return mode.compareTo(right.mode);
            }
            if (not && !right.not) {
                return -1;
            }
            if (!not && right.not) {
                return 1;
            }
            return 0;
        } else {
            return this.getClass().getName().compareTo(o.getClass().getName());
        }
    }

    public State buildingStatus() {
        return state;
    }

    public ActorByMode prototype() {
        return new ActorByMode();
    }

    public Iterator<Actor> executeIterator(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
        if(!not){
            return new ActorIterator(g,actorList,linkList);
        }else{
            XorActorQuery xor = (XorActorQuery)ActorQueryFactory.newInstance().create("XorActorQuery");
            LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
            ActorByMode all = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
            all.buildQuery(".*", ".*", false);
            list.add(all);
            ActorByMode link = this.prototype();
            link.id=id;
            link.mode=mode;
            list.add(link);
            xor.buildQuery(list);
            return xor.executeIterator(g, actorList, linkList);
        }
    }

    public class ActorIterator implements Iterator<Actor> {

        Actor next = null;
        boolean remaining = true;
        Graph g;
        Iterator<String> modeMatches;
        Iterator<Actor> rIt;

        public ActorIterator(Graph g, SortedSet<Actor> actorList, SortedSet<Link> linkList) {
            LinkedList<String> modeList = new LinkedList<String>();
            Iterator<String> source = g.getActorTypes().iterator();
            while (source.hasNext()) {
                String s = source.next();
                if (s.matches(mode)) {
                    modeList.add(s);
                }
            }
            modeMatches = modeList.iterator();
            if (actorList != null) {
                LinkedList<Actor> r = new LinkedList<Actor>();
                r.addAll(actorList);
                Collections.sort(r);
                rIt = r.iterator();
                modeMatches = new LinkedList<String>().iterator();
            } else {
                modeMatches = modeList.iterator();
                rIt = g.getActorIterator(modeMatches.next());
            }

        }

        public boolean hasNext() {
            if (remaining) {
                if (next == null) {
                    if (!rIt.hasNext() && !modeMatches.hasNext()) {
                        remaining = false;
                        return false;
                    }
                    while (!rIt.hasNext() && modeMatches.hasNext()) {
                        rIt = g.getActorIterator(modeMatches.next());
                    }
                    if (rIt.hasNext()) {
                        next = rIt.next();
                        while ((!next.getMode().matches(mode) || !next.getID().matches(id)) && (rIt.hasNext())) {
                            next = rIt.next();
                            while (!rIt.hasNext() && modeMatches.hasNext()) {
                                rIt = g.getActorIterator(modeMatches.next());
                            }
                        }
                        if((!next.getMode().matches(mode) || !next.getID().matches(id))){
                            remaining = false;
                            return false;
                        }else{
                            return true;
                        }
                    } else {
                        remaining = false;
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        public Actor next() {
            hasNext();
            Actor ret = next;
            next = null;
            return ret;
        }

        public void remove() {
            ;
        }
    }
}
