/*

 * Created 22-1-08

 * Copyright Daniel McEnnis, see license.txt

 */

package org.mcennis.graphrat.algorithm.visual;



import java.util.Collection;
import java.util.HashMap;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import javax.swing.JFrame;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;

import org.mcennis.graphrat.link.Link;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.graphdisplay.PrefuseGraphView;

import prefuse.data.Edge;

import prefuse.data.Schema;

import prefuse.util.ColorLib;



/**

 *  Class for mapping one mode and one relation into a prefuse display demo.

 * 

 * @author Daniel McEnnis

 */

public class ColoredByPropertyGraph extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    public ColoredByPropertyGraph() {
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

        name = ParameterFactory.newInstance().create("Mode",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
    }



    public void execute(Graph g) {

        prefuse.data.Graph graph = new prefuse.data.Graph();

        Schema schema = new Schema();

        schema.addColumn("label", String.class, "");

        schema.addColumn("subgraph", String.class, "");

        schema.addColumn("actor",Actor.class,null);

        graph.getNodeTable().addColumns(schema);

        graph.getEdgeTable().addColumn("Link",Link.class,null);

        Collection<Actor> actor = (AlgorithmMacros.filterActor(parameter, g, ((ActorQuery)parameter.get("Mode").get()).execute(g, null, null)));
        Actor[] a = actor.toArray(new Actor[]{});

        if (a != null) {

            prefuse.data.Node[] n = new prefuse.data.Node[a.length];

            HashMap<Actor, prefuse.data.Node> map = new HashMap<Actor, prefuse.data.Node>();

            HashSet<String> propertyValues = new HashSet<String>();

            for (int i = 0; i < a.length; ++i) {

                n[i] = graph.addNode();

                n[i].setString("label", a[i].getID());

                Property property = a[i].getProperty(AlgorithmMacros.getSourceID(parameter,g,(String) parameter.get("SourceProperty").get()));

                if ((property != null) && (property.getValue().size() > 0) && (property.getPropertyClass().isAssignableFrom(String.class))) {

                    n[i].setString("subgraph", (String) property.getValue().get(0));

                    n[i].set("actor", a[i]);

                    propertyValues.add((String) property.getValue().get(0));

                }

                map.put(a[i], n[i]);

            }

        Link[] l = (AlgorithmMacros.filterLink(parameter, g, ((LinkQuery)parameter.get("Relation").get()).execute(g, actor, actor,null))).toArray(new Link[]{});

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

            Logger.getLogger(ColoredByPropertyGraph.class.getName()).log(Level.WARNING,"No actors found");

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

     * initializes the object has the following parameters:

     * <ul>

     *  <li>'name' - name of this algorithm. default 'Basic Display Graph'

     *  <li>'relation' - name of the relation to use. default 'References'

     *  <li>'actorType' - name of the actor mode to use. default 'Paper'

     * </ul>

     * @param map

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    null,
                    (ActorQuery)parameter.get("Mode").get(),
                    null,"",false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    null,
                    (LinkQuery)parameter.get("Relation").get(),
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    (LinkQuery)parameter.get("Relation").get(),
                    (String)parameter.get("SourceProperty").get(),
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);
        }

    }

    public ColoredByPropertyGraph prototype(){
        return new ColoredByPropertyGraph();
    }

}

