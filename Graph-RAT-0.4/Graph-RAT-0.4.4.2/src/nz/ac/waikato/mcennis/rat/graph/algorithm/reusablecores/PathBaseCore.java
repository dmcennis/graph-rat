/*
 * Copyright Daniel McEnnis under BSD license
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.path.PathNode;

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

    /**
     * Generates a spanning tree for a given actor.  Calls the abstract methods:
     * <br>setSize - gives the derived class the size of the user list.
     * <br>doAnalysis - gives a set of PathNodes and the root of the tree.
     * <br>doCleanup - allows any extra calculations to be made before the algorithm
     * returns control to the scheduler.
     *
     */
    public void execute(Graph g) {
        HashSet<PathNode> lastSet = new HashSet<PathNode>();
        HashSet<PathNode> nextSet = new HashSet<PathNode>();
        HashSet<PathNode> seen = new HashSet<PathNode>();
        Actor[] a = g.getActor(mode);
        if (a != null) {
            Arrays.sort(a);
            for (int i = 0; i < a.length; ++i) {
                actorToID.put(a[i], i);
            }
            path = new PathNode[a.length];
            for (int i = 0; i < path.length; ++i) {
                path[i] = new PathNode();
                path[i].setId(i);
                path[i].setActor(a[i]);
                path[i].setCost(Double.POSITIVE_INFINITY);
            }
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
                while (lastSet.size() > 0) {
                    Iterator<PathNode> last_it = lastSet.iterator();
                    while (last_it.hasNext()) {
                        PathNode currentSource = last_it.next();
                        Link[] linkSet = g.getLinkBySource(relation, currentSource.getActor());
                        if (linkSet != null) {
                            for (int j = 0; j < linkSet.length; ++j) {
                                PathNode dest = path[actorToID.get(linkSet[j].getDestination())];
                                compare(currentSource, dest, linkSet[j]);
                                if (!seen.contains(dest)) {
                                    nextSet.add(dest);
                                }
                                seen.add(dest);
                            }
                        }
                    }
                    lastSet = nextSet;
                    nextSet = new HashSet<PathNode>();
                }
                doAnalysis(path, path[i]);
            }
            doCleanup(path, g);
        }
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
    }

    /**
     * Returns which relation of links to use in path calculations
     * 
     * @return relation of links utilized
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Set the mode of actors to use for calculations
     * @param m mode of actors to utilize
     */
    public void setMode(String m) {
        mode = m;
    }

    /**
     * Return which mode of actors are to be used for shortest path calculations
     * 
     * @return mode of actors to use
     */
    public String getMode() {
        return mode;
    }
}
