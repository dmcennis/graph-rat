/* * AddDegreeCentrality.java * * Created on 27 June 2007, 18:43 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;import nz.ac.waikato.mcennis.rat.graph.algorithm.*;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptorInternal;import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptorInternal;import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;import nz.ac.waikato.mcennis.rat.graph.property.Property;import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Calculates centrality and prestige based on actor degree. see Freeman79 * <br> * Freeman, L. "Centrality in social networks: I. Conceptual clarification." * <i>Social Networks</i>. 1:215--239. * <br> * @author Daniel McEnnis *  */public class AddDegreeCentrality extends ModelShell implements Algorithm {    private ParameterInternal[] parameter = new ParameterInternal[4];    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[6];    private InputDescriptorInternal[] input = new InputDescriptorInternal[3];//    private String[] actorList;    private double prestigeMaxDegree = Double.NEGATIVE_INFINITY;    private double[] centralityValue;    private double[] prestigeValue;    private double centralityMaxDegree = Double.NEGATIVE_INFINITY;    public static final long serialVersionUID = 2;    int actorCount=0;    /** Creates a new instance of addDegreeCentrality */    public AddDegreeCentrality() {        init(null);    }    @Override    public void execute(Graph g) {        fireChange(Scheduler.SET_ALGORITHM_COUNT,getActor(g).length*2+4);        actorCount=0;        calculatePrestige(g);        calculateCentrality(g);        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);        calculateGraphPrestige(g);        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);        calculateGraphCentrality(g);        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);        calculateGraphPrestigeSD(g);        fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);        calculateGraphCentralitySD(g);        centralityValue = null;        prestigeValue = null;    }    /**     * Calaculate In-Degree of each actor     *      * @param g graph to be modified     */    public void calculatePrestige(Graph g) {        try {            Actor[] userList = getActor(g);            prestigeValue = new double[userList.length];            for (int i = 0; i < userList.length; ++i) {                Property inDegree = userList[i].getProperty((String) parameter[1].getValue() + " InDegree");                double value = ((Double) inDegree.getValue()[0]).doubleValue() / (userList.length - 1);                if (value > prestigeMaxDegree) {                    prestigeMaxDegree = value;                }                prestigeValue[i] = value;                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);            }            if (((Boolean) parameter[3].getValue()).booleanValue()) {                double norm = 0.0;                for (int i = 0; i < prestigeValue.length; ++i) {                    norm += prestigeValue[i] * prestigeValue[i];                }                for (int i = 0; i < prestigeValue.length; ++i) {                    prestigeValue[i] /= Math.sqrt(norm);                }            }            for (int i = 0; i < userList.length; ++i) {                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) parameter[1].getValue() + " DegreePrestige");                Property prestige = PropertyFactory.newInstance().create(props);                prestige.add(new Double(prestigeValue[i]));                userList[i].add(prestige);            }        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddDegreeCentrality.class.getName()).log(Level.SEVERE, "Property class of "+(String) parameter[1].getValue()+" DegreePrestige does not match java.lang.Double", ex);        }    }    /**     * Calaculate Out-Degree of each actor     *      * @param g graph to be modified     */    public void calculateCentrality(Graph g) {        try {            Actor[] userList = getActor(g);            centralityValue = new double[userList.length];            for (int i = 0; i < userList.length; ++i) {                Property outDegree = userList[i].getProperty((String) parameter[1].getValue() + " OutDegree");                double value = ((Double) outDegree.getValue()[0]).doubleValue() / (userList.length - 1);                if (value > centralityMaxDegree) {                    centralityMaxDegree = value;                }                centralityValue[i] = value;                fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);            }            if (((Boolean) parameter[3].getValue()).booleanValue()) {                double norm = 0.0;                for (int i = 0; i < centralityValue.length; ++i) {                    norm += centralityValue[i] * centralityValue[i];                }                for (int i = 0; i < centralityValue.length; ++i) {                    centralityValue[i] /= Math.sqrt(norm);                }            }            for (int i = 0; i < userList.length; ++i) {                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) parameter[1].getValue() + " DegreeCentrality");                Property centrality = PropertyFactory.newInstance().create(props);                centrality.add(new Double(centralityValue[i]));                userList[i].add(centrality);            }        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddDegreeCentrality.class.getName()).log(Level.SEVERE, "Property class of "+(String) parameter[1].getValue()+" DegreeCentrality does not match java.lang.Double", ex);        }    }    /**     * Calculate mean Degree Centrality as given by Freeman79     *      * @param g graph to be modified     */    public void calculateGraphCentrality(Graph g) {        try {            double centrality = 0.0;            for (int i = 0; i < centralityValue.length; ++i) {                centrality += centralityMaxDegree - centralityValue[i];            }            centrality /= ((centralityValue.length - 1) * (centralityValue.length - 2));            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String) parameter[1].getValue() + " DegreeCentrality");            Property degreeAverage = PropertyFactory.newInstance().create(props);            degreeAverage.add(new Double(centrality));            g.add(degreeAverage);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddDegreeCentrality.class.getName()).log(Level.SEVERE, "Property class of "+(String) parameter[1].getValue()+" DegreeCentrality does not match java.lang.Double", ex);        }    }    /**     * Calculate mean Degree Prestige as given by Freeman79     *      * @param g graph to be modified     */    public void calculateGraphPrestige(Graph g) {        try {            double prestige = 0.0;            for (int i = 0; i < centralityValue.length; ++i) {                prestige += prestigeMaxDegree - prestigeValue[i];            }            prestige /= ((prestigeValue.length - 1) * (prestigeValue.length - 2));            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String) parameter[1].getValue() + " DegreePrestige");            Property degreeAverage = PropertyFactory.newInstance().create(props);            degreeAverage.add(new Double(prestige));            g.add(degreeAverage);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddDegreeCentrality.class.getName()).log(Level.SEVERE, "Property class of "+(String) parameter[1].getValue()+" DegreePrestige does not match java.lang.Double", ex);        }    }    /**     * Calculate standard deviation of Degree Centrality as given by Freeman79     *      * @param g graph to be modified     */    public void calculateGraphCentralitySD(Graph g) {        try {            double sd = 0.0;            double centralitySquare = 0.0;            double centralitySum = 0.0;            for (int i = 0; i < centralityValue.length; ++i) {                centralitySquare += centralityValue[i] * centralityValue[i];                centralitySum += centralityValue[i];            }            sd = centralityValue.length * centralitySquare - centralitySum * centralitySum;            sd /= centralityValue.length;            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String)parameter[1].getValue()+" DegreeCentralitySD");            Property prop = PropertyFactory.newInstance().create(props);            prop.add(new Double(sd));            g.add(prop);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddDegreeCentrality.class.getName()).log(Level.SEVERE, "Property class of "+(String) parameter[1].getValue()+" DegreeCentralitySD does not match java.lang.Double", ex);        }    }    /**     * Calculate standard deviation of Degree Prestige as given by Freeman79     *      * @param g graph to be modified     */    public void calculateGraphPrestigeSD(Graph g) {        try {            double sd = 0.0;            double prestigeSquare = 0.0;            double prestigeSum = 0.0;            for (int i = 0; i < centralityValue.length; ++i) {                prestigeSquare += prestigeValue[i] * prestigeValue[i];                prestigeSum += prestigeValue[i];            }            sd = prestigeValue.length * prestigeSquare - prestigeSum * prestigeSum;            sd /= prestigeValue.length;            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String)parameter[1].getValue()+" DegreePrestigeSD");            Property prop = PropertyFactory.newInstance().create(props);            prop.add(new Double(sd));            g.add(prop);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(AddDegreeCentrality.class.getName()).log(Level.SEVERE, "Property class of "+(String) parameter[1].getValue()+" DegreePrestigeSD does not match java.lang.Double", ex);        }    }//    public void setActor(String[] actor){//        actorList = actor;//    }//    //    public String[] getActor(){//        return actorList;//    }//        protected Actor[] getActor(Graph g) {        java.util.Vector<Actor> actor = new java.util.Vector<Actor>();        if (((String) parameter[2].getValue()).contentEquals("*")) {            for (int i = 0; i < g.getActorTypes().length; ++i) {                Actor[] temp = g.getActor(g.getActorTypes()[i]);                if (temp != null) {                    for (int j = 0; j < temp.length; ++j) {                        actor.add(temp[j]);                    }                }            }        } else {            return g.getActor((String) parameter[2].getValue());        }        return actor.toArray(new Actor[]{});    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return null;    }    @Override    public SettableParameter getSettableParameter(String param) {        return null;    }    /**     * Parameters for initialization     * <br>     * <ol>     * <li>'name' - name of this isntance. Default is 'Degree Centrality'.     * <li>'relation' - type (relation) of link to use.  Default is 'Knows'.     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.     * <li>'normalize' - should the sum of squares of all entries be normalized to 1. Default is 'false'.     * </ol>     * <br>     * <br>Input 1 - Link     * <br>Output 1 - ActorProperty     * <br>Output 2 - ActorProperty     * <br>Output 3 - GraphProperty     * <br>Output 4 - GraphProperty     * <br>Output 5 - GraphProperty     * <br>Output 6 - GraphProperty     */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Degree Centrality");        }        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actor type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.getProperty("actorType"));        } else {            parameter[2].setValue("User");        }        // Parameter  3 - normalize        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "normalize");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[3] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("normalize") != null)) {            parameter[3].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("normalize"))));        } else {            parameter[3].setValue(new Boolean(false));        }        // Construct input descriptors        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        props.setProperty("Type", "Actor");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " InDegree");        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);        props.setProperty("Type", "Actor");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " OutDegree");        input[2] = DescriptorFactory.newInstance().createInputDescriptor(props);        // Construct Output Descriptors        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " DegreeCentrality");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " DegreeCentralitySD");        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " DegreePrestige");        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " DegreePrestigeSD");        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "GraphProperty");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Centrality");        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Prestige");        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}