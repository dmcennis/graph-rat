/* * AddDegreeGraphProperties.java * * Created on 6 July 2007, 12:49 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.InputDescriptorInternal;import org.dynamicfactory.descriptors.OutputDescriptorInternal;import org.dynamicfactory.descriptors.Parameter;import org.dynamicfactory.descriptors.ParameterInternal;import org.dynamicfactory.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.link.Link;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import org.dynamicfactory.property.InvalidObjectTypeException;import org.dynamicfactory.property.Property;import org.dynamicfactory.property.PropertyFactory;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Add basic degree-based properties for actors and graphs. * @author Daniel McEnnis *  */public class AddDegreeGraphProperties extends ModelShell implements Algorithm {    public static final long serialVersionUID = 2;    ParameterInternal[] parameter = new ParameterInternal[3];    InputDescriptorInternal[] input = new InputDescriptorInternal[1];    OutputDescriptorInternal[] output = new OutputDescriptorInternal[4];//    String relation;//    String[] actors;    /** Creates a new instance of AddDegreeGraphProperties */    public AddDegreeGraphProperties() {        init(null);    }//    public void setRelation(String r){//        relation = r;//    }////    public String getRelation(){//        return relation;//    }////    public String[] getActors(){//        return actors;//    }////    public void setActors(String[] a){//        actors = a;//    }//    @Override    public void execute(Graph g) {        calculateInDegree(g);        calculateOutDegree(g);        calculateDensity(g);    }    /**     * Calculate the in-degree of each node.     *      * @param g the graph to be modified     */    public void calculateInDegree(Graph g) {        Actor[] baseActor = g.getActor();        if (baseActor != null) {        fireChange(Scheduler.SET_ALGORITHM_COUNT,baseActor.length*2+1);            for (int i = 0; i < baseActor.length; ++i) {                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) parameter[1].getValue() + " InDegree");                Property inDegree = PropertyFactory.newInstance().create(props);                Link[] links = g.getLinkByDestination((String) parameter[1].getValue(), baseActor[i]);                if (links == null) {                    try {                        inDegree.add(new Double(0.0));                    } catch (InvalidObjectTypeException ex) {                        Logger.getLogger(AddDegreeGraphProperties.class.getName()).log(Level.SEVERE, "Property type java.lang.Double does match type '"+inDegree.getPropertyClass().getName()+"'", ex);                    }                } else {                    try {                        double inDegreeStrength = 0.0;                        for (int j = 0; j < links.length; ++j) {                            inDegreeStrength += links[j].getStrength();                        }                        inDegree.add(new Double(inDegreeStrength));                    } catch (InvalidObjectTypeException ex) {                        Logger.getLogger(AddDegreeGraphProperties.class.getName()).log(Level.SEVERE,"Property type java.lang.Double does match type '"+inDegree.getPropertyClass().getName()+"'",ex);                    }                }                baseActor[i].add(inDegree);                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);            }        }    }    /**     * Calculate the out-degree of each node.     *      * @param g the graph to be modified     */    public void calculateOutDegree(Graph g) {        Actor[] baseActor = getActor(g);        if (baseActor != null) {            for (int i = 0; i < baseActor.length; ++i) {                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) parameter[1].getValue() + " OutDegree");                Property inDegree = PropertyFactory.newInstance().create(props);                Link[] links = g.getLinkBySource((String) parameter[1].getValue(), baseActor[i]);                if (links == null) {                    try {                        inDegree.add(new Double(0.0));                    } catch (InvalidObjectTypeException ex) {                        Logger.getLogger(AddDegreeGraphProperties.class.getName()).log(Level.SEVERE,"Property type java.lang.Double does match type '"+inDegree.getPropertyClass().getName()+"'",ex);                    }                } else {                    try {                        double outDegreeStrength = 0.0;                        for (int j = 0; j < links.length; ++j) {                            outDegreeStrength += links[j].getStrength();                        }                        inDegree.add(new Double(outDegreeStrength));                    } catch (InvalidObjectTypeException ex) {                        Logger.getLogger(AddDegreeGraphProperties.class.getName()).log(Level.SEVERE,"Property type java.lang.Double does match type '"+inDegree.getPropertyClass().getName()+"'",ex);                    }                }                baseActor[i].add(inDegree);                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,baseActor.length+i);            }        }    }    /**     * Calculate the density of the graph using the absolute value of all link strengths     *      * @param g the graph to be modified     */    public void calculateAbsoluteDensity(Graph g) {        Actor[] actor = getActor(g);        if (actor != null) {            int actorCount = actor.length;            Link[] links = g.getLink((String) parameter[1].getValue());            if (links != null) {                Property density = null;                try {                    double linkStrength = 0.0;                    for (int i = 0; i < links.length; ++i) {                        linkStrength += Math.abs(links[i].getStrength());                    }                    linkStrength /= actorCount * (actorCount - 1);                    java.util.Properties props = new java.util.Properties();                    props.setProperty("PropertyType", "Basic");                    props.setProperty("PropertyClass", "java.lang.Double");                    props.setProperty("PropertyID", (String) parameter[1].getValue() + " Absolute Density");                    density = PropertyFactory.newInstance().create(props);                    density.add(new Double(linkStrength));                    g.add(density);                } catch (InvalidObjectTypeException ex) {                    if(density != null){                        Logger.getLogger(AddDegreeGraphProperties.class.getName()).log(Level.SEVERE,"Property type java.lang.Double does match type '"+density.getPropertyClass().getName()+"'",ex);                    }                }            }        }    }    /**     * Calculate the density of the graph     *      * @param g the graph to be modified     */    public void calculateDensity(Graph g) {        Actor[] actor = getActor(g);        if (actor != null) {            int actorCount = actor.length;            Link[] links = g.getLink((String) parameter[1].getValue());            if (links != null) {                try {                    double linkStrength = 0.0;                    for (int i = 0; i < links.length; ++i) {                        linkStrength += links[i].getStrength();                    }                    linkStrength /= actorCount * (actorCount - 1);                    java.util.Properties props = new java.util.Properties();                    props.setProperty("PropertyType", "Basic");                    props.setProperty("PropertyID", (String) parameter[1].getValue() + " Density");                    Property density = PropertyFactory.newInstance().create(props);                    density.add(new Double(linkStrength));                    g.add(density);                } catch (InvalidObjectTypeException ex) {                    Logger.getLogger(AddDegreeGraphProperties.class.getName()).log(Level.SEVERE, null, ex);                }            }        }    }    protected Actor[] getActor(Graph g) {        java.util.Vector<Actor> actor = new java.util.Vector<Actor>();        if (((String) parameter[2].getValue()).contentEquals("*")) {            for (int i = 0; i < g.getActorTypes().length; ++i) {                Actor[] temp = g.getActor(g.getActorTypes()[i]);                if (temp != null) {                    for (int j = 0; j < temp.length; ++j) {                        actor.add(temp[j]);                    }                }            }            return actor.toArray(new Actor[]{});        } else {            return g.getActor((String) parameter[2].getValue());        }    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    /**     * Parameters for initialization of this algorithm     * <br>     * <ol>     * <li>'name' - name of this isntance. Default is 'Degree Graph Properties'.     * <li>'relation' - type (relation) of link to use.  Default is 'Knows'.     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.     * </ol>     * <br>     * <br>Input 1 - Link     * <br>Output 1 - Actor Property     * <br>Output 2 - Actor Property     * <br>Output 3 - Graph Property     * <br>Output 4 - Graph Property     */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Degree Graph Properties");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actor type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // Construct input descriptors        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // Construct Output Descriptors        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " InDegree");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " OutDegree");        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Absolute Density");        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Density");        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}