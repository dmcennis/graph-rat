/*
 * AddMusicRecommendationsTest.java
 * JUnit based test
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;

import junit.framework.*;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.GraphFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import java.util.Vector;

/**
 *
 * @author Daniel McEnnis
 */
public class AddMusicRecommendationsTest extends TestCase {
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
    Link abI;
    Link baI;
    Link acI;
    Link caI;
    Link bcI;
    Link cbI;
    Link bdI;
    Link dcI;
    Link aeI;
    Link abM;
    Link baM;
    Link acM;
    Link caM;
    Link bcM;
    Link cbM;
    Link bdM;
    Link dcM;
    
    
    Graph test;
    
    public AddMusicRecommendationsTest(String testName) {
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
        props.setProperty("LinkType","Interest");
        abI = LinkFactory.newInstance().create(props);
        abI.set(a,2.50,b);
        test.add(abI);
        baI = LinkFactory.newInstance().create(props);
        baI.set(b,1.35,a);
        test.add(baI);
        acI = LinkFactory.newInstance().create(props);
        acI.set(a,-1.15,c);
        test.add(acI);
        caI = LinkFactory.newInstance().create(props);
        caI.set(c,-3.0935,a);
        test.add(caI);
        bcI = LinkFactory.newInstance().create(props);
        bcI.set(b,1.35,c);
        test.add(bcI);
        cbI = LinkFactory.newInstance().create(props);
        cbI.set(c,1.35,b);
        test.add(cbI);
        bdI = LinkFactory.newInstance().create(props);
        bdI.set(b,-3.0935,d);
        test.add(bdI);
        dcI = LinkFactory.newInstance().create(props);
        dcI.set(d,-1.15,c);
        test.add(dcI);
        props.setProperty("LinkType","Music");
        abM = LinkFactory.newInstance().create(props);
        abM.set(a,2.0,b);
        test.add(abM);
        baM = LinkFactory.newInstance().create(props);
        baM.set(b,1.75,a);
        test.add(baM);
        acM = LinkFactory.newInstance().create(props);
        acM.set(a,-0.25,c);
        test.add(acM);
        caM = LinkFactory.newInstance().create(props);
        caM.set(c,-0.525,a);
        test.add(caM);
        bcM = LinkFactory.newInstance().create(props);
        bcM.set(b,1.75,c);
        test.add(bcM);
        cbM = LinkFactory.newInstance().create(props);
        cbM.set(c,1.75,b);
        test.add(cbM);
        bdM = LinkFactory.newInstance().create(props);
        bdM.set(b,-0.525,d);
        test.add(bdM);
        dcM = LinkFactory.newInstance().create(props);
        dcM.set(d,-0.25,c);
        test.add(dcM);        
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicRecommendations.
     */
    public void testExecuteA() {
        System.out.println("execute A");
        
        AddMusicRecommendations instance = new AddMusicRecommendations();
        
        instance.execute(test);
        
        Link[] users = test.getLinkBySource("Derived",a);
        assertNotNull(users);
        assertEquals(2,users.length);
        for(int i=0;i<users.length;++i){
            if(users[i].getDestination().getID().contentEquals("3")){
                assertEquals(3.3,users[i].getStrength(),0.001);
            }else if(users[i].getDestination().getID().contentEquals("5")){
                assertEquals(-1.3,users[i].getStrength(),0.001);
            }
        }
    }

    public void testExecuteB() {
        System.out.println("execute B");
        
        AddMusicRecommendations instance = new AddMusicRecommendations();
        
        instance.execute(test);
        
        Link[] users = test.getLinkBySource("Derived",b);
        assertNotNull(users);
        assertEquals(2,users.length);
        for(int i=0;i<users.length;++i){
            if(users[i].getDestination().getID().contentEquals("5")){
                assertEquals(3.2,users[i].getStrength(),0.001);
            }else if(users[i].getDestination().getID().contentEquals("7")){
                assertEquals(-3.5185,users[i].getStrength(),0.001);
            }
        }

    }

    public void testExecuteC() {
        System.out.println("execute C");
        
        AddMusicRecommendations instance = new AddMusicRecommendations();
        
        instance.execute(test);
        
        Link[] users = test.getLinkBySource("Derived",c);
        assertNotNull(users);
        assertEquals(1,users.length);
        assertEquals("1",users[0].getDestination().getID());
        assertEquals(-0.3185,users[0].getStrength(),0.001);
    }

    public void testExecuteD() {
        System.out.println("execute D");
        
        AddMusicRecommendations instance = new AddMusicRecommendations();
        
        instance.execute(test);
        
        Link[] users = test.getLinkBySource("Derived",d);
        assertNotNull(users);
        assertEquals(2,users.length);
        assertEquals(-1.3,users[0].getStrength(),0.001);
        assertEquals(-1.3,users[1].getStrength(),0.001);
    }

    public void testExecuteE() {
        System.out.println("execute E");
        
        AddMusicLinks musicLinks = new AddMusicLinks();
        AddMusicRecommendations instance = new AddMusicRecommendations();
        
        musicLinks.execute(test);
        instance.execute(test);
        
        Link[] users = test.getLinkBySource("Derived",e);
        assertNull(users);
    }

    /**
     * Test of calculateArtistWeight method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicRecommendations.
     */
    public void testCalculateArtistWeight() {
        System.out.println("calculateArtistWeight");

        AddMusicLinks musicLinks = new AddMusicLinks();
        musicLinks.execute(test);
       
        Link knows = test.getLinkBySource("Knows",d)[0];
        Link[] interest = test.getLinkBySource("Interest",d);
        Link[] music = test.getLinkBySource("Music",d);
        AddMusicRecommendations instance = new AddMusicRecommendations();
        
//        double expResult = 0.0;
        double result = instance.calculateArtistWeight(knows, interest, music);
//        assertEquals(expResult, result);
        
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
