/*


 * GZipFileCrawlerTest.java


 * JUnit based test


 *


 * Created on 12 September 2007, 14:37


 *


 * Copyright Daniel McEnnis, all rights reserved


 */





package org.mcennis.graphrat.crawler;





import junit.framework.*;


/**


 *


 * @author Daniel McEnnis


 */


public class GZipFileCrawlerTest extends TestCase {


    


    public GZipFileCrawlerTest(String testName) {


        super(testName);


    }





    protected void setUp() throws Exception {


    }





    protected void tearDown() throws Exception {


    }





    /**


     * Test of crawl method, of class nz.ac.waikato.mcennis.arm.crawler.GZipFileCrawler.


     */


    public void testCrawl() throws Exception {


        System.out.println("crawl");


        


        String site = "c:\\";


        GZipFileCrawler instance = new GZipFileCrawler();


        


        instance.crawl(site);


        


        


    }





    


}


