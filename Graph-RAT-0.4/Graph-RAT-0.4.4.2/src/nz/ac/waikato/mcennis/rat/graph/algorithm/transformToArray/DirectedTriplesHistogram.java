/*
 * Created 29/05/2008-14:15:01
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.transformToArray;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.InputDescriptorInternal;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptorInternal;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import org.dynamicfactory.property.InvalidObjectTypeException;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Rediscovery of Batagel-Mrvar algorithm
 * 
 * Batagel, V., A. Mrvar. 2001. A subquadratic triad census algorithm for large
 * sparse networks with small maximum degree. <i>Social Networks</i> 23:237-43.
 * 
 * Transforms a graph object (one mode and relation) into 16 dimensional double
 * array describing a histogram of all triples of actors in the graph.  
 * <br/>
 * Links are classified in the following way:
 * <ul>
 * <li/>0: no link exists
 * <li/>1: smaller->greater link exists
 * <li/>2: greater->smaller link exists
 * <li/>3: bi-directional link exists
 * </ul>
 * 
 * 
 * 
 * @author Daniel McEnnis
 */
public class DirectedTriplesHistogram extends ModelShell implements Algorithm {

    private ParameterInternal[] parameter = new ParameterInternal[4];
    private InputDescriptorInternal[] input = new InputDescriptorInternal[2];
    private OutputDescriptorInternal[] output = new OutputDescriptorInternal[1];

    public void DirectedTriplesHistogram() {
        init(null);
    }

    @Override
    public void execute(Graph g) {
        TreeSet<Actor> triplesBase = new TreeSet<Actor>();
        Iterator<Actor> iterator = g.getActorIterator((String) parameter[1].getValue());
        int nonZeroCount = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                triplesBase.add(iterator.next());
            }
            double[] tripleCount = new double[16];
            Arrays.fill(tripleCount, 0.0);
            for (Actor first : triplesBase) {
                TreeSet<Actor> firstActorList = getLinks(g, first, first);
                for (Actor second : firstActorList) {
                    if (second.compareTo(first) > 0) {
                        TreeSet<Actor> secondActorList = getLinks(g, second, first);

                        // handle all fully linked triples
                        TreeSet<Actor> fullyLinked = new TreeSet<Actor>();
                        fullyLinked.addAll(firstActorList);
                        fullyLinked.retainAll(secondActorList);
                        int firstSecond = getLinkType(g, first, second);
                        for (Actor third : fullyLinked) {
                            int firstThird = getLinkType(g, first, third);
                            int secondThird = getLinkType(g, second, third);

                            tripleCount[mapIndex(firstSecond, firstThird, secondThird)]++;
                            nonZeroCount++;
                        }

                        // handle all 2 sided triples
                        TreeSet<Actor> twoSided = new TreeSet<Actor>();
                        twoSided.addAll(secondActorList);
                        twoSided.removeAll(firstActorList);
                        for (Actor third : twoSided) {
                            int firstThird = getLinkType(g, first, third);
                            int secondThird = getLinkType(g, second, third);
                            tripleCount[mapIndex(firstSecond, firstThird, secondThird)]++;
                            nonZeroCount++;
                        }

                        // handle all 1 sided triples
                        tripleCount[mapIndex(firstSecond, 0, 0)] += (triplesBase.tailSet(second).size() - 1) - fullyLinked.size() - twoSided.size();
                        nonZeroCount += (triplesBase.tailSet(second).size() - 1) - fullyLinked.size() - twoSided.size();
                    }
                }
            }
            tripleCount[0] = triplesBase.size() * (triplesBase.size() - 1) * (triplesBase.size() - 2) - nonZeroCount;
            FastVector base = new FastVector();
            for (int i = 0; i < tripleCount.length; ++i) {
                base.addElement(new Attribute(g.getID() + (String) parameter[3].getValue() + ":" + i));
            }
            Instances meta = new Instances("Directed Triples", base, 1);
            Instance value = new Instance(tripleCount.length, tripleCount);
            value.setDataset(meta);
            Properties props = new Properties();
            props.setProperty("PropertyType", "Basic");
            props.setProperty("PropertyClass", "weka.core.Instance");
            props.setProperty("PropertyID", g.getID() + (String) parameter[3].getValue());
            Property property = PropertyFactory.newInstance().create(props);
            try {
                property.add(value);
                g.add(property);
            } catch (InvalidObjectTypeException ex) {
                Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Property class doesn't match double[]", ex);
            }
        } else {
            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.WARNING, "No actors of mode '" + (String) parameter[1].getValue() + "' were found in graph '" + g.getID() + "'");
        }
    }

    protected int mapIndex(int first, int second, int third) {
        boolean bad = false;
        if ((first > 3) || (first < 0)) {
            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "First link is not 0-4:" + first);
            bad = true;
        }
        if ((second > 3) || (second < 0)) {
            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Second link is not 0-4:" + second);
            bad = true;
        }
        if ((third > 3) || (third < 0)) {
            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "Third link is not 0-4:" + third);
            bad = true;
        }
        if (bad) {
            return -1;
        }
        switch (first) {
            case 0:
                switch (second) {
                    case 0:
                        switch (third) {
                            case 0:
                                return 0;
                            case 1:
                                return 1;
                            case 2:
                                return 1;
                            case 3:
                                return 2;
                        }
                    case 1:
                        switch (third) {
                            case 0:
                                return 1;
                            case 1:
                                return 4;
                            case 2:
                                return 5;
                            case 3:
                                return 6;
                        }

                    case 2:
                        switch (third) {
                            case 0:
                                return 1;
                            case 1:
                                return 5;
                            case 2:
                                return 3;
                            case 3:
                                return 6;
                        }
                    case 3:
                        switch (third) {
                            case 0:
                                return 2;
                            case 1:
                                return 6;
                            case 2:
                                return 6;
                            case 3:
                                return 10;
                        }
                }
            case 1:
                switch (second) {
                    case 0:
                        switch (third) {
                            case 0:
                                return 1;
                            case 1:
                                return 2;
                            case 2:
                                return 5;
                            case 3:
                                return 6;
                        }
                    case 1:
                        switch (third) {
                            case 0:
                                return 5;
                            case 1:
                                return 9;
                            case 2:
                                return 8;
                            case 3:
                                return 13;
                        }
                    case 2:
                        switch (third) {
                            case 0:
                                return 4;
                            case 1:
                                return 8;
                            case 2:
                                return 8;
                            case 3:
                                return 12;
                        }

                    case 3:
                        switch (third) {
                            case 0:
                                return 6;
                            case 1:
                                return 11;
                            case 2:
                                return 13;
                            case 3:
                                return 14;
                        }

                }
            case 2:
                switch (second) {
                    case 0:
                        switch (third) {
                            case 0:
                                return 1;
                            case 1:
                                return 5;
                            case 2:
                                return 4;
                            case 3:
                                return 6;
                        }
                    case 1:
                        switch (third) {
                            case 0:
                                return 2;
                            case 1:
                                return 8;
                            case 2:
                                return 8;
                            case 3:
                                return 11;
                        }
                    case 2:
                        switch (third) {
                            case 0:
                                return 5;
                            case 1:
                                return 9;
                            case 2:
                                return 8;
                            case 3:
                                return 13;
                        }
                    case 3:
                        switch (third) {
                            case 0:
                                return 6;
                            case 1:
                                return 13;
                            case 2:
                                return 12;
                            case 3:
                                return 14;
                        }
                }
            case 3:
                switch (second) {
                    case 0:
                        switch (third) {
                            case 0:
                                return 2;
                            case 1:
                                return 6;
                            case 2:
                                return 6;
                            case 3:
                                return 10;
                        }
                    case 1:
                        switch (third) {
                            case 0:
                                return 6;
                            case 1:
                                return 12;
                            case 2:
                                return 13;
                            case 3:
                                return 14;
                        }
                    case 2:
                        switch (third) {
                            case 0:
                                return 6;
                            case 1:
                                return 13;
                            case 2:
                                return 11;
                            case 3:
                                return 14;
                        }
                    case 3:
                        switch (third) {
                            case 0:
                                return 10;
                            case 1:
                                return 14;
                            case 2:
                                return 14;
                            case 3:
                                return 15;
                        }
                }
        }
        return -1;
    }

    protected int twoSidesMap(int twoSide, int oneSide) {
        if (twoSide == 0) {
            switch (oneSide) {
                case 1:
                    return 5;
                case 2:
                    return 6;
                case 3:
                    return 7;
                default:
                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");
                    return -1;
            }
        } else if (twoSide == 1) {
            switch (oneSide) {
                case 0:
                    return 8;
                case 2:
                    return 9;
                case 3:
                    return 10;
                default:
                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");
                    return -1;
            }
        } else if (twoSide == 2) {
            switch (oneSide) {
                case 0:
                    return 11;
                case 1:
                    return 12;
                case 3:
                    return 13;
                default:
                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");
                    return -1;
            }
        } else if (twoSide == 3) {
            switch (oneSide) {
                case 0:
                    return 14;
                case 1:
                    return 15;
                case 2:
                    return 16;
                default:
                    Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid oneSide link type " + oneSide + " in DirectedTriplesHistogram");
                    return -1;
            }
        } else {
            Logger.getLogger(DirectedTriplesHistogram.class.getName()).log(Level.SEVERE, "INTERNAL ERROR - invalid twoSided link type " + twoSide + " in DirectedTriplesHistogram");
            return -1;
        }
    }

    protected int getLinkType(Graph g, Actor left, Actor right) {
        Link[] leftToRight = g.getLink((String) parameter[2].getValue(), left, right);
        Link[] rightToLeft = g.getLink((String) parameter[2].getValue(), right, left);
        if ((leftToRight == null) && (rightToLeft == null)) {
            return 0;
        } else if ((leftToRight != null) && (rightToLeft == null)) {
            return 1;
        } else if ((leftToRight == null) && (rightToLeft != null)) {
            return 2;
        } else {
            return 3;
        }
    }

    protected TreeSet<Actor> getLinks(Graph g, Actor v, Actor comparator) {
        TreeSet<Actor> actors = new TreeSet<Actor>();
        Link[] out = g.getLinkBySource((String) parameter[1].getValue(), v);
        if (out != null) {
            for (int i = 0; i < out.length; ++i) {
                if (out[i].getDestination().compareTo(comparator) > 0) {
                    actors.add(out[i].getDestination());
                }
            }
        }
        Link[] in = g.getLinkByDestination((String) parameter[1].getValue(), v);
        if (in != null) {
            for (int i = 0; i < in.length; ++i) {
                if (!actors.contains(in[i].getSource()) && in[i].getSource().compareTo(comparator) > 0) {
                    actors.add(in[i].getSource());
                }
            }
        }
        return actors;
    }

    @Override
    public InputDescriptor[] getInputType() {
        return input;
    }

    @Override
    public OutputDescriptor[] getOutputType() {
        return output;
    }

    @Override
    public Parameter[] getParameter() {
        return parameter;
    }

    @Override
    public Parameter getParameter(String param) {
        for (int i = 0; i < parameter.length; ++i) {
            if (parameter[i].getName().contentEquals(param)) {
                return parameter[i];
            }
        }
        return null;
    }

    @Override
    public SettableParameter[] getSettableParameter() {
        return null;
    }

    @Override
    public SettableParameter getSettableParameter(String param) {
        return null;
    }

    /**
     * Parameters:<br/>
     * <ul>
     * <li/><b>name</b>: name of this algorithm. Default is 'Graph Triples Histogram'
     * <li/><b>actorType</b>: mode to execute over.  Default is 'tag'
     * <li/><b>relation</b>: realtion to execute over. Default is 'Tags'
     * <li/><b>propertyNameSuffix</b>: Suffix for the property name. Default is ' Directed Triples Histogram'
     * </ul>
     * @param map parameters to be loaded - may be null
     */
    public void init(Properties map) {
        Properties props = new Properties();
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "name");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[0] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("name") != null)) {
            parameter[0].setValue(map.getProperty("name"));
        } else {
            parameter[0].setValue("Graph Triples Histogram");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("tag");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "relation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("relation") != null)) {
            parameter[2].setValue(map.getProperty("relation"));
        } else {
            parameter[2].setValue("Tags");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "propertyNameSuffix");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("propertyNameSuffix") != null)) {
            parameter[3].setValue(map.getProperty("propertyNameSuffix"));
        } else {
            parameter[3].setValue(" Directed Triples Histogram");
        }

        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[1].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", (String) parameter[3].getValue());
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

    }
}
