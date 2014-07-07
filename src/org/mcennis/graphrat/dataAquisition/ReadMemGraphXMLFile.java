/**

 *

 * Created Jun 7, 2008 - 10:06:19 PM

 * Copyright Daniel McEnnis, see license.txt

 *

 */

package org.mcennis.graphrat.dataAquisition;





import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.InputStream;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;
import org.dynamicfactory.descriptors.*;

import java.util.zip.GZIPInputStream;

import org.mcennis.graphrat.graph.Graph;



import org.mcennis.graphrat.descriptors.IODescriptor;

import org.dynamicfactory.model.ModelShell;


import org.mcennis.graphrat.parser.XMLParser;

import org.mcennis.graphrat.parser.xmlHandler.GraphReader;




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



    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    Graph graph = null;



    public ReadMemGraphXMLFile() {

        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("Read MemGraph XML");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("Read MemGraph XML");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Reader");
        properties.add(name);

        name = ParameterFactory.newInstance().create("File",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("Compressed",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(true);
        properties.add(name);

    }



    @Override

    public void start() {

        XMLParser parseXML = new XMLParser();

        GraphReader handler = new GraphReader();

        parseXML.setHandler(handler);

        parseXML.set(graph);

        InputStream input = null;

        try{

        if((Boolean)properties.get("Compressed").get()){

            input = new GZIPInputStream(new FileInputStream((File)properties.get("File").get()));

        }else{

            input = new FileInputStream((File)properties.get("File").get());

        }

        parseXML.parse(input,((File)properties.get("File").get()).getAbsolutePath());

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

     * <li/><b>name</b>: Name of the algorithm. Default is 'File LastFM Tag Reader'

     * <li/><b>fileLocation</b>: location of the file to be read. Default is the

     * first entry in File.listRoots()

     * <li/><b>gzipped</b>: is this file GZipped compressed. Default is 'true'

     * </ul>

     * 

     * @param map properties to load - null is permitted.

     */ 

    public void init(Properties map) {
        if(properties.check(map)){
            properties.merge(map);
        }
    }

    public ReadMemGraphXMLFile prototype(){
        return new ReadMemGraphXMLFile();
    }

}

