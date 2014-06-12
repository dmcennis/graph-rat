/*
 * Created 27-3-08
 * Copyright Daniel McEnnis, saee license.txt
 */
package nz.ac.waikato.mcennis.rat.dataAquisition;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.ACE.DataTypes.FeatureDefinition;
import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;
import jAudioFeatureExtractor.Aggregators.Aggregator;
import jAudioFeatureExtractor.Aggregators.AggregatorContainer;
import jAudioFeatureExtractor.DataModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.DescriptorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Module for creating a set of song actors where the features extracted from the
 * audio are added as weka Instance objects.  A Instances object is added to the 
 * graph object without being tied to the inidividual Instance objects.
 * 
 * @author Daniel McEnnis
 */
public class ReadAudioFiles extends ModelShell implements DataAquisition {

    Graph graph = null;
    ParameterInternal[] parameter = new ParameterInternal[7];
    InputDescriptor[] input = new InputDescriptor[0];
    OutputDescriptor[] output = new OutputDescriptor[3];
    Instances globalInstances;

    public ReadAudioFiles() {
        init(null);
    }

    public void start() {
        try {
            Object[] settings = (Object[]) XMLDocumentParser.parseXMLDocument((String) parameter[2].getValue(), "batchFile");
            Batch base = loadSettings(settings);
            File audioDirectory = new File((String) parameter[1].getValue());
            File[] music = audioDirectory.listFiles();
            if (music != null) {
                boolean firstRun = true;
                for (int i = 0; i < music.length; ++i) {
                    Logger.getLogger(ReadAudioFiles.class.getName()).log(Level.INFO,music[i].getPath());
                    base.setRecordings(new File[]{music[i]});
                    base.getDataModel().featureKey = new ByteArrayOutputStream();
                    base.getDataModel().featureValue = new ByteArrayOutputStream();
                    base.execute();
                    DataModel data = base.getDataModel();
                    if (firstRun) {
                        createInstances(data.container);
                        firstRun = false;
                    }
                    createSong(data.container, music[i].getName());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ReadAudioFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected Batch loadSettings(Object[] settings) {
        DataModel dm = new DataModel((String) parameter[3].getValue(), null);
        Batch ret = (Batch)settings[0];
        ret.setDataModel(dm);
//        ret.setWindowSize(Integer.parseInt((String) settings[0]));
//        ret.setWindowOverlap(Double.parseDouble((String) settings[1]));
//        ret.setSamplingRate(((Double) settings[2]).doubleValue());
//        ret.setNormalise(((Boolean) settings[3]).booleanValue());
//        ret.setOverall(true);
//        ret.setPerWindow(false);
//        HashMap<String, Boolean> checked = (HashMap<String, Boolean>) settings[7];
//        HashMap<String, String[]> featureAttributes = (HashMap<String, String[]>) settings[8];
//        ret.setFeatures(checked, featureAttributes);
//        LinkedList<String> aggNames = (LinkedList<String>) settings[9];
//        LinkedList<String[]> aggFeatures = (LinkedList<String[]>) settings[10];
//        LinkedList<String[]> aggParameters = (LinkedList<String[]>) settings[11];
//        ret.setAggregators(aggNames.toArray(new String[]{}), aggFeatures.toArray(new String[][]{}), aggParameters.toArray(new String[][]{}));
        return ret;
    }

    protected void createInstances(AggregatorContainer container) {
        globalInstances = null;
        FeatureDefinition[] definitions = container.getFeatureDefinitions();
        LinkedList<String> attributesList = new LinkedList<String>();
        for (int i = 0; i < definitions.length; ++i) {
            for (int j = 0; j < definitions[i].dimensions; ++j) {
                attributesList.add(definitions[i].name + j);
            }
        }
        FastVector attributes = new FastVector(attributesList.size() + 1);

        for (String att : attributesList) {
            attributes.addElement(new Attribute(att));
        }
        attributes.addElement(new Attribute("class"));
        globalInstances = new Instances((String)parameter[6].getValue(), attributes, 100);
        globalInstances.setClassIndex(attributes.size() - 1);
        Properties properties = new Properties();
        properties.setProperty("PropertyID", (String) parameter[5].getValue());
        properties.setProperty("PropertyClass", "weka.core.Instances");
        Property prop = PropertyFactory.newInstance().create(properties);
        try {
            prop.add(globalInstances);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(ReadAudioFiles.class.getName()).log(Level.SEVERE, "Property class does not match weka.core.Instances", ex);
        }
        graph.add(prop);
    }

    protected void createSong(AggregatorContainer container, String ID) {
        // create Instance
        double[][] rawValues = container.getResults();
        int total = 0;
        for (int i = 0; i < rawValues.length; ++i) {
            if (rawValues[i] != null) {
                total += rawValues[i].length;
            }
        }
        double[] results = new double[total + 1];
        int count = 0;
        for (int i = 0; i < rawValues.length; ++i) {
            if (rawValues[i] != null) {
                for (int j = 0; j < rawValues[i].length; ++j) {
                    results[count++] = rawValues[i][j];
                }
            }
        }
        results[results.length - 1] = Double.NaN;
        Instance instance = new Instance(results.length, results);
        instance.setDataset(globalInstances);

        //attach instance to new actor.
        Properties properties = new Properties();
        properties.setProperty("ActorType", (String) parameter[4].getValue());
        properties.setProperty("ActorID", ID);
        Actor song = ActorFactory.newInstance().create(properties);
        properties.setProperty("PropertyID", (String) parameter[5].getValue());
        properties.setProperty("PropertyClass", "weka.core.Instance");
        Property prop = PropertyFactory.newInstance().create(properties);
        try {
            prop.add(instance);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(ReadAudioFiles.class.getName()).log(Level.SEVERE, "Property class does not match weka.core.Instance", ex);
        }
        song.add(prop);
        graph.add(song);
    }

    @Override
    public void set(Graph g) {
        graph = g;
    }

    @Override
    public Graph get() {
        return graph;
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputDescriptor[] getInputType() {
        return new InputDescriptor[]{};
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
     * <br/>
     * <ul>
     * <li/><b>name</>: Name of the algorithm. Default is 'Read Audio Files'
     * <li/><b>audioDirectoryLocation</b>: location of the audio files. Default is
     * '/tmp/music/'
     * <li/><b>settingsFile</b>: location of the settings used for extraction in jAudio. 
     * Default is '/tmp/settings.xml'
     * <li/><b>featuresFile</b>: location of the XML file describing all features
     * in the jAudio system.  Default is '/tmp/features.xml'
     * <li/><b>musicMode</b>: mode used to represent a song. Default is 'Song'
     * <li/><b>propertyID</b>: name of the property ID to store the Instance objects
     * in. Default is 'songVector'
     * <li/><b>instancesTitle</b>: name to give the Instances object. Default is
     * 'Training'
     * </ul>
     * @param map properties to load - null is permitted.
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
            parameter[0].setValue("Read Audio Files");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "audioDirectoryLocation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("audioDirectoryLocation") != null)) {
            parameter[1].setValue(map.getProperty("audioDirectoryLocation"));
        } else {
            parameter[1].setValue("/tmp/music/");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "settingsFile");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("settingsFile") != null)) {
            parameter[2].setValue(map.getProperty("settingsFile"));
        } else {
            parameter[2].setValue("/tmp/settings.xml");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "featuresFile");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("featuresFile") != null)) {
            parameter[3].setValue(map.getProperty("featuresFile"));
        } else {
            parameter[3].setValue("/tmp/features.xml");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "musicMode");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("musicMode") != null)) {
            parameter[4].setValue(map.getProperty("musicMode"));
        } else {
            parameter[4].setValue("Song");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "propertyID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("propertyID") != null)) {
            parameter[5].setValue(map.getProperty("propertyID"));
        } else {
            parameter[5].setValue("songVector");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "instancesTitle");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("instancesTitle") != null)) {
            parameter[6].setValue(map.getProperty("instancesTitle"));
        } else {
            parameter[6].setValue("Training");
        }

        props.setProperty("Type", "Actor");
        props.setProperty("Relation", (String)parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "GraphProperty");
        props.remove("Relation");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[5].getValue());
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String)parameter[4].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property",(String)parameter[5].getValue());
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);

    }
}
