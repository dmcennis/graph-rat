/*
 * AddMusicReferencesTest.java
 * JUnit based test
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;

import junit.framework.*;
import nz.ac.waikato.mcennis.rat.graph.artist.decider.ArtistDecider;
import nz.ac.waikato.mcennis.rat.graph.artist.decider.ArtistDeciderFactory;
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
public class AddMusicReferencesTest extends TestCase {
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
    Graph test;
    ArtistDecider decider;
    
    public AddMusicReferencesTest(String testName) {
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
        props.setProperty("ArtistDeciderType","Basic");
        decider = ArtistDeciderFactory.newInstance().create(props);
        decider.addArtist("1");
        decider.addChecked("2");
        decider.addArtist("3");
        decider.addChecked("4");
        decider.addArtist("5");
        decider.addChecked("6");
        decider.addArtist("7");
        decider.addChecked("8");
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of setDecider method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicReferences.
     */
    public void testSetDecider() {
        System.out.println("setDecider");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ArtistDeciderType","Basic");
        ArtistDecider d = ArtistDeciderFactory.newInstance().create(props);
        AddMusicReferences instance = new AddMusicReferences();
        
        instance.setDecider(d);
        
        assertEquals(d,instance.getDecider());
    }



    /**
     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddMusicReferences.
     */
    public void testPartA() {
        System.out.println("execute");
        
        AddMusicReferences instance = new AddMusicReferences();
        instance.setDecider(decider);
        
        instance.execute(test);
        
        assertNotNull(test.getActor("Artist","1"));
        assertNull(test.getActor("Artist","2"));
        Link[] links = test.getLinkBySource("Given",a);
        assertNotNull(links);
        assertEquals(1,links.length);
        assertEquals("1",links[0].getDestination().getID());
    }
    
    public void testPartB() {
        System.out.println("execute");
        
        AddMusicReferences instance = new AddMusicReferences();
        instance.setDecider(decider);
        
        instance.execute(test);

        assertNotNull(test.getActor("Artist","1"));
        assertNull(test.getActor("Artist","2"));
        assertNotNull(test.getActor("Artist","3"));
        assertNull(test.getActor("Artist","4"));
        Link[] links = test.getLinkBySource("Given",b);
        assertNotNull(links);
        assertEquals(2,links.length);
    }
    
    public void testPartC() {
        System.out.println("execute");
        
         AddMusicReferences instance = new AddMusicReferences();
         instance.setDecider(decider);
        
        instance.execute(test);

        assertNotNull(test.getActor("Artist","3"));
        assertNull(test.getActor("Artist","4"));
        assertNotNull(test.getActor("Artist","5"));
        assertNull(test.getActor("Artist","6"));
        Link[] links = test.getLinkBySource("Given",c);
        assertNotNull(links);
        assertEquals(2,links.length);
    }
    
    public void testPartD() {
        System.out.println("execute");
        
        AddMusicReferences instance = new AddMusicReferences();
        instance.setDecider(decider);
        
        instance.execute(test);
        
        assertNotNull(test.getActor("Artist","7"));
        assertNull(test.getActor("Artist","8"));
        Link[] links = test.getLinkBySource("Given",d);
        assertNotNull(links);
        assertEquals(1,links.length);
    }
    
    public void testPartE() {
        System.out.println("execute");
        
        AddMusicReferences instance = new AddMusicReferences();
        instance.setDecider(decider);
        
        instance.execute(test);
        
        assertNull(test.getLinkBySource("Given",e));
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