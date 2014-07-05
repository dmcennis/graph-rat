/*
 * Created 29/05/2008-14:15:01
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.transformToArray;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Rediscovery of Batagel-Mrvar algorithm
 * 
 * Batagel, V., A. Mrvar. 2001. A subquadratic triad census algorithm for large
 * sparse networks with small maximum degree. <i>Social Networks</i> 23:237-43.
 * 
 * Transforms a graph object (one mode and relation) into 4 dimensional double
 * array describing a histogram of all triples of actors in the graph.  
 * <br/>
 * Links are classified in the following way:
 * <ul>
 * <li/>0: no link exists
 * <li/>1: smaller->greater link exists
 * <li/>2: greater->smaller link exists
 * <li/>3: bi-directional link exists
 * </ul>
 * 
 * 
 * 
 * @author Daniel McEnnis
 */
public class UndirectedTriplesHistogram extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[4];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];
    
    public UndirectedTriplesHistogram(){
        init(null);
    }

    @Override
    public void execute(Graph g) {
        TreeSet<Actor> triplesBase = new TreeSet<Actor>();
        Iterator<Actor> iterator = g.getActorIterator((String) parameter[1].getValue());
        if (iterator != null) {
            while (iterator.hasNext()) {
                triplesBase.add(iterator.next());
            }
            double[] tripleCount = new double[4];
            Arrays.fill(tripleCount, 0.0);
            for (Actor first : triplesBase) {
                TreeSet<Actor> firstActorList = getLinks(g, first, first);
                for (Actor second : firstActorList) {
                    if (second.compareTo(first) > 0) {

                        // handle all fully linked triples
                        TreeSet<Actor> secondActorList = getLinks(g,second,second);
                        TreeSet<Actor> fullyLinked = new TreeSet<Actor>();
                        fullyLinked.addAll(firstActorList);
                        fullyLinked.retainAll(secondActorList);
                        tripleCount[3] += fullyLinked.size();
                        
                        // handle all 2 sided triples
                        secondActorList = getLinks(g, second, first);
                        TreeSet<Actor> twoSided = new TreeSet<Actor>();
                        twoSided.addAll(secondActorList);
                        twoSided.removeAll(firstActorList);
                        tripleCount[2] += twoSided.size();
                        
                        // handle all 1 sided triples
                        tripleCount[1] += (triplesBase.tailSet(second).size()-1)-fullyLinked.size()-twoSided.size();
                    }
                }
            }
            tripleCount[0] = triplesBase.size()*(triplesBase.size()-1)*(triplesBase.size()-2)-tripleCount[1]-tripleCount[2]-tripleCount[3];
            FastVector base = new FastVector();
            for(int i=0;i<tripleCount.length;++i){
                base.addElement(new Attribute(g.getID()+(String)parameter[3].getValue()+":"+i));
            }
            Instances meta = new Instances("Undirected Triples",base,1);
            Instance value = new Instance(tripleCount.length,tripleCount);
            value.setDataset(meta);
            Properties props = new Properties();
            props.setProperty("PropertyType","Basic");
            props.setProperty("PropertyClass", "weka.core.Instance");
            props.setProperty("PtopertyID",g.getID()+(String)parameter[3].getValue());
            Property property = PropertyFactory.newInstance().create(props);
            try{
                property.add(value);
                g.add(property);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(UndirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Property class does not match double[]", ex);
            }
        }else{
            Logger.getLogger(UndirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "No actors of mode '"+(String)parameter[1].getValue()+"' were found in graph '"+g.getID()+"'");
        }
    }
    
    protected TreeSet<Actor> getLinks(Graph g, Actor v, Actor comparator) {
        TreeSet<Actor> actors = new TreeSet<Actor>();
        Link[] out = g.getLinkBySource((String) parameter[1].getValue(), v);
        if (out != null) {
            for (int i = 0; i < out.length; ++i) {
                if(out[i].getDestination().compareTo(comparator)>0){
                actors.add(out[i].getDestination());
                }
            }
        }
        Link[] in = g.getLinkByDestination((String) parameter[1].getValue(), v);
        if (in != null) {
            for (int i = 0; i < in.length; ++i) {
                if (!actors.contains(in[i].getSource())&&in[i].getSource().compareTo(comparator)>0) {
                    actors.add(in[i].getSource());
                }
            }
        }
        return actors;
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
        return null;
    }

    @Override
    public SettableParameter getSettableParameter(String param) {
        return null;
    }

    /**
     * Parameters: <br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: name of this algorithm. Default is 'Graph Undirected Triples Histogram'
     * <li/><b>actorType</b>: mode of actor to execute over. Default is 'tag'
     * <li/><b>relation</b>: link relation to execute over. Default is 'Tags'
     * <li/><b>propertyNameSuffix</b>: Property ID suffix for the graph property.
     * Deafult is ' Undirected Triples Histogram'
     * </ul>
     * 
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
            parameter[0].setValue("Graph Undirected Triples Histogram");
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
        props.setProperty("Name", "propertyNameSuffix");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("propertyNameSuffix") != null)) {
            parameter[3].setValue(map.getProperty("propertyNameSuffix"));
        } else {
            parameter[3].setValue(" Undirected Triples Histogram");
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

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[3].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

    }
}
