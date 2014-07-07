/**
 * Aug 15, 2008-4:39:14 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.machinelearning;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.algorithm.Algorithm;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import org.dynamicfactory.model.ModelShell;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class for utilizing Weka Machine-Learning non-probabilistic Classifiers.  The source data are
 * the given Instance properties on the given source mode. (This assumes that the dataset of the Instance objects exist 
 * and is the same across all Instance objects in the mode.) The ground truth utilized is the
 * given groundTruthActor and groundTruthRelation.  Only one link may exist between
 * the source node and a groundTruthActor.  If this is violated, only the first link is used
 * and it may be non-deterministic which link is returned.
 * 
 * The analysis itself is performed by cross-validation (default 10-fold). Ultimately,
 * every source node will be classified to one groundTruthActor of relation type 
 * derivedRelation.
 * 
 * @author Daniel McEnnis
 */
public class WekaClassifierOneAttribute extends ModelShell implements Algorithm {

    ParameterInternal[] parameter = new ParameterInternal[11];
    OutputDescriptor[] output = new OutputDescriptor[1];
    InputDescriptor[] input = new InputDescriptor[2];

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
     * <br>Input 1 - ActorProperty
     * <br>Output 0 - Link
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

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[1].setValue(map.getProperty("actorType"));
        } else {
            parameter[1].setValue("User");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorProperty");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorProperty") != null)) {
            parameter[2].setValue(map.getProperty("actorProperty"));
        } else {
            parameter[2].setValue("instance");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "groundTruthActor");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("groundTruthActor") != null)) {
            parameter[3].setValue(map.getProperty("groundTruthActor"));
        } else {
            parameter[3].setValue("Artist");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "groundTruthRelation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("groundTruthRelation") != null)) {
            parameter[4].setValue(map.getProperty("groundTruthRelation"));
        } else {
            parameter[4].setValue("Given");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "derivedRelation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("derivedRelation") != null)) {
            parameter[5].setValue(map.getProperty("derivedRelation"));
        } else {
            parameter[5].setValue("Derived");
        }

        props.setProperty("Type", "java.lang.Class");
        props.setProperty("Name", "wekaClass");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("wekaClass") != null)) {
            try {
                parameter[6].setValue(Class.forName(map.getProperty("wekaClass")));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.SEVERE, "Clasifier set to J48", ex);
                parameter[6].setValue(J48.class);
            }
        } else {
            parameter[6].setValue(J48.class);
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "wekaOptions");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("wekaOptions") != null)) {
            parameter[7].setValue(map.getProperty("wekaOptions"));
        } else {
            parameter[7].setValue("");
        }

        props.setProperty("Type", "java.lang.Integer");
        props.setProperty("Name", "numberOfFolds");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("numberOfFolds") != null)) {
            parameter[8].setValue(Integer.valueOf(Integer.parseInt(map.getProperty("numberOfFolds"))));
        } else {
            parameter[8].setValue(new Integer(10));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "sourceAppendGraphID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[9] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("sourceAppendGraphID") != null)) {
            parameter[9].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("sourceAppendGraphID"))));
        } else {
            parameter[9].setValue(new Boolean(false));
        }
        
        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkAppendGraphID");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[10] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkAppendGraphID") != null)) {
            parameter[10].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("linkAppendGraphID"))));
        } else {
            parameter[10].setValue(new Boolean(false));
        }
        

        // init input 0
        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[3].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[0] = DescriptorFactory.newInstance().createInputDescriptor(props);

        // init input 3
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", (String) parameter[2].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        input[1] = DescriptorFactory.newInstance().createInputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", (String) parameter[9].getValue());
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }

    @Override
    public void execute(Graph g) {
        Actor[] source = g.getActor((String) parameter[1].getValue());
        if (source != null) {

            // create the Instance sets for each ac
            FastVector classTypes = new FastVector();
            FastVector sourceTypes = new FastVector();
            Actor[] dest = g.getActor((String) parameter[3].getValue());
            if (dest != null) {
                for (int i = 0; i < dest.length; ++i) {
                    classTypes.addElement(dest[i].getID());
                }
                Attribute classAttribute = new Attribute((String) parameter[5].getValue(), classTypes);

                Instance[] trainingData = new Instance[source.length];
                Instances masterSet = null;
                for (int i = 0; i < source.length; ++i) {


                    // First, acquire the instance objects for each actor
                    Property p = null;
                    if((Boolean)parameter[9].getValue()){
                        p = source[i].getProperty((String) parameter[2].getValue()+g.getID());
                    }else{
                        p = source[i].getProperty((String) parameter[2].getValue());                        
                    }
                    if (p != null) {
                        Object[] values = p.getValue();
                        if (values.length > 0) {
                            sourceTypes.addElement(source[i].getID());
                            trainingData[i] = (Instance) ((Instance) values[0]).copy();
                            // assume that this Instance has a backing dataset 
                            // that contains all Instance objects to be tested
                            if (masterSet == null) {
                                masterSet = new Instances(trainingData[i].dataset(), source.length);
                            }
                            masterSet.add(trainingData[i]);
                        } else {
                            trainingData[i] = null;
                            Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.WARNING, "Actor " + source[i].getType() + ":" + source[i].getID() + " does not have an Instance value of property ID " + p.getType());
                        }
                    } else {
                        trainingData[i] = null;
                        Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.WARNING, "Actor " + source[i].getType() + ":" + source[i].getID() + " does not have a property of ID " + p.getType());
                    }


                } // for every actor, fix the instance
                Attribute sourceID = new Attribute("sourceID", sourceTypes);
                masterSet.insertAttributeAt(sourceID, masterSet.numAttributes());
                masterSet.insertAttributeAt(classAttribute, masterSet.numAttributes());
                masterSet.setClass(classAttribute);
                for (int i = 0; i < source.length; ++i) {
                    if (trainingData[i] != null) {
                        trainingData[i].setValue(sourceID, source[i].getID());
                        Link[] link = g.getLinkBySource((String) parameter[4].getValue(), source[i]);
                        if (link == null) {
                            trainingData[i].setClassValue(Double.NaN);
                        } else {
                            trainingData[i].setClassValue(link[0].getDestination().getID());
                        }
                    }
                }

                String[] opts = ((String) parameter[7].getValue()).split("\\s+");
                Properties props = new Properties();
                if((Boolean)parameter[10].getValue()){
                    props.setProperty("LinkType", (String) parameter[5].getValue()+g.getID());
                }else{
                    props.setProperty("LinkType", (String) parameter[5].getValue());
                }
                props.setProperty("LinkClass", "Basic");
                try {
                    for (int i = 0; i < (Integer) parameter[8].getValue(); ++i) {
                        Instances test = masterSet.testCV((Integer) parameter[8].getValue(), i);
                        Instances train = masterSet.testCV((Integer) parameter[8].getValue(), i);
                        Classifier classifier = (Classifier) ((Class) parameter[6].getValue()).newInstance();
                        classifier.setOptions(opts);
                        classifier.buildClassifier(train);
                        for (int j = 0; j < test.numInstances(); ++j) {
                            String sourceName = sourceID.value((int) test.instance(j).value(sourceID));
                            double result = classifier.classifyInstance(test.instance(j));
                            String predicted = masterSet.classAttribute().value((int) result);
                            Link derived = LinkFactory.newInstance().create(props);
                            derived.set(g.getActor((String) parameter[2].getValue(), sourceName), 1.0, g.getActor((String) parameter[3].getValue(), predicted));
                            g.add(derived);
                        }
                    }
                } catch (InstantiationException ex) {
                    Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else { // dest==null
                Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.WARNING, "Ground truth mode '" + (String) parameter[3].getValue() + "' has no actors");
            }
        } else { // source==null
            Logger.getLogger(WekaClassifierOneAttribute.class.getName()).log(Level.WARNING, "Source mode '" + (String) parameter[2].getValue() + "' has no actors");
        }
    }
}
