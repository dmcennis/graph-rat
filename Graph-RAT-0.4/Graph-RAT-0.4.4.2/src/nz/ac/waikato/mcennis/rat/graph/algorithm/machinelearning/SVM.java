/*
 * Created 5-3-08
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.machinelearning;

import java.util.Properties;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.SVMreg;
import weka.classifiers.meta.AdaBoostM1;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;

/**
 *
 * @author Daniel McEnnis
 */
public class SVM extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[12];
    OutputDescriptor[] output = new OutputDescriptor[1];
    InputDescriptor[] input = new InputDescriptor[4];
    Actor[] user = null;
    Actor[] artists = null;

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
     * 
     * Paramters are defined as follows:
     * <ol>
     * <li>'name' - name for this instance of this algorithm. Default 'Weka Classifier'.
     * <li>'output' - directory where output is stored/ Default '/tmp/output'.
     * <li>'artistType' - type (mode) of actor representing total artists. Default
     * 'Artist'.
     * <li>'groundTruthType'- type (relation) of link representing given musical tastes
     * <li>'sourceType1' - type (relation) of link describing interest links. Default
     * 'Interest'.
     * <li>'sourceType2' - type (relation) of link describing music links. Default
     * 'Music'.
     * <li>'equalizeInstanceCounts' - Boolean describing whether to balance number
     * of positive and negative instances. Deafult 'true'.
     * <li>'userType' - type (mode) of actor representing the users consuming music.
     * Default 'User'.
     * <li>'classifierType' - type of Weka classifier. Default is 'J48'.
     * </ol>
     * <br>
     * <br>Input 0 - Link
     * <br>Input 1 - Link
     * <br>Input 2 - Link
     * <br>Input 3 - Actor
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
            parameter[0].setValue("Weka Classifier");
        }
        // Parameter 1 - output
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "output");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("output") != null)) {
            parameter[1].setValue(map.getProperty("output"));
        } else {
            parameter[1].setValue("/tmp/output/");
        }
        // Parameter 2 - artist type
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "artistType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("artistType") != null)) {
            parameter[2].setValue(map.getProperty("artistType"));
        } else {
            parameter[2].setValue("Artist");
        }
        // Parameter 3 - Ground Truth Type
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "groundTruthType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("groundTruthType") != null)) {
            parameter[3].setValue(map.getProperty("groundTruthType"));
        } else {
            parameter[3].setValue("Given");
        }
        // Parameter 4 - source link type
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "sourceType1");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceType1") != null)) {
            parameter[4].setValue(map.getProperty("sourceType1"));
        } else {
            parameter[4].setValue("Interest");
        }
        // Parameter 5 - source link type 2
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "sourceType2");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceType2") != null)) {
            parameter[5].setValue(map.getProperty("sourceType2"));
        } else {
            parameter[5].setValue("Music");
        }
        // Parameter 6 - source link type 2
        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "equalizeInstanceCounts");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "false");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("equalizeInstanceCounts") != null)) {
            parameter[6].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("equalizeInstanceCounts"))));
        } else {
            parameter[6].setValue(new Boolean(true));
        }
        // Parameter 2 - artist type
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "userType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("userType") != null)) {
            parameter[7].setValue(map.getProperty("userType"));
        } else {
            parameter[7].setValue("User");
        }
        // Parameter 9 - classifier type
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "classifierType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("classifierType") != null)) {
            parameter[8].setValue(map.getProperty("classifierType"));
        } else {
            parameter[8].setValue("J48");
        }
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[9] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkType") != null)) {
            parameter[9].setValue(map.getProperty("linkType"));
        } else {
            parameter[9].setValue("Derived");
        }
        props.setProperty("Type", "java.lang.Integer");
        props.setProperty("Name", "maxPositive");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[10] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("maxPositive") != null)) {
            parameter[10].setValue(new Integer(Integer.parseInt(map.getProperty("maxPositive"))));
        } else {
            parameter[10].setValue(new Integer(160));
        }
        props.setProperty("Type", "java.lang.Double");
        props.setProperty("Name", "ratioNegative2Positive");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[11] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("ratioNegative2Positive") != null)) {
            parameter[11].setValue(new Double(Double.parseDouble(map.getProperty("ratioNegative2Positive"))));
        } else {
            parameter[11].setValue(new Double(4.0));
        }



        // init input 0
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);
        // init input 1
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);
        // init input 2
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[5].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[2] = DescriptorFactory.newInstance().createInputDescriptor(props);
        // init input 3
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[3] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[9].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }

    public void execute(Graph g) {
        artists = g.getActor((String) parameter[2].getValue());
        java.util.Arrays.sort(artists);
        user = g.getActor((String) parameter[7].getValue());
        fireChange(Scheduler.SET_ALGORITHM_COUNT, artists.length);
        int totalPerFile = user.length;
        for (int i = 0; i < artists.length; ++i) {
            try {
                if (i % 10 == 0) {
                    System.out.println("Evaluating for artist " + artists[i].getID() + " " + i + " of " + artists.length);
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
                }
                Instances dataSet = createDataSet(artists);
                int totalThisArtist = g.getLinkByDestination((String) parameter[3].getValue(), artists[i]).length;
                int positiveSkipCount = 1;
                if ((((Integer) parameter[10].getValue()).intValue()!=0)&&(totalThisArtist > ((Integer) parameter[10].getValue()))) {
                    positiveSkipCount = (totalThisArtist / 160) + 1;
                }
                if (totalThisArtist > 0) {
                    int skipValue = (int) ((((Double) parameter[11].getValue()).doubleValue() * totalPerFile) / (totalThisArtist / positiveSkipCount));
                    if (skipValue <= 0) {
                        skipValue = 1;
                    }
                    if (!(Boolean) parameter[6].getValue()) {
                        skipValue = 1;
                    }
                    addInstances(g, dataSet, artists[i], skipValue, positiveSkipCount);
//                    Classifier classifier = getClassifier();
                    AdaBoostM1 classifier = new AdaBoostM1();
                    try {
//                        System.out.println("Building Classifier");
                        classifier.buildClassifier(dataSet);
//                        System.out.println("Evaluating Classifer");
                        evaluateClassifier(classifier, dataSet, g, artists[i]);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    classifier = null;
                }
                dataSet = null;
            } catch (java.lang.OutOfMemoryError e) {
                System.err.println("Artist " + artists[i].getID() + " (" + i + ") ran out of memory");
                System.gc();
            }
        }
    }

    protected Instances createDataSet(Actor[] artists) {
        Instances ret = null;
        FastVector attributes = new FastVector(2 + artists.length);
        for (int i = 0; i < artists.length; ++i) {
            FastVector artist = new FastVector(2);
            artist.addElement("false");
            artist.addElement("true");
            attributes.addElement(new Attribute(artists[i].getID(), artist));
        }
        FastVector classValue = new FastVector(2);
        classValue.addElement("false");
        classValue.addElement("true");
        attributes.addElement(new Attribute("class", classValue));
        ret = new Instances("Training", attributes, 100);
        ret.setClassIndex(attributes.size() - 1);
        return ret;
    }

    protected void addInstances(Graph g, Instances dataSet, Actor artist, int skipCount, int positiveSkipCount) {
        int skipCounter = 0;
        int positiveSkipCounter = 0;
        for (int i = 0; i < user.length; ++i) {
            String result = "false";
            if (g.getLink((String) parameter[3].getValue(), user[i], artist) != null) {
                result = "true";
            }
            Link[] given = g.getLinkBySource((String) parameter[3].getValue(), user[i]);
            if (given != null) {
                if (((result.contentEquals("true")) && (positiveSkipCounter % positiveSkipCount == 0)) ||
                        ((result.contentEquals("false")) && (skipCounter % skipCount == 0))) {
                    double[] values = new double[artists.length + 1];
                    java.util.Arrays.fill(values, 0.0);
                    for (int k = 0; k < given.length; ++k) {
                        if (given[k].getDestination() == artist) {
                            values[java.util.Arrays.binarySearch(artists, given[k].getDestination())] = Double.NaN;
                        } else {
                            values[java.util.Arrays.binarySearch(artists, given[k].getDestination())] = 1.0;
                        }
                    }
                    if (result.compareTo("true") == 0) {
                        values[values.length - 1] = 1.0;
                    }
                    Instance instance = new SparseInstance(1 + artists.length, values);
                    instance.setDataset(dataSet);
                    instance.setClassValue(result);
                    dataSet.add(instance);
//                            System.out.println("Adding instance for user "+i);
                    if (result.contentEquals("false")) {
                        skipCounter++;
                    } else {
                        positiveSkipCounter++;
                    }
                } else if (result.contentEquals("false")) {
                    skipCounter++;
                } else {
                    positiveSkipCounter++;
                }
            }
        }
    }

    protected void evaluateClassifier(Classifier classifier, Instances dataSet, Graph g, Actor toBePredicted) throws Exception {
        if (user != null) {
            for (int i = 0; i < user.length; ++i) {
                // evaluate all propositionalized instances and evalulate the results
                if (i % 100 == 0) {
 //                   System.out.println("Evaluating for user " + i);
                }
                Link[] given = g.getLinkBySource((String) parameter[3].getValue(), user[i]);
                if (given != null) {
                    Instances evaluateData = dataSet.stringFreeStructure();
                    double[] values = new double[artists.length + 3];
                    java.util.Arrays.fill(values, 0.0);
                    for (int k = 0; k < given.length; ++k) {
                        if (given[k].getDestination().equals(toBePredicted)) {
                            values[java.util.Arrays.binarySearch(artists, given[k].getDestination())] = Double.NaN;
                        } else {
                            values[java.util.Arrays.binarySearch(artists, given[k].getDestination())] = 1.0;
                        }
                    }
                    Instance instance = new SparseInstance(artists.length + 3, values);
                    instance.setDataset(evaluateData);
                    double result = classifier.classifyInstance(instance);
                    if (result == 1.0) {
                        Properties props = new Properties();
                        props.setProperty("LinkType", (String) parameter[9].getValue());
                        Link derived = LinkFactory.newInstance().create(props);
                        derived.set(user[i], 1.0, toBePredicted);
                        g.add(derived);
                    }
                }
            }
        }
    }
}
