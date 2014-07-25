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
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.G;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;

public class XorActorQueryTest extends TestCase {

    ActorByMode all;
    ActorByMode none;
    ActorByMode a;
    ActorByMode number;
    XorActorQuery query;
    G g;

    public void setUp() throws Exception {
        g = new G();
        all = new ActorByMode();
        all.buildQuery(".*",".*",false);
        none = new ActorByMode();
        none.buildQuery(".*",".*",true);
        a = new ActorByMode();
        a.buildQuery(".*","a.*",false);
        number = new ActorByMode();
        number.buildQuery(".*",".[0-9]+",false);
        query = new XorActorQuery();
    }

    public void testExecuteAAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteUAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteNAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteASAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteSSAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUSAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteAUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteSUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteUUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteANAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteSNAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteUNAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteNNAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUSLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUNLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteASAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUSAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteAUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteSNAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUNAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUSLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUNLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorUAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorNAAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorASAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorSSAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUSAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorAUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorSUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorUUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNUAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorANAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorSNAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorUNAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
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

    public void testExecuteIteratorNNAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNALA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUSLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNULA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUNLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
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

    public void testExecuteIteratorASAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUSAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
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

    public void testExecuteIteratorAUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNUAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
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

    public void testExecuteIteratorSNAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUNAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,null,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNALL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUSLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNULL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUNLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph,g.actorSet,g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExportQuery() throws Exception {

    }

    public void testImportQuery() throws Exception {

    }

    public void testBuildQuery() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
    }

    public void testPrototype() throws Exception {
        assertNotNull(query.prototype());
        assertEquals(XorActorQuery.class,query.prototype().getClass());
    }
}