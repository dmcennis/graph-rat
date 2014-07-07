/*
 * OutputBibliographyXML.java
 * 
 * Created on 8/01/2008, 14:01:39
 * 
 * 
 */
package nz.ac.waikato.bibliography.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import nz.ac.waikato.mcennis.rat.util.Duples;

/**
 * Class for outputting into an XML format the pagerank of each file.
 * 
 * @author Daniel McEnnis
 */
public class OutputDifference extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[8];
    InputDescriptor[] input = new InputDescriptor[2];
    OutputDescriptor[] output = new OutputDescriptor[0];

    public OutputDifference() {
    }

    public void execute(Graph g) {
        Vector<Duples<Double, Actor>> sortedByBridgeScore = new Vector<Duples<Double, Actor>>();
//        Vector<Duples<Double,Actor>> sortedByMaxPageRank = new Vector<Duples<Double,Actor>>();
        HashMap<Actor, Double> clusterRankMap = new HashMap<Actor, Double>();
        HashMap<Actor, Double> globalRankMap = new HashMap<Actor, Double>();

//        try {
        Actor[] list = g.getActor((String) parameter[1].getValue());
        if (list != null) {
            fireChange(Scheduler.SET_ALGORITHM_COUNT, list.length);
            for (int i = 0; i < list.length; ++i) {

                Property differenceProperty = list[i].getProperty((String) parameter[3].getValue());
                if (differenceProperty != null) {
                    if (differenceProperty.getPropertyClass().isAssignableFrom(Double.class)) {
                        if (differenceProperty.getValue() != null) {
                            sortedByBridgeScore.add(new Duples<Double, Actor>(((Double) differenceProperty.getValue()[0]), list[i]));
                        } else {
                            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String) parameter[3].getValue() + " of actor ID " + list[i].getID() + " has no values");
                        }
                    } else {
                        Logger.getLogger(OutputDifference.class.getName()).log(Level.SEVERE,(String) parameter[3].getValue() + " of actor ID " + list[i].getID() + "is not a double property type");
                    }
                } else {
                    Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String) parameter[3].getValue() + " of actor ID " + list[i].getID() + " does not exist");
                }

                Property clusterProperty = list[i].getProperty((String) parameter[4].getValue());
                if (clusterProperty != null) {
                    if (clusterProperty.getPropertyClass().isAssignableFrom(Double.class)) {
                        if (clusterProperty.getValue() != null) {
//                                    sortedByMaxPageRank.add(new Duples(((Double)clusterGlobalProperty.getValue()[0]),list[i]));
                            clusterRankMap.put(list[i], (Double) clusterProperty.getValue()[0]);
                        } else {
                            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String) parameter[4].getValue() + " of actor ID " + list[i].getID() + " exists but  has no values");
                        }
                    } else {
                        Logger.getLogger(OutputDifference.class.getName()).log(Level.SEVERE,"'" + (String) parameter[4].getValue() + "' of actor ID " + list[i].getID() + " exists but is not a Double property type");
                    }
                } else {
                    Logger.getLogger(OutputDifference.class.getName()).log(Level.FINE,"'"+(String)parameter[4].getValue()+"' of actor ID "+list[i].getID()+" does not have a cluster ranking");
                }

                Property globalProperty = list[i].getProperty((String) parameter[5].getValue());
                if (globalProperty != null) {
                    if (globalProperty.getPropertyClass().isAssignableFrom(Double.class)) {
                        if (globalProperty.getValue() != null) {
//                                    sortedByMaxPageRank.add(new Duples(((Double)clusterGlobalProperty.getValue()[0]),list[i]));
                            globalRankMap.put(list[i], (Double) globalProperty.getValue()[0]);
                        } else {
                            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,(String) parameter[4].getValue() + " of actor ID " + list[i].getID() + " exists but  has no values");
                        }
                    } else {
                        Logger.getLogger(OutputDifference.class.getName()).log(Level.SEVERE,"'" + (String) parameter[4].getValue() + "' of actor ID " + list[i].getID() + " exists but is not a Double property type");
                    }
                } else {
                    Logger.getLogger(OutputDifference.class.getName()).log(Level.FINE,"'"+(String)parameter[4].getValue()+"' of actor ID "+list[i].getID()+" does not have a cluster ranking");
                }


                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
            }
        } else {
            Logger.getLogger(OutputDifference.class.getName()).log(Level.WARNING,"No actors of type '" + parameter[1].getValue() + "' found");
        }
//        } catch (IOException ex) {
//            Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE, null, ex);
//        }

        Collections.sort(sortedByBridgeScore);
//        Collections.sort(sortedByMaxPageRank);

        System.out.println("Max Bridge Scores for cluster " + g.getID());
        System.out.println("Bridge \t Local Rank \t Global Rank \t Title");
        System.out.println("-----------------");
        int maxCount = ((Integer) parameter[7].getValue()).intValue();
        if (((String) parameter[6].getValue()).equalsIgnoreCase("min")) {
            for (int i = 0; i < Math.min(maxCount, sortedByBridgeScore.size()); ++i) {
                Actor actor = sortedByBridgeScore.get(i).getRight();
                Logger.getLogger(OutputDifference.class.getName()).log(Level.INFO,sortedByBridgeScore.get(i).getLeft() + "\t" + clusterRankMap.get(actor) + "\t" + globalRankMap.get(actor) + "\t" + actor.getProperty("title").getValue()[0]);
            }
        } else {
            for (int i = sortedByBridgeScore.size() - 1; i >= Math.max(sortedByBridgeScore.size() - maxCount, 0); i--) {
                Actor actor = sortedByBridgeScore.get(i).getRight();
                Logger.getLogger(OutputDifference.class.getName()).log(Level.INFO,sortedByBridgeScore.get(i).getLeft() + "\t" + clusterRankMap.get(actor) + "\t" + globalRankMap.get(actor) + "\t" + actor.getProperty("title").getValue()[0]);
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
     * Initializes the object using the property map provided
     * 
     * Parameters of this algorithm:
     * <ul>
     *  <li>name: name of this algorithm. default 'Output PageRank'
     *  <li>actorType: mode of actor to evaluate over. default 'Paper'
     *  <li>outputFile: file to write results to. default '/tmp/PageRank'
     *  <li>property1: property name to write contents of. default 'Knows PageRank Centrality'
     *  <li>property2: property name to write contents of. default 'SubGraph PageRank'
     * </ul>
     * 
     * Input 1: ActorProperty
     * Input 2: ActorProperty
     * 
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
            parameter[0].setValue("Output PageRank");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("Paper");
        }


        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "outputFile");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("outputFile") != null)) {
            parameter[2].setValue(map.getProperty("outputFile"));
        } else {
            parameter[2].setValue("/tmp/output.xml");
        }


        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "property1");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("property1") != null)) {
            parameter[3].setValue(map.getProperty("property1"));
        } else {
            parameter[3].setValue("Global PageRank Centrality");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "property2");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("property2") != null)) {
            parameter[4].setValue(map.getProperty("property2"));
        } else {
            parameter[4].setValue("Knows PageRank Centrality");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "property3");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("property3") != null)) {
            parameter[5].setValue(map.getProperty("property3"));
        } else {
            parameter[5].setValue("Knows PageRank Centrality");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "minOrMax");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("minOrMax") != null)) {
            parameter[6].setValue(map.getProperty("minOrMax"));
        } else {
            parameter[6].setValue("min");
        }

        props.setProperty("Type", "java.lang.Integer");
        props.setProperty("Name", "displayCount");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("displayCount") != null)) {
            parameter[7].setValue(new Integer(Integer.parseInt(map.getProperty("displayCount"))));
        } else {
            parameter[7].setValue(new Integer(20));
        }


        // Construct input descriptors
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[3].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[4].getValue());
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);



    }
}
