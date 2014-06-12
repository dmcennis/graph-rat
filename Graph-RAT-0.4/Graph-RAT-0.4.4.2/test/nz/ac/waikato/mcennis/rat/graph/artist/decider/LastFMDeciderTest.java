/*
 * LastFMDeciderTest.java
 * JUnit based test
 *
 * Created on 11 June 2007, 16:43
 */

package nz.ac.waikato.mcennis.rat.graph.artist.decider;

import junit.framework.*;
import nz.ac.waikato.mcennis.rat.crawler.WebCrawler;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.ParserFactory;
import nz.ac.waikato.mcennis.rat.parser.XMLParser;
import nz.ac.waikato.mcennis.rat.parser.LastFmBasicArtistParser;
import nz.ac.waikato.mcennis.rat.crawler.Authenticator;

/**
 *
 * @author work
 */
public class LastFMDeciderTest extends TestCase {
    
    LastFMDecider test;
    
    public LastFMDeciderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        test = LastFMDecider.newInstance();
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of setParser method, of class nz.ac.waikato.mcennis.arm.graph.artist.decider.LastFMDecider.
     */
    public void testSetParserXML() {
        System.out.println("setParser XML");
        
        String type = "XML";
        
        test.setParser(type);
        assertTrue(test.crawler.getParser()[0] instanceof XMLParser);
    }
    
    public void testSetParserBasic(){
        System.out.println("setParser Basic");
        
        String type = "Basic";
        
        test.setParser(type);
        assertTrue(test.crawler.getParser()[0] instanceof LastFmBasicArtistParser);
    }

    /**
     * Test of lookup method, of class nz.ac.waikato.mcennis.arm.graph.artist.decider.LastFMDecider.
     */
    public void testXMLLookup() {
        System.out.println("lookup XML Beatles");
        
        String name = "Beatles";
        
        test.setParser("XML");
        test.setProxy(true);
        
        
        
        assertTrue(test.isArtist(name));
        assertTrue(test.isChecked(name));
        assertTrue(test.artist.contains("The Who"));
        assertTrue(test.checked.contains("The Who"));  
    }
    
    public void testXMLLookupFail(){
        System.out.println("lookup XML nosuchband");
        
        String name = "nosuchband";
        
        test.setParser("XML");
        test.setProxy(true);
        
        assertFalse(test.isArtist(name));
        assertTrue(test.isChecked(name));
    }
    
    public void testBasicLookup(){
        System.out.println("lookup basic Metallica");
        String name = "Metallica";
        
        test.setParser("Basic");
        test.setProxy(true);
        
        assertTrue(test.isArtist(name));
        assertTrue(test.isChecked(name));
    }
    
    public void testBasicLookupFail(){
        System.out.println("lookup basic nosuchband");
        String name = "nosuchband";
        
        test.setParser("Basic");
        test.setProxy(true);
        
        assertFalse(test.isArtist(name));
        assertTrue(test.isChecked(name));
    }
}
