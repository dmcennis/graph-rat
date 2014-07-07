/*
 * OptimizedCloseness.java
 *
 * Created on 22 October 2007, 14:36
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
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
package org.mcennis.graphrat.algorithm;

import java.util.Arrays;
import java.util.HashSet;

import java.util.Hashtable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.dynamicfactory.descriptors.*;

import org.mcennis.graphrat.link.Link;

import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.path.PathNode;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.mcennis.graphrat.scheduler.Scheduler;

/**
 * Class that calculates geodesic paths using Djikstra's spanning tree algorithm
 * Hooks are provided for subclasses to use each spanning tree for one actor so
 * that the algorithms can become linear in space rather than O(n2).
 *
 * @author Daniel McEnnis
 * 
 */
public abstract class OptimizedPathBase extends ModelShell implements Algorithm {

    protected PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    protected LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    protected LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();
    Hashtable<Actor, Integer> actorToID = new Hashtable<Actor, Integer>();
    protected PathNode[] path = null;

    /** Creates a new instance of OptimizedCloseness */
    public OptimizedPathBase() {
        ParameterInternal name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
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

        name = ParameterFactory.newInstance().create("Normalize", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);
    }

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

        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);

        Actor[] a = (AlgorithmMacros.filterActor(parameter, g, mode.execute(g,null,null))).toArray(new Actor[]{});

        if (a != null) {
            Arrays.sort(a);
            for (int i = 0; i < a.length; ++i) {

                actorToID.put(a[i], i);

            }

            path = new PathNode[a.length];
            fireChange(Scheduler.SET_ALGORITHM_COUNT, a.length);
            for (int i = 0; i < path.length; ++i) {

                path[i] = new PathNode();

                path[i].setId(i);

                path[i].setActor(a[i]);

            }

            setSize(path.length);



            // determine max length of each element sequentially

            for (int i = 0; i < path.length; ++i) {

                Logger.getLogger(OptimizedPathBase.class.getName()).log(Level.FINE,"Creating paths from source " + i);

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
                        LinkedList<Actor> source = new LinkedList<Actor>();
                        source.add(currentSource.getActor());

                        Link[] linkSet = (AlgorithmMacros.filterLink(parameter, g, relation.execute(g, source, null, null))).toArray(new Link[]{});

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
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
            }

            doCleanup(path, g);
        } else {
            Logger.getLogger(OptimizedPathBase.class.getName()).log(Level.WARNING,"No Actors of type '" + (String) parameter.get("Mode").get() + "' were found");
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

    protected void compare(PathNode current, PathNode next, Link link) {

        double totalCost = current.getCost() + link.getStrength();

        if (next.getCost() > totalCost) {

            next.setPrevious(current);

            next.setPreviousLink(link);

            next.setCost(totalCost);

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
    public abstract void init(Properties map) ;

    public abstract Algorithm prototype();
}

