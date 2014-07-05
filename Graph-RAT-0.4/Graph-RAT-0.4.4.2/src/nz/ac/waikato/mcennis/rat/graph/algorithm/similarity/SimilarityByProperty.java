/*
 * Created 28/05/2008-13:04:32
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.similarity;

import java.util.Properties;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DoubleArrayDataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DistanceFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DistanceFunction;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import org.dynamicfactory.property.Property;

/**
 * Calculates similarity between actors via a double[] describing a vector property
 * 
 * @author Daniel McEnnis
 */
public class SimilarityByProperty extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[6];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[1];

    public SimilarityByProperty() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] actors = g.getActor((String) parameter[1].getValue());
        if (actors != null) {
            Properties props = new Properties();
            props.setProperty("DistanceFunction", (String) parameter[3].getValue());
            DistanceFunction distance = DistanceFactory.newInstance().create(props);
            for (int i = 0; i < actors.length; ++i) {
                for (int j = 0; j < actors.length; ++j) {
                    if (i != j) {
                        Property leftProperty = actors[i].getProperty((String) parameter[2].getValue());
                        Property rightProperty = actors[j].getProperty((String) parameter[2].getValue());
                        if ((leftProperty != null) && (rightProperty != null)) {
                            Object[] leftObject = leftProperty.getValue();
                            Object[] rightObject = rightProperty.getValue();
                            if ((leftObject != null) && (leftObject.length > 0) && (rightObject != null) && (rightObject.length > 0)) {
                                DataVector left = new DoubleArrayDataVector((double[]) (leftObject[0]));
                                DataVector right = new DoubleArrayDataVector((double[]) (rightObject[0]));

                                double similarity = distance.distance(left, right);

                                if (similarity >= ((Double) parameter[4].getValue()).doubleValue()) {
                                    props.setProperty("LinkClass", "Basic");
                                    props.setProperty("LinkType", (String) parameter[5].getValue());
                                    Link sim = LinkFactory.newInstance().create(props);
                                    sim.set(actors[i], similarity, actors[j]);
                                    g.add(sim);
                                }
                            }
                        }
                    }
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
     * <li/><b>name</b>: Name of this algorithm. Default is 'Simlarity By Property'
     * <li/><b>actorType</b>: mode of actor to calculate similarity with. Default 
     * is 'tag'
     * <li/><b>property</b>: Name of the property containing double arrays. Default
     * is 'Tags'
     * <li/><b>distanceFunction</b>: Distance function to use. Default is 'EuclideanDistance'
     * <li/><b>threshold</b>: threshold for creating a similarity link. Default is '0.75'
     * <li/><b>relation</b>: relation for similarity. Default is 'Similarity'
     * </ul>
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
            parameter[0].setValue("Simlarity By Property");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("tag");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "property");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("property") != null)) {
            parameter[2].setValue(map.getProperty("property"));
        } else {
            parameter[2].setValue("Tags");
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

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "threshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("threshold") != null)) {
            parameter[4].setValue(new Double(Double.parseDouble(map.getProperty("threshold"))));
        } else {
            parameter[4].setValue(new Double(0.75));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[5].setValue(map.getProperty("relation"));
        } else {
            parameter[5].setValue("Similarity");
        }

        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[2].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[5].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
