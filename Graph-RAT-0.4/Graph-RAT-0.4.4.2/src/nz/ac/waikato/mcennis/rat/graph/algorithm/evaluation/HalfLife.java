/*
 * Created 24-3-08
 * 
 * Copyright Daniel McEnnis, see license.txt
 * 
 */
package org.mcennis.graphrat.algorithm.evaluation;

import java.util.HashMap;

import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.dynamicfactory.descriptors.DescriptorFactory;

import org.dynamicfactory.descriptors.InputDescriptor;

import org.dynamicfactory.descriptors.InputDescriptorInternal;

import org.dynamicfactory.descriptors.OutputDescriptor;

import org.dynamicfactory.descriptors.OutputDescriptorInternal;

import org.dynamicfactory.descriptors.SettableParameter;

import org.mcennis.graphrat.link.Link;

import java.util.Iterator;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.scheduler.Scheduler;

/**
 * 
 * Uses Given(user->artist) as valued ground truth.  Uses Derived(user->artist)
 * as predicted data.  Sequentially moving from highest predicted recommendation, 
 * the score (from ground truth) is added in, divided by an exponentially increasing
 * penalty. The result is a value between 0 and 1 where 0
 * contained nothing from the recommendation list while
 * 1 contained the entire recommended list from highest
 * score to lowest score.
 * 
 * Herlocker, J., J. Konstan, L. Terveen, J. Reidl. 2004. Evaluating collaborative 
 * filtering recommender systems. ACM Transactions on Information Systems 22(1):5-53
 *
 * @author Daniel McEnnis
 */
public class HalfLife extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    OutputDescriptorInternal[] output = new OutputDescriptorInternal[3];

    /** Creates a new instance of Evaluation */
    public HalfLife() {

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
        halfLife(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);

    }

    protected void halfLife(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double halfLifeSquaredSum = 0.0;

            double halfLifeSum = 0.0;

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
                    HashMap<Actor, Double> strength = new HashMap<Actor, Double>();
                    double rA = 0.0;
                    double rAMax = 0.0;
                    double alpha = 5.0;

                    // create max value from given list and populate artist->strength map
                    if (gLink != null) {
                        java.util.Arrays.sort(gLink);
                        for (int i = gLink.length - 1; i >= 0; i--) {
                            strength.put(gLink[i].getDestination(), gLink[i].getStrength());
                            if (gLink[i].getStrength() > 0) {
                                rAMax += gLink[i].getStrength() / (Math.pow(2.0, (gLink.length - (i + 1)) / (alpha - 1.0)));
                            }
                        }
                    } else {
                        rAMax = Double.NaN;
                    }

                    // calculate the score for derived list
                    if (dLink != null) {
                        java.util.Arrays.sort(dLink);
                        for (int i = dLink.length - 1; i >= 0; --i) {
                            if (strength.containsKey(dLink[i].getDestination())) {
                                rA += strength.get(dLink[i].getDestination()) / (Math.pow(2.0, (dLink.length - (i + 1)) / (alpha - 1.0)));
                            }
                        }
                    }

                    if (Double.isNaN(rAMax)) {
                        if ((dLink == null) || (dLink.length == 0)) {
                            rA = 1.0;
                        } else {
                            rA = 0.0;
                        }
                    } else {
                        if (rAMax != 0) {
                            rA = rA / rAMax;
                        } else if (rA != 0) {
                            rA = 1.0;
                        } else {
                            rA = 0.0;
                        }
                        java.util.Properties props = new java.util.Properties();
                        props.setProperty("PropertyType", "Basic");
                        if ((Boolean) parameter[6].getValue()) {
                            props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "HalfLife");
                        } else {
                            props.setProperty("PropertyID", (String) parameter[4].getValue() + "HalfLife");
                        }
                        props.setProperty("PropertyClass", "java.lang.Double");
                        Property precisionProperty = PropertyFactory.newInstance().create(props);
                        precisionProperty.add(new Double(rA));
                        actor.add(precisionProperty);
                    }

                    halfLifeSum += rA;
                    halfLifeSquaredSum += rA * rA;
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph
            double sd = ((n * halfLifeSquaredSum) - halfLifeSum * halfLifeSum) / n;

            double mean = halfLifeSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

            if ((Boolean) parameter[6].getValue()) {
                props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "HalfLifeSD");
            } else {
                props.setProperty("PropertyID", (String) parameter[4].getValue() + "HalfLifeSD");
            }

            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

            props.setProperty("PropertyID", (String) parameter[4].getValue() + "HalfLife");

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Half Life\t" + mean);

            System.out.println("Half Life SD\t" + sd);
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
     * <li>'alpha' - parameter describing nature of exponential drop off. Default 5.0
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

        props.setProperty("Property", (String) parameter[4].getValue() + "HalfLife");

        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "HalfLifeSD");

        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 2

        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "HalfLife");

        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
