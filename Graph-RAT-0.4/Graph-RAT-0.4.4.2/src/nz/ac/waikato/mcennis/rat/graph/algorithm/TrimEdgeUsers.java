/* * TrimEdgeUsers.java * * Created on 12 June 2007, 16:55 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm;import java.util.Properties;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.InputDescriptorInternal;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptorInternal;import org.dynamicfactory.descriptors.SettableParameter;import org.dynamicfactory.model.ModelShell;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Class for removing all actors of a given type (mode) that have no outgoing links * * @author Daniel McEnnis * */public class TrimEdgeUsers extends ModelShell implements Algorithm {    public static final long serialVersionUID = 2;    ParameterInternal[] parameter = new ParameterInternal[3];    InputDescriptorInternal[] input = new InputDescriptorInternal[1];    OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];    /** Creates a new instance of TrimEdgeUsers */    public TrimEdgeUsers() {        init(null);    }    /**     * Remove actors without outgoing links.     */    public void execute(Graph g) {        java.util.Iterator<Actor> masterList = g.getActorIterator((String) parameter[2].getValue());        fireChange(Scheduler.SET_ALGORITHM_COUNT,g.getActorCount((String) parameter[2].getValue()));        int actorCount=0;        while (masterList.hasNext()) {            Actor actor = masterList.next();            if (g.getLinkBySource((String) parameter[1].getValue(), actor) == null) {                g.remove(actor);            }            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);        }    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    /**     * Parameters to be initialized.      *      * <ol>     * <li>'name' - name of this instance of the algorithm. Default 'Trim Edge Actors'     * <li>'relation' - type (relation) of link to compute over. Default 'Knows'.     * <li>'actorType' - type (mode) of actor to compute over. Default 'User'.     * </ol>     * <br>     * <br>Input 0 - Actor     * <br>Output 0 - Actor     *      */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Trim Edge Actors");        }        // Parameter 1 - relation        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actorType        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // input 0        props.setProperty("Type", "Actor");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // output 0        props.setProperty("Type", "Actor");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}