/* * OptimizedCloseness.java * * Created on 22 October 2007, 15:13 * * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt) */package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;import nz.ac.waikato.mcennis.rat.graph.algorithm.*;import java.util.Properties;import java.util.logging.Level;import java.util.logging.Logger;import nz.ac.waikato.mcennis.rat.graph.Graph;import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;import nz.ac.waikato.mcennis.rat.graph.path.PathNode;import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;import nz.ac.waikato.mcennis.rat.graph.property.Property;import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;/** * *  * Class that calculates Closeness in O(n) space using OptimizedPathBase.  This  * implements Closeness as described in Freeman79. * <br> * <br> * Freeman, L. "Centrality in social networks: I. Conceptual clarification." * <i>Social Networks</i>. 1:215--239. * @author Daniel McEnnis */public class OptimizedCloseness extends OptimizedPathBase implements Algorithm{        double[] prestigeValue = null;    double[] centralityValue = null;    double maxPrestige = 0.0;    double maxCentrality = 0.0;        /** Creates a new instance of OptimizedCloseness */    public OptimizedCloseness() {        super();    }        protected void setSize(int size){        prestigeValue = new double[size];        centralityValue = new double[size];    }    protected void doAnalysis(PathNode[] path,PathNode source) {        for(int i=0;i<path.length;++i){            double cost = path[i].getCost();            if(Double.isInfinite(cost)){                cost = 0.0;            }            prestigeValue[path[i].getId()] += cost;            centralityValue[source.getId()] += cost;        }    }        protected void doCleanup(PathNode path[], Graph g){        try{        Properties props = new Properties();        for(int i=0;i<prestigeValue.length;++i){            if(prestigeValue[i]!=0){                prestigeValue[i] = 1/prestigeValue[i];                if(prestigeValue[i]>maxPrestige){                    maxPrestige = prestigeValue[i];                }            }else{                prestigeValue[i]=0.0;            }            if(centralityValue[i]!=0){                centralityValue[i] = 1/centralityValue[i];                if(centralityValue[i]>maxCentrality){                    maxCentrality = centralityValue[i];                }            }else{                centralityValue[i] = 0.0;            }                        if(maxPrestige < prestigeValue[i]){                maxPrestige = prestigeValue[i];            }            if(maxCentrality < centralityValue[i]){                maxCentrality = centralityValue[i];            }        }        // Normalize if necessary        if(((Boolean)(super.getParameter())[3].getValue()).booleanValue()){            double norm = 0.0;            for(int i=0;i<centralityValue.length;++i){                norm += centralityValue[i]*centralityValue[i];            }            for(int i=0;i<centralityValue.length;++i){                centralityValue[i] /= Math.sqrt(norm);            }        }                if(((Boolean)(super.getParameter())[3].getValue()).booleanValue()){            double norm = 0.0;            for(int i=0;i<prestigeValue.length;++i){                norm += prestigeValue[i]*prestigeValue[i];            }            for(int i=0;i<prestigeValue.length;++i){                prestigeValue[i] /= Math.sqrt(norm);            }        }                //create and set the properties defined        for(int i=0;i<prestigeValue.length;++i){            props.setProperty("PropertyType","Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID",(String)(super.getParameter())[1].getValue()+" ClosenessCentrality");            Property prop = PropertyFactory.newInstance().create(props);            if(centralityValue[i]!=0.0){                prop.add(new Double(centralityValue[i]));            }else{                prop.add(new Double(Double.NaN));            }            path[i].getActor().add(prop);                        props.setProperty("PropertyType","Basic");            props.setProperty("PropertyClass", "java.lang.Double");            props.setProperty("PropertyID",(String)(super.getParameter())[1].getValue()+" ClosenessPrestige");            prop = PropertyFactory.newInstance().create(props);            if(prestigeValue[i] != 0.0){                prop.add(new Double(prestigeValue[i]));            }else{                prop.add(new Double(Double.NaN));            }            path[i].getActor().add(prop);        }        calculateGraphCentrality(g);        calculateGraphCentralitySD(g);        calculateGraphPrestige(g);        calculateGraphPrestigeSD(g);       } catch (InvalidObjectTypeException ex) {            Logger.getLogger(OptimizedCloseness.class.getName()).log(Level.SEVERE, "Property class does not match java.lang.Double", ex);        }    }    protected void calculateGraphCentrality(Graph g) throws InvalidObjectTypeException{        double centrality = 0.0;        for(int i=0;i<centralityValue.length;++i){            centrality += maxCentrality - centralityValue[i];        }        centrality *= 2;        centrality /= (centralityValue.length-1)*(centralityValue.length-1)*(centralityValue.length-2);        java.util.Properties props = new java.util.Properties();        props.setProperty("PropertyType","Basic");            props.setProperty("PropertyClass", "java.lang.Double");        props.setProperty("PropertyID",(String)(super.getParameter())[1].getValue()+" ClosenessCentrality");        Property prop = PropertyFactory.newInstance().create(props);        prop.add(new Double(centrality));        g.add(prop);    }        protected void calculateGraphPrestige(Graph g) throws InvalidObjectTypeException{        double prestige = 0.0;        for(int i=0;i<prestigeValue.length;++i){            prestige += maxPrestige - prestigeValue[i];        }        prestige *= 2;        prestige /= (prestigeValue.length-1)*(prestigeValue.length-1)*(prestigeValue.length-2);        java.util.Properties props = new java.util.Properties();        props.setProperty("PropertyType","Basic");            props.setProperty("PropertyClass", "java.lang.Double");        props.setProperty("PropertyID",(String)(super.getParameter())[1].getValue()+" ClosenessPrestige");        Property prop = PropertyFactory.newInstance().create(props);        prop.add(new Double(prestige));        g.add(prop);    }        protected void calculateGraphCentralitySD(Graph g) throws InvalidObjectTypeException{        double sd = 0.0;        double centralitySquare = 0.0;        double centralitySum = 0.0;        for(int i=0;i<centralityValue.length;++i){            centralitySquare += centralityValue[i]*centralityValue[i];            centralitySum += centralityValue[i];        }        sd = centralityValue.length*centralitySquare-centralitySum*centralitySum;        sd /= centralityValue.length;        java.util.Properties props = new java.util.Properties();        props.setProperty("PropertyType","Basic");            props.setProperty("PropertyClass", "java.lang.Double");        props.setProperty("PropertyID",(String)(super.getParameter())[1].getValue()+" ClosenessCentralitySD");        Property prop = PropertyFactory.newInstance().create(props);        prop.add(new Double(sd));        g.add(prop);    }        protected void calculateGraphPrestigeSD(Graph g) throws InvalidObjectTypeException{        double sd = 0.0;        double prestigeSquare = 0.0;        double prestigeSum = 0.0;        for(int i=0;i<centralityValue.length;++i){            prestigeSquare += prestigeValue[i]*prestigeValue[i];            prestigeSum += prestigeValue[i];        }        double squareSum = prestigeSum*prestigeSum;        sd = (prestigeValue.length*prestigeSquare)-squareSum;        sd /= prestigeValue.length;        java.util.Properties props = new java.util.Properties();        props.setProperty("PropertyType","Basic");            props.setProperty("PropertyClass", "java.lang.Double");        props.setProperty("PropertyID",(String)(super.getParameter())[1].getValue()+" ClosenessPrestigeSD");        Property prop = PropertyFactory.newInstance().create(props);        prop.add(new Double(sd));        g.add(prop);    }        protected void constructOutput(OutputDescriptor[] output, ParameterInternal[] parameter) {        if(((String)parameter[0].getValue()).contentEquals("")){            parameter[0].setValue("Optimized Closeness");        }                Properties props = new Properties();        output = new OutputDescriptor[6];        // Construct Output Descriptors        props.setProperty("Type","Actor");        props.setProperty("Relation",(String)parameter[2].getValue());        props.setProperty("AlgorithmName",(String)parameter[0].getValue());        props.setProperty("Property",(String)parameter[1].getValue()+" ClosenessPrestige");        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);                props.setProperty("Type","Actor");        props.setProperty("Relation",(String)parameter[2].getValue());        props.setProperty("AlgorithmName",(String)parameter[0].getValue());        props.setProperty("Property",(String)parameter[1].getValue()+" ClosenessCentrality");        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);                props.setProperty("Type","Graph");        props.remove("Relation");        props.setProperty("AlgorithmName",(String)parameter[0].getValue());        props.setProperty("Property",(String)parameter[1].getValue()+" ClosenessPrestige");        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);                props.setProperty("Type","Graph");        props.remove("Relation");        props.setProperty("AlgorithmName",(String)parameter[0].getValue());        props.setProperty("Property",(String)parameter[1].getValue()+" ClosenessPrestigeSD");        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);                props.setProperty("Type","Graph");        props.remove("Relation");        props.setProperty("AlgorithmName",(String)parameter[0].getValue());        props.setProperty("Property",(String)parameter[1].getValue()+" ClosenessCentrality");        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);                props.setProperty("Type","Graph");        props.remove("Relation");        props.setProperty("AlgorithmName",(String)parameter[0].getValue());        props.setProperty("Property",(String)parameter[1].getValue()+" ClosenessCentralitySD");        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);    }    }