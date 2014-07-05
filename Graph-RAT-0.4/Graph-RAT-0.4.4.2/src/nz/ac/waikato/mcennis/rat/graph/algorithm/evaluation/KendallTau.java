/*
 * Created 24-3-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;

import java.util.HashMap;

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
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * This compares all ordered pairs of recommendations.  The algorithm uses 
 * Given(usr->artist) as ground truth and Derived(user->artist) as predicted values
 * The score is based on correctly ordered pairs minus incorrectly ordered pairs
 * with pairs with unrocemmended items having no value.
 * 
 * Herlocker, J., J. Konstan, L. Terveen, J. Reidl. 2004. Evaluating collaborative 
 * filtering recommender systems. ACM Transactions on Information Systems 22(1):5-53
 *
 * @author Daniel McEnnis
 * 
 */
public class KendallTau extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    OutputDescriptorInternal[] output = new OutputDescriptorInternal[3];

    /** Creates a new instance of Evaluation */
    public KendallTau() {

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
        kendallTau(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);

    }

    protected void kendallTau(Graph g) {
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
                    if((Boolean)parameter[5].getValue()){
                        dLink = g.getLinkBySource((String) parameter[2].getValue()+g.getID(), actor);
                    }else{
                        dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);                     
                    }
                    double givenMean = 0.0;
                    double givenSD = 0.0;
                    double derivedMean = 0.0;
                    double derivedSD = 0.0;

                    HashMap<Actor, HashMap<Actor, Integer>> given = new HashMap<Actor, HashMap<Actor, Integer>>();
                    HashSet<Actor> allArtists = new HashSet<Actor>();
                    
                    
                    if ((gLink != null) && (dLink != null)) {
                        int c = 0;
                        int d = 0;
                        int tp = 0;
                        int tr = 0;
                        for(int i=0;i<gLink.length;++i){
                            allArtists.add(gLink[i].getDestination());
                        }
                        for(int i=0;i<dLink.length;++i){
                            allArtists.add(dLink[i].getDestination());
                        }
                        
                        for (int i = 0; i < gLink.length; ++i) {
                            for (int j = 0; j < gLink.length; ++j) {
                                if (i != j) {
                                    if (!given.containsKey(gLink[i].getDestination())) {
                                        given.put(gLink[i].getDestination(), new HashMap<Actor, Integer>());
                                    }
                                    if (gLink[i].getStrength() > gLink[j].getStrength()) {
                                        given.get(gLink[i].getDestination()).put(gLink[j].getDestination(), 1);
                                    } else if (gLink[i].getStrength() == gLink[j].getStrength()) {
                                        given.get(gLink[i].getDestination()).put(gLink[j].getDestination(), 0);
                                        tr++;
                                    } else {
                                        given.get(gLink[i].getDestination()).put(gLink[j].getDestination(), -1);
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < dLink.length; ++i) {
                            if (given.containsKey(dLink[i].getDestination())) {
                                for (int j = 0; j < dLink.length; ++j) {
                                    if ((i != j) && (given.containsKey(dLink[j].getDestination()))) {
                                        if (dLink[i].getStrength() > dLink[j].getStrength()) {
                                            if (given.get(dLink[i].getDestination()).get(dLink[j].getDestination()) == 1) {
                                                c++;
                                            } else {
                                                d++;
                                            }
                                        } else if (dLink[i].getStrength() == dLink[j].getStrength()) {
                                            if (given.get(dLink[i].getDestination()).get(dLink[j].getDestination()) == 1) {
                                                c++;
                                            } else {
                                                d++;
                                            }
                                            tp++;
                                        } else {
                                            if (given.get(dLink[i].getDestination()).get(dLink[j].getDestination()) == -1) {
                                                c++;
                                            } else {
                                                d++;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        double tau = (c - d) / Math.sqrt((c + d + tr) * (c + d + tp));
                        if (!Double.isNaN(tau) && !Double.isInfinite(tau)) {
                            java.util.Properties props = new java.util.Properties();
                            props.setProperty("PropertyType", "Basic");
                            if((Boolean)parameter[6].getValue()){
                                props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "KendallTau");
                            }else{
                                props.setProperty("PropertyID", (String) parameter[4].getValue() + "KendallTau");
                            }
                            props.setProperty("PropertyClass", "java.lang.Double");
                            Property precisionProperty = PropertyFactory.newInstance().create(props);
                            precisionProperty.add(new Double(tau));
                            actor.add(precisionProperty);
                            correlationSum += tau;
                            correlationSquaredSum += tau * tau;
                        }
                    }



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
                props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "KendallTauSD");
            }else{
                props.setProperty("PropertyID", (String) parameter[4].getValue() + "KendallTauSD");                
            }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

            props.setProperty("PropertyID", (String) parameter[4].getValue() + "KendallTau");

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Kendall Tau\t" + mean);

            System.out.println("Kendall Tau SD\t" + sd);
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

        props.setProperty("Property", (String) parameter[4].getValue() + "KendallTau");

        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "KendallTauSD");

        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 2

        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + "KendallTau");

        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);



    }
}
