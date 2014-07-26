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

package org.mcennis.graphrat.query.link;

import junit.framework.TestCase;
import org.dynamicfactory.propertyQuery.Query;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.G;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.actor.ActorByMode;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class LinkByActorTest extends TestCase {

    G g;
    ActorByMode all;
    ActorByMode some;
    ActorByMode none;
    TreeSet<Actor> extraSet;
    LinkByActor query;

    public void setUp() throws Exception {
        g = new G();
        all = new ActorByMode();
        all.buildQuery(".*",".*",false);
        some = new ActorByMode();
        some.buildQuery(".*","a.*",false);
        none = new ActorByMode();
        none.buildQuery(".*",".*",true);
        extraSet = new TreeSet<Actor>();
        extraSet.add(g.a);
        extraSet.add(g.a3);
        query = new LinkByActor();
    }

    public void testBuildQuery() throws Exception {
        query.buildQuery(false,all,all, LinkQuery.SetOperation.OR);
    }

    public void testExecutePAAAAAA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAAAAA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAAAAA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAAAAA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAAAAA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAAAAA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAAAAA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAAAAA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASAAAA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASAAAA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSAAAA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSAAAA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSAAAA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSAAAA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSAAAA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSAAAA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANAAAA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANAAAA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNAAAA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNAAAA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNAAAA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNAAAA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNAAAA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNAAAA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUAAAA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUAAAA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUAAAA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUAAAA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUAAAA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUAAAA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUAAAA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUAAAA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOAAA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOAAA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOAAA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOAAA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOAAA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOAAA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOAAA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOAAA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOAAA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOAAA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOAAA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOAAA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOAAA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOAAA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOAAA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOAAA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOAAA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOAAA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOAAA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOAAA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOAAA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOAAA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOAAA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOAAA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOAAA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOAAA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOAAA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOAAA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOAAA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOAAA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOAAA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOAAA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXAAA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXAAA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXAAA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXAAA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXAAA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXAAA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXAAA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXAAA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXAAA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXAAA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXAAA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXAAA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXAAA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXAAA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXAAA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXAAA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXAAA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXAAA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXAAA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXAAA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXAAA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXAAA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXAAA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXAAA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXAAA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXAAA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXAAA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXAAA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXAAA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXAAA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXAAA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXAAA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }



    public void testExecutePAAALAA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAALAA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAALAA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAALAA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAALAA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAALAA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAALAA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAALAA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASALAA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASALAA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSALAA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSALAA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSALAA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSALAA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSALAA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSALAA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANALAA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANALAA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNALAA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNALAA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNALAA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNALAA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNALAA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNALAA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUALAA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUALAA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUALAA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUALAA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUALAA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUALAA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUALAA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUALAA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOLAA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOLAA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOLAA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOLAA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOLAA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOLAA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOLAA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOLAA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOLAA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOLAA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOLAA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOLAA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOLAA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOLAA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOLAA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOLAA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOLAA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOLAA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOLAA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOLAA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOLAA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOLAA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOLAA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOLAA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOLAA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOLAA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOLAA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOLAA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOLAA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOLAA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOLAA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOLAA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXLAA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXLAA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXLAA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXLAA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXLAA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXLAA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXLAA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXLAA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXLAA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXLAA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXLAA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXLAA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXLAA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXLAA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXLAA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXLAA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXLAA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXLAA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXLAA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXLAA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXLAA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXLAA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXLAA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXLAA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXLAA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXLAA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXLAA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXLAA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXLAA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXLAA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXLAA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXLAA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }







    public void testExecutePAAAALA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAAALA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAAALA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAAALA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAAALA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAAALA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAAALA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAAALA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASAALA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASAALA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSAALA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSAALA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSAALA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSAALA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSAALA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSAALA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANAALA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANAALA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNAALA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNAALA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNAALA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNAALA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNAALA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNAALA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUAALA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUAALA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUAALA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUAALA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUAALA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUAALA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUAALA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUAALA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOALA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOALA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOALA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOALA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOALA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOALA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOALA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOALA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOALA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOALA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOALA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOALA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOALA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOALA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOALA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOALA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOALA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOALA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOALA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOALA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOALA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOALA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOALA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOALA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOALA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOALA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOALA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOALA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOALA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOALA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOALA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOALA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXALA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXALA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXALA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXALA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXALA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXALA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXALA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXALA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXALA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXALA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXALA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXALA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXALA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXALA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXALA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXALA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXALA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXALA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXALA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXALA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXALA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXALA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXALA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXALA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXALA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXALA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXALA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXALA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXALA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXALA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXALA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXALA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }



    public void testExecutePAAALLA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAALLA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAALLA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAALLA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAALLA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAALLA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAALLA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAALLA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASALLA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASALLA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSALLA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSALLA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSALLA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSALLA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSALLA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSALLA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANALLA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANALLA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNALLA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNALLA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNALLA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNALLA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNALLA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNALLA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUALLA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUALLA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUALLA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUALLA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUALLA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUALLA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUALLA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUALLA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOLLA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOLLA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOLLA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOLLA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOLLA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOLLA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOLLA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOLLA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOLLA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOLLA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOLLA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOLLA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOLLA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOLLA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOLLA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOLLA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOLLA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOLLA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOLLA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOLLA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOLLA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOLLA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOLLA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOLLA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOLLA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOLLA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOLLA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOLLA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOLLA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOLLA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOLLA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOLLA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXLLA() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXLLA() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXLLA() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXLLA() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXLLA() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXLLA() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXLLA() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXLLA() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXLLA() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXLLA() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXLLA() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXLLA() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXLLA() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXLLA() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXLLA() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXLLA() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXLLA() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXLLA() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXLLA() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXLLA() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXLLA() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXLLA() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXLLA() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXLLA() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXLLA() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXLLA() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXLLA() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXLLA() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXLLA() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXLLA() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXLLA() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXLLA() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,null);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }
















    public void testExecutePAAAAAL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAAAAL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAAAAL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAAAAL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAAAAL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAAAAL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAAAAL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAAAAL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASAAAL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASAAAL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSAAAL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSAAAL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSAAAL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSAAAL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSAAAL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSAAAL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANAAAL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANAAAL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNAAAL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNAAAL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNAAAL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNAAAL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNAAAL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNAAAL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUAAAL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUAAAL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUAAAL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUAAAL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUAAAL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUAAAL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUAAAL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUAAAL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOAAL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOAAL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOAAL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOAAL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOAAL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOAAL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOAAL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOAAL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOAAL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOAAL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOAAL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOAAL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOAAL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOAAL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOAAL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOAAL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOAAL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOAAL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOAAL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOAAL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOAAL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOAAL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOAAL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOAAL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOAAL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOAAL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOAAL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOAAL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOAAL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOAAL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOAAL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOAAL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXAAL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXAAL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXAAL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXAAL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXAAL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXAAL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXAAL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXAAL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXAAL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXAAL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXAAL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXAAL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXAAL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXAAL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXAAL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXAAL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXAAL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXAAL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXAAL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXAAL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXAAL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXAAL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXAAL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXAAL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXAAL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXAAL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXAAL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXAAL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXAAL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXAAL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXAAL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXAAL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a1a2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a4a3));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }



    public void testExecutePAAALAL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAALAL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAALAL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAALAL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAALAL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAALAL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAALAL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAALAL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASALAL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASALAL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSALAL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSALAL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSALAL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSALAL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSALAL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSALAL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANALAL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANALAL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNALAL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNALAL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNALAL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNALAL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNALAL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNALAL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUALAL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUALAL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUALAL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUALAL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUALAL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUALAL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUALAL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUALAL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOLAL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOLAL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOLAL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOLAL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOLAL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOLAL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOLAL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOLAL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOLAL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOLAL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOLAL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOLAL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOLAL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOLAL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOLAL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOLAL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOLAL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOLAL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOLAL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOLAL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOLAL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOLAL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOLAL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOLAL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOLAL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOLAL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOLAL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOLAL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOLAL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOLAL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOLAL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOLAL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXLAL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXLAL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXLAL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXLAL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXLAL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXLAL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXLAL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXLAL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXLAL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXLAL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXLAL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXLAL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXLAL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXLAL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXLAL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXLAL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXLAL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXLAL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXLAL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXLAL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXLAL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXLAL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXLAL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXLAL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXLAL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXLAL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXLAL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXLAL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXLAL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXLAL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXLAL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXLAL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,null,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.aa2));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3a4));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ac));
        assertFalse(linkIT.hasNext());
    }







    public void testExecutePAAAALL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAAALL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAAALL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAAALL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAAALL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAAALL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAAALL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAAALL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASAALL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASAALL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSAALL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSAALL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSAALL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSAALL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSAALL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSAALL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANAALL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANAALL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNAALL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNAALL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNAALL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNAALL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNAALL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNAALL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUAALL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUAALL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUAALL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUAALL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUAALL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUAALL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUAALL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUAALL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOALL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOALL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOALL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOALL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOALL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOALL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOALL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOALL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOALL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOALL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOALL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOALL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOALL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOALL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOALL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOALL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOALL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOALL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOALL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOALL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOALL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOALL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.bc));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOALL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOALL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOALL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOALL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOALL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOALL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOALL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOALL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOALL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOALL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXALL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXALL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXALL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXALL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXALL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXALL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXALL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXALL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXALL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXALL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXALL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXALL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXALL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXALL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXALL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXALL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXALL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXALL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXALL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXALL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXALL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXALL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXALL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXALL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXALL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXALL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXALL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXALL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXALL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXALL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXALL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXALL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,null,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a2a1));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.cd));
        assertFalse(linkIT.hasNext());
    }



    public void testExecutePAAALLL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAALLL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAALLL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAALLL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAALLL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAALLL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAALLL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAALLL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASALLL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASALLL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSALLL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSALLL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSALLL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSALLL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSALLL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSALLL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANALLL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANALLL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNALLL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNALLL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNALLL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNALLL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNALLL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNALLL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUALLL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUALLL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUALLL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUALLL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUALLL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUALLL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUALLL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUALLL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.AND);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAOLLL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAOLLL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAOLLL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAOLLL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAOLLL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAOLLL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAOLLL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAOLLL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASOLLL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASOLLL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSOLLL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSOLLL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSOLLL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSOLLL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSOLLL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSOLLL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANOLLL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANOLLL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNOLLL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNOLLL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNOLLL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNOLLL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNOLLL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNOLLL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUOLLL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUOLLL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUOLLL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUOLLL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUOLLL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUOLLL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUOLLL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUOLLL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.OR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAAXLLL() throws Exception {
        query.buildQuery(false,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAAXLLL() throws Exception {
        query.buildQuery(true,all,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSAXLLL() throws Exception {
        query.buildQuery(false,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSAXLLL() throws Exception {
        query.buildQuery(true,some,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNAXLLL() throws Exception {
        query.buildQuery(false,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNAXLLL() throws Exception {
        query.buildQuery(true,none,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUAXLLL() throws Exception {
        query.buildQuery(false,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUAXLLL() throws Exception {
        query.buildQuery(true,null,all,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePASXLLL() throws Exception {
        query.buildQuery(false,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNASXLLL() throws Exception {
        query.buildQuery(true,all,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSSXLLL() throws Exception {
        query.buildQuery(false,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSSXLLL() throws Exception {
        query.buildQuery(true,some,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNSXLLL() throws Exception {
        query.buildQuery(false,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNSXLLL() throws Exception {
        query.buildQuery(true,none,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUSXLLL() throws Exception {
        query.buildQuery(false,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUSXLLL() throws Exception {
        query.buildQuery(true,null,some,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePANXLLL() throws Exception {
        query.buildQuery(false,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNANXLLL() throws Exception {
        query.buildQuery(true,all,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSNXLLL() throws Exception {
        query.buildQuery(false,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSNXLLL() throws Exception {
        query.buildQuery(true,some,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNNXLLL() throws Exception {
        query.buildQuery(false,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNNXLLL() throws Exception {
        query.buildQuery(true,none,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUNXLLL() throws Exception {
        query.buildQuery(false,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUNXLLL() throws Exception {
        query.buildQuery(true,null,none,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePAUXLLL() throws Exception {
        query.buildQuery(false,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNAUXLLL() throws Exception {
        query.buildQuery(true,all,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePSUXLLL() throws Exception {
        query.buildQuery(false,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNSUXLLL() throws Exception {
        query.buildQuery(true,some,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePNUXLLL() throws Exception {
        query.buildQuery(false,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNNUXLLL() throws Exception {
        query.buildQuery(true,none,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testExecutePUUXLLL() throws Exception {
        query.buildQuery(false,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertFalse(linkIT.hasNext());
    }

    public void testExecuteNUUXLLL() throws Exception {
        query.buildQuery(true,null,null,LinkQuery.SetOperation.XOR);
        SortedSet<Link> result = query.execute(g.graph,extraSet,g.actorSet,g.linkSet2);
        Iterator<Link> linkIT = result.iterator();
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.a3b));
        assertTrue(linkIT.hasNext());
        assertEquals(0,linkIT.next().compareTo(g.ab));
        assertFalse(linkIT.hasNext());
    }

    public void testBuildingStatus() throws Exception {
        assertEquals(Query.State.UNINITIALIZED,query.buildingStatus());
        query.buildQuery(false,all,all, LinkQuery.SetOperation.OR);
        assertEquals(Query.State.READY,query.buildingStatus());
    }

    public void testPrototype() throws Exception {
        assertNotNull(query.prototype());
        assertEquals(LinkByActor.class,query.prototype().getClass());
    }

    public void testExecuteIterator() throws Exception {

    }
}