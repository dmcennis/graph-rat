/*

 * OutputBibliographyXML.java

 * 

 * Created on 8/01/2008, 14:01:39

 * 

 * 

 */

package nz.ac.waikato.bibliography.algorithm;





import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;

import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

import nz.ac.waikato.mcennis.rat.util.Duples;



/**

 * Class for outputting into an XML format the pagerank of each file.

 * 

 * @author Daniel McEnnis

 */

public class OutputDifference extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();

    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    public OutputDifference() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Output PageRank");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Output PageRank");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Bibliography");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Paper");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("OutputFile",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DifferenceProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Knows PageRank Centrality");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LocalProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("SubGraph PageRank");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("GlobalProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Global PageRank");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Ordering",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("min");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DisplayCount",Integer.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Integer.class);
        name.setRestrictions(syntax);
        name.add(20);
        parameter.add(name);
    }



    public void execute(Graph g) {

        Vector<Duples<Double, Actor>> sortedByBridgeScore = new Vector<Duples<Double, Actor>>();

//        Vector<Duples<Double,Actor>> sortedByMaxPageRank = new Vector<Duples<Double,Actor>>();

        HashMap<Actor, Double> clusterRankMap = new HashMap<Actor, Double>();

        HashMap<Actor, Double> globalRankMap = new HashMap<Actor, Double>();



//        try {
        int actorCount = g.getActorCount((String)parameter.get("Mode").get());
        ActorByMode query = new ActorByMode();
        query.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Iterator<Actor> list = AlgorithmMacros.filterActor(parameter, g, query, null, null);

        if (list.hasNext()) {

            fireChange(Scheduler.SET_ALGORITHM_COUNT, actorCount);
            int i=0;
            while(list.hasNext()) {
                Actor a = list.next();


                Property differenceProperty = a.getProperty((String) parameter.get("DifferenceProperty").get());

                if (differenceProperty != null) {

                    if (differenceProperty.getPropertyClass().isAssignableFrom(Double.class)) {

                        if (differenceProperty.getValue() != null) {

                            sortedByBridgeScore.add(new Duples<Double, Actor>(((Double) differenceProperty.getValue().get(0)), a));

                        } else {

                            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String) parameter.get("DifferenceProperty").get() + " of actor ID " + a.getID() + " has no values");

                        }

                    } else {

                        Logger.getLogger(OutputDifference.class.getName()).log(Level.SEVERE,(String) parameter.get("DifferenceProperty").get() + " of actor ID " + a.getID() + "is not a double property type");

                    }

                } else {

                    Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String) parameter.get("DifferenceProperty").get() + " of actor ID " + a.getID() + " does not exist");

                }



                Property clusterProperty = a.getProperty((String)parameter.get("LocalProperty").get());

                if (clusterProperty != null) {

                    if (clusterProperty.getPropertyClass().isAssignableFrom(Double.class)) {

                        if (clusterProperty.getValue() != null) {

//                                    sortedByMaxPageRank.add(new Duples(((Double)clusterGlobalProperty.getValue()[0]),a));

                            clusterRankMap.put(a, (Double) clusterProperty.getValue().get(0));

                        } else {

                            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String)parameter.get("LocalProperty").get() + " of actor ID " + a.getID() + " exists but  has no values");

                        }

                    } else {

                        Logger.getLogger(OutputDifference.class.getName()).log(Level.SEVERE,"'" + (String)parameter.get("LocalProperty").get() + "' of actor ID " + a.getID() + " exists but is not a Double property type");

                    }

                } else {

                    Logger.getLogger(OutputDifference.class.getName()).log(Level.FINE,"'"+(String)parameter.get("LocalProperty").get()+"' of actor ID "+a.getID()+" does not have a cluster ranking");

                }



                Property globalProperty = a.getProperty((String)parameter.get("GlobalProperty").get());

                if (globalProperty != null) {

                    if (globalProperty.getPropertyClass().isAssignableFrom(Double.class)) {

                        if (globalProperty.getValue() != null) {

//                                    sortedByMaxPageRank.add(new Duples(((Double)clusterGlobalProperty.getValue()[0]),a));

                            globalRankMap.put(a, (Double) globalProperty.getValue().get(0));

                        } else {

                            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String)parameter.get("GlobalProperty").get() + " of actor ID " + a.getID() + " exists but  has no values");

                        }

                    } else {

                        Logger.getLogger(OutputDifference.class.getName()).log(Level.SEVERE,"'" + (String)parameter.get("GlobalProperty").get() + "' of actor ID " + a.getID() + " exists but is not a Double property type");

                    }

                } else {

                    Logger.getLogger(OutputDifference.class.getName()).log(Level.FINE,"'"+(String)parameter.get("GlobalProperty").get()+"' of actor ID "+a.getID()+" does not have a cluster ranking");

                }





                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i++);

            }

        } else {

            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,"No actors of type '" + (String)parameter.get("Mode").get() + "' found");

        }

//        } catch (IOException ex) {

//            Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE, null, ex);

//        }



        Collections.sort(sortedByBridgeScore);

//        Collections.sort(sortedByMaxPageRank);



        System.out.println("Max Bridge Scores for cluster " + g.getID());

        System.out.println("Bridge \t Local Rank \t Global Rank \t Title");

        System.out.println("-----------------");

        int maxCount = ((Integer)parameter.get("DisplayCount").get()).intValue();

        if (((String)parameter.get("Ordering").get()).equalsIgnoreCase("min")) {

            for (int i = 0; i < Math.min(maxCount, sortedByBridgeScore.size()); ++i) {

                Actor actor = sortedByBridgeScore.get(i).getRight();

                Logger.getLogger(OutputDifference.class.getName()).log(Level.INFO,sortedByBridgeScore.get(i).getLeft() + "\t" + clusterRankMap.get(actor) + "\t" + globalRankMap.get(actor) + "\t" + (String)actor.getProperty("title").getValue().get(0));

            }

        } else {

            for (int i = sortedByBridgeScore.size() - 1; i >= Math.max(sortedByBridgeScore.size() - maxCount, 0); i--) {

                Actor actor = sortedByBridgeScore.get(i).getRight();

                Logger.getLogger(OutputDifference.class.getName()).log(Level.INFO,
                        sortedByBridgeScore.get(i).getLeft() + "\t" + clusterRankMap.get(actor) + "\t" + globalRankMap.get(actor) + "\t" + (String)actor.getProperty("title").getValue().get(0));

            }

        }



//        System.out.println("Min Bridge Scores for cluster "+g.getID());

//        System.out.println("-----------------");

//        for(int i=sortedByBridgeScore.size()-1;i>Math.max(0,sortedByBridgeScore.size()-20);--i){

//            System.out.println(sortedByBridgeScore.get(i).getLeft()+"\t"+sortedByBridgeScore.get(i).getRight().getProperty("title").getValue()[0]);

//        }



//        System.out.println("Max Local PageRank "+g.getID());

//        System.out.println("-----------------");

//        for(int i=0;i<Math.min(20,sortedByMaxPageRank.size());++i){

//            System.out.println(sortedByMaxPageRank.get(i).getLeft()+"\t"+sortedByMaxPageRank.get(i).getRight().getProperty("title").getValue()[0]);

//        }





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

     * Initializes the object using the property map provided

     * 

     * Parameters of this algorithm:

     * <ul>
     * </ul>

     * 

     * Input 1: ActorProperty

     * Input 2: ActorProperty

     * 

     * @param map

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DifferenceProperty").get(),"");
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("LocalProperty").get(),"");
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("GlobalProperty").get(),"");
            input.add(desc);
        }

    }

    public OutputDifference prototype(){
        return new OutputDifference();
    }

}

