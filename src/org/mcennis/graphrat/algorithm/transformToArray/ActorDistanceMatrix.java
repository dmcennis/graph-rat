/*

 * Created 30/05/2008-13:57:59

 * Copyright Daniel McEnnis, see license.txt

 */

package org.mcennis.graphrat.algorithm.transformToArray;




import cern.colt.matrix.DoubleMatrix2D;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.algorithm.AlgorithmMacros;
import org.mcennis.graphrat.reusablecores.ActorDistanceMatrixCore;

import org.mcennis.graphrat.descriptors.IODescriptorFactory;



import org.mcennis.graphrat.descriptors.IODescriptor;


import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.actor.ActorByMode;



/**

 * Class for creating a matrix representing the distances between graph nodes.

 *

 * @author Daniel McEnnis

 */

public class ActorDistanceMatrix extends ModelShell implements Algorithm {



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    ActorDistanceMatrixCore base = new ActorDistanceMatrixCore();



    /**

     * Creates an algorithm with default parameters.

     */

    public ActorDistanceMatrix(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Actor Distance Matrix");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Actor Distance Matrix");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Transform To Array");
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

        name = ParameterFactory.newInstance().create("DestinationProperty",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Transform To Array");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Store2DMatrix",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);

        name = ParameterFactory.newInstance().create("Store1DMatrix",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        parameter.add(name);
    }

    

    @Override

    public void execute(Graph g) {

        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        base.setProperties(parameter);

//        base.setMode((String) parameter[1].getValue());

//        base.setRelation((String) parameter[2].getValue());

        base.execute(g);



        if (base.getActorMatrix() != null) {

            if ((Boolean) parameter.get("Store2DMatrix").get()) {

                Property graphProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),DoubleMatrix2D.class);

                try {

                    graphProperty.add(base.getActorMatrix());

                    g.add(graphProperty);

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(ActorDistanceMatrix.class.getName()).log(Level.SEVERE, "Property class doesn't match DoubleMatrix2D", ex);

                }

            }



            if ((Boolean) parameter.get("Store1DMatrix").get()) {

                Iterator<Actor> actors = AlgorithmMacros.filterActor(parameter, g, mode, null, null);
                if (actors.hasNext()) {

                    while(actors.hasNext()){
                        Actor actor = actors.next();
                        try {

                            Property actorProperty = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),DoubleMatrix2D.class);

                            actorProperty.add(base.getActorMatrix().viewColumn(base.getColumn(actor)));

                            actor.add(actorProperty);

                        } catch (InvalidObjectTypeException ex) {

                            Logger.getLogger(ActorDistanceMatrix.class.getName()).log(Level.SEVERE, "Property class doesn't match DoubleMatrix1D", ex);

                        }

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

     * Parameter:<br/>

     * <br/>

     * <ul>

     * <li/><b>name</b>: Name of this algorithm. Default is 'Graph Triples Histogram'

     * <li/><b>mode</b>: Mode of the actors to calcualtes distances over. Default

     * is 'Artist'

     * <li/><b>relation</b>: Relation used to calculate distances from.  Default

     * is 'Similarity'

     * <li/><b>makeGraphProperty</b>: Should the 2D colt matrix be added to the graph

     * object as a property.  Deafult is 'true'

     * <li/><b>graphProperty</b>: Property ID of the graph property. Default is 

     * 'Actor Distance Matrix'

     * <li/><b>makeActorProperty</b>: Should the 1D colt matrix be added to each actor

     * as a property. Deafult is 'false'

     * <li/><b>actorProperty</b>: Property ID of the actor property. Default is 'Actor Distance Vector'

     * </ul>

     * @param map parameters to be loaded - null is permitted

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),"",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("DestinationProperty").get(),"",
                    (Boolean)parameter.get("DestinationAppendGraphID").get());
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Relation").get(),
                    null,
                    null,"");
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    null,"");
            input.add(desc);

        }
    }

    public ActorDistanceMatrix prototype(){
        return new ActorDistanceMatrix();
    }

}

