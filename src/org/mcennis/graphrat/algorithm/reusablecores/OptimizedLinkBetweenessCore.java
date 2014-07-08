/*

 *
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.

 */

package org.mcennis.graphrat.algorithm.reusablecores;



import java.util.HashMap;

import java.util.Iterator;

import java.util.Map;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.path.PathNode;



/**

 * Provides link betweeness calculations for algorithms that use it continuosly

 * rather than once.  Use the full algorithm for edge betweeness if possible since

 * this allows bookeeping of which algorithm for edge betweeness was used.

 * 

 * @author Daniel McEnnis

 */

public class OptimizedLinkBetweenessCore extends PathBaseCore {



    protected double maxBetweeness = 0.0;

    protected HashMap<Link, Double> linkMap = new HashMap<Link,Double>();



    @Override

    protected void doCleanup(PathNode[] path, Graph g) {

        Iterator<Link> it = linkMap.keySet().iterator();

        int n = g.getActorCount(mode);

            while (it.hasNext()) {

                Link l = it.next();

                linkMap.put(l, linkMap.get(l) / ((double) 0.5 * n * (n - 1)));

            }

    }



    @Override

    protected void doAnalysis(PathNode[] path, PathNode source) {

        for (int i = 0; i < path.length; ++i) {

            PathNode traversal = path[i];

            while (traversal.getPrevious() != null) {

                Link key = traversal.getPreviousLink();

                if (linkMap.containsKey(key)) {

                    linkMap.put(key, linkMap.get(key) + 1.0);

                }else{

                    linkMap.put(key,1.0);

                }

                traversal = traversal.getPrevious();

            }

        }

    }



    @Override

    protected void setSize(int size) {

       linkMap = new HashMap<Link,Double>();

    }



    /**

     * Return the mapping between links and their betweeness values.  Null values 

     * indicate 0.0 betweeness.  Returns an empty collection if no links exist 

     * in this subgraph

     * @return mapping between links and their betweeness value

     */

    public Map<Link, Double> getLinkMap() {

        return linkMap;

    }

}



