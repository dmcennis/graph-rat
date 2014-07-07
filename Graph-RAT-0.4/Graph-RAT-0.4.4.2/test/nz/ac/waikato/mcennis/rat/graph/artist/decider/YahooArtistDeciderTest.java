/*
 * YahooArtistDeciderTest.java
 * JUnit based test
 *
 * Created on 11 December 2007, 13:55
 * 
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.artist.decider;

import junit.framework.TestCase;
import org.mcennis.graphrat.crawler.Authenticator;

/**
 *
 * @author Daniel McEnnis
 */
public class YahooArtistDeciderTest extends TestCase {
    
    public YahooArtistDeciderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testNewInstance() {
        System.out.println("newInstance");
        YahooArtistDecider result = YahooArtistDecider.newInstance();
        assertNotNull(result);
    }

    public void testSetProxy() {
        System.out.println("setProxy");
        boolean p = false;
        YahooArtistDecider instance = YahooArtistDecider.newInstance();
        instance.setProxy(p);
    }
    
    public void testGetArtist() throws Exception{
        System.out.append("lookup");
        YahooArtistDecider instance = YahooArtistDecider.newInstance();
        instance.setProxy(true);
        Authenticator.setUser("dm75");
        java.io.LineNumberReader password = new java.io.LineNumberReader(new java.io.FileReader("/research/password"));
        Authenticator.setPassword(password.readLine());
        password.close();
        instance.lookup("Madonna");
        assertTrue(instance.artist.size()>0);
        assertTrue(instance.checked.size()>0);
    }
    
}
