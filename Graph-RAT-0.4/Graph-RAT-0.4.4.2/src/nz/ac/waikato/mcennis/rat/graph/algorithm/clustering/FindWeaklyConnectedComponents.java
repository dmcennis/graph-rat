/*
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.clustering;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Create subgraphs from all weakly connected components.  A weakly connected 
 * component is one where there exists a path between every pair of actors if all edges
 * are considered undirectional.  Subgraphs are disjoint and spanning.
 * 
 * @author Daniel McEnnis
 */
public class FindWeaklyConnectedComponents extends ModelShell implements Algorithm{

    private ParameterInternal[] parameter = new ParameterInternal[5];
    private InputDescriptor[] input = new InputDescriptor[1];
    private OutputDescriptor[] output = new OutputDescriptor[1];
    int graphCount=0;

    HashSet<Actor> component = new HashSet<Actor>();
    HashSet<Actor> seen = new HashSet<Actor>();
    LinkedList<Graph> children = new LinkedList<Graph>();
    
    public FindWeaklyConnectedComponents(){
        init(null);
    }
    
    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String)parameter[2].getValue());
        if(actor != null){
            fireChange(Scheduler.SET_ALGORITHM_COUNT,actor.length);
            for(int i=0;i<actor.length;++i){
                if(!seen.contains(actor[i])){
                    getComponent(actor[i],g);
                    getGraph(g);
                }
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
            }
        }
        if(children.size()>1){
            Iterator<Graph> child = children.iterator();
            while(child.hasNext()){
                g.addChild(child.next());
            }
        }
    }
    
    protected void getComponent(Actor seed,Graph g){
        component.add(seed);
        seen.add(seed);
        Link[] links = g.getLinkBySource((String)parameter[1].getValue(),seed);
        if(links != null){
            for(int i=0;i<links.length;++i){
                if(!seen.contains(links[i].getDestination())){
                    getComponent(links[i].getDestination(),g);
                }
            }
        }
        links = g.getLinkByDestination((String)parameter[1].getValue(),seed);
        if(links != null){
            for(int i=0;i<links.length;++i){
                if(!seen.contains(links[i].getSource())){
                    getComponent(links[i].getSource(),g);
                }
            }
        }
    }
    
    protected void getGraph(Graph g){
        try {
            Properties props = new Properties();
            props.setProperty("GraphClass", (String)parameter[3].getValue());
            props.setProperty("GraphID", ((String)parameter[4].getValue())+graphCount);
            graphCount++;
            children.add(g.getSubGraph(props, component));
            component.clear();
        } catch (Exception ex) {
            Logger.getLogger(FindWeaklyConnectedComponents.class.getName()).log(Level.SEVERE, null, ex);
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
        for(int i=0;i<parameter.length;++i){
            if(parameter[i].getName().contentEquals(param)){
                return parameter[i];
            }
        }
        return null;
    }

    @Override
    public SettableParameter[] getSettableParameter() {
        return null;
    }

    @Override
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

}
