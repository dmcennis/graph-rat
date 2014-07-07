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
import org.dynamicfactory.model.ModelShell;
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

    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    OutputDescriptorInternal[] output = new OutputDescriptorInternal[18];

    /** Creates a new instance of Evaluation */
    public PrecisionRecallFMeasure() {

        init(null);

    }

    /**
     * Calculate all the evaluation metrics against the given graph.
     * 
     * TODO: add F-measure stastic
     * 
     */
    public void execute(Graph g) {

        fireChange(Scheduler.SET_ALGORITHM_COUNT, 6);
        positivePrecision(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);
        positiveRecall(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);
        positiveFMeasure(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 3);
        negativePrecision(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 4);
        negativeRecall(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 5);
        negativeFMeasure(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 6);

    }

    protected void positivePrecision(Graph g) {
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
                    if ((Boolean) parameter[5].getValue()) {
                        dLink = g.getLinkBySource((String) parameter[2].getValue() + g.getID(), actor);
                    } else {
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
                    if (gLink != null) {
                        for (int i = 0; i < derived.size(); ++i) {
                            boolean isPresent = false;
                            for (int j = 0; j < gLink.length; ++j) {
                                if ((derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID()) && (derived.get(i).getStrength() > 0.0))) {
                                    isPresent = true;
                                }
                            }
                            if (isPresent) {
                                numerator += 1.0;
                            }
                        }
                    }
                    double precision = 0.0;
                    if ((gLink == null) && (derived.size() == 0)) {
                        precision = 1.0;
                    } else if (derived.size() > 0) {
                        precision = numerator / ((double) derived.size());
                    } else {
                        precision = 0.0;
                    }
                    java.util.Properties props = new java.util.Properties();
                    props.setProperty("PropertyType", "Basic");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "Precision");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "Precision");
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
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "PrecisionSD");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "PrecisionSD");
                    }
            props.setProperty("PropertyClass", "java.lang.Double");
            Property property = PropertyFactory.newInstance().create(props);
            property.add(new Double(sd));
            g.add(property);
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "Precision");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "Precision");
                    }
            property = PropertyFactory.newInstance().create(props);
            property.add(new Double(mean));
            g.add(property);
            System.out.println("Positive Precision\t" + mean);
            System.out.println("Positive Precision SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void positiveRecall(Graph g) {
        try {
            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());
            double recallSquaredSum = 0.0;
            double recallSum = 0.0;
            while (it.hasNext()) {

                Actor actor = it.next();
                // determine given (ground truth) links
                Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);
                    Link[] dLink = null;
                    if ((Boolean) parameter[5].getValue()) {
                        dLink = g.getLinkBySource((String) parameter[2].getValue() + g.getID(), actor);
                    } else {
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
                if (gLink != null) {
                    for (int i = 0; i < derived.size(); ++i) {
                        boolean isPresent = false;
                        for (int j = 0; j < gLink.length; ++j) {
                            if ((derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID()) && (derived.get(i).getStrength() > 0.0))) {
                                isPresent = true;
                            }
                        }
                        if (isPresent) {
                            numerator += 1.0;
                        }
                    }
                }
                double recall = 0.0;
                if ((gLink != null) && (gLink.length > 0)) {
                    recall = numerator / ((double) gLink.length);
                }else if((gLink == null)&&(derived.size()==0)){
                    recall=1.0;
                }
                java.util.Properties props = new java.util.Properties();
                props.setProperty("PropertyType", "Basic");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "Recall");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "Recall");
                    }
                props.setProperty("PropertyClass", "java.lang.Double");
                Property precisionProperty = PropertyFactory.newInstance().create(props);
                precisionProperty.add(new Double(recall));
                actor.add(precisionProperty);
                recallSum += recall;
                recallSquaredSum += recall * recall;

            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph
            double sd = ((n * recallSquaredSum) - recallSum * recallSum) / n;

            double mean = recallSum / n;

            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyType", "Basic");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "RecallSD");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "RecallSD");
                    }
            props.setProperty("PropertyClass", "java.lang.Double");
            Property property = PropertyFactory.newInstance().create(props);
            property.add(new Double(sd));
            g.add(property);
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "Recall");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "Recall");
                    }
            property = PropertyFactory.newInstance().create(props);
            property.add(new Double(mean));
            g.add(property);

            System.out.println("Positive Recall\t" + mean);
            System.out.println("Positive Recall SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void positiveFMeasure(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double fMeasureSquaredSum = 0.0;

            double fMeasureSum = 0.0;

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
                    if (gLink != null) {
                        for (int i = 0; i < derived.size(); ++i) {
                            boolean isPresent = false;
                            for (int j = 0; j < gLink.length; ++j) {
                                if ((derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID()) && (derived.get(i).getStrength() > 0.0))) {
                                    isPresent = true;
                                }
                            }
                            if (isPresent) {
                                numerator += 1.0;
                            }
                        }
                    }
                    double fMeasure = 0.0;

                    if ((derived.size() > 0) && (gLink != null) && (gLink.length > 0)) {

                        double p = numerator / ((double) derived.size());
                        double r = numerator / ((double) gLink.length);

                        if(p+r != 0.0){
                            fMeasure = (2 * p * r) / (p + r);
                        }else{
                            fMeasure=0.0;
                        }

                    }else if((gLink == null)&&(derived.size()==0)){
                        fMeasure=1.0;
                    }

                    java.util.Properties props = new java.util.Properties();
                    props.setProperty("PropertyType", "Basic");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "FMeasure");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "FMeasure");
                    }
                    props.setProperty("PropertyClass", "java.lang.Double");
                    Property precisionProperty = PropertyFactory.newInstance().create(props);
                    precisionProperty.add(new Double(fMeasure));
                    actor.add(precisionProperty);
                    fMeasureSum += fMeasure;
                    fMeasureSquaredSum += fMeasure * fMeasure;
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph
            double sd = ((n * fMeasureSquaredSum) - fMeasureSum * fMeasureSum) / n;

            double mean = fMeasureSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "FMeasureSD");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "FMeasureSD");
                    }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "FMeasure");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "FMeasure");
                    }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Positive FMeasure\t" + mean);

            System.out.println("Positive FMeasure SD\t" + sd);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void negativePrecision(Graph g) {
        try {
            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double precisionSquaredSum = 0.0;

            double precisionSum = 0.0;

            while (it.hasNext()) {

                Actor actor = it.next();

                // determine given (ground truth) links

                Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);

                // acquire derived links

                    Link[] dLink = null;
                    if ((Boolean) parameter[5].getValue()) {
                        dLink = g.getLinkBySource((String) parameter[2].getValue() + g.getID(), actor);
                    } else {
                        dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);
                    }

                // strip all non-positive links

                Vector<Link> derived = new Vector<Link>();

                if (dLink != null) {

                    for (int i = 0; i < dLink.length; ++i) {

                        if (dLink[i].getStrength() < 0) {

                            derived.add(dLink[i]);

                        }

                    }

                }

                // (# given in set)/(#total in set)

                double numerator = 0.0;

                if (gLink != null) {

                    for (int i = 0; i < derived.size(); ++i) {

                        boolean isPresent = false;

                        for (int j = 0; j < gLink.length; ++j) {

                            if ((derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID()) && (derived.get(i).getStrength() < 0.0))) {

                                isPresent = true;

                            }

                        }

                        if (isPresent) {

                            numerator += 1.0;

                        }

                    }

                }

                double precision = 0.0;

                if ((gLink == null) && (derived.size() == 0)) {
                    precision = 1.0;
                } else if (derived.size() > 0) {
                    precision = numerator / ((double) derived.size());
                } else {
                    precision = 0.0;
                }

                java.util.Properties props = new java.util.Properties();

                props.setProperty("PropertyType", "Basic");
                props.setProperty("PropertyClass", "java.lang.Double");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativePrecision");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativePrecision");
                    }

                Property precisionProperty = PropertyFactory.newInstance().create(props);

                precisionProperty.add(new Double(precision));

                actor.add(precisionProperty);

                precisionSum += precision;

                precisionSquaredSum += precision * precision;

            }



            int n = g.getActor((String) parameter[3].getValue()).length;

            // calculate mean and SD of precison for graph

            double sd = ((n * precisionSquaredSum) - precisionSum * precisionSum) / n;

            double mean = precisionSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");
            props.setProperty("PropertyClass", "java.lang.Double");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativePrecisionSD");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativePrecisionSD");
                    }

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativePrecision");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativePrecision");
                    }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Negative Precision\t" + mean);

            System.out.println("Negative Precision SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void negativeRecall(Graph g) {
        try {
            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double recallSquaredSum = 0.0;

            double recallSum = 0.0;

            while (it.hasNext()) {

                Actor actor = it.next();

                // determine given (ground truth) links

                Link[] gLink = g.getLinkBySource((String) parameter[1].getValue(), actor);

                // acquire derived links

                    Link[] dLink = null;
                    if ((Boolean) parameter[5].getValue()) {
                        dLink = g.getLinkBySource((String) parameter[2].getValue() + g.getID(), actor);
                    } else {
                        dLink = g.getLinkBySource((String) parameter[2].getValue(), actor);
                    }

                // strip all non-positive links

                Vector<Link> derived = new Vector<Link>();



                if (dLink != null) {

                    for (int i = 0; i < dLink.length; ++i) {

                        if (dLink[i].getStrength() < 0) {

                            derived.add(dLink[i]);

                        }

                    }

                }

                // (# given in set)/(#total in set)

                double numerator = 0.0;

                if (gLink != null) {

                    for (int i = 0; i < derived.size(); ++i) {

                        boolean isPresent = false;

                        for (int j = 0; j < gLink.length; ++j) {

                            if ((derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID()) && (derived.get(i).getStrength() < 0.0))) {

                                isPresent = true;

                            }

                        }

                        if (isPresent) {

                            numerator += 1.0;

                        }

                    }

                }

                double recall = 0.0;

                if ((gLink != null) && (gLink.length > 0)) {
                    recall = numerator / ((double) gLink.length);
                }else if((gLink == null)&&(derived.size()==0)){
                    recall=1.0;
                }

                java.util.Properties props = new java.util.Properties();

                props.setProperty("PropertyType", "Basic");
                props.setProperty("PropertyClass", "java.lang.Double");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativeRecall");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativeRecall");
                    }

                Property precisionProperty = PropertyFactory.newInstance().create(props);

                precisionProperty.add(new Double(recall));

                actor.add(precisionProperty);

                recallSum += recall;

                recallSquaredSum += recall * recall;

            }



            int n = g.getActorCount((String) parameter[3].getValue());

            // calculate mean and SD of precison for graph

            double sd = ((n * recallSquaredSum) - recallSum * recallSum) / n;

            double mean = recallSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");
            props.setProperty("PropertyClass", "java.lang.Double");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativeRecallSD");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativeRecallSD");
                    }

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativeRecall");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativeRecall");
                    }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Negative Recall\t" + mean);

            System.out.println("Negative Recall SD\t" + sd);

        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void negativeFMeasure(Graph g) {
        try {

            Iterator<Actor> it = g.getActorIterator((String) parameter[3].getValue());

            double fMeasureSquaredSum = 0.0;

            double fMeasureSum = 0.0;

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
                    if (gLink != null) {
                        for (int i = 0; i < derived.size(); ++i) {
                            boolean isPresent = false;
                            for (int j = 0; j < gLink.length; ++j) {
                                if ((derived.get(i).getDestination().getID().contentEquals(gLink[j].getDestination().getID())) && (derived.get(i).getStrength() < 0.0)) {
                                    isPresent = true;
                                }
                            }
                            if (isPresent) {
                                numerator += 1.0;
                            }
                        }
                    }
                    double fMeasure = 0.0;

                    if ((derived.size() > 0) && (gLink != null) && (gLink.length > 0)) {

                        double p = numerator / ((double) derived.size());
                        double r = numerator / ((double) gLink.length);

                        if(p+r != 0.0){
                            fMeasure = (2 * p * r) / (p + r);
                        }else{
                            fMeasure=0.0;
                        }

                    }

                    java.util.Properties props = new java.util.Properties();
                    props.setProperty("PropertyType", "Basic");
                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativeFMeasure");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativeFMeasure");
                    }
                    props.setProperty("PropertyClass", "java.lang.Double");
                    Property precisionProperty = PropertyFactory.newInstance().create(props);
                    precisionProperty.add(new Double(fMeasure));
                    actor.add(precisionProperty);
                    fMeasureSum += fMeasure;
                    fMeasureSquaredSum += fMeasure * fMeasure;
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(PrecisionRecallFMeasure.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



            int n = g.getActor((String) parameter[3].getValue()).length;

            // calculate mean and SD of precison for graph
            double sd = ((n * fMeasureSquaredSum) - fMeasureSum * fMeasureSum) / n;

            double mean = fMeasureSum / n;

            java.util.Properties props = new java.util.Properties();

            props.setProperty("PropertyType", "Basic");

                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativeFMeasure SD");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativeFMeasure SD");
                    }
            props.setProperty("PropertyClass", "java.lang.Double");

            Property property = PropertyFactory.newInstance().create(props);

            property.add(new Double(sd));

            g.add(property);

                    if((Boolean)parameter[6].getValue()){
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + g.getID() + "NegativeFMeasure");
                    }else{
                        props.setProperty("PropertyID", (String) parameter[4].getValue() + "NegativeFMeasure");
                    }

            property = PropertyFactory.newInstance().create(props);

            property.add(new Double(mean));

            g.add(property);

            System.out.println("Negative FMeasure\t" + mean);

            System.out.println("Negative FMeasure SD\t" + sd);
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

        props.setProperty("Property", (String) parameter[4].getValue() + " Precision");

        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " PrecisionSD");

        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);


        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " Precision");

        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);




        // output 2

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " Recall");

        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 3

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " RecallSD");

        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);


        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " Recall");

        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);




        // output 6

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativePrecision");

        output[6] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 7

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativePrecisionSD");

        output[7] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativePrecision");

        output[8] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 8

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativeRecall");

        output[9] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 9

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativeRecallSD");

        output[10] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativeRecall");

        output[11] = DescriptorFactory.newInstance().createOutputDescriptor(props);




        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " FMeasure");

        output[12] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " FMeasureSD");

        output[13] = DescriptorFactory.newInstance().createOutputDescriptor(props);




        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " FMeasure");

        output[14] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativeFMeasure");

        output[15] = DescriptorFactory.newInstance().createOutputDescriptor(props);



        // output 1

        props.setProperty("Type", "GraphProperty");

        props.remove("Relation");

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativeFMeasureSD");

        output[16] = DescriptorFactory.newInstance().createOutputDescriptor(props);


        props.setProperty("Type", "ActorProperty");

        props.setProperty("Relation", (String) parameter[3].getValue());

        props.setProperty("AlgorithmName", (String) parameter[0].getValue());

        props.setProperty("Property", (String) parameter[4].getValue() + " NegativeFMeasure");

        output[17] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
