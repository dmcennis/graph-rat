/**
 *
 * Created Jun 7, 2008 - 10:06:19 PM
 * Copyright Daniel McEnnis, see license.txt
 *
 */
package nz.ac.waikato.mcennis.rat.dataAquisition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.DescriptorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.ParameterInternal;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;
import nz.ac.waikato.mcennis.rat.parser.XMLParser;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.GraphReader;

/**
 * Class for reading an arbitrary MemGraph XML file.  Uses the GraphReader XML 
 * Parser. THe DTD is as follows:
 * 
 * <pre>
 * &lt;!DOCTYPE dataObject [
 *   &lt;!ELEMENT dataObject (graph)&gt;
 *   &lt;!ELEMENT graph (graphClass,graphName,graphProperties*,pathSet*,user*,userLink*,graph*)&gt;
 *   &lt;!ELEMENT graphClass (#PCDATA)&gt;
 *   &lt;!ELEMENT graphName (#PCDATA)&gt;
 *   &lt;!ELEMENT graphProperties (gClass,gValueClass,gType,gValue+)&gt;
 *   &lt;!ELEMENT gClass (#PCDATA)&gt;
 *   &lt;!ELEMENT gValueClass (#PCDATA)&gt;
 *   &lt;!ELEMENT gType (#PCDATA)&gt;
 *   &lt;!ELEMENT gValue (#PCDATA)&gt;
 *   &lt;!ELEMENT pathSet (path+)&gt;
 *   &lt;!ELEMENT path (actor+)&gt;
 *   &lt;!ELEMENT actor (actorType,actorID)&gt;
 *   &lt;!ELEMENT actorType (#PCDATA)&gt;
 *   &lt;!ELEMENT actorID (#PCDATA)&gt;
 *   &lt;!ELEMENT user (userClass,ID,properties*,page*)&gt;
 *   &lt;!ELEMENT ID (#PCDATA)&gt;
 *   &lt;!ELEMENT properties (propertiesClass,valueClass,type,value*)&gt;
 *   &lt;!ELEMENT type (#PCDATA)&gt;
 *   &lt;!ELEMENT value (#PCDATA)&gt;
 *   &lt;!ELEMENT valueClass (#PCDATA)&gt;
 *   &lt;!ELEMENT userLink (uClass,uStrength,uSourceType,uSourceID,uDestinationType,uDestinationID,uProperties)&gt;
 *   &lt;!ELEMENT uClass (#PCDATA)&gt;
 *   &lt;!ELEMENT uSourceType (#PCDATA)&gt;
 *   &lt;!ELEMENT uSourceID (#PCDATA)&gt;
 *   &lt;!ELEMENT uStrength (#PCDATA)&gt;
 *   &lt;!ELEMENT uDestinationType (#PCDATA)&gt;
 *   &lt;!ELEMENT uDestinationID (#PCDATA)&gt;
 *   &lt;!ELEMENT uProperties (uPropertiesClass,uValueClass,uPropertiesType,uPropertiesValue+)&gt;
 *   &lt;!ELEMENT uPropertiesClass (#PCDATA)&gt;
 *   &lt;!ELEMENT uPropertiesValueClass (#PCDATA)&gt;
 *   &lt;!ELEMENT uPropertiesType (#PCDATA)&gt;
 *   &lt;!ELEMENT uPropertiesValue (#PCDATA)&gt;
 *   &lt;!ELEMENT page (#PCDATA)&gt;
 * ]&gt;
 * </pre>
 * @author Daniel McEnnis
 */
public class ReadMemGraphXMLFile extends ModelShell implements DataAquisition {

    ParameterInternal[] parameter = new ParameterInternal[3];
    Graph graph = null;

    public ReadMemGraphXMLFile() {
        init(null);
    }

    @Override
    public void start() {
        XMLParser parseXML = new XMLParser();
        GraphReader handler = new GraphReader();
        parseXML.setHandler(handler);
        parseXML.set(graph);
        InputStream input = null;
        try{
        if((Boolean)parameter[2].getValue()){
            input = new GZIPInputStream(new FileInputStream((String)parameter[1].getValue()));
        }else{
            input = new FileInputStream((String)parameter[1].getValue());
        }
        parseXML.parse(input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadMemGraphXMLFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadMemGraphXMLFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReadMemGraphXMLFile.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            
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
     * <li/><b>name</b>: Name of the algorithm. Default is 'File LastFM Tag Reader'
     * <li/><b>fileLocation</b>: location of the file to be read. Default is the
     * first entry in File.listRoots()
     * <li/><b>gzipped</b>: is this file GZipped compressed. Default is 'true'
     * </ul>
     * 
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

        props.setProperty("Type", "java.lang.Boolean");
        props.setProperty("Name", "gzipped");
        props.setProperty("Class", "Basic");
        props.setProperty("Structural", "true");
        parameter[2] = DescriptorFactory.newInstance().createParameter(props);
        if ((map != null) && (new Boolean(map.getProperty("gzipped") != null))) {
            parameter[2].setValue(new Boolean(Boolean.parseBoolean(map.getProperty("gzipped"))));
        } else {
            parameter[2].setValue(new Boolean(true));
        }

    }
}
