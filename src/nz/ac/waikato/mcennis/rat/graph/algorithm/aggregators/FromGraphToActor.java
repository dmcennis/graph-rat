/**
 * Jul 26, 2008-9:17:07 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.IODescriptor;
import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.descriptors.PropertiesInternal;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;

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

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public FromGraphToActor() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("From Graph To Actor");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("From Graph TO Actor");
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

        name = ParameterFactory.newInstance().create("ActorQuery", ActorQuery.class);
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
       name = ParameterFactory.newInstance().create("SourceProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {
        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, (ActorQuery) parameter.get("Query").get(), null, null);
        Property property = g.getProperty(AlgorithmMacros.getSourceID(parameter,g, (String)parameter.get("SourceProperty").get()));
        if (property != null) {
            Property destination = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("DestinationProperty").get()), property.getPropertyClass());
            Iterator values = property.getValue().iterator();
            while (values.hasNext()) {
                try {
                    destination.add(values.next());
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(FromGraphToActor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            while (actorIt.hasNext()) {
                actorIt.next().add(destination.duplicate());
            }
        } else {
            Logger.getLogger(AggregateByLinkProperty.class.getName()).log(Level.SEVERE, "Graph Property '" + (String) parameter.get("SourceProperty").get() + "' does not exist on graph '" + g.getID() + "'");
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
    public Parameter getParameter(
            String param) {
        return parameter.get(param);
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
        if (parameter.check(map)) {
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    null,
                    (String) parameter.get("SourceProperty").get(),"");
                if((Boolean)parameter.get("SourceAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    (ActorQuery) parameter.get("ActorQuery").get(),
                    (String) parameter.get("SourceProperty").get(),"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);
        }
    }

    public FromGraphToActor prototype(){
        return new FromGraphToActor();
    }
}
