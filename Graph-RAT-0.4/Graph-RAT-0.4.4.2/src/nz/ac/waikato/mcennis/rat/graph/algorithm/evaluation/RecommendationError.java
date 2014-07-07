/*
 * Created 24-3-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;

import java.util.HashSet;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import org.dynamicfactory.descriptors.DescriptorFactory;

import org.dynamicfactory.descriptors.InputDescriptor;

import org.dynamicfactory.descriptors.InputDescriptorInternal;

import org.dynamicfactory.descriptors.OutputDescriptor;

import org.dynamicfactory.descriptors.OutputDescriptorInternal;

import org.dynamicfactory.descriptors.SettableParameter;

import nz.ac.waikato.mcennis.rat.graph.link.Link;

import java.util.Iterator;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Takes Given(user->artist) links as ground truth and then calculates error using
 * Derived(user->artist) as the predicted values.  Error is the percentage of the
 * recommendations that are not given.  Described in Herlocker et al.
 * 
 * Herlocker, J., J. Konstan, L. Terveen, J. Reidl. 2004. Evaluating collaborative 
 * filtering recommender systems. ACM Transactions on Information Systems 22(1):5-53
 * 
 * @author Daniel McEnnis
 */
public class RecommendationError extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    OutputDescriptorInternal[] output = new OutputDescriptorInternal[3];

    /** Creates a new instance of Evaluation */
    public RecommendationError() {

        init(null);

    }

    /**
     * Calculate all the evaluation metrics against the given graph.
     * 
     * TODO: add F-measure stastic
     * 
     */
    public void execute(Graph g) {

        fireChange(Scheduler.SET_ALGORITHM_COUNT, 1);
        AdHoc(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);

    }

    protected void AdHoc(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double rocSquaredSum = 0.0;

            double rocSum = 0.0;

            while (it.hasNext()) {
                try {
                    Actor actor = it.next();
                    // determine given (ground truth) links
                    Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);
                    Link[] dLink = null;
                    if ((Boolean) parameter[5].getValue()) {
                        dLink = g.getLinkBySource((String) parameter[2].getValue() + g.getID(), actor);
                    } else {
                        dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);
                    }
                    double givenMean = 0.0;
                    double givenSD = 0.0;
                    double derivedMean = 0.0;
                    double derivedSD = 0.0;

                    // acquire derived links

                    if ((dLink != null) && (gLink != null)) {
                        HashSet<Actor> given = new HashSet<Actor>();
                        for (int i = 0; i < gLink.length; ++i) {
                            given.add(gLink[i].getDestination());
                        }

                        int count = 0;
                        for (int i = 0; i < dLink.length; ++i) {
                            if (!given.contains(dLink[i].getDestination())) {
                                count++;
                            }
                        }
                        double roc = (double) count / (double) dLink.length;
                        java.util.Properties props = new java.util.Properties();
                        props.setProperty("PropertyType", "Basic");
                        if ((Boolean) parameter[6].getValue()) {
                            props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "AdHocClassification");
                        } else {
                            props.setProperty("PropertyID", (String) parameter[4].getValue() + "AdHocClassification");
                        }
                        props.setProperty("PropertyClass", "java.lang.Double");
                        Property precisionProperty = PropertyFactory.newInstance().create(props);
                        precisionProperty.add(new Double(roc));
                        actor.add(precisionProperty);
                        rocSum += roc;
                        rocSquaredSum += roc * roc;
                    } else if (dLink != null) {
                        java.util.Properties props = new java.util.Properties();
                        props.setProperty("PropertyType", "Basic");
                        if ((Boolean) parameter[6].getValue()) {
                            props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "AdHocClassification");
                        } else {
                            props.setProperty("PropertyID", (String) parameter[4].getValue() + "AdHocClassification");
                        }
                        props.setProperty("PropertyClass", "java.lang.Double");
                        Property precisionProperty = PropertyFactory.newInstance().create(props);
                        precisionProperty.add(new Double(1.0));
                        actor.add(precisionProperty);
                        rocSum += 1.0;
                        rocSquaredSum += 1.0;
                    }


                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(ROCAreaEvaluation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActor("User").length;

            // calculate mean and SD of precison for graph
            double sd = ((n * rocSquaredSum) - rocSum * rocSum) / n;

            double mean = rocSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

            if ((Boolean) parameter[6].getValue()) {
                props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "AdHocClassificationSD");
            } else {
                props.setProperty("PropertyID", (String) parameter[4].getValue() + "AdHocClassificationSD");
            }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

            if ((Boolean) parameter[6].getValue()) {
                props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "AdHocClassification");
            } else {
                props.setProperty("PropertyID", (String) parameter[4].getValue() + "AdHocClassification");
            }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Ad Hoc Classification\t" + mean);

            System.out.println("Ad Hoc Classification SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
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
     * Parameters to be initialized
     * 
     * <ol>
     * <li>'name' - name of this instance of this algorithm. Deafult 'Evaluation'
     * <li>'relationGroundTruth' - type (relation) of link that describes ground
     * truth relations between two actor types (modes). Default 'Given'
     * <li>'relationDerived' - type (relation) of link that describes calculated
     * references to be evaluated between two modes. Deafult 'Derived'.
     * <li>'propertyPrefix' - prefix for the property names generated. Default 
     * 'Evaluation '.
     * </ol>
     * <br>
     * <br>Input 1 - Link
     * <br>Input 2 - Link
     * <br>Output 1-12 - Graph Property
     * 
     * 
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

            parameter[0].setValue("Evaluation");

        }



        // Parameter 1 - relation ground truth

        props.setProperty("Type", "java.lang.String");

        props.setProperty("Name", "relationGroundTruth");

        props.setProperty("Class", "Basic");

        props.setProperty("Structural", "true");

        parameter[1] = DescriptorFactory.newInstance().createParameter(props);

        if ((map != null) && (map.getProperty("relationGroundTruth") != null)) {

            parameter[1].setValue(map.getProperty("relationGroundTruth"));

        } else {

            parameter[1].setValue("Given");

        }



        // Parameter 2 - relation derived

        props.setProperty("Type", "java.lang.String");

        props.setProperty("Name", "relationDerived");

        props.setProperty("Class", "Basic");

        props.setProperty("Structural", "true");

        parameter[2] = DescriptorFactory.newInstance().createParameter(props);

        if ((map != null) && (map.getProperty("relationDerived") != null)) {

            parameter[2].setValue(map.getProperty("relationDerived"));

        } else {

            parameter[2].setValue("Derived");

        }



        // Parameter 3 - actorType

        props.setProperty("Type", "java.lang.String");

        props.setProperty("Name", "actorType");

        props.setProperty("Class", "Basic");

        props.setProperty("Structural", "true");

        parameter[3] = DescriptorFactory.newInstance().createParameter(props);

        if ((map != null) && (map.getProperty("actorType") != null)) {

            parameter[3].setValue(map.getProperty("actorType"));

        } else {

            parameter[3].setValue("User");

        }



        // Parameter 4 - propertyPrefix

        props.setProperty("Type", "java.lang.String");

        props.setProperty("Name", "propertyPrefix");

        props.setProperty("Class", "Basic");

        props.setProperty("Structural", "true");

        parameter[4] = DescriptorFactory.newInstance().createParameter(props);

        if ((map != null) && (map.getProperty("propertyPrefix") != null)) {

            parameter[4].setValue(map.getProperty("propertyPrefix"));

        } else {

            parameter[4].setValue("Evaluation ");

        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "sourceAppendGraphID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceAppendGraphID") != null)) {
            parameter[5].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("sourceAppendGraphID"))));
        } else {
            parameter[5].setValue(new Boolean(false));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "destinationAppendGraphID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationAppendGraphID") != null)) {
            parameter[6].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("destinationAppendGraphID"))));
        } else {
            parameter[6].setValue(new Boolean(false));
        }



        // input 0

        props.setProperty("Type", "Link");

        props.setProperty("Relation", (String) parameter[1].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.remove("Property");

        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);



        // input 1

        props.setProperty("Type", "Link");

        props.setProperty("Relation", (String) parameter[2].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.remove("Property");

        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);



        // output 0

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "AdHocClassification");

        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "AdHocClassificationSD");

        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 2

        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "AdHocClassification");

        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);


    }
}
