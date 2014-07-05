/**
 * Sep 12, 2008-5:36:56 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Properties;
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
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyToLink  extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[5];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[1];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    /**
     * Create a new algorithm utilizing default parameters.
     */
    public PropertyToLink() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        Actor[] actor = g.getActor((String) parameter[1].getValue());

        if (actor != null) {
            for (int i = 0; i < actor.length; ++i) {
                Property linkSource = actor[i].getProperty((String) parameter[4].getValue());
                if (linkSource != null) {
                    Object[] values = linkSource.getValue();
                    if(values.length>0){
                        Instance data = (Instance)values[0];
                        Instances meta = data.dataset();
                        for(int j=0;j<meta.numAttributes();++j){
                            Actor dest = g.getActor((String)parameter[2].getValue(), meta.attribute(j).name());
                            if((dest != null)&&(!Double.isNaN(data.value(j)))){
                                Properties props = new Properties();
                                props.setProperty("LinkClass","Basic");
                                props.setProperty("LinkType",(String)parameter[3].getValue());
                                Link link = LinkFactory.newInstance().create(props);
                                link.set(actor[i], data.value(j), dest);
                                g.add(link);
                            }
                        }
                    }else{
                        Logger.getLogger(PropertyToLink.class.getName()).log(Level.WARNING, "Property '" + linkSource.getType() + "' on actor '" + actor[i].getID() + "' has no value");
                    }
                } else {
                    Logger.getLogger(PropertyToLink.class.getName()).log(Level.WARNING, "Property '" + (String) parameter[4].getValue() + "' on actor '" + actor[i].getID() + "' does not exist");
                }
            }
        } else {
            Logger.getLogger(PropertyToLink.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter[1].getValue() + "' were found");
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
    public Parameter getParameter(
            String param) {
        for (int i = 0; i <
                parameter.length; ++i) {
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
    public SettableParameter getSettableParameter(
            String param) {
        return null;
    }

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
            parameter[0].setValue("From Graph To Actor");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("artist");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "destinationActorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("destinationActorType") != null)) {
            parameter[2].setValue(map.getProperty("destinationActorType"));
        } else {
            parameter[2].setValue("genre");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[3].setValue(map.getProperty("relation"));
        } else {
            parameter[3].setValue("Derived Artist");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "property");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("property") != null)) {
            parameter[4].setValue(map.getProperty("property"));
        } else {
            parameter[4].setValue("CombinedProperty");
        }

        props.setProperty("Type", "ActorProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[4].getValue());
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
