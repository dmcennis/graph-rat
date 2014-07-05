/* * AddMusicLinks.java * * Created on 12 June 2007, 21:37 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm;import java.util.Properties;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import org.dynamicfactory.descriptors.DescriptorFactory;import org.dynamicfactory.descriptors.InputDescriptor;import org.dynamicfactory.descriptors.InputDescriptorInternal;import org.dynamicfactory.descriptors.OutputDescriptor;import org.dynamicfactory.descriptors.OutputDescriptorInternal;import org.dynamicfactory.descriptors.Parameter;import org.dynamicfactory.descriptors.ParameterInternal;import org.dynamicfactory.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;import nz.ac.waikato.mcennis.rat.graph.link.Link;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import org.dynamicfactory.property.Property;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Takes set of artist links and creates music userlinks between users based on * the degree of similarity in musical tastes.  Note that this weight only occurs * between users that are connected by a 'knows' link and has the direction of the * knows link.  Links are not bidirectional if the knows link is not bidrectional. * Weights differ between directions since the negative is not the cumalitive * negative between both, but the unmatched on the source side of the link. * * @author Daniel McEnnis * */public class AddMusicLinks extends ModelShell implements Algorithm {    public static final long serialVersionUID = 2;    private ParameterInternal[] parameter = new ParameterInternal[10];    private InputDescriptorInternal[] input = new InputDescriptorInternal[3];    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];    /**     * Sum_x^# matching artists(positiveBase*(postiveRate)^x     *///    private double positiveBase=2.0;    /**     * Sum_x^# unmatched artists (positiveBase*(postiveRate)^x     *///    private double positiveRate=2.0;    /**     * Sum_x^# unmatched artists(negativeBase*(negativeRate)^x     *///    private double negativeBase=0.25;    /**     * Sum_x^# unmatched artists(negativeBase*(negativeRate)^x     *///    private double negativeRate=1.1;    /**     * variable holding the weight for the source to destination link     */    private double leftCount = 0.0;    /**     *variable holding weight for the destination to the source music link     */    private double rightCount = 0.0;    /** Creates a new instance of AddMusicLinks */    public AddMusicLinks() {        init(null);    }    /**     * Execute the algorithm against the current graph     *      * @param g graph to be modified     */    public void execute(Graph g) {        java.util.Iterator<Actor> list = g.getActorIterator((String)parameter[2].getValue());        fireChange(Scheduler.SET_ALGORITHM_COUNT,g.getActorCount((String)parameter[2].getValue()));        int actorCount = 0;        while (list.hasNext()) {            fireChange(Scheduler.SET_ALGORITHM_PROGRESS,actorCount++);            Actor userList = list.next();            Link[] leftArtist = g.getLinkBySource((String)parameter[3].getValue(), userList);            if (leftArtist != null) {                Link[] links = g.getLinkBySource((String)parameter[1].getValue(), userList);                if (links != null) {                    for (int j = 0; j < links.length; ++j) {                        Actor dest = links[j].getDestination();                        Link[] rightArtist = g.getLinkBySource((String)parameter[3].getValue(), dest);                        if (rightArtist != null) {                            compareInterests(leftArtist, rightArtist, getBetweeness(dest));//                        if(leftCount>2.0){                            java.util.Properties props = new java.util.Properties();                            props.setProperty("LinkType", (String)parameter[4].getValue());                            Link ul = LinkFactory.newInstance().create(props);                            ul.set(userList, leftCount, dest);                            g.add(ul);//                        }                        }                    }                }            }        }    }    /**     * Exponential similarity measure between two users.  All words are binary on-off     *      * Algorithm:     *      * positiveBase*(1+positiveExponent*actorBetweeness)^(#shared Interest)     * - negativeBase*(1+negativeExponent*actorBetweeness)^(left actor # interest - #shared Interest)     *      *     * @param left source of link     * @param right destination of link     * @param between betweeness value of destination     */    public void compareInterests(Link[] left, Link[] right, double between) {        int positiveCount = 0;        int negativeCount = 0;        leftCount = 0.0;        rightCount = 0.0;        String[] leftNames = new String[left.length];        String[] rightNames = new String[right.length];        for (int i = 0; i < left.length; ++i) {            leftNames[i] = left[i].getDestination().getID();        }        for (int i = 0; i < right.length; ++i) {            rightNames[i] = right[i].getDestination().getID();        }        java.util.Arrays.sort(leftNames);        java.util.Arrays.sort(rightNames);        int i = 0;        int j = 0;        while ((i < leftNames.length) && (j < rightNames.length)) {            if (leftNames[i].compareTo(rightNames[j]) > 0) {                ++j;            } else if (leftNames[i].compareTo(rightNames[j]) < 0) {                ++i;            } else {                positiveCount++;                ++i;                ++j;            }        }        for (i = 0; i < positiveCount; ++i) {            leftCount += (Double) parameter[6].getValue() * Math.pow(1 + (Double) parameter[7].getValue() * between, (double) i);        }        rightCount = leftCount;        for (i = 0; i < left.length - positiveCount; ++i) {            leftCount -= (Double) parameter[8].getValue() * Math.pow(1 + (Double) parameter[9].getValue() * between, (double) i);//            leftCount -= negativeBase*Math.pow(negativeRate,(double)i);        }        for (i = 0; i < right.length - positiveCount; ++i) {            rightCount -= (Double) parameter[8].getValue() * Math.pow(1 + (Double) parameter[9].getValue() * between, (double) i);//            rightCount -= negativeBase*Math.pow(negativeRate,(double)i);        }    }    double getBetweeness(Actor dest) {        Property property = dest.getProperty((String) parameter[5].getValue());        if ((property != null) && (property.getPropertyClass().getName().contentEquals("java.lang.Double"))) {            Object[] value = dest.getProperty((String) parameter[5].getValue()).getValue();            if (value != null) {                return ((Double) (value[0])).doubleValue();            }        }        return 1.0;    }//    public void setPositiveRate(double rate){//        positiveRate = rate;//    }////    public void setNegativeRate(double rate){//        negativeRate = rate;//    }////    public void setPositiveBase(double rate){//        positiveBase = rate;//    }////    public void setNegativeBase(double rate){//        negativeBase = rate;//    }////    public double getPositiveRate(){//        return positiveRate;//    }////    public double getNegativeRate(){//        return negativeRate;//    }////    public double getPositiveBase(){//        return positiveBase;//    }////    public double getNegativeBase(){//        return negativeBase;//    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    @Override    public SettableParameter[] getSettableParameter() {        return new SettableParameter[]{parameter[6], parameter[7], parameter[8], parameter[9]};    }    @Override    public SettableParameter getSettableParameter(String param) {        for (int i = 6; i < 10; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    /**     * Parameters for initialization of this algorithm     * <br>     * <ol>     * <li>'name' - name of this isntance. Default is 'Basic Music Link'.     * <li>'relation' - type (relation) of link to use.  Default is 'Knows'.     * <li>'actorType' - type (mode) of actor to use. Default is 'User'.     * <li>'dataLinkType' - type (relation) of link representing existing musical      * tastes. Default is 'Given'.     * <li>'outputLinkType'- type (relation) of link created. Default is 'Music'.     * <li>'adjustorPropertyType' - name of the property type where prestige is stored. Default is 'Knows Betweeness'.     * <li>'positiveBase' - see compareInterests. Default is '1.0'.     * <li>'positiveExponent' - see compareInterests. Default is '0.5'.     * <li>'negativeBase' - see compareInterests. Default is '0.5'.     * <li>'negativeExponent' - see compareInterests Default is '0.3'.     * </ol>     * <br>     * <br>Input 1 - Link     * <br>Input 2 - Link     * <br>Input 3 - Actor Property     * <br>Output 1 - Link     *      */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Basic Music Link");        }        // Parameter 1 - relation        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actor type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorType") != null)) {            parameter[2].setValue(map.get("actorType"));        } else {            parameter[2].setValue("User");        }        // Parameter 3 - property type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "dataLinkType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[3] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("dataLinkType") != null)) {            parameter[3].setValue(map.getProperty("dataLinkType"));        } else {            parameter[3].setValue("Given");        }        // Parameter 4 - output property type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "outputLinkType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[4] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("outputLinkType") != null)) {            parameter[4].setValue(map.getProperty("outputLinkType"));        } else {            parameter[4].setValue("Music");        }        // Parameter 5 - adjustor property type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "adjustorPropertyType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[5] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("adjustorPropertyType") != null)) {            parameter[5].setValue(map.getProperty("adjustorPropertyType"));        } else {            parameter[5].setValue("Knows Betweeness");        }        // Parameter 6 - positive base        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "positiveBase");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[6] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("positiveBase") != null)) {            parameter[6].setValue(Double.parseDouble(map.getProperty("positiveBase")));        } else {            parameter[6].setValue(new Double(1.0));        }        // Parameter 7 - positive exponent        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "positiveExponent");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[7] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("positiveExponent") != null)) {            parameter[7].setValue(Double.parseDouble(map.getProperty("positiveExponent")));        } else {            parameter[7].setValue(new Double(0.5));        }        // Parameter 8 - negative base        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "negativeBase");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[8] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("negativeBase") != null)) {            parameter[8].setValue(Double.parseDouble(map.getProperty("negativeBase")));        } else {            parameter[8].setValue(new Double(0.5));        }        // Parameter 9 - negative exponent        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "negativeExponent");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "false");        parameter[9] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("negativeExponent") != null)) {            parameter[9].setValue(Double.parseDouble(map.getProperty("negativeExponent")));        } else {            parameter[9].setValue(new Double(0.3));        }        // init input 0        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init input 1        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[3].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init input 2        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[5].getValue());        input[2] = DescriptorFactory.newInstance().createInputDescriptor(props);        // init output 0        // Construct Output Descriptors        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[4].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}