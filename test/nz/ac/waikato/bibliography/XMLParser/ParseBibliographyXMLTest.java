/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */



package nz.ac.waikato.bibliography.XMLParser;



import java.io.FileInputStream;

import java.util.List;
import org.dynamicfactory.descriptors.Properties;

import junit.framework.TestCase;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.MemGraph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;



import org.dynamicfactory.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.parser.XMLParser;





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

        Properties props = PropertiesFactory.newInstance().create();

        ParseBibliographyXML handler = new ParseBibliographyXML();

        handler.set(graph);

        parser.setHandler(handler);

        parser.parse(new FileInputStream("c:\\Users\\mcennis\\Documents\\3_clusters.xml"),"c:\\Users\\mcennis\\Documents\\3_clusters.xml");

    }



    @Override

    protected void tearDown() throws Exception {

        super.tearDown();

    }



    public void testLoadGraph() throws Exception {

    }

    

    public void testActorLength() throws Exception {

        List<Actor> author = graph.getActor("Author");

        assertNotNull(author);

        assertEquals(758,author.size());

        List<Actor> paper = graph.getActor("Paper");

        assertNotNull(paper);

        assertEquals(605,paper.size());

    }



    public void testSubGraphs() throws Exception {

        List<Graph> children = graph.getChildren();

        assertFalse(children.isEmpty());

        assertEquals(5,children.size());

        for(int i=0;i<children.size();++i){

            if(children.get(i).getID().contentEquals("0")){

                List<Actor> a = children.get(i).getActor();

                assertNotNull(a);

                assertEquals(302,a.size());

            }else if(children.get(i).getID().contentEquals("1")){

                List<Actor> a = children.get(i).getActor();

                assertNotNull(a);

                assertEquals(191,a.size());

            }else if(children.get(i).getID().contentEquals("2")){

                List<Actor> a = children.get(i).getActor();

                assertNotNull(a);

                assertEquals(107,a.size());

            }else if(children.get(i).getID().contentEquals("3")){

                List<Actor> a = children.get(i).getActor();

                assertNotNull(a);

                assertEquals(1,a.size());

            }else if(children.get(i).getID().contentEquals("4")){

                List<Actor> a = children.get(i).getActor();

                assertNotNull(a);

                assertEquals(1,a.size());

            }else{

                fail("Unknown GraphID '"+children.get(i).getID()+"' detected");

            }

        }

    }

}

