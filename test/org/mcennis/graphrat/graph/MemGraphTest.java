/*


 * MemGraphTest.java


 * JUnit based test


 *


 * Created on 13 June 2007, 11:01


 *


 * Copyright Daniel McEnnis, all rights reserved


 */


package org.mcennis.graphrat.graph;





import java.io.ByteArrayOutputStream;


import java.io.OutputStreamWriter;


import java.util.*;

import junit.framework.*;


import org.mcennis.graphrat.link.Link;


import org.mcennis.graphrat.link.LinkFactory;


import org.mcennis.graphrat.actor.Actor;


import org.mcennis.graphrat.actor.ActorFactory;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import org.mcennis.graphrat.graph.MemGraph;


/**


 *


 * @author work


 */


public class MemGraphTest extends TestCase {





    Actor a;


    Actor b;


    Actor c;


    Actor d;


    Actor e;


    Actor art1;


    Actor art3;


    Actor art5;


    Actor art7;


    Link ab;


    Link ba;


    Link bc;


    Link cb;


    Link ac;


    Link ca;


    Link ae;


    Link bd;


    Link dc;


    Link a1;


    Link b1;


    Link b3;


    Link c3;


    Link c5;


    Link d7;


    MemGraph test;


    MemGraph child1;


    MemGraph child2;


    MemGraph child11;





    public MemGraphTest(String testName) {


        super(testName);


    }





    protected void setUp() throws Exception {


        test = new MemGraph();


        test.setID("test");


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("PropertyID", "graphProperty");


        props.set("PropertyValueClass", Double.class);

        Property graphProp = PropertyFactory.newInstance().create(props);


        graphProp.add(new Double(1.0));


        graphProp.add(new Double(2.0));


        test.add(graphProp);


        props.set("Relation", "Interest");

        props.set("ActorMode", "User");


        props.set("ActorID", "A");


        a = ActorFactory.newInstance().create(props);


        props.set("PropertyValueClass", String.class);


        props.set("PropertyID", "interest");


        Property ia = PropertyFactory.newInstance().create(props);


        ia.add("1");


        ia.add("2");


        a.add(ia);


        test.add(a);


        props.set("ActorMode", "User");


        props.set("ActorID", "B");


        b = ActorFactory.newInstance().create(props);


        Property ib = PropertyFactory.newInstance().create(props);


        ib.add("1");


        ib.add("2");


        ib.add("3");


        ib.add("4");


        b.add(ib);


        test.add(b);


        props.set("ActorID", "C");


        c = ActorFactory.newInstance().create(props);


        Property ic = PropertyFactory.newInstance().create(props);


        ic.add("3");


        ic.add("4");


        ic.add("5");


        ic.add("6");


        c.add(ic);


        test.add(c);


        props.set("ActorID", "D");


        d = ActorFactory.newInstance().create(props);


        Property id = PropertyFactory.newInstance().create(props);


        id.add("7");


        id.add("8");


        d.add(id);


        test.add(d);


        props.set("ActorID", "E");


        e = ActorFactory.newInstance().create(props);


        test.add(e);


        props.set("Relation", "Knows");


        ab = LinkFactory.newInstance().create(props);


        ab.set(a, 1.0, b);


        test.add(ab);


        ba = LinkFactory.newInstance().create(props);


        ba.set(b, 1.0, a);


        test.add(ba);


        cb = LinkFactory.newInstance().create(props);


        cb.set(c, 1.0, b);


        test.add(cb);


        bc = LinkFactory.newInstance().create(props);


        bc.set(b, 1.0, c);


        test.add(bc);


        ca = LinkFactory.newInstance().create(props);


        ca.set(c, 1.0, a);


        test.add(ca);


        ac = LinkFactory.newInstance().create(props);


        ac.set(a, 1.0, c);


        test.add(ac);


        ae = LinkFactory.newInstance().create(props);


        ae.set(a, 1.0, e);


        test.add(ae);


        bd = LinkFactory.newInstance().create(props);


        bd.set(b, 1.0, d);


        test.add(bd);


        dc = LinkFactory.newInstance().create(props);


        dc.set(d, 1.0, c);


        test.add(dc);


        props.set("ActorMode", "Artist");


        props.set("ActorID", "1");


        art1 = ActorFactory.newInstance().create(props);


        test.add(art1);


        props.set("ActorID", "3");


        art3 = ActorFactory.newInstance().create(props);


        test.add(art3);


        props.set("ActorID", "5");


        art5 = ActorFactory.newInstance().create(props);


        test.add(art5);


        props.set("ActorID", "7");


        art7 = ActorFactory.newInstance().create(props);


        test.add(art7);


        props.set("Relation", "Given");


        a1 = LinkFactory.newInstance().create(props);


        a1.set(a, 1.79769E+308, art1);


        test.add(a1);


        b1 = LinkFactory.newInstance().create(props);


        b1.set(b, 1.79769E+308, art1);


        test.add(b1);


        b3 = LinkFactory.newInstance().create(props);


        b3.set(b, 1.79769E+308, art3);


        test.add(b3);


        c3 = LinkFactory.newInstance().create(props);


        c3.set(c, 1.79769E+308, art3);


        props.set("PropertyID","DummyProperty");


        props.set("PropertyValueClass",Double.class);


        Property linkProp = PropertyFactory.newInstance().create(props);


        linkProp.add(new Double(1.0));


        linkProp.add(new Double(3.0));


        c3.add(linkProp);


        test.add(c3);


        c5 = LinkFactory.newInstance().create(props);


        c5.set(c, 1.79769E+308, art5);


        test.add(c5);


        d7 = LinkFactory.newInstance().create(props);


        d7.set(d, 1.79769E+308, art7);


        test.add(d7);





    }





    protected void addChildren() throws Exception{


        // adds two children, one of which has an inner child


        child1 = new MemGraph();


        child1.setID("child1");


        child1.add(a);


        child1.add(b);


        child1.add(art1);


        child1.add(art3);


        child1.add(a1);


        child1.add(b1);


        child1.add(b3);


        child1.add(ab);


        test.addChild(child1);





        child11 = new MemGraph();


        child11.setID("child11");


        child11.add(a);


        child11.add(b);


        child11.add(ab);


        child1.addChild(child11);





        child2 = new MemGraph();


        child2.setID("child2");


        child2.add(d);


        child2.add(art7);


        child2.add(d7);


        Properties props = PropertiesFactory.newInstance().create();


        props.set("PropertyID", "graphProperty");


        props.set("PropertyValueClass", Double.class);


        props.set("PropertyType", "Basic");


        Property graphProp = PropertyFactory.newInstance().create(props);


        graphProp.add(new Double(1.0));


        graphProp.add(new Double(2.0));


        child2.add(graphProp);


        test.addChild(child2);


    }





    protected void tearDown() throws Exception {


    }





    /**


     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testAdd() {


        System.out.println("add");





        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("Relation", "Knows");


        props.set("LinkClass", "Basic");


        Link link = LinkFactory.newInstance().create(props);


        assertEquals(1, test.links.get("Knows").get("User").get("D").size());


        assertEquals(2, test.invertedLinks.get("Knows").get("User").get("A").size());





        link.set(d, 1.0, a);





        test.add(link);





        assertEquals(2, test.links.get("Knows").get("User").get("D").size());


        assertEquals(c, test.links.get("Knows").get("User").get("D").get(c).getDestination());


        assertEquals(3, test.invertedLinks.get("Knows").get("User").get("A").size());


    }





    /**


     * Test of getUser method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testGetUser() {


        System.out.println("getUser");





        String ID = "A";








        Actor expResult = a;


        Actor result = test.getActor("User", "A");


        assertEquals(expResult, result);





    }





    /**


     * Test of getArtist method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testGetArtist() {


        System.out.println("getArtist");





        String ID = "5";





        Actor expResult = art5;


        Actor result = test.getActor("Artist", ID);


        assertEquals(expResult, result);





    }





    /**


     * Test of outputXML method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testOutputXML() throws Exception {


        System.out.println("outputXML");


        ByteArrayOutputStream content = new ByteArrayOutputStream();


        OutputStreamWriter output = new OutputStreamWriter(content);





        test.outputXML(output, false);


        output.flush();


        output.close();





        String contentString = content.toString();


        assertNotNull(contentString);


        assertTrue(contentString.length() > 0);





    }





    /**


     * Test of outputXML method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testComplicatedOutputXML() throws Exception {


        System.out.println("outputXML");


        ByteArrayOutputStream content = new ByteArrayOutputStream();


        OutputStreamWriter output = new OutputStreamWriter(content);





        addChildren();


        test.outputXML(output, false);


        output.flush();


        output.close();





        String contentString = content.toString();


        assertNotNull(contentString);


        assertTrue(contentString.length() > 0);


        System.out.println(contentString);


    }





    /**


     * Test of getUserLink method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testGetUserLink() {


        System.out.println("getUserLink");





        String type = "Knows";


        String sourceID = "A";





        SortedSet<Link> expResult = new TreeSet<Link>();
        expResult.add(ab);
        expResult.add(ac);
        expResult.add(ae);


        SortedSet<Link> result = test.getLinkBySource(type, a);


        assertEquals(expResult.size(), result.size());

        Iterator<Link> resultIT = result.iterator();
        for(Link i : expResult) {
            assertEquals(i.getDestination().getID(), resultIT.next().getDestination().getID());
        }
    }





    public void testGetUserLinks() {


        SortedSet<Link> result = test.getLink("Knows");


        assertEquals(9, result.size());


    }





    public void testGetUserLinksByDestination() {


        SortedSet<Link> result = test.getLinkByDestination("Knows", c);


        assertEquals(3, result.size());


        for (Link i : result) {


            if (i.getSource().equals(a)) {


                assertEquals(c, i.getDestination());


            } else if (i.getSource().equals(b)) {


                assertEquals(c, i.getDestination());


            } else if (i.getSource().equals(d)) {


                assertEquals(c, i.getDestination());


            } else {


                fail("Source " + i.getSource().getID() + " should not be present");


            }


        }


    }





    public void testGetUserLinksBySourceAndDestination() {


        SortedSet<Link> result = test.getLink("Knows", a, e);


        assertEquals(1, result.size());


        assertEquals(a, result.first().getSource());


        assertEquals(e, result.first().getDestination());


    }





    /**


     * Test of getSimilarArtist method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


//    public void testGetSimilarArtist() {


//        System.out.println("getSimilarArtist");


//        


//        String type = "";


//        String sourceID = "";


//        MemGraph instance = new MemGraph();


//        


//        List<Link> expResult = null;


//        List<Link> result = instance.getLink(type, sourceID);


//        assertEquals(expResult, result);


//        


//        fail("Test case not written yet!");


//    }


//


    /**


     * Test of getArtistLink method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testGetArtistLink() {


        System.out.println("getArtistLink");





        String type = "Given";


        String sourceID = "A";




        SortedSet<Link> expResult = new TreeSet<Link>();
        expResult.add(a1);


        SortedSet<Link> result = test.getLinkBySource(type, a);


        assertEquals(expResult.size(), result.size());


        assertEquals(expResult.first().getDestination().getID(), result.first().getDestination().getID());





    }





    /**


     * Test of remove method, of class nz.ac.waikato.mcennis.arm.graph.MemGraph.


     */


    public void testBasicRemove() {


        System.out.println("basic remove");








        test.remove(e);





        assertNull(test.getActor("User", "E"));


        assertEquals(0, test.getLinkBySource("Knows", e).size());


        assertEquals(0, test.getLinkByDestination("Knows", e).size());


    }





    public void testComplicatedRemovePart1() {


        System.out.println("remove 1");





        test.remove(d);





        assertEquals(0,test.getActor("D").size());


        assertEquals(0, test.getLinkBySource("Knows", d).size());


        assertEquals(2, test.getLinkBySource("Knows", b).size());


        assertEquals(0, test.getLinkByDestination("Knows", d).size());


        assertEquals(2, test.getLinkByDestination("Knows", c).size());


    }


    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.MemGraph.
     */
    public void testGetPropertyNotNull() throws Exception{
        System.out.println("getProperty");

        MemGraph instance = new MemGraph();
        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode1");
        Property prop1 = PropertyFactory.newInstance().create(props);
        prop1.add("Value");
        instance.add(prop1);
        props.set("PropertyID","Mode2");
        Property prop2 = PropertyFactory.newInstance().create(props);
        prop2.add("value");
        instance.add(prop2);
        List<Property> result = instance.getProperty();
        assertNotNull(result);
        assertEquals(2, result.size());
        for(int i=0;i<result.size();++i){
            if(!result.get(i).equals(prop1)&&!result.get(i).equals(prop2)){
                fail("Unexpected property encountered");
            }
        }

    }

    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.MemGraph.
     */
    public void testGetPropertyNull() {
        System.out.println("getProperty");

        MemGraph instance = new MemGraph();

        List<Property> result = instance.getProperty();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.MemGraph.
     */
    public void testRemoveProperty() {
        System.out.println("removeProperty");

        String ID = "Prop";
        MemGraph instance = new MemGraph();

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Prop");
        Property prop = PropertyFactory.newInstance().create(props);
        instance.add(prop);
        instance.removeProperty(ID);

        assertNull(instance.getProperty("Prop"));
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.MemGraph.
     */
    public void testRemovePropertyNotPresent() {
        System.out.println("removePropertyNotPresent");

        String ID = "Prop";
        MemGraph instance = new MemGraph();

        instance.removeProperty(ID);
    }


    // FIXME: tests for adding andPathSets 


}


