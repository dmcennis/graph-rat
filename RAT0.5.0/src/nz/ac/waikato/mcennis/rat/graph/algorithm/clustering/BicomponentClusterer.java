/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 * 
 * Modified for RAT by Daniel McEnnis Dec 2 2007
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;

import java.util.Collection;
import java.util.HashMap;

import java.util.HashSet;

import java.util.Iterator;
import java.util.LinkedHashSet;

import java.util.LinkedList;

import java.util.List;
import java.util.Map;

import java.util.Set;

import java.util.Stack;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.GraphFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;

import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;

import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;

import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;

import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Finds all biconnected components (bicomponents) of an undirected graph.  
 * A graph is a biconnected component if 
 * at least 2 vertices must be removed in order to disconnect the graph.  (Graphs 
 * consisting of one vertex, or of two connected vertices, are also biconnected.)  Biconnected
 * components of three or more vertices have the property that every pair of vertices in the component
 * are connected by two or more vertex-disjoint paths.
 * <p>
 * Running time: O(|V| + |E|) where |V| is the number of vertices and |E| is the number of edges
 * @see "Depth first search and linear graph algorithms by R. E. Tarjan (1972), SIAM J. Comp."
 * 
 * @author Joshua O'Madadhain
 * 
 * Modified for RAT by Daniel McEnnis
 */
public class BicomponentClusterer extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();
    protected Map<Actor, Number> dfs_num;
    protected Map<Actor, Number> high;
    protected Map<Actor, Actor> parents;
    protected Stack<Link> stack;
    protected int converse_depth;
    protected int graphCount = 0;

    public BicomponentClusterer() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Bicomponent Clusterer");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Bicomponent Clusterer");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Clustering");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkQuery", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("GraphIDPrefix", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("AddContext", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {

        Set<Set<Actor>> bicomponents = new LinkedHashSet<Set<Actor>>();

        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(),".*",false);




        // initialize DFS number for each vertex to 0

        dfs_num = new HashMap<Actor, Number>();

        Iterator<Actor> actorList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        if (actorList.hasNext()) {
            int actorListSize = 0;
            fireChange(Scheduler.SET_ALGORITHM_COUNT, g.getActorCount((String) parameter.get("Mode").get()));
            while (actorList.hasNext()) {

                dfs_num.put(actorList.next(), 0);
                actorListSize++;
            }
            actorList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            int i=0;
            while (actorList.hasNext()) {
                Actor actor = actorList.next();
                if (dfs_num.get(actor).intValue() == 0) // if we haven't hit this vertex yet...
                {

                    high = new HashMap<Actor, Number>();

                    stack = new Stack<Link>();

                    parents = new HashMap<Actor, Actor>();

                    converse_depth = actorListSize;

                    // find the biconnected components for this subgraph, starting from v

                    findBiconnectedComponents(g, actor, bicomponents);



                    // if we only visited one vertex, this method won't have

                    // ID'd it as a biconnected component, so mark it as one

                    if (actorListSize - converse_depth == 1) {

                        Set<Actor> s = new HashSet<Actor>();

                        s.add(actor);

                        bicomponents.add(s);

                    }

                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i++);
            }

        }

        for (Set<Actor> set : bicomponents) {

            try {
                Graph graph = GraphFactory.newInstance().create((String) parameter.get("GraphIDPrefix").get() + graphCount, parameter);
                Iterator<Actor> setIt = set.iterator();
                while (setIt.hasNext()) {
                    graph.add(setIt.next());
                }
                LinkQuery query = (LinkQuery) parameter.get("LinkQuery").get();
                Iterator<Link> link = query.executeIterator(g, set, set, null);
                while (link.hasNext()) {
                    graph.add(link.next());
                }
                if ((Boolean) parameter.get("AddContext").get()) {
                    HashSet<Actor> actorSet = new HashSet<Actor>();
                    actorSet.addAll(graph.getActor());
                    link = query.executeIterator(g, actorSet, null, null);
                    while (link.hasNext()) {
                        Link l = link.next();
                        Actor d = l.getDestination();
                        if (graph.getActor(d.getMode(), d.getID()) == null) {
                            graph.add(d);
                        }
                        if (graph.getLink(l.getRelation(), l.getSource(), l.getDestination()) == null) {
                            graph.add(l);
                        }
                    }

                    link = query.executeIterator(g, null, actorSet, null);
                    while (link.hasNext()) {
                        Link l = link.next();
                        Actor d = l.getSource();
                        if (graph.getActor(d.getMode(), d.getID()) == null) {
                            graph.add(d);
                        }
                        if (graph.getLink(l.getRelation(), l.getSource(), l.getDestination()) == null) {
                            graph.add(l);
                        }
                    }
                }

                graphCount++;

                g.addChild(graph);

            } catch (Exception ex) {

                Logger.getLogger(BicomponentClusterer.class.getName()).log(Level.SEVERE, "Actors or properties are null", ex);

            }

        }
    }

    @Override
    public List<IODescriptor> getInputType() {

        return input;

    }

    @Override
    public List<IODescriptor> getOutputType() {

        return output;

    }

    @Override
    public Properties getParameter() {

        return parameter;

    }

    @Override
    public Parameter getParameter(
            String param) {
        return parameter.get(param);

    }

    /**
     * Parameters to be initialized.  Subclasses should override if they provide
     * any additional parameters or require additional inputs.
     *
     * <ol>
     * <li>'name' - Name of this instance of the algorithm.  Default is ''.
     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.
     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'.
     * <li>'graphClass' - graph class used to create subgraphs
     * <li>'graphIDprefix' - prefix used for graphIDs.
     * </ol>
     * <br>
     * <br>Input 0 - Link
     * <br>Output 0 - Graph
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    null,
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("GraphIDPrefix").get(),
                    null,
                    null,
                    "",
                    true);
            output.add(desc);
        }
    }

    /**
     * <p>Stores, in <code>bicomponents</code>, all the biconnected
     * components that are reachable from <code>v</code>.</p>
     *
     * <p>The algorithm basically proceeds as follows: do a depth-first
     * traversal starting from <code>v</code>, marking each vertex with
     * a value that indicates the order in which it was encountered (dfs_num),
     * and with
     * a value that indicates the highest point in the DFS tree that is known
     * to be reachable from this vertex using non-DFS edges (high).  (Since it
     * is measured on non-DFS edges, "high" tells you how far back in the DFS
     * tree you can reach by two distinct paths, hence biconnectivity.)
     * Each time a new vertex w is encountered, push the edge just traversed
     * on a stack, and call this method recursively.  If w.high is no greater than
     * v.dfs_num, then the contents of the stack down to (v,w) is a
     * biconnected component (and v is an articulation point, that is, a
     * component boundary).  In either case, set v.high to max(v.high, w.high),
     * and continue.  If w has already been encountered but is
     * not v's parent, set v.high max(v.high, w.dfs_num) and continue.
     *
     * <p>(In case anyone cares, the version of this algorithm on p. 224 of
     * Udi Manber's "Introduction to Algorithms: A Creative Approach" seems to be
     * wrong: the stack should be initialized outside this method,
     * (v,w) should only be put on the stack if w hasn't been seen already,
     * and there's no real benefit to putting v on the stack separately: just
     * check for (v,w) on the stack rather than v.  Had I known this, I could
     * have saved myself a few days.  JRTOM)</p>
     *
     */
    protected void findBiconnectedComponents(Graph g, Actor v, Set<Set<Actor>> bicomponents) {

        int v_dfs_num = converse_depth;

        dfs_num.put(v, v_dfs_num);

        converse_depth--;

        high.put(v, v_dfs_num);

        Link[] link = getLinks(g, v);

        if (link != null) {

            for (int i = 0; i <
                    link.length; ++i) {

                int w_dfs_num = dfs_num.get(getOther(link[i], v)).intValue();//get(w, dfs_num);

//            E vw = g.findEdge(v, w);

                if (w_dfs_num == 0) // w hasn't yet been visited
                {

                    parents.put(getOther(link[i], v), v); // v is w's parent in the DFS tree

                    stack.push(link[i]);

                    findBiconnectedComponents(g, getOther(link[i], v), bicomponents);

                    int w_high = high.get(getOther(link[i], v)).intValue();//get(w, high);

                    if (w_high <= v_dfs_num) {

                        // v disconnects w from the rest of the graph,

                        // i.e., v is an articulation point

                        // thus, everything between the top of the stack and

                        // v is part of a single biconnected component

                        Set<Actor> bicomponent = new HashSet<Actor>();

                        Link e;

                        do {

                            e = stack.pop();

                            bicomponent.add(e.getSource());

                            bicomponent.add(e.getDestination());

                        } while (e != link[i]);

                        bicomponents.add(bicomponent);

                    }

                    high.put(v, Math.max(w_high, high.get(v).intValue()));

                } else if (getOther(link[i], v) != parents.get(v)) // (v,w) is a back or a forward edge
                {

                    high.put(v, Math.max(w_dfs_num, high.get(v).intValue()));

                }

            }

        }

    }

    protected Link[] getLinks(Graph g, Actor v) {
        LinkByRelation query = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        query.buildQuery((String)parameter.get("Relation").get(),false);
        HashSet<Actor> actors = new HashSet<Actor>();

        LinkedList<Link> list = new LinkedList<Link>();
        LinkedList<Actor> actor = new LinkedList<Actor>();
        actor.add(v);

        Collection<Link> out = query.execute(g, actors, null, null);
        if (out.size()>0) {
            Iterator<Link> outIt = out.iterator();
            while (outIt.hasNext()) {
                Link l = outIt.next();
                actors.add(l.getDestination());

                list.add(l);

            }

        }

        out = query.execute(g, null, actors, null);
        if (out.size()>0) {
            Iterator<Link> outIt = out.iterator();
            while (outIt.hasNext()) {
                Link l = outIt.next();
                if(!actors.contains(l.getSource())){
                    list.add(l);
                }
            }

        }

        if (list.size() > 0) {

            return list.toArray(new Link[]{});

        } else {

            return null;

        }

    }

    protected Actor getOther(Link l, Actor a) {

        if (l.getSource().equals(a)) {

            return l.getDestination();

        } else {

            return l.getSource();

        }
    }

    public BicomponentClusterer prototype(){
        return new BicomponentClusterer();
    }
}

