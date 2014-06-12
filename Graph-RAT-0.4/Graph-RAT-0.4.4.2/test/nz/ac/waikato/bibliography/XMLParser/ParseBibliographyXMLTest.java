/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.bibliography.XMLParser;

import java.io.FileInputStream;
import java.util.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.parser.ParsedObject;
import nz.ac.waikato.mcennis.rat.parser.Parser;
import nz.ac.waikato.mcennis.rat.parser.XMLParser;
import nz.ac.waikato.mcennis.rat.parser.xmlHandler.Handler;
import org.xml.sax.Attributes;

/**
 *
 * @author mcennis
 */
public class ParseBibliographyXMLTest extends TestCase {
    
    MemGraph graph = null;
    public ParseBibliographyXMLTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        graph = new MemGraph();
        XMLParser parser = new XMLParser();
        Properties props = new Properties();
        ParseBibliographyXML handler = new ParseBibliographyXML();
        handler.set(graph);
        parser.setHandler(handler);
        parser.parse(new FileInputStream("c:\\Users\\mcennis\\Documents\\3_clusters.xml"));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLoadGraph() throws Exception {
    }
    
    public void testActorLength() throws Exception {
        Actor[] author = graph.getActor("Author");
        assertNotNull(author);
        assertEquals(758,author.length);
        Actor[] paper = graph.getActor("Paper");
        assertNotNull(paper);
        assertEquals(605,paper.length);
    }

    public void testSubGraphs() throws Exception {
        Graph[] children = graph.getChildren();
        assertNotNull(children);
        assertEquals(5,children.length);
        for(int i=0;i<children.length;++i){
            if(children[i].getID().contentEquals("0")){
                Actor[] a = children[i].getActor();
                assertNotNull(a);
                assertEquals(302,a.length);
            }else if(children[i].getID().contentEquals("1")){
                Actor[] a = children[i].getActor();
                assertNotNull(a);
                assertEquals(191,a.length);
            }else if(children[i].getID().contentEquals("2")){
                Actor[] a = children[i].getActor();
                assertNotNull(a);
                assertEquals(107,a.length);
            }else if(children[i].getID().contentEquals("3")){
                Actor[] a = children[i].getActor();
                assertNotNull(a);
                assertEquals(1,a.length);
            }else if(children[i].getID().contentEquals("4")){
                Actor[] a = children[i].getActor();
                assertNotNull(a);
                assertEquals(1,a.length);
            }else{
                fail("Unknown GraphID '"+children[i].getID()+"' detected");
            }
        }
    }
}
