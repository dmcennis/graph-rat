/*
 * Created 30/05/2008-13:57:59
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.transformToArray;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix2D;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.ActorDistanceMatrixCore;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

/**
 * Class for creating a matrix representing the distances between graph nodes.
 *
 * @author Daniel McEnnis
 */
public class ActorDistanceMatrix extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[7];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[2];
    ActorDistanceMatrixCore base = new ActorDistanceMatrixCore();

    /**
     * Creates an algorithm with default parameters.
     */
    public ActorDistanceMatrix(){
        init(null);
    }
    
    @Override
    public void execute(Graph g) {
        base.setMode((String) parameter[1].getValue());
        base.setRelation((String) parameter[2].getValue());
        base.execute(g);

        if (base.getActorMatrix() != null) {
            if ((Boolean) parameter[3].getValue()) {
                Properties props = new Properties();
                props.setProperty("PropertyType", "Basic");
                props.setProperty("PropertyID", (String) parameter[5].getValue());
                props.setProperty("PropertyClass", "cern.colt.matrix.DoubleMatrix2D");
                Property graphProperty = PropertyFactory.newInstance().create(props);
                try {
                    graphProperty.add(base.getActorMatrix());
                    g.add(graphProperty);
                } catch (InvalidObjectTypeException ex) {
                    Logger.getLogger(ActorDistanceMatrix.class.getName()).log(Level.SEVERE, "Property class doesn't match DoubleMatrix2D", ex);
                }
            }

            if ((Boolean) parameter[4].getValue()) {
                Properties props = new Properties();
                props.setProperty("PropertyType", "Basic");
                props.setProperty("PropertyID", (String) parameter[6].getValue());
                props.setProperty("PropertyClass", "cern.colt.matrix.DoubleMatrix1D");
                Actor[] actors = g.getActor((String) parameter[1].getValue());
                if (actors != null) {
                    for(int i=0;i<actors.length;++i){
                        try {
                            Property actorProperty = PropertyFactory.newInstance().create(props);
                            actorProperty.add(base.getActorMatrix().viewColumn(base.getColumn(actors[i])));
                            actors[i].add(actorProperty);
                        } catch (InvalidObjectTypeException ex) {
                            Logger.getLogger(ActorDistanceMatrix.class.getName()).log(Level.SEVERE, "Property class doesn't match DoubleMatrix1D", ex);
                        }
                    }
                }
            }
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
        return null;
    }

    @Override
    public SettableParameter getSettableParameter(String param) {
        return null;
    }

    /**
     * Parameter:<br/>
     * <br/>
     * <ul>
     * <li/><b>name</b>: Name of this algorithm. Default is 'Graph Triples Histogram'
     * <li/><b>mode</b>: Mode of the actors to calcualtes distances over. Default
     * is 'Artist'
     * <li/><b>relation</b>: Relation used to calculate distances from.  Default
     * is 'Similarity'
     * <li/><b>makeGraphProperty</b>: Should the 2D colt matrix be added to the graph
     * object as a property.  Deafult is 'true'
     * <li/><b>graphProperty</b>: Property ID of the graph property. Default is 
     * 'Actor Distance Matrix'
     * <li/><b>makeActorProperty</b>: Should the 1D colt matrix be added to each actor
     * as a property. Deafult is 'false'
     * <li/><b>actorProperty</b>: Property ID of the actor property. Default is 'Actor Distance Vector'
     * </ul>
     * @param map parameters to be loaded - null is permitted
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
            parameter[0].setValue("Graph Triples Histogram");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "mode");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("mode") != null)) {
            parameter[1].setValue(map.getProperty("mode"));
        } else {
            parameter[1].setValue("Artist");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[2].setValue(map.getProperty("relation"));
        } else {
            parameter[2].setValue("Similarity");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "makeGraphProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("makeGraphProperty") != null)) {
            parameter[3].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("makeGraphProperty"))));
        } else {
            parameter[3].setValue(new Boolean(true));
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "graphProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("graphPropertyName") != null)) {
            parameter[4].setValue(map.getProperty("graphPropertyName"));
        } else {
            parameter[4].setValue("Actor Distance Matrix");
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "makeActorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("makeActorProperty") != null)) {
            parameter[5].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("makeActorProperty"))));
        } else {
            parameter[5].setValue(new Boolean(false));
        }

        props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorProperty") != null)) {
            parameter[6].setValue(map.getProperty("actorProperty"));
        } else {
            parameter[6].setValue("Actor Distance Vector");
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
        props.setProperty("Property",(String)parameter[4].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation",(String)parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[6].getValue());
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);

    }
}
