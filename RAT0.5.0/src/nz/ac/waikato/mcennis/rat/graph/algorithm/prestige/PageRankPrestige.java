/*
 * AddPageRankPrestige.java
 *
 * Created on 27 June 2007, 19:06
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;

import nz.ac.waikato.mcennis.rat.graph.algorithm.*;

import cern.colt.matrix.DoubleFactory2D;

import cern.colt.matrix.DoubleMatrix1D;

import cern.colt.matrix.DoubleMatrix2D;

import cern.colt.matrix.linalg.EigenvalueDecomposition;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;


import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;

import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;
import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;

import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Calcuates the PageRank of an actor using the PageRank algorithm as defined
 * in Langeville and Meyer 
 * <br>
 * <br>
 * Langeville, A., and C. Meyer. 2003. "Deeper inside PageRank". <i>Internet Mathematics</i> 1(3):335--80.
 *
 * @author Daniel McEnnis
 * 
 */
public class PageRankPrestige extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of AddPageRankPrestige */
    public PageRankPrestige() {
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

        name = ParameterFactory.newInstance().create("StoreMatrix", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
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
     * Implements the PageRank algorithm in a naive fashion - directly calculating
     * the eigenvector matrix and taking the largest eigenvector (using the Colt
     * scientific computing toolkit.)
     * 
     */
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);


        try {
            Actor[] a = (AlgorithmMacros.filterActor(parameter, g, mode.execute(g,null,null))).toArray(new Actor[]{});
            fireChange(Scheduler.SET_ALGORITHM_COUNT,2);
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

                    Logger.getLogger(PageRankPrestige.class.getName()).log(Level.WARNING, "User " + a[i].getID() + " has no outgoing links");

                }

            }

            for (int i = 0; i < links.columns(); ++i) {

                links.set(i, a.length, ((Double) parameter.get("TeleportFactor").get()).doubleValue());

                links.set(a.length, i, 1.0 / ((double) a.length));

            }
            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);
            EigenvalueDecomposition decomp = new EigenvalueDecomposition(links);

            DoubleMatrix1D eigenValues = decomp.getRealEigenvalues();

            DoubleMatrix2D eigenVectors = decomp.getV();

            double max = eigenValues.get(0);

            int index = 0;

       for(int i=1;i<eigenValues.size();++i){

           Logger.getLogger(PageRankPrestige.class.getName()).log(Level.FINER, "Vector-"+i+" Value:"+eigenValues.get(i));

           if(eigenValues.get(i)>max){

                index = i;

               max = eigenValues.get(i);
            }
        }

        Logger.getLogger(PageRankPrestige.class.getName()).log(Level.FINER, "Eigenvector "+index+" of value '"+max+"'");

            java.util.Properties props = new java.util.Properties();



            // Normalize the results

            DoubleMatrix1D pageRank = eigenVectors.viewColumn(index);



            // check if the negative of the eigenvalue was found

            double fudgeFactor = 1.0;

            if (pageRank.get(0) < 0.0) {

                fudgeFactor = -1.0;

            }

            cern.colt.matrix.linalg.Algebra base = new cern.colt.matrix.linalg.Algebra();

            double norm = base.norm2(pageRank);

            for (int i = 0; i < pageRank.size(); ++i) {

                pageRank.set(i, fudgeFactor * pageRank.get(i) / Math.sqrt(norm));

            }







            double minPageRank = pageRank.get(0);

            double maxPageRank = pageRank.get(0);

            for (int i = 0; i < pageRank.size() - 1; ++i) {

            Logger.getLogger(PageRankPrestige.class.getName()).log(Level.FINER, "pageRank - "+a[i].getID()+":"+pageRank.get(i));

                Property rank = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

                rank.add(new Double(pageRank.get(i)));

                a[i].add(rank);

//            if(eigenVector.get(i) < minPageRank){

//                minPageRank = eigenVector.get(i);

//            }

//            if(eigenVector.get(i)>maxPageRank){

//                maxPageRank = eigenVector.get(i);

//            }

            }
            if((Boolean)parameter.get("StoreMatrix").get()){
                Property property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),DoubleMatrix2D.class);
                property.add(eigenVectors);
                g.add(property);
            }

        Logger.getLogger(PageRankPrestige.class.getName()).log(Level.FINER, "MAX PageRank :"+maxPageRank);

        Logger.getLogger(PageRankPrestige.class.getName()).log(Level.FINER, "MIN PageRank :"+minPageRank);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PageRankPrestige.class.getName()).log(Level.SEVERE, "Property does not match java.lang.Double", ex);
        }

    }


    /**
     * Parameters for intializing this algorithm
     * 
     * <ol>
     * <li>'name' - name of this instance of this algorithm. Default is 'Page Rank Centrality'.
     * <li>'relation' - type (relation) of link to use to create a link matrix. Default is 'Knows'.
     * <li>'actorSourceType - type (mode) of actor to define Page Rank for. Default is 'User'.
     * <li>'propertyName' - name for the property to be created. Deafult is 'Knows PageRank Centrality'.
     * <li>'teleportationFactor' - Percent of links for a given node will link to 
     * a 'master node' equally connected to every other node. Deafult is '0.15'.
     * </ol>
     * <br>
     * <br>Input 1 - Link
     * <br>Output 1 - Actor Property
     * 
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("SourceProperty").get()+" InDegree",
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

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get(),
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public PageRankPrestige prototype(){
        return new PageRankPrestige();
    }
}

