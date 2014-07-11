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

public class ActorByLinkTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void testExecute() throws Exception {

    }

    public void testExportQuery() throws Exception {

    }

    public void testBuildQuery() throws Exception {

    }

    public void testCompareTo() throws Exception {

    }

    public void testBuildingStatus() throws Exception {

    }

    public void testPrototype() throws Exception {
        ActorByLink ret = new ActorByLink();
        ActorByLink item = ret.prototype();
        assertNotNull(item);
        assertEquals(ActorByLink.class,item.getClass());
    }

    public void testExecuteIterator() throws Exception {

    }
}