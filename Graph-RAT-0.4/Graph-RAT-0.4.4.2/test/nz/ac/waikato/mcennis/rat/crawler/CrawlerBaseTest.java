/*
 * CrawlerBaseTest.java
 * JUnit based test
 *
 * Created on 3 May 2007, 15:18
 * copyright Daniel McEnnis - all rights reserved
 */

package org.mcennis.graphrat.crawler;

import junit.framework.*;
import java.io.IOException;

import org.mcennis.graphrat.parser.Parser;
import org.mcennis.graphrat.parser.NullParser;
import org.mcennis.graphrat.parser.ParserFactory;

/**
 *
 * @author dm75
 */
public class CrawlerBaseTest extends TestCase {
    
    public CrawlerBaseTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of set method, of class nz.ac.waikato.mcennis.arm.crawler.CrawlerBase.
     */
    public void testSet() {
        System.out.println("set");
                            java.util.Properties props = new java.util.Properties();
                    props.setProperty("ParserType","HTMLParser");
        Parser[] parser = new Parser[]{ParserFactory.newInstance().create(props)};
        CrawlerBase instance = new CrawlerBase();
        
        instance.set(parser);
        
    }

    /**
     * Test of crawl method, of class nz.ac.waikato.mcennis.arm.crawler.CrawlerBase.
     */
    public void testLocalCrawl() throws IOException{
        System.out.println("local crawl");
       
        String site = "http://www.waikato.ac.nz";
        CrawlerBase instance = new CrawlerBase();
        NullParser base = new NullParser();
        Parser[] parser = new Parser[]{base};
        instance.set(parser);
        
        instance.crawl(site);
        Parser[] result = instance.getParser();
        assertNotNull(((NullParser)result[0]).content);
        assertFalse(((NullParser)result[0]).content.contentEquals(""));
    }
    
    public void testRemoteCrawl() throws IOException{
          System.out.println("remote crawl");
       
        String site = "http://www.ccl.net";
        CrawlerBase instance = new CrawlerBase();
        NullParser base = new NullParser();
        Parser[] parser = new Parser[]{base};
        instance.set(parser);
        
        instance.crawl(site);
        Parser[] result = instance.getParser();
        assertNotNull(((NullParser)result[0]).content);
        assertFalse(((NullParser)result[0]).content.contentEquals(""));
    }

    /**
     * Test of doParse method, of class nz.ac.waikato.mcennis.arm.crawler.CrawlerBase.
     */
    public void testDoParse() throws Exception {
        System.out.println("doParse");
        String content = "This is a little bit of content for parsing to see if it works";
        byte[] raw_data = content.getBytes();
        NullParser base = new NullParser();
        Parser[] parser = new Parser[]{base};
        CrawlerBase instance = new CrawlerBase();
        instance.set(parser);
        instance.doParse(raw_data,new String[]{base.getName()});
    }
    
}
