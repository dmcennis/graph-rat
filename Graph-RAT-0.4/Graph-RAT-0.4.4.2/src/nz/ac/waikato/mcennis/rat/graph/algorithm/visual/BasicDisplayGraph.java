/*
 * Created 22-1-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.visual;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graphdisplay.PrefuseGraphView;
import prefuse.data.Schema;

/**
 *  Class for mapping one mode and one relation into a prefuse display demo.
 * 
 * @author Daniel McEnnis
 */
public class BasicDisplayGraph extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[3];
    InputDescriptor[] input = new InputDescriptor[2];
    OutputDescriptor[] output = new OutputDescriptor[0];

    public BasicDisplayGraph() {
        init(null);
    }

    public void execute(Graph g) {
        prefuse.data.Graph graph = new prefuse.data.Graph();
        Schema schema = new Schema();
        schema.addColumn("label", java.lang.String.class, "");
        graph.getNodeTable().addColumns(schema);
        Actor[] a = g.getActor((String) parameter[2].getValue());
        if (a != null) {
            Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.INFO, "Number of nodes of type '" + (String) parameter[2].getValue() + "' - " + a.length);
            prefuse.data.Node[] n = new prefuse.data.Node[a.length];
            HashMap<Actor, prefuse.data.Node> map = new HashMap<Actor, prefuse.data.Node>();
            for (int i = 0; i < a.length; ++i) {
                n[i] = graph.addNode();
                n[i].setString("label", a[i].getID());
                map.put(a[i], n[i]);
            }
            Link[] l = g.getLink((String) parameter[1].getValue());
            if (l != null) {
                for (int i = 0; i < l.length; ++i) {
                    Actor s = l[i].getSource();
                    Actor d = l[i].getDestination();
                    if ((s != null) && (d != null)) {
                        graph.addEdge(map.get(s), map.get(d));
                    } else {
                        if (s == null) {
                            Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.WARNING, "Source actor is null");
                        }
                        if (d == null) {
                            Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.WARNING, "Destination actor is null");
                        }
                    }
                }
            } else {
                Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.WARNING, "No links of type '" + (String) parameter[1].getValue() + "' were found");
            }
            JFrame frame = PrefuseGraphView.demo(graph, "label", false, null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter[2].getValue() + "' found");
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
     * initializes the object has the following parameters:
     * <ul>
     *  <li>'name' - name of this algorithm. default 'Basic Display Graph'
     *  <li>'relation' - name of the relation to use. default 'References'
     *  <li>'actorType' - name of the actor mode to use. default 'Paper'
     * </ul>
     * @param map
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
            parameter[0].setValue("Basic Display Graph");
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

        // Construct input descriptors
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        // Construct input descriptors
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

    }
}
