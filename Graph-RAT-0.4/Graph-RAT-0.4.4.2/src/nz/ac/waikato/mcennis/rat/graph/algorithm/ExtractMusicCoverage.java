/* * ExtractMusicCoverage.java * * Created on 23 October 2007, 14:51 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm;import java.io.IOException;import java.util.HashSet;import java.util.Properties;import org.mcennis.graphrat.algorithm.Algorithm;import org.mcennis.graphrat.graph.Graph;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.SettableParameter;import org.mcennis.graphrat.link.Link;import org.dynamicfactory.model.ModelShell;import org.mcennis.graphrat.scheduler.Scheduler;/** * Class that enumerates all artists that are present in the given dataset. * * @author Daniel McEnnis *  */public class ExtractMusicCoverage extends ModelShell implements Algorithm {    ParameterInternal[] parameter = new ParameterInternal[3];    OutputDescriptor[] output = new OutputDescriptor[0];    InputDescriptor[] input = new InputDescriptor[1];    /** Creates a new instance of ExtractMusicCoverage */    public ExtractMusicCoverage() {        init(null);    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    /**     * Parameters to be initialized:     *      * <ol>     * <li>'name' - name of this instance of the algorithm. Deafult 'Extract Music Coverage'.     * <li>'relation' - name of the type (relation) of link to acquire artists from.     * <li>'output' - location to store the list of artists discovered. Default '/tmp/artistCoverage.txt'.     * </ol>     *      * <br>Input 1 - Link     */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Extract Music Coverage");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Given");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "output");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("output") != null)) {            parameter[2].setValue(map.getProperty("output"));        } else {            parameter[2].setValue("/tmp/artistCoverage.txt");        }        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    /**     * Generate a list of all artists linked to in this graph.     *      */    public void execute(Graph g) {        Link[] links = g.getLink((String) parameter[1].getValue());        java.util.HashSet<String> bandNames = new HashSet<String>();        if (links != null) {            fireChange(Scheduler.SET_ALGORITHM_COUNT,links.length);            for (int i = 0; i < links.length; ++i) {                bandNames.add(links[i].getDestination().getID());                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);            }        }        try {            java.io.FileWriter output = new java.io.FileWriter((String) parameter[2].getValue());            java.util.Iterator<String> it = bandNames.iterator();            while (it.hasNext()) {                output.write(it.next());                output.write("\n");            }            output.close();        } catch (IOException ex) {            ex.printStackTrace();        }    }}