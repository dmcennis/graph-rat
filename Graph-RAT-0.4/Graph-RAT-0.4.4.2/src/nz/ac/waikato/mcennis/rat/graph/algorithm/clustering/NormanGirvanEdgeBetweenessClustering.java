/*
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.FindStronglyConnectedComponentsCore;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.OptimizedLinkBetweenessCore;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import nz.ac.waikato.mcennis.rat.util.Duples;

/**
 * Class implementing NormanGirvan edge betweeness clustering.  This differs from
 * traditional edge betweeness clustering by recalculating edge-betweeness after
 * every edge is removed.
 * 
 * Girvan, M. and M. Newman. 2002. "Community structure in social and biological networks."
 * Proceedings of the National Academy of Science. 99(12):7821-6.
 * 
 * @author Daniel McEnnis
 */
public class NormanGirvanEdgeBetweenessClustering extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[5];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[1];
    Graph graph;
    OptimizedLinkBetweenessCore core = new OptimizedLinkBetweenessCore();
    Properties graphProperties = new Properties();
    FindStronglyConnectedComponentsCore stronglyConnected = new FindStronglyConnectedComponentsCore();
    int count= 0;

    public NormanGirvanEdgeBetweenessClustering() {
        init(null);
    }

    public void execute(Graph g) {
        count=0;
        stronglyConnected.setGraphPrefix((String) parameter[4].getValue());
        fireChange(Scheduler.SET_ALGORITHM_COUNT,g.getActorCount((String) parameter[2].getValue()));
        splitGraph(g, (String) parameter[4].getValue());
    }

    protected void splitGraph(Graph base, String prefix) {
        try {
            Duples<Double, Link>[] orderedBetweeness;
            HashSet<Actor> actorSet = new HashSet<Actor>();
            Actor[] actors = base.getActor(core.getMode());



            if ((actors != null) && (actors.length > 1)) {
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,count++);
                for(int i=0;i<actors.length;++i){
                    actorSet.add(actors[i]);
                }
                Graph rootGraph = base.getSubGraph(graphProperties, actorSet);
                stronglyConnected.setGraphPrefix(prefix);
                stronglyConnected.execute(rootGraph);
                Graph[] components = stronglyConnected.getGraph();
                if (components != null) {
                    for (int i = 0; i < components.length; ++i) {
                        orderedBetweeness = null;
                        splitGraph(components[i], prefix + i);
                        base.addChild(components[i]);
                    }
                } else {

//                    for (int i = 0; i < actors.length; ++i) {
//                        actorSet.add(actors[i]);
//                    }
                    //if one or fewer actors, return graph unaltered
                    //else

                    while (stronglyConnected.getGraph() == null) {
                            Link[] links = rootGraph.getLink(core.getRelation());
                            if (links == null) {
                                orderedBetweeness = new Duples[]{};
                            } else {
                                orderedBetweeness = new Duples[links.length];
                            }
                            for (int i = 0; i < orderedBetweeness.length; ++i) {
                                orderedBetweeness[i] = new Duples<Double, Link>();
                            }
                            calculateBetweeness(rootGraph, orderedBetweeness);
                            rootGraph.remove(orderedBetweeness[orderedBetweeness.length - 1].getRight());
                            stronglyConnected.setGraphPrefix(prefix);
                            stronglyConnected.execute(rootGraph);
                        
                    }
                    orderedBetweeness = null;
                    components = stronglyConnected.getGraph();
                    for (int i = 0; i < components.length; ++i) {
                        splitGraph(components[i], prefix + i);
                        base.addChild(stronglyConnected.getGraph()[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void calculateBetweeness(Graph root, Duples<Double, Link>[] orderedBetweeness) {
        core.execute(root);
        Map<Link, Double> linkMap = core.getLinkMap();
        Link[] links = root.getLink(core.getRelation());
        if (links != null) {
            for (int i = 0; i < links.length; ++i) {
                if (!linkMap.containsKey(links[i])) {
                    linkMap.put(links[i], 0.0);
                }
            }
        }
        Iterator<Link> it = linkMap.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            Link l = it.next();
            orderedBetweeness[count].setLeft(linkMap.get(l));
            orderedBetweeness[count].setRight(l);
            count++;
        }
        java.util.Arrays.sort(orderedBetweeness);
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
     * Parameters to be initialized.  Subclasses should override if they provide
     * any additional parameters or require additional inputs.
     * 
     * <ol>
     * <li>'name' - Name of this instance of the algorithm.  Default is ''.
     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.
     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'.
     * <li>'normalize' - boolean for whether or not to normalize prestige vectors. 
     * Default 'false'.
     * </ol>
     * <br>
     * <br>Input 0 - Link
     * <br>NOTE - subclasses define the ouput - see subclasses for output information
     * 
     * @param map parameters to be loaded - may be null.
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
            parameter[0].setValue("Traditional Edge Betweeness Clustering");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[1].setValue(map.getProperty("relation"));
        } else {
            parameter[1].setValue("Knows");
        }
        core.setRelation((String) parameter[1].getValue());
        stronglyConnected.setRelation((String) parameter[1].getValue());

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[2].setValue(map.getProperty("actorType"));
        } else {
            parameter[2].setValue("User");
        }
        core.setMode((String) parameter[2].getValue());
        stronglyConnected.setMode((String) parameter[2].getValue());

        // Parameter 3 - graphClass
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "graphClass");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("graphClass") != null)) {
            parameter[3].setValue(map.getProperty("graphClass"));
        } else {
            parameter[3].setValue("MemGraph");
        }
        stronglyConnected.setGraphClass((String) parameter[3].getValue());

        // Parameter 4 - graphID
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "graphIDprefix");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("graphIDprefix") != null)) {
            parameter[4].setValue(map.getProperty("graphIDprefix"));
        } else {
            parameter[4].setValue("Bicomponent ");
        }
        stronglyConnected.setGraphPrefix((String) parameter[4].getValue());


        // Create Input Descriptors
        // Construct input descriptors
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        output = new OutputDescriptor[1];
        // Construct Output Descriptors
        props.setProperty("Type", "Graph");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
