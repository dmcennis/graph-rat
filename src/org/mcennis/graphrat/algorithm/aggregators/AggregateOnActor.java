/**
 * Jul 23, 2008-6:35:45 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.aggregators;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.reusablecores.InstanceManipulation;
import org.mcennis.graphrat.reusablecores.aggregator.AggregatorFunction;
import org.mcennis.graphrat.util.Duples;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
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

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();


    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateOnActor() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate On Actor");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Aggregate On Actor");
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

        name = ParameterFactory.newInstance().create("DestinationProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        ActorByMode query = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        query.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, query, null, null);

        AggregatorFunction innerAggregate = (AggregatorFunction)parameter.get("InnerFunction").get();
        AggregatorFunction outerAggregate = (AggregatorFunction)parameter.get("OuterFunction").get();
        if ((innerAggregate != null) && (outerAggregate != null)) {
            if (actorIt.hasNext()) {
                int count = 0;
                LinkedList<Instance> values = new LinkedList<Instance>();
                HashMap<Actor,Duples<Integer,Integer>> actorMap = new HashMap<Actor,Duples<Integer,Integer>>();
                while (actorIt.hasNext()) {
                    Actor actor = actorIt.next();
                    LinkedList<Instance> instanceFromProperty = new LinkedList<Instance>();
                    Iterator<Property> properties = actor.getProperty().iterator();
                        while(properties.hasNext()){
                            LinkedList<Instance> actorProperty = InstanceManipulation.propertyToInstance(properties.next());
                            double[] weight = new double[actorProperty.size()];
                            Arrays.fill(weight,1.0);
                            Instance[] toBeAdded = innerAggregate.aggregate(actorProperty.toArray(new Instance[]{}), weight);
                            for(int k=0;k<toBeAdded.length;++k){
                                instanceFromProperty.add(toBeAdded[k]);
                            }
                        }
                    Instance[] result = new Instance[]{};
                    if(instanceFromProperty.size()>0){
//                        Instance[] data = InstanceManipulation.normalizeFieldNames(instanceFromProperty.toArray(new Instance[]{}), new Instances[]{instanceFromProperty.getFirst().dataset()});
                        double[] weights = new double[instanceFromProperty.size()];
                        Arrays.fill(weights, 1.0);
                        result = outerAggregate.aggregate(instanceFromProperty.toArray(new Instance[]{}), weights);                        
                    }
                    actorMap.put(actor, new Duples<Integer,Integer>(count,count+result.length));
                    count += result.length;
                    for(int j=0;j<result.length;++j){
                        values.add(result[j]);
                    }
                }
                int i=0;
                actorIt = AlgorithmMacros.filterActor(parameter, g, query, null, null);
                while(actorIt.hasNext()){
                    Actor actor = actorIt.next();
                    Instance[] result = values.toArray(new Instance[]{});
                    Instances[] meta = new Instances[result.length];
                    for(int j=0;j<meta.length;++j){
                        meta[j] = result[j].dataset();
                    }
                    result = InstanceManipulation.normalizeFieldNames(result,meta);
                    Property aggregator = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("DestinationProperty").get()),weka.core.Instance.class);
                    for(int j=actorMap.get(actor).getLeft();j<actorMap.get(actor).getRight();++j){
                        try {
                            aggregator.add(result[j]);
                        } catch (InvalidObjectTypeException ex) {
                            Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    actorIt.next().add(aggregator);
                }
            } else {
                Logger.getLogger(AggregateOnActor.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter.get("Mode").get() + "' are present");
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
     * <li/><b>name</b>: Name of the algorithm. Default is 'Aggregate On Actor'
     * <li/><b>actorType</b>: Mode of the actor to compare. Default is 'tag'
     * <li/><b>innerAggregator</b>: function for aggregating the values
     * of properties on an actor. Default is 'org.mcennis.graphrat.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction'.
     * <li/><b>outerAggregator</b>: function for aggregating across properties of
     * an actor. Default is 'org.mcennis.graphrat.algorithm.reusablecores.aggregator.ConcatenationAggregator'
     * <li/><b>actorProperty</b>: Name of the property on each actor to store results
     * into. Default is 'Actor Instance'.
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
                    ".*","");
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);
        }
    }

    public AggregateOnActor prototype(){
        return new AggregateOnActor();
    }
}
