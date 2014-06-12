/*
 * AddDegreeCentrality.java
 *
 * Created on 27 June 2007, 18:43
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AlgorithmMacros;
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
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;

import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQuery.LinkEnd;
import nz.ac.waikato.mcennis.rat.graph.query.LinkQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByLink;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.graph.query.link.LinkByRelation;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Calculates centrality and prestige based on actor degree. see Freeman79
 * <br>
 * Freeman, L. "Centrality in social networks: I. Conceptual clarification."
 * <i>Social Networks</i>. 1:215--239.
 * <br>
 * @author Daniel McEnnis
 * 
 */
public class DegreePrestige extends ModelShell implements Algorithm {

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();
//    private String[] actorList;
    private double prestigeMaxDegree = Double.NEGATIVE_INFINITY;
    private double[] centralityValue;
    private double[] prestigeValue;
    private double centralityMaxDegree = Double.NEGATIVE_INFINITY;
    public static final long serialVersionUID = 2;
    int actorCount = 0;

    /** Creates a new instance of addDegreeCentrality */
    public DegreePrestige() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Centrality");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Centrality");
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

        name = ParameterFactory.newInstance().create("SourceAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
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
        name = ParameterFactory.newInstance().create("SourceProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Normalize", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);
    }

    @Override
    public void execute(Graph g) {

        fireChange(Scheduler.SET_ALGORITHM_COUNT,  2);
        actorCount = 0;
        calculatePrestige(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, actorCount++);
        calculateCentrality(g);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, actorCount++);

        centralityValue = null;

        prestigeValue = null;

    }

    /**
     * Calaculate In-Degree of each actor
     * 
     * @param g graph to be modified
     */
    public void calculatePrestige(Graph g) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(),".*",false);

        try {
            Iterator<Actor> userList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

            int userListSize = 0;
            while (userList.hasNext()) {
                userListSize++;
                userList.next();
            }
            prestigeValue = new double[userListSize];

            userList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            int i=0;
            while (userList.hasNext()) {

                Property inDegree = userList.next().getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get() + " InDegree"));
                if ((inDegree != null) && (inDegree.getValue().size() > 0)) {
                    double value = ((Double) inDegree.getValue().get(0)).doubleValue() / (userListSize - 1);

                    if (value > prestigeMaxDegree) {

                        prestigeMaxDegree = value;

                    }

                    prestigeValue[i++] = value;

                }


                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, actorCount++);
            }



            if (((Boolean) parameter.get("Normalize").get()).booleanValue()) {

                double norm = 0.0;

                for (i = 0; i < prestigeValue.length; ++i) {

                    norm += prestigeValue[i] * prestigeValue[i];

                }

                for (i = 0; i < prestigeValue.length; ++i) {

                    prestigeValue[i] /= Math.sqrt(norm);

                }

            }


            userList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            i=0;
            while (userList.hasNext()) {

                Property prestige = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" DegreePrestige"),Double.class);

                prestige.add(new Double(prestigeValue[i]));

                userList.next().add(prestige);

            }
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(DegreePrestige.class.getName()).log(Level.SEVERE, "Property class of " + (String) parameter.get("Mode").get() + " DegreePrestige does not match java.lang.Double", ex);
        }

    }

    /**
     * Calaculate Out-Degree of each actor
     * 
     * @param g graph to be modified
     */
    public void calculateCentrality(Graph g) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(),".*",false);

        try {
            Iterator<Actor> userList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

            int userListSize = 0;
            while (userList.hasNext()) {
                userListSize++;
                userList.next();
            }
            prestigeValue = new double[userListSize];

            userList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            int i=0;
            while (userList.hasNext()) {

                Property inDegree = userList.next().getProperty(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("SourceProperty").get() + " OutDegree"));
                if ((inDegree != null) && (inDegree.getValue().size() > 0)) {
                    double value = ((Double) inDegree.getValue().get(0)).doubleValue() / (userListSize - 1);

                    if (value > prestigeMaxDegree) {

                        prestigeMaxDegree = value;

                    }

                    prestigeValue[i++] = value;

                }


                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, actorCount++);
            }



            if (((Boolean) parameter.get("Normalize").get()).booleanValue()) {

                double norm = 0.0;

                for (i = 0; i < prestigeValue.length; ++i) {

                    norm += prestigeValue[i] * prestigeValue[i];

                }

                for (i = 0; i < prestigeValue.length; ++i) {

                    prestigeValue[i] /= Math.sqrt(norm);

                }

            }


            userList = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
            i=0;
            while (userList.hasNext()) {

                Property prestige = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" DegreeCentrality"),Double.class);

                prestige.add(new Double(prestigeValue[i]));

                userList.next().add(prestige);

            }
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(DegreePrestige.class.getName()).log(Level.SEVERE, "Property class of " + (String) parameter.get("Mode").get() + " DegreePrestige does not match java.lang.Double", ex);
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
     * Parameters for initialization
     * <br>
     * <ol>
     * <li>'name' - name of this isntance. Default is 'Degree Centrality'.
     * <li>'relation' - type (relation) of link to use.  Default is 'Knows'.
     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.
     * <li>'normalize' - should the sum of squares of all entries be normalized to 1. Default is 'false'.
     * </ol>
     * <br>
     * <br>Input 1 - Link
     * <br>Output 1 - ActorProperty
     * <br>Output 2 - ActorProperty
     * <br>Output 3 - GraphProperty
     * <br>Output 4 - GraphProperty
     * <br>Output 5 - GraphProperty
     * <br>Output 6 - GraphProperty
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
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
                    (String)parameter.get("SourceProperty").get()+" OutDegree",
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" DegreePrestige",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" DegreeCentrality",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
        }
    }

    public DegreePrestige prototype(){
        return new DegreePrestige();
    }
}

