/*
 * YahooArtistDecider.java
 *
 * Created on 11/12/2007, 12:43:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.artist.decider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mcennis.graphrat.crawler.CrawlerBase;
import org.mcennis.graphrat.parser.Parser;
import org.mcennis.graphrat.parser.ParserFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class YahooArtistDecider extends ArtistDeciderBase {
    
    CrawlerBase base;
    private static YahooArtistDecider instance = null;
    
    public static YahooArtistDecider newInstance(){
        if(instance == null){
            instance = new YahooArtistDecider();
        }
        return instance;
    }
    
    private YahooArtistDecider() {
        base = new CrawlerBase();
        Properties props = new Properties();
        props.setProperty("ParserType","YahooArtistDecider");
        base.set(new Parser[]{ParserFactory.newInstance().create(props)});
        base.getParser()[0].set(this);
    }
    
    protected void lookup(String name) {
        try {
            java.lang.String url = java.net.URLEncoder.encode(name, "UTF-8");
            url = "http://search.yahooapis.com/AudioSearchService/V1/artistSearch?appid=r.Yh5t3V34HJX4OjON3PG2ml8bvxL1jZEjnbABIzHTiZBHVsN89XM8Vnmd2wYg._Zjh7&artist="+url+"&results=50";
            base.crawl(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } 
    }
    
    public void setProxy(boolean p) {
        base.setProxy(p);
    }
    
}
