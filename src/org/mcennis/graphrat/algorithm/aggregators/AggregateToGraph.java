/**
 * Jul 23, 2008-9:31:14 PM
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
package org.mcennis.graphrat.algorithm.aggregators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.algorithm.reusablecores.InstanceManipulation;
import org.mcennis.graphrat.algorithm.reusablecores.aggregator.AggregatorFunction;
import weka.core.Instance;

/**
 *
 * Class for taking a property of a given ID across actors of a given mode and 
 * aggregating these properties into a single property on the enclosing graph. 
 * The source property is converted into a Weka Instance object if it is not already.
 * 
 * There are two places aggregating functions are used---once where multiple 
 * values attached to the property on a single actor and the other aggregating
 * to a graph property from all actors.  Each of these two aggregators utilize an
 * aggregator helper function determined by parameter. 
 * 
 * @author Daniel McEnnis
 */
public class AggregateToGraph extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateToGraph() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate By Graph");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate By Graph");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregator");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("InnerFunction",AggregatorFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,AggregatorFunction.class);
        name.setRestrictions(syntax);
        name.add("FirstItem");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("OuterFunction",AggregatorFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,AggregatorFunction.class);
        name.setRestrictions(syntax);
        name.add("Sum");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("GraphProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        ActorByMode query = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        query.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Iterator<Actor> actor = AlgorithmMacros.filterActor(parameter, g, query, null, null);
        AggregatorFunction innerAggregate = (AggregatorFunction)parameter.get("InnerFunction").get();
        AggregatorFunction outerAggregate = (AggregatorFunction)parameter.get("OuterFunction").get();
        if ((innerAggregate != null) && (outerAggregate != null)) {
            if (actor != null) {
                LinkedList<Instance> instanceFromProperty = new LinkedList<Instance>();
                while (actor.hasNext()) {
                    Property properties = actor.next().getProperty(AlgorithmMacros.getSourceID(parameter,g, (String)parameter.get("ActorProperty").get()));
                    if (properties != null) {
                        LinkedList<Instance> actorProperty = InstanceManipulation.propertyToInstance(properties);
                        double[] weight = new double[actorProperty.size()];
                        Arrays.fill(weight, 1.0);
                        Instance[] toBeAdded = innerAggregate.aggregate(actorProperty.toArray(new Instance[]{}), weight);
                        for (int k = 0; k < toBeAdded.length; ++k) {
                            instanceFromProperty.add(toBeAdded[k]);
                        }
                    }
                }
                Instance[] result = new Instance[]{};
                if (instanceFromProperty.size() > 0) {
//                    Instance[] data = InstanceManipulation.normalizeFieldNames(instanceFromProperty.toArray(new Instance[]{}), new Instances[]{instanceFromProperty.getFirst().dataset()});
                    double[] weights = new double[instanceFromProperty.size()];
                    Arrays.fill(weights, 1.0);
                    result = outerAggregate.aggregate(instanceFromProperty.toArray(new Instance[]{}), weights);
                }

                Property aggregator = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("GraphProperty").get()),weka.core.Instance.class);
                for (int j = 0; j < result.length; ++j) {
                    try {
                        aggregator.add(result[j]);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                g.add(aggregator);
            } else {
                Logger.getLogger(AggregateToGraph.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter.get("Mode").get() + "' are present");
            }
        } else {
            if (innerAggregate == null) {
                Logger.getLogger(AggregateToGraph.class.getName()).log(Level.SEVERE, "Inner Aggregator Function does not exist");
            }
            if (outerAggregate == null) {
                Logger.getLogger(AggregateToGraph.class.getName()).log(Level.SEVERE, "Outer Aggregator Function does not exist");
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
    public Parameter getParameter(String param) {
        return parameter.get(param);
    }
    /**
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of the algorithm. Default is 'Aggregate By Graph'
     * <li/><b>actorType</b>: Mode of the actor to aggregate over. Default is 'tag'
     * <li/><b>innerAggregatorFunction</b>: aggregator function to use over values inside 
     * an actor property. Default is 'org.mcennis.graphrat.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction'
     * <li/><b>outerAggregatorFunction</b>: aggregator function to use over all 
     * actors. Deafult is 'org.mcennis.graphrat.algorithm.reusablecores.aggregator.SumAggregator'
     * <li/><b>actorProperty</b>: ID of the actor property to aggregate across.  Default is 'actorProperty'
     * <li/><b>graphProperty</b>: ID of the graph property to create. By default, it is
     * the value of 'outerAggregatorFunction' property concatenated with a space and the value 
     * of 'actorProperty' property.
     * </ul>
     * @param map parameters to be loaded - may be null
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("ActorProperty").get(),"");
                if((Boolean)parameter.get("SourceAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("GraphProperty").get(),"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);
        }
    }

    public AggregateToGraph prototype(){
        return new AggregateToGraph();
    }
}
