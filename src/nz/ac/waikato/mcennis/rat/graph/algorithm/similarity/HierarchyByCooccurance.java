/*

 * Created 7-5-08

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.similarity;



import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.algorithm.*;
import java.util.Vector;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMiningItems;


import org.dynamicfactory.descriptors.IODescriptor;


import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.descriptors.PropertiesInternal;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import org.dynamicfactory.property.Property;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



/**

 * Initial algorithm for creating an ontology from a folksonomy.  Utilizes 

 * dispersion to determine terms that are specializations of another term.  Also

 * determines a set of terms that are disjoint - never used together.

 * 

 * @author Daniel McEnnis

 */

public class HierarchyByCooccurance extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    /**

     * Create a new algorithm that uses default parameters.

     */

    public HierarchyByCooccurance() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Similarity By Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Similarity By Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Similarity");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("AssociativeMiningItemsID",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Generalization",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Disjoint",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Threshold",Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Double.class);
        name.setRestrictions(syntax);
        name.add(0.75);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
    }



    @Override

    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Vector<Actor> wordBase = new Vector<Actor>();
        wordBase.addAll(AlgorithmMacros.filterActor(parameter, g, mode.execute(g, null, null)));

        if (!wordBase.isEmpty()) {

            Logger.getLogger(HierarchyByCooccurance.class.getName()).log(Level.INFO,"Acquiring descriptions of correlations");

            // Extract the properties for analysis

            AssociativeMiningItems[][] correlations = new AssociativeMiningItems[wordBase.size()][];

            for (int i = 0; i < wordBase.size(); ++i) {

                Property property = wordBase.get(i).getProperty(AlgorithmMacros.getSourceID(parameter,g,(String)parameter.get("AssociativeMiningItemsID").get()));

                if (property != null) {

                    Iterator value = property.getValue().iterator();

                    correlations[i] = new AssociativeMiningItems[property.getValue().size()];

                    for (int j = 0; j < correlations[i].length; ++j) {

                        correlations[i][j] = (AssociativeMiningItems) value.next();

                    }

                } else {

                    correlations[i] = new AssociativeMiningItems[]{};

                }

            }



            this.fireChange(Scheduler.SET_ALGORITHM_COUNT, wordBase.size());

            // For every pair of tags, determine if a generalization or sibling 

            // relationship exists.

            for (int i = 0; i < wordBase.size(); ++i) {

                Logger.getLogger(HierarchyByCooccurance.class.getName()).log(Level.INFO,"Processing actor '" + wordBase.get(i).getID() + "' - " + i + "/" + wordBase.size());

                double maxValue = 0.0;

                Link bestGeneralization = null;

                for (int j = 0; j < wordBase.size(); ++j) {

                    if (i != j) {

                        // Does left tag have a relationship with right tag

                        double leftToRight = Double.NaN;

                        double leftDisjoint = Double.NaN;

                        for (int k = 0; k < correlations[i].length; ++k) {

                            Actor[] actors = correlations[i][k].getActors();

                            if ((actors.length == 1) && (actors[0].equals(wordBase.get(j)))) {

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

                            if ((actors.length == 1) && (actors[0].equals(wordBase.get(i)))) {

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
                                    bestGeneralization = LinkFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("Generalization").get()));
                                    bestGeneralization.set(wordBase.get(i), getMagnitude(leftToRight, rightToLeft), wordBase.get(j));
                                }

//                            props.setProperty("LinkType", (String) parameter[5].getValue());

//                            link = LinkFactory.newInstance().create(props);

//                            link.set(wordBase[j], 1.0, wordBase[i]);

//                            g.add(link);

//                            break;

                            default:

                        }



                        if ((!Double.isNaN(leftDisjoint)) && (!Double.isNaN(rightDisjoint))) {
                            Link link = LinkFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("Disjoint").get()));

                            link.set(wordBase.get(i), 1.0, wordBase.get(j));

                            g.add(link);

                            link = LinkFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("Disjoint").get()));

                            link.set(wordBase.get(j), getDisjointMagnitude(leftDisjoint,rightDisjoint), wordBase.get(i));

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

            Logger.getLogger(HierarchyByCooccurance.class.getName()).log(Level.WARNING,"No actors of type '" + (String) parameter.get("Mode").get() + "' found in graph '" + g.getID() + "'");

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

        if ((left / right) > ((Double) parameter.get("Threshold").get()).doubleValue()) {

            return 1;

        }

        if ((right / left) > ((Double) parameter.get("Threshold").get()).doubleValue()) {

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

    public List<IODescriptor> getInputType() {

        return input;

    }



    @Override

     public List<IODescriptor> getOutputType() {

        return output;

    }



    @Override

     public Properties getParameter() {

        return parameter;

    }



    @Override

     public Parameter getParameter(String param) {
        return parameter.get(param);
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
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("AssociativeMiningItemsID").get(),
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Generalization").get(),
                    null,
                    null,
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Disjoint").get(),
                    null,
                    null,
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public HierarchyByCooccurance prototype(){
        return new HierarchyByCooccurance();
    }
}

