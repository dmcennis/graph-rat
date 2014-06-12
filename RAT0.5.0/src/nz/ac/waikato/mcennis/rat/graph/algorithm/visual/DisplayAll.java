/*

 * Created 25-1-08

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.visual;



import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;

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

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
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



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    /**

     * create a new display algorithm with deafult properties

     */

    public DisplayAll() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Colored By Property Graph");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Colored By Property Graph");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Visual");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);
    }



    @Override

    public void execute(Graph g) {

        String[] actorTypes = (g.getActorTypes()).toArray(new String[]{});

        String[] linkTypes = g.getLinkTypes().toArray(new String[]{});

        HashMap<Actor, Node> map = new HashMap<Actor, Node>();

        prefuse.data.Graph graph = new prefuse.data.Graph(true);

        graph.getNodeTable().addColumn("label", String.class, "");

        graph.getNodeTable().addColumn("mode", String.class, "");

        graph.getNodeTable().addColumn("actor", Actor.class, null);

        graph.getEdgeTable().addColumn("relation", String.class, "");

        graph.getEdgeTable().addColumn("link", Link.class, null);

        if ((actorTypes != null) && (linkTypes != null)) {
            Collection<Actor> a = AlgorithmMacros.filterActor(parameter, g, g.getActor());
            Iterator<Actor> aIt = a.iterator();

            while (aIt.hasNext()) {
                    Actor actor = aIt.next();

                    Node n = graph.addNode();

                    n.setString("label", actor.getID());

                    n.setString("mode", actor.getMode());

                    n.set("actor", actor);

                    map.put(actor, n);


            }

            Collection<Link> link = AlgorithmMacros.filterLink(parameter, g, g.getLink());
            Iterator<Link> linkIt = link.iterator();

            while (linkIt.hasNext()) {
                Link l = linkIt.next();
                    Edge e = graph.addEdge(map.get(l.getSource()), map.get(l.getDestination()));

                    e.setString("relation", l.getRelation());

                    e.set("link", l);
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

     * Initializes the algorithm.  Only accepts the name of the algorithm as a 

     * property. 'name' key with default value 'Display Graph'.

     * @param map property set to initialize the algorithm with.

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    ".*",
                    null,
                    null,"",false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    ".*",
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    null,
                    "",
                    false);
            input.add(desc);
        }

    }

    public DisplayAll prototype(){
        return new DisplayAll();
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

