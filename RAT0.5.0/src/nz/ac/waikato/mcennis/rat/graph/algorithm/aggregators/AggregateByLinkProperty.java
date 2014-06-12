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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByLink;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.reusablecores.InstanceManipulation;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.AggregatorFunction;
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

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateByLinkProperty() {
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

        name = ParameterFactory.newInstance().create("Relation",String.class);
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

        name = ParameterFactory.newInstance().create("SourceProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorEnd",LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
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
            int i=0;
            if(actorIt.hasNext()){
                LinkedList<Instance> values = new LinkedList<Instance>();
                HashMap<Integer,Duples<Integer,Integer>> actorMap = new HashMap<Integer,Duples<Integer,Integer>>();
                int count=0;
                while (actorIt.hasNext()) {
                    Actor actor = actorIt.next();
                    HashSet<Actor> total = new HashSet<Actor>();
                    HashMap<Actor, Double> links = getMap(actor, total, g);

                    LinkedList<Instance> instanceFromLink = new LinkedList<Instance>();
                    LinkedList<Double> weightFromLink = new LinkedList<Double>();
                    Iterator<Actor> links_it = links.keySet().iterator();
                    while (links_it.hasNext()) {
                        Instance[] destInstance = new Instance[]{};
                        Actor dest = links_it.next();

                        Property property = dest.getProperty(AlgorithmMacros.getSourceID(parameter,g, (String)parameter.get("SourceProperty").get()));
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
                    actorMap.put(i++, new Duples<Integer,Integer>(count,count+result.length));
                    count+= result.length;
                    for(int j=0;j<result.length;++j){
                        values.add(result[j]);
                    }
                }
                i=0;
                actorIt = AlgorithmMacros.filterActor(parameter, g, query, null, null);
                while(actorIt.hasNext()){
                    Actor actor = actorIt.next();
                    Instance[] result = values.toArray(new Instance[]{});
                    Instances[] meta = new Instances[result.length];
                    for(int j=0;j<meta.length;++j){
                        meta[j] = result[j].dataset();
                    }
                    result = InstanceManipulation.normalizeFieldNames(result, meta);
                    Property aggregator = PropertyFactory.newInstance().create(AlgorithmMacros.getSourceID(parameter,g, (String)parameter.get("SourceProperty").get()),weka.core.Instance.class);
                    for(int j=actorMap.get(i).getLeft();j<actorMap.get(i).getRight();++j){
                        try {
                            aggregator.add(result[j]);
                        } catch (InvalidObjectTypeException ex) {
                            Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    actor.add(aggregator);
                    ++i;
                }
            } else {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter.get("Mode").get() + "' are present");
            }
        } else {
            if (innerAggregate == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Inner Aggregator Function does not exist");
            }
            if (outerAggregate == null) {
                Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Outer Aggregator Function does not exist");
            }
        }
    }

    protected HashMap<Actor, Double> getMap(Actor tag, HashSet<Actor> total, Graph g) {
        HashMap<Actor, Double> ret = new HashMap<Actor, Double>();
            LinkedList<Actor> actor = new LinkedList<Actor>();
            actor.add(tag);
            LinkByRelation query = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
            query.buildQuery((String)parameter.get("Relation").get(),false);

        if ( (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.SOURCE) || (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL)) {
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, query, actor, null, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    ret.put(l.getDestination(), l.getStrength());
                    total.add(l.getDestination());
                }
        }
        if ( (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.DESTINATION) || (((LinkEnd)parameter.get("ActorEnd").get())==LinkEnd.ALL)) {
            Iterator<Link> link = AlgorithmMacros.filterLink(parameter, g, query, null, actor, null);
                while (link.hasNext()) {
                    Link l = link.next();
                    total.add(l.getSource());
                    if (!ret.containsKey(l.getSource())) {
                        ret.put(l.getSource(), l.getStrength());
                    } else {
                        ret.put(l.getSource(), l.getStrength() + ret.get(l.getSource()));
                    }
                }
        }
        return ret;
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
        return parameter.get("param");
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
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,"");
            input.add(desc);

            if(((LinkEnd)parameter.get("").get())==LinkEnd.ALL){
                desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("SourceProperty").get(),"");
                if((Boolean)parameter.get("SourceAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
                input.add(desc);
            }else{
//                ActorByLink(LinkByRelation)
                ActorByLink actorQuery = (ActorByLink)ActorQueryFactory.newInstance().create("ActorByLink");
                LinkByRelation linkQuery = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
                linkQuery.buildQuery((String)parameter.get("Relation").get(), false);
                actorQuery.buildQuery((LinkEnd)parameter.get("ActorEnd").get(), false, linkQuery);
                desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    actorQuery,
                    (String)parameter.get("SourceProperty").get(),"");
                if((Boolean)parameter.get("SourceAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
                input.add(desc);

            }
        }
    }

    public AggregateByLinkProperty prototype(){
        return new AggregateByLinkProperty();
    }
}
