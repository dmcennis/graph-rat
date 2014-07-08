/*

 * Created 29/05/2008-16:15:19

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

package org.mcennis.graphrat.algorithm.similarity;



import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;


import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.algorithm.Algorithm;


import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.descriptors.IODescriptor;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorFactory;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.GraphQuery;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.algorithm.reusablecores.InstanceManipulation;
import org.mcennis.graphrat.algorithm.reusablecores.datavector.DataVector;
import org.mcennis.graphrat.algorithm.reusablecores.datavector.DoubleArrayDataVector;
import org.mcennis.graphrat.algorithm.reusablecores.datavector.InstanceDataVector;
import org.mcennis.graphrat.algorithm.reusablecores.distance.DistanceFunction;
import org.mcennis.graphrat.util.Duples;
import weka.core.Instance;



/**

 * Calculates the similarity between two graphs by a given property.

 * 

 * @author Daniel McEnnis

 */

public class GraphSimilarityByProperty extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();
    /**

     * Create a new graph similarity algorithm with deafult parameters.

     */

    public GraphSimilarityByProperty() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Graph Similarity By Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Graph Similarity By Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Similarity");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("LinkFilter",LinkQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,LinkQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("ActorFilter",ActorQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,ActorQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationAppendGraphID",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("GraphQuery",GraphQuery.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,GraphQuery.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SimilarityFunction",DistanceFunction.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,DistanceFunction.class);
        name.setRestrictions(syntax);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("SourceProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Threshold",Double.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Double.class);
        name.setRestrictions(syntax);
        name.add(0.75);
        parameter.add(name);
    }



    @Override

    public void execute(Graph g) {

        Collection<Graph> coll  = ((GraphQuery)parameter.get("GraphQuery").get()).execute(g,null,null);
        Graph[] data = coll.toArray(new Graph[]{});

        if (data.length > 0) {

            DistanceFunction distance = (DistanceFunction)parameter.get("DistanceFunction").get();

            for (int i=0;i<data.length;++i) {

                Property similarityProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter,g, (String)parameter.get("DestinationProperty").get()),Duples.class);

                for (int j = 0; j < data.length; ++j) {

                    if (i != j) {

                        Property leftProperty = data[i].getProperty(AlgorithmMacros.getSourceID(parameter,g, (String)parameter.get("SourceProperty").get()));

                        Property rightProperty = data[j].getProperty(AlgorithmMacros.getSourceID(parameter,g, (String)parameter.get("SourceProperty").get()));

                        if ((leftProperty != null) && (rightProperty != null)) {

                            if ((leftProperty.getValue().size() > 0) && (rightProperty.getValue().size() > 0)) {

                                DataVector left = getDataVector(leftProperty);
                                DataVector right = getDataVector(rightProperty);


                                double similarity = distance.distance(left, right);



                                if (similarity > ((Double) parameter.get("Threshold").get()).doubleValue()) {

                                    try {

                                        Duples<String, Double> sim = new Duples<String, Double>();

                                        sim.setLeft(data[j].getID());

                                        sim.setRight(similarity);

                                        similarityProperty.add(sim);

                                    } catch (InvalidObjectTypeException ex) {

                                        Logger.getLogger(GraphSimilarityByProperty.class.getName()).log(Level.SEVERE, "Property class does not match Duples", ex);

                                    }

                                }

                            }

                        }

                    }

                }

                if (similarityProperty.getValue().size() > 0) {

                    data[i].add(similarityProperty);

                }

            }

        }

    }

    protected DataVector getDataVector(Property type){
        if(type.getPropertyClass().getName().contentEquals(DoubleArrayDataVector.class.getName())){
            return (DataVector)type.getValue().get(0);
        }
        if(type.getPropertyClass().getName().contentEquals(Instance.class.getName())){
            return new InstanceDataVector((Instance)type.getValue().get(0));
        }
        if(type.getPropertyClass().getName().contentEquals(InstanceDataVector.class.getName())){
            return (InstanceDataVector)type.getValue().get(0);
        }
        if(type.getPropertyClass().getName().contentEquals("[D")){
            return new DoubleArrayDataVector((double[])type.getValue().get(0));
        }else{
            LinkedList<Instance> inst = InstanceManipulation.propertyToInstance(type);
            return new InstanceDataVector(inst.get(0));
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
        return parameter.get("param");
    }

    /**

     * Parameters:<br/>

     * <br/>

     * <ul>

     * <li/><b>name</b>: Name of this algorithm. Deafult is 'Graph Simlarity By Property'

     * <li/><b>byChildren</b>: Should graphs be chosen by being children of the given graph.

     * Default is 'true'

     * <li/><b>pattern</b>: Regular expression describing the graph IDs to be included

     * in this calculation.  The closest-to-root graph matching this pattern is used.

     * Deafult is '.*'

     * <li/><b>distanceFunction</b>: Which distance functiuon to use. Default is 'EuclideanDistance'

     * <li/><b>sourceProperty</b>: Which property contains the double[] to use as source data. 

     * Default is 'Directed Triples Histogram'

     * <li/><b>threshold</b>: Threshold for declaring two graphs similar. Default is '0.75'

     * <li/><b>destinationProperty</b>: Property for storing graph similarity.  This is stored

     * as duples&lt;GraphID,Double&gt;. Deafult is 'Similarity'

     * </ul>

     * 

     * @param map parameters to be loaded - may be null

     */ 

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("SourceProperty").get(),"");
                if((Boolean)parameter.get("SourceAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    null,
                    null,
                    (String)parameter.get("DestinationProperty").get(),"");
                if((Boolean)parameter.get("DestinationAppendGraphID").get()){
                    desc.setAppendGraphID(true);
                }
            output.add(desc);
        }

    }

    public GraphSimilarityByProperty prototype(){
        return new GraphSimilarityByProperty();
    }

}

