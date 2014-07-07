/*

 *  Copyright Daniel McEnnis, see license.txt

 */
/*
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

package org.mcennis.graphrat.algorithm.clustering;




import java.util.Collection;
import java.util.HashSet;

import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import java.util.logging.Logger;
import org.dynamicfactory.descriptors.*;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.algorithm.Algorithm;

import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;

import org.mcennis.graphrat.link.Link;

import org.dynamicfactory.model.ModelShell;


import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.mcennis.graphrat.reusablecores.FindStronglyConnectedComponentsCore;
import org.mcennis.graphrat.reusablecores.OptimizedLinkBetweenessCore;
import org.mcennis.graphrat.scheduler.Scheduler;

import org.mcennis.graphrat.util.Duples;



/**

 * Creates clusters by removing each edge sequentially by order of link betweeness.

 * Betweeness is calculated once at te outset, then used so seperate all components

 * thereafter

 * 

 * @author Daniel McEnnis

 */

public class TraditionalEdgeBetweenessClustering extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    Graph graph;

    Duples<Double, Link>[] orderedBetweeness;

    OptimizedLinkBetweenessCore core = new OptimizedLinkBetweenessCore();

    FindStronglyConnectedComponentsCore stronglyConnected = new FindStronglyConnectedComponentsCore();

    int top;

    

    public TraditionalEdgeBetweenessClustering(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Traditional Edge Betweeness Clustering");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Traditional Edge Betweeness Clustering");
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

        name = ParameterFactory.newInstance().create("LinkQuery", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkQuery.class);
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

        fireChange(Scheduler.SET_ALGORITHM_COUNT,2);

        core.execute(g);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);

        Map<Link, Double> linkMap = core.getLinkMap();

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);

        Collection<Link> links = AlgorithmMacros.filterLink(parameter, g, relation.execute(g,null,null,null));

        if (links.size()>0) {
            Iterator<Link> linkIt = links.iterator();
            while (linkIt.hasNext()) {
                Link l = linkIt.next();
                if (!linkMap.containsKey(l)) {

                    linkMap.put(l, 0.0);

                }

            }

        }

        orderedBetweeness = new Duples[linkMap.size()];

        Iterator<Link> it = linkMap.keySet().iterator();

        int count = 0;

        top = 0;

        while (it.hasNext()) {

            Link l = it.next();

            orderedBetweeness[count] = new Duples<Double,Link>();

            orderedBetweeness[count].setLeft(linkMap.get(l));

            orderedBetweeness[count].setRight(l);

            count++;

        }

        java.util.Arrays.sort(orderedBetweeness);

        splitGraph(g, count - 1,(String)parameter.get((String)parameter.get("GraphIDPrefix").get()).get());

    }



    protected void splitGraph(Graph base, int top,String prefix) {

        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        try {

            HashSet<Actor> actorSet = new HashSet<Actor>();

            Collection<Actor> actors = AlgorithmMacros.filterActor(parameter, base, mode.execute(base, null, null));

            if ((actors.size()>1) ) {

                Logger.getLogger(TraditionalEdgeBetweenessClustering.class.getName()).log(Level.FINE, " Splitting "+actors.size()+" actors - ");
                Iterator<Actor> actorIt = actors.iterator();
                while (actorIt.hasNext()) {
                    Actor actor = actorIt.next();
                    actorSet.add(actor);

                    Logger.getLogger(TraditionalEdgeBetweenessClustering.class.getName()).log(Level.FINE, actor.getID()+" ");

                }

                Logger.getLogger(TraditionalEdgeBetweenessClustering.class.getName()).log(Level.FINE, "");

                Graph rootGraph = getGraph(base,actorSet,prefix);
                //if one or fewer actors, return graph unaltered

                //else

                stronglyConnected.setGraphPrefix(prefix);

                stronglyConnected.execute(rootGraph);

                Graph[] components = stronglyConnected.getGraph();

                if (components != null) {

                    for (int i = 0; i < components.length; ++i) {

                        splitGraph(components[i], top,prefix+i);

                        base.addChild(components[i]);

                    }

                } else {

                    while (stronglyConnected.getGraph() == null) {

                        if (rootGraph.getLink(core.getRelation(), orderedBetweeness[top].getRight().getSource(), orderedBetweeness[top].getRight().getDestination()) == null) {

                            --top;

                        } else {

                            rootGraph.remove(orderedBetweeness[top].getRight());

                            stronglyConnected.setGraphPrefix(prefix);

                            stronglyConnected.execute(rootGraph);

                        }

                    }

                    components = stronglyConnected.getGraph();

                    for (int i = 0; i < components.length; ++i) {

                        splitGraph(components[i],top,prefix+i);

                        base.addChild(components[i]);

                    }

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

        protected Graph getGraph(Graph g, HashSet<Actor> component, String prefix){

        try {
                Graph graph = GraphFactory.newInstance().create(prefix,parameter);
                Iterator<Actor> it = component.iterator();
                while(it.hasNext()){
                    graph.add(it.next());
                }
                LinkQuery query = (LinkQuery) parameter.get("LinkQuery").get();
                Iterator<Link> link = query.executeIterator(g, component, component, null);
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
                return graph;
        } catch (Exception ex) {

            Logger.getLogger(FindWeaklyConnectedComponents.class.getName()).log(Level.SEVERE, null, ex);

        }
        return null;
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

    public Parameter getParameter(String param) {
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

     * <li>'normalize' - boolean for whether or not to normalize prestige vectors. 

     * Default 'false'.

     * </ol>

     * <br>

     * <br>Input 0 - Link

     * <br>NOTE - subclasses define the ouput - see subclasses for output information

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

   public TraditionalEdgeBetweenessClustering prototype(){
       return new TraditionalEdgeBetweenessClustering();
   }

}

