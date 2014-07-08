/*
 * OptimizedBetweeness.java
 *
 * Created on 22 October 2007, 16:27
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package org.mcennis.graphrat.algorithm.prestige;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mcennis.graphrat.algorithm.Algorithm;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.algorithm.reusablecores.OptimizedLinkBetweenessCore;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.model.Listener;
import org.dynamicfactory.model.Model;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.path.PathNode;

/**
 * Class built upon OptimizedPathBase that calculates Betweeness for links.  OptimizedPathBase
 * only records one geodesic path for each actor pair, otherwise same as the 
 * betweeness metric deefined in Freeman79. 
 * <br>
 * <br>
 * Freeman, L. "Centrality in social networks: I. Conceptual clarification."
 * <i>Social Networks</i>. 1:215--239.
 *
 * @author Daniel McEnnis
 * 
 */
public class OptimizedLinkBetweeness extends ModelShell implements Algorithm, Listener {

    private ParameterInternal[] parameter = new ParameterInternal[3];
    InputDescriptor[] input = new InputDescriptor[1];
    OutputDescriptor[] output = new OutputDescriptor[3];
    protected double maxBetweeness = 0.0;
    protected OptimizedLinkBetweenessCore core = new OptimizedLinkBetweenessCore();

    /** Creates a new instance of OptimizedBetweeness */
    public OptimizedLinkBetweeness() {
        super();
    }

    public void execute(Graph g){
        try{
        core.execute(g);
        Map<Link,Double> linkMap = core.getLinkMap();
        if (((Boolean) (getParameter())[3].getValue()).booleanValue()) {
            Logger.getLogger(OptimizedLinkBetweeness.class.getName()).log(Level.INFO, "Normalizing Betweeness values");
            double norm = 0.0;
            Iterator<Link> it = linkMap.keySet().iterator();
            while (it.hasNext()) {
                Link l = it.next();
                norm += linkMap.get(l) * linkMap.get(l);

            }
            it = linkMap.keySet().iterator();
            while (it.hasNext()) {
                Link l = it.next();
                linkMap.put(l, linkMap.get(l) / Math.sqrt(norm));
            }
        }
        Link[] links = g.getLink(core.getRelation());
        if(links != null){
        for(int i=0;i<links.length;++i) {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("PropertyType", "Basic");
            props.setProperty("PropertyClass", "java.lang.Double");
            props.setProperty("PropertyID", (String) (getParameter())[1].getValue() + " Betweeness");
            Property prop = PropertyFactory.newInstance().create(props);
            double betweeness = 0.0;
            if(linkMap.containsKey(links[i])){
                betweeness = linkMap.get(links[i]);
            }
            prop.add(new Double(betweeness));
            
            links[i].add(prop);
            if (betweeness > maxBetweeness) {
                maxBetweeness = betweeness;
            }
        }
        }
       } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(OptimizedLinkBetweeness.class.getName()).log(Level.SEVERE, "Property class of "+(String) (getParameter())[1].getValue()+" Betweeness does not match java.lang.Double", ex);
        }
    }
    
    protected void doCleanup(PathNode[] path, Graph g) {
    }


        @Override
    public SettableParameter[] getSettableParameter() {
        return null;
    }
    
    @Override
    public SettableParameter getSettableParameter(String param) {
        return null;
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


    /**
     * Parameters to be initialized.  Subclasses should override if they provide
     * any additional parameters or require additional inputs.
     * 
     * <ol>
     * <li>'name' - Name of this instance of the algorithm.  Default is ''.
     * <li>'relation' - type (relation) of link to calculate over. Default 'Knows'.
     * <li>'actorType' - type (mode) of actor to calculate over. Deafult 'User'.
     * <li>'normalize' - boolean for whether or not to normalize prestige vectors. 
     * Default 'false'.
     * </ol>
     * <br>
     * <br>Input 0 - Link
     * <br>Output 0 - LinkProperty
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
            parameter[0].setValue("Optimized Link Betweeness");
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
        
        // Parameter 3 - normalize
        props.setProperty("Type","java.lang.Boolean");
        props.setProperty("Name","normalize");
        props.setProperty("Class","Basic");
        props.setProperty("Structural","true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if((map != null)&&(map.getProperty("normalize") != null)){
            parameter[3].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("normalize"))));
        }else{
            parameter[3].setValue(new Boolean(false));
        }
        
        // Create Input Descriptors
        // Construct input descriptors
        props.setProperty("Type","Link");
        props.setProperty("Relation",(String)parameter[1].getValue());
        props.setProperty("AlgorithmName",(String)parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);
        
        // Construct Output Descriptors
        
        if (((String) parameter[0].getValue()).contentEquals("")) {
            parameter[0].setValue("Optimized Link Betweeness");
        }
        output = new OutputDescriptor[0];
        // Construct Output Descriptors
        props.setProperty("Type", "LinkProperty");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[1].getValue() + " Link Betweeness");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }

    public void publishChange(Model m, int type, int argument) {
        fireChange(type,argument);
    }
}
