/*
 * AddPageRankPrestige.java
 *
 * Created on 27 June 2007, 19:06
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.prestige;

import cern.colt.matrix.DoubleFactory1D;

import cern.colt.matrix.DoubleFactory2D;

import cern.colt.matrix.DoubleMatrix1D;

import cern.colt.matrix.DoubleMatrix2D;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;

import org.mcennis.graphrat.link.Link;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.mcennis.graphrat.scheduler.Scheduler;

/**
 * Class for calculating the PageRank of an algorithm using the Power method.  
 * Algorithm is implemented using the Colt library as defined in Langville and Meyer 2003.
 * <br>
 * <br>
 * Langeville, A., C. Meyer. 2003. "Deeper inside Page Rank." <i>Internet Mathematics</i>.
 * 1(3):335--380.
 *
 * @author Daniel McEnnis
 * 
 */
public class ScalablePageRankPrestige extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of AddPageRankPrestige */
    public ScalablePageRankPrestige() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("HITS Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("HITS Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Prestige");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("TeleportFactor", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.15);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Normalize", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);
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
     * 
     * Implements the power version of the Page Rank algorithm using the colt library.
     * The algorithm terminates after sum of changes between iterations are less than the
     * tolerance. 
     */
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);


        try {
            Actor[] a = (AlgorithmMacros.filterActor(parameter, g, mode.execute(g,null,null))).toArray(new Actor[]{});
            fireChange(Scheduler.SET_ALGORITHM_COUNT, 3);
            if (a != null) {
                java.util.Arrays.sort(a);
                cern.colt.matrix.DoubleMatrix2D links = DoubleFactory2D.sparse.make(a.length + 1, a.length + 1);
                links.assign(0.0);
//        Link[] l = g.getLink((String)parameter[2].getValue());
//        for(int i=0;i<l.length;++i){
//            int source = java.util.Arrays.binarySearch(a,l[i].getSource());
//            links.set(source,dest,0.15);
//        }
                for (int i = 0; i < a.length; ++i) {
                LinkedList<Actor> source = new LinkedList<Actor>();
                source.add(a[i]);

                Link[] l = (AlgorithmMacros.filterLink(parameter, g, relation.execute(g, source, null, null))).toArray(new Link[]{});
                    if (l != null) {
                        for (int j = 0; j < l.length; ++j) {
                            int dest = java.util.Arrays.binarySearch(a, l[j].getDestination());
                            links.set(i, dest, (1.0 - ((Double) parameter.get("TeleportFactor").get()).doubleValue()) / ((double) l.length));
                        }
                    } else {
                        Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.WARNING, "User " + a[i].getID() + " has no outgoing links");
                    }
                }
                for (int i = 0; i < links.columns(); ++i) {
                    links.set(i, a.length, ((Double) parameter.get("TeleportFactor").get()).doubleValue());
                    links.set(a.length, i, 1.0 / ((double) a.length));

                }



                DoubleMatrix2D eigenVector = DoubleFactory2D.dense.make(1, a.length + 1);

                DoubleMatrix2D oldEigenVector = DoubleFactory2D.dense.make(1, a.length + 1);

                DoubleMatrix2D temp = null;

                eigenVector.assign(1.0);

                oldEigenVector.assign(1.0);





                double tolerance = ((Double) parameter.get("Tolerance").get()).doubleValue();

                oldEigenVector.zMult(links, eigenVector);

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);
                while (error(eigenVector, oldEigenVector) > tolerance) {

                    temp = oldEigenVector;

                    oldEigenVector = eigenVector;

                    eigenVector = temp;

                    oldEigenVector.zMult(links, eigenVector);

                }

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);

                DoubleMatrix1D pageRank = DoubleFactory1D.dense.make(a.length);

                for (int i = 0; i < pageRank.size(); ++i) {

                    pageRank.set(i, eigenVector.get(0, i));

                }



                cern.colt.matrix.linalg.Algebra base = new cern.colt.matrix.linalg.Algebra();

                double norm = base.norm2(pageRank);

                for (int i = 0; i < pageRank.size(); ++i) {

                    pageRank.set(i, pageRank.get(i) / Math.sqrt(norm));

                }



                double minPageRank = Double.POSITIVE_INFINITY;
                double maxPageRank = Double.NEGATIVE_INFINITY;
                for (int i = 0; i < eigenVector.size() - 1; ++i) {

                    Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, "pageRank - " + a[i].getID() + ":" + pageRank.get(i));

                Property rank = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

                    rank.add(new Double(pageRank.get(i)));
                    Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, Double.toString(pageRank.get(i)));

                    a[i].add(rank);

                    if (pageRank.get(i) < minPageRank) {
                        minPageRank = pageRank.get(i);
                    }
                    if (pageRank.get(i) > maxPageRank) {
                        maxPageRank = pageRank.get(i);
                    }

                }

                Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, "MAX PageRank :" + maxPageRank);

                Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, "MIN PageRank :" + minPageRank);
            } else {
                Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.WARNING, "No actors found of type '" + parameter.get("Mode").get() + "'");
            }
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.SEVERE, "Property class does not match java.lang.Double", ex);
        }
    }

    /**
     * 
     * Parameters to be initialized:
     * 
     * <ol>
     * <li>'name' - Name for this instance of the algorithm. Default is 'Page Rank Centrality'.
     * <li>'relation' - type (relation) of link that is calculated over. Deafult 'Knows'.
     * <li>'actorSourceType' - type (mode) of actor that is calculated over. Default 'User'.
     * <li>'propertyName' - name for the property added to actors. Deafult 'Knows PageRank Centrality'.
     * <li>'teleportationFactor' - double for the percent chance of moving to a 
     * random page. Deafult '0.15'.
     * <li>'tolerance' - maximum difference between sum of error before algorithm 
     * is considered converged.  Default is '1e-50'.
     * </ol>
     * <br>
     * <br>Input 0 - Link
     * <br>Output 0 - ActorProperty
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
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
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

        }
    }

    protected double error(DoubleMatrix2D newVector, DoubleMatrix2D oldVector) {

        double ret = 0.0;

        for (int i = 0; i < newVector.size(); ++i) {

            ret += Math.abs(newVector.get(0, i) - oldVector.get(0, i));

        }

        return ret;

    }

    public ScalablePageRankPrestige prototype(){
        return new ScalablePageRankPrestige();
    }
}

