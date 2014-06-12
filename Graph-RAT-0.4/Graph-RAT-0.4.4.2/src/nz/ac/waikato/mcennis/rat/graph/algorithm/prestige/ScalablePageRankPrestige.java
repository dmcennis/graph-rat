/* * AddPageRankPrestige.java * * Created on 27 June 2007, 19:06 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;import nz.ac.waikato.mcennis.rat.graph.algorithm.*;import cern.colt.matrix.DoubleFactory1D;import cern.colt.matrix.DoubleFactory2D;import cern.colt.matrix.DoubleMatrix1D;import cern.colt.matrix.DoubleMatrix2D;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.actor.Actor;import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptorInternal;import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptorInternal;import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;import nz.ac.waikato.mcennis.rat.graph.link.Link;import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;import nz.ac.waikato.mcennis.rat.graph.property.Property;import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;/** * Class for calculating the PageRank of an algorithm using the Power method.   * Algorithm is implemented using the Colt library as defined in Langville and Meyer 2003. * <br> * <br> * Langeville, A., C. Meyer. 2003. "Deeper inside Page Rank." <i>Internet Mathematics</i>. * 1(3):335--380. * * @author Daniel McEnnis *  */public class ScalablePageRankPrestige extends ModelShell implements Algorithm {    ParameterInternal[] parameter = new ParameterInternal[6];    InputDescriptorInternal[] input = new InputDescriptorInternal[1];    OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];    /** Creates a new instance of AddPageRankPrestige */    public ScalablePageRankPrestige() {        init(null);    }    @Override    public InputDescriptor[] getInputType() {        return input;    }    @Override    public OutputDescriptor[] getOutputType() {        return output;    }    @Override    public Parameter[] getParameter() {        return parameter;    }    @Override    public Parameter getParameter(String param) {        for (int i = 0; i < parameter.length; ++i) {            if (parameter[i].getName().contentEquals(param)) {                return parameter[i];            }        }        return null;    }    /**     *      * Implements the power version of the Page Rank algorithm using the colt library.     * The algorithm terminates after sum of changes between iterations are less than the     * tolerance.      */    public void execute(Graph g) {        try {            fireChange(Scheduler.SET_ALGORITHM_COUNT, 3);            Actor[] a = g.getActor((String) parameter[2].getValue());            if (a != null) {                java.util.Arrays.sort(a);                cern.colt.matrix.DoubleMatrix2D links = DoubleFactory2D.sparse.make(a.length + 1, a.length + 1);                links.assign(0.0);//        Link[] l = g.getLink((String)parameter[2].getValue());//        for(int i=0;i<l.length;++i){//            int source = java.util.Arrays.binarySearch(a,l[i].getSource());//            links.set(source,dest,0.15);//        }                for (int i = 0; i < a.length; ++i) {                    Link[] l = g.getLinkBySource((String) parameter[1].getValue(), a[i]);                    if (l != null) {                        for (int j = 0; j < l.length; ++j) {                            int dest = java.util.Arrays.binarySearch(a, l[j].getDestination());                            links.set(i, dest, (1.0 - ((Double) parameter[4].getValue()).doubleValue()) / ((double) l.length));                        }                    } else {                        Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.WARNING, "User " + a[i].getID() + " has no outgoing links");                    }                }                for (int i = 0; i < links.columns(); ++i) {                    links.set(i, a.length, ((Double) parameter[4].getValue()).doubleValue());                    links.set(a.length, i, 1.0 / ((double) a.length));                }                DoubleMatrix2D eigenVector = DoubleFactory2D.dense.make(1, a.length + 1);                DoubleMatrix2D oldEigenVector = DoubleFactory2D.dense.make(1, a.length + 1);                DoubleMatrix2D temp = null;                eigenVector.assign(1.0);                oldEigenVector.assign(1.0);                double tolerance = ((Double) parameter[5].getValue()).doubleValue();                oldEigenVector.zMult(links, eigenVector);                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 1);                while (error(eigenVector, oldEigenVector) > tolerance) {                    temp = oldEigenVector;                    oldEigenVector = eigenVector;                    eigenVector = temp;                    oldEigenVector.zMult(links, eigenVector);                }                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, 2);                DoubleMatrix1D pageRank = DoubleFactory1D.dense.make(a.length);                for (int i = 0; i < pageRank.size(); ++i) {                    pageRank.set(i, eigenVector.get(0, i));                }                cern.colt.matrix.linalg.Algebra base = new cern.colt.matrix.linalg.Algebra();                double norm = base.norm2(pageRank);                for (int i = 0; i < pageRank.size(); ++i) {                    pageRank.set(i, pageRank.get(i) / Math.sqrt(norm));                }                Properties props = new Properties();                double minPageRank = Double.POSITIVE_INFINITY;                double maxPageRank = Double.NEGATIVE_INFINITY;                for (int i = 0; i < eigenVector.size() - 1; ++i) {                    Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, "pageRank - " + a[i].getID() + ":" + pageRank.get(i));                    props.setProperty("PropertyType", "Basic");                    props.setProperty("PropertyClass", "java.lang.Double");                    props.setProperty("PropertyID", (String) parameter[3].getValue());                    Property rank = PropertyFactory.newInstance().create(props);                    rank.add(new Double(pageRank.get(i)));                    Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, Double.toString(pageRank.get(i)));                    a[i].add(rank);                    if (pageRank.get(i) < minPageRank) {                        minPageRank = pageRank.get(i);                    }                    if (pageRank.get(i) > maxPageRank) {                        maxPageRank = pageRank.get(i);                    }                }                Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, "MAX PageRank :" + maxPageRank);                Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.FINE, "MIN PageRank :" + minPageRank);            } else {                Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.WARNING, "No actors found of type '" + parameter[2].getValue() + "'");            }        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(ScalablePageRankPrestige.class.getName()).log(Level.SEVERE, "Property class does not match java.lang.Double", ex);        }    }    @Override    public SettableParameter[] getSettableParameter() {        return new SettableParameter[]{parameter[4]};    }    @Override    public SettableParameter getSettableParameter(String param) {        if (parameter[4].getName().contentEquals(param)) {            return parameter[4];        } else {            return null;        }    }    /**     *      * Parameters to be initialized:     *      * <ol>     * <li>'name' - Name for this instance of the algorithm. Default is 'Page Rank Centrality'.     * <li>'relation' - type (relation) of link that is calculated over. Deafult 'Knows'.     * <li>'actorSourceType' - type (mode) of actor that is calculated over. Default 'User'.     * <li>'propertyName' - name for the property added to actors. Deafult 'Knows PageRank Centrality'.     * <li>'teleportationFactor' - double for the percent chance of moving to a      * random page. Deafult '0.15'.     * <li>'tolerance' - maximum difference between sum of error before algorithm      * is considered converged.  Default is '1e-50'.     * </ol>     * <br>     * <br>Input 0 - Link     * <br>Output 0 - ActorProperty     */    public void init(Properties map) {        Properties props = new Properties();        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "name");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[0] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("name") != null)) {            parameter[0].setValue(map.getProperty("name"));        } else {            parameter[0].setValue("Page Rank Centrality");        }        // Parameter 1 - relation        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "relation");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[1] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("relation") != null)) {            parameter[1].setValue(map.getProperty("relation"));        } else {            parameter[1].setValue("Knows");        }        // Parameter 2 - actor source type        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "actorSourceType");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[2] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("actorSourceType") != null)) {            parameter[2].setValue(map.getProperty("actorSourceType"));        } else {            parameter[2].setValue("User");        }        // Parameter 3 - propertyName        props.setProperty("Type", "java.lang.String");        props.setProperty("Name", "propertyName");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[3] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("propertyName") != null)) {            parameter[3].setValue(map.getProperty("propertyName"));        } else {            parameter[3].setValue("Knows PageRank Centrality");        }        // Parameter 4 - teleportation factor        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "teleportationFactor");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[4] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("teleportationFactor") != null)) {            parameter[4].setValue(new Double(Double.parseDouble(map.getProperty("teleportationFactor"))));        } else {            parameter[4].setValue(new Double(0.15));        }        // Parameter 5 - tolerance factor        props.setProperty("Type", "java.lang.Double");        props.setProperty("Name", "tolerance");        props.setProperty("Class", "Basic");        props.setProperty("Structural", "true");        parameter[5] = DescriptorFactory.newInstance().createParameter(props);        if ((map != null) && (map.getProperty("tolerance") != null)) {            parameter[5].setValue(new Double(Double.parseDouble(map.getProperty("tolerance"))));        } else {            parameter[5].setValue(new Double(1e-50));        }        // input 0        props.setProperty("Type", "Link");        props.setProperty("Relation", (String) parameter[1].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.remove("Property");        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);        // output 0        props.setProperty("Type", "ActorProperty");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[3].getValue());        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }    protected double error(DoubleMatrix2D newVector, DoubleMatrix2D oldVector) {        double ret = 0.0;        for (int i = 0; i < newVector.size(); ++i) {            ret += Math.abs(newVector.get(0, i) - oldVector.get(0, i));        }        return ret;    }}