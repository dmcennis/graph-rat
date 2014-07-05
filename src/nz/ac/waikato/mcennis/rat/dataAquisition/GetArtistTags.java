/*

 * Created 1-5-08

 * 

 * Copyright Daniel McEnnis, see license.txt

 */

package nz.ac.waikato.mcennis.rat.dataAquisition;




import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.LineNumberReader;

import java.io.FileReader;

import java.io.UnsupportedEncodingException;

import java.util.LinkedList;

import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.crawler.Authenticator;

import nz.ac.waikato.mcennis.rat.crawler.CrawlerBase;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import org.dynamicfactory.descriptors.IODescriptorFactory;


import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptor.Type;
import org.dynamicfactory.descriptors.Parameter;

import org.dynamicfactory.descriptors.ParameterFactory;
import org.dynamicfactory.descriptors.ParameterInternal;

import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.descriptors.PropertiesInternal;
import org.dynamicfactory.descriptors.SyntaxCheckerFactory;
import org.dynamicfactory.descriptors.SyntaxObject;
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

    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();



    public GetArtistTags() {
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Reader 2 Pass");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Reader 2 Pass");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Category",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("File Crawler");
        properties.add(name);

        name = ParameterFactory.newInstance().create("UseProxy",Boolean.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,Boolean.class);
        name.setRestrictions(syntax);
        name.add(false);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyUser",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("dm75");
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyPassword",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,String.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ProxyLocation",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(0,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("proxy.cs.waikato.ac.nz");
        properties.add(name);

        name = ParameterFactory.newInstance().create("ToFileDirectory",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);

        name = ParameterFactory.newInstance().create("ArtistFile",File.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,File.class);
        name.setRestrictions(syntax);
        properties.add(name);
}



    public void start() {

        CrawlerBase crawler = new CrawlerBase();

        if((Boolean)properties.get("Proxy").get()){
            crawler.setProxy(true);
            crawler.setProxyHost((String)properties.get("ProxyLocation").get());
            crawler.setProxyPort(((Integer)properties.get("ProxyPort").get()).toString());
            crawler.setProxyType(((Integer)properties.get("ProxyType").get()).toString());
            Authenticator auth = new Authenticator();
            Authenticator.setUser((String)properties.get("ProxyUser").get());
            Authenticator.setPassword((String)properties.get("ProxyPassword").get());
            java.net.Authenticator.setDefault(auth);
        }else{
            crawler.setProxy(false);
        }

        properties.add("ParserClass","File");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(properties)};
        parser[0].set(graph);
        crawler.set(parser);



        LineNumberReader reader = null;

        try {

            int count = 0;

            reader = new LineNumberReader(new FileReader((File)properties.get("ArtistFile").get()));

            String line = reader.readLine();

            while (line != null) {

                if (!line.contentEquals("")) {

                    Logger.getLogger(GetArtistTags.class.getName()).log(Level.FINE,count + ": '" + line + "'");

                    try {

                        crawler.crawl(buildURL(line));

                        Thread.sleep(500);

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



    public List<IODescriptor> getInputType() {

        return input;

    }



    public List<IODescriptor> getOutputType() {

        return output;

    }



    public Properties getParameter() {
        return properties;
    }



    public Parameter getParameter(String param) {
        return properties.get(param);
    }

    /**

     * Parameters of this module:

     * <ul>

     * <li/><b>name</b>: name of this algorithm.

     * </ul>

     * @param map properties to load - null is permitted.

     */

    public void init(Properties map) {

        if(properties.check(map)){
            properties.merge(map);

            IODescriptor desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    "artist",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR,
                    (String)properties.get("Name").get(),
                    "tag",
                    null,
                    null,"");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.ACTOR_PROPERTY,
                    (String)properties.get("Name").get(),
                    "tag",
                    null,
                    "URL","");
            output.add(desc);

            desc = IODescriptorFactory.newInstance().create(
                    Type.LINK,
                    (String)properties.get("Name").get(),
                    "ArtistTag",
                    null,
                    null,"");
            output.add(desc);
        }
    }

    public GetArtistTags prototype(){
        return new GetArtistTags();
    }

}

