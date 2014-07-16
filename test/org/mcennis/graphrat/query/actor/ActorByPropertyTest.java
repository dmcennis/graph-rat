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

import javafx.beans.property.StringProperty;
import junit.framework.TestCase;
import org.dynamicfactory.propertyQuery.NumericQuery;
import org.dynamicfactory.propertyQuery.StringQuery;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.query.G;

import java.util.Iterator;
import java.util.SortedSet;

public class ActorByPropertyTest extends TestCase {

    G g;
    ActorByProperty props;
    StringQuery string;
    NumericQuery number;

    public void setUp() throws Exception {
        g = new G();
        props = new ActorByProperty();
        string = new StringQuery();
        number = new NumericQuery();
    }

    public void testExecuteAPASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
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

    public void testExecuteSNASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteNNASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteAPNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
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

    public void testExecuteSPNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
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

    public void testExecuteNPNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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


    public void testExecuteANNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteSNNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteNNNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteAPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteSPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteNPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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


    public void testExecuteANANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteSNANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteNNANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteAPNNAA() throws Exception {
                number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteSPNNAA() throws Exception {
                number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteNPNNAA() throws Exception {
                number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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


    public void testExecuteANNNAA() throws Exception {
                number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteSNNNAA() throws Exception {
                number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testExecuteNNNNAA() throws Exception {
                number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
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

    public void testBuildQuery() throws Exception {

    }

    public void testExportQuery() throws Exception {

    }

    public void testCompareTo() throws Exception {

    }

    public void testBuildingStatus() throws Exception {

    }

    public void testPrototype() throws Exception {

    }
}