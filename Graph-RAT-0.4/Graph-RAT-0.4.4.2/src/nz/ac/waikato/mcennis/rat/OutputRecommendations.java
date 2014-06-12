/*
 * MakeRecommendations.java
 *
 * Created on 13 June 2007, 22:31
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat;

import nz.ac.waikato.mcennis.rat.crawler.FileListCrawler;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.parser.XMLParser;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.artist.decider.BasicArtistDecider;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AddBasicInterestLink;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AddMusicLinks;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AddMusicRecommendations;
import nz.ac.waikato.mcennis.rat.graph.algorithm.AddMusicReferences;
import nz.ac.waikato.mcennis.rat.graph.algorithm.TrimEdgeUsers;

/**
 *
 * @author work
 */
public class OutputRecommendations {
    
    /** Creates a new instance of MakeRecommendations */
    public OutputRecommendations() {
        
        
    }
    
    public static void main(String[] args) throws Exception{
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType","FOAF");
        props.setProperty("GraphType","MemGraph");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        FileListCrawler crawler  = new FileListCrawler();
        crawler.set(parser);
        
        java.io.File directory = new java.io.File("c:\\Users\\work\\Documents\\data\\LiveJournal");
        java.io.File[] files = directory.listFiles();
        for(int i=0;i<files.length;++i){
            try {
                if(i%100==0){
                    System.out.println(i +" of "+files.length);
                }
                crawler.crawl(files[i].getAbsolutePath());
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        
       
        
        MemGraph graph = ((MemGraph)crawler.getParser()[0].get());

        System.out.println("Trimming Users");
        TrimEdgeUsers trimmer = new TrimEdgeUsers();
        trimmer.execute(graph);
        trimmer = null;
        
        System.out.println("Adding Interests");
        AddBasicInterestLink interest = new AddBasicInterestLink();
        interest.execute(graph);
        interest = null;
 
        System.out.println("Loading decider");
        BasicArtistDecider decider = new BasicArtistDecider();
        loadDecider(decider);
        
        System.out.println("Adding artist references");
        AddMusicReferences references = new AddMusicReferences();
        references.setDecider(decider);
        references.execute(graph);
        decider = null;
        references = null;
        
        System.out.println("Adding music links");
        AddMusicLinks musicLinks = new AddMusicLinks();
        musicLinks.execute(graph);
        musicLinks = null;
        
        System.out.println("Adding Recommendations");
        AddMusicRecommendations recommend = new AddMusicRecommendations();
        recommend.execute(graph);
        recommend = null;
        
        Link[] recommendations = graph.getLink("Derived");
        java.util.Arrays.sort(recommendations);
        for(int i=recommendations.length-1;i>recommendations.length-11;--i){
            System.out.println("User "+recommendations[i].getSource().getID()+" will like "+recommendations[i].getDestination().getID()+" with strength "+recommendations[i].getStrength());
        }
    }
    
    static public void loadDecider(BasicArtistDecider d) throws Exception{
        java.io.LineNumberReader reader = new java.io.LineNumberReader(new java.io.FileReader("c:\\Users\\work\\Documents\\artist.txt"));
        String input = reader.readLine();
        while((input!=null)&&(!input.contentEquals(""))){
            d.addArtist(input);
            input = reader.readLine();
        }
        reader.close();
    }
}
