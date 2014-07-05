/*

 * Created 7-5-08

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.dataAquisition;



import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import org.dynamicfactory.descriptors.IODescriptorFactory;

import org.dynamicfactory.descriptors.IODescriptor;

import org.dynamicfactory.descriptors.IODescriptor.Type;

import nz.ac.waikato.mcennis.rat.graph.model.ModelShell;

import nz.ac.waikato.mcennis.rat.parser.Parser;

import nz.ac.waikato.mcennis.rat.parser.ParserFactory;

import nz.ac.waikato.mcennis.rat.scheduler.Scheduler;



/**

 * Read a directory of files containing tags for each artist in an XML file read

 * by the LastFMArtistTag XML parser.

 * 

 * @author Daniel McEnnis

 */

public class LastFMTagFileReader extends ModelShell implements DataAquisition{


    PropertiesInternal properties = PropertiesFactory.newInstance().create();

    LinkedList<IODescriptor> input = new LinkedList<IODescriptor>();
    LinkedList<IODescriptor> output = new LinkedList<IODescriptor>();

    

    Graph graph = null;

    public LastFMTagFileReader(){
        ParameterInternal name = ParameterFactory.newInstance().create("AlgorithmClass",String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);
        name.setRestrictions(syntax);
        name.add("LastFM Tag File Reader");
        properties.add(name);

        name = ParameterFactory.newInstance().create("Name",String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);
        name.setRestrictions(syntax);
        name.add("LastFM Tag File Reader");
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


    }
    

    public void start() {

        fireChange(Scheduler.SET_GRAPH_COUNT,1);


        properties.set("ParserClass","LastFMArtistTag");

        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(properties)};

        parser[0].set(graph);

        FileListCrawler crawler  = new FileListCrawler();

        crawler.set(parser);

        

        File directory = (File)properties.get("Directory").get();
        File[] files = directory.listFiles();

        fireChange(Scheduler.SET_ALGORITHM_COUNT,files.length);

        for(int i=0;i<files.length;++i){

            try {

                if(i%100==0){

                    Logger.getLogger(LastFMTagFileReader.class.getName()).log(Level.FINE,i +" of "+files.length);

                    fireChange(Scheduler.SET_ALGORITHM_PROGRESS,i);

                }

                crawler.crawl(files[i].getAbsolutePath());

            } catch(Exception e){

                e.printStackTrace();

            }

        }

        

        graph = ((Graph)crawler.getParser()[0].get());

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

     * <li/><b>name</b>: name of this algorithm. Default is 'File LastFM Tag Reader'

     * <li/><b>artistTagDirectory</b>: location of the artist directory. Default 

     * is the first root from File.listRoots()

     * </ul>

     * @param map properties to load - null is permitted.

     */

    public void init(Properties map) {
        if(properties.check(map)){
            
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

    public LastFMTagFileReader prototype(){
        return new LastFMTagFileReader();
    }

}

