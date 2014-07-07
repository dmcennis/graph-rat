/*

 *  Created 11-1-08

 *  Copyright Daniel McEnnis, see license.txt

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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.descriptors.IODescriptorFactory;

import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import org.mcennis.graphrat.descriptors.IODescriptor;

import org.mcennis.graphrat.descriptors.IODescriptor.Type;
import org.mcennis.graphrat.descriptors.IODescriptorInternal;

import org.dynamicfactory.model.ModelShell;

import org.mcennis.graphrat.query.ActorQuery;
import org.mcennis.graphrat.query.ActorQueryFactory;
import org.mcennis.graphrat.query.actor.ActorByMode;
import org.mcennis.graphrat.scheduler.Scheduler;



/**

 *  Algorithm for calculating the difference across properties of an actor

 * 

 * @author Daniel McEnnis

 */

public class Difference extends ModelShell implements Algorithm{

    

    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public Difference(){
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

        name = ParameterFactory.newInstance().create("LeftProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("RightProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

        name = ParameterFactory.newInstance().create("DestinationProperty", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        name.setRestrictions(syntax);
        name.add("Property");
        parameter.add(name);

    }

    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        Iterator<Actor> actor = AlgorithmMacros.filterActor(parameter, g, mode, null, null);

        if(actor.hasNext()){

        fireChange(Scheduler.SET_ALGORITHM_COUNT,g.getActorCount((String)parameter.get("Mode").get()));
            int i=0;
            while(actor.hasNext()){
                Actor a = actor.next();
                Property left = a.getProperty(AlgorithmMacros.getSourceID(parameter, g, (String)parameter.get("LeftProperty").get()));

                Property right = a.getProperty(AlgorithmMacros.getSourceID(parameter, g, (String)parameter.get("RightProperty").get()));

                if((left != null)&&(right != null)){

                    if(left.getPropertyClass().isAssignableFrom(Double.class)&&right.getPropertyClass().isAssignableFrom(Double.class)){

                        if((!left.getValue().isEmpty())&&(!right.getValue().isEmpty())){

                            try {

                                double leftValue = ((Double) (left.getValue().get(0))).doubleValue();

                                double rightValue = ((Double) (right.getValue().get(0))).doubleValue();

                                double answerValue = leftValue - rightValue;
                                Property answer = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

                                answer.add(new Double(answerValue));

                                a.add(answer);

                            } catch (InvalidObjectTypeException ex) {

                                Logger.getLogger(Difference.class.getName()).log(Level.SEVERE, "Type of object differs from what was specified in property class", ex);

                            }

                        }else{

                            if(left.getValue() == null){

                                Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Left property '"+left.getType()+"' does not contain any entries");

                            }

                           if(right.getValue() == null){

                                Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Left property '"+right.getType()+"' does not contain any entries");

                            }

                        }

                    }else{

                        if(!left.getPropertyClass().isAssignableFrom(Double.class)){

                            Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Left argument '"+left.getType()+"' is not of class Double");

                        }

                        if(!right.getPropertyClass().isAssignableFrom(Double.class)){

                            Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Left argument '"+right.getType()+"' is not of class Double");

                        }

                    }

                }else{

                    if(left == null){

                        Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Left (Positive) property '"+(String)parameter.get("LeftProperty").get()+"' is null for actor '"+a.getID()+"'");

                    }

                    if(right == null){

                        Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Right (Negative) property '"+(String)parameter.get("RightProperty").get()+"' is null for actor '"+a.getID()+"'");

                    }

                }

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);

            }

        }else{

            Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Mode "+(String)parameter.get("Mode").get()+" has no actors in graph '"+g.getID()+"'");

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

     * Initializes the algorithm using the provided properties map.  Parameters are:

     * 

     * <ul>

     *  <li>name: name of this algorithm. default 'Difference'.

     *  <li>actorType: mode of actors to evaluate over. default 'Paper'.

     *  <li>leftProperty: input property. default 'Knows PageRank Centrality'.

     *  <li>rightProperty: input property to aubtract from the leftProperty. default 'Knows PageRank Centrality'.

     *  <li>outputProperty: output property to store results. default 'Knows PageRank Ranking'.

     * </ul>

     * 

     * Input 1: ActorProperty

     * Output 1: ActorProperty

     * @param map properties to initialize this algorithm

     */

    public void init(Properties map) {
        if(parameter.check(map)){
            parameter.merge(map);
            IODescriptorInternal desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("LeftProperty").get(),
                    "",
                    (Boolean)parameter.get("SourceAppendGraphID").get());
            input.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)parameter.get("Name").get(),
                    (String)parameter.get("Mode").get(),
                    null,
                    (String)parameter.get("RightProperty").get(),
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
        }
    }

    public Difference prototype(){
        return new Difference();
    }

}

