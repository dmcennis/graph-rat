/*
 *  Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.prestige.OptimizedLinkBetweeness;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.FindStronglyConnectedComponentsCore;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.OptimizedLinkBetweenessCore;
import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.path.PathNode;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import nz.ac.waikato.mcennis.rat.util.Duples;

/**
 * Creates clusters by removing each edge sequentially by order of link betweeness.
 * Betweeness is calculated once at te outset, then used so seperate all components
 * thereafter
 * 
 * @author Daniel McEnnis
 */
public class TraditionalEdgeBetweenessClustering extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[5];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[1];
    Graph graph;
    Duples<Double, Link>[] orderedBetweeness;
    OptimizedLinkBetweenessCore core = new OptimizedLinkBetweenessCore();
    Properties graphProperties = new Properties();
    FindStronglyConnectedComponentsCore stronglyConnected = new FindStronglyConnectedComponentsCore();
    int top;
    
    public TraditionalEdgeBetweenessClustering(){
        init(null);
    }

    @Override
    public void execute(Graph g) {
        fireChange(Scheduler.SET_ALGORITHM_COUNT,2);
        core.execute(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);
        Map<Link, Double> linkMap = core.getLinkMap();
        Link[] links = g.getLink(core.getRelation());
        if (links != null) {
            for (int i = 0; i < links.length; ++i) {
                if (!linkMap.containsKey(links[i])) {
                    linkMap.put(links[i], 0.0);
                }
            }
        }
        orderedBetweeness = new Duples[linkMap.size()];
        Iterator<Link> it = linkMap.keySet().iterator();
        int count = 0;
        top = 0;
        while (it.hasNext()) {
            Link l = it.next();
            orderedBetweeness[count] = new Duples<Double,Link>();
            orderedBetweeness[count].setLeft(linkMap.get(l));
            orderedBetweeness[count].setRight(l);
            count++;
        }
        java.util.Arrays.sort(orderedBetweeness);
        splitGraph(g, count - 1,(String)parameter[4].getValue());
    }

    protected void splitGraph(Graph base, int top,String prefix) {

        try {
            HashSet<Actor> actorSet = new HashSet<Actor>();
            Actor[] actors = base.getActor(core.getMode());
            if ((actors != null) && (actors.length > 1)) {
                Logger.getLogger(TraditionalEdgeBetweenessClustering.class.getName()).log(Level.FINE, " Splitting "+actors.length+" actors - ");
                for (int i = 0; i < actors.length; ++i) {
                    actorSet.add(actors[i]);
                    Logger.getLogger(TraditionalEdgeBetweenessClustering.class.getName()).log(Level.FINE, actors[i].getID()+" ");
                }
                Logger.getLogger(TraditionalEdgeBetweenessClustering.class.getName()).log(Level.FINE, "");
                Graph rootGraph = base.getSubGraph(graphProperties, actorSet);
                //if one or fewer actors, return graph unaltered
                //else
                stronglyConnected.setGraphPrefix(prefix);
                stronglyConnected.execute(rootGraph);
                Graph[] components = stronglyConnected.getGraph();
                if (components != null) {
                    for (int i = 0; i < components.length; ++i) {
                        splitGraph(components[i], top,prefix+i);
                        base.addChild(components[i]);
                    }
                } else {
                    while (stronglyConnected.getGraph() == null) {
                        if (rootGraph.getLink(core.getRelation(), orderedBetweeness[top].getRight().getSource(), orderedBetweeness[top].getRight().getDestination()) == null) {
                            --top;
                        } else {
                            rootGraph.remove(orderedBetweeness[top].getRight());
                            stronglyConnected.setGraphPrefix(prefix);
                            stronglyConnected.execute(rootGraph);
                        }
                    }
                    components = stronglyConnected.getGraph();
                    for (int i = 0; i < components.length; ++i) {
                        splitGraph(components[i],top,prefix+i);
                        base.addChild(components[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
