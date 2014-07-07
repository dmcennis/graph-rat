/*
 * OutputBibliographyXML.java
 * 
 * Created on 8/01/2008, 14:01:39
 * 
 * 
 */
package nz.ac.waikato.bibliography.algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.scheduler.Scheduler;

/**
 * Class for outputting into an XML format the pagerank of each file.
 * 
 * @author Daniel McEnnis
 */
public class OutputBibliographyXML extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[6];
    InputDescriptor[] input = new InputDescriptor[2];
    OutputDescriptor[] output = new OutputDescriptor[0];

    public OutputBibliographyXML() {
    }

    public void execute(Graph g) {
        FileWriter output = null;
        try {
            output = new FileWriter((String) parameter[2].getValue());
            output.append("<?xml version=\"1.0\"?>\n");
            output.append("<!DOCTYPE pageRankValues [\n");
            output.append("  <!ELEMENT pageRankValues (paper*)>\n");
            output.append("  <!ELEMENT paper (paperID,title,pageRank*)>\n");
            output.append("  <!ELEMENT paperID (#PCDATA)>\n");
            output.append("  <!ELEMENT title (#PCDATA)>\n");
            output.append("  <!ELEMENT pageRank (#PCDATA)>\n");
            output.append("]>\n\n");
            output.append("<pageRankValues>\n");
            Actor[] list = g.getActor((String) parameter[1].getValue());
            if (list != null) {
                fireChange(Scheduler.SET_ALGORITHM_COUNT, 0);
                for (int i = 0; i < list.length; ++i) {
                    output.append("\t<paper>\n");
                    output.append("\t\t<paperID>" + list[i].getID() + "</paperID>\n");
                    String title = (String) list[i].getProperty("title").getValue()[0];
                    output.append("\t\t<title>").append(title).append("</title>\n");
                    Property globalProperty = list[i].getProperty((String) parameter[3].getValue());
                    if (globalProperty != null) {
                        if (globalProperty.getPropertyClass().isAssignableFrom(Double.class)) {
                            if (globalProperty.getValue() != null) {
                                output.append("\t\t<pageRank>" + ((Double) globalProperty.getValue()[0]).toString() + "</pageRank>\n");
                            } else {
                                Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,(String) parameter[3].getValue() + " of actor ID " + list[i].getID() + " has no values");
                            }
                        } else {
                            Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE,(String) parameter[3].getValue() + " of actor ID " + list[i].getID() + "is not a double property type");
                        }
                    } else {
                        Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,(String) parameter[3].getValue() + " of actor ID " + list[i].getID() + " does not exist");
                    }

                    Property clusterGlobalProperty = list[i].getProperty((String) parameter[4].getValue());
                    if (clusterGlobalProperty != null) {
                        if (clusterGlobalProperty.getPropertyClass().isAssignableFrom(Double.class)) {
                            if (clusterGlobalProperty.getValue() != null) {
                                output.append("\t\t<pageRank>" + ((Double) clusterGlobalProperty.getValue()[0]).toString() + "</pageRank>\n");
                            } else {
                                Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,(String) parameter[4].getValue() + " of actor ID " + list[i].getID() + " exists but  has no values");
                            }
                        } else {
                            Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE,"'" + (String) parameter[4].getValue() + "' of actor ID " + list[i].getID() + " exists but is not a Double property type");
                        }
                    } else {
                        Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.FINER,"'"+(String)parameter[4].getValue()+"' of actor ID "+list[i].getID()+" does not have a cluster ranking");
                    }

                    Property clusterProperty = list[i].getProperty((String) parameter[5].getValue());
                    if (clusterProperty != null) {
                        if (clusterProperty.getPropertyClass().isAssignableFrom(Double.class)) {
                            if (clusterProperty.getValue() != null) {
                                output.append("\t\t<pageRank>" + ((Double) clusterProperty.getValue()[0]).toString() + "</pageRank>\n");
                            } else {
                                Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,(String) parameter[5].getValue() + " of actor ID " + list[i].getID() + " exists but  has no values");
                            }
                        } else {
                            Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE,"'" + (String) parameter[5].getValue() + "' of actor ID " + list[i].getID() + " exists but is not a Double property type");
                        }
                    } else {
                        Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.FINER,"'"+(String)parameter[4].getValue()+"' of actor ID "+list[i].getID()+" does not have a cluster ranking");
                    }

                    output.append("\t\t<cluster>");
                    if (list[i].getProperty("cluster") != null) {
                        output.append((String) list[i].getProperty("cluster").getValue()[0]);
                    } else {
                        output.append("None");
                    }
                    output.append("</cluster>\n");
                    output.append("\t</paper>\n");
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
                }
                output.append("</pageRankValues>");
            } else {
                Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.WARNING,"No actors of type '" + parameter[1].getValue() + "' found");
            }
        } catch (IOException ex) {
            Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(OutputBibliographyXML.class.getName()).log(Level.SEVERE, null, ex);
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
            parameter[5].setValue("SubGraph PageRank");
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
