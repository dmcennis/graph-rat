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

package org.mcennis.graphrat.query.actor;

import junit.framework.TestCase;
import org.dynamicfactory.propertyQuery.Query;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.graph.MemGraph;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public class ActorByModeTest extends TestCase {

    ActorByMode mode;

    MemGraph graph;

    Actor a;
    Actor b;
    Actor c;
    Actor d;
    Actor e;
    
    Actor a1;
    Actor a2;
    Actor a3;
    Actor a4;

    Link ab;
    Link ac;
    Link bc;
    Link cd;

    Link a1a2;
    Link a2a1;
    Link a3a4;
    Link a4a3;

    public void setUp() throws Exception {
        graph = new MemGraph();
        a = ActorFactory.newInstance().create("A","a");
        graph.add(a);
        b = ActorFactory.newInstance().create("A","b");
        graph.add(b);
        c = ActorFactory.newInstance().create("A","c");
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
        a4a3 = LinkFactory.newInstance().create("Links");
        a4a3.set(a4,1.0,a3);
        graph.add(a4a3);
    }

    public void testExecuteAAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);

    }

    public void testExecuteSAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*",".*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteASPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSSPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","a.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNSPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteANPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSNPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","E.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNNPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteAANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*",".*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteASNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSSNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","a.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNSNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteANNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSNNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","E.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNNNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteAAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*",".*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteASPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSSPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","a.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNSPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteANPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        Vector<Actor> actors = new Vector<Actor>();
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSNPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","E.*",false);
        Vector<Actor> actors = new Vector<Actor>();
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNNPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        Vector<Actor> actors = new Vector<Actor>();
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteAANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        Vector<Actor> actors = new Vector<Actor>();
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*",".*",true);
        Vector<Actor> actors = new Vector<Actor>();
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        Vector<Actor> actors = new Vector<Actor>();
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteASNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        Vector<Actor> actors = new Vector<Actor>();
        Collection<Actor> actor = mode.execute(graph,actors,null);
    }

    public void testExecuteSSNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","a.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNSNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteANNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteSNNALA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("R.*","E.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testExecuteNNNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        Collection<Actor> actor = mode.execute(graph,null,null);
    }

    public void testBuildQuery() throws Exception {
        ActorByMode entry = new ActorByMode();
        assertEquals(Query.State.UNINITIALIZED,entry.buildingStatus());
        entry.buildQuery(".*",".*",false);
        assertEquals(Query.State.READY,entry.buildingStatus());
    }

    public void testExportQuery() throws Exception {

    }

    public void testCompareTo() throws Exception {

    }

    public void testBuildingStatus() throws Exception {

    }

    public void testPrototype() throws Exception {
        ActorByMode mode = new ActorByMode();
        ActorByMode ret = mode.prototype();
        assertNotNull(ret);
        assertEquals(ActorByMode.class,ret.getClass());
    }

    public void testExecuteIterator() throws Exception {

    }
}