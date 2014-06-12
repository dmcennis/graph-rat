/*
 * Created 29/05/2008-16:15:19
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.similarity;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DoubleArrayDataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DistanceFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DistanceFunction;
import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.util.Duples;

/**
 * Calculates the similarity between two graphs by a given property.
 * 
 * @author Daniel McEnnis
 */
public class GraphSimilarityByProperty extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[1];

    /**
     * Create a new graph similarity algorithm with deafult parameters.
     */
    public GraphSimilarityByProperty() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Graph[] data = null;
        if ((Boolean) parameter[1].getValue()) {
            data = g.getChildren();
        } else {
            data = g.getGraphs(Pattern.compile((String) parameter[2].getValue()));
        }
        if (data != null) {
            Properties props = new Properties();
            props.setProperty("DistanceFunction", (String) parameter[3].getValue());
            DistanceFunction distance = DistanceFactory.newInstance().create(props);
            for (int i = 0; i < data.length; ++i) {
                props.setProperty("PropertyClass", "nz.ac.waikato.mcennis.rat.util.Duples");
                props.setProperty("PropertyType", "Basic");
                props.setProperty("PropertyID", (String) parameter[6].getValue());
                Property similarityProperty = PropertyFactory.newInstance().create(props);
                for (int j = 0; j < data.length; ++j) {
                    if (i != j) {
                        Property leftProperty = data[i].getProperty((String) parameter[4].getValue());
                        Property rightProperty = data[j].getProperty((String) parameter[4].getValue());
                        if ((leftProperty != null) && (rightProperty != null)) {
                            Object[] leftObject = leftProperty.getValue();
                            Object[] rightObject = rightProperty.getValue();
                            if ((leftObject != null) && (leftObject.length > 0) && (rightObject != null) && (rightObject.length > 0)) {
                                DataVector left = new DoubleArrayDataVector((double[]) (leftObject[0]));
                                DataVector right = new DoubleArrayDataVector((double[]) (rightObject[0]));

                                double similarity = distance.distance(left, right);

                                if (similarity > ((Double) parameter[5].getValue()).doubleValue()) {
                                    try {
                                        Duples<String, Double> sim = new Duples<String, Double>();
                                        sim.setLeft(data[j].getID());
                                        sim.setRight(similarity);
                                        similarityProperty.add(sim);
                                    } catch (InvalidObjectTypeException ex) {
                                        Logger.getLogger(GraphSimilarityByProperty.class.getName()).log(Level.SEVERE, "Property class does not match Duples", ex);
                                    }
                                }
                            }
                        }
                    }
                }
                if (similarityProperty.getValue().length != 0) {
                    data[i].add(similarityProperty);
                }
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
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of this algorithm. Deafult is 'Graph Simlarity By Property'
     * <li/><b>byChildren</b>: Should graphs be chosen by being children of the given graph.
     * Default is 'true'
     * <li/><b>pattern</b>: Regular expression describing the graph IDs to be included
     * in this calculation.  The closest-to-root graph matching this pattern is used.
     * Deafult is '.*'
     * <li/><b>distanceFunction</b>: Which distance functiuon to use. Default is 'EuclideanDistance'
     * <li/><b>sourceProperty</b>: Which property contains the double[] to use as source data. 
     * Default is 'Directed Triples Histogram'
     * <li/><b>threshold</b>: Threshold for declaring two graphs similar. Default is '0.75'
     * <li/><b>destinationProperty</b>: Property for storing graph similarity.  This is stored
     * as duples&lt;GraphID,Double&gt;. Deafult is 'Similarity'
     * </ul>
     * 
     * @param map parameters to be loaded - may be null
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
            parameter[0].setValue("Graph Simlarity By Property");
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "byChildren");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("byChildren") != null)) {
            parameter[1].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("byChildren"))));
        } else {
            parameter[1].setValue(new Boolean(true));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "pattern");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("pattern") != null)) {
            parameter[2].setValue(map.getProperty("pattern"));
        } else {
            parameter[2].setValue(".*");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "distanceFunction");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("distanceFunction") != null)) {
            parameter[3].setValue(map.getProperty("distanceFunction"));
        } else {
            parameter[3].setValue("CosineDistance");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "sourceProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceProperty") != null)) {
            parameter[4].setValue(map.getProperty("sourceProperty"));
        } else {
            parameter[4].setValue("Directed Triples Histogram");
        }

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "threshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("threshold") != null)) {
            parameter[5].setValue(new Double(Double.parseDouble(map.getProperty("threshold"))));
        } else {
            parameter[5].setValue(new Double(0.75));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "destinationProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationProperty") != null)) {
            parameter[6].setValue(map.getProperty("destinationProperty"));
        } else {
            parameter[6].setValue("Similarity");
        }

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[4].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[6].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
