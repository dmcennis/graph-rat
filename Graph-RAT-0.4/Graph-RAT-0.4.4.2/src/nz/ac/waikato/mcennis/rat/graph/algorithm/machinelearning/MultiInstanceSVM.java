/*
 * OutputARFF.java
 *
 * Created on October 23, 2007, 5:05 PM
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.machinelearning;
import nz.ac.waikato.mcennis.rat.graph.algorithm.*;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.SVMreg;
import weka.classifiers.meta.AdaBoostM1;
import weka.core.SparseInstance;
/**
 * Derive globally using the same data as the AddMusicRecommendation algorithm.
 * Takes the weka classifier as a parameter.  See execute for how the propositionalization
 * is performed.
 *
 * @author Daniel McEnnis
 * 
 */
public class MultiInstanceSVM extends ModelShell implements Algorithm {
    ParameterInternal[] parameter = new ParameterInternal[12];
    OutputDescriptor[] output = new OutputDescriptor[1];
    InputDescriptor[] input = new InputDescriptor[4];
    Actor[] user = null;
    Actor[] artists = null;
//    int[] correctlyClassified = null;
//    int[] totalClassified = null;
//    int[] totalPresent = null;
    /** Creates a new instance of OutputARFF */
    public MultiInstanceSVM() {
        init(null);
    }
    /**
     * Generate music predictions for a user as follows:
     * Calculate all artists A present in the data set.
     * Create a data set containing two numeric attributes (typically generated by the
     * AddBasicInterestLink and AddMusicLinks algorithms), a boolean for every artist
     * and a boolean class variable.  These fields are populated as follows
     * <br>
     * For each artist, generate a 2-class classifier.
     * <br>
     * For every user, for every friend of the user:
     * First two fields are the interest and music link (0 if absent).
     * The artist fields are the music listened to by the friend
     * The final field is whether or not the user listens to the music specified.
     * 
     * For memory reasons, not all training data is used.  
     * FIXME: hard coded to a maximum 160 positive instances - should be a parameter
     */
    public void execute(Graph g) {
        artists = g.getActor((String) parameter[2].getValue());
        java.util.Arrays.sort(artists);
        user = g.getActor((String) parameter[7].getValue());
        fireChange(Scheduler.SET_ALGORITHM_COUNT,artists.length);
//        correctlyClassified = new int[user.length];
//        totalClassified = new int[user.length];
//        totalPresent = new int[user.length];
//        java.util.Arrays.fill(correctlyClassified, 0);
//        java.util.Arrays.fill(totalClassified, 0);
//        java.util.Arrays.fill(totalPresent, 0);
//        for (int i = 0; i < user.length; ++i) {
//            Link[] given = g.getLinkBySource((String) parameter[3].getValue(), user[i]);
//            if (given != null) {
//                totalPresent[i] = given.length;
//            }
//        }
        int totalPerFile = countTotal(g);
        for (int i = 0; i < artists.length; ++i) {
            try {
                if (i % 10 == 0) {
                    Logger.getLogger(MultiInstanceSVM.class.getName()).log(Level.INFO,"Evaluating for artist " + artists[i].getID() + " " + i + " of " + artists.length);
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);
                }
                Instances dataSet = createDataSet(artists);
                int totalThisArtist = totalYes(g, artists[i]);
                int positiveSkipCount = 1;
                if ((((Integer) parameter[10].getValue()).intValue()!=0)&&(totalThisArtist > ((Integer) parameter[10].getValue()))) {
                    positiveSkipCount = (totalThisArtist / 160) + 1;
                }
                if (totalThisArtist > 0) {
                    int skipValue = (int)((((Double)parameter[11].getValue()).doubleValue()*totalPerFile) / (totalThisArtist / positiveSkipCount));
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
                        Logger.getLogger(MultiInstanceSVM.class.getName()).log(Level.FINER,"Building Classifier");
                        classifier.buildClassifier(dataSet);
                        Logger.getLogger(MultiInstanceSVM.class.getName()).log(Level.FINER,"Evaluating Classifer");
                        evaluateClassifier(classifier, dataSet, g, artists[i]);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    classifier = null;
                }else{
                    Logger.getLogger(MultiInstanceSVM.class.getName()).log(Level.WARNING,"Artist '"+artists[i].getID()+"' has no users listening to them");
                }
                dataSet = null;
            } catch (java.lang.OutOfMemoryError e) {
                Logger.getLogger(MultiInstanceSVM.class.getName()).log(Level.WARNING,"Artist " + artists[i].getID() + " (" + i + ") ran out of memory");
//                System.gc();
            }
        }
//        double precision = 0.0;
//        double precisionSum = 0.0;
//        double precisionSquared = 0.0;
//        double recall = 0.0;
//        double recallSum = 0.0;
//        double recallSquared = 0.0;
//        for (int i = 0; i < correctlyClassified.length; ++i) {
//            if (totalClassified[i] > 0) {
//                precision = ((double) correctlyClassified[i]) / ((double) totalClassified[i]);
//            } else {
//                precision = 0.0;
//            }
//            precisionSum += precision;
//            precisionSquared += precision * precision;
//        }
//        for (int i = 0; i < totalPresent.length; ++i) {
//            if (totalPresent[i] > 0) {
//                recall = ((double) correctlyClassified[i]) / ((double) totalPresent[i]);
//            } else {
//                recall = 0;
//            }
//            recallSum += recall;
//            recallSquared += recall * recall;
//        }
//        double sd = ((correctlyClassified.length * precisionSquared) - precisionSum * precisionSum) / correctlyClassified.length;
//        double mean = precisionSum / correctlyClassified.length;
//        System.out.println("Positive Precision\t" + mean);
//        System.out.println("Positive Precision SD\t" + sd);
//        sd = ((correctlyClassified.length * recallSquared) - recallSum * recallSum) / correctlyClassified.length;
//        mean = recallSum / correctlyClassified.length;
//        System.out.println("Positive Recall\t" + mean);
//        System.out.println("Positive Recall SD\t" + sd);
    }
    protected void evaluateClassifier(Classifier classifier, Instances dataSet, Graph g, Actor toBePredicted) throws Exception {
        if (user != null) {
            for (int i = 0; i < user.length; ++i) {
                int total = 0;
                int setTrue = 0;
                // evaluate all propositionalized instances and evalulate the results
                Link[] interests = g.getLinkBySource((String) parameter[4].getValue(), user[i]);
                if (i % 100 == 0) {
                    Logger.getLogger(MultiInstanceSVM.class.getName()).log(Level.FINER,"Evaluating for user " + i);
                }
                if (interests != null) {
                    for (int j = 0; j < interests.length; ++j) {
                        Link[] music = g.getLink((String) parameter[5].getValue(), user[i], interests[j].getDestination());
                        if (music != null) {
                            Link[] given = g.getLinkBySource((String) parameter[3].getValue(), interests[j].getDestination());
                            if (given != null) {
                                Instances evaluateData = dataSet.stringFreeStructure();
                                double[] values = new double[artists.length + 3];
                                java.util.Arrays.fill(values, 0.0);
                                values[0] = interests[j].getStrength();
                                values[1] = music[0].getStrength();
                                for (int k = 0; k < given.length; ++k) {
                                    values[java.util.Arrays.binarySearch(artists, given[k].getDestination()) + 2] = 1.0;
                                }
                                Instance instance = new SparseInstance(artists.length + 3, values);
                                instance.setDataset(evaluateData);
                                double result = classifier.classifyInstance(instance);
                                if (result == 1.0) {
                                    setTrue++;
                                }
                                total++;
                            }
                        }
                    }
                }
                boolean evaluate = evaluateResult(setTrue, total);
                if(evaluate){
                    Properties props = new Properties();
                    props.setProperty("LinkType", (String)parameter[9].getValue());
                    Link derived = LinkFactory.newInstance().create(props);
                    derived.set(user[i],1.0,toBePredicted);
                    g.add(derived);
                }
//                if ((g.getLink((String) parameter[3].getValue(), user[i], toBePredicted) != null) && (evaluate)) {
//                    correctlyClassified[i]++;
//                }
//                if (evaluate) {
//                    totalClassified[i]++;
//                }
            }
        }
    }
    protected boolean evaluateResult(int count, int total) {
        if (count > (total / 2)) {
            return true;
        } else {
            return false;
        }
    }
    public int totalYes(Graph g, Actor a) {
        Link[] givenLinks = g.getLinkByDestination((String) parameter[3].getValue(), a);
        int count = 0;
        if (givenLinks != null) {
            for (int i = 0; i < givenLinks.length; ++i) {
                Actor source = givenLinks[i].getSource();
                Link[] friends = g.getLinkBySource((String) parameter[4].getValue(), source);
                if (friends != null) {
                    count += friends.length;
                //                    for (int j = 0; j < friends.length; ++j) {
                //                        Link[] derived = g.getLinkBySource((String) parameter[3].getValue(), friends[j].getDestination());
                //                        if (derived != null) {
                //                            count += derived.length;
                //                        }
                //                    }
                }
            }
        }
        return count;
    }
    protected int countTotal(Graph g) {
        int count = 0;
        for (int i = 0; i < user.length; ++i) {
            Link[] friends = g.getLinkBySource((String) parameter[4].getValue(), user[i]);
            if (friends != null) {
                count += friends.length;
            //                for (int j = 0; j < friends.length; ++j) {
            //                    Link[] derived = g.getLinkBySource((String) parameter[3].getValue(), friends[j].getDestination());
            //                    if (derived != null) {
            //                        count += derived.length;
            //                    }
            //                }
            }
        }
        return count;
    }
    protected Instances createDataSet(Actor[] artists) {
        Instances ret = null;
        FastVector attributes = new FastVector(4 + artists.length);
        attributes.addElement(new Attribute((String) parameter[4].getValue()));
        attributes.addElement(new Attribute((String) parameter[5].getValue()));
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
            Link[] interests = g.getLinkBySource((String) parameter[4].getValue(), user[i]);
            if (interests != null) {
                for (int j = 0; j < interests.length; ++j) {
                    Link[] music = g.getLink((String) parameter[5].getValue(), user[i], interests[j].getDestination());
                    Link[] given = g.getLinkBySource((String) parameter[3].getValue(), interests[j].getDestination());
                    if ((given != null) && (music != null)) {
                        if (((result.contentEquals("true")) && (positiveSkipCounter % positiveSkipCount == 0)) ||
                                ((result.contentEquals("false")) && (skipCounter % skipCount == 0))) {
                            double[] values = new double[artists.length + 3];
                            java.util.Arrays.fill(values, 0.0);
                            values[0] = interests[j].getStrength();
                            values[1] = music[0].getStrength();
                            for (int k = 0; k < given.length; ++k) {
                                values[java.util.Arrays.binarySearch(artists, given[k].getDestination()) + 2] = 1.0;
                            }
                            if (result.compareTo("true") == 0) {
                                values[values.length - 1] = 1.0;
                            }
                            Instance instance = new SparseInstance(3 + artists.length, values);
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
        }
    }
    protected Classifier getClassifier() {
        if (((String) parameter[8].getValue()).contentEquals("J48")) {
            return new J48();
        } else if (((String) parameter[8].getValue()).contentEquals("IBk")) {
            return new weka.classifiers.lazy.IBk();
        } else if (((String) parameter[8].getValue()).contentEquals("PART")) {
            return new weka.classifiers.rules.PART();
        } else if (((String) parameter[8].getValue()).contentEquals("NaiveBayes")) {
            return new weka.classifiers.bayes.NaiveBayes();
        } else if (((String) parameter[8].getValue()).contentEquals("OneR")) {
            return new weka.classifiers.rules.OneR();
        } else if (((String) parameter[8].getValue()).contentEquals("SMO")) {
            return new weka.classifiers.functions.SMO();
        } else if (((String) parameter[8].getValue()).contentEquals("Logistir")) {
            return new weka.classifiers.functions.Logistic();
        } else {
            System.out.println("ERROR: Classifer '" + (String) parameter[8].getValue() + "' does not exist");
            return null;
        }
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
}

