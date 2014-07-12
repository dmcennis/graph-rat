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
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.actor.G;

import java.util.*;

public class ActorByModeTest extends TestCase {
    ActorByMode mode;
    G g;
    TreeSet<Actor> actorSet;
    TreeSet<Link> linkSet;
    public void setUp() throws Exception {
        g = new G();
        actorSet = new TreeSet<Actor>();
        linkSet = new TreeSet<Link>();
        actorSet.add(g.b);
        actorSet.add(g.d);
        actorSet.add(g.a1);
        linkSet.add(g.a1a2);
        linkSet.add(g.a2a1);
        linkSet.add(g.bc);
    }

    public void testExecuteAAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAAPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAANAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSANAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNANAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,null,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAAPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAANLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSANLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNANLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        SortedSet<Actor> actor = mode.execute(g.graph,actorSet,linkSet);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
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

    public void testExecuteIteratorAAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        SortedSet<Actor> actor = mode.execute(g.graph,null,null);
        Iterator<Actor> actorIT = actor.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNANAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNAA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNANLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNLA() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAANAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSANAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNANAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNAL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,null,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",false);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAANLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSANLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNANLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*",".*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","a.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery(".*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("A.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNLL() throws Exception {
        mode = new ActorByMode();
        mode.buildQuery("Q.*","E.*",true);
        Iterator<Actor> actorIT = mode.executeIterator(g.graph,actorSet,linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }
}