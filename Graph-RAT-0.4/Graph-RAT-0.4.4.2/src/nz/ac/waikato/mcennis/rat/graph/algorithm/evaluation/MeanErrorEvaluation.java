/*
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;

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

import java.util.Vector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Utilizes Given(user->artist) as ground truth and Derived(user->artist) as predicted values.
 * Error is calculated from difference between derived and given link strengths.
 * Derived links with no given are compared against link strength of 0 while 
 * the reverse are ignored.
 *
 * Herlocker, J., J. Konstan, L. Terveen, J. Reidl. 2004. Evaluating collaborative 
 * filtering recommender systems. ACM Transactions on Information Systems 22(1):5-53
 *
 * 
 * @author Daniel McEnnis
 * 
 * 
 */
public class MeanErrorEvaluation extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    OutputDescriptorInternal[] output = new OutputDescriptorInternal[9];
    double maxRecommendation = 0.0;
    double minRecommendation = 0.0;

    /** Creates a new instance of Evaluation */
    public MeanErrorEvaluation() {

        init(null);

    }

    /**
     * Calculate all the evaluation metrics against the given graph.
     * 
     * TODO: add F-measure stastic
     * 
     */
    public void execute(Graph g) {
        fireChange(Scheduler.SET_ALGORITHM_COUNT, 3);
//        scaleRecommendations(g);
//        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);
        meanError(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);
        meanSquaredError(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);
        rootMeanSquaredError(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 3);

    }

    protected void scaleRecommendations(Graph g) {
        Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());
        while (it.hasNext()) {
            Actor actor = it.next();
            Link[] dLink = null;
            if((Boolean)parameter[5].getValue()){
                dLink = g.getLinkBySource((String) parameter[2].getValue()+g.getID(), actor);
            }else{
                dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);
            }
            if (dLink != null) {
                for (int i = 0; i < dLink.length; ++i) {
                    if (maxRecommendation < dLink[i].getStrength()) {
                        maxRecommendation = dLink[i].getStrength();
                    }
                    if (minRecommendation > dLink[i].getStrength()) {
                        minRecommendation = dLink[i].getStrength();
                    }
                }
            }
        }
        if (maxRecommendation <= 0.0) {
            maxRecommendation = 1.0;
        }
        if (minRecommendation >= 0.0) {
            minRecommendation = 1.0;
        } else {
            Math.abs(minRecommendation);
        }
    }

    protected void meanError(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double precisionSquaredSum = 0.0;

            double precisionSum = 0.0;

            while (it.hasNext()) {
                try {

                    Actor actor = it.next();

                    // determine given (ground truth) links
                    Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);


            Link[] dLink = null;
            if((Boolean)parameter[5].getValue()){
                dLink = g.getLinkBySource((String) parameter[2].getValue()+g.getID(), actor);
            }else{
                dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);
            }

                    // acquire derived links
                    Vector<Link> derived = new Vector<Link>();



                    if (dLink != null) {

                        for (int i = 0; i < dLink.length; ++i) {

                            if (dLink[i].getStrength() > 0) {

                                derived.add(dLink[i]);
                            }
                        }
                    }


                    double numerator = 0.0;

                    for (int i = 0; i < derived.size(); ++i) {
                        boolean match = false;
                        if (gLink != null) {
                            for (int j = 0; j < gLink.length; ++j) {

                                if (derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID())) {
                                    match = true;
                                    numerator += Math.abs((derived.get(i).getStrength()) - gLink[j].getStrength());
                                }
                            }
                        }
                        if (!match) {
                            numerator += Math.abs((derived.get(i).getStrength()));
                        }
                    }

                    double precision = 0.0;

                    if (derived.size() > 0) {

                        precision = numerator / ((double) derived.size());
                    } else {
                        precision = 0.0;
                    }

                    java.util.Properties props = new java.util.Properties();

                    props.setProperty("PropertyType", "Basic");

                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "MeanError");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "MeanError");
                    }
                    props.setProperty("PropertyClass", "java.lang.Double");

                    Property precisionProperty = PropertyFactory.newInstance().create(props);

                    precisionProperty.add(new Double(precision));

                    actor.add(precisionProperty);

                    precisionSum += precision;

                    precisionSquaredSum += precision * precision;
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph
            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;

            double mean = precisionSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

            if((Boolean)parameter[6].getValue()){
                props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "MeanErrorSD");
            }else{
                props.setProperty("PropertyID", (String) parameter[4].getValue() + "MeanErrorSD");
            }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

            if((Boolean)parameter[6].getValue()){
                props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID()+ "MeanError");
            }else{
                props.setProperty("PropertyID", (String) parameter[4].getValue() + "MeanError");                
            }
            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Mean Error\t" + mean);

            System.out.println("Mean Error SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void meanSquaredError(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double precisionSquaredSum = 0.0;

            double precisionSum = 0.0;

            while (it.hasNext()) {
                try {

                    Actor actor = it.next();

                    // determine given (ground truth) links
                    Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);


            Link[] dLink = null;
            if((Boolean)parameter[5].getValue()){
                dLink = g.getLinkBySource((String) parameter[2].getValue()+g.getID(), actor);
            }else{
                dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);
            }

                    // acquire derived links
                    Vector<Link> derived = new Vector<Link>();



                    if (dLink != null) {

                        for (int i = 0; i < dLink.length; ++i) {

                            if (dLink[i].getStrength() > 0) {

                                derived.add(dLink[i]);
                            }
                        }
                    }


                    double numerator = 0.0;

                    for (int i = 0; i < derived.size(); ++i) {
                        boolean match = false;
                        if (gLink != null) {

                            for (int j = 0; j < gLink.length; ++j) {

                                if (derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID())) {
                                    match = true;
                                    numerator += Math.pow((derived.get(i).getStrength()) - gLink[j].getStrength(), 2);
                                }
                            }
                        }
                        if (!match) {
                            numerator += Math.abs((derived.get(i).getStrength()));
                        }
                    }

                    double precision = 0.0;

                    if (derived.size() > 0) {

                        precision = numerator / ((double) derived.size());
                    }

                    java.util.Properties props = new java.util.Properties();

                    props.setProperty("PropertyType", "Basic");


            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "MeanSquaredError");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "MeanSquaredError");
            }
                    props.setProperty("PropertyClass", "java.lang.Double");

                    Property precisionProperty = PropertyFactory.newInstance().create(props);

                    precisionProperty.add(new Double(precision));

                    actor.add(precisionProperty);

                    precisionSum += precision;

                    precisionSquaredSum += precision * precision;
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph
            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;

            double mean = precisionSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "MeanSquaredErrorSD");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "MeanSquaredErrorSD");
            }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "MeanSquaredError");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "MeanSquaredError");
            }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Mean Squared Error\t" + mean);

            System.out.println("Mean Squared Error SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void rootMeanSquaredError(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double precisionSquaredSum = 0.0;

            double precisionSum = 0.0;

            while (it.hasNext()) {
                try {

                    Actor actor = it.next();

                    // determine given (ground truth) links
                    Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);


            Link[] dLink = null;
            if((Boolean)parameter[5].getValue()){
                dLink = g.getLinkBySource((String) parameter[2].getValue()+g.getID(), actor);
            }else{
                dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);
            }

                    // acquire derived links
                    Vector<Link> derived = new Vector<Link>();



                    if (dLink != null) {

                        for (int i = 0; i < dLink.length; ++i) {

                            if (dLink[i].getStrength() > 0) {

                                derived.add(dLink[i]);
                            }
                        }
                    }


                    double numerator = 0.0;

                    for (int i = 0; i < derived.size(); ++i) {
                        boolean match = false;
                        if (gLink != null) {
                            for (int j = 0; j < gLink.length; ++j) {

                                if (derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID())) {
                                    match = true;
                                    numerator += Math.pow((derived.get(i).getStrength()) - gLink[j].getStrength(), 2);
                                }
                            }
                        }
                        if (!match) {
                            numerator += Math.pow(derived.get(i).getStrength(), 2);
                        }
                    }

                    double precision = 0.0;

                    if (derived.size() > 0) {

                        precision = Math.sqrt(numerator) / ((double) derived.size());
                    }

                    java.util.Properties props = new java.util.Properties();

                    props.setProperty("PropertyType", "Basic");

            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "RootMeanSquaredError");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "RootMeanSquaredError");
            }
                    props.setProperty("PropertyClass", "java.lang.Double");

                    Property precisionProperty = PropertyFactory.newInstance().create(props);

                    precisionProperty.add(new Double(precision));

                    actor.add(precisionProperty);

                    precisionSum += precision;

                    precisionSquaredSum += precision * precision;
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph
            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;

            double mean = precisionSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "RootMeanSquaredErrorSD");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "RootMeanSquaredErrorSD");
            }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

            if((Boolean)parameter[6].getValue()){
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "RootMeanSquaredError");
            }else{
                    props.setProperty("PropertyID", (String) parameter[4].getValue() + "RootMeanSquaredError");
            }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Root Mean Sqaured Error\t" + mean);

            System.out.println("Root Mean Sqaured Error SD\t" + sd);
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

        props.setProperty("Property", (String) parameter[4].getValue() + "MeanError");

        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "MeanErrorSD");

        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "MeanError");

        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 2

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "MeanSquaredError");

        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 3

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "MeanSquaredErrorSD");

        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);


        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "MeanSquaredError");

        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);




        // output 6

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "RootMeanSqauredError");

        output[6] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 7

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "RootMeanSqauredErrorSD");

        output[7] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "RootMeanSqauredError");

        output[8] = DescriptorFactory.newInstance().createOutputDescriptor(props);



    }
}
