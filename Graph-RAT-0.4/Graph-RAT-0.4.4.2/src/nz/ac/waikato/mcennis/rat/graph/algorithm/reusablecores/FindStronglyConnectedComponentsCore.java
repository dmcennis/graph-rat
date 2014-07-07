/*
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Stack;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Class for finding only components that are strongly connected.  A strongly 
 * connected component is a sub-graph where their exists a path from every actor
 * in the component to every actor in the component.
 * 
 * @author Daniel McEnnis
 */
public class FindStronglyConnectedComponentsCore extends ModelShell{

    private int graphCount = 0;
    LinkedList<Graph> graphs = new LinkedList<Graph>();
    LinkedHashMap<Actor, Integer> number = new LinkedHashMap<Actor, Integer>();
    LinkedHashMap<Actor, Integer> lowLink = new LinkedHashMap<Actor, Integer>();
    Stack<Actor> stack = new Stack<Actor>();
    int actorCount = 0;
    LinkedHashSet<Actor> component = new LinkedHashSet<Actor>();
    String relation = "Knows";
    String mode = "User";
    String graphPrefix = "Component ";
    String graphClass = "MemGraph";

    /**
     * Creates sub-graphs consisting of all strongly connected componenents in the
     * graph.  These sub-graphs are disjoint and spanning of the original graph.
     * @param g graph to be analyzed
     */
    public void execute(Graph g) {
        graphs.clear();
        number.clear();
        lowLink.clear();
        stack.clear();
        component.clear();
        Actor[] actors = g.getActor(mode);
        if (actors != null) {
            fireChange(Scheduler.SET_ALGORITHM_COUNT,actors.length);
            for (int i = 0; i < actors.length; ++i) {
                if (!number.containsKey(actors[i])) {
                    strongConnect(actors[i], g);
                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
            }
        }
    }

    protected void strongConnect(Actor start, Graph g) {
        number.put(start, actorCount);
        lowLink.put(start, actorCount);
        actorCount++;
        stack.push(start);

        Link[] links = g.getLinkBySource(relation, start);
        if (links != null) {
            for (int i = 0; i < links.length; ++i) {
                if (!number.containsKey(links[i].getDestination())) {
                    strongConnect(links[i].getDestination(), g);
                    lowLink.put(start, Math.min(lowLink.get(start), lowLink.get(links[i].getDestination())));
                } else if (number.get(links[i].getDestination()) < number.get(start)) {
                    if (stack.contains(links[i].getDestination())) {
                        lowLink.put(start, Math.min(lowLink.get(start), number.get(links[i].getDestination())));
                    }
                }
            }
        }
        if (lowLink.get(start) == number.get(start)) {
            Actor top = stack.peek();
            while ((stack.size() > 0) && (number.get(top) >= number.get(start))) {
                component.add(top);
                stack.pop();
                if (stack.size() > 0) {
                    top = stack.peek();
                }
            }

            Graph graphRep = getGraph(g);
            graphs.add(graphRep);
            component.clear();
        }
    }

    protected Graph getGraph(Graph g) {
        Graph ret = null;

        try {
            Properties props = new Properties();
            props.setProperty("GraphClass", graphClass);
            props.setProperty("GraphID", graphPrefix + graphCount);
            graphCount++;
            ret = g.getSubGraph(props, component);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /**
     * Set which relation of links in the graph to use
     * @param r relation of links to utilize
     */
    public void setRelation(String r) {
        relation = r;
    }

    /**
     * Set which mode of actors in the graph are to be used
     * @param m mode of actors to utilize
     */
    public void setMode(String m) {
        mode = m;
    }

    /**
     * Relation of link in the graph to use in calculations
     * @return relation of links to utilize
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Which mode of actors in the graph should be used in calculations
     * @return mode of actors to utilize
     */
    public String getMode() {
        return mode;
    }

    /**
     * Array of newly created subgraphs.  Is null no graphs are created. Graphs
     * are not created if 1 or 0 subgrahs are found. Returns null if no graph
     * has been run against the object.
     * @return array of created sub-graphs
     */
    public Graph[] getGraph() {
        if (graphs.size() > 1) {
            return graphs.toArray(new Graph[]{});
        } else {
            return null;
        }
    }

    /**
     * Set the string prefix used to generate ids for new sub-graphs
     * @param g new prefix for graph ids
     */
    public void setGraphPrefix(String g) {
        graphPrefix = g;
    }

    /**
     * Return the prefix used to name new graph-ids
     * @return prefix for sub-graph components
     */
    public String getGraphPrefix() {
        return graphPrefix;
    }

    /**
     * Set the string determining the class of sub-graph to create
     * @param g class of sub-graph to create
     */
    public void setGraphClass(String g) {
        graphClass = g;
    }

    /**
     * return the string representing the class of sub-graph to create
     * @return class of sub-graph to create
     */
    public String getGraphClass() {
        return graphClass;
    }
}
