/*
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.path.PathNode;

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
        int n = 0;
        Actor[] actors = g.getActor(mode);
        if (actors != null) {
            n = actors.length;
            actors = null;
            while (it.hasNext()) {
                Link l = it.next();
                linkMap.put(l, linkMap.get(l) / ((double) 0.5 * n * (n - 1)));
            }
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

