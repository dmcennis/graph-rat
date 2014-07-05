/*
 * Created 7-5-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.similarity;

import nz.ac.waikato.mcennis.rat.graph.algorithm.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMiningItems;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import org.dynamicfactory.property.Property;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Initial algorithm for creating an ontology from a folksonomy.  Utilizes 
 * dispersion to determine terms that are specializations of another term.  Also
 * determines a set of terms that are disjoint - never used together.
 * 
 * @author Daniel McEnnis
 */
public class HierarchyByCooccurance extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[8];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[1];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[4];

    /**
     * Create a new algorithm that uses default parameters.
     */
    public HierarchyByCooccurance() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] wordBase = g.getActor((String) parameter[4].getValue());
        if (wordBase != null) {
            Logger.getLogger(HierarchyByCooccurance.class.getName()).log(Level.INFO,"Acquiring descriptions of correlations");
            // Extract the properties for analysis
            AssociativeMiningItems[][] correlations = new AssociativeMiningItems[wordBase.length][];
            for (int i = 0; i < wordBase.length; ++i) {
                Property property = wordBase[i].getProperty((String) parameter[1].getValue());
                if (property != null) {
                    Object[] value = property.getValue();
                    correlations[i] = new AssociativeMiningItems[value.length];
                    for (int j = 0; j < correlations[i].length; ++j) {
                        correlations[i][j] = (AssociativeMiningItems) value[j];
                    }
                } else {
                    correlations[i] = new AssociativeMiningItems[]{};
                }
            }

            this.fireChange(Scheduler.SET_ALGORITHM_COUNT, wordBase.length);
            // For every pair of tags, determine if a generalization or sibling 
            // relationship exists.
            for (int i = 0; i < wordBase.length; ++i) {
                Logger.getLogger(HierarchyByCooccurance.class.getName()).log(Level.INFO,"Processing tag '" + wordBase[i].getID() + "' - " + i + "/" + wordBase.length);
                double maxValue = 0.0;
                Link bestGeneralization = null;
                for (int j = 0; j < wordBase.length; ++j) {
                    if (i != j) {
                        // Does left tag have a relationship with right tag
                        double leftToRight = Double.NaN;
                        double leftDisjoint = Double.NaN;
                        for (int k = 0; k < correlations[i].length; ++k) {
                            Actor[] actors = correlations[i][k].getActors();
                            if ((actors.length == 1) && (actors[0].equals(wordBase[j]))) {
                                if (correlations[i][k].isPositive()) {
                                    leftToRight = correlations[i][k].getSignificance();
                                } else {
                                    leftDisjoint = -correlations[i][k].getSignificance();
                                }
                            }
                        }

                        // Does right tag have a relationship with left tag
                        double rightToLeft = Double.NaN;
                        double rightDisjoint = Double.NaN;
                        for (int k = 0; k < correlations[j].length; ++k) {
                            Actor[] actors = correlations[j][k].getActors();
                            if ((actors.length == 1) && (actors[0].equals(wordBase[i]))) {
                                if (correlations[j][k].isPositive()) {
                                    rightToLeft = correlations[j][k].getSignificance();
                                } else {
                                    rightDisjoint = correlations[j][k].getSignificance();
                                }
                            }
                        }

                        int relationship = relationshipType(leftToRight, rightToLeft);
                        // create links
                        switch (relationship) {
//                        case 2:
//                            Properties props = new Properties();
//                            props.setProperty("LinkClass", "Basic");
//                            props.setProperty("LinkType", (String) parameter[2].getValue());
//                            Link link = LinkFactory.newInstance().create(props);
//                            link.set(wordBase[i], 1.0, wordBase[j]);
//                            g.add(link);
//                            link = LinkFactory.newInstance().create(props);
//                            link.set(wordBase[j], 1.0, wordBase[i]);
//                            g.add(link);
//                            break;
//                        case 1:
//                            Properties props = new Properties();
//                            props.setProperty("LinkClass", "Basic");
//                            props.setProperty("LinkType", (String) parameter[5].getValue());
//                            Link link = LinkFactory.newInstance().create(props);
//                            link.set(wordBase[i], 1.0, wordBase[j]);
//                            g.add(link);
//                            props.setProperty("LinkType", (String) parameter[3].getValue());
//                            link = LinkFactory.newInstance().create(props);
//                            link.set(wordBase[j], 1.0, wordBase[i]);
//                            g.add(link);
//                            break;
                            case -1:
                                if (getMagnitude(leftToRight, rightToLeft) > maxValue) {
                                    maxValue = getMagnitude(leftToRight, rightToLeft);
                                    Properties props = new Properties();
                                    props.setProperty("LinkClass", "Basic");
                                    props.setProperty("LinkType", (String) parameter[3].getValue());
                                    bestGeneralization = LinkFactory.newInstance().create(props);
                                    bestGeneralization.set(wordBase[i], getMagnitude(leftToRight, rightToLeft), wordBase[j]);
                                }
//                            props.setProperty("LinkType", (String) parameter[5].getValue());
//                            link = LinkFactory.newInstance().create(props);
//                            link.set(wordBase[j], 1.0, wordBase[i]);
//                            g.add(link);
//                            break;
                            default:
                        }

                        if ((!Double.isNaN(leftDisjoint)) && (!Double.isNaN(rightDisjoint))) {
                            Properties props = new Properties();
                            props.setProperty("LinkClass", "Basic");
                            props.setProperty("LinkType", (String) parameter[6].getValue());
                            Link link = LinkFactory.newInstance().create(props);
                            link.set(wordBase[i], 1.0, wordBase[j]);
                            g.add(link);
                            link = LinkFactory.newInstance().create(props);
                            link.set(wordBase[j], getDisjointMagnitude(leftDisjoint,rightDisjoint), wordBase[i]);
                            g.add(link);
                        }
                    }
                }// for wordbase - j 
                if (bestGeneralization != null) {
                    g.add(bestGeneralization);
                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
            } // for word base - i
        } else {
            Logger.getLogger(HierarchyByCooccurance.class.getName()).log(Level.WARNING,"No actors of type '" + (String) parameter[4].getValue() + "' found in graph '" + g.getID() + "'");
        }
    }

    protected int relationshipType(double left, double right) {
        if ((Double.isNaN(left)) && (Double.isNaN(right))) {
            return 0;
        }
        if ((Double.isNaN(left)) && (!Double.isNaN(right))) {
            return -1;
        }
        if ((!Double.isNaN(left)) && (Double.isNaN(right))) {
            return 1;
        }
        if ((left / right) > ((Double) parameter[7].getValue()).doubleValue()) {
            return 1;
        }
        if ((right / left) > ((Double) parameter[7].getValue()).doubleValue()) {
            return -1;
        }
//        if ((left / right) <= ((Double) parameter[7].getValue()).doubleValue()) {
//            return 2;
//        }
        return 0;
    }

    double getMagnitude(double left, double right) {
        if (left != Double.NaN) {
            return right / left;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
    
    double getDisjointMagnitude(double left,double right){
        if(Double.isNaN(left)){
            return right;
        }else if(Double.isNaN(right)){
            return left;
        }else{
            return Math.min(left, right);
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
     * <li/><b>name</b>: Name of this algorithm. Default is 'Basic Interest Link'
     * <li/><b>association</b>: name of the property containing lists of association 
     * objectgs (such as created by the AssociativeMining algorithm). Deafult is 'Correlated Artist'
     * <li/><b>siblingLink</b>: relation to describe terms that are siblings
     * (are specializations of the same term). Default is 'siblingLink'
     * <li/><b>generalizationLink</b>: relation to describe a term that is a more
     * general term than the term linked to it in the same context. Deafult is 'Generalization'
     * <li/><b>actorType</b>: mode used to describes tag terms. Default is 'tag'
     * <li/><b>specializationLink</b>: relation to describe a term that is a more specific 
     * term than the tgerm linked to it in the same context. Deafult is 'Specialization'
     * <li/><b>disjointLink</b>: relation to describe two terms that do not occur
     * next to one another. Default is 'Disjoint'
     * <li/><b>significanceRatioThreshold</b>: threshold before a tag is viewed
     * to be a generalization or specialization of another term.
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
            parameter[0].setValue("Basic Interest Link");
        }

        props.setProperty("Type", "nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMiningItems");
        props.setProperty("Name", "association");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("association") != null)) {
            parameter[1].setValue(map.getProperty("association"));
        } else {
            parameter[1].setValue("Correlated Artist");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "siblingLink");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("siblingLink") != null)) {
            parameter[2].setValue(map.getProperty("siblingLink"));
        } else {
            parameter[2].setValue("Sibling");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "generalizationLink");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("generalizationLink") != null)) {
            parameter[3].setValue(map.getProperty("generalizationLink"));
        } else {
            parameter[3].setValue("Generalization");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[4].setValue(map.getProperty("actorType"));
        } else {
            parameter[4].setValue("tag");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "specializationLink");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("specializationLink") != null)) {
            parameter[5].setValue(map.getProperty("specializationLink"));
        } else {
            parameter[5].setValue("Specialization");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "disjointLink");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("disjointLink") != null)) {
            parameter[6].setValue(map.getProperty("disjointLink"));
        } else {
            parameter[6].setValue("Disjoint");
        }

        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "significanceRatioThreshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("significanceRatioThreshold") != null)) {
            parameter[7].setValue(new Double(Double.parseDouble(map.getProperty("significanceRatioThreshold"))));
        } else {
            parameter[7].setValue(new Double(2.0));
        }

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[1].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[5].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[6].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);

    }
}
