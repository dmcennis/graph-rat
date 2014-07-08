/* * AddBetweenessCentrality.java * * Created on 27 June 2007, 19:02 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package org.mcennis.graphrat.algorithm.prestige;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import org.mcennis.graphrat.algorithm.Algorithm;import org.mcennis.graphrat.graph.Graph;import org.mcennis.graphrat.actor.Actor;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.InputDescriptorInternal;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptorInternal;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.SettableParameter;import org.dynamicfactory.model.ModelShell;import org.mcennis.graphrat.path.PathSet;import org.mcennis.graphrat.path.Path;import org.mcennis.graphrat.scheduler.Scheduler;/** * Class for calculating Betweeness as described in Freeman 79 except that  * multiple geodesic paths between two actors are treated as if they are unique paths. * <br> * <br> * Freeman, L. "Centrality in social networks: I. Conceptual clarification." * <i>Social Networks</i>. 1:215--239. * * @author Daniel McEnnis *  */public class AddBasicBetweenessCentrality extends ModelShell implements Algorithm {    public static final long serialVersionUID = 1;    Actor[] userList;    double[] betweeness;    double maxBetweeness = Double.NEGATIVE_INFINITY;    java.util.HashMap<Actor, Integer> map = new java.util.HashMap<Actor, Integer>();    ParameterInternal[] parameter = new ParameterInternal[4];    OutputDescriptorInternal[] output = new OutputDescriptorInternal[3];    InputDescriptorInternal[] input = new InputDescriptorInternal[1];    /** Creates a new instance of AddBetweenessCentrality */    public AddBasicBetweenessCentrality() {        init(null);    }    /**     * Implements betweeness as in Freeman79 except multiple geodesic paths of the     * same type are treated as independent geodesic paths.     * <br>     * Freeman, L. "Centrality in social networks: I. Conceptual clarification."     * <i>Social Networks</i>. 1:215--239.     * <br>     * @see nz.ac.waikato.mcennis.rat.graph.algorithm.AddBasicBetweenessCentrality#init     *      * @param g graph to be modified     */    public void execute(Graph g) {        userList = g.getActor("User");        for (int i = 0; i < userList.length; ++i) {            map.put(userList[i], i);        }        betweeness = new double[userList.length];        fireChange(Scheduler.SET_ALGORITHM_COUNT,userList.length+2);        calculateBetweeness(g);         calculateGraphBetweeness(g);        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,userList.length);        calculateBetweenessSD(g);        betweeness = null;        userList = null;    }    /**     * Adds Betweeness properties to every actor of the given relation.     *      * @param g graph to be modified     */    protected void calculateBetweeness(Graph g) {        try {            PathSet pathSet = g.getPathSet("Directional Geodesic by " + (String) parameter[1].getValue());            for (int i = 0; i < userList.length; ++i) {                if (i % 100 == 0) {                    Logger.getLogger(AddBasicBetweenessCentrality.class.getName()).log(Level.FINER,"Processing " + i + " of " + userList.length);                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);                }                for (int j = 0; j < userList.length; ++j) {                    if (i != j) {                        sumPath(i, j, pathSet);                    }                }            }            if (((Boolean) parameter[3].getValue()).booleanValue()) {                Logger.getLogger(AddBasicBetweenessCentrality.class.getName()).log(Level.INFO,"Normalizing Betweeness values");                double norm = 0.0;                for (int i = 0; i < betweeness.length; ++i) {                    norm += betweeness[i] * betweeness[i];                }                for (int i = 0; i < betweeness.length; ++i) {                    betweeness[i] /= Math.sqrt(norm);                }            }            for (int i = 0; i < betweeness.length; ++i) {                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) parameter[1].getValue() + " Betweeness");                Property prop = PropertyFactory.newInstance().create(props);                prop.add(new Double(betweeness[i]));                userList[i].add(prop);                if (betweeness[i] > maxBetweeness) {                    maxBetweeness = betweeness[i];                }            }        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddBasicBetweenessCentrality.class.getName()).log(Level.SEVERE, "Property class of property "+(String) parameter[1].getValue()+" Betweeness does match java.lang.Double", ex);        }    }    /**     * Calculates mean betweeness as a graph property.     *      * @param g graph to be modified     */    public void calculateGraphBetweeness(Graph g) {        try {            double between = 0.0;            for (int i = 0; i < betweeness.length; ++i) {                between += maxBetweeness - betweeness[i];            }            between *= 2;            between /= (betweeness.length - 1) * (betweeness.length - 1) * (betweeness.length - 2);            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String) parameter[1].getValue() + " Betweeness");            Property prop = PropertyFactory.newInstance().create(props);            prop.add(new Double(between));            g.add(prop);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddBasicBetweenessCentrality.class.getName()).log(Level.SEVERE, "Property class of property "+(String) parameter[1].getValue()+" Betweeness does match java.lang.Double", ex);        }    }    /**     * Calculates SD of betweeness as a graph property.     *      * @param g graph to be modified     */    public void calculateBetweenessSD(Graph g) {        try {            double squareSum = 0.0;            double sum = 0.0;            double sd = 0.0;            for (int i = 0; i < betweeness.length; ++i) {                squareSum += betweeness[i] * betweeness[i];                sum += betweeness[i];            }            sd = betweeness.length * squareSum - sum * sum;            sd /= betweeness.length;            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String) parameter[1].getValue() + " BetweenessSD");            Property prop = PropertyFactory.newInstance().create(props);            prop.add(new Double(sd));            g.add(prop);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddBasicBetweenessCentrality.class.getName()).log(Level.SEVERE, "Property class of property "+(String) parameter[1].getValue()+" BetweenessSD does match java.lang.Double", ex);        }    }    /**     * total sum of the number of times a node appears in a geodesic between two actors     *     * @param i index fo the source actor     * @param j index of the destination actor     * @param pathSet set of all geodesic paths     */    protected void sumPath(int i, int j, PathSet pathSet) {        Path[] paths = pathSet.getPath(userList[i].getID(), userList[j].getID());        if (paths != null) {            for (int k = 0; k < paths.length; ++k) {                Actor[] actorList = paths[k].getMiddle();                for (int l = 0; l < actorList.length; ++l) {                    betweeness[map.get(actorList[l])] += 1.0;                }            }        }    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    /**     * Initializes this algorithm.     *      * Parameters are as follows:     * <ul><li>'name' - (String) name of this algorithm in the application.  All applications     * must have a unique ID so this may change. Default is 'Basic Betweeness'     * <li>'relation' - (String) name of the type (relation) of links to use. Default is 'Knows'     * <li>'actorType' - (String) name of the type (mode) of Actor to use.  Default is 'User'     * <li>'normalize' - (Boolean) should Actor betweeness be nromalized so the sum of squares     * across acll actors of type relation is 1. Default is 'false'     * </ul>     * <br>     * Input - this algorithm consumes a Path object.     * <br>Output - this algorithm produces an Actor property and two Graph properties.     */    public void init(Properties map) {        // Construct Properties        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Basic Betweeness");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actor type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // Parameter 3 - normalize        props.setProperty("Type", "java.lang.Boolean");        props.setProperty("Name", "normalize");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[3] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("normalize") != null)) {            parameter[3].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("normalize"))));        } else {            parameter[3].setValue(false);        }        // Construct input descriptors        props.setProperty("Type", "Path");        props.setProperty("Relation", "Directional Geodesic By " + (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // Construct Output Descriptors        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Betweeness");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " BetweenessSD");        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Betweeness");        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}