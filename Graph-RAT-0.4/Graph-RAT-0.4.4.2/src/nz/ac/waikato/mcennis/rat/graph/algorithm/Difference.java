/*
 *  Created 11-1-08
 *  Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 *  Algorithm for calculating the difference across properties of an actor
 * 
 * @author Daniel McEnnis
 */
public class Difference extends ModelShell implements Algorithm{
    
    ParameterInternal[] parameter = new ParameterInternal[5];
    
    InputDescriptor[] input = new InputDescriptor[1];
    
    OutputDescriptor[] output = new OutputDescriptor[1];

    public void execute(Graph g) {
        Actor[] actor = g.getActor((String)parameter[1].getValue());
        if(actor != null){
        fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length);
            for(int i=0;i<actor.length;++i){
                Property left = actor[i].getProperty((String)parameter[2].getValue());
                Property right = actor[i].getProperty((String)parameter[3].getValue());
                if((left != null)&&(right != null)){
                    if(left.getPropertyClass().isAssignableFrom(Double.class)&&right.getPropertyClass().isAssignableFrom(Double.class)){
                        if((left.getValue() != null)&&(right.getValue() != null)){
                            try {
                                double leftValue = ((Double) (left.getValue()[0])).doubleValue();
                                double rightValue = ((Double) (right.getValue()[0])).doubleValue();
                                double answerValue = leftValue - rightValue;
                                Properties props = new Properties();
                                props.setProperty("PropertyID", (String) parameter[4].getValue());
                                props.setProperty("PropertyClass", "java.lang.Double");
                                props.setProperty("PropertyType", "Basic");
                                Property answer = PropertyFactory.newInstance().create(props);
                                answer.add(new Double(answerValue));
                                actor[i].add(answer);
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
                        Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Left (Positive) property '"+(String)parameter[2].getValue()+"' is null for actor '"+actor[i].getID()+"'");
                    }
                    if(right == null){
                        Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Right (Negative) property '"+(String)parameter[2].getValue()+"' is null for actor '"+actor[i].getID()+"'");
                    }
                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
            }
        }else{
            Logger.getLogger(Difference.class.getName()).log(Level.WARNING,"Mode "+(String)parameter[1].getValue()+" has no actors in this graph");
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
        Properties props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "name");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("name") != null)) {
            parameter[0].setValue(map.getProperty("name"));
        } else {
            parameter[0].setValue("Difference");
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
        props.setProperty("Name", "leftProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("leftProperty") != null)) {
            parameter[2].setValue(map.getProperty("leftProperty"));
        } else {
            parameter[2].setValue("Knows PageRank Centrality");
        }
       
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "rightProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("rightProperty") != null)) {
            parameter[3].setValue(map.getProperty("rightProperty"));
        } else {
            parameter[3].setValue("Knows PageRank Centrality");
        }
       
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "outputProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("outputProperty") != null)) {
            parameter[4].setValue(map.getProperty("outputProperty"));
        } else {
            parameter[4].setValue("Knows PageRank Ranking");
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
        props.setProperty("Property",(String)parameter[4].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }

}
