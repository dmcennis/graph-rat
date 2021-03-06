/**
 * Created 3/06/2008 - 15:11:32
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.algorithm.clustering;

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
import weka.clusterers.Clusterer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionHandler;

/**
 * Utilize an arbitrary probabilistic-clusterer (vector of probable membership
 * in a cluster).  Which clusterer used is determined by parameter.
 * 
 * @author Daniel McEnnis
 */
public class WekaProbablisticClusterer extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[9];
    InputDescriptor[] input = new InputDescriptor[2];
    OutputDescriptor[] output = new OutputDescriptor[2];

    public WekaProbablisticClusterer() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        try {
            Clusterer clusterer = (Clusterer) ((Class) parameter[1].getValue()).newInstance();
            Property graphProperty = g.getProperty((String) parameter[2].getValue());
            Instances data = null;
            if (graphProperty != null) {
                Object[] value = graphProperty.getValue();
                if (value.length > 0) {
                    data = (Instances) value[0];
                }
            }
            if (data != null) {
                String[] options = ((String) parameter[3].getValue()).split("\\s+");
                ((OptionHandler) clusterer).setOptions(options);
                clusterer.buildClusterer(data);
                Actor[] actors = g.getActor((String) parameter[4].getValue());
                if (actors != null) {
                    for (int i = 0; i < actors.length; ++i) {
                        Property instanceProperty = actors[i].getProperty((String) parameter[5].getValue());
                        if (instanceProperty != null) {
                            Object[] value = instanceProperty.getValue();
                            if (value.length > 0) {
                                Instance instance = (Instance) (value[0]);
                                double[] cluster = new double[]{};
                                try {
                                    cluster = clusterer.distributionForInstance(instance);
                                } catch (Exception ex) {
                                    Logger.getLogger(WekaProbablisticClusterer.class.getName()).log(Level.SEVERE, "ClusterInstance on clusterer failed", ex);
                                }
                                Properties props = new Properties();
                                props.setProperty("PropertyType", "Basic");
                                props.setProperty("PropertyID", (String) parameter[6].getValue());
                                props.setProperty("PropertyClass", cluster.getClass().getName());
                                Property clusterAssignment = PropertyFactory.newInstance().create(props);
                                clusterAssignment.add(cluster);
                                actors[i].add(clusterAssignment);
                            }
                        }
                    }
                    if ((Boolean) parameter[7].getValue()) {
                        Properties props = new Properties();
                        props.setProperty("PropertyType", "Basic");
                        props.setProperty("PropertyID", (String) parameter[8].getValue());
                        props.setProperty("PropertyClass", "weka.core.Clusterer");
                        Property clusterCore = PropertyFactory.newInstance().create(props);
                        clusterCore.add(clusterer);
                        g.add(clusterCore);
                    }
                } else {
                    Logger.getLogger(WekaProbablisticClusterer.class.getName()).log(Level.WARNING, "No actors of type '" + (String) parameter[4].getValue() + "' were found");
                }
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(WekaProbablisticClusterer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(WekaProbablisticClusterer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(WekaProbablisticClusterer.class.getName()).log(Level.SEVERE, null, ex);
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
     * Parameter:<br/>
     * <br/>
     * <ul>
    * <li/><b>name</b>: name of this Algorithm.  Default is 'Weka Classifier Clustering Algorithm'
     * <li/><b>class</b>: class of the clusterer. Default is 'weka.clusterers.Cobweb'
     * <li/><b>options</b>: string of options for this clusterer. Default is ''
     * <li/><b>instancesProperty</b>: ID of the graph property containing the 
     * Instances object. Deafult is 'AudioFile'
     * <li/><b>actorType</b>: mode of the actor containing the Instance property to
     * be clustered. Default is 'User'
     * <li/><b>actorProperty</b>: ID of the property where Instance objects are stored.
     * Default is 'AudioFile'
     * <li/><b>destinationProperty</b>: ID of the property to create on each actor.
     * Deafult is 'clusterID'
     * <li/><b>storeClassifier</b>: is the classifier to be stored on the graph.
     * Default is 'false'
     * <li/><b>classifierProperty</b>: property ID for storing the built classifier.
     * Deafult is 'clusterer'
      * </ul>
     * @param map parameters to be loaded - may be null.
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
            parameter[0].setValue("Weka Probablistic Clustering Algorithm");
        }

        props.setProperty("Type", "java.lang.Class");
        props.setProperty("Name", "class");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("class") != null)) {
            try {
                parameter[1].setValue(Class.forName(map.getProperty("class")));
            } catch (ClassNotFoundException ex) {
                parameter[1].setValue(weka.clusterers.EM.class);
                Logger.getLogger(WekaProbablisticClusterer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            parameter[1].setValue(weka.clusterers.EM.class);
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "options");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("options") != null)) {
            parameter[2].setValue(map.getProperty("options"));
        } else {
            parameter[2].setValue("");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "instancesProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("options") != null)) {
            parameter[3].setValue(map.getProperty("options"));
        } else {
            parameter[3].setValue("AudioFile");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[4].setValue(map.getProperty("actorType"));
        } else {
            parameter[4].setValue("User");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorProperty") != null)) {
            parameter[5].setValue(map.getProperty("actorProperty"));
        } else {
            parameter[5].setValue("AudioFile");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "destinationProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationProperty") != null)) {
            parameter[6].setValue(map.getProperty("destinationProperty"));
        } else {
            parameter[6].setValue("clusterID");
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "storeClassifier");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("storeClassifier") != null)) {
            parameter[7].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("storeClassifier"))));
        } else {
            parameter[7].setValue(new Boolean(false));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "classifierProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("classifierProperty") != null)) {
            parameter[8].setValue(map.getProperty("classifierProperty"));
        } else {
            parameter[8].setValue("clusterer");
        }

        // Create Input Descriptors
        // Construct input descriptors
        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[2].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[5].getValue());
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        output = new OutputDescriptor[1];
        // Construct Output Descriptors
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[6].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[8].getValue());
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
