/*
 * AggregateOnGraph - created 4/02/2009 - 5:15:05 PM
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
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.algorithm.reusablecores.InstanceManipulation;
import org.mcennis.graphrat.algorithm.reusablecores.aggregator.AggregatorFunction;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregateOnGraph extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public AggregateOnGraph() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate On Graph");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate On Graph");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Aggregator");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
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

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("InnerFunction", AggregatorFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, AggregatorFunction.class);
        name.setRestrictions(syntax);
        name.add("FirstItem");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("OuterFunction", AggregatorFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, AggregatorFunction.class);
        name.setRestrictions(syntax);
        name.add("Sum");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
    }

    public void execute(Graph g) {
        AggregatorFunction innerAggregate = (AggregatorFunction) parameter.get("InnerFunction").get();
        AggregatorFunction outerAggregate = (AggregatorFunction) parameter.get("OuterFunction").get();
        if ((innerAggregate != null) && (outerAggregate != null)) {
            LinkedList<Instance> values = new LinkedList<Instance>();
            LinkedList<Instance> instanceFromProperty = new LinkedList<Instance>();
            Iterator<Property> properties = g.getProperty().iterator();
            while (properties.hasNext()) {
                LinkedList<Instance> actorProperty = InstanceManipulation.propertyToInstance(properties.next());
                double[] weight = new double[actorProperty.size()];
                Arrays.fill(weight, 1.0);
                Instance[] toBeAdded = innerAggregate.aggregate(actorProperty.toArray(new Instance[]{}), weight);
                for (int k = 0; k < toBeAdded.length; ++k) {
                    instanceFromProperty.add(toBeAdded[k]);
                }
            }
            Instance[] result = new Instance[]{};
            if (instanceFromProperty.size() > 0) {
//                        Instance[] data = InstanceManipulation.normalizeFieldNames(instanceFromProperty.toArray(new Instance[]{}), new Instances[]{instanceFromProperty.getFirst().dataset()});
                double[] weights = new double[instanceFromProperty.size()];
                Arrays.fill(weights, 1.0);
                result = outerAggregate.aggregate(instanceFromProperty.toArray(new Instance[]{}), weights);
            }
            for (int j = 0; j < result.length; ++j) {
                values.add(result[j]);
            }
            result = values.toArray(new Instance[]{});
            Instances[] meta = new Instances[result.length];
            for (int j = 0; j < meta.length; ++j) {
                meta[j] = result[j].dataset();
            }
            result = InstanceManipulation.normalizeFieldNames(result, meta);
            Property aggregator = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("DestinationProperty").get()), weka.core.Instance.class);
            for (int i=0;i <result.length; ++i) {
                try {
                    aggregator.add(result[i]);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            if (innerAggregate == null) {
                Logger.getLogger(AggregateOnActor.class.getName()).log(Level.SEVERE, "Inner Aggregator Function does not exist");
            }
            if (outerAggregate == null) {
                Logger.getLogger(AggregateOnActor.class.getName()).log(Level.SEVERE, "Outer Aggregator Function does not exist");
            }
        }
    }

    public AggregateOnGraph prototype() {
        return new AggregateOnGraph();
    }

    public List<IODescriptor> getInputType() {
        return input;
    }

    public List<IODescriptor> getOutputType() {
        return output;
    }

    public Properties getParameter() {
        return parameter;
    }

    public Parameter getParameter(String param) {
        return parameter.get(param);
    }

    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    null,
                    ".*","");
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    null,
                    (String) parameter.get("DestinationProperty").get(),"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);
        }
    }
}
