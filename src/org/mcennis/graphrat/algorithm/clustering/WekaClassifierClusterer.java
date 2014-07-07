/**

 * Created 3/06/2008 - 15:11:32

 * Copyright Daniel McEnnis, see license.txt

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

import java.util.HashMap;

import java.util.HashSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.algorithm.Algorithm;

import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptor;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;

import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.actor.ActorByMode;
import weka.clusterers.Clusterer;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;

import weka.core.Instances;

import weka.core.OptionHandler;

/**

 * Utilize an arbitrary classifier-clusterer (clusterer that assigns an unknown cluster

 * to exactly one category). Which clusterer used is determined by parameter.

 * 

 * @author Daniel McEnnis

 */
public class WekaClassifierClusterer extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public WekaClassifierClusterer() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Weka Classifier Clusterer");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Weka Classifier Clusterer");
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

        name = ParameterFactory.newInstance().create("Clusterer", Class.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Class.class);
        name.setRestrictions(syntax);
        name.add(weka.classifiers.meta.AdaBoostM1.class);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Options", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("");
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {

        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("GroundMode").get(),".*",false);

        try {

            Clusterer clusterer = (Clusterer) ((Class) parameter.get("Clusterer").get()).newInstance();
            String[] options = ((String) parameter.get("Options").get()).split("\\s+");

            ((OptionHandler) clusterer).setOptions(options);

            Iterator<Actor> actor = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            Instances dataSet = null;
            while (actor.hasNext()) {
                Actor a = actor.next();
                Property property = a.getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get()));

                if (!property.getValue().isEmpty()) {

                    Instance value = (Instance) property.getValue().get(0);

                    if ((dataSet == null) && (value.dataset() != null)) {
                        FastVector attributes = new FastVector();
                        for (int i = 0; i < value.dataset().numAttributes(); ++i) {
                            attributes.addElement(value.dataset().attribute(i));
                        }
                        dataSet = new Instances("Clustering", attributes, 1000);
                    } else if ((dataSet == null)) {
                        FastVector attributes = new FastVector();
                        for (int i = 0; i < value.numAttributes(); ++i) {
                            Attribute element = new Attribute(Integer.toString(i));
                            attributes.addElement(element);
                        }
                        dataSet = new Instances("Clustering", attributes, 1000);
                    }
                    dataSet.add(value);
                }

            }
            clusterer.buildClusterer(dataSet);
            actor = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            HashMap<Integer, Graph> clusters = new HashMap<Integer, Graph>();
            while (actor.hasNext()) {
                Actor a = actor.next();
                Property property = a.getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get()));
                if (!property.getValue().isEmpty()) {

                    Instance instance = (Instance) property.getValue().get(0);
                    int cluster = -1;

                    try {

                        cluster = clusterer.clusterInstance(instance);
                        if (!clusters.containsKey(cluster)) {
                            Graph graph = GraphFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("GraphID").get() + cluster), parameter);
                            clusters.put(cluster, graph);
                        }
                        clusters.get(cluster).add(a);
                    } catch (Exception ex) {

                        Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, "ClusterInstance on clusterer failed", ex);

                    }

                    Property clusterProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String) parameter.get("DestinationProperty").get()), Integer.class);

                    clusterProperty.add(new Integer(cluster));

                    a.add(clusterProperty);

                }
            }

            Iterator<Graph> graphIt = clusters.values().iterator();
            while (graphIt.hasNext()) {
                LinkQuery query = (LinkQuery) parameter.get("LinkQuery").get();
                Graph graph = graphIt.next();
                Iterator<Link> link = query.executeIterator(g, graph.getActor(), graph.getActor(), null);
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
            }

        } catch (InstantiationException ex) {

            Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {

            Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, null, ex);

        } catch (Exception ex) {

            Logger.getLogger(WekaClassifierClusterer.class.getName()).log(Level.SEVERE, null, ex);

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

     * Parameters:<br/>

     * <br/>

     * <ul>

     * <li/><b>name</b>: name of this Algorithm.  Default is 'Weka Classifier Clustering Algorithm'

     * <li/><b>class</b>: class of the clusterer. Default is 'weka.clusterers.Cobweb'

     * <li/><b>options</b>: string of options for this clusterer. Default is ''

     * <li/><b>instancesProperty</b>: ID of the graph property containing the 

     * Instances object. Deafult is 'AudioFile'

     * <li/><b>actorType</b>: mode of the actor containing the Instance property to

     * be clustered. Default is 'User'

     * <li/><b>actorProperty</b>: ID of the property where Instance objects are stored.

     * Default is 'AudioFile'

     * <li/><b>destinationProperty</b>: ID of the property to create on each actor.

     * Deafult is 'clusterID'

     * <li/><b>storeClassifier</b>: is the classifier to be stored on the graph.

     * Default is 'false'

     * <li/><b>classifierProperty</b>: property ID for storing the built classifier.

     * Deafult is 'clusterer'

     * </ul>

     * @param map parameters to be loaded - may be null.

     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("SourceProperty").get(),
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

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

    public WekaClassifierClusterer prototype(){
        return new WekaClassifierClusterer();
    }
}

