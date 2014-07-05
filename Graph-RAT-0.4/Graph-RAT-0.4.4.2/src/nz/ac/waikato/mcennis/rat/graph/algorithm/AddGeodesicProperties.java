/* * AddGeodesicProperties.java * * Created on 6 July 2007, 13:54 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Graph;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.InputDescriptorInternal;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptorInternal;import org.dynamicfactory.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.path.PathSet;import nz.ac.waikato.mcennis.rat.graph.path.Path;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * * Calculates properties of a node: *  * (In/Out) Eccentricity - Wasserman and Faust p111 - modified for directional links * <br> * Diameter - Wasserman and Faust p111. Modified to be longest non-infinite  * geodesic path, eliminating infinite diameters. * <br> * Wasserman, S., and K. Faust. 1997. <i>Social Network Analysis</i>. New York: Cambridge University Press. * @author Daniel McEnnis * * FIXME: Only uses existance of the link - does not use the link's strength. */public class AddGeodesicProperties extends ModelShell implements Algorithm {    public final static long serialVersionUID = 2;    private ParameterInternal[] parameter = new ParameterInternal[3];    private InputDescriptorInternal[] input = new InputDescriptorInternal[1];    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[3];    /** Creates a new instance of AddGeodesicProperties */    public AddGeodesicProperties() {        init(null);    }    /**     * Calculates properties of a node:     *      * (In/Out) Eccentricity - Wasserman and Faust p111 - modified for directional links     * <br>     * Diameter - Wasserman and Faust p111. Modified to be longest non-infinite      * geodesic path, eliminating infinite diameters.     * <br>     * Wasserman, S., and K. Faust. 1997. <i>Social Network Analysis</i>. New York: Cambridge University Press.     *      */    public void execute(Graph g) {        fireChange(Scheduler.SET_ALGORITHM_COUNT, 3);        addInEccentricity(g);        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);        addOutEccentricity(g);        fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);        addDiameter(g);    }    protected void addInEccentricity(Graph g) {        PathSet pathSet = g.getPathSet("Directional Geodesic by " + (String) parameter[1].getValue());        java.util.Iterator<Actor> list = g.getActorIterator((String) parameter[2].getValue());        while (list.hasNext()) {            Property eccentricity = null;            try {                Actor userList = list.next();                Path[] paths = pathSet.getPathByDestination(userList.getID());                double cost = Double.POSITIVE_INFINITY;                if (paths != null) {                    cost = Double.NEGATIVE_INFINITY;                    for (int j = 0; j < paths.length; ++j) {                        if (paths[j].getCost() > cost) {                            cost = paths[j].getCost();                        }                    }                }                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) parameter[1].getValue() + " In Eccentricity");                eccentricity = PropertyFactory.newInstance().create(props);                eccentricity.add(new Double(cost));                userList.add(eccentricity);            } catch (InvalidObjectTypeException ex) {                if (eccentricity != null) {                    Logger.getLogger(AddGeodesicProperties.class.getName()).log(Level.SEVERE, "Property type java.lang.Double does match type '" + eccentricity.getPropertyClass().getName() + "'", ex);                }            }        }    }    protected void addOutEccentricity(Graph g) {        PathSet pathSet = g.getPathSet("Directional Geodesic by " + (String) parameter[1].getValue());        java.util.Iterator<Actor> list = g.getActorIterator((String) parameter[2].getValue());        while (list.hasNext()) {            try {                Actor userList = list.next();                Path[] paths = pathSet.getPathBySource(userList.getID());                double cost = Double.POSITIVE_INFINITY;                if (paths != null) {                    cost = Double.NEGATIVE_INFINITY;                    for (int j = 0; j < paths.length; ++j) {                        if (paths[j].getCost() > cost) {                            cost = paths[j].getCost();                        }                    }                }                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) parameter[1].getValue() + " Out Eccentricity");                Property eccentricity = PropertyFactory.newInstance().create(props);                eccentricity.add(new Double(cost));                userList.add(eccentricity);            } catch (InvalidObjectTypeException ex) {                Logger.getLogger(AddGeodesicProperties.class.getName()).log(Level.SEVERE, null, ex);            }        }    }    /**     * Diamter defined as the longest geodesic path.  Avoids problems of infinite     * or undefined diamters if some actors are poorly connected.     * @param g graph to be modified     */    protected void addDiameter(Graph g) {        try {            double diameter = Double.NEGATIVE_INFINITY;            Path[] paths = g.getPathSet("Directional Geodesic by " + (String) parameter[1].getValue()).getPath();            for (int i = 0; i < paths.length; ++i) {                if (paths[i].getCost() > diameter) {                    diameter = paths[i].getCost();                }            }            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String) parameter[1].getValue() + " Diameter");            Property diameterProp = PropertyFactory.newInstance().create(props);            diameterProp.add(new Double(diameter));            g.add(diameterProp);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddGeodesicProperties.class.getName()).log(Level.SEVERE, null, ex);        }    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    /**     * Parameters for initialization of this algorithm     * <br>     * <ol>     * <li>'name' - name of this isntance. Default is 'Geodesic Properties'.     * <li>'relation' - type (relation) of link to use.  Default is 'Knows'.     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.     * </ol>     * <br>     * <br>Input 1 - Path     * <br>Output 1 - Actor Property     * <br>Output 2 - Actor Property     * <br>Output 3 - Graph Property     */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Geodesic Properties");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actor type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // Construct input descriptors        props.setProperty("Type", "Path");        props.setProperty("Relation", "Directional Geodesic By " + (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        //Output Descriptors        props.setProperty("Type", "Actor");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " In Eccentricity");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "Actor");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Out Eccentricity");        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "Graph");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Diameter");        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}