/*
 * Copyright (c) Daniel McEnnis
 *
 *
 *    This file is part of GraphRAT.
 *
 *    GraphRAT is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    GraphRAT is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mcennis.graphrat.query;

import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.graph.MemGraph;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;

import java.util.TreeSet;

/**
 * Created by dmcennis on 7/11/2014.
 */
public class G {
    public MemGraph graph;

    public Actor a;
    public Actor b;
    public Actor c;
    public Actor d;
    public Actor e;

    public Actor a1;
    public Actor a2;
    public Actor a3;
    public Actor a4;

    public Link ab;
    public Link ac;
    public Link bc;
    public Link cd;

    public Link a1a2;
    public Link a2a1;
    public Link a3a4;
    public Link a4a3;

    public Link aa2;
    public Link a3b;

    public Property aP1;
    public Property bP1;
    public Property bP2;
    public Property cP2;

    public TreeSet<Actor> actorSet;
    public TreeSet<Link> linkSet;

    public G(){
        graph = new MemGraph();
        a = ActorFactory.newInstance().create("A","a");
        aP1 = PropertyFactory.newInstance().create("BasicProperty","One",Double.class);
        a.add(aP1);
        try {
            aP1.add(2.0);
        } catch (InvalidObjectTypeException e1) {
            e1.printStackTrace();
        }
        graph.add(a);
        b = ActorFactory.newInstance().create("A","b");
        bP1 = PropertyFactory.newInstance().create("BasicProperty","One",Double.class);
        try {
            bP1.add(0.0);
        } catch (InvalidObjectTypeException e1) {
            e1.printStackTrace();
        }
        b.add(bP1);
        bP2 = PropertyFactory.newInstance().create("BasicProperty","Two",String.class);
        b.add(bP2);
        graph.add(b);
        c = ActorFactory.newInstance().create("A","c");
        cP2 = PropertyFactory.newInstance().create("BasicProperty","Two",String.class);
        try {
            cP2.add("A String");
        } catch (InvalidObjectTypeException e1) {
            e1.printStackTrace();
        }
        graph.add(c);
        d = ActorFactory.newInstance().create("A","d");
        graph.add(d);
        e = ActorFactory.newInstance().create("A","e");
        graph.add(e);

        a1 = ActorFactory.newInstance().create("B","a1");
        graph.add(a1);
        a2 = ActorFactory.newInstance().create("B","a2");
        graph.add(a2);
        a3 = ActorFactory.newInstance().create("B","a3");
        graph.add(a3);
        a4 = ActorFactory.newInstance().create("B","a4");
        graph.add(a4);

        ab = LinkFactory.newInstance().create("Relation");
        ab.set(a,1.0,b);
        graph.add(ab);
        bc = LinkFactory.newInstance().create("Relation");
        bc.set(b, 1.0, c);
        graph.add(bc);
        cd = LinkFactory.newInstance().create("Relation");
        cd.set(c, 1.0, d);
        graph.add(cd);
        ac = LinkFactory.newInstance().create("Relation");
        ac.set(a,1.0,c);
        graph.add(ac);

        a1a2 = LinkFactory.newInstance().create("Links");
        a1a2.set(a1,1.0,a2);
        graph.add(a1a2);
        a2a1 = LinkFactory.newInstance().create("Links");
        a2a1.set(a2,1.0,a1);
        graph.add(a2a1);
        a3a4 = LinkFactory.newInstance().create("Links");
        a3a4.set(a3,1.0,a4);
        graph.add(a3a4);

        aa2 = LinkFactory.newInstance().create("Crossover");
        aa2.set(a,1.0,a2);
        graph.add(aa2);
        a3b = LinkFactory.newInstance().create("Crossover");
        a3b.set(a3,1.0,b);
        graph.add(a3b);

        actorSet = new TreeSet<Actor>();
        linkSet = new TreeSet<Link>();
        actorSet.add(b);
        actorSet.add(d);
        actorSet.add(a1);
        linkSet.add(a1a2);
        linkSet.add(a2a1);
        linkSet.add(bc);

    }
}
