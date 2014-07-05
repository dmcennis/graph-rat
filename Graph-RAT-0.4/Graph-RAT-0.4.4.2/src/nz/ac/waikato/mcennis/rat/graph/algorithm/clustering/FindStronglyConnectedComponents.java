/*
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;

import nz.ac.waikato.mcennis.rat.graph.algorithm.*;
import java.util.Properties;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.FindStronglyConnectedComponentsCore;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.Listener;
import nz.ac.waikato.mcennis.rat.graph.model.Model;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

/**
 * Algorithm for finding all strongly connected components in a graph.  Uses 
 * Robert Tarjan's algorithm for finding components in linear time.
 * 
 * Tarjan, R. 1972. Depth-first search and linear graph algorithms. <i>Society of 
 * Industrial and Applied Mathematics.</i> 1(20):146--160
 * 
 * @author Daniel McEnnis
 */
public class FindStronglyConnectedComponents extends ModelShell implements Algorithm, Listener {
    private ParameterInternal[] parameter = new ParameterInternal[5];
    private InputDescriptor[] input = new InputDescriptor[1];
    private OutputDescriptor[] output = new OutputDescriptor[1];
   
    FindStronglyConnectedComponentsCore core = new FindStronglyConnectedComponentsCore();
    
    public FindStronglyConnectedComponents(){
        init(null);
        core.addListener(this);
    }
    public void execute(Graph g){
        core.execute(g);
        Graph[] subgraphs = core.getGraph();
        if((subgraphs!=null)&&(subgraphs.length>1)){
            for(int i=0;i<subgraphs.length;++i){
                g.addChild(subgraphs[i]);
            }
        }
    }
    
    public InputDescriptor[] getInputType() {
        return input;
    }

    public OutputDescriptor[] getOutputType() {
        return output;
    }

    public Parameter[] getParameter() {
        return parameter;
    }

    public Parameter getParameter(String param) {
        for(int i=0;i<parameter.length;++i){
            if(parameter[i].getName().contentEquals(param)){
                return parameter[i];
            }
        }
        return null;
    }

    public SettableParameter[] getSettableParameter() {
        return null;
    }

    public SettableParameter getSettableParameter(String param) {
        return null;
    }

     /**
     * Parameters to be initialized.  Subclasses should override if they provide
     * any additional parameters or require additional inputs.
     * 
     * <ol>
     * <li>'name' - Name of this instance of the algorithm.  Default is ''.
     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.
     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'.
     * <li>'graphClass' - graph class used to create subgraphs
     * <li>'graphIDprefix' - prefix used for graphIDs.
     * </ol>
     * <br>
     * <br>Input 0 - Link
     * <br>Output 0 - Graph
     */
   public void init(Properties map) {
        Properties props = new Properties();
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","name");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if((map != null)&&(map.getProperty("name") != null)){
            parameter[0].setValue(map.getProperty("name"));
        }else{
            parameter[0].setValue("Bicomponent Clusterer");
        }
        
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","relation");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if((map != null)&&(map.getProperty("relation") != null)){
            parameter[1].setValue(map.getProperty("relation"));
        }else{
            parameter[1].setValue("Knows");
        }
        core.setRelation((String)parameter[1].getValue());
        
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","actorType");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if((map != null)&&(map.getProperty("actorType") != null)){
            parameter[2].setValue(map.getProperty("actorType"));
        }else{
            parameter[2].setValue("User");
        }
        core.setMode((String)parameter[2].getValue());
        
        // Parameter 3 - graphClass
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","graphClass");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if((map != null)&&(map.getProperty("graphClass") != null)){
            parameter[3].setValue(map.getProperty("graphClass"));
        }else{
            parameter[3].setValue("MemGraph");
        }
        core.setGraphClass((String)parameter[3].getValue());
        
        // Parameter 4 - graphID
        props.setProperty("Type","java.lang.String");
        props.setProperty("Name","graphIDprefix");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if((map != null)&&(map.getProperty("graphIDprefix") != null)){
            parameter[4].setValue(map.getProperty("graphIDprefix"));
        }else{
            parameter[4].setValue("Bicomponent ");
        }
        core.setGraphPrefix((String)parameter[4].getValue());
        
        
        // Create Input Descriptors
        // Construct input descriptors
        props.setProperty("Type","Link");
        props.setProperty("Relation",(String)parameter[1].getValue());
        props.setProperty("AlgorithmName",(String)parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);
        
        // Construct Output Descriptors
        props.setProperty("Type", "Graph");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);    
   }

    public void publishChange(Model m, int type, int argument) {
        fireChange(type,argument);
    }

}
