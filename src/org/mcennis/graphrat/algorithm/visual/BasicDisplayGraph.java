/*

 * Created 22-1-08

 * Copyright Daniel McEnnis, see license.txt

 */

package org.mcennis.graphrat.algorithm.visual;



import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import javax.swing.JFrame;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;
import org.dynamicfactory.descriptors.*;

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

import prefuse.data.Schema;



/**

 *  Class for mapping one mode and one relation into a prefuse display demo.

 * 

 * @author Daniel McEnnis

 */

public class BasicDisplayGraph extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    public BasicDisplayGraph() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Basic Display Graph");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Basic Display Graph");
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
    }



    public void execute(Graph g) {

        prefuse.data.Graph graph = new prefuse.data.Graph();

        Schema schema = new Schema();

        schema.addColumn("label", String.class, "");

        graph.getNodeTable().addColumns(schema);
        Collection<Actor> actor = (AlgorithmMacros.filterActor(parameter, g, ((ActorQuery)parameter.get("Mode").get()).execute(g, null, null)));
        Actor[] a = actor.toArray(new Actor[]{});
        if (a != null) {

            Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.INFO, "Number of nodes  - " + a.length);

            prefuse.data.Node[] n = new prefuse.data.Node[a.length];

            HashMap<Actor, prefuse.data.Node> map = new HashMap<Actor, prefuse.data.Node>();

            for (int i = 0; i < a.length; ++i) {

                n[i] = graph.addNode();

                n[i].setString("label", a[i].getID());

                map.put(a[i], n[i]);

            }

        Link[] l = (AlgorithmMacros.filterLink(parameter, g, ((LinkQuery)parameter.get("Relation").get()).execute(g, actor, actor,null))).toArray(new Link[]{});

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

                Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.WARNING, "No links were found");

            }

            JFrame frame = PrefuseGraphView.demo(graph, "label", false, null);

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } else {

            Logger.getLogger(BasicDisplayGraph.class.getName()).log(Level.WARNING, "No actors found");

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
        }

    }

    public BasicDisplayGraph prototype(){
        return new BasicDisplayGraph();
    }

}

