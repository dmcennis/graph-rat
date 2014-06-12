/*

 *  Created 11-1-08

 *  Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.graph.algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;


import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;

import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
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
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



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
                                Property answer = PropertyFactory.newInstance().create(AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

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

