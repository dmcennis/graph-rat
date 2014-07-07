/*

 * Created 24-3-08

 * 

 * Copyright Daniel McEnnis, see license.txt

 * 

 */

package org.mcennis.graphrat.algorithm.evaluation;



import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.descriptors.IODescriptor;


import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.mcennis.graphrat.algorithm.Algorithm;

import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.AndLinkQuery;
import org.mcennis.graphrat.query.link.LinkByActor;
import org.mcennis.graphrat.query.link.LinkByRelation;
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



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    /** Creates a new instance of Evaluation */

    public HalfLife() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("HalfLife");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("HalfLife");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Evaluation");
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

        name = ParameterFactory.newInstance().create("Mode",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Direction",LinkEnd.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,LinkEnd.class);
        name.setRestrictions(syntax);
        name.add(LinkEnd.SOURCE);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceRelation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DerivedRelation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

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
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);
        Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        LinkedList<LinkQuery> derived = new LinkedList<LinkQuery>();
        LinkByRelation derivedRelation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        derivedRelation.buildQuery(AlgorithmMacros.getSourceID(parameter, g, (String)parameter.get("DerivedRelation").get()),false);
        derived.add(derivedRelation);
        LinkByActor derivedActor = (LinkByActor) LinkQueryFactory.newInstance().create("LinkByActor");
        LinkEnd end = (LinkEnd)parameter.get("LinkEnd").get();
        if(end==LinkEnd.SOURCE){
            derivedActor.buildQuery(false,mode,null,null);
        }else{
            derivedActor.buildQuery(false,null,mode,null);
        }
        derived.add(derivedActor);
        if(!parameter.get("LinkFilter").getValue().isEmpty()){
            derived.add((LinkQuery)parameter.get("LinkFilter").get());
        }
        AndLinkQuery derivedAnd = (AndLinkQuery)LinkQueryFactory.newInstance().create("AndLinkQuery");
        derivedAnd.buildQuery(derived);

        LinkedList<LinkQuery> source = new LinkedList<LinkQuery>();
        LinkByRelation sourceRelation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        sourceRelation.buildQuery((String)parameter.get("SourceRelation").get(),false);
        source.add(sourceRelation);
        LinkByActor sourceActor = (LinkByActor) LinkQueryFactory.newInstance().create("LinkByActor");
        if(end==LinkEnd.SOURCE){
            sourceActor.buildQuery(false,mode,null,null);
        }else{
            sourceActor.buildQuery(false,null,mode,null);
        }
        source.add(sourceActor);
        if(!parameter.get("LinkFilter").getValue().isEmpty()){
            source.add((LinkQuery)parameter.get("LinkFilter").get());
        }
        AndLinkQuery sourceAnd = (AndLinkQuery)LinkQueryFactory.newInstance().create("AndLinkQuery");
        sourceAnd.buildQuery(source);



        try {



            double halfLifeSquaredSum = 0.0;



            double halfLifeSum = 0.0;


            int n=0;
            while (it.hasNext()) {
                n++;
                try {

                    LinkedList<Actor> actor = new LinkedList<Actor>();
                    actor.add(it.next());
                    // determine given (ground truth) links

//                    Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);
                    Vector<Link> gLink = new Vector<Link>();
                    if((LinkEnd)parameter.get("LinkEnd").get()==LinkEnd.SOURCE){
                        gLink.addAll(sourceAnd.execute(g,actor,null,null));
                    }else{
                        gLink.addAll(sourceAnd.execute(g,null,actor,null));
                    }
                    Collections.sort(gLink);
//                    Link[] dLink = null;
                    Vector<Link> dLink = new Vector<Link>();
                    if((LinkEnd)parameter.get("LinkEnd").get()==LinkEnd.SOURCE){
                        dLink.addAll(derivedAnd.execute(g,actor,null,null));
                    }else{
                        dLink.addAll(derivedAnd.execute(g,null,actor,null));
                    }
                    Collections.sort(dLink);

                    HashMap<Actor, Double> strength = new HashMap<Actor, Double>();

                    double rA = 0.0;

                    double rAMax = 0.0;

                    double alpha = 5.0;



                    // create max value from given list and populate artist->strength map

                    if (gLink.size()>0) {

                        for (int i = gLink.size() - 1; i >= 0; i--) {

                            if(end==LinkEnd.SOURCE){
                                strength.put(gLink.get(i).getDestination(), gLink.get(i).getStrength());
                            }else{
                                strength.put(gLink.get(i).getSource(), gLink.get(i).getStrength());
                            }
                            if (gLink.get(i).getStrength() > 0) {

                                rAMax += gLink.get(i).getStrength() / (Math.pow(2.0, (gLink.size() - (i + 1)) / (alpha - 1.0)));

                            }

                        }

                    } else {

                        rAMax = Double.NaN;

                    }



                    // calculate the score for derived list

                    if (dLink.size()>0) {

                        for (int i = dLink.size() - 1; i >= 0; --i) {

                            if (strength.containsKey(dLink.get(i).getDestination())) {
                                if(end==LinkEnd.SOURCE){
                                rA += strength.get(dLink.get(i).getDestination()) / (Math.pow(2.0, (dLink.size() - (i + 1)) / (alpha - 1.0)));
                                }else{
                                rA += strength.get(dLink.get(i).getSource()) / (Math.pow(2.0, (dLink.size() - (i + 1)) / (alpha - 1.0)));
                                }
                            }

                        }

                    }



                    if (Double.isNaN(rAMax)) {

                        if ((dLink.isEmpty())) {

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
                        Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

                        precisionProperty.add(new Double(rA));

                        actor.get(0).add(precisionProperty);

                    }



                    halfLifeSum += rA;

                    halfLifeSquaredSum += rA * rA;

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(HalfLife.class.getName()).log(Level.SEVERE, null, ex);

                }

            }







            // calculate mean and SD of precison for graph

            double sd = ((n * halfLifeSquaredSum) - halfLifeSum * halfLifeSum) / n;



            double mean = halfLifeSum / n;

           Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Standard Deviation"),Double.class);
            property.add(new Double(sd));
            g.add(property);



            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean"),Double.class);

            property.add(new Double(mean));

            g.add(property);



            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Half Life\t" + mean);



            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Half Life SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

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
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("SourceRelation").get(),
                    null,
                    null,
                    "",
                    false);
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("DerivedRelation").get(),
                    null,
                    null,
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" StandardDeviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);
        }

    }

    public HalfLife prototype(){
        return new HalfLife();
    }

}

