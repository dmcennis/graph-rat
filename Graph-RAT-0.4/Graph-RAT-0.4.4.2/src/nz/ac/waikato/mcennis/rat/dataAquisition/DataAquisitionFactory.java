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

/**
 * Class for creating DataAquisition Objects
 *
 * @author Daniel McEnnis
 * 
 */
public class DataAquisitionFactory {
    
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
    public DataAquisition create(java.util.Properties props){
        if(props.getProperty("aquisition")!= null){
            DataAquisition ret = null;
            if(props.getProperty("aquisition").compareToIgnoreCase("File Reader")==0){
                ret = new FileReader();
            }else if(props.getProperty("aquisition").compareToIgnoreCase("File Reader 2 Pass")==0){
                ret = new FileReader2Pass();
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Crawl LiveJournal")==0){
                ret = new CrawlLiveJournal();
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Load BibliographyXML")==0){
                ret = new LoadBibliographyXML();
                ret.init(props);
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Load Bibliography and Class")==0){
                ret = new LoadBibliographyAndClass();
                ret.init(props);
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Get Artist Tags")==0){
                ret = new GetArtistTags();
                ret.init(props);
            }else if(props.getProperty("aquisition").compareToIgnoreCase("File Artist Tag Reader")==0){
                ret = new LastFMTagFileReader();
                ret.init(props);
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Crawl LastFM")==0){
                ret = new CrawlLastFM();
                ret.init(props);
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Read LastFM Profile File")==0){
                ret = new ReadLastFMProfileFile();
                ret.init(props);
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Read MemGraph XML File")==0){
                ret = new ReadMemGraphXMLFile();
                ret.init(props);
            }else if(props.getProperty("aquisition").compareToIgnoreCase("Read Audio Files")==0){
                ret = new ReadMemGraphXMLFile();
                ret.init(props);
            }else{
                Logger.getLogger(DataAquisitionFactory.class.getName()).log(Level.SEVERE,"Aquisition Module '"+props.getProperty("aquisition")+"' not found");
                return null;
            }
            ret.init(props);
            return ret;
        }else{
            return null;
        }
    }
    
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
}
