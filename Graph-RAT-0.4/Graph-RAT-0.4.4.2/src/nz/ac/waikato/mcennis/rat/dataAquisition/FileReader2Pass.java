/*
 * MakeRecommendation2Pass.java
 *
 * Created on 4 October 2007, 08:10
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.UserIDList;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Reads a directory of FOAF descriptions as defined by the FOAF parser (the 
 * LiveJournal specific dialect) in two passes.  The first pass determines
 * which users are present in the directory.  The second pass only creates an actor
 * if the users are present, not just references.<br/>
 * <br/>
 * See the output descriptors for the description of the graph created.
 * 
 * @author Daniel McEnnis
 */
public class FileReader2Pass extends ModelShell implements DataAquisition {

    Graph graph = null;
    ParameterInternal[] parameter = new ParameterInternal[9];
    private OutputDescriptor[] output = new OutputDescriptor[12];
    Properties props = null;

    /** Creates a new instance of MakeRecommendation2Pass */
    public FileReader2Pass() {
        init(null);
    }

    @Override
    public void start() {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType", "FOAF");
        props.setProperty("Graph", "UserIDList");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        FileListCrawler crawler = new FileListCrawler();
        crawler.set(parser);
        parser = null;

//        java.io.File directory = new java.io.File("/research/data/backup-21-06-07");
        java.io.File directory = new java.io.File((String) parameter[1].getValue());
        java.io.File[] files = directory.listFiles();
        Logger.getLogger(FileReader2Pass.class.getName()).log(Level.INFO, "First Pass...");
        fireChange(Scheduler.SET_GRAPH_COUNT, 2);
        fireChange(Scheduler.SET_ALGORITHM_COUNT, files.length);
        for (int i = 0; i < files.length; ++i) {
            try {
                if (i % 100 == 0) {
                    Logger.getLogger(FileReader2Pass.class.getName()).log(Level.FINE, i + " of " + files.length);
                    ((Graph) crawler.getParser()[0].get()).commit();
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
                }
                crawler.crawl(files[i].getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((Graph) crawler.getParser()[0].get()).commit();

        UserIDList uil = (UserIDList) (crawler.getParser()[0].get());
        parser = new Parser[]{ParserFactory.newInstance().create(props)};
        parser[0].set(graph);
        ((Graph) parser[0].get()).add(uil);
        crawler.set(parser);
        parser = null;

        Logger.getLogger(FileReader2Pass.class.getName()).log(Level.INFO, "Second pass...");
        fireChange(Scheduler.SET_GRAPH_PROGRESS, 1);
        fireChange(Scheduler.SET_ALGORITHM_COUNT, files.length);
        for (int i = 0; i < files.length; ++i) {
            try {
                if (i % 100 == 0) {
                    Logger.getLogger(FileReader2Pass.class.getName()).log(Level.FINE, i + " of " + files.length);
                    ((Graph) crawler.getParser()[0].get()).commit();
                }
                crawler.crawl(files[i].getAbsolutePath());
                fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((Graph) crawler.getParser()[0].get()).commit();
        uil = null;
        graph = ((Graph) crawler.getParser()[0].get());
        graph.add(uil);
        if (((Boolean) parameter[3].getValue()).booleanValue()) {
            Logger.getLogger(FileReader2Pass.class.getName()).log(Level.INFO, "Anonymizing");
            graph.anonymize();
        }
        crawler = null;
//        graph.add((UserIDList)null);
    }

    @Override
    public void set(Graph g) {
        graph = g;
    }

    @Override
    public Graph get() {
        return graph;
    }

    /**
     * FIXME: write cancel operation
     */
    public void cancel() {
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
     * Initializes this object using the parameters. Also creates the input and 
     * output descriptors
     * 
     * <br><b>parameter[0]</b>
     * <br><i>Key-name:</i>'name'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> Name of this component.
     * <br>
     * <br><b>parameter[1]</b>
     * <br><i>Key-name:</i>'foafDirectory'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> Directory where the FOAF xml files are located
     * <br>
     * <br><b>parameter[2]</b>
     * <br><i>Key-name:</i>'pageDirectory'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> Directory where the web-page files are located 
     * <br>FIXME: page parsing not currently implemented
     * <br>
     * <br><b>parameter[3]</b>
     * <br><i>Key-name:</i>'anonymizing'
     * <br><i>Type:</i>java.lang.Boolean
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> Should users be anonymized.
     * <br>
     * <br><b>parameter[4]</b>
     * <br><i>Key-name:</i>'actorClass'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> Class of the actor that should be used to produce
     * users described by the FOAF files.
     * <br>
     * <br><b>parameter[5]</b>
     * <br><i>Key-name:</i>'actorType'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> The name of the 'mode' (type name) used to describe
     * users described by the FOAF files.
     * <br>
     * <br><b>parameter[6]</b>
     * <br><i>Key-name:</i>'linkClass'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> class of links created by FOAF 'Knows' links.
     * <br>
     * <br><b>parameter[7]</b>
     * <br><i>Key-name:</i>'linkType'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> Relation (arc type, edge type) created by FOAF 'Knows' links
     * <br>
     * <br><b>parameter[8]</b>
     * <br><i>Key-name:</i>'propertyClass'
     * <br><i>Type:</i>java.lang.String
     * <br><i>Structural:</i> true
     * <br><i>Description:</i> Class for the property objects created
     * <br>
     * 
     * @param map the key-value pairs to initialize this component
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
            parameter[0].setValue("File FOAF Reader");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "foafDirectory");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("foafDirectory") != null)) {
            parameter[1].setValue(map.getProperty("foafDirectory"));
        } else {
            parameter[1].setValue(java.io.File.listRoots()[0].getAbsolutePath());
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "pageDirectory");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("pageDirectory") != null)) {
            parameter[2].setValue(map.getProperty("pageDirectory"));
        } else {
            parameter[2].setValue("");
        }

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "anonymizing");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[3] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("anonymizing") != null)) {
            parameter[3].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("anonymizing"))));
        } else {
            parameter[3].setValue(new Boolean(false));
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorClass");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[4] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorClass") != null)) {
            parameter[4].setValue(map.getProperty("actorClass"));
        } else {
            parameter[4].setValue("Basic");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "actorType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[5] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("actorType") != null)) {
            parameter[5].setValue(map.getProperty("actorType"));
        } else {
            parameter[5].setValue("Artist");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkClass");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[6] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkClass") != null)) {
            parameter[6].setValue(map.getProperty("linkClass"));
        } else {
            parameter[6].setValue("Basic");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "linkType");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[7] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("linkType") != null)) {
            parameter[7].setValue(map.getProperty("linkType"));
        } else {
            parameter[7].setValue("User");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "propertyClass");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[8] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("propertyClass") != null)) {
            parameter[8].setValue(map.getProperty("propertyClass"));
        } else {
            parameter[8].setValue("Basic");
        }
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "Link");
        props.setProperty("Relation", "Knows");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "foaf:title");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "foaf:phone");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "foaf:gender");
        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "ya:country");
        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "ya:city");
        output[6] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "foaf:dateOfBirth");
        output[7] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "foaf:aimChatID");
        output[8] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "foaf:msnChatID");
        output[9] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "ya:bio");
        output[10] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "User");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property", "interest");
        output[11] = DescriptorFactory.newInstance().createOutputDescriptor(props);
    }
}
