/*
 * Created 21-1-08
 */
package nz.ac.waikato.bibliography.algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
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
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import nz.ac.waikato.mcennis.rat.util.Duples;

/**
 *
 * @author Daniel McEnnis
 */
public class OutputBridgesByCluster extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[6];
    InputDescriptor[] input = new InputDescriptor[3];
    OutputDescriptor[] output = new OutputDescriptor[0];

    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[1].getValue());
        FileWriter output = null;
        try {
            output = new FileWriter((String) parameter[2].getValue());
            if (actor != null) {
                fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length*2);
                Vector<Duples<Double, Actor>> results = new Vector<Duples<Double, Actor>>();
                for (int i = 0; i < actor.length; ++i) {
                    Property magnitude = actor[i].getProperty((String) parameter[5].getValue());
                    double maxExternal = 0.0;
                    if ((magnitude != null) && (magnitude.getValue() != null)) {
                        for (int j = 0; j < magnitude.getValue().length; ++j) {
                            if (((Double) magnitude.getValue()[j]).doubleValue() > maxExternal) {
                                maxExternal = ((Double) magnitude.getValue()[j]).doubleValue();
                            }
                        }
                        Duples<Double, Actor> ret = new Duples<Double, Actor>();
                        ret.setLeft(maxExternal);
                        ret.setRight(actor[i]);
                        results.add(ret);
                    }
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
                }
                fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length+results.size());
                Collections.sort(results);
                Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.INFO,"Total number of Bridges Found " + results.size());
                output.append("<?xml version=\"1.0\"?>\n");
                output.append("<!DOCTYPE bridges [\n");
                output.append("\t<!ELEMENT bridges (paper+)>\n");
                output.append("\t<!ELEMENT paper (paperID,title,link*)>\n");
                output.append("\t<!ELEMENT paperID (#PCDATA)>\n");
                output.append("\t<!ELEMENT title (#PCDATA)>\n");
                output.append("\t<!ELEMENT link (cluster,score)>\n");
                output.append("\t<!ELEMENT cluster (#PCDATA)>\n");
                output.append("\t<!ELEMENT score (#PCDATA)>\n");
                output.append("]>\n\n");
                output.append("<bridges>\n");
                for (int i = 0; i < results.size(); ++i) {
//                    System.out.println("");
                    String title = (String) results.get(i).getRight().getProperty("title").getValue()[0];
                    output.append("\t<paper>\n");
                    output.append("\t\t<paperID>" + results.get(i).getRight().getID() + "</paperID>\n");
                    output.append("\t\t<title>" + title + "</title>\n");
                    Property cluster = results.get(i).getRight().getProperty((String) parameter[3].getValue());
                    if ((cluster != null) && (cluster.getValue() != null)) {
                        Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.FINER,(String) cluster.getValue()[0] + "\t");
                    }

                    Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.FINE,results.get(i).getRight().getID() + "\t" + title);
                    Property bridgeEndpoint = results.get(i).getRight().getProperty((String) parameter[4].getValue());
                    Property bridgeMagnitude = results.get(i).getRight().getProperty((String) parameter[5].getValue());
                    if ((bridgeEndpoint != null) && (bridgeMagnitude != null) && (bridgeEndpoint.getValue() != null) && (bridgeMagnitude.getValue() != null) && (bridgeEndpoint.getValue().length == bridgeMagnitude.getValue().length)) {
                        for (int j = 0; j < bridgeEndpoint.getValue().length; ++j) {
                            output.append("\t\t<link>\n");
                            output.append("\t\t\t<cluster>" + (String) bridgeEndpoint.getValue()[j] + "</cluster>\n");
                            output.append("\t\t\t<score>" + ((Double) bridgeMagnitude.getValue()[j]).toString() + "</score>\n");
                            Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.FINE,(String) bridgeEndpoint.getValue()[j] + "\t" + ((Double) bridgeMagnitude.getValue()[j]).toString());
                            output.append("\t\t</link>\n");
                        }
                    }
                    output.append("\t</paper>");
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actor.length+i);
                }
                output.append("</bridges>\n");
            // output source , dest, magnitude, title
            // output source , dest, magnitude, title
            } else {
                Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.WARNING,"No actors of type '" + (String) parameter[1].getValue() + "' found");
            }
        } catch (IOException ex) {
            Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(OutputBridgesByCluster.class.getName()).log(Level.SEVERE, null, ex);
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
            parameter[0].setValue("Output Bridges By Cluster");
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
        props.setProperty("Name", "clusterID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("clusterID") != null)) {
            parameter[3].setValue(map.getProperty("clusterID"));
        } else {
            parameter[3].setValue("cluster");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "bridgeEntry");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("bridgeEntry") != null)) {
            parameter[4].setValue(map.getProperty("bridgeEntry"));
        } else {
            parameter[4].setValue("Bridge Endpoint");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "bridgeMagnitude");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("bridgeMagnitude") != null)) {
            parameter[5].setValue(map.getProperty("bridgeMagnitude"));
        } else {
            parameter[5].setValue("Bridge Magnitude");
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

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[5].getValue());
        input[2] = DescriptorFactory.newInstance().createInputDescriptor(props);
    }
}
