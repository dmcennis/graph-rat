/*
 * HITSPrestige.java
 *
 * Created on 19 October 2007, 13:46
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.prestige;

import cern.colt.matrix.DoubleFactory2D;

import cern.colt.matrix.DoubleMatrix1D;

import cern.colt.matrix.DoubleMatrix2D;

import cern.colt.matrix.linalg.EigenvalueDecomposition;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor;

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
 * Class implementing a naive implementation of Kelinberg's HITS algorithm (Kleinberg99)
 * <br>
 * <br>Kleinberg, J. 1999. "Authoritative sources in a hyperlinked environment." 
 * <i>Journal of the ACM</i>. 46(5):604--32.
 *
 * @author Daniel McEnnis
 * 
 */
public class HITSPrestige extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of HITSPrestige */
    public HITSPrestige() {
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

    /**
     * Implements the HITS algorithm by directly calculating the eigenvector matrix
     * using Colt's matrix operators.  This is O(n2) in space.  Creates a Hub and 
     * Authority score for each actor.
     * 
     */
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);
        try {
            fireChange(Scheduler.SET_ALGORITHM_COUNT,3);
            Actor[] a = null;
            a= (AlgorithmMacros.filterActor(parameter, g, mode.execute(g,null,null))).toArray(new Actor[]{});

            Arrays.sort(a);

            DoubleMatrix2D links = DoubleFactory2D.sparse.make(a.length + 1, a.length + 1);

            links.assign(0.0);

//        Link[] l = g.getLink((String)parameter[2].getValue());

//        for(int i=0;i<l.length;++i){

//            int source = java.util.Arrays.binarySearch(a,l[i].getSource());

//            links.set(source,dest,0.15);

//        }

            for (int i = 0; i < a.length; ++i) {
                LinkedList<Actor> source = new LinkedList<Actor>();
                source.add(a[i]);
                Collection<Link> linkColl = relation.execute(g, source, null, null);
                Iterator<Link> l = linkColl.iterator();
                if (l.hasNext()) {

                    while (l.hasNext()) {
                        Link link = l.next();
                        int dest = Arrays.binarySearch(a, link.getDestination());

                        links.set(i, dest, (1.0 - ((Double) parameter.get("TeleportFactor").get()).doubleValue()) / ((double) linkColl.size()));

                    }

                } else {

                    Logger.getLogger(HITSPrestige.class.getName()).log(Level.WARNING, "User " + a[i].getID() + " has no outgoing links");

                }

            }

            for (int i = 0; i < links.columns(); ++i) {

                links.set(i, a.length, ((Double) parameter.get("TeleportFactor").get()).doubleValue());

                links.set(a.length, i, 1.0 / ((double) a.length));

            }

            DoubleMatrix2D hubs = DoubleFactory2D.dense.make(a.length + 1, a.length + 1);

            hubs.assign(0.0);

            DoubleMatrix2D authorities = DoubleFactory2D.dense.make(a.length + 1, a.length + 1);

            authorities.assign(0.0);



            links.zMult(links, hubs, 1.0, 0.0, true, false);

            links.zMult(links, authorities, 1.0, 0.0, false, true);


            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);
            EigenvalueDecomposition hubsEigen = new EigenvalueDecomposition(hubs);

            DoubleMatrix1D hubEigenVector = hubsEigen.getV().viewColumn(0);

            if (hubEigenVector.get(0) < 0.0) {

                for (int i = 0; i < hubEigenVector.size(); ++i) {

                    hubEigenVector.setQuick(i, -1.0 * hubEigenVector.getQuick(i));

                }

            }

            hubsEigen = null;

            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,2);

            EigenvalueDecomposition authoritiesEigen = new EigenvalueDecomposition(authorities);

            DoubleMatrix1D authoritiesEigenVector = authoritiesEigen.getV().viewColumn(0);

            authoritiesEigen = null;

            if (authoritiesEigenVector.get(0) < 0.0) {

                for (int i = 0; i < authoritiesEigenVector.size(); ++i) {

                    authoritiesEigenVector.setQuick(i, -1.0 * authoritiesEigenVector.getQuick(i));

                }

            }



            // normalize the results to length 1

            cern.colt.matrix.linalg.Algebra base = new cern.colt.matrix.linalg.Algebra();

            double norm = base.norm2(hubEigenVector);

            for (int i = 0; i < hubEigenVector.size(); ++i) {

                hubEigenVector.set(i, hubEigenVector.get(i) / Math.sqrt(norm));

            }



            norm = base.norm2(authoritiesEigenVector);

            for (int i = 0; i < authoritiesEigenVector.size(); ++i) {

                authoritiesEigenVector.set(i, authoritiesEigenVector.get(i) / Math.sqrt(norm));

            }





            for (int i = 0; i < a.length; ++i) {
                Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Hub"),Double.class);

                property.add(new Double(hubEigenVector.get(i)));

                a[i].add(property);

                property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Authority"),Double.class);

                property.add(new Double(authoritiesEigenVector.get(i)));

                a[i].add(property);

            }

            if((Boolean)parameter.get("StoreMatrix").get()){
                Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Hub"),DoubleMatrix2D.class);
                property.add(hubsEigen.getV());
                g.add(property);
                property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Hub"),DoubleMatrix2D.class);
                property.add(authoritiesEigen.getV());
                g.add(property);
            }

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(HITSPrestige.class.getName()).log(Level.SEVERE, "Property class does not match java.lang.Double", ex);

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
     * <li>'name' - Name of this instance of the algorithm. Default is 'HITS Centrality'.
     * <li>'relation' - name of the type (relation) of link to calculate over. 
     * Default is 'Knows'.
     * <li>'actorSourceType' - type (mode) of actor to calculate over.  Default 'User'.
     * <li>'propertyNamePrefix' - name of the property prefic to use when listing 
     * algorithm output.  Deafult 'Knows HITS'.
     * </ol>
     * <br>
     * <br>Input 0 - Link
     * <br>Output 0 - Actor Property
     * <br>Output 1 - Actor Property
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
                    (String)parameter.get("DestinationProperty").get()+" Hubs",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Authorities",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Hubs",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get() +" Authorities",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public HITSPrestige prototype(){
        return new HITSPrestige();
    }
}

