/*

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;



import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;


import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;


import nz.ac.waikato.mcennis.rat.graph.link.Link;


import java.util.Iterator;



import java.util.LinkedList;
import java.util.List;

import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;

import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
import org.dynamicfactory.descriptors.IODescriptor;
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

 * Class for calculating a variety of evaluation metrics between Given(user->artist) (ground

 * truth) and a Derived(user->artist) (predicted) set of links.  Caculates precision, recall, and FMeasure

 * both for positive and negative evaluations and with standard deviations for both

 * metrics.

 *

 * @author Daniel McEnnis

 * 

 */

public class PrecisionRecallFMeasure extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    /** Creates a new instance of Evaluation */

    public PrecisionRecallFMeasure() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Precision Recall FMeasure");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Precision Recall FMeasure");
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


        fireChange(Scheduler.SET_ALGORITHM_COUNT, 6);

        positivePrecision(g,mode,sourceAnd,derivedAnd,end);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);

        positiveRecall(g,mode,sourceAnd,derivedAnd,end);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);

        positiveFMeasure(g,mode,sourceAnd,derivedAnd,end);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 3);

        negativePrecision(g,mode,sourceAnd,derivedAnd,end);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 4);

        negativeRecall(g,mode,sourceAnd,derivedAnd,end);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 5);

        negativeFMeasure(g,mode,sourceAnd,derivedAnd,end);

        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 6);



    }



    protected void positivePrecision(Graph g,ActorQuery mode,LinkQuery sourceAnd,LinkQuery derivedAnd,LinkEnd end) {

        try {
            
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            double precisionSquaredSum = 0.0;
            double precisionSum = 0.0;
            int n=0;
            while (it.hasNext()) {
                ++n;
                try {
                    LinkedList<Actor> s = new LinkedList<Actor>();
                    LinkedList<Actor> r = new LinkedList<Actor>();
                    s.add(it.next());
                    // determine given (ground truth) links

                    Iterator<Link> gLink = null;//g.getLinkBySource((String) parameter[1].getValue(), actor);
                    if(end == LinkEnd.SOURCE){
                        gLink = sourceAnd.executeIterator(g,s,null,null);
                    }else{
                        gLink = sourceAnd.executeIterator(g,null,s,null);
                    }

                    // acquire derived links
                    int derivedSize =0;
                    Iterator<Link> dLink = null;
                    if(end == LinkEnd.SOURCE){
                        dLink = derivedAnd.executeIterator(g,s,null,null);
                    }else{
                        dLink = derivedAnd.executeIterator(g,null,s,null);
                    }
                    while(dLink.hasNext()){
                        if(dLink.next().getStrength()>0){
                            derivedSize++;
                        }
                    }

                    double numerator = 0.0;
                    while (gLink.hasNext()) {
                        Iterator<Link> derivedIt = null;
                        Link source = gLink.next();
                        if(end == LinkEnd.SOURCE){
                            r.add(source.getDestination());
                            derivedIt=derivedAnd.executeIterator(g, s, r, null);
                        }else{
                            r.add(source.getSource());
                            derivedIt=derivedAnd.executeIterator(g, r, s, null);
                        }
                        if ((source.getStrength()>0)&&(derivedIt.hasNext())&&(derivedIt.next().getStrength()>0)) {
                            numerator+=1.0;
                        }
                        r.clear();
                    }


                    double precision = 0.0;

                    if ((derivedSize == 0)) {

                        precision = 1.0;

                    } else if (derivedSize > 0) {

                        precision = numerator / ((double) derivedSize);

                    } else {

                        precision = 0.0;

                    }

                    Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Precision"),Double.class);

                    precisionProperty.add(new Double(precision));

                    s.get(0).add(precisionProperty);

                    precisionSum += precision;

                    precisionSquaredSum += precision * precision;

                    s.clear();
                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

                }
            }


            // calculate mean and SD of precison for graph

            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;

            double mean = precisionSum / n;
            Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Precision Standard Deviation"),Double.class);

            property.add(new Double(sd));

            g.add(property);
            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Precision Mean"),Double.class);
            property.add(new Double(mean));

            g.add(property);

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Positive Precision\t" + mean);

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Positive Precision SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

        }

    }



    protected void positiveRecall(Graph g,ActorQuery mode,LinkQuery sourceAnd,LinkQuery derivedAnd, LinkEnd end) {

        try {
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            double recallSquaredSum = 0.0;
            double recallSum = 0.0;
            int n=0;
            while (it.hasNext()) {
                ++n;

                    LinkedList<Actor> s = new LinkedList<Actor>();
                    LinkedList<Actor> r = new LinkedList<Actor>();
                    s.add(it.next());
                    // determine given (ground truth) links

                    Iterator<Link> gLink = null;//g.getLinkBySource((String) parameter[1].getValue(), actor);
                    if(end == LinkEnd.SOURCE){
                        gLink = sourceAnd.executeIterator(g,s,null,null);
                    }else{
                        gLink = sourceAnd.executeIterator(g,null,s,null);
                    }


                    // acquire derived links
                    double numerator = 0.0;
                    while (gLink.hasNext()) {
                        Iterator<Link> derivedIt = null;
                        Link source = gLink.next();
                        if(end == LinkEnd.SOURCE){
                            r.add(source.getDestination());
                            derivedIt=derivedAnd.executeIterator(g, s, r, null);
                        }else{
                            r.add(source.getSource());
                            derivedIt=derivedAnd.executeIterator(g, r, s, null);
                        }
                        if ((source.getStrength()>0)&&(derivedIt.hasNext())&&(derivedIt.next().getStrength()>0)) {
                            numerator+=1.0;
                        }
                        r.clear();
                    }

                    int sourceSize =0;
                    while (gLink.hasNext()) {
                        if(gLink.next().getStrength()>0){
                            sourceSize++;
                        }
                    }


                double recall = 0.0;

                if (sourceSize>0) {

                    recall = numerator / ((double) sourceSize);

                }else{

                    recall=1.0;

                }

                Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Recall"),Double.class);

                precisionProperty.add(new Double(recall));

                s.get(0).add(precisionProperty);

                recallSum += recall;

                recallSquaredSum += recall * recall;
                s.clear();
            }

            // calculate mean and SD of precison for graph

            double sd = ((n * recallSquaredSum) - recallSum * recallSum) / n;
            double mean = recallSum / n;
            Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Recall Standard Deviation"),Double.class);

            property.add(new Double(sd));

            g.add(property);

            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Recall Mean"),Double.class);

            property.add(new Double(mean));

            g.add(property);



            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Positive Recall\t" + mean);

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Positive Recall SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

        }

    }



    protected void positiveFMeasure(Graph g,ActorQuery mode,LinkQuery sourceAnd,LinkQuery derivedAnd, LinkEnd end) {

        try {
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            double fMeasureSquaredSum = 0.0;
            double fMeasureSum = 0.0;
            int n=0;
            while (it.hasNext()) {
                try {
                    LinkedList<Actor> s = new LinkedList<Actor>();
                    LinkedList<Actor> r = new LinkedList<Actor>();
                    s.add(it.next());
                    // determine given (ground truth) links

                    Iterator<Link> gLink = null;//g.getLinkBySource((String) parameter[1].getValue(), actor);
                    if(end == LinkEnd.SOURCE){
                        gLink = sourceAnd.executeIterator(g,s,null,null);
                    }else{
                        gLink = sourceAnd.executeIterator(g,null,s,null);
                    }


                    // acquire derived links
                    Iterator<Link> dLink = null;
                    if(end == LinkEnd.SOURCE){
                        dLink = derivedAnd.executeIterator(g,s,null,null);
                    }else{
                        dLink = derivedAnd.executeIterator(g,null,s,null);
                    }

                    double numerator = 0.0;
                    int derivedSize = 0;
                    while(dLink.hasNext()){
                        derivedSize++;
                        Link derived = dLink.next();
                        Iterator<Link> derivedIt = null;
                        if(end == LinkEnd.SOURCE){
                            r.add(derived.getDestination());
                            derivedIt=sourceAnd.executeIterator(g, s, r, null);
                        }else{
                            r.add(derived.getSource());
                            derivedIt=sourceAnd.executeIterator(g, r, s, null);
                        }
                        if ((derived.getStrength()>0)&&(derivedIt.hasNext())&&(derivedIt.next().getStrength()>0)) {
                            numerator+=1.0;
                        }
                        r.clear();
                    }

                    int sourceSize =0;
                    while (gLink.hasNext()) {
                        if(gLink.next().getStrength()>0){
                            sourceSize++;
                        }
                    }

                    double fMeasure = 0.0;



                    if ((derivedSize > 0) && (sourceSize>0)) {



                        double p = numerator / ((double) derivedSize);

                        double rD = numerator / ((double) sourceSize);



                        if(p+rD != 0.0){

                            fMeasure = (2 * p * rD) / (p + rD);

                        }else{

                            fMeasure=0.0;

                        }



                    }else if((sourceSize==0)){

                        fMeasure=1.0;

                    }



                    Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" FMeasure"),Double.class);

                    precisionProperty.add(new Double(fMeasure));

                    s.get(0).add(precisionProperty);

                    fMeasureSum += fMeasure;

                    fMeasureSquaredSum += fMeasure * fMeasure;

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
            // calculate mean and SD of precison for graph

            double sd = ((n * fMeasureSquaredSum) - fMeasureSum * fMeasureSum) / n;
            double mean = fMeasureSum / n;
                    Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" FMeasure Standard Deviation"),Double.class);
            property.add(new Double(sd));
            g.add(property);
            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" FMeasure Mean"),Double.class);
            property.add(new Double(mean));
            g.add(property);
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Positive FMeasure\t" + mean);
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Positive FMeasure SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

        }



    }



    protected void negativePrecision(Graph g,ActorQuery mode,LinkQuery sourceAnd,LinkQuery derivedAnd, LinkEnd end) {

        try {
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            double precisionSquaredSum = 0.0;
            double precisionSum = 0.0;
            int n=0;
            while (it.hasNext()) {
                ++n;
                try {
                    LinkedList<Actor> s = new LinkedList<Actor>();
                    LinkedList<Actor> r = new LinkedList<Actor>();
                    s.add(it.next());
                    // determine given (ground truth) links

                    Iterator<Link> gLink = null;//g.getLinkBySource((String) parameter[1].getValue(), actor);
                    if(end == LinkEnd.SOURCE){
                        gLink = sourceAnd.executeIterator(g,s,null,null);
                    }else{
                        gLink = sourceAnd.executeIterator(g,null,s,null);
                    }

                    // acquire derived links
                    int derivedSize =0;
                    Iterator<Link> dLink = null;
                    if(end == LinkEnd.SOURCE){
                        dLink = derivedAnd.executeIterator(g,s,null,null);
                    }else{
                        dLink = derivedAnd.executeIterator(g,null,s,null);
                    }
                    while(dLink.hasNext()){
                        if(dLink.next().getStrength()<0){
                            derivedSize++;
                        }
                    }

                    double numerator = 0.0;
                    while (gLink.hasNext()) {
                        Iterator<Link> derivedIt = null;
                        Link source = gLink.next();
                        if(end == LinkEnd.SOURCE){
                            r.add(source.getDestination());
                            derivedIt=derivedAnd.executeIterator(g, s, r, null);
                        }else{
                            r.add(source.getSource());
                            derivedIt=derivedAnd.executeIterator(g, r, s, null);
                        }
                        if ((source.getStrength()<0)&&(derivedIt.hasNext())&&(derivedIt.next().getStrength()<0)) {
                            numerator+=1.0;
                        }
                        r.clear();
                    }


                    double precision = 0.0;

                    if ((derivedSize == 0)) {

                        precision = 1.0;

                    } else if (derivedSize > 0) {

                        precision = numerator / ((double) derivedSize);

                    } else {

                        precision = 0.0;

                    }

                    Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative Precision"),Double.class);

                    precisionProperty.add(new Double(precision));

                    s.get(0).add(precisionProperty);

                    precisionSum += precision;

                    precisionSquaredSum += precision * precision;

                    s.clear();
                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

                }
            }


            // calculate mean and SD of precison for graph

            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;

            double mean = precisionSum / n;
            Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative Precision Standard Deviation"),Double.class);

            property.add(new Double(sd));

            g.add(property);
            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative Precision Mean"),Double.class);
            property.add(new Double(mean));

            g.add(property);

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Negative Precision\t" + mean);

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Negative Precision SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

        }

    }



    protected void negativeRecall(Graph g,ActorQuery mode,LinkQuery sourceAnd,LinkQuery derivedAnd, LinkEnd end) {
        try {
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            double recallSquaredSum = 0.0;
            double recallSum = 0.0;
            int n=0;
            while (it.hasNext()) {
                ++n;

                    LinkedList<Actor> s = new LinkedList<Actor>();
                    LinkedList<Actor> r = new LinkedList<Actor>();
                    s.add(it.next());
                    // determine given (ground truth) links

                    Iterator<Link> gLink = null;//g.getLinkBySource((String) parameter[1].getValue(), actor);
                    if(end == LinkEnd.SOURCE){
                        gLink = sourceAnd.executeIterator(g,s,null,null);
                    }else{
                        gLink = sourceAnd.executeIterator(g,null,s,null);
                    }


                    // acquire derived links
                    double numerator = 0.0;
                    while (gLink.hasNext()) {
                        Iterator<Link> derivedIt = null;
                        Link source = gLink.next();
                        if(end == LinkEnd.SOURCE){
                            r.add(source.getDestination());
                            derivedIt=derivedAnd.executeIterator(g, s, r, null);
                        }else{
                            r.add(source.getSource());
                            derivedIt=derivedAnd.executeIterator(g, r, s, null);
                        }
                        if ((source.getStrength()<0)&&(derivedIt.hasNext())&&(derivedIt.next().getStrength()<0)) {
                            numerator+=1.0;
                        }
                        r.clear();
                    }

                    int sourceSize =0;
                    while (gLink.hasNext()) {
                        if(gLink.next().getStrength()<0){
                            sourceSize++;
                        }
                    }


                double recall = 0.0;

                if (sourceSize>0) {

                    recall = numerator / ((double) sourceSize);

                }else{

                    recall=1.0;

                }

                Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative Recall"),Double.class);

                precisionProperty.add(new Double(recall));

                s.get(0).add(precisionProperty);

                recallSum += recall;

                recallSquaredSum += recall * recall;
                s.clear();
            }

            // calculate mean and SD of precison for graph

            double sd = ((n * recallSquaredSum) - recallSum * recallSum) / n;
            double mean = recallSum / n;
            Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative Recall Standard Deviation"),Double.class);

            property.add(new Double(sd));

            g.add(property);

            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative Recall Mean"),Double.class);

            property.add(new Double(mean));

            g.add(property);



            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Negative Recall\t" + mean);

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Negative Recall SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

        }
    }



    protected void negativeFMeasure(Graph g,ActorQuery mode,LinkQuery sourceAnd,LinkQuery derivedAnd, LinkEnd end) {

        try {
            Iterator<Actor> it = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            double fMeasureSquaredSum = 0.0;
            double fMeasureSum = 0.0;
            int n=0;
            while (it.hasNext()) {
                try {
                    LinkedList<Actor> s = new LinkedList<Actor>();
                    LinkedList<Actor> r = new LinkedList<Actor>();
                    s.add(it.next());
                    // determine given (ground truth) links

                    Iterator<Link> gLink = null;//g.getLinkBySource((String) parameter[1].getValue(), actor);
                    if(end == LinkEnd.SOURCE){
                        gLink = sourceAnd.executeIterator(g,s,null,null);
                    }else{
                        gLink = sourceAnd.executeIterator(g,null,s,null);
                    }


                    // acquire derived links
                    Iterator<Link> dLink = null;
                    if(end == LinkEnd.SOURCE){
                        dLink = derivedAnd.executeIterator(g,s,null,null);
                    }else{
                        dLink = derivedAnd.executeIterator(g,null,s,null);
                    }

                    double numerator = 0.0;
                    int derivedSize = 0;
                    while(dLink.hasNext()){
                        derivedSize++;
                        Link derived = dLink.next();
                        Iterator<Link> derivedIt = null;
                        if(end == LinkEnd.SOURCE){
                            r.add(derived.getDestination());
                            derivedIt=sourceAnd.executeIterator(g, s, r, null);
                        }else{
                            r.add(derived.getSource());
                            derivedIt=sourceAnd.executeIterator(g, r, s, null);
                        }
                        if ((derived.getStrength()<0)&&(derivedIt.hasNext())&&(derivedIt.next().getStrength()<0)) {
                            numerator+=1.0;
                        }
                        r.clear();
                    }

                    int sourceSize =0;
                    while (gLink.hasNext()) {
                        if(gLink.next().getStrength()<0){
                            sourceSize++;
                        }
                    }

                    double fMeasure = 0.0;



                    if ((derivedSize > 0) && (sourceSize>0)) {



                        double p = numerator / ((double) derivedSize);

                        double rD = numerator / ((double) sourceSize);



                        if(p+rD != 0.0){

                            fMeasure = (2 * p * rD) / (p + rD);

                        }else{

                            fMeasure=0.0;

                        }



                    }else if((sourceSize==0)){

                        fMeasure=1.0;

                    }



                    Property precisionProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative FMeasure"),Double.class);

                    precisionProperty.add(new Double(fMeasure));

                    s.get(0).add(precisionProperty);

                    fMeasureSum += fMeasure;

                    fMeasureSquaredSum += fMeasure * fMeasure;

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
            // calculate mean and SD of precison for graph

            double sd = ((n * fMeasureSquaredSum) - fMeasureSum * fMeasureSum) / n;
            double mean = fMeasureSum / n;
                    Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative FMeasure Standard Deviation"),Double.class);
            property.add(new Double(sd));
            g.add(property);
            property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Negative FMeasure Mean"),Double.class);
            property.add(new Double(mean));
            g.add(property);
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Negative FMeasure\t" + mean);
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.INFO,"Negative FMeasure SD\t" + sd);
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
                    (String)parameter.get("DestinationProperty").get()+" Precision",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Precision Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Precision Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Recall",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                   (String)parameter.get("DestinationProperty").get()+" Recall Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Recall Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+ " FMeasure",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" FMeasure Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" FMeasure Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Negative Precision",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Negative Precision Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Negative Precision Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Negative Recall",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                   (String)parameter.get("DestinationProperty").get()+" Negative Recall Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Negative Recall Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+ " Negative FMeasure",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Negative FMeasure Standard Deviation",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Negative FMeasure Mean",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            output.add(desc);
        }

    }


    public PrecisionRecallFMeasure prototype(){
        return new PrecisionRecallFMeasure();
    }
}

