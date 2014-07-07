/*
 * AddDegreeGraphProperties.java
 *
 * Created on 6 July 2007, 12:49
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.dynamicfactory.propertyQuery.Query;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.query.link.LinkByRelation;
import org.mcennis.graphrat.scheduler.Scheduler;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

/**
 * Add basic degree-based properties for actors and graphs.
 * @author Daniel McEnnis
 * 
 */
public class DegreeGraphProperties extends ModelShell implements Algorithm {

    public static final long serialVersionUID = 2;
    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

//    String relation;
//    String[] actors;
    /** Creates a new instance of AddDegreeGraphProperties */
    public DegreeGraphProperties() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Graph Properties");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Degree Graph Properties");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", Query.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, Query.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", Query.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, Query.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Relation", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Knows");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.add("User");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("PropertyPrefix", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("InDegreePropertySuffix", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("InDegree");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("OutDegreePropertySuffix", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("OutDegree");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DensityPropertySuffix", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Density");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("AbsoluteDensityPropertySuffix", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("AbsoluteDensity");
        parameter.add(name);


    }

//    public void setRelation(String r){

//        relation = r;

//    }

//

//    public String getRelation(){

//        return relation;

//    }

//

//    public String[] getActors(){

//        return actors;

//    }

//

//    public void setActors(String[] a){

//        actors = a;

//    }

//
    @Override
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode) ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String) parameter.get("Mode").get(),".*", false);

        LinkByRelation relation = (LinkByRelation) LinkQueryFactory.newInstance().create("LinkByRelation");
        relation.buildQuery((String) parameter.get("Relation").get(), false);

        calculateInDegree(g,mode,relation);

        calculateOutDegree(g,mode,relation);

        calculateDensity(g,mode,relation);

        calculateAbsoluteDensity(g,mode,relation);
    }

    /**
     * Calculate the in-degree of each node.
     * 
     * @param g the graph to be modified
     */
    public void calculateInDegree(Graph g,ActorQuery mode, LinkQuery relation) {
        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
        fireChange(Scheduler.SET_ALGORITHM_COUNT, g.getActorCount((String) parameter.get("Mode").get()));
        int i = 0;
        while (actorIt.hasNext()) {
            Actor actor = actorIt.next();
            LinkedList<Actor> source = new LinkedList<Actor>();
            source.add(actor);
            Property inDegree = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g,(String) parameter.get("PropertyPrefix").get() + (String) parameter.get("InDegreeSuffix").get()), Double.class);
            Iterator<Link> links = AlgorithmMacros.filterLink(parameter, g, relation, source, null, null);
            while (links.hasNext()) {
                try {
                    double inDegreeStrength = 0.0;
                    inDegreeStrength += links.next().getStrength();
                    inDegree.add(new Double(inDegreeStrength));
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(DegreeGraphProperties.class.getName()).log(Level.SEVERE, "Property type java.lang.Double does match type '" + inDegree.getPropertyClass().getName() + "'", ex);
                }
                actor.add(inDegree);
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i++);
            }
        }
    }

    /**
     * Calculate the out-degree of each node.
     * 
     * @param g the graph to be modified
     */
    public void calculateOutDegree(Graph g,ActorQuery mode, LinkQuery relation) {
        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
        fireChange(Scheduler.SET_ALGORITHM_COUNT, g.getActorCount((String)parameter.get("Mode").get()) * 2 + 1);

        while (actorIt.hasNext()) {
            Actor actor = actorIt.next();
            LinkedList<Actor> source = new LinkedList<Actor>();
            source.add(actor);
            Property inDegree = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g,(String) parameter.get("PropertyPrefix").get() + (String) parameter.get("OutDegreeSuffix").get()), Double.class);
            Iterator<Link> links = AlgorithmMacros.filterLink(parameter, g, relation, source, null, null);
	    while(links.hasNext()){
		try {    
		    double inDegreeStrength = 0.0;
                    inDegreeStrength += links.next().getStrength();
                    inDegree.add(new Double(inDegreeStrength));
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(DegreeGraphProperties.class.getName()).log(Level.SEVERE, "Property type java.lang.Double does match type '" + inDegree.getPropertyClass().getName() + "'", ex);
                }
                actor.add(inDegree);
//                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
            }
        }
    }

    /**
     * Calculate the density of the graph using the absolute value of all link strengths
     * 
     * @param g the graph to be modified
     */
    public void calculateAbsoluteDensity(Graph g,ActorQuery mode, LinkQuery relation) {

        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
        int actorCount=0;
        while(actorIt.hasNext()){
            actorCount++;
        }
        actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
        while (actorIt.hasNext()) {
            LinkedList<Actor> source = new LinkedList<Actor>();
            source.add(actorIt.next());

            Iterator<Link> links = AlgorithmMacros.filterLink(parameter, g, relation, source, null, null);

            if (links.hasNext()) {

                double linkStrength = 0.0;
                Property density = null;
                try {

                    while (links.hasNext()) {

                        linkStrength += Math.abs(links.next().getStrength());
                    }

                    linkStrength /= actorCount * (actorCount - 1);
                    density = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g,(String) parameter.get("DestinationProperty").get()+(String)parameter.get("AbsoluteDensityPropertySuffix").get()),Double.class);

                    density.add(new Double(linkStrength));

                    g.add(density);
                } catch (InvalidObjectTypeException ex) {
                    if (density != null) {
                        Logger.getLogger(DegreeGraphProperties.class.getName()).log(Level.SEVERE, "Property type java.lang.Double does match type '" + density.getPropertyClass().getName() + "'", ex);
                    }
                }

            }

        }

    }

    /**
     * Calculate the density of the graph
     * 
     * @param g the graph to be modified
     */
    public void calculateDensity(Graph g,ActorQuery mode, LinkQuery relation) {



        Iterator<Actor> actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
        int actorCount=0;
        while(actorIt.hasNext()){
            actorCount++;
        }
        actorIt = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
        while (actorIt.hasNext()) {
            LinkedList<Actor> source = new LinkedList<Actor>();
            source.add(actorIt.next());


            Iterator<Link> links = AlgorithmMacros.filterLink(parameter, g, relation, source, null, null);

            if (links.hasNext()) {
                try {

                    double linkStrength = 0.0;

                    while (links.hasNext()) {

                        linkStrength += links.next().getStrength();
                    }

                    linkStrength /= actorCount * (actorCount - 1);
                    Property density = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g,(String) parameter.get("DestinationProperty").get()+(String)parameter.get("DensityPropertySuffix").get()),Double.class);

                    density.add(new Double(linkStrength));

                    g.add(density);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(DegreeGraphProperties.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

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
     * Parameters for initialization of this algorithm
     * <br>
     * <ol>
     * <li>'name' - name of this isntance. Default is 'Degree Graph Properties'.
     * <li>'relation' - type (relation) of link to use.  Default is 'Knows'.
     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.
     * </ol>
     * <br>
     * <br>Input 1 - Link
     * <br>Output 1 - Actor Property
     * <br>Output 2 - Actor Property
     * <br>Output 3 - Graph Property
     * <br>Output 4 - Graph Property
     */
    public void init(Properties map) {
        if (parameter.check(map)) {
            parameter.merge(map);

            IODescriptorInternal descriptor = IODescriptorFactory.newInstance().create(
                    IODescriptor.Type.LINK,
                    (String) parameter.get("Name").getValue().get(0),
                    (String) parameter.get("Relation").get(),
                    null,
                    (String) null,
                    "");
            input.add(descriptor);

            descriptor = IODescriptorFactory.newInstance().create(
                    IODescriptor.Type.ACTOR,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("Mode").get(),
                     null,
                    (String) null,
                    "");
            input.add(descriptor);        // Construct input descriptors

            descriptor = IODescriptorFactory.newInstance().create(
                    IODescriptor.Type.ACTOR_PROPERTY,
                    (String) parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                     (Query)null,
                    (String) parameter.get("PropertyPrefix").get() + (String) parameter.get("InDegreeSuffix").get(),
                    "");
            output.add(descriptor);

            descriptor = IODescriptorFactory.newInstance().create(
                    IODescriptor.Type.ACTOR_PROPERTY,
                    (String) parameter.get("Name").get(),
                    (String) parameter.get("Mode").get(),
                     null,
                    (String) parameter.get("PropertyPrefix").get() + (String) parameter.get("OutDegreeSuffix").get(),
                    "");
            output.add(descriptor);

            descriptor = IODescriptorFactory.newInstance().create(
                    IODescriptor.Type.GRAPH_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    null,
                    (String) parameter.get("PropertyPrefix").get() + (String) parameter.get("DensitySuffix").get(),
                    "");
            output.add(descriptor);

            descriptor = IODescriptorFactory.newInstance().create(
                    IODescriptor.Type.GRAPH_PROPERTY,
                    (String) parameter.get("Name").get(),
                    null,
                    null,
                    (String) parameter.get("PropertyPrefix").get() + (String) parameter.get("AbsoluteDensitySuffix").get(),
                    "");
            output.add(descriptor);
        }
    }

    public DegreeGraphProperties prototype() {
        return new DegreeGraphProperties();
    }
}

