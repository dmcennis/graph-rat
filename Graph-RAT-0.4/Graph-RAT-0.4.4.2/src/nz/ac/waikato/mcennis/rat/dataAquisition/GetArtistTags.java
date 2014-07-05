/*
 * Created 1-5-08
 * 
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.Authenticator;
import nz.ac.waikato.mcennis.rat.crawler.CrawlerBase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;

/**
 * Single threaded crawler for parsing a set of predetermined artists from LastFM.
 * For every artist as listed in a text file, the set of all tags for that artist
 * is downloaded from LastFM using the LastFMArtistTag XML parser.<br/>
 * <br/>
 * See the output descriptors for a description of the graph created.
 * 
 * @author Daniel McEnnis
 */
public class GetArtistTags extends ModelShell implements DataAquisition {

    Graph graph = null;
    OutputDescriptor[] output = new OutputDescriptor[4];
    ParameterInternal[] parameter = new ParameterInternal[1];

    public GetArtistTags() {
        init(null);
    }

    public void start() {
        CrawlerBase crawler = new CrawlerBase();
        Authenticator.getPassword();
        crawler.setProxy(true);
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType", "File");
        props.setProperty("GraphType", "MemGraph");
        props.setProperty("ToFileDirectory", "/research/data/artistTags/");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        crawler.set(parser);

        LineNumberReader reader = null;
        try {
            int count = 0;
            reader = new LineNumberReader(new FileReader("/research/PopTracks/NZTop40ArtistList.txt"));
            String line = reader.readLine();
            while (line != null) {
                if (!line.contentEquals("")) {
                    Logger.getLogger(GetArtistTags.class.getName()).log(Level.FINE,count + ": '" + line + "'");
                    try {
                        crawler.crawl(buildURL(line));
                        java.lang.Thread.sleep(500);
                    } catch (Exception ex) {
                        Logger.getLogger(GetArtistTags.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                line = reader.readLine();
                count++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GetArtistTags.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GetArtistTags.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(GetArtistTags.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected String buildURL(String line) {
        if (line.matches(".*?&.*")) {
            line = line.split("\\s*&")[0];
        }
        if (line.matches(".*?Feat..*")) {
            line = line.split("\\s*Feat.")[0];
        }
        if (line.matches(".*?,.*")) {
            line = line.split("\\s*,")[0];
        }
        if (line.matches(".*?/.*")) {
            line = line.split("\\s*/")[0];
        }
        try {
            line = java.net.URLEncoder.encode(line, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GetArtistTags.class.getName()).log(Level.SEVERE, null, ex);
        }
        line = "http://ws.audioscrobbler.com/1.0/artist/"+line+"/toptags.xml";
        return line;
    }

    public void set(Graph g) {
        graph = g;
    }

    public Graph get() {
        return graph;
    }

    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputDescriptor[] getInputType() {
        return new InputDescriptor[]{};
    }

    public OutputDescriptor[] getOutputType() {
        return output;
    }

    public Parameter[] getParameter() {
        return parameter;
    }

    public Parameter getParameter(String param) {
        for (int i = 0; i < parameter.length; ++i) {
            if (parameter[i].getName().contentEquals(param)) {
                return parameter[i];
            }
        }
        return null;
    }

    public SettableParameter[] getSettableParameter() {
        return null;
    }

    public SettableParameter getSettableParameter(String param) {
        return null;
    }

    /**
     * Parameters of this module:
     * <ul>
     * <li/><b>name</b>: name of this algorithm.
     * </ul>
     * @param map properties to load - null is permitted.
     */
    public void init(Properties map) {
            Properties props = new Properties();
            props.setProperty("Type","java.lang.String");
            props.setProperty("Name","name");
            props.setProperty("Class","Basic");
            props.setProperty("Structural","true");
            parameter[0] = DescriptorFactory.newInstance().createParameter(props);
            if((map != null)&&(map.getProperty("name") != null)){
                parameter[0].setValue(map.getProperty("name"));
            }else{
                parameter[0].setValue("Crawl FOAF");
            }
        
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "artist");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","URL");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "ArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
    }
}
