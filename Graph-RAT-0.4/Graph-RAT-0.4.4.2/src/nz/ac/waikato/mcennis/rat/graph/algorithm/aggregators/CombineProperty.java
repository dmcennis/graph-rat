/**
 * Sep 12, 2008-5:11:24 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.InstanceManipulation;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class CombineProperty extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[5];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[1];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public CombineProperty() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[1].getValue());

        if (actor != null) {
            for (int i = 0; i < actor.length; ++i) {
                Property source1 = actor[i].getProperty((String) parameter[2].getValue());
                Property source2 = actor[i].getProperty((String) parameter[3].getValue());
                if ((source1 == null) && (source2 == null)) {
                    Object[] instance1O = source1.getValue();
                    Object[] instance2O = source2.getValue();
                    if ((instance1O.length > 0) && (instance2O.length > 0)) {
                        Instance instance1 = (Instance) instance1O[0];
                        Instance instance2 = (Instance) instance2O[0];
                        Instance result = InstanceManipulation.instanceConcatenation(new Instance[]{instance1, instance2}, new Instances[]{instance1.dataset(), instance2.dataset()});
                        Properties props = new Properties();
                        props.setProperty("PropertyType", "Basic");
                        props.setProperty("PropertyClass", "weka.core.Instance");
                        props.setProperty("PropertyID", (String) parameter[4].getValue());
                        Property aggregator = PropertyFactory.newInstance().create(props);
                        try {
                            aggregator.add(result);
                        } catch (InvalidObjectTypeException ex) {
                            Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        actor[i].add(aggregator);
                    } else {
                        if (instance1O.length == 0) {
                            Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.WARNING, "Property '" + source1.getType() + "' on actor '" + actor[i].getID() + "' has no value");
                        }
                        if (instance2O.length == 0) {
                            Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.WARNING, "Property '" + source2.getType() + "' on actor '" + actor[i].getID() + "' has no value");
                        }
                    }
                } else {
                    if (source1 == null) {
                        Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.WARNING, "Property '" + (String) parameter[2].getValue() + "' on actor '" + actor[i].getID() + "' does not exist");
                    }
                    if (source2 == null) {
                        Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.WARNING, "Property '" + (String) parameter[3].getValue() + "' on actor '" + actor[i].getID() + "' does not exist");
                    }
                }
            }
        } else {
            Logger.getLogger(CombineProperty.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter[1].getValue() + "' were found");
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
        props.setProperty("Name", "sourceProperty1");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceProperty1") != null)) {
            parameter[2].setValue(map.getProperty("sourceProperty1"));
        } else {
            parameter[2].setValue("Property1");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "sourceProperty2");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceProperty2") != null)) {
            parameter[3].setValue(map.getProperty("sourceProperty2"));
        } else {
            parameter[3].setValue("Property2");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "destinationProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationProperty") != null)) {
            parameter[4].setValue(map.getProperty("destinationProperty"));
        } else {
            parameter[4].setValue("CombinedProperty");
        }

        props.setProperty("Type", "ActorProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[2].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[3].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[4].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
