/*

 * CliqueTest.java

 * JUnit based test

 *

 * Created on 22 July 2007, 17:40

 *

 * Copyright Daniel McEnnis, all rights reserved

 */



package nz.ac.waikato.mcennis.rat.graph;



import java.util.List;
import junit.framework.*;




import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;

import nz.ac.waikato.mcennis.rat.graph.path.PathSet;

import nz.ac.waikato.mcennis.rat.graph.query.Query;



/**

 *

 * @author Daniel McEnnis

 */

public class CliqueTest extends TestCase {

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

    Clique clique;

    

    public CliqueTest(String testName) {

        super(testName);

    }



    protected void setUp() throws Exception {

        test = new MemGraph();

         org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("ActorType","User");

        props.set("ActorID","A");

        props.set("PropertyType","Basic");

        props.set("PropertyID","interest");

        props.set("LinkType","Interest");

        props.set("LinkClass","Basic");

        a = ActorFactory.newInstance().create(props);

        Property ia = PropertyFactory.newInstance().create(props);

        ia.add("1");

        ia.add("2");

        a.add(ia);

        test.add(a);

        props.set("ActorType","User");

        props.set("ActorID","B");

        b = ActorFactory.newInstance().create(props);

        Property ib = PropertyFactory.newInstance().create(props);

        ib.add("1");

        ib.add("2");

        ib.add("3");

        ib.add("4");

        b.add(ib);

        test.add(b);

        props.set("ActorID","C");

        c = ActorFactory.newInstance().create(props);

        Property ic = PropertyFactory.newInstance().create(props);

        ic.add("3");

        ic.add("4");

        ic.add("5");

        ic.add("6");

        c.add(ic);

        test.add(c);

        props.set("ActorID","D");

        d = ActorFactory.newInstance().create(props);

        Property id = PropertyFactory.newInstance().create(props);

        id.add("7");

        id.add("8");

        d.add(id);

        test.add(d);

        props.set("ActorID","E");

        e = ActorFactory.newInstance().create(props);

        test.add(e);

        props.set("LinkType","Knows");

        ab = LinkFactory.newInstance().create(props);

        ab.set(a,1.0,b);

        test.add(ab);

        ba = LinkFactory.newInstance().create(props);

        ba.set(b,1.0,a);

        test.add(ba);

        cb = LinkFactory.newInstance().create(props);

        cb.set(c,1.0,b);

        test.add(cb);

        bc = LinkFactory.newInstance().create(props);

        bc.set(b,1.0,c);

        test.add(bc);

        ca = LinkFactory.newInstance().create(props);

        ca.set(c,1.0,a);

        test.add(ca);

        ac = LinkFactory.newInstance().create(props);

        ac.set(a,1.0,c);

        test.add(ac);

        ae = LinkFactory.newInstance().create(props);

        ae.set(a,1.0,e);

        test.add(ae);

        bd = LinkFactory.newInstance().create(props);

        bd.set(b,1.0,d);

        test.add(bd);

        dc = LinkFactory.newInstance().create(props);

        dc.set(d,1.0,c);

        test.add(dc);

        props.set("ActorType","Artist");

        props.set("ActorID","1");

        art1 = ActorFactory.newInstance().create(props);

        test.add(art1);

        props.set("ActorID","3");

        art3 = ActorFactory.newInstance().create(props);

        test.add(art3);

        props.set("ActorID","5");

        art5 = ActorFactory.newInstance().create(props);

        test.add(art5);

        props.set("ActorID","7");

        art7 = ActorFactory.newInstance().create(props);

        test.add(art7);

        props.set("LinkType","Given");

        a1 = LinkFactory.newInstance().create(props);

        a1.set(a,1.79769E+308,art1);

        test.add(a1);

        b1 = LinkFactory.newInstance().create(props);

        b1.set(b,1.79769E+308,art1);

        test.add(b1);

        b3 = LinkFactory.newInstance().create(props);

        b3.set(b,1.79769E+308,art3);

        test.add(b3);

        c3 = LinkFactory.newInstance().create(props);

        c3.set(c,1.79769E+308,art3);

        test.add(c3);

        c5 = LinkFactory.newInstance().create(props);

        c5.set(c,1.79769E+308,art5);

        test.add(c5);

        d7 = LinkFactory.newInstance().create(props);

        d7.set(d,1.79769E+308,art7);

        test.add(d7);

        test.setID("Home");

        clique = new Clique();

        clique.setID("cliqueTest");

        clique.setRelation("Knows");

        clique.setActorType("User");

    }



    protected void tearDown() throws Exception {

    }



    public void testSetParent(){

        System.out.println("setParent");

        test.addChild(clique);

        assertEquals(1,test.children.size());

        assertEquals(clique,test.children.getFirst());

        assertEquals(test,clique.parent);

    }

    

    public void testGetParent(){

        System.out.println("getParent");

        test.addChild(clique);

        assertEquals(test,clique.getParent());

    }

    

    public void testGetChild(){

        System.out.println("getChild");

        test.addChild(clique);

        assertEquals(clique,test.getChildren("cliqueTest"));

    }

    

    public void testGetChildArray(){

        System.out.println("getChildArray");

        test.addChild(clique);

        assertEquals(1,test.getChildren().size());

        assertEquals(clique,test.getChildren().get(0));

    }

    

    /**

     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testAddStart() {

        System.out.println("addStart");

        

        test.addChild(clique);

        clique.add(a);

        

        assertEquals(1,clique.map.size());

        assertEquals(3,clique.intersection.size());

    }



    /**

     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testAddNextInClique() {

        System.out.println("addNextInClique");

        

        test.addChild(clique);

        clique.add(a);

        clique.add(b);

        

        assertEquals(2,clique.map.size());

        assertEquals(1,clique.intersection.size());

        

    }



    /**

     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testAddNextNotInClique() {

        System.out.println("addNextNotInClique");

        

        test.addChild(clique);

        clique.add(a);

        clique.add(d);

        

        assertEquals(1,clique.map.size());

        assertEquals(3,clique.intersection.size());

        

    }





    /**

     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetActorPresent() {

        System.out.println("getActorPresent");

        

        String type = "User";

        String ID = "A";

        

        Actor expResult = a;

        test.addChild(clique);

        clique.add(a);

        Actor result = clique.getActor(type, ID);

        assertEquals(expResult, result);

        

    }



    /**

     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetActorNotPresent() {

        System.out.println("getActorNotPresent");

        

        String type = "User";

        String ID = "D";

        

        Actor expResult = null;

       test.addChild(clique);

        clique.add(a);

        Actor result = clique.getActor(type, ID);

        assertEquals(expResult, result);

        

    }



    /**

     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetActorArrayPresent() {

        System.out.println("getActorArrayPresent");

        

        String type = "User";

        

        Actor expResult = a;

        test.addChild(clique);

        clique.add(a);

        List<Actor> result = clique.getActor(type);

        assertEquals(1,result.size());

        assertEquals(expResult, result.get(0));

        

    }



    /**

     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetActorArrayNotPresent() {

        System.out.println("getActorArrayNotPresent");

        

        String type = "User";

        

        Actor expResult = a;

        test.addChild(clique);

        List<Actor> result = clique.getActor(type);

        assertNull(result);

    }



    /**

     * Test of getActorTypes method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetActorTypes() {

        System.out.println("getActorTypes");

        

        

        String expResult = "User";

        List<String> result = clique.getActorTypes();

        assertEquals(1, result.size());

        assertEquals(expResult,result.get(0));

        

    }



    /**

     * Test of getLink method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetLink() {

        System.out.println("getLink");

        

        

        List<Link> result = clique.getLink();

        assertNull(result);

        

    }



    /**

     * Test of getLinkBySource method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetLinkBySource() {

        System.out.println("getLinkBySource");

        

        String type = "Knows";

        Actor sourceActor = a;

        

        List<Link> result = clique.getLinkBySource(type, sourceActor);

        assertNull(result);

        

    }



    /**

     * Test of getLinkByDestination method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetLinkByDestination() {

        System.out.println("getLinkByDestination");

        

        String type = "Knows";

        Actor destActor = a;

        

        List<Link> result = clique.getLinkByDestination(type, destActor);

        assertNull(result);

        

    }



    /**

     * Test of getLinkTypes method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetLinkTypes() {

        System.out.println("getLinkTypes");

        

        Clique instance = new Clique();

        

        List<String> result = clique.getLinkTypes();

        assertFalse(result.isEmpty());

        

    }





    /**

     * Test of setSubGraph method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testSetSubGraph() {

        System.out.println("setSubGraph");

        

        Query q = null;

        Clique instance = new Clique();

        

        instance.setSubGraph(q);

        

        // TODO review the generated test code and remove the default call to fail.

        fail("The test case is a prototype.");

    }



    /**

     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetProperty() {

        System.out.println("getProperty");

        

        Clique instance = new Clique();

        

        List<Property> expResult = null;

        List<Property> result = instance.getProperty();

        assertEquals(expResult, result);

        

        // TODO review the generated test code and remove the default call to fail.

        fail("The test case is a prototype.");

    }



    /**

     * Test of getPathSet method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetPathSet() {

        System.out.println("getPathSet");

        

        Clique instance = new Clique();

        

        PathSet[] expResult = null;

        List<PathSet> result = instance.getPathSet();

        assertTrue(result.isEmpty());

        

        // TODO review the generated test code and remove the default call to fail.

        fail("The test case is a prototype.");

    }



    /**

     * Test of getID method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testGetID() {

        System.out.println("getID");

        

        

        String expResult = "cliqueTest";

        String result = clique.getID();

        assertEquals(expResult, result);

        

    }



    /**

     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.Clique.

     */

    public void testCompareTo() {

        System.out.println("compareTo");

        

        Clique o = null;

        Clique instance = new Clique();

        

        int expResult = 0;

        int result = instance.compareTo(o);

        assertEquals(expResult, result);

        

        // TODO review the generated test code and remove the default call to fail.

        fail("The test case is a prototype.");

    }

    

}

