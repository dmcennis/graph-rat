/* * OptimizedBetweeness.java * * Created on 22 October 2007, 16:27 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;import nz.ac.waikato.mcennis.rat.graph.algorithm.*;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;import nz.ac.waikato.mcennis.rat.graph.path.PathNode;import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;import nz.ac.waikato.mcennis.rat.graph.property.Property;import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;/** * *  * Class built upon OptimizedPathBase that claculates Betweeness.  OptimizedPathBase * only records one geodesic path for each actor pair, otherwise same as the  * betweeness metric deefined in Freeman79. * <br> * <br> * Freeman, L. "Centrality in social networks: I. Conceptual clarification." * <i>Social Networks</i>. 1:215--239. * @author Daniel McEnnis */public class OptimizedBetweeness extends OptimizedPathBase implements Algorithm {    double[] betweeness = null;    double maxBetweeness = 0.0;    /** Creates a new instance of OptimizedBetweeness */    public OptimizedBetweeness() {        super();    }    protected void doCleanup(PathNode[] path, Graph g) {        try {            if (((Boolean) (super.getParameter())[3].getValue()).booleanValue()) {                Logger.getLogger(OptimizedBetweeness.class.getName()).log(Level.INFO, "Normalizing Betweeness values");                double norm = 0.0;                for (int i = 0; i < betweeness.length; ++i) {                    norm += betweeness[i] * betweeness[i];                }                for (int i = 0; i < betweeness.length; ++i) {                    betweeness[i] /= Math.sqrt(norm);                }            }            for (int i = 0; i < betweeness.length; ++i) {                java.util.Properties props = new java.util.Properties();                props.setProperty("PropertyType", "Basic");                props.setProperty("PropertyClass", "java.lang.Double");                props.setProperty("PropertyID", (String) (super.getParameter())[1].getValue() + " Betweeness");                Property prop = PropertyFactory.newInstance().create(props);                prop.add(new Double(betweeness[i]));                path[i].getActor().add(prop);                if (betweeness[i] > maxBetweeness) {                    maxBetweeness = betweeness[i];                }            }            calculateGraphBetweeness(g);            calculateBetweenessSD(g);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(OptimizedBetweeness.class.getName()).log(Level.SEVERE, "Property class of "+super.getParameter()[1].getValue()+" Betweeness does not match java.lang.Double", ex);        }    }    protected void doAnalysis(PathNode[] path, PathNode source) {        for (int i = 0; i < path.length; ++i) {            PathNode traversal = path[i].getPrevious();            while ((traversal != null) && (traversal.compareTo(source) != 0)) {                betweeness[traversal.getId()] += 1.0;                traversal = traversal.getPrevious();            }        }    }    protected void setSize(int size) {        betweeness = new double[size];        java.util.Arrays.fill(betweeness, 0.0);    }    protected void calculateGraphBetweeness(Graph g) {        try {            double between = 0.0;            for (int i = 0; i < betweeness.length; ++i) {                between += maxBetweeness - betweeness[i];            }            between *= 2;            between /= (betweeness.length - 1) * (betweeness.length - 1) * (betweeness.length - 2);            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String) (super.getParameter())[1].getValue() + " Betweeness");            Property prop = PropertyFactory.newInstance().create(props);            prop.add(new Double(between));            g.add(prop);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(OptimizedBetweeness.class.getName()).log(Level.SEVERE, "Property class of "+super.getParameter()[1].getValue()+" Betweeness does not match java.lang.Double", ex);        }    }    protected void calculateBetweenessSD(Graph g) {        try {            double squareSum = 0.0;            double sum = 0.0;            double sd = 0.0;            for (int i = 0; i < betweeness.length; ++i) {                squareSum += betweeness[i] * betweeness[i];                sum += betweeness[i];            }            sd = betweeness.length * squareSum - sum * sum;            sd /= betweeness.length;            java.util.Properties props = new java.util.Properties();            props.setProperty("PropertyType", "Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID", (String) (super.getParameter())[1].getValue() + " BetweenessSD");            Property prop = PropertyFactory.newInstance().create(props);            prop.add(new Double(sd));            g.add(prop);        } catch (InvalidObjectTypeException ex) {            Logger.getLogger(OptimizedBetweeness.class.getName()).log(Level.SEVERE, "Property class of "+super.getParameter()[1].getValue()+" BetweenessSD does not match java.lang.Double", ex);        }    }    protected void constructOutput(OutputDescriptor[] output, ParameterInternal[] parameter) {        if (((String) parameter[0].getValue()).contentEquals("")) {            parameter[0].setValue("Optimized Betweeness");        }        output = new OutputDescriptor[3];        // Construct Output Descriptors        Properties props = new Properties();        props.setProperty("Type", "Graph");        props.setProperty("Relation", "");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Betweeness");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "Graph");        props.remove("Relation");        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " BetweenessSD");        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);        props.setProperty("Type", "Actor");        props.setProperty("Relation", (String) parameter[2].getValue());        props.setProperty("AlgorithmName", (String) parameter[0].getValue());        props.setProperty("Property", (String) parameter[1].getValue() + " Betweeness");        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }}