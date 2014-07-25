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
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
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
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
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
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
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
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPNNAA() throws Exception {
                number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
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
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
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


    public void testExecuteANNNAA() throws Exception {
                number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
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

    public void testExecuteAPASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPNNLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, null);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteAPASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
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

    public void testExecuteNNASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
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

    public void testExecuteAPNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
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

    public void testExecuteAPANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPNNAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
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


    public void testExecuteANNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
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

    public void testExecuteNNNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, null, g.linkSet);
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

    public void testExecuteAPASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteAPNNLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSPNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNPNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteANNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteSNNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteNNNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        SortedSet<Actor> result = props.execute(g.graph, g.actorSet, g.linkSet);
        Iterator<Actor> actorIT = result.iterator();
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
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

    public void testExecuteIteratorAPASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorSNASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorNNASAA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorAPNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorSPNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorSNNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNSAA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorAPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorSNANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNANAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPNNAA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
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

    public void testExecuteIteratorSPNNAA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a));
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

    public void testExecuteIteratorNPNNAA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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


    public void testExecuteIteratorANNNAA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
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

    public void testExecuteIteratorSNNNAA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorNNNNAA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, null);
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

    public void testExecuteIteratorAPASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNASLA() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNSLA() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNANLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPNNLA() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNNLA() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, null);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.d));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorAPASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
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

    public void testExecuteIteratorNNASAL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
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

    public void testExecuteIteratorAPNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNSAL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
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

    public void testExecuteIteratorAPANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNANAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPNNAL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.c));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
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


    public void testExecuteIteratorANNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a2));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
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

    public void testExecuteIteratorNNNNAL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
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

    public void testExecuteIteratorAPASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, null, g.linkSet);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNASLL() throws Exception {
        string.buildQuery(".*",false, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",false,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery(".*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("O.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNSLL() throws Exception {
        string.buildQuery(".*",true, StringQuery.Operation.MATCHES);
        props.buildQuery("A.*",true,string);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("O.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNANLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery("A.*",false,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorAPNNLL() throws Exception {
        number.buildQuery(500.0,false, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSPNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNPNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }


    public void testExecuteIteratorANNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery(".*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorSNNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("O.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

    public void testExecuteIteratorNNNNLL() throws Exception {
        number.buildQuery(500.0,true, NumericQuery.Operation.NE);
        props.buildQuery("A.*",true,number);
        Iterator<Actor> actorIT = props.executeIterator(g.graph, g.actorSet, g.linkSet);
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.b));
        assertTrue(actorIT.hasNext());
        assertEquals(0,actorIT.next().compareTo(g.a1));
        assertFalse(actorIT.hasNext());
    }

}