/*
 * Created 25-1-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.visual;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graphdisplay.VisualizationPanel;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.util.ColorLib;

/**
 * Class that displays an entire graph inside a Prefuse display window.  Each
 * relation is assigned a color.  Each actor is assigned a color.  Properties
 * are accessed via a side panel, where relation name and ID are displayed.
 * 
 * @author Daniel McEnnis
 */
public class DisplayAll extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[1];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[0];

    /**
     * create a new display algorithm with deafult properties
     */
    public DisplayAll() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        String[] actorTypes = g.getActorTypes();
        String[] linkTypes = g.getLinkTypes();
        HashMap<Actor, Node> map = new HashMap<Actor, Node>();
        prefuse.data.Graph graph = new prefuse.data.Graph(true);
        graph.getNodeTable().addColumn("label", java.lang.String.class, "");
        graph.getNodeTable().addColumn("mode", java.lang.String.class, "");
        graph.getNodeTable().addColumn("actor", Actor.class, null);
        graph.getEdgeTable().addColumn("relation", java.lang.String.class, "");
        graph.getEdgeTable().addColumn("link", Link.class, null);
        if ((actorTypes != null) && (linkTypes != null)) {
            Actor[] a = g.getActor();
            if (a != null) {
                for (int i = 0; i < a.length; ++i) {
                    Node n = graph.addNode();
                    n.setString("label", a[i].getID());
                    n.setString("mode", a[i].getType());
                    n.set("actor", a[i]);
                    map.put(a[i], n);
                }
            }
            Link[] link = g.getLink();
            if (link != null) {
                for (int i = 0; i < link.length; ++i) {
                    Edge e = graph.addEdge(map.get(link[i].getSource()), map.get(link[i].getDestination()));
                    e.setString("relation", link[i].getType());
                    e.set("link", link[i]);
                }
            }
            VisualizationPanel view = new VisualizationPanel(graph, "label", getCategoryPalette(actorTypes.length, 0.4f, 1.0f, 1f, 1f), getCategoryPalette(linkTypes.length, 0.4f, 1.0f, 1f, 1f));

        } else {
            if (actorTypes == null) {
                Logger.getLogger(DisplayAll.class.getName()).log(Level.WARNING, "No actors are in this graph");
            }
            if (linkTypes == null) {
                Logger.getLogger(DisplayAll.class.getName()).log(Level.WARNING, "No links are in this graph");
            }
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
     * Initializes the algorithm.  Only accepts the name of the algorithm as a 
     * property. 'name' key with default value 'Display Graph'.
     * @param map property set to initialize the algorithm with.
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
            parameter[0].setValue("Display Graph");
        }

        props.setProperty("Type", "Graph");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);


    }
    public static final float[] CATEGORY_HUES = {
        0f, 7f / 12f, 1f / 2f, 11f / 12f, 1f / 3f, 2f / 3f, 1f / 6f, 5f / 6f, 1f / 12f,    /*3f/4f,*/

    };

    /**
     * Stolen from BSD licensed Jung2 toolkit. Copyright authors of the original
     * //TODO: Restore original BSD license to this procedure
     * 
     * Returns a color palette of given size tries to provide colors
     * appropriate as category labels. There are 12 basic color hues
     * (red, orange, yellow, olive, green, cyan, blue, purple, magenta,
     * and pink). If the size is greater than 12, these colors will be
     * continually repeated, but with varying saturation levels.
     * @param size the size of the color palette
     * @param s1 the initial saturation to use
     * @param s2 the final (most distant) saturation to use
     * @param b the brightness value to use
     * @param a the alpha value to use
     */
    public static int[] getCategoryPalette(int size,
            float s1, float s2, float b, float a) {
        int[] cm = new int[size];
        float s = s1;
        for (int i = 0; i < size; i++) {
            int j = i % CATEGORY_HUES.length;
            if (j == 0) {
                s = s1 + (((float) i) / size) * (s2 - s1);
            }
            cm[i] = ColorLib.hsba(CATEGORY_HUES[j], s, b, a);
        }
        return cm;
    }
}
