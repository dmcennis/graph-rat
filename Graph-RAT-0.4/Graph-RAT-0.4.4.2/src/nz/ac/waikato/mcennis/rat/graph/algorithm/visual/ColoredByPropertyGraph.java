/*
 * Created 22-1-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.visual;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graphdisplay.PrefuseGraphView;
import prefuse.data.Edge;
import prefuse.data.Schema;
import prefuse.util.ColorLib;

/**
 *  Class for mapping one mode and one relation into a prefuse display demo.
 * 
 * @author Daniel McEnnis
 */
public class ColoredByPropertyGraph extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[4];
    InputDescriptor[] input = new InputDescriptor[3];
    OutputDescriptor[] output = new OutputDescriptor[0];

    public ColoredByPropertyGraph() {
        init(null);
    }

    public void execute(Graph g) {
        prefuse.data.Graph graph = new prefuse.data.Graph();
        Schema schema = new Schema();
        schema.addColumn("label", java.lang.String.class, "");
        schema.addColumn("subgraph", java.lang.String.class, "");
        schema.addColumn("actor",Actor.class,null);
        graph.getNodeTable().addColumns(schema);
        graph.getEdgeTable().addColumn("Link",Link.class,null);
        Actor[] a = g.getActor((String) parameter[2].getValue());
        if (a != null) {
            prefuse.data.Node[] n = new prefuse.data.Node[a.length];
            HashMap<Actor, prefuse.data.Node> map = new HashMap<Actor, prefuse.data.Node>();
            HashSet<String> propertyValues = new HashSet<String>();
            for (int i = 0; i < a.length; ++i) {
                n[i] = graph.addNode();
                n[i].setString("label", a[i].getID());
                Property property = a[i].getProperty((String) parameter[3].getValue());
                if ((property != null) && (property.getValue().length > 0) && (property.getPropertyClass().isAssignableFrom(java.lang.String.class))) {
                    n[i].setString("subgraph", (String) property.getValue()[0]);
                    n[i].set("actor", a[i]);
                    propertyValues.add((String) property.getValue()[0]);
                }
                map.put(a[i], n[i]);
            }
            Link[] l = g.getLink((String) parameter[1].getValue());
            for (int i = 0; i < l.length; ++i) {
                Actor s = l[i].getSource();
                Actor d = l[i].getDestination();
                if ((s != null) && (d != null)) {
                    Edge e = graph.addEdge(map.get(s), map.get(d));
                    e.set("Link", l[i]);
                } else {
                    if (s == null) {
                        Logger.getLogger(ColoredByPropertyGraph.class.getName()).log(Level.WARNING,"Source actor is null");
                    }
                    if (d == null) {
                        Logger.getLogger(ColoredByPropertyGraph.class.getName()).log(Level.WARNING,"Destination actor is null");
                    }
                }
            }
            int[] colorScheme = null;
            if (propertyValues.size() > 64) {
                colorScheme = ColorLib.getCategoryPalette(64);
            } else {
                colorScheme = ColorLib.getCategoryPalette(propertyValues.size());
            }
            JFrame frame = PrefuseGraphView.demo(graph, "label", true, colorScheme);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            Logger.getLogger(ColoredByPropertyGraph.class.getName()).log(Level.WARNING,"No actors of mode '" + (String) parameter[2].getValue() + "' found");
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
            parameter[0].setValue("Color By Property Display Graph");
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
        props.setProperty("Name", "classProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("classProperty") != null)) {
            parameter[3].setValue(map.getProperty("classProperty"));
        } else {
            parameter[3].setValue("cluster");
        }
        

            // Construct input descriptors
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[3].getValue());
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

                // Construct input descriptors
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[2] = DescriptorFactory.newInstance().createInputDescriptor(props);


}
}
