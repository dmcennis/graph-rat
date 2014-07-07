/**
 * Jul 23, 2008-5:34:26 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.algorithm.aggregators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.reusablecores.InstanceManipulation;
import org.mcennis.graphrat.algorithm.reusablecores.aggregator.AggregatorFunction;
import org.mcennis.graphrat.algorithm.reusablecores.aggregator.AggregatorFunctionFactory;
import org.mcennis.graphrat.algorithm.reusablecores.aggregator.FirstItemAggregatorFunction;
import org.mcennis.graphrat.algorithm.reusablecores.aggregator.SumAggregator;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.ModelShell;
import weka.core.Attribute;
import weka.core.FastVector;
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
public class AggregateLinks extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[8];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public AggregateLinks() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[1].getValue());
        Properties props = new Properties();

        if (actor != null) {
            int count = 0;

            // create the Instances metadata object
            Instances meta = new Instances("AggregateLinks", new FastVector(), 1);
            FastVector attributeList = new FastVector();
            FastVector attributeItems = new FastVector();
            Link[] link = g.getLink((String) parameter[2].getValue());
            Set<Actor> list = null;
            if ((Boolean) parameter[5].getValue()) {
                list = new HashSet<Actor>();
                if (link != null) {
                    for (int i = 0; i < link.length; ++i) {
                        list.add(link[i].getDestination());
                    }
                    for (Actor name : list) {
                        attributeItems.addElement(name.getID());
                    }
                    attributeItems.addElement("None");
                    attributeList.addElement(new Attribute("ActorList", attributeItems));
                    meta = new Instances("AggregateLinks", attributeList, 1);
                } else {
                    Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter[2].getValue() + "' are found");
                }
            } else {
                list = new TreeSet<Actor>();
                if (link != null) {
                    for (int i = 0; i < link.length; ++i) {
                        list.add(link[i].getDestination());
                    }
                    for (Actor name : list) {
                        attributeList.addElement(new Attribute(name.getID()));
                    }
                    meta = new Instances("AggregateLinks", attributeList, 1);
                } else {
                    Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter[2].getValue() + "' are found");
                }
            }
            for (int i = 0; i < actor.length; ++i) {
                double[] values = new double[meta.numAttributes()];
                HashSet<Actor> total = new HashSet<Actor>();
                HashMap<Actor, Double> links = getMap(actor[i], total, g);
                Instance instance = null;
                if ((Boolean) parameter[5].getValue()) {
                    if (links.size() > 0) {
                        String name = links.keySet().iterator().next().getID();
                        values[0] = meta.attribute("ActorList").indexOfValue(name);
                    } else {
                        values[0] = Double.NaN;
                    }
                    instance = new Instance(values.length, values);
                    instance.setDataset(meta);
                } else {
                    Iterator<Actor> it = list.iterator();
                    for (int j = 0; j < list.size(); ++j) {
                        Actor artist = it.next();
                        if (links.containsKey(artist)) {
                            values[j] = links.get(artist);
                        } else {
                            values[j] = Double.NaN;
                        }
                    }
                    instance = new Instance(values.length, values);
                    instance.setDataset(meta);
                }


                props.setProperty("PropertyType", "Basic");
                props.setProperty("PropertyClass", "weka.core.Instance");
                props.setProperty("PropertyID", (String) parameter[6].getValue());
                Property aggregator = PropertyFactory.newInstance().create(props);
                try {
                    aggregator.add(instance);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(AggregateLinks.class.getName()).log(Level.SEVERE, null, ex);
                }
                actor[i].add(aggregator);
            }

        } else {
            Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No tags of mode '" + (String) parameter[1].getValue() + "' are present");
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
            Logger.getLogger(AggregateLinks.class.getName()).log(Level.SEVERE, "Invalid direction of link '" + (String) parameter[7].getValue() + "'");
        }
        return ret;
    }

    public Instances buildInstances(Graph g) {
        Instances ret = new Instances("AggregateLinks", new FastVector(), 1);
        FastVector attributeList = new FastVector();
        FastVector attributeItems = new FastVector();
        Link[] link = g.getLink((String) parameter[2].getValue());
        if ((Boolean) parameter[5].getValue()) {
            HashSet<String> list = new HashSet<String>();
            if (link != null) {
                for (int i = 0; i < link.length; ++i) {
                    list.add(link[i].getDestination().getID());
                }
                for (String name : list) {
                    attributeItems.addElement(name);
                }
                attributeList.addElement(attributeItems);
                ret = new Instances("AggregateLinks", attributeList, 1);
            } else {
                Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter[2].getValue() + "' are found");
            }
            return ret;
        } else {
            TreeSet<String> list = new TreeSet<String>();
            if (link != null) {
                for (int i = 0; i < link.length; ++i) {
                    list.add(link[i].getDestination().getID());
                }
                for (String name : list) {
                    attributeList.addElement(new Attribute(name));
                }
                ret = new Instances("AggregateLinks", attributeList, 1);
            } else {
                Logger.getLogger(AggregateLinks.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter[2].getValue() + "' are found");
            }
            return ret;
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
        props.setProperty("Name", "destinationProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationProperty") != null)) {
            parameter[3].setValue(map.getProperty("destinationProperty"));
        } else {
            parameter[3].setValue("actorProperty");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkDirection");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkDirection") != null)) {
            parameter[4].setValue(map.getProperty("linkDirection"));
        } else {
            parameter[4].setValue("Outgoing");
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "makeSingleValueInstance");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("makeSingleValueInstance") != null)) {
            parameter[5].setValue(new Boolean(map.getProperty("makeSingleValueInstance")));
        } else {
            parameter[5].setValue(new Boolean(false));
        }

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[5].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[6].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
