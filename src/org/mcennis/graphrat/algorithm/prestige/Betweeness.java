/*

 * OptimizedBetweeness.java

 *

 * Created on 22 October 2007, 16:27

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */

package org.mcennis.graphrat.algorithm.prestige;

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


import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.algorithm.OptimizedPathBase;

import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;
import org.mcennis.graphrat.descriptors.IODescriptor.Type;

import org.mcennis.graphrat.path.PathNode;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;


/**

 *


 * 

 * Class built upon OptimizedPathBase that claculates Betweeness.  OptimizedPathBase

 * only records one geodesic path for each actor pair, otherwise same as the 

 * betweeness metric deefined in Freeman79.

 * <br>

 * <br>

 * Freeman, L. "Centrality in social networks: I. Conceptual clarification."

 * <i>Social Networks</i>. 1:215--239.

 * @author Daniel McEnnis
 */

public class Betweeness extends OptimizedPathBase implements Algorithm {



    double[] betweeness = null;

    double maxBetweeness = 0.0;



    /** Creates a new instance of OptimizedBetweeness */

    public Betweeness() {
        super();

        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Betweeness");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("Betweeness");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Prestige");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        super.parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        super.parameter.add(name);
    }



    protected void doCleanup(PathNode[] path, Graph g) {

        try {

            if (((Boolean) (super.getParameter()).get("Normalize").get()).booleanValue()) {

                Logger.getLogger(Betweeness.class.getName()).log(Level.INFO, "Normalizing Betweeness values");

                double norm = 0.0;

                for (int i = 0; i < betweeness.length; ++i) {

                    norm += betweeness[i] * betweeness[i];

                }

                for (int i = 0; i < betweeness.length; ++i) {

                    betweeness[i] /= Math.sqrt(norm);

                }

            }



            for (int i = 0; i < betweeness.length; ++i) {
                Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

                prop.add(new Double(betweeness[i]));

                path[i].getActor().add(prop);

                if (betweeness[i] > maxBetweeness) {

                    maxBetweeness = betweeness[i];

                }

            }

            calculateGraphBetweeness(g);

            calculateBetweenessSD(g);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(Betweeness.class.getName()).log(Level.SEVERE, "Property class of Betweeness does not match java.lang.Double", ex);

        }

    }



    protected void doAnalysis(PathNode[] path, PathNode source) {

        for (int i = 0; i < path.length; ++i) {

            PathNode traversal = path[i].getPrevious();

            while ((traversal != null) && (traversal.compareTo(source) != 0)) {

                betweeness[traversal.getId()] += 1.0;

                traversal = traversal.getPrevious();

            }

        }

    }



    protected void setSize(int size) {

        betweeness = new double[size];

        java.util.Arrays.fill(betweeness, 0.0);

    }



    protected void calculateGraphBetweeness(Graph g) {

        try {

            double between = 0.0;

            for (int i = 0; i < betweeness.length; ++i) {

                between += maxBetweeness - betweeness[i];

            }

            between *= 2;

            between /= (betweeness.length - 1) * (betweeness.length - 1) * (betweeness.length - 2);

            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Mean"),Double.class);

            prop.add(new Double(between));

            g.add(prop);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(Betweeness.class.getName()).log(Level.SEVERE, "Property class of Betweeness does not match java.lang.Double", ex);

        }

    }



    protected void calculateBetweenessSD(Graph g) {

        try {

            double squareSum = 0.0;

            double sum = 0.0;

            double sd = 0.0;

            for (int i = 0; i < betweeness.length; ++i) {

                squareSum += betweeness[i] * betweeness[i];

                sum += betweeness[i];

            }

            sd = betweeness.length * squareSum - sum * sum;

            sd /= betweeness.length;

            Property prop = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(super.parameter, g, (String)parameter.get("DestinationProperty").get()+" Standard Deviation"),Double.class);

            prop.add(new Double(sd));

            g.add(prop);

        } catch (InvalidObjectTypeException ex) {

            Logger.getLogger(Betweeness.class.getName()).log(Level.SEVERE, "Property class of BetweenessSD does not match java.lang.Double", ex);

        }



    }


    @Override
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
                    false);
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
                    (String)parameter.get("DestinationProperty").get()+" Mean",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get()+" Standard Deviation",
                    "",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

        }
    }

    @Override
    public Betweeness prototype() {
        return new Betweeness();
    }

}

