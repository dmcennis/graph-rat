/**
 * Jul 23, 2008-9:31:14 PM
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
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction;
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
public class AggregateByGraph extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[6];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[1];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateByGraph() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[1].getValue());
        Properties props = new Properties();

        props.setProperty("AggregatorFunction", (String) parameter[2].getValue());
        AggregatorFunction innerAggregate = AggregatorFunctionFactory.newInstance().create(props);
        props.setProperty("AggregatorFunction", (String) parameter[3].getValue());
        AggregatorFunction outerAggregate = AggregatorFunctionFactory.newInstance().create(props);
        if ((innerAggregate != null) && (outerAggregate != null)) {
            if (actor != null) {
                LinkedList<Instance> instanceFromProperty = new LinkedList<Instance>();
                for (int i = 0; i < actor.length; ++i) {
                    Property properties = actor[i].getProperty((String) parameter[4].getValue());
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

                props.setProperty("PropertyType", "Basic");
                props.setProperty("PropertyClass", "weka.core.Instance");
                props.setProperty("PropertyID", (String) parameter[5].getValue());
                Property aggregator = PropertyFactory.newInstance().create(props);
                for (int j = 0; j < result.length; ++j) {
                    try {
                        aggregator.add(result[j]);
                    } catch (InvalidObjectTypeException ex) {
                        Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                g.add(aggregator);
            } else {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter[1].getValue() + "' are present");
            }
        } else {
            if (innerAggregate == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Inner Aggregator Function '" + (String) parameter[2].getValue() + "' does not exist");
            }
            if (outerAggregate == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Outer Aggregator Function '" + (String) parameter[2].getValue() + "' does not exist");
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
    public Parameter getParameter(String param) {
        for (int i = 0; i < parameter.length; ++i) {
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
    public SettableParameter getSettableParameter(String param) {
        return null;
    }

    /**
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of the algorithm. Default is 'Aggregate By Graph'
     * <li/><b>actorType</b>: Mode of the actor to aggregate over. Default is 'tag'
     * <li/><b>innerAggregatorFunction</b>: aggregator function to use over values inside 
     * an actor property. Default is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction'
     * <li/><b>outerAggregatorFunction</b>: aggregator function to use over all 
     * actors. Deafult is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.SumAggregator'
     * <li/><b>actorProperty</b>: ID of the actor property to aggregate across.  Default is 'actorProperty'
     * <li/><b>graphProperty</b>: ID of the graph property to create. By default, it is
     * the value of 'outerAggregatorFunction' property concatenated with a space and the value 
     * of 'actorProperty' property.
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
            parameter[0].setValue("Aggregate By Graph");
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
        props.setProperty("Name", "innerAggregatorFunction");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("innerAggregatorFunction") != null)) {
            parameter[2].setValue(map.getProperty("innerAggregatorFunction"));
        } else {
            parameter[2].setValue(FirstItemAggregatorFunction.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "outerAggregatorFunction");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("outerAggregatorFunction") != null)) {
            parameter[3].setValue(map.getProperty("outerAggregatorFunction"));
        } else {
            parameter[3].setValue(SumAggregator.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorProperty") != null)) {
            parameter[4].setValue(map.getProperty("actorProperty"));
        } else {
            parameter[4].setValue("actorProperty");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "graphProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("graphProperty") != null)) {
            parameter[5].setValue(map.getProperty("graphProperty"));
        } else {
            parameter[5].setValue((String)parameter[3].getValue()+" "+(String)parameter[4].getValue());
        }

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[4].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[5].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
