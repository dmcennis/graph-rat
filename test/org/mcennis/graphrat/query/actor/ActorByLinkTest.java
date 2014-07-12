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
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.query.G;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.mcennis.graphrat.query.link.NullLinkQuery;

import java.util.Iterator;
import java.util.SortedSet;

public class ActorByLinkTest extends TestCase {

    G g;
    ActorByLink actorByLink;
    LinkByRelation all;
    LinkByRelation some;

    public void setUp() throws Exception {
        g = new G();
        actorByLink = new ActorByLink();
        all = new LinkByRelation();
        all.buildQuery(".*",false);
        some = new LinkByRelation();
        some.buildQuery("R.*",false);
    }

    public void testExecuteAPAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
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
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDPAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
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
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
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
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDNAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDPSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteDNSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteSNSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,null);
        Iterator<Actor> actorIT = result.iterator();
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

    public void testExecuteAPALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDPALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDNALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDPSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDNSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
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

    public void testExecuteDPAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
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

    public void testExecuteSPAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDNAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDPSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDNSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,null,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDPALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDNALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDPSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteANSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteDNSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        SortedSet<Actor> result = actorByLink.execute(g.graph,g.actorSet,g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExportQuery() throws Exception {

    }

    public void testBuildQuery() throws Exception {
        ActorByLink ret = new ActorByLink();
        NullLinkQuery n = new NullLinkQuery();
        n.buildQuery();
    }

    public void testCompareTo() throws Exception {
        fail("not written yet.");
    }

    public void testBuildingStatus() throws Exception {
        ActorByLink ret = new ActorByLink();
        assertEquals(Query.State.UNINITIALIZED,ret.buildingStatus());
        NullLinkQuery n = new NullLinkQuery();
        n.buildQuery();
        ret.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,n);
        assertEquals(Query.State.READY,ret.buildingStatus());
    }

    public void testPrototype() throws Exception {
        ActorByLink ret = new ActorByLink();
        ActorByLink item = ret.prototype();
        assertNotNull(item);
        assertEquals(ActorByLink.class,item.getClass());
    }


    public void testExecuteIteratorAPAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
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

    public void testExecuteIteratorDPAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDNAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a3));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNAAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.e));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a4));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDPSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorDNSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorSNSAA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorAPALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDPALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDNALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNALA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDPSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDNSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNSLA() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
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

    public void testExecuteIteratorDPAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
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

    public void testExecuteIteratorSPAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDNAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNAAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDPSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDNSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNSAL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDPALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDNALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNALL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,all);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDPSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,false,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorANSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.ALL,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorDNSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.DESTINATION,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNSLL() throws Exception {
        actorByLink.buildQuery(LinkQuery.LinkEnd.SOURCE,true,some);
        Iterator<Actor> actorIT = actorByLink.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }
}