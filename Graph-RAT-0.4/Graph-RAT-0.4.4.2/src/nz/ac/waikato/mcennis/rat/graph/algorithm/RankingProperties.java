/*
 * Created 9-1-08
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;

import java.util.Collections;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
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

    ParameterInternal[] parameter = new ParameterInternal[4];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[1];
    
    public RankingProperties(){
        init(null);
    }
    
    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String)parameter[1].getValue());
        Vector<Duples> rankingVector = new Vector<Duples>();
        if(actor != null){
            fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length);
            for(int i=0;i<actor.length;++i){
                Property rank = actor[i].getProperty((String)parameter[2].getValue());
                if(rank != null){
                    if(rank.getPropertyClass().isAssignableFrom(Double.class)){
                        if(rank.getValue() != null){
                            Duples duples = new Duples();
                            duples.setLeft(rank.getValue()[0]);
                            duples.setRight(actor[i]);
                            rankingVector.add(duples);
                        }else{
                            Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "Actor "+actor[i].getID()+" has no value");
                        }
                    }else{
                        Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "Actor "+actor[i].getID()+"'s property of type '"+(String)parameter[2].getValue()+"' is not of type Double");
                    }
                }else{
                    Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "Actor "+actor[i].getID()+" does not have a property of type '"+(String)parameter[2].getValue()+"'");
                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
            }
            Collections.sort(rankingVector);
            for(int i=0;i<rankingVector.size();++i){
                try {
                    Properties props = new Properties();
                    props.setProperty("PropertyClass", "java.lang.Double");
                    props.setProperty("PropertyID", (String) parameter[3].getValue());
                    Property property = PropertyFactory.newInstance().create(props);
                    property.add(new Double(i));
                    ((Actor)rankingVector.get(i).getRight()).add(property);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(RankingProperties.class.getName()).log(Level.SEVERE, "Class Double does not match class of property", ex);
                }
            }

        }else{
            Logger.getLogger(RankingProperties.class.getName()).log(Level.WARNING, "no actors of type '"+(String)parameter[1].getValue()+"' exist");
        }
        
    }

    @Override
    public InputDescriptor[] getInputType() {
        return input;
    }

    @Override
    public OutputDescriptor[] getOutputType() {
        return output;
    }

    @Override
    public Parameter[] getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(String param) {
        for (int i = 0; i < parameter.length; ++i) {
            if (parameter[i].getName().contentEquals(param)) {
                return parameter[i];
            }
        }
        return null;
    }

    @Override
    public SettableParameter[] getSettableParameter() {
        return new SettableParameter[]{parameter[4]};
    }

    @Override
    public SettableParameter getSettableParameter(String param) {
        if (parameter[4].getName().contentEquals(param)) {
            return parameter[4];
        } else {
            return null;
        }
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
        Properties props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "name");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("name") != null)) {
            parameter[0].setValue(map.getProperty("name"));
        } else {
            parameter[0].setValue("Ranking PageRank");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("Paper");
        }

        
       
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "inputProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("inputProperty") != null)) {
            parameter[2].setValue(map.getProperty("inputProperty"));
        } else {
            parameter[2].setValue("Knows PageRank Centrality");
        }
       
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "outputProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("outputProperty") != null)) {
            parameter[3].setValue(map.getProperty("outputProperty"));
        } else {
            parameter[3].setValue("Knows PageRank Ranking");
        }

        // Construct input descriptors
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[2].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[3].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }

}
