/**
 * Jul 23, 2008-5:34:26 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.util.Duples;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * Aggregates a single property across links.  This property is converted into 
 * a Weka Instance object if it is not already.  Aggregation can be across incoming links, outgoing
 * links, or both. This aggregation is performed across all actors of a given mode.  
 * Two aggregators functions are used.  The first aggregates values 
 * on a single actor on a single property.  The second aggregates values across 
 * actors.  
 * 
 * @author Daniel McEnnis
 */
public class AggregateByLinkProperty extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[9];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateByLinkProperty() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[1].getValue());
        Properties props = new Properties();

        props.setProperty("AggregatorFunction", (String) parameter[3].getValue());
        AggregatorFunction innerAggregate = AggregatorFunctionFactory.newInstance().create(props);
        props.setProperty("AggregatorFunction", (String) parameter[4].getValue());
        AggregatorFunction outerAggregate = AggregatorFunctionFactory.newInstance().create(props);
        if ((innerAggregate != null) && (outerAggregate != null)) {
            if (actor != null) {
                LinkedList<Instance> values = new LinkedList<Instance>();
                HashMap<Integer,Duples<Integer,Integer>> actorMap = new HashMap<Integer,Duples<Integer,Integer>>();
                int count=0;
                for (int i = 0; i < actor.length; ++i) {
                    HashSet<Actor> total = new HashSet<Actor>();
                    HashMap<Actor, Double> links = getMap(actor[i], total, g);

                    LinkedList<Instance> instanceFromLink = new LinkedList<Instance>();
                    LinkedList<Double> weightFromLink = new LinkedList<Double>();
                    Iterator<Actor> links_it = links.keySet().iterator();
                    while (links_it.hasNext()) {
                        Instance[] destInstance = new Instance[]{};
                        Actor dest = links_it.next();
                        Property property = dest.getProperty((String) parameter[5].getValue());
                        LinkedList<Instance> instances = InstanceManipulation.propertyToInstance(property);
                        if (instances.size() > 0) {
                            double[] weights = new double[instances.size()];
                            Arrays.fill(weights, 1.0);
                            destInstance = innerAggregate.aggregate(instances.toArray(new Instance[]{}), weights);
                        }
                        for(int j=0;j<destInstance.length;++j){
                            instanceFromLink.add(destInstance[j]);
                            weightFromLink.add(links.get(dest));
                        }
                    }
                    Instance[] result = new Instance[]{};
                    if(instanceFromLink.size()>0){
//                        Instance[] data = InstanceManipulation.normalizeFieldNames(instanceFromLink.toArray(new Instance[]{}), new Instances[]{instanceFromLink.getFirst().dataset()});
                        Double[] weightsArray = weightFromLink.toArray(new Double[]{});
                        double[] weights = new double[weightsArray.length];
                        for(int j=0;j<weightsArray.length;++j){
                            weights[j] = weightsArray[j];
                        }
                        result = outerAggregate.aggregate(instanceFromLink.toArray(new Instance[]{}), weights);
                        
                    }
                    actorMap.put(i, new Duples<Integer,Integer>(count,count+result.length));
                    count+= result.length;
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
                    result = InstanceManipulation.normalizeFieldNames(result, meta);
                    props.setProperty("PropertyType", "Basic");
                    props.setProperty("PropertyClass","weka.core.Instance" );
                    if((Boolean)parameter[8].getValue()){
                        props.setProperty("PropertyID", (String)parameter[6].getValue()+g.getID());
                    }else{
                        props.setProperty("PropertyID", (String)parameter[6].getValue());
                    }
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
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Inner Aggregator Function '" + (String) parameter[3].getValue() + "' does not exist");
            }
            if (outerAggregate == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Outer Aggregator Function '" + (String) parameter[4].getValue() + "' does not exist");
            }
        }
    }

    protected HashMap<Actor, Double> getMap(Actor tag, HashSet<Actor> total, Graph g) {
        HashMap<Actor, Double> ret = new HashMap<Actor, Double>();

        if ("Outgoing".contentEquals((String) parameter[7].getValue()) || "All".contentEquals((String) parameter[7].getValue())) {
            Link[] link = g.getLinkBySource((String) parameter[2].getValue(), tag);
            if (link != null) {
                for (int i = 0; i < link.length; ++i) {
                    ret.put(link[i].getDestination(), link[i].getStrength());
                    total.add(link[i].getDestination());
                }
            }
        }
        if ("Incoming".contentEquals((String) parameter[7].getValue()) || "All".contentEquals((String) parameter[7].getValue())) {
            Link[] link = g.getLinkByDestination((String) parameter[2].getValue(), tag);
            if (link != null) {
                for (int i = 0; i < link.length; ++i) {
                    total.add(link[i].getSource());
                    if (!ret.containsKey(link[i].getSource())) {
                        ret.put(link[i].getSource(), link[i].getStrength());
                    } else {
                        ret.put(link[i].getSource(), link[i].getStrength() + ret.get(link[i].getSource()));
                    }
                }
            }
        }

        if ((!"Incoming".contentEquals((String) parameter[7].getValue())) && (!"All".contentEquals((String) parameter[7].getValue())) && (!"Outgoing".contentEquals((String) parameter[7].getValue()))) {
            Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Invalid direction of link '" + (String) parameter[7].getValue() + "'");
        }
        return ret;
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
     * <li/><b>name</b>: Name of the algorithm. Default is 'Aggregate By Link'
     * <li/><b>actorType</b>: Mode of the actor to compare. Default is 'tag'
     * <li/><b>relation</b>: Relation to calculate similarity from. Default is 'Tags'
     * <li/><b>innerAggregator</b>: Function for aggregating values inside a property on an actor.
     * Deafult value is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction'
     * <li/><b>outerAggregator</b>: Function for aggreagting across actors. Deafult
     * is 'nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.SumAggregator'
     * <li/><b>sourceProperty</b>: Property to aggregate across links. Default 
     * is 'actor profile'
     * <li/><b>destinationProperty</b>: Property to store result of aggreation in.
     * Default is 'actorProperty'
     * <li/><b>linkDirection</b>: type of link to use- Incoming, Ougoing, or All.
     * Default is 'Outgoing'
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
            parameter[0].setValue("Aggregate By Link");
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
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[2].setValue(map.getProperty("relation"));
        } else {
            parameter[2].setValue("Tags");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "innerAggregator");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("innerAggregator") != null)) {
            parameter[3].setValue(map.getProperty("innerAggregator"));
        } else {
            parameter[3].setValue(FirstItemAggregatorFunction.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "outerAggregator");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("outerAggregator") != null)) {
            parameter[4].setValue(map.getProperty("outerAggregator"));
        } else {
            parameter[4].setValue(SumAggregator.class.getName());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "sourceProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceProperty") != null)) {
            parameter[5].setValue(map.getProperty("sourceProperty"));
        } else {
            parameter[5].setValue("actor profile");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "destinationProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationProperty") != null)) {
            parameter[6].setValue(map.getProperty("destinationProperty"));
        } else {
            parameter[6].setValue("actorProperty");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkDirection");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkDirection") != null)) {
            parameter[7].setValue(map.getProperty("linkDirection"));
        } else {
            parameter[7].setValue("Outgoing");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "appendGraphID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("appendGraphID") != null)) {
            parameter[8].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("appendGraphID"))));
        } else {
            parameter[8].setValue(new Boolean(false));
        }

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[5].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[6].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
