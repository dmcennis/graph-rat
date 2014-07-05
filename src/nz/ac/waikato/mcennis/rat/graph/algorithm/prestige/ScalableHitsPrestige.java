/*
 * HITSPrestige.java
 *
 * Created on 19 October 2007, 13:46
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;

import nz.ac.waikato.mcennis.rat.graph.algorithm.*;

import cern.colt.matrix.DoubleFactory1D;

import cern.colt.matrix.DoubleFactory2D;

import cern.colt.matrix.DoubleMatrix1D;

import cern.colt.matrix.DoubleMatrix2D;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
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

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.dynamicfactory.property.Property;

import org.dynamicfactory.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Class Implementing the HITS algoerithm by a variation of the power metric as 
 * defined in Kleinberg99.
 * <br>
 * <br>Kleinberg, J. 1999. "Authoritative sources in a hyperlinked environment." 
 * <i>Journal of the ACM</i>. 46(5):604--32.
 * 
 *
 * @author Daniel McEnnis
 * 
 */
public class ScalableHitsPrestige extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of HITSPrestige */
    public ScalableHitsPrestige() {
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

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
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

        name = ParameterFactory.newInstance().create("Tolerance", Double.class);
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
     * Implements the following algorithm:
     * <br><code>
     * Iterate(G,k)
     *  G: a collection of n linked pages
     *  k: a natural number
     *  Let z denote the vector (1, 1, 1, . . . , 1) Rn .
     *  Set x 0 : z.
     *  Set y 0 : z.
     *  For i    1, 2, . . . , k
     *     Apply the operation to (x i 1 , y i 1 ), obtaining new x-weights x i .
     *     Apply the operation to (x i , y i 1 ), obtaining new y-weights y i .
     *     Normalize x i , obtaining x i .
     *     Normalize y i , obtaining y i .
     *  End
     * Return (x k , y k ).</code>
     */
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String)parameter.get("Relation").get(),false);

        try {
            fireChange(Scheduler.SET_ALGORITHM_COUNT,3);
            Actor[] a = (AlgorithmMacros.filterActor(parameter, g, mode.execute(g,null,null))).toArray(new Actor[]{});

            java.util.Arrays.sort(a);

            DoubleMatrix2D links = DoubleFactory2D.sparse.make(a.length, a.length);

            links.assign(0.0);

//        Link[] l = g.getLink((String)parameter[2].getValue());

//        for(int i=0;i<l.length;++i){

//            int source = java.util.Arrays.binarySearch(a,l[i].getSource());

//            links.set(source,dest,0.15);

//        }

            for (int i = 0; i < a.length; ++i) {
                LinkedList<Actor> source = new LinkedList<Actor>();
                source.add(a[i]);
                Link[] l = (AlgorithmMacros.filterLink(parameter, g, relation.execute(g,source,null,null))).toArray(new Link[]{});

                if (l != null) {

                    for (int j = 0; j < l.length; ++j) {

                        int dest = java.util.Arrays.binarySearch(a, l[j].getDestination());

                        links.set(i, dest, (1.0));

                    }

                } else {

                    Logger.getLogger(ScalableHitsPrestige.class.getName()).log(Level.WARNING, "User "+a[i].getID()+" has no outgoing links");

                }

            }



            cern.colt.matrix.linalg.Algebra base = new cern.colt.matrix.linalg.Algebra();



            DoubleMatrix2D inverseLinks = base.transpose(links);



            DoubleMatrix1D hubVector = DoubleFactory1D.dense.make(a.length);

            DoubleMatrix1D oldHubVector = DoubleFactory1D.dense.make(a.length);

            hubVector.assign(Math.sqrt(1.0 / ((double) a.length)));

            oldHubVector.assign(Math.sqrt(1.0 / ((double) a.length)));



            DoubleMatrix1D authorityVector = DoubleFactory1D.dense.make(a.length);

            DoubleMatrix1D oldAuthorityVector = DoubleFactory1D.dense.make(a.length);

            authorityVector.assign(Math.sqrt(1.0 / ((double) a.length)));

            oldAuthorityVector.assign(Math.sqrt(1.0 / ((double) a.length)));



            DoubleMatrix1D temp = null;



            double tolerance = ((Double) parameter.get("Tolerance").get()).doubleValue();



            // links from authority vector to hub vector

            inverseLinks.zMult(authorityVector, hubVector);

            // links from hub vector to authority vector

            links.zMult(hubVector, authorityVector);

            // normalize hub vector

            double norm = base.norm2(hubVector);

            for (int i = 0; i < hubVector.size(); ++i) {

                hubVector.set(i, hubVector.get(i) / Math.sqrt(norm));

            }

            // normalize hub vector

            norm = base.norm2(authorityVector);

            for (int i = 0; i < authorityVector.size(); ++i) {

                authorityVector.set(i, authorityVector.get(i) / Math.sqrt(norm));

            }


            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,1);
            while ((error(oldHubVector, hubVector) > tolerance) || (error(oldAuthorityVector, authorityVector) > tolerance)) {

                //swap old and new vectors

                temp = hubVector;

                hubVector = oldHubVector;

                oldHubVector = temp;



                temp = authorityVector;

                authorityVector = oldAuthorityVector;

                oldAuthorityVector = temp;



                // links from authority vector to hub vector

                inverseLinks.zMult(authorityVector, hubVector);

                // links from hub vector to authority vector

                links.zMult(hubVector, authorityVector);

                // normalize hub vector

                norm = base.norm2(hubVector);

                for (int i = 0; i < hubVector.size(); ++i) {

                    hubVector.set(i, hubVector.get(i) / Math.sqrt(norm));

                }

                // normalize hub vector

                norm = base.norm2(authorityVector);

                for (int i = 0; i < authorityVector.size(); ++i) {

                    authorityVector.set(i, authorityVector.get(i) / Math.sqrt(norm));

                }

            }


            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,2);
            for (int i = 0; i < a.length; ++i) {
                Property property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Hubs"),Double.class);

                property.add(new Double(hubVector.get(i)));

                a[i].add(property);

                property = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Authorities"),Double.class);

                property.add(new Double(authorityVector.get(i)));

                a[i].add(property);

            }

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(ScalableHitsPrestige.class.getName()).log(Level.SEVERE, "Property class does not match java.lang.Double", ex);

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
     * 
     * Parameters to be initialized:
     * <ol>
     * <li>'name' - name of this instance of the algorithm. Default 'HITS Centrality'.
     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.
     * <li>'actorSourceType' - type (mode) of actor to calculate over. Default 'User'.
     * <li>'propertyNamePrefix' - prefix for output property names. Deafult 'Knows HITS'.
     * <li>'tolerance' - Double value to indicate the difference between 
     * iterations indicating the final version is discovered.
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

    protected double error(DoubleMatrix1D newVector, DoubleMatrix1D oldVector) {

        double ret = 0.0;

        for (int i = 0; i < newVector.size(); ++i) {

            ret += Math.abs(newVector.get(i) - oldVector.get(i));

        }

        return ret;

    }

    public ScalableHitsPrestige prototype(){
        return new ScalableHitsPrestige();
    }
}

