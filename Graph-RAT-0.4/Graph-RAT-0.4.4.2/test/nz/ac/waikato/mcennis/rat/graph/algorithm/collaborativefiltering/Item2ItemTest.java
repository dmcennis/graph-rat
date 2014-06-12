/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering;

import java.util.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;

/**
 *
 * @author dm75
 */
public class Item2ItemTest extends TestCase {
    
        MemGraph base;
    
    Actor a;
    Actor b;
    Actor c;
    Actor d;
    Actor e;
    Actor f;
    Actor g;
    Actor h;
    
    Actor artist1;
    Actor artist2;
    Actor artist3;
    Actor artist4;
    Actor artist5;
    Actor artist6;
    
    Link a1;
    Link a2;
    Link a5;
    Link b1;
    Link b3;
    Link c1;
    Link c4;
    Link d1;
    Link d3;
    Link d4;
    Link e1;
    Link e3;
    Link e4;
    Link f1;
    Link f2;
    Link f5;
    Link g1;
    

    public Item2ItemTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        base = new MemGraph();
        base.setID("test");
        
        Properties props = new Properties();
        props.setProperty("ActorClass", "MemGraph");
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","a");
        a = ActorFactory.newInstance().create(props);
        base.add(a);
        props.setProperty("ActorID","b");
        b = ActorFactory.newInstance().create(props);
        base.add(b);
        props.setProperty("ActorID","c");
        c = ActorFactory.newInstance().create(props);
        base.add(c);
        props.setProperty("ActorID","d");
        d = ActorFactory.newInstance().create(props);
        base.add(d);
        props.setProperty("ActorID","e");
        e = ActorFactory.newInstance().create(props);
        base.add(e);
        props.setProperty("ActorID","f");
        f = ActorFactory.newInstance().create(props);
        base.add(f);
        props.setProperty("ActorID","g");
        g = ActorFactory.newInstance().create(props);
        base.add(g);
        props.setProperty("ActorID","h");
        h = ActorFactory.newInstance().create(props);
        base.add(h);
        
        props.setProperty("ActorType", "Artist");
        props.setProperty("ActorID","1");
        artist1 = ActorFactory.newInstance().create(props);
        base.add(artist1);
        props.setProperty("ActorID","2");
        artist2 = ActorFactory.newInstance().create(props);
        base.add(artist2);
        props.setProperty("ActorID","3");
        artist3 = ActorFactory.newInstance().create(props);
        base.add(artist3);
        props.setProperty("ActorID","4");
        artist4 = ActorFactory.newInstance().create(props);
        base.add(artist4);
        props.setProperty("ActorID","5");
        artist5 = ActorFactory.newInstance().create(props);
        base.add(artist5);
        props.setProperty("ActorID","6");
        artist6 = ActorFactory.newInstance().create(props);
        base.add(artist6);
        
        props.setProperty("LinkType","Given");
        a1 = LinkFactory.newInstance().create(props);
        a1.set(a, 1.0, artist1);
        base.add(a1);
        a2 = LinkFactory.newInstance().create(props);
        a2.set(a, 1.0, artist2);
        base.add(a2);
        a5 = LinkFactory.newInstance().create(props);
        a5.set(a, 3.0, artist5);
        base.add(a5);
        b1 = LinkFactory.newInstance().create(props);
        b1.set(b, 1.0, artist1);
        base.add(b1);
        b3 = LinkFactory.newInstance().create(props);
        b3.set(b, 1.0, artist3);
        base.add(b3);
        c1 = LinkFactory.newInstance().create(props);
        c1.set(c, 1.0, artist1);
        base.add(c1);
        c4 = LinkFactory.newInstance().create(props);
        c4.set(c, 1.0, artist4);
        base.add(c4);
        d1 = LinkFactory.newInstance().create(props);
        d1.set(d, 1.0, artist1);
        base.add(d1);
        d3 = LinkFactory.newInstance().create(props);
        d3.set(d, 2.0, artist3);
        base.add(d3);
        d4 = LinkFactory.newInstance().create(props);
        d4.set(d, 2.0, artist4);
        base.add(d4);
        e1 = LinkFactory.newInstance().create(props);
        e1.set(e, 1.0, artist1);
        base.add(e1);
        e3 = LinkFactory.newInstance().create(props);
        e3.set(e, 2.0, artist3);
        base.add(e3);
        e4 = LinkFactory.newInstance().create(props);
        e4.set(e, 2.0, artist4);
        base.add(e4);
        f1 = LinkFactory.newInstance().create(props);
        f1.set(f, 1.0, artist1);
        base.add(f1);
        f2 = LinkFactory.newInstance().create(props);
        f2.set(f, 1.0, artist2);
        base.add(f2);
        f5 = LinkFactory.newInstance().create(props);
        f5.set(f, 3.0, artist5);
        base.add(f5);
        g1 = LinkFactory.newInstance().create(props);
        g1.set(g, 1.0, artist1);
        base.add(g1);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class Item2Item.
     */
    public void testExecute() {
        System.out.println("execute");
        Item2Item instance = new Item2Item();
        instance.execute(base);
    }

    public void testExecute1(){
        System.out.println("execute 1");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Correlated Artist", artist1);
        assertNull(correlation);
    }

    public void testExecute2(){
        System.out.println("execute 2");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Correlated Artist", artist2);
        assertNull(correlation);
    }

    public void testExecute3(){
        System.out.println("execute 3");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Correlated Artist", artist3);
        assertNotNull(correlation);
        assertEquals(1,correlation.length);
        assertEquals(artist4,correlation[0].getDestination());
        assertEquals(0.6364,correlation[0].getStrength(),0.0001);
    }

    public void testExecute4(){
        System.out.println("execute 4");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Correlated Artist", artist4);
        assertNotNull(correlation);
        assertEquals(1,correlation.length);
        assertEquals(artist3,correlation[0].getDestination());
        assertEquals(0.6364,correlation[0].getStrength(),0.0001);
    }

    public void testExecute5(){
        System.out.println("execute 5");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Correlated Artist", artist5);
        assertNull(correlation);
    }

    public void testExecute6(){
        System.out.println("execute 6");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Correlated Artist", artist6);
        assertNull(correlation);
    }

    public void testExecuteB(){
       System.out.println("execute B");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Derived", b);
        assertNotNull(correlation);
        assertEquals(0.3182,correlation[0].getStrength(),0.0001);
        assertEquals(artist4,correlation[0].getDestination());
    }
    
    public void testExecuteC(){
       System.out.println("execute C");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Derived", c);
        assertNotNull(correlation);
        assertEquals(0.3182,correlation[0].getStrength(),0.0001);
        assertEquals(artist3,correlation[0].getDestination());
    }
    
    public void testExecuteA(){
       System.out.println("execute A");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Derived", a);
        assertNull(correlation);
    }
    
    public void testEvaluation(){
       System.out.println("execute B");
        Properties props = new Properties();
        props.setProperty("createArtistSimilarity","true");
        props.setProperty("evaluation", "true");
        Item2Item instance = new Item2Item();
        instance.init(props);
        instance.execute(base);
        
        Link[] correlation = base.getLinkBySource("Derived", d);
        assertNotNull(correlation);
        assertEquals(2,correlation.length);
    }
}
