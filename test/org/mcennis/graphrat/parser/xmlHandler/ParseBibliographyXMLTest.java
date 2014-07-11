/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */



package org.mcennis.graphrat.parser.xmlHandler;



import java.io.FileInputStream;

import java.util.List;
import java.util.SortedSet;

import junit.framework.TestCase;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.MemGraph;

import org.mcennis.graphrat.actor.Actor;


import org.mcennis.graphrat.parser.XMLParser;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.mcennis.graphrat.parser.xmlHandler.ParseBibliographyXML;


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

        SortedSet<Actor> author = graph.getActor("Author");

        assertNotNull(author);

        assertEquals(758,author.size());

        SortedSet<Actor> paper = graph.getActor("Paper");

        assertNotNull(paper);

        assertEquals(605,paper.size());

    }



    public void testSubGraphs() throws Exception {

        SortedSet<Graph> children = graph.getChildren();

        assertFalse(children.isEmpty());

        assertEquals(5,children.size());

        for(Graph i : children){

            if(i.getID().contentEquals("0")){

                SortedSet<Actor> a = i.getActor();

                assertNotNull(a);

                assertEquals(302,a.size());

            }else if(i.getID().contentEquals("1")){

                SortedSet<Actor> a = i.getActor();

                assertNotNull(a);

                assertEquals(191,a.size());

            }else if(i.getID().contentEquals("2")){

                SortedSet<Actor> a = i.getActor();

                assertNotNull(a);

                assertEquals(107,a.size());

            }else if(i.getID().contentEquals("3")){

                SortedSet<Actor> a = i.getActor();

                assertNotNull(a);

                assertEquals(1,a.size());

            }else if(i.getID().contentEquals("4")){

                SortedSet<Actor> a = i.getActor();

                assertNotNull(a);

                assertEquals(1,a.size());

            }else{

                fail("Unknown GraphID '"+i.getID()+"' detected");

            }

        }

    }

}

