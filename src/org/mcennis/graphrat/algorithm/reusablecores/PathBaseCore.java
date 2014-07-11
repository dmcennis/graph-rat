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




import java.util.*;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.path.PathNode;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;


/**

 * Core abstract class for algorithm cores that utilize shortest-path info

 * in their algorithms.  It creates shortest-path using Djikstra's algorithm

 * for each source actor, allows performing of caluclations then, and allows 

 * algorithms to perform additional calculatio afterwards.

 * 

 * @author Daniel McEnnis

 */

public abstract class PathBaseCore {



    HashMap<Actor, Integer> actorToID = new HashMap<Actor, Integer>();

    PathNode[] path = null;

    String relation = "Knows";

    String mode = "User";

    Properties parameter = PropertiesFactory.newInstance().create();

    /**

     * Generates a spanning tree for a given actor.  Calls the abstract methods:

     * <br>setSize - gives the derived class the size of the user list.

     * <br>doAnalysis - gives a set of PathNodes and the root of the tree.

     * <br>doCleanup - allows any extra calculations to be made before the algorithm

     * returns control to the scheduler.

     *

     */

    public void execute(Graph g) {

        TreeSet<PathNode> lastSet = new TreeSet<PathNode>();

        TreeSet<PathNode> nextSet = new TreeSet<PathNode>();

        TreeSet<PathNode> seen = new TreeSet<PathNode>();

        TreeSet<Actor> sortActor = new TreeSet<Actor>();

        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        sortActor.addAll(AlgorithmMacros.filterActor(parameter, g, mode.execute(g, null, null)));

        int count = 0;

        Iterator<Actor> a = sortActor.iterator();

        while (a.hasNext()) {

            actorToID.put(a.next(), count++);

        }

        path = new PathNode[sortActor.size()];

        a = sortActor.iterator();

        count = 0;

        while (a.hasNext()) {

            path[count] = new PathNode();

            path[count].setId(count);

            path[count].setActor(a.next());

            path[count].setCost(Double.POSITIVE_INFINITY);

            count++;

        }

        sortActor = null;

        setSize(path.length);



        // determine max length of each element sequentially

        for (int i = 0; i < path.length; ++i) {

            Logger.getLogger(PathBaseCore.class.getName()).log(Level.FINE, "Creating paths from source " + i);

            lastSet.clear();

            nextSet.clear();

            seen.clear();

            lastSet.add(path[i]);

            seen.add(path[i]);

            for (int j = 0; j < path.length; ++j) {

                path[j].setCost(Double.POSITIVE_INFINITY);

                path[j].setPrevious(null);

            }

            path[i].setCost(0.0);

            LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            relation.buildQuery((String)parameter.get("Relation").get(),false);

            while (lastSet.size() > 0) {

                Iterator<PathNode> last_it = lastSet.iterator();

                while (last_it.hasNext()) {

                    PathNode currentSource = last_it.next();
                    TreeSet<Actor> actor = new TreeSet<Actor>();
                    actor.add(currentSource.getActor());
                    Iterator<Link> linkSet = AlgorithmMacros.filterLink(parameter, g, relation, actor, null, null);

                    while (linkSet.hasNext()) {

                        Link link = linkSet.next();

                        PathNode dest = path[actorToID.get(link.getDestination())];

                        compare(currentSource, dest, link);

                        if (!seen.contains(dest)) {

                            nextSet.add(dest);

                        }

                        seen.add(dest);

                    }

                }

                lastSet = nextSet;

                nextSet = new TreeSet<PathNode>();

            }

            doAnalysis(path, path[i]);

        }

        doCleanup(path, g);

    }



    protected void compare(PathNode current, PathNode next, Link link) {

        double totalCost = current.getCost() + link.getStrength();

        if (next.getCost() > totalCost) {

            next.setPrevious(current);

            next.setPreviousLink(link);

            next.setCost(totalCost);

        }

    }



    /**

     * Any additional calculations to be performed before control returns to the

     * scheduler

     * @param path 

     * @param g 

     */

    protected abstract void doCleanup(PathNode[] path, Graph g);



    /**

     * 

     * @param path 

     * @param source 

     */

    protected abstract void doAnalysis(PathNode[] path, PathNode source);



    /**

     * 

     * @param size 

     */

    protected abstract void setSize(int size);



    /**

     * Set which relation of links to use in path calculations

     * 

     * @param r relation of links to utilize

     */

    public void setRelation(String r) {

        relation = r;
        parameter.set("Relation", relation);

    }



    /**

     * Returns which relation of links to use in path calculations

     * 

     * @return relation of links utilized

     */

    public String getRelation() {
        if(parameter.get("Relation").getValue().size()>0){
            return (String)parameter.get("Relation").get();
        }
        return relation;

    }



    /**

     * Set the mode of actors to use for calculations

     * @param m mode of actors to utilize

     */

    public void setMode(String m) {
        parameter.set("Mode", m);
        mode = m;

    }



    /**

     * Return which mode of actors are to be used for shortest path calculations

     * 

     * @return mode of actors to use

     */

    public String getMode() {
        if(parameter.get("Mode").getValue().size()>0){
            return (String)parameter.get("Mode").get();
        }

        return mode;

    }

    public Properties getProperties(){
        return parameter;
    }

    public void setProperties(Properties props){
        parameter = props;
    }

}

