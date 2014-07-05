/*

 *  Created 11-1-08

 *  Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;



import java.util.HashMap;

import java.util.HashSet;
import java.util.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import org.dynamicfactory.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.descriptors.PropertiesInternal;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import org.dynamicfactory.property.InvalidObjectTypeException;

import org.dynamicfactory.property.Property;

import org.dynamicfactory.property.PropertyFactory;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



/**

 *  Algorithm for calculating the difference across properties of an actor

 * 

 * @author Daniel McEnnis

 */

public class IdentifyBridges extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public IdentifyBridges(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Centrality");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Centrality");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Direction", LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ClusterBridgeThreshold", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.1);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorBridgeThreshold", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.5);
        parameter.add(name);
    }

    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);

        Actor[] actor = (AlgorithmMacros.filterActor(parameter, g, mode.execute(g,null,null))).toArray(new Actor[]{});

        HashMap<Actor, String> clusterMap = new HashMap<Actor, String>();

        if (actor != null) {

            fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length);

            for (int i = 0; i < actor.length; ++i) {
                // create map between actor and cluster

                Property cluster = actor[i].getProperty(AlgorithmMacros.getSourceID(parameter, g, (String)parameter.get("SourceProperty").get()));

                if ((cluster != null) && (!cluster.getValue().isEmpty())&&(cluster.getPropertyClass().isAssignableFrom(String.class))) {

                    clusterMap.put(actor[i], (String) cluster.getValue().get(0));

                }

            }

            for (int i = 0; i < actor.length; ++i) {
                LinkedList<Actor> source = new LinkedList<Actor>();
                source.add(actor[i]);

                HashMap<String, Double> actorBridgeValue = new HashMap<String, Double>();

                // identify all other links that are in a different cluster

                Link[] links = null;

                if (((LinkEnd) parameter.get("Direction").get())==LinkEnd.SOURCE) {

                    links = AlgorithmMacros.filterLink(parameter, g, relation.execute(g, source, null, null)).toArray(new Link[]{});
                } else if (((LinkEnd) parameter.get("Direction").get())==LinkEnd.DESTINATION) {

                    links = AlgorithmMacros.filterLink(parameter, g, relation.execute(g, null, source, null)).toArray(new Link[]{});

                } else{
                    HashSet<Link> ret = new HashSet<Link>();
                    ret.addAll(AlgorithmMacros.filterLink(parameter, g, relation.execute(g, source, null, null)));
                    ret.addAll(AlgorithmMacros.filterLink(parameter, g, relation.execute(g, null, source, null)));
                    links = ret.toArray(new Link[]{});
                }

                if (links != null) {

                    for (int j = 0; j < links.length; ++j) {

                        Actor dest = null;

                        if (links[j].getSource().equals(actor[i])) {

                            dest = links[j].getDestination();

                        } else if (links[j].getDestination().equals(actor[i])) {

                            dest = links[j].getSource();

                        } else {

                            Logger.getLogger(IdentifyBridges.class.getName()).log(Level.WARNING,"Link Between '" + links[j].getSource().getID() + "' and '" + links[j].getDestination().getID() + "' do not match actor '" + actor[i].getID() + "'");

                        }

                        String cluster = clusterMap.get(dest);

                        if ((cluster != null) && (!cluster.equals(clusterMap.get(actor[i])))) {

                            if (!actorBridgeValue.containsKey(cluster)) {

                                actorBridgeValue.put(cluster, 1.0);

                            } else {

                                actorBridgeValue.put(cluster, actorBridgeValue.get(cluster) + 1.0);

                            }

                        }

                    }

                    int count = 0;

                    Iterator<String> cluster_it = actorBridgeValue.keySet().iterator();

                    Property bridgeTo = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" ID"),String.class);

                    Property magnitude = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Value"),Double.class);

                    double totalPercent = 0.0;

                    while (cluster_it.hasNext()) {

                        String key = cluster_it.next();

                        double percent = actorBridgeValue.get(key) / ((double) links.length);

                        if (percent > ((Double) parameter.get("ClusterBridgeThreshold").get()).doubleValue()) {

                            totalPercent += percent;

                            try {

                                bridgeTo.add(key);

                                magnitude.add(new Double(percent));

                            } catch (InvalidObjectTypeException ex) {

                                Logger.getLogger(IdentifyBridges.class.getName()).log(Level.SEVERE, null, ex);

                            }

                        }

                    }

                    if (totalPercent > ((Double) parameter.get("ActorBridgeThreshold").get()).doubleValue()) {

                        actor[i].add(bridgeTo);

                        actor[i].add(magnitude);

                    }

                }

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);

            }

        } else {

            Logger.getLogger(IdentifyBridges.class.getName()).log(Level.WARNING,"Mode " + (String) parameter.get("Mode").get() + " has no actors in this graph");

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

     * Initializes the algorithm using the provided properties map.  Parameters are:

     * 

     * <ul>

     *  <li>name: name of this algorithm. default 'Identify Bridges'.

     *  <li>relation: name of the link relation to calculate from. default 'References'.

     *  <li>actorType: mode of actors to evaluate over. default 'Paper'.

     *  <li>clusterID: input property defining cluster name. default 'cluster'.

     *  <li>bridgeEndpoint: output property to sstore cluster names linked to by 

     *      more than threshold percentage of references. default 'Bridge To'.

     *  <li>bridgeMagnitude: output property to store percent of links for each bridgeTo entry. default 'Bridge Magnitude'.

     *  <li>threshold: percentage of links to external clusters before a cluster is listed. default '0.1'.

     *  <li>globalThreshold: percentage of links to external clusters before a node is a bridge. default '0.5'.

     *  <li>IncomingOutgoing: string for whether outgoing, incoming, or outgoingincoming links are used. default 'outgoing'. 

     * </ul>

     * 

     * Input 1: ActorProperty

     * Input 2: Link

     * Output 1: ActorProperty

     * Output 2: ActorProperty

     * @param map properties to initialize this algorithm

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("MODE").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" ID",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+"Value",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public IdentifyBridges prototype(){
        return new IdentifyBridges();
    }

}

