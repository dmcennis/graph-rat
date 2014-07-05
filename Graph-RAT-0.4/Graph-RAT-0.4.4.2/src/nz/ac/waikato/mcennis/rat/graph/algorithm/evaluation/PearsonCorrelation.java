/*
 * Created 24-3-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;

import java.util.HashMap;

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

import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.ParameterInternal;

import org.dynamicfactory.descriptors.SettableParameter;

import nz.ac.waikato.mcennis.rat.graph.link.Link;

import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;

import org.dynamicfactory.property.PropertyFactory;

import java.util.Iterator;

import java.util.Vector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Utilizes Given(user->artist) as ground truth and Derived(user->artist) as predictions.
 * Calcualtes the Pearsons Correlation between two sets of recommendation with
 * correlations made only if links are present in both sets.
 * 
 * Herlocker, J., J. Konstan, L. Terveen, J. Reidl. 2004. Evaluating collaborative 
 * filtering recommender systems. ACM Transactions on Information Systems 22(1):5-53
 *
 * @author Daniel McEnnis
 * 
 */
public class PearsonCorrelation extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    OutputDescriptorInternal[] output = new OutputDescriptorInternal[3];

    /** Creates a new instance of Evaluation */
    public PearsonCorrelation() {

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
        pearsonsCorrelation(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);

    }

    protected void pearsonsCorrelation(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double correlationSquaredSum = 0.0;

            double correlationSum = 0.0;

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
                    HashMap<Actor, Link> derived = new HashMap<Actor, Link>();
                    if (dLink != null) {
                        for (int i = 0; i < dLink.length; ++i) {
                            derived.put(dLink[i].getDestination(), dLink[i]);
                        }
                    }

                    Vector<Link> given = new Vector<Link>();
                    if (gLink != null) {
                        double sum = 0.0;
                        double squaredSum = 0.0;
                        for (int i = 0; i < gLink.length; ++i) {
                            if (gLink[i].getStrength() > 0.0) {
                                given.add(gLink[i]);
                                sum += gLink[i].getStrength();
                                squaredSum += gLink[i].getStrength() * gLink[i].getStrength();
                            }
                        }
                        if (gLink.length > 0) {
                            givenMean = sum / gLink.length;
                            givenSD = ((gLink.length * squaredSum) - sum * sum) / gLink.length;
                            givenSD += 1e-10;
                        }
                    }

                    double correlation = 0.0;
                    double sum = 0.0;
                    double squaredSum = 0.0;
                    for (int i = 0; i < given.size(); ++i) {
                        if (derived.containsKey(given.get(i).getDestination())) {
                            sum += derived.get(given.get(i).getDestination()).getStrength();
                            squaredSum += derived.get(given.get(i).getDestination()).getStrength() * derived.get(given.get(i).getDestination()).getStrength();
                        }
                    }
                    if (given.size() > 0) {
                        derivedMean = sum / given.size();
                        derivedSD = ((given.size() * squaredSum) - sum * sum) / given.size();
                    } else {
                        derivedMean = 0.0;
                        derivedSD = 0.0;
                    }
                    derivedSD += 1e-10;
                    for (int i = 0; i < given.size(); ++i) {
                        if (derived.containsKey(given.get(i).getDestination())) {
                            // add to mean
                            correlation += (derived.get(given.get(i).getDestination()).getStrength() - derivedMean) * (given.get(i).getStrength() - givenMean);
                        }
                        correlation += 1e-20;
                    }
                    if (given.size() > 0) {
                        correlation /= (given.size() * derivedSD * givenSD);
                    } else {
                        correlation = 1.0;
                    }

                    java.util.Properties props = new java.util.Properties();
                    props.setProperty("PropertyType", "Basic");
            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "PearsonsCorrelation");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "PearsonsCorrelation");
            }
                    props.setProperty("PropertyClass", "java.lang.Double");
                    Property precisionProperty = PropertyFactory.newInstance().create(props);
                    precisionProperty.add(new Double(correlation));
                    actor.add(precisionProperty);
                    correlationSum += correlation;
                    correlationSquaredSum += correlation * correlation;
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph
            double sd = ((n * correlationSquaredSum) - correlationSum * correlationSum) / n;

            double mean = correlationSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "PearsonsCorrelationSD");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "PearsonsCorrelationSD");
            }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "PearsonsCorrelation");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "PearsonsCorrelation");
            }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Pearsons Correlation\t" + mean);

            System.out.println("Pearsons Correlation SD\t" + sd);
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

        props.setProperty("Property", (String) parameter[4].getValue() + "PearsonsCorrelation");

        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "PearsonsCorrelationSD");

        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 2

        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "PearsonsCorrelation");

        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);

    }
}
