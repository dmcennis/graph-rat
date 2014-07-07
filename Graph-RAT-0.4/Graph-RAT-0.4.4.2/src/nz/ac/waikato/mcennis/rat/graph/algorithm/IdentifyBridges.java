/*
 *  Created 11-1-08
 *  Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.scheduler.Scheduler;

/**
 *  Algorithm for calculating the difference across properties of an actor
 * 
 * @author Daniel McEnnis
 */
public class IdentifyBridges extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[9];
    InputDescriptor[] input = new InputDescriptor[2];
    OutputDescriptor[] output = new OutputDescriptor[2];

    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[2].getValue());
        HashMap<Actor, String> clusterMap = new HashMap<Actor, String>();
        if (actor != null) {
            fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length);
            for (int i = 0; i < actor.length; ++i) {
                // create map between actor and cluster
                Property cluster = actor[i].getProperty((String) parameter[3].getValue());
                if ((cluster != null) && (cluster.getValue() != null) && (cluster.getValue().length > 0)) {
                    clusterMap.put(actor[i], (String) cluster.getValue()[0]);
                }
            }
            for (int i = 0; i < actor.length; ++i) {
                HashMap<String, Double> actorBridgeValue = new HashMap<String, Double>();
                // identify all other links that are in a different cluster
                Link[] links = null;
                if (((String) parameter[8].getValue()).equalsIgnoreCase("Outgoing")) {
                    links = g.getLinkBySource((String) parameter[1].getValue(), actor[i]);
                } else if (((String) parameter[8].getValue()).equalsIgnoreCase("Incoming")) {
                    links = g.getLinkByDestination((String) parameter[1].getValue(), actor[i]);
                } else if (((String) parameter[8].getValue()).equalsIgnoreCase("IncomingOutgoing")) {
                    Link[] in = g.getLinkBySource((String) parameter[1].getValue(), actor[i]);
                    Link[] out = g.getLinkByDestination((String) parameter[1].getValue(), actor[i]);
                    int count = 0;
                    int inCount = 0;
                    if (in != null) {
                        count += in.length;
                        inCount = in.length;
                    }
                    if (out != null) {
                        count += out.length;
                    }
                    links = new Link[count];
                    if (in != null) {
                        for (int j = 0; j < in.length; ++j) {
                            links[j] = in[j];
                        }
                    }
                    if (out != null) {
                        for (int j = 0; j < out.length; ++j) {
                            links[j + inCount] = out[j];
                        }
                    }
                } else {
                    Logger.getLogger(IdentifyBridges.class.getName()).log(Level.WARNING,"Not reading any links for actor " + actor[i].getID());
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
                    Properties props = new Properties();
                    props.setProperty("PropertyID", (String) parameter[4].getValue());
                    props.setProperty("PropertyClass", "java.lang.String");
                    props.setProperty("PropertyType", "Basic");
                    Property bridgeTo = PropertyFactory.newInstance().create(props);

                    props.setProperty("PropertyID", (String) parameter[5].getValue());
                    props.setProperty("PropertyClass", "java.lang.Double");
                    Property magnitude = PropertyFactory.newInstance().create(props);
                    double totalPercent = 0.0;
                    while (cluster_it.hasNext()) {
                        String key = cluster_it.next();
                        double percent = actorBridgeValue.get(key) / ((double) links.length);
                        if (percent > ((Double) parameter[6].getValue()).doubleValue()) {
                            totalPercent += percent;
                            try {
                                bridgeTo.add(key);
                                magnitude.add(new Double(percent));
                            } catch (InvalidObjectTypeException ex) {
                                Logger.getLogger(IdentifyBridges.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    if (totalPercent > ((Double) parameter[7].getValue()).doubleValue()) {
                        actor[i].add(bridgeTo);
                        actor[i].add(magnitude);
                    }
                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
            }
        } else {
            Logger.getLogger(IdentifyBridges.class.getName()).log(Level.WARNING,"Mode " + (String) parameter[1].getValue() + " has no actors in this graph");
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
        return new SettableParameter[]{parameter[4]};
    }

    @Override
    public SettableParameter getSettableParameter(String param) {
        if (parameter[4].getName().contentEquals(param)) {
            return parameter[4];
        } else {
            return null;
        }
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
        Properties props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "name");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("name") != null)) {
            parameter[0].setValue(map.getProperty("name"));
        } else {
            parameter[0].setValue("Identify Bridges");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[1].setValue(map.getProperty("relation"));
        } else {
            parameter[1].setValue("References");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[2].setValue(map.getProperty("actorType"));
        } else {
            parameter[2].setValue("Paper");
        }



        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "clusterID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("clusterID") != null)) {
            parameter[3].setValue(map.getProperty("clusterID"));
        } else {
            parameter[3].setValue("cluster");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "bridgeEndpoint");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("bridgeEndpoint") != null)) {
            parameter[4].setValue(map.getProperty("bridgeEndpoint"));
        } else {
            parameter[4].setValue("Bridge Endpoint");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "bridgeMagnitude");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("bridgeMagnitude") != null)) {
            parameter[5].setValue(map.getProperty("bridgeMagnitude"));
        } else {
            parameter[5].setValue("Bridge Magnitude");
        }

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "threshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("threshold") != null)) {
            parameter[6].setValue(Double.parseDouble(map.getProperty("threshold")));
        } else {
            parameter[6].setValue(0.1);
        }

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "globalThreshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("globalThreshold") != null)) {
            parameter[7].setValue(Double.parseDouble(map.getProperty("globalThreshold")));
        } else {
            parameter[7].setValue(0.50);
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "IncomingOutgoing");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("IncomingOutgoing") != null)) {
            parameter[8].setValue(map.getProperty("IncomingOutgoing"));
        } else {
            parameter[8].setValue("Outgoing");
        }
        // Construct input descriptors
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[3].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[4].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[5].getValue());
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
