/*
 * AddGeodesicProperties.java
 *
 * Created on 6 July 2007, 13:54
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mcennis.graphrat.algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.path.PathSet;

import org.mcennis.graphrat.path.Path;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.scheduler.Scheduler;

/**
 *
 * Calculates properties of a node:
 * 
 * (In/Out) Eccentricity - Wasserman and Faust p111 - modified for directional links
 * <br>
 * Diameter - Wasserman and Faust p111. Modified to be longest non-infinite 
 * geodesic path, eliminating infinite diameters.
 * <br>
 * Wasserman, S., and K. Faust. 1997. <i>Social Network Analysis</i>. New York: Cambridge University Press.
 * @author Daniel McEnnis
 *
 * FIXME: Only uses existance of the link - does not use the link's strength.
 */
public class GeodesicProperties extends ModelShell implements Algorithm {

    public final static long serialVersionUID = 2;
    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    /** Creates a new instance of AddGeodesicProperties */
    public GeodesicProperties() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Basic Geodesic Paths");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Basic Geodesic Paths");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter", ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("PathSetName", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("tag");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);
    }

    /**
     * Calculates properties of a node:
     * 
     * (In/Out) Eccentricity - Wasserman and Faust p111 - modified for directional links
     * <br>
     * Diameter - Wasserman and Faust p111. Modified to be longest non-infinite 
     * geodesic path, eliminating infinite diameters.
     * <br>
     * Wasserman, S., and K. Faust. 1997. <i>Social Network Analysis</i>. New York: Cambridge University Press.
     * 
     */
    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);
        fireChange(Scheduler.SET_ALGORITHM_COUNT, 3);
        addInEccentricity(g,mode);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);
        addOutEccentricity(g,mode);
        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);
        addDiameter(g);

    }

    protected void addInEccentricity(Graph g,ActorQuery mode) {

        PathSet pathSet = g.getPathSet(AlgorithmMacros.getSourceID(parameter,g,(String)parameter.get("PathSetName").get()));

        java.util.Iterator<Actor> list = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        while (list.hasNext()) {
            Property eccentricity = null;
            try {

                Actor userList = list.next();

                Path[] paths = pathSet.getPathByDestination(userList.getID());

                double cost = Double.POSITIVE_INFINITY;

                if (paths != null) {

                    cost = Double.NEGATIVE_INFINITY;

                    for (int j = 0; j < paths.length; ++j) {

                        if (paths[j].getCost() > cost) {

                            cost = paths[j].getCost();
                        }
                    }
                }

                eccentricity = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" In"),Double.class);

                eccentricity.add(new Double(cost));

                userList.add(eccentricity);
            } catch (InvalidObjectTypeException ex) {
                if (eccentricity != null) {
                    Logger.getLogger(GeodesicProperties.class.getName()).log(Level.SEVERE, "Property type java.lang.Double does match type '" + eccentricity.getPropertyClass().getName() + "'", ex);
                }
            }

        }

    }

    protected void addOutEccentricity(Graph g,ActorQuery mode) {

        PathSet pathSet = g.getPathSet(AlgorithmMacros.getSourceID(parameter, g, (String)parameter.get("PathSetName").get()));

        java.util.Iterator<Actor> list = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        while (list.hasNext()) {
            try {

                Actor userList = list.next();

                Path[] paths = pathSet.getPathBySource(userList.getID());

                double cost = Double.POSITIVE_INFINITY;

                if (paths != null) {

                    cost = Double.NEGATIVE_INFINITY;

                    for (int j = 0; j < paths.length; ++j) {

                        if (paths[j].getCost() > cost) {

                            cost = paths[j].getCost();
                        }
                    }
                }

                Property eccentricity = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()+" Out"),Double.class);

                eccentricity.add(new Double(cost));

                userList.add(eccentricity);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(GeodesicProperties.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Diamter defined as the longest geodesic path.  Avoids problems of infinite
     * or undefined diamters if some actors are poorly connected.
     * @param g graph to be modified
     */
    protected void addDiameter(Graph g) {
        try {

            double diameter = Double.NEGATIVE_INFINITY;

            PathSet pathSet = g.getPathSet(AlgorithmMacros.getSourceID(parameter, g, (String) parameter.get("PathSetName").get()));

            Path[] paths = pathSet.getPath();
            for (int i = 0; i < paths.length; ++i) {

                if (paths[i].getCost() > diameter) {

                    diameter = paths[i].getCost();
                }
            }

            Property diameterProp = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

            diameterProp.add(new Double(diameter));

            g.add(diameterProp);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(GeodesicProperties.class.getName()).log(Level.SEVERE, null, ex);
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
     * <li>'name' - name of this isntance. Default is 'Geodesic Properties'.
     * <li>'relation' - type (relation) of link to use.  Default is 'Knows'.
     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.
     * </ol>
     * <br>
     * <br>Input 1 - Path
     * <br>Output 1 - Actor Property
     * <br>Output 2 - Actor Property
     * <br>Output 3 - Graph Property
     */
    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" In",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Out",
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

            desc = IODescriptorFactory.newInstance().create(
                    Type.PATHSET,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("PathSetName").get(),
                    null,
                    null,
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);
        }
    }

    public GeodesicProperties prototype(){
        return new GeodesicProperties();
    }
}

