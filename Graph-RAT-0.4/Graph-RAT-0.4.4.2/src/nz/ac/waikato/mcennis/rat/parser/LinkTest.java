/*
 * LinkTest.java
 *
 * Created on 3 May 2007, 15:30
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
 */

package nz.ac.waikato.mcennis.rat.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.waikato.mcennis.rat.crawler.Crawler;
import nz.ac.waikato.mcennis.rat.crawler.WebCrawler;

/**
 * Dummy class designed purely to test spidering capabilities
 *
 * @author Daniel McEnnis
 * 
 */
public class LinkTest implements Parser{
    
    String id = "LinkTest";
    /** Creates a new instance of LinkTest */
    public LinkTest() {
    }

    /**
     * Intentionally a no-op
     * @param data ignored
     */
    public void parse(InputStream data) {
        //deliberately empty
        ;
    }

    @Override
    public Parser duplicate() {
        return new LinkTest();
    }

    /**
     * Parses CCL website - ignores data
     * @param data ignores
     * @param crawler crawler to be tested
     */
    public void parse(InputStream data, Crawler crawler) {
        try {
            crawler.crawl("http://www.ccl.net");
        } catch (MalformedURLException ex) {
            Logger.getLogger(LinkTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LinkTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns null
     * @return null
     */
    public ParsedObject get(){
        return null;
    }

    /**
     * Deliberately a no-op
     * @param o ignored
     */
    public void set(ParsedObject o) {
        ;//intentionally null
    }

    public void setName(String name) {
        id = name;
    }

    public String getName() {
        return id;
    }
}
