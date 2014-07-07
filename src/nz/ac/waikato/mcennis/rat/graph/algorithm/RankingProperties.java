/*

 * Created 9-1-08

 */



package nz.ac.waikato.mcennis.rat.graph.algorithm;



import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.dynamicfactory.property.InvalidObjectTypeException;

import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;

import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptorInternal;

import org.dynamicfactory.model.ModelShell;

import nz.ac.waikato.mcennis.rat.graph.query.ActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.ActorQueryFactory;
import nz.ac.waikato.mcennis.rat.graph.query.actor.ActorByMode;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

import nz.ac.waikato.mcennis.rat.util.Duples;



/**

 *

 * Reads a Double property and creates a new property reflecting the index of the

 * actor ordered by this property.

 * 

 * @author Daniel McEnnis

 */

public class RankingProperties extends ModelShell implements Algorithm{



    PropertiesInternal parameter = PropertiesFactory.newInstance().create();
    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    public RankingProperties(){
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
    }

    

    @Override

    public void execute(Graph g) {
        ActorByMode mode = (ActorByMode)ActorQueryFactory.newInstance().create("ActorByMode");
        mode.buildQuery((String)parameter.get("Mode").get(),".*",false);

        Actor[] actor = AlgorithmMacros.filterActor(parameter, g, mode.execute(g, null, null)).toArray(new Actor[]{});

        Vector<Duples> rankingVector = new Vector<Duples>();

        if(actor != null){

            fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length);

            for(int i=0;i<actor.length;++i){

                Property rank = actor[i].getProperty(AlgorithmMacros.getSourceID(parameter, g, (String)parameter.get("SourceProperty").get()));

                if(rank != null){

                    if(rank.getPropertyClass().isAssignableFrom(Double.class)){

                        if(rank.getValue() != null){

                            Duples duples = new Duples();

                            duples.setLeft(rank.getValue().get(0));

                            duples.setRight(actor[i]);

                            rankingVector.add(duples);

                        }else{

                            Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "Actor "+actor[i].getID()+" has no value");

                        }

                    }else{

                        Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "Actor "+actor[i].getID()+"'s property of type '"+(String)parameter.get("SourceProperty").get()+"' is not of type Double");

                    }

                }else{

                    Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "Actor "+actor[i].getID()+" does not have a property of type '"+(String)parameter.get("SourceProperty").get()+"'");

                }

                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);

            }

            Collections.sort(rankingVector);

            for(int i=0;i<rankingVector.size();++i){

                try {
                    Property property = PropertyFactory.newInstance().create("BasicProperty",AlgorithmMacros.getDestID(parameter, g, (String)parameter.get("DestinationProperty").get()),Double.class);

                    property.add(new Double(i));

                    ((Actor)rankingVector.get(i).getRight()).add(property);

                } catch (InvalidObjectTypeException ex) {

                    Logger.getLogger(RankingProperties.class.getName()).log(Level.SEVERE, "Class Double does not match class of property", ex);

                }

            }



        }else{

            Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "no actors of type '"+(String)parameter.get("Mode").get()+"' exist");

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

     *  <li>name: name of this algorithm. default 'Output PageRank'.

     *  <li>actorType: mode of actors to evaluate over. default 'Paper'.

     *  <li>inputProperty: input property to read. default 'Knows PageRank Centrality'.

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
                    (String)parameter.get("SourceProperty").get(),
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

    public RankingProperties prototype(){
        return new RankingProperties();
    }

}

