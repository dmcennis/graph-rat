/**
 *
 * Created Jun 7, 2008 - 10:06:19 PM
 * Copyright Daniel McEnnis, see license.txt
 *
 */
package org.mcennis.graphrat.dataAquisition;

import com.ice.tar.TarEntry;
import com.ice.tar.TarInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.mcennis.graphrat.graph.Graph;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.dynamicfactory.model.ModelShell;
import org.mcennis.graphrat.parser.Parser;
import org.mcennis.graphrat.parser.xmlHandler.LastFMUserExpansion;

/**
 * Class for parsing a (possibly compressed) tar file containing the files created
 * by the CrawlLastFM module.
 * 
 * @author Daniel McEnnis
 */
public class ReadLastFMProfileFile extends ModelShell implements DataAquisition {

    ParameterInternal[] parameter = new ParameterInternal[2];
    OutputDescriptor[] output = new OutputDescriptor[22];
    Graph graph = null;

    public ReadLastFMProfileFile() {
        init(null);
    }

    @Override
    public void start() {
        // open the tar file for reading
        TarInputStream input = null;
        int count = 1;
        try {
            input = new TarInputStream(new GZIPInputStream(new FileInputStream((String) parameter[1].getValue())));
            LastFMUserExpansion userExpansion = new LastFMUserExpansion();
            Parser[] parsers = userExpansion.setUpParsers(null, graph);
            TarEntry entry = input.getNextEntry();
            while (entry != null) {
                if (count % 1000 == 0) {
                    Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.INFO, "Reading file " + count);
                }
                String name = entry.getName();
                String[] pieces = name.split("/");
                if (!entry.isDirectory() && pieces.length > 1) {
                    ByteArrayOutputStream copyOfFile = new ByteArrayOutputStream();
                    input.copyEntryContents(copyOfFile);
                    ByteArrayInputStream data = new ByteArrayInputStream(copyOfFile.toByteArray());
                    if (pieces[0].contentEquals("ArtistDirectory")) {
                        try {
                            parsers[4].parse(data);
                        } catch (Exception ex) {
                            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                        }
                    } else {
                        if (pieces[2].contentEquals("friends.xml")) {
                            try {
                                parsers[5].parse(data);
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("topArtists.xml")) {
                            try {
                                parsers[1].parse(data);
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("profile.xml")) {
                            try {
                                parsers[0].parse(data);
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("tags.xml")) {
                            try {
                                parsers[3].parse(data);
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else if (pieces[2].contentEquals("neighbours.xml")) {
                            try {
                                parsers[6].parse(data);
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        } else {
                            try {
                                parsers[2].parse(data);
                            } catch (Exception ex) {
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, name);
                                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.FINER, null, ex);
                            }
                        }
                    }
                }
                count++;
                entry = input.getNextEntry();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadLastFMProfileFile.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputDescriptor[] getInputType() {
        return new InputDescriptor[]{};
    }

    @Override
    public OutputDescriptor[] getOutputType() {
        return new OutputDescriptor[]{};
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
     * <li/>
     * <li/>
     * </ul>
     * @param map  properties to load - null is permitted.
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
            parameter[0].setValue("File LastFM Tag Reader");
        }

        props.setProperty("Type", "java.lang.String");
        props.setProperty("Name", "fileLocation");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[1] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (map.getProperty("fileLocation") != null)) {
            parameter[1].setValue(map.getProperty("fileLocation"));
        } else {
            parameter[1].setValue(java.io.File.listRoots()[0].getAbsolutePath());
        }

        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[0] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","LastFM Homepage");
        output[1] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Age");
        output[2] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Avatar");
        output[3] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Country");
        output[4] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Gender");
        output[5] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Icon");
        output[6] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","MBox");
        output[7] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","PlayCount");
        output[8] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","name");
        output[9] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Cluster ID");
        output[10] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "user");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","User ID");
        output[11] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "artist");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[12] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Actor");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[13] = DescriptorFactory.newInstance().createOutputDescriptor(props);

        props.setProperty("Type", "ActorProperty");
        props.setProperty("Relation", "tag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","URL");
        output[14] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "ArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[15] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "UserTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[16] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "UserArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[17] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "LinkProperty");
        props.setProperty("Relation", "UserArtistTag");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.setProperty("Property","Tags");
        output[18] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "ListensTo");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[19] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "Similar");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[20] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
        props.setProperty("Type", "Link");
        props.setProperty("Relation", "Friend");
        props.setProperty("AlgorithmName", (String) parameter[0].getValue());
        props.remove("Property");
        output[21] = DescriptorFactory.newInstance().createOutputDescriptor(props);
       
    }
}
