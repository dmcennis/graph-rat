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

public class AndActorQueryTest extends TestCase {

    ActorByMode all;
    ActorByMode none;
    ActorByMode a;
    ActorByMode number;
    AndActorQuery query;
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
        query = new AndActorQuery();
    }

    public void testExecuteAAPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteSAPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteUAPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteNAPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteSSPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteUSPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteNSPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteSUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteUUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
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

    public void testExecuteNUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUNPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUSPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUNPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAAPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
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

    public void testExecuteSAPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUAPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUSPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUNPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteASPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSSPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUSPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNSPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteUNPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        SortedSet<Actor> result = query.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorUAPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorNAPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorUSPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorNSPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorSUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorUUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorNUPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUNPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPAA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUSPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNUPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUNPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPLA() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
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
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUAPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUSPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNUPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUNPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPAL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNAPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorASPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSSPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(all);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUSPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNSPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(a);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNUPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(number);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(all);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(a);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorUNPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(number);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNPLL() throws Exception {
        LinkedList<ActorQuery> list = new LinkedList<ActorQuery>();
        list.add(none);
        list.add(none);
        query.buildQuery(list);
        Iterator<Actor> actorIT = query.executeIterator(g.graph, g.actorSet, g.linkSet);
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
        assertEquals(AndActorQuery.class,query.prototype().getClass());
    }
}