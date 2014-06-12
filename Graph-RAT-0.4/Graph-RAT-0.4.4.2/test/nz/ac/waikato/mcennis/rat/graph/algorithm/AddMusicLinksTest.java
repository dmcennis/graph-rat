/*
 * AddMusicLinksTest.java
 * JUnit based test
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;

import junit.framework.*;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.GraphFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class AddMusicLinksTest extends TestCase {
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
    Graph test;
    
    public AddMusicLinksTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","A");
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","interest");
        props.setProperty("LinkType","Interest");
        props.setProperty("LinkClass","Basic");
        props.setProperty("Graph","Memory");
        test = GraphFactory.newInstance().create(props);
        a = ActorFactory.newInstance().create(props);
        Property ia = PropertyFactory.newInstance().create(props);
        ia.add("1");
        ia.add("2");
        a.add(ia);
        test.add(a);
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","B");
        b = ActorFactory.newInstance().create(props);
        Property ib = PropertyFactory.newInstance().create(props);
        ib.add("1");
        ib.add("2");
        ib.add("3");
        ib.add("4");
        b.add(ib);
        test.add(b);
        props.setProperty("ActorID","C");
        c = ActorFactory.newInstance().create(props);
        Property ic = PropertyFactory.newInstance().create(props);
        ic.add("3");
        ic.add("4");
        ic.add("5");
        ic.add("6");
        c.add(ic);
        test.add(c);
        props.setProperty("ActorID","D");
        d = ActorFactory.newInstance().create(props);
        Property id = PropertyFactory.newInstance().create(props);
        id.add("7");
        id.add("8");
        d.add(id);
        test.add(d);
        props.setProperty("ActorID","E");
        e = ActorFactory.newInstance().create(props);
        test.add(e);
        props.setProperty("LinkType","Knows");
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
        props.setProperty("ActorType","Artist");
        props.setProperty("ActorID","1");
        art1 = ActorFactory.newInstance().create(props);
        test.add(art1);
        props.setProperty("ActorID","3");
        art3 = ActorFactory.newInstance().create(props);
        test.add(art3);
        props.setProperty("ActorID","5");
        art5 = ActorFactory.newInstance().create(props);
        test.add(art5);
        props.setProperty("ActorID","7");
        art7 = ActorFactory.newInstance().create(props);
        test.add(art7);
        props.setProperty("LinkType","Given");
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
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicLinks.
     */
    public void testExecuteA() {
        System.out.println("execute A");
       
        AddMusicLinks instance = new AddMusicLinks();
        
        instance.execute(test);
        Link[] A = test.getLinkBySource("Music",a);
        assertNotNull(A);
        assertEquals(2,A.length);
        for(int i=0;i<A.length;++i){
            if(A[i].getDestination().getID().contentEquals("B")){
                assertEquals(2.0,A[i].getStrength(),0.001);                
            }else if(A[i].getDestination().getID().contentEquals("C")){
                assertEquals(-0.25,A[i].getStrength(),0.001);                
            }
        }
    }

    /**
     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicLinks.
     */
    public void testExecuteB() {
        System.out.println("execute B");
       
        AddMusicLinks instance = new AddMusicLinks();
        
        instance.execute(test);
        Link[] B = test.getLinkBySource("Music",b);
        assertNotNull(B);
        assertEquals(3,B.length);
        for(int i=0;i<B.length;++i){
            if(B[i].getDestination().getID().contentEquals("A")){
                assertEquals(1.75,B[i].getStrength(),0.001);
            }else if(B[i].getDestination().getID().contentEquals("C")){
                assertEquals(1.75,B[i].getStrength(),0.001);
            }else if(B[i].getDestination().getID().contentEquals("D")){
                assertEquals(-0.525,B[i].getStrength(),0.001);
            }
        }
    }

    /**
     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicLinks.
     */
    public void testExecuteC() {
        System.out.println("execute C");
       
        AddMusicLinks instance = new AddMusicLinks();
        
        instance.execute(test);
        Link[] C = test.getLinkBySource("Music",c);
        assertNotNull(C);
        assertEquals(2,C.length);
        for(int i=0;i<C.length;++i){
            if(C[i].getDestination().getID().contentEquals("A")){
                assertEquals(-0.525,C[i].getStrength(),0.001);                
            }else if(C[i].getDestination().getID().contentEquals("B")){
                assertEquals(1.75,C[i].getStrength(),0.001);             
            }
        }
    }

    /**
     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicLinks.
     */
    public void testExecuteD() {
        System.out.println("execute D");
       
        AddMusicLinks instance = new AddMusicLinks();
        
        instance.execute(test);
        Link[] D = test.getLinkBySource("Music",d);
        assertNotNull(D);
        assertEquals(1,D.length);
        assertEquals(-0.25,D[0].getStrength(),0.001);
    }

    /**
     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicLinks.
     */
    public void testExecuteE() {
        System.out.println("execute E");
       
        AddMusicLinks instance = new AddMusicLinks();
        
        instance.execute(test);
        Link[] E = test.getLinkBySource("Music",e);
        assertNull(E);
    }


    /**
     * Test of compareInterests method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicLinks.
     */
    public void testCompareInterests() {
        System.out.println("compareInterests");
        
        Link[] left = test.getLinkBySource("Given",a);
        Link[] right = test.getLinkBySource("Given",b);
        AddMusicLinks instance = new AddMusicLinks();
        
        instance.compareInterests(left, right, 1.0);
    }

    
        public void testGetName() {
        fail("Test not yet written");
    }

    public void testSetName() {
        fail("Test not yet written");
    }

    public void testGetInputType() {
        fail("Test not yet written");
    }

    public void testGetOutputType() {
        fail("Test not yet written");
    }

    public void testGetParameter() {
        fail("Test not yet written");
    }

    public void testGetParameterString() {
        fail("Test not yet written");
    }

    public void testGetParameterObject() {
        fail("Test not yet written");
    }

    public void testSetParameter() {
        fail("Test not yet written");
    }
}
