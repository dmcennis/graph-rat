/*

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;



import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;



import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.descriptors.IODescriptor;


import nz.ac.waikato.mcennis.rat.graph.link.Link;


import java.util.Iterator;



import java.util.LinkedList;
import java.util.List;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;

import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.AndLinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByActor;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
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



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    double maxRecommendation = 0.0;

    double minRecommendation = 0.0;



    /** Creates a new instance of Evaluation */

    public MeanErrorEvaluation() {
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
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

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

        fireChange(Scheduler.SET_ALGORITHM_COUNT, 3);

//        scaleRecommendations(g);

//        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);

        meanError(g,mode,sourceAnd,derivedAnd);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);

        meanSquaredError(g,mode,sourceAnd,derivedAnd);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);

        rootMeanSquaredError(g,mode,sourceAnd,derivedAnd);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 3);



    }



    protected void scaleRecommendations(Graph g, ActorQuery mode, LinkQuery source, LinkQuery derived) {

        Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        while (it.hasNext()) {

            Actor actor = it.next();

            Iterator<Link> dLink = derived.executeIterator(g,null,null,null);


                while (dLink.hasNext()) {
                    Link l = dLink.next();
                    if (maxRecommendation < l.getStrength()) {

                        maxRecommendation = l.getStrength();

                    }

                    if (minRecommendation > l.getStrength()) {

                        minRecommendation = l.getStrength();

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



    protected void meanError(Graph g, ActorQuery mode, LinkQuery source, LinkQuery derived) {

        try {


            LinkedList<Actor> a = new LinkedList<Actor>();
            LinkedList<Actor> b = new LinkedList<Actor>();
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            LinkEnd end = (LinkEnd)parameter.get("Direction").get();

            double precisionSquaredSum = 0.0;



            double precisionSum = 0.0;


            int n=0;
            while (it.hasNext()) {
                n++;
                try {



                    a.add(it.next());
                Iterator<Link> dLink = derived.executeIterator(g, null, null, null);

                int size = 0;
                double numerator=0.0;
                while(dLink.hasNext()){
                    Link linkD = dLink.next();
                    if(end==LinkEnd.SOURCE){
                       b.add(linkD.getDestination());
                    }else{
                        b.add(linkD.getSource());
                    }

                    if(linkD.getStrength()>0.0){
                        size++;
                    

                    Iterator<Link> test = null;
                    if(end==LinkEnd.SOURCE){
                        test = source.executeIterator(g, a, b, null);
                    }else{
                        test = source.executeIterator(g, b, a, null);
                    }
                    if(test.hasNext()){
                        numerator += Math.abs((linkD.getStrength()) - test.next().getStrength());
                    }else{
                        numerator += Math.abs(linkD.getStrength());
                    }
                    }
                    b.clear();
                }
                    // acquire derived links

                    double precision = 0.0;



                    if (size > 0) {



                        precision = numerator / ((double)size);

                    } else {

                        precision = 0.0;

                    }

                    Property precisionProperty = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean Error"),Double.class);
                    precisionProperty.add(new Double(precision));
                    a.get(0).add(precisionProperty);
                    a.clear();



                    precisionSum += precision;



                    precisionSquaredSum += precision * precision;

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
            // calculate mean and SD of precison for graph

            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;
            double mean = precisionSum / n;

            Property property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean Error Standard Deviation"),Double.class);
            property.add(new Double(sd));
            g.add(property);

            property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean Error Mean"),Double.class);
            property.add(new Double(mean));
            g.add(property);
            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.INFO,"Mean Error\t" + mean);
            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.INFO,"Mean Error SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.SEVERE, null, ex);

        }



    }



    protected void meanSquaredError(Graph g, ActorQuery mode, LinkQuery source, LinkQuery derived) {

        try {


            LinkedList<Actor> a = new LinkedList<Actor>();
            LinkedList<Actor> b = new LinkedList<Actor>();
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            LinkEnd end = (LinkEnd)parameter.get("Direction").get();

            double precisionSquaredSum = 0.0;



            double precisionSum = 0.0;


            int n=0;
            while (it.hasNext()) {
                n++;
                try {



                    a.add(it.next());
                Iterator<Link> dLink = derived.executeIterator(g, null, null, null);

                int size = 0;
                double numerator=0.0;
                while(dLink.hasNext()){
                    Link linkD = dLink.next();
                    if(end==LinkEnd.SOURCE){
                       b.add(linkD.getDestination());
                    }else{
                        b.add(linkD.getSource());
                    }

                    if(linkD.getStrength()>0.0){
                        size++;


                    Iterator<Link> test = null;
                    if(end==LinkEnd.SOURCE){
                        test = source.executeIterator(g, a, b, null);
                    }else{
                        test = source.executeIterator(g, b, a, null);
                    }
                    if(test.hasNext()){
                                    numerator += Math.pow((linkD.getStrength()) - test.next().getStrength(), 2);
                    }else{
                            numerator += Math.pow((linkD.getStrength()),2);
                    }
                    }
                    b.clear();
                }
                    // acquire derived links

                    double precision = 0.0;



                    if (size > 0) {



                        precision = numerator / ((double)size);

                    } else {

                        precision = 0.0;

                    }

                    Property precisionProperty = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean Squared Error"),Double.class);
                    precisionProperty.add(new Double(precision));
                    a.get(0).add(precisionProperty);
                    a.clear();



                    precisionSum += precision;



                    precisionSquaredSum += precision * precision;

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
            // calculate mean and SD of precison for graph

            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;
            double mean = precisionSum / n;

            Property property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean Squared Error Standard Deviation"),Double.class);
            property.add(new Double(sd));
            g.add(property);

            property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean Error Mean"),Double.class);
            property.add(new Double(mean));
            g.add(property);
            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.INFO,"Mean Squared Error\t" + mean);
            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.INFO,"Mean Squared Error SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.SEVERE, null, ex);

        }
    }



    protected void rootMeanSquaredError(Graph g, ActorQuery mode, LinkQuery source, LinkQuery derived) {

        try {


            LinkedList<Actor> a = new LinkedList<Actor>();
            LinkedList<Actor> b = new LinkedList<Actor>();
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            LinkEnd end = (LinkEnd)parameter.get("Direction").get();

            double precisionSquaredSum = 0.0;



            double precisionSum = 0.0;


            int n=0;
            while (it.hasNext()) {
                n++;
                try {



                    a.add(it.next());
                Iterator<Link> dLink = derived.executeIterator(g, null, null, null);

                int size = 0;
                double numerator=0.0;
                while(dLink.hasNext()){
                    Link linkD = dLink.next();
                    if(end==LinkEnd.SOURCE){
                       b.add(linkD.getDestination());
                    }else{
                        b.add(linkD.getSource());
                    }

                    if(linkD.getStrength()>0.0){
                        size++;


                    Iterator<Link> test = null;
                    if(end==LinkEnd.SOURCE){
                        test = source.executeIterator(g, a, b, null);
                    }else{
                        test = source.executeIterator(g, b, a, null);
                    }
                    if(test.hasNext()){
                                    numerator += Math.pow((linkD.getStrength()) - test.next().getStrength(), 2);
                    }else{
                            numerator += Math.pow(linkD.getStrength(), 2);
                    }
                    }
                    b.clear();
                }
                    // acquire derived links

                    double precision = 0.0;



                    if (size > 0) {



                        precision = Math.sqrt(numerator / ((double)size));

                    } else {

                        precision = 0.0;

                    }

                    Property precisionProperty = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Root Mean Squared Error"),Double.class);
                    precisionProperty.add(new Double(precision));
                    a.get(0).add(precisionProperty);
                    a.clear();



                    precisionSum += precision;



                    precisionSquaredSum += precision * precision;

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
            // calculate mean and SD of precison for graph

            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;
            double mean = precisionSum / n;

            Property property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Root Mean Squared Error Standard Deviation"),Double.class);
            property.add(new Double(sd));
            g.add(property);

            property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Root Mean Error Mean"),Double.class);
            property.add(new Double(mean));
            g.add(property);
            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.INFO,"Mean Squared Error\t" + mean);
            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.INFO,"Mean Squared Error SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(MeanErrorEvaluation.class.getName()).log(Level.SEVERE, null, ex);

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
                    (String)parameter.get("DestinationProperty").get()+" Mean Error",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean Error Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean Error Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean Squared Error",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                   (String)parameter.get("DestinationProperty").get()+" Mean Squared Error Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean Squared Error Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+ " Mean Root Squared Error",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean Root Squared Error Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Mean Root Squared Error Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);
        }

    }

    public MeanErrorEvaluation prototype(){
        return new MeanErrorEvaluation();
    }
}

