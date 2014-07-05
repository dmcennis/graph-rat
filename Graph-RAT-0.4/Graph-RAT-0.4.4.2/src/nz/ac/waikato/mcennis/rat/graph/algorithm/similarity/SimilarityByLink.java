/*
 * Created 7-5-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.similarity;

import nz.ac.waikato.mcennis.rat.graph.algorithm.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.DataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.MapDataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DistanceFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.DistanceFunction;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

/**
 * Determine the similarity between two actors by the data vector implied by the 
 * given relation.  Also can be described as a percentage of role similarity between
 * two actors (Social Network Analysis).
 * 
 * @author Daniel McEnnis
 */
public class SimilarityByLink extends ModelShell implements Algorithm{
    private ParameterInternal[] parameter = new ParameterInternal[7];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];
        
    /**
     * Create a new algorithm utilizing default parameters.
     */
    public SimilarityByLink(){
        init(null);
    }
    
    @Override
    public void execute(Graph g){
        Actor[] tags = g.getActor((String)parameter[1].getValue());
        Properties props= new Properties();
        props.setProperty("DistanceFunction", (String)parameter[6].getValue());
        DistanceFunction similarity = DistanceFactory.newInstance().create(props);
        if(tags != null){
            HashMap<Actor,Double>[] actorMap = new HashMap[tags.length];
            HashSet<Actor> actorCount = new HashSet<Actor>();
            DataVector[] dataVectorArray = new DataVector[actorMap.length];
            for(int i=0;i<tags.length;++i){
                actorMap[i] = getMap(tags[i],actorCount,g);
//                actorCount.add(e);
                dataVectorArray[i] = new MapDataVector(actorMap[i],tags.length);
                dataVectorArray[i].setSize(tags.length);
            }
            for(int i=0;i<tags.length;++i){
                for(int j=0;j<tags.length;++j){
                    if(i != j){
                        double sim =  similarity.distance(dataVectorArray[i], dataVectorArray[j]); //similarity(actorMap,i,j,actorCount.size(),g);
                        if(sim >= ((Double)parameter[5].getValue()).doubleValue()){
                            props.setProperty("LinkClass", "Basic");
                            props.setProperty("LinkType",(String)parameter[4].getValue());
                            Link simLink = LinkFactory.newInstance().create(props);
                            simLink.set(tags[i], sim, tags[j]);
                            g.add(simLink);
                        }
                    }
                }
            }
        }else{
            Logger.getLogger(SimilarityByLink.class.getName()).log(Level.WARNING,"No tags of mode '"+(String)parameter[1].getValue()+"' are present");
        }
    }
    
    protected HashMap<Actor,Double> getMap(Actor tag, HashSet<Actor> total, Graph g){
        HashMap<Actor,Double> ret = new HashMap<Actor,Double>();
        
        if("Outgoing".contentEquals((String)parameter[3].getValue())||"All".contentEquals((String)parameter[3].getValue())){
            Link[] link = g.getLinkBySource((String)parameter[2].getValue(), tag);
            if(link != null){
                for(int i=0;i<link.length;++i){
                            ret.put(link[i].getDestination(), link[i].getStrength());
                            total.add(link[i].getDestination());
                }
            }
        }
        if("Incoming".contentEquals((String)parameter[3].getValue())||"All".contentEquals((String)parameter[3].getValue())){
            Link[] link = g.getLinkByDestination((String)parameter[2].getValue(), tag);
            if(link != null){
                for(int i=0;i<link.length;++i){
                    total.add(link[i].getSource());
                    if(!ret.containsKey(link[i].getSource())){
                        ret.put(link[i].getSource(), link[i].getStrength());
                    }else{
                        ret.put(link[i].getSource(), link[i].getStrength()+ret.get(link[i].getSource()));
                    }
                }
            }
        }
        
        if((!"Incoming".contentEquals((String)parameter[3].getValue()))&&(!"All".contentEquals((String)parameter[3].getValue()))&&(!"Outgoing".contentEquals((String)parameter[3].getValue()))){
            Logger.getLogger(SimilarityByLink.class.getName()).log(Level.SEVERE,"Invalid direction of link '"+(String)parameter[3].getValue()+"'");
        }
        return ret;
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
     * Parameters:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of the algorithm. Default is 'Simlarity By Link'
     * <li/><b>actorType</b>: Mode of the actor to compare. Default is 'tag'
     * <li/><b>relation</b>: Relation to calculate similarity from. Default is 'Tags'
     * <li/><b>linkDirection</b>: type of link to use- Incoming, Ougoing, or All.
     * Default is 'Outgoing'
     * <li/><b>linkName</b>: Name of generated similarity links.  Default is 'Tag Similarity'
     * <li/><b>threshold</b>: threshold of similarity before a new link is created.
     * Deafult is '0.75'
     * <li/><b>distanceFunction</b>: which distance function to use. Deafult is 
     * 'EuclideanDistance'
     * </ul>
     * @param map parameters to be loaded - may be null
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
            parameter[0].setValue("Simlarity By Link");
        }
        
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("tag");
        }
        
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[2].setValue(map.getProperty("relation"));
        } else {
            parameter[2].setValue("Tags");
        }
        
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkDirection");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkDirection") != null)) {
            parameter[3].setValue(map.getProperty("linkDirection"));
        } else {
            parameter[3].setValue("Outgoing");
        }
        
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkName");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkName") != null)) {
            parameter[4].setValue(map.getProperty("linkName"));
        } else {
            parameter[4].setValue("Tag Similarity");
        }
        
        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "threshold");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("threshold") != null)) {
            parameter[5].setValue(new Double(Double.parseDouble(map.getProperty("threshold"))));
        } else {
            parameter[5].setValue(new Double(0.75));
        }
        
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "distanceFunction");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("distanceFunction") != null)) {
            parameter[6].setValue(map.getProperty("distanceFunction"));
        } else {
            parameter[6].setValue("CosineDistance");
        }
        
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);
        
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }

}
