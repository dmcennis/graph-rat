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
import org.mcennis.graphrat.query.G;

public class NullActorQueryTest extends TestCase {

    NullActorQuery all;
    NullActorQuery none;
    G g;
    public void setUp() throws Exception {
        g = new G();
        all = new NullActorQuery();
    }

    public void testExecuteAll() throws Exception {
        all.buildQuery();
        assertNotNull(all.execute(g.graph,null,null));
        assertEquals(0,all.execute(g.graph,null,null).size());
    }

    public void testExecuteLimit() throws Exception {
        all.buildQuery();
        assertNotNull(all.execute(g.graph,g.actorSet,g.linkSet));
        assertEquals(0,all.execute(g.graph,g.actorSet,g.linkSet).size());
    }

    public void testExecuteIteratorAll() throws Exception {
        all.buildQuery();
        assertNotNull(all.executeIterator(g.graph,null,null));
        assertFalse(all.executeIterator(g.graph,null,null).hasNext());
    }

    public void testExecuteIteratorLimit() throws Exception {
        all.buildQuery();
        assertNotNull(all.executeIterator(g.graph,g.actorSet,g.linkSet));
        assertFalse(all.executeIterator(g.graph,g.actorSet,g.linkSet).hasNext());
    }

    public void testBuildQuery() throws Exception {
        all.buildQuery();
    }

    public void testPrototype() throws Exception {
        assertNotNull(all.prototype());
        assertEquals(NullActorQuery.class,all.prototype().getClass());
    }
}