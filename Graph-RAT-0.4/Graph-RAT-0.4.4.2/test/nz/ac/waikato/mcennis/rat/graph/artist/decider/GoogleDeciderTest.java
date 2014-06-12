/*
 * GoogleDeciderTest.java
 * JUnit based test
 *
 * Created on 11 June 2007, 16:43
 */

package nz.ac.waikato.mcennis.rat.graph.artist.decider;

import junit.framework.*;
import java.io.IOException;
import java.net.MalformedURLException;
import nz.ac.waikato.mcennis.rat.crawler.CrawlerBase;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;

/**
 *
 * @author work
 */
public class GoogleDeciderTest extends TestCase {
    
    GoogleDecider instance;
    CrawlerBase crawler;
    
    public GoogleDeciderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        instance = GoogleDecider.newInstance();
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of lookup method, of class nz.ac.waikato.mcennis.arm.graph.artist.decider.GoogleDecider.
     */
    public void testLookupSongNoArtist() {
        System.out.println("lookup SongNoArtist");
        
        String name = "Elanor Rigby";
        
        assertFalse(instance.isArtist(name));
        assertTrue(instance.isChecked(name));
    }
    
    public void testLookupNumberArtists(){
        System.out.println("lookup NumberArtist");
        String name = "Beatles";
        
        assertTrue(instance.isArtist(name));
        assertTrue(instance.isChecked(name));
    }
    
    public void testLookupNothing(){
        System.out.println("lookup Nothing");
        String name = "nosuchband";
        
        assertFalse(instance.isArtist(name));
        assertTrue(instance.isChecked(name));
    }
    
    public void testLookup0Artist(){
        System.out.println("lookup 0 Artists");
        String name = "For Whom the Bell Tolls";
        
        assertFalse(instance.isArtist(name));
        assertTrue(instance.isChecked(name));
    }
}
