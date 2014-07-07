/*
 * MakeRecommendation.java
 *
 * Created on 4 October 2007, 08:09
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */
package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;

/**
 * Reads FOAF files in the given directory in one pass.
 * FIXME: Parse web-pages, reviews, and blogs.
 *
 * @author Daniel McEnnis
 * 
 */
public class FileReader extends ModelShell implements DataAquisition {

    private ParameterInternal[] parameter = new ParameterInternal[6];
    private OutputDescriptor[] output = new OutputDescriptor[12];
    private Graph graph = null;

    /** Creates a new instance of MakeRecommendation */
    public FileReader() {
        init(null);
    }

    @Override
    public void start() {
        fireChange(Scheduler.SET_GRAPH_COUNT, 1);
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType", "FOAF");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        parser[0].set(graph);
        FileListCrawler crawler = new FileListCrawler();
        crawler.set(parser);

        java.io.File directory = new java.io.File((String) parameter[1].getValue());
        java.io.File[] files = directory.listFiles();
        fireChange(Scheduler.SET_ALGORITHM_COUNT, files.length);
        for (int i = 0; i < files.length; ++i) {
            try {
                if (i % 100 == 0) {
                    Logger.getLogger(FileReader.class.getName()).log(Level.FINE, i + " of " + files.length);
                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS, i);
                }
                crawler.crawl(files[i].getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        graph = ((Graph) crawler.getParser()[0].get());

        if (((Boolean) parameter[3].getValue()).booleanValue()) {
            graph.anonymize();
        }
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
     * Initializes the FileReader object with up to 4 parameters.  
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
     *  
     * 
     * 
     * @param map key-value pairs used to set parameters.
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
