/**
 * Jul 23, 2008-6:35:45 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Arrays;
import java.util.HashMap;
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
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.util.Duples;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * Class for Aggregating the properties of an actor into a single Instance object.
 * Each property is transformed and then aggregated.  Aggregation is first performed
 * over the values of each property, then against all properties of each actor.
 * This aggregation is repeated across all actors of a given mode.
 * 
 * @author Daniel McEnnis
 */
public class AggregateOnActor  extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[5];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[1];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateOnActor() {
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
                int count = 0;
                LinkedList<Instance> values = new LinkedList<Instance>();
                HashMap<Integer,Duples<Integer,Integer>> actorMap = new HashMap<Integer,Duples<Integer,Integer>>();
                for (int i = 0; i < actor.length; ++i) {
                    LinkedList<Instance> instanceFromProperty = new LinkedList<Instance>();
                    Property[] properties = actor[i].getProperty();
                    if(properties != null){
                        for(int j=0;j<properties.length;++j){
                            LinkedList<Instance> actorProperty = InstanceManipulation.propertyToInstance(properties[j]);
                            double[] weight = new double[actorProperty.size()];
                            Arrays.fill(weight,1.0);
                            Instance[] toBeAdded = innerAggregate.aggregate(actorProperty.toArray(new Instance[]{}), weight);
                            for(int k=0;k<toBeAdded.length;++k){
                                instanceFromProperty.add(toBeAdded[k]);
                            }
                        }
                    }
                    Instance[] result = new Instance[]{};
                    if(instanceFromProperty.size()>0){
//                        Instance[] data = InstanceManipulation.normalizeFieldNames(instanceFromProperty.toArray(new Instance[]{}), new Instances[]{instanceFromProperty.getFirst().dataset()});
                        double[] weights = new double[instanceFromProperty.size()];
                        Arrays.fill(weights, 1.0);
                        result = outerAggregate.aggregate(instanceFromProperty.toArray(new Instance[]{}), weights);                        
                    }
                    actorMap.put(i, new Duples<Integer,Integer>(count,count+result.length));
                    count += result.length;
                    for(int j=0;j<result.length;++j){
                        values.add(result[j]);
                    }
                }
                for(int i=0;i<actor.length;++i){
                    Instance[] result = values.toArray(new Instance[]{});
                    Instances[] meta = new Instances[result.length];
                    for(int j=0;j<meta.length;++j){
                        meta[j] = result[j].dataset();
                    }
                    result = InstanceManipulation.normalizeFieldNames(result,meta);
                    props.setProperty("PropertyType", "Basic");
                    props.setProperty("PropertyClass","weka.core.Instance" );
                    props.setProperty("PropertyID", (String)parameter[4].getValue());
                    Property aggregator = PropertyFactory.newInstance().create(props);
                    for(int j=actorMap.get(i).getLeft();j<actorMap.get(i).getRight();++j){
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
        } else {
            if (innerAggregate == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Inner Aggregator Function '" + (String) parameter[2].getValue() + "' does not exist");
            }
            if (outerAggregate == null) {
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
     * <li/><b>name</b>: Name of the algorithm. Default is 'Aggregate On Actor'
     * <li/><b>actorType</b>: Mode of the actor to compare. Default is 'tag'
     * <li/><b>innerAggregator</b>: function for aggregating the values
     * of properties on an actor. Default is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction'.
     * <li/><b>outerAggregator</b>: function for aggregating across properties of
     * an actor. Default is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.ConcatenationAggregator'
     * <li/><b>actorProperty</b>: Name of the property on each actor to store results
     * into. Default is 'Actor Instance'.
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
            parameter[0].setValue("Aggregate On Actor");
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
        props.setProperty("Name", "innerAggregator");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("innerAggregator") != null)) {
            parameter[2].setValue(map.getProperty("innerAggregator"));
        } else {
            parameter[2].setValue(FirstItemAggregatorFunction.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "outerAggregator");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("outerAggregator") != null)) {
            parameter[3].setValue(map.getProperty("outerAggregator"));
        } else {
            parameter[3].setValue(ConcatenationAggregator.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "propertyName");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("propertyName") != null)) {
            parameter[4].setValue(map.getProperty("propertyName"));
        } else {
            parameter[4].setValue("Actor Instance");
        }

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
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
