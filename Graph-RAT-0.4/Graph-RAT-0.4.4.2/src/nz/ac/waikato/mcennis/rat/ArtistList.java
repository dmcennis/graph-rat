/*
 * ArtistList.java
 *
 * Created on 3 May 2007, 22:17
 *
 * Copyright Daniel McEnnis published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.artist.decider.LastFMDecider;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.property.Property;


/**
 *
 * @author Daniel McEnnis
 */
public class ArtistList {
    
    /** Creates a new instance of ArtistList */
    public ArtistList() {
    }
    
    public static void main(String[] args) throws Exception{
        LastFMDecider decider = LastFMDecider.newInstance();
        decider.setParser("Basic");
        decider.setProxy(false);
        preloadArtistList(decider);
        
        FileListCrawler crawler = new FileListCrawler();
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType","FOAF");
        props.setProperty("GraphType","MemGraph");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        crawler.set(parser);
        java.io.File directory = new java.io.File("c:\\Users\\work\\Documents\\data\\LiveJournal\\");
        java.io.File[] foafFiles = directory.listFiles();
        for(int i=0;i<foafFiles.length;++i){
            crawler.crawl(foafFiles[i].getAbsolutePath());
            if(i%100==0){
                System.gc();
                System.out.println(i+"/"+foafFiles.length);
            }
        }
        Graph graph = ((Graph)crawler.getParser()[0].get());
        Actor[] user = graph.getActor("User");

        java.io.OutputStreamWriter logArtist = new java.io.OutputStreamWriter(new java.io.FileOutputStream("c:\\users\\work\\Documents\\artist.txt"));
        java.io.OutputStreamWriter logChecked = new java.io.OutputStreamWriter(new java.io.FileOutputStream("c:\\Users\\work\\Documents\\checked.txt"));
        for(int i=0;i<user.length;++i){
            Property property = user[i].getProperty("interest");
            if((property != null)&&(property.getPropertyClass().getName().contentEquals("java.lang.String"))){
                Object[] interest = property.getValue();
                System.out.println("User "+i);
                for(int j=0;j<interest.length;++j){
                        String propertyValue = (String)interest[j];
                    try {
                        
                        if(decider.isArtist(propertyValue)){
                            System.out.println("+");
                            logArtist.write(propertyValue);
                            logArtist.write("\n");
                            logArtist.flush();
                        }else{
                            System.out.println("-");
                        }
                        logChecked.write(propertyValue);
                        logChecked.write("\n");
                        logChecked.flush();
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    
                }
            }
        }
        logArtist.close();
        logChecked.close();
        java.io.FileWriter xmlOutput = new java.io.FileWriter("/research/decider.xml");
        decider.output(xmlOutput);
        xmlOutput.close();
    }
    
    public static void preloadArtistList(LastFMDecider decider){
        try {
            File artistList = new File("c:\\Users\\work\\Documents\\oldArtist.txt");
            File checkedList = new File("c:\\Users\\work\\Documents\\oldChecked.txt");
            java.io.LineNumberReader artistReader = new java.io.LineNumberReader(new java.io.FileReader(artistList));
            java.io.LineNumberReader checkedReader = new java.io.LineNumberReader(new java.io.FileReader(artistList));
            String line = artistReader.readLine();
            while((line != null)&&(!line.contentEquals(""))){
                decider.addArtist(line);
                line = artistReader.readLine();
            }
            artistReader.close();
            
            line = checkedReader.readLine();
            while((line != null)&&(!line.contentEquals(""))){
                decider.addChecked(line);
                line = checkedReader.readLine();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
