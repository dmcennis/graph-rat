/*

 * Created 30/05/2008-14:58:51

 * Copyright Daniel McEnnis, see license.txt

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



import cern.colt.matrix.DoubleFactory2D;

import cern.colt.matrix.DoubleMatrix1D;

import cern.colt.matrix.DoubleMatrix2D;

import cern.colt.matrix.doublealgo.Statistic;

import cern.colt.matrix.linalg.EigenvalueDecomposition;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.descriptors.IODescriptor;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;
import org.dynamicfactory.descriptors.*;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.util.Duples;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;


/**

 * Performs principal component analysis on a colt 2D matrix describing a distance matrix. Calculates a matrix

 * for transforming a distance vector into a (usually lower) space where the basis

 * vectors describing this space are oriented for maximum variance in the original

 * distance matrix. Also calculates a set of new vectors in this new space for each actor

 * in the matrix. These vecors are also stored as a 2D matrix on the graph object.

 * Any one of these three outputs can be turned off. 

 * 

 * @author Daniel McEnnis

 */

public class PrincipalComponentsAnalysis extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public PrincipalComponentsAnalysis(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("PrincipalComponentAnalysis");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, Integer.MAX_VALUE, null, String.class);
        name.setRestrictions(syntax);
        name.add("PrincipalComponentAnalysis");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter", LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, LinkQuery.class);
        name.setRestrictions(syntax);
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

        name = ParameterFactory.newInstance().create("SaveDistanceMatrix", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SaveTranspositionMatrix", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Save1DMatrix", Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Threshold", Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        name.setRestrictions(syntax);
        name.add(0.75);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("TranspositionProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Transform To Array");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DistanceProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Transform To Array");
        parameter.add(name);


        name = ParameterFactory.newInstance().create("Mode", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("User");
        parameter.add(name);
    }


    @Override

    public void execute(Graph g) {

        Property distanceProperty = g.getProperty(AlgorithmMacros.getSourceID(parameter,g,(String) parameter.get("SourceProperty").get()));

        if (distanceProperty != null) {

            if ((distanceProperty.getValue().size()>0)) {

                DoubleMatrix2D matrix = (DoubleMatrix2D) distanceProperty.getValue().get(0);

                DoubleMatrix2D covariance = Statistic.covariance(matrix);

                EigenvalueDecomposition decomp = new EigenvalueDecomposition(covariance);

                covariance = null;

                DoubleMatrix1D values = decomp.getRealEigenvalues();

                double totalEnergy = 0.0;

                for (int i = 0; i < values.size(); ++i) {

                    if (values.get(i) > 0) {

                        totalEnergy += values.get(i);

                    }

                }

                DoubleMatrix2D eigenMatrix = decomp.getV();



                // get indeci for sorting the columns by descing eigenvalue

                Vector<Duples<Double, Integer>> valueVector = new Vector<Duples<Double, Integer>>();

                for (int i = 0; i < values.size(); ++i) {

                    Duples duple = new Duples();

                    duple.setLeft(Math.max(values.get(i), 0.0));

                    duple.setRight(i);

                    valueVector.add(duple);

                }

                Collections.sort(valueVector);

                double foundEnergy = 0.0;

                int count = 0;

                for (int i = valueVector.size() - 1; i >= 0; --i) {

                    foundEnergy += valueVector.get(i).getLeft();

                    count++;

                    if ((foundEnergy / totalEnergy) > (Double) parameter.get("Threshold").get()) {

                        break;

                    }

                }

                int[] columns = new int[count];

                count = 0;

                for (int i = valueVector.size() - 1; i >= 0; --i) {

                    columns[count++] = valueVector.get(i).getRight();

                }

                int[] rows = new int[eigenMatrix.rows()];

                for (int i = 0; i < rows.length; ++i) {

                    rows[i] = i;

                }



                valueVector = null;

                DoubleMatrix2D result = eigenMatrix.viewSelection(rows, columns);

                DoubleMatrix2D actorMatrix = DoubleFactory2D.dense.make(matrix.rows(), result.columns());

                matrix.zMult(result, actorMatrix);



                // save transposition matrici

                if ((Boolean) parameter.get("SaveTranspositionMatrix").get()) {

                    Property transposition = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("TranspositionProperty").get()),DoubleMatrix2D.class);

                    try {

                        transposition.add(result);

                        g.add(transposition);

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(PrincipalComponentsAnalysis.class.getName()).log(Level.SEVERE, "actual class DoubleMatrix2D does not match property class '"+transposition.getPropertyClass().getName()+"'", ex);

                    }

                }



                // calculate actor-vector matrici

                if ((Boolean) parameter.get("Save1DMatrix").get()) {
                    Vector<Actor> actors = new Vector<Actor>();
                    ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
                    mode.buildQuery((String)parameter.get("Mode").get(),".*",false);
                    actors.addAll(AlgorithmMacros.filterActor(parameter, g, mode.execute(g,null,null)));

                    if (actors.size()>0) {

                        Collections.sort(actors);

                        for (int i = 0; i < actors.size(); ++i) {
                            Property actorMatrixProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DistanceProperty").get()),DoubleMatrix1D.class);

                            try {

                                actorMatrixProperty.add(actorMatrix.viewRow(i));

                                actors.get(i).add(actorMatrixProperty);

                            } catch (InvalidObjectTypeException ex) {

                                Logger.getLogger(PrincipalComponentsAnalysis.class.getName()).log(Level.SEVERE, "actual class DoubleMatrix1D does not match property class '"+actorMatrixProperty.getPropertyClass().getName()+"'", ex);

                            }

                        }

                    }

                }



                if ((Boolean) parameter.get("SaveDistanceMatrix").get()) {
                    Property actorMatrixProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DistanceProperty").get()),DoubleMatrix2D.class);

                    try {

                        actorMatrixProperty.add(actorMatrix);

                        g.add(actorMatrixProperty);

                    } catch (InvalidObjectTypeException ex) {

                        Logger.getLogger(PrincipalComponentsAnalysis.class.getName()).log(Level.SEVERE, "actual class DoubleMatrix2D does not match property class '"+actorMatrixProperty.getPropertyClass().getName()+"'", ex);

                    }

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

     * Parameters:<br/>

     * <br/>

     * <ul>

     * <li/><b>name</b>: Name of this algorithm. Deafult is 'Principal Components Analysis'

     * <li/><b>distanceMatrix</b>: name of the property containing the distance matrix.

     * Default is 'Actor Distance Matrix'

     * <li/><b>actorType</b>: mode of actor from which the distance matrix is derived.

     * Default is 'Artist'

     * <li/><b>storeTransformMatrix</b>: is the transform matrix to be stored. 

     * Default is 'true'

     * <li/><b>transformMatrix</b>: property name for the transform matrix. Default

     * is 'Actor Transform Matrix'

     * <li/><b>storeActorProperty</b>: are the new vectors to be stored on each actor

     * Default is 'false'

     * <li/><b>actorProperty</b>: property name for storing vector descriptions.

     * Default is 'Actor Distance Vector'

     * <li/><b>storeVectorMatrix</b>: is the matrix representing collectively all

     * actor's vectors to be stored. Default is 'false'

     * <li/><b>vectorMatrixProperty</b>: property name for storing the new actor 

     * matrix. Deafult is 'Actor Distance Matrix'

     * </ul>

     * @param map

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);

            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("SourceProperty").get(),"",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            if((Boolean)parameter.get("SaveTranspositionMatrix").get()){
            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("TranspositionProperty").get(),"",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
            }

            if((Boolean)parameter.get("SaveDistanceMatrix").get()){
            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DistanceProperty").get(),"",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
            }

            if((Boolean)parameter.get("Save1DMatrix").get()){
            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DistanceProperty").get(),"",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);
            }
        }
    }

    public PrincipalComponentsAnalysis prototype(){
        return new PrincipalComponentsAnalysis();
    }

}

