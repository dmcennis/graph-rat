/*

 * DataAquisitionFactory.java

 *

 * Created on 3 October 2007, 14:03

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.dataAquisition;



import java.util.LinkedList;

import java.util.logging.Level;

import java.util.logging.Logger;

import nz.ac.waikato.bibliography.LoadBibliographyAndClass;

import nz.ac.waikato.bibliography.LoadBibliographyXML;

import nz.ac.waikato.mcennis.rat.AbstractFactory;

import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;

import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;

import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;

import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;

import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxCheckerFactory;

import nz.ac.waikato.mcennis.rat.graph.descriptors.SyntaxObject;



/**

 * Class for creating DataAquisition Objects

 *

 * @author Daniel McEnnis

 * 

 */

public class DataAquisitionFactory extends AbstractFactory<DataAquisition>{

    

    /**

     * Singelton instance of the DataAquisitionFactory

     */

    static DataAquisitionFactory instance = null;

    

    /**

     * acquire a reference to this singleton

     * @return this class's static instance object initialized

     */

    public static DataAquisitionFactory newInstance(){

        if(instance == null){

            instance = new DataAquisitionFactory();

        }

        return instance;

    }

          

    /** Creates a new instance of DataAquisitionFactory */

    private DataAquisitionFactory() {

        ParameterInternal name = ParameterFactory.newInstance().create("DataAquisitionClass",String.class);

        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1,1,null,String.class);

        name.setRestrictions(syntax);

        name.add("FileReader2Pass");

        properties.add(name);

        

        name = ParameterFactory.newInstance().create("Category",String.class);

        syntax = SyntaxCheckerFactory.newInstance().create(1,Integer.MAX_VALUE,null,String.class);

        name.setRestrictions(syntax);

        properties.add(name);

        

        map.put("File Reader",new FileReader());

        map.put("File Reader 2 Pass",new FileReader2Pass());

        map.put("Crawl LiveJournal",new CrawlLiveJournal());

        map.put("Load BibliographyXML",new LoadBibliographyXML());

        map.put("Load Bibliography and Class",new LoadBibliographyAndClass());

        map.put("Get Artist Tags",new GetArtistTags());

        map.put("File Artist Tag Reader",new LastFMTagFileReader());

        map.put("Crawl LastFM",new CrawlLastFM());

        map.put("Read LastFM Profile File",new ReadLastFMProfileFile());

        map.put("Read MemGraph XML File",new ReadMemGraphXMLFile());

        map.put("Read Audio Files",new ReadAudioFiles());

    }

    

       /**

     * Creates a new DataAquisition object using the 'aquisition' property.  See the

     * init() function of each object for parameter info.

     * 

     * <ul>

     * <li/>'File Reader' - single pass file reader of FOAF files

     * <li/>'File Reader 2 Pass' - two pass file reader of FOAF files

     * <li/>'Crawl LiveJournal' - crawls LiveJournal using given files as seeds

     * <li/>'GetArtistTags' - crawl LastFm for artist tags

     * <li/>'Load BibliographyXML' - load bibliographic info from an XML file with similar info to bibtex journal entries

     * <li/>'Load Bibliography and Class' - load bibliographic info from an XML file and class id ground truth from another XML file

     * <li/>'File Artist Tag Reader' - Read a directory of ArtistTag XML files

     * <li/>'Crawl LastFM' - crawl LastFm by spidering on usernames and artists

     * <li/>'Read LastFM Profile File' - Parse the XML files in the directory structure created by CrawlLastFM in a tar format

     * <li/>'Read MemGraph XML File' - read an arbitrary MemGraph XML serialization

     * </ul>

     *

     * @param props key-value pairs used to configure 

     * @return DataAquisition object requested

     */



    public String[] getKnownModules(){

        LinkedList<String> knownModules = new LinkedList<String>();

        knownModules.add("File Reader");

        knownModules.add("File Artist Tag Reader");

        knownModules.add("File Reader 2 Pass");

        knownModules.add("Crawl LiveJournal");

        knownModules.add("Get Artist Tags");

        knownModules.add("Crawl LastFM");

        knownModules.add("Read LastFM Profile File");

        knownModules.add("Read MemGraph XML File");

        knownModules.add("Load BibliographyXML");

        knownModules.add("Load Bibliography and Class");

        knownModules.add("Read Audio Files");

        return knownModules.toArray(new String[]{});

    }

    

    public DataAquisition create(String name){

        return create(name,properties);

    }

    

    public DataAquisition create(String name, Properties parameters){

        String classType = "";

        if ((parameters.get("DataAquisitionClass") != null) && (parameters.get("DataAquisitionClass").getParameterClass().getName().contentEquals(String.class.getName()))) {

            classType = (String) parameters.get("DataAquisitionClass").getValue().iterator().next();

        } else {

            classType = (String) properties.get("DataAquisitionClass").getValue().iterator().next();

        }

        DataAquisition ret = null;

        if(map.containsKey(classType)){

            ret = (DataAquisition)map.get(classType).prototype();

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"Data Aquisition class '"+classType+"' not found - assuming 'File Reader 2 Pass'");

            ret = new FileReader2Pass();

        }

        ret.init(parameters);

        return ret;

    }



    @Override

    public DataAquisition create(Properties props) {

        return create(null,props);

    }



    @Override

    public boolean check(Properties parameters) {

        String classType = "";

        if ((parameters.get("DataAquisitionClass") != null) && (parameters.get("DataAquisitionClass").getParameterClass().getName().contentEquals(String.class.getName()))) {

            classType = (String) parameters.get("DataAquisitionClass").getValue().iterator().next();

        } else {

            classType = (String) properties.get("DataAquisitionClass").getValue().iterator().next();

        }



        if(map.containsKey(classType)){

            return map.get(classType).getParameter().check(parameters);

        }else{

            Logger.getLogger(this.getClass().getName()).log(Level.INFO,"DataAquisition '"+classType+"' does not exist");

            return false;

        }

    }

    

    @Override

    public Properties getParameter(){

        String classType = (String) properties.get("DataAquisitionClass").getValue().iterator().next();

        if(map.containsKey(classType)){

            return map.get(classType).getParameter();

        }else{

            return properties;

        }

    }

    

    public Parameter getClassParameter(){

        return properties.get("DataAquisitionClass");

    }

    

}

