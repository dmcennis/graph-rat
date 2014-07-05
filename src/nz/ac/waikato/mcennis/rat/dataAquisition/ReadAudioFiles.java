/*

 * Created 27-3-08

 * Copyright Daniel McEnnis, saee license.txt

 */

package nz.ac.waikato.mcennis.rat.dataAquisition;



import jAudioFeatureExtractor.ACE.DataTypes.Batch;

import jAudioFeatureExtractor.ACE.DataTypes.FeatureDefinition;

import jAudioFeatureExtractor.ACE.XMLParsers.XMLDocumentParser;


import jAudioFeatureExtractor.Aggregators.AggregatorContainer;

import jAudioFeatureExtractor.DataModel;

import java.io.ByteArrayOutputStream;

import java.io.File;



import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

import org.dynamicfactory.descriptors.IODescriptorFactory;
import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptor.Type;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

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

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    Instances globalInstances;



    public ReadAudioFiles() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Read Audio Files");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Read Audio Files");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Crawler");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Directory",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("SettingsFile",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        name.add(new File("settings.xml"));
        properties.add(name);

        name = ParameterFactory.newInstance().create("FeaturesFile",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        name.add(new File("features.xml"));
        properties.add(name);

        name = ParameterFactory.newInstance().create("Mode",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Song");
        properties.add(name);

        name = ParameterFactory.newInstance().create("PropertyID",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("SongVector");
        properties.add(name);

        name = ParameterFactory.newInstance().create("InstancesID",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Training");
        properties.add(name);
    }



    public void start() {

        try {

            Object[] settings = (Object[]) XMLDocumentParser.parseXMLDocument(((File) properties.get("SettingsFile").get()).getAbsolutePath(), "batchFile");

            Batch base = loadSettings(settings);

            File audioDirectory = (File)properties.get("Directory").get();

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

        DataModel dm = new DataModel(((File) properties.get("FeaturesFile").get()).getAbsolutePath(), null);

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

        globalInstances = new Instances((String)properties.get("InstancesID").get(), attributes, 100);

        globalInstances.setClassIndex(attributes.size() - 1);

        Property prop = PropertyFactory.newInstance().create((String)properties.get("PropertyID").get(),Instances.class);

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

        Actor song = ActorFactory.newInstance().create((String)properties.get("Mode").get(),ID);
        Property prop = PropertyFactory.newInstance().create((String)properties.get("PropertyID").get(),Instances.class);

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

    public List<IODescriptor> getInputType() {

        return input;

    }



    @Override

    public List<IODescriptor> getOutputType() {

        return output;

    }



    @Override

    public Properties getParameter() {

        return properties;

    }



    @Override

    public Parameter getParameter(String param) {
        return properties.get(param);
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
        if(properties.check(map)){
            properties.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    (String)properties.get("Mode").get(),
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.GRAPH_PROPERTY,
                    (String)properties.get("Name").get(),
                    null,
                    null,
                    (String)properties.get("PropertyID").get(),"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    (String)properties.get("Mode").get(),
                    null,
                    (String)properties.get("PropertyID").get(),"");
            output.add(desc);
        }
    }

    public ReadAudioFiles prototype(){
        return new ReadAudioFiles();
    }

}

