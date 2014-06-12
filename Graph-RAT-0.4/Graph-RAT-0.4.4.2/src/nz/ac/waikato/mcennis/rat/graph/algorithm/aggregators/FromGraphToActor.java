/**
 * Jul 26, 2008-9:17:07 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.InstanceManipulation;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.AggregatorFunction;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.AggregatorFunctionFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.ConcatenationAggregator;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.SumAggregator;
import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptorInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptorInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * Class aggregates all the properties of a graph and propogates them to each actor
 * in the graph.  
 * 
 * All properties are transformed into Weka Instance objects first, 
 * then the values of each property are aggregated, then the results are aggregated
 * into Instance values. Finally, this new set of Instance objects are propogated 
 * onto each of the actors in the graph in a single property.
 * 
 * @author Daniel McEnnis
 */
public class FromGraphToActor extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[5];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[1];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public FromGraphToActor() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[1].getValue());
        Properties props = new Properties();

        props.setProperty("AggregatorFunction", (String) parameter[2].getValue());
        AggregatorFunction graphProperty = AggregatorFunctionFactory.newInstance().create(props);


        props.setProperty("AggregatorFunction", (String) parameter[3].getValue());
        AggregatorFunction graph = AggregatorFunctionFactory.newInstance().create(props);

        if ((graphProperty != null) && (graph != null)) {
            Property[] graphProperties = g.getProperty();
            LinkedList<Instance> graphInstances = new LinkedList<Instance>();
            if (graphProperties != null) {
                for (int i = 0; i < graphProperties.length; ++i) {
//                    Property[] properties = actor[i].getProperty();
//                    if(properties != null){
//                        for(int j=0;j<properties.length;++j){
                    LinkedList<Instance> actorProperty = InstanceManipulation.propertyToInstance(graphProperties[i]);
                    double[] weight = new double[actorProperty.size()];
                    Arrays.fill(weight, 1.0);
                    Instance[] toBeAdded = graphProperty.aggregate(actorProperty.toArray(new Instance[]{}), weight);
                    for (int k = 0; k < toBeAdded.length; ++k) {
                        graphInstances.add(toBeAdded[k]);
                    }
//                        }
                }
                Instance[] result = new Instance[]{};
                if (graphInstances.size() > 0) {
//                    Instance[] data = InstanceManipulation.normalizeFieldNames(graphInstances.toArray(new Instance[]{}), new Instances[]{graphInstances.getFirst().dataset()});
                    double[] weights = new double[graphInstances.size()];
                    Arrays.fill(weights, 1.0);
                    result = graph.aggregate(graphInstances.toArray(new Instance[]{}), weights);
                }
                if (actor != null) {
                    for (int i = 0; i < actor.length; ++i) {
                        props.setProperty("PropertyType", "Basic");
                        props.setProperty("PropertyClass", "weka.core.Instance");
                        props.setProperty("PropertyID", (String) parameter[4].getValue());
                        Property aggregator = PropertyFactory.newInstance().create(props);
                        for (int j = 0; j < result.length; ++j) {
                            try {
                                aggregator.add(result[j]);
                            } catch (InvalidObjectTypeException ex) {
                                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        actor[i].add(aggregator);
                    }
                } else {
                    Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter[1].getValue() + "' are present");
                }
            }
        } else {
            if (graphProperty == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Inner Aggregator Function '" + (String) parameter[2].getValue() + "' does not exist");
            }
            if (graph == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Outer Aggregator Function '" + (String) parameter[3].getValue() + "' does not exist");
            }
        }
    }

    @Override
    public InputDescriptor[] getInputType() {
        return input;
    }

    @Override
    public OutputDescriptor[] getOutputType() {
        return output;
    }

    @Override
    public Parameter[] getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(
            String param) {
        for (int i = 0; i <
                parameter.length; ++i) {
            if (parameter[i].getName().contentEquals(param)) {
                return parameter[i];
            }

        }
        return null;
    }

    @Override
    public SettableParameter[] getSettableParameter() {
        return null;
    }

    @Override
    public SettableParameter getSettableParameter(
            String param) {
        return null;
    }

    /**
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of the algorithm. Default is 'From Graph To Actor'
     * <li/><b>actorType</b>: Mode of the actor to compare. Default is 'tag'
     * <li/><b>graphPropertyAggregator</b>: function for aggregating the values
     * of properties on a graph. Default is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.SumAggregator'.
     * <li/><b>graphAggregator</b>: function for aggregating across properties of
     * a graph. Default is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.ConcatenationAggregator'
     * <li/><b>actorProperty</b>: ID of the property on the actors where the results are stored.
     * Default is 'GraphProperty'
     * </ul>
     * @param map parameters to be loaded - may be null
     */
    public void init(Properties map) {
        Properties props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "name");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("name") != null)) {
            parameter[0].setValue(map.getProperty("name"));
        } else {
            parameter[0].setValue("From Graph To Actor");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("tag");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "graphPropertyAggregator");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("graphPropertyAggregator") != null)) {
            parameter[2].setValue(map.getProperty("graphPropertyAggregator"));
        } else {
            parameter[2].setValue(SumAggregator.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "graphAggregator");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("graphAggregator") != null)) {
            parameter[3].setValue(map.getProperty("graphAggregator"));
        } else {
            parameter[3].setValue(ConcatenationAggregator.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorProperty") != null)) {
            parameter[4].setValue(map.getProperty("actorProperty"));
        } else {
            parameter[4].setValue("GraphProperty");
        }

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",".*");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[4].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
