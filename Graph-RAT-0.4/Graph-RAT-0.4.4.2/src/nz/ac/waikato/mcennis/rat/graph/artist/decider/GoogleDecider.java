/*
 * GoogleDecider.java
 *
 * Created on 8 June 2007, 16:52
 *
 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.graph.artist.decider;

import java.io.IOException;
import java.net.MalformedURLException;
import org.mcennis.graphrat.crawler.CrawlerBase;
import org.mcennis.graphrat.parser.Parser;
import org.mcennis.graphrat.parser.ParserFactory;
/**
 * Class for utilizing a Google music finder web query to determine if a string
 * matches an artist name or not.  Note: this algorithm hits Google's servers
 * enough that it rapidly triggers a banning - seeks permission before using.
 *
 * @author Daniel McEnnis
 * 
 */
public class GoogleDecider extends ArtistDeciderBase{
    
    CrawlerBase base;
    private static GoogleDecider instance = null;
    
    /**
     * Returns a static instance of this variable
     * @return
     */
    public static GoogleDecider newInstance(){
        if(instance == null){
            instance = new GoogleDecider();
        }
        return instance;
    }
    
    /** Creates a new instance of GoogleDecider */
    private GoogleDecider() {
        super();
        base = new CrawlerBase();
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ParserType","Google");
        base.set(new Parser[]{ParserFactory.newInstance().create(props)});
    }
    
    /**
     * Looks up the artist name on Google using a Google parser embedded in 
     * a single threaded CrawlerBase crawler.
     * @param name potential artist name.
     */
    protected void lookup(String name){
        try {
            base.crawl("http://www.google.com/musicsearch?q="+name);
            if(base.getParser()[0].get() != null){
                super.checked.add(name);
                super.artist.add(name);
            }else{
                super.checked.add(name);
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Should the web crawler use a proxy or not.
     * @param p true/false use a proxy
     */
    public void setProxy(boolean p){
        base.setProxy(p);
    }
    
}
