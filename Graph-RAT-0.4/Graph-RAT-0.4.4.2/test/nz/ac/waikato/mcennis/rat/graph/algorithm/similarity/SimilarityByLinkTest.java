/**
 * Created Jul 29, 2008 - 7:00:27 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.similarity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.BasicUser;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.datavector.MapDataVector;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.CosineDistance;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.distance.EuclideanDistance;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.BasicUserLink;
import nz.ac.waikato.mcennis.rat.graph.link.Link;

/**
 *
 * @author Daniel McEnnis
 */
public class SimilarityByLinkTest extends TestCase {
    
    Graph graph;
    Actor a;
    Actor b;
    Actor c;
    Actor d;
    Actor A1;
    Actor A2;
    Actor A3;
    Actor A4;
    Actor A5;
    Actor A6;
    Actor A7;
    Actor A8;
    CosineDistance distance = new CosineDistance();
    
    public SimilarityByLinkTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        graph = new MemGraph();
        a = new BasicUser("A");
        graph.add(a);
        b = new BasicUser("B");
        graph.add(b);
        c = new BasicUser("C");
        graph.add(c);
        d = new BasicUser("D");
        graph.add(d);
        A1 = new BasicUser("1");
        A1.setType("Target");
        graph.add(A1);
        A2 = new BasicUser("2");
        A2.setType("Target");
        graph.add(A2);
        A3 = new BasicUser("3");
        A3.setType("Target");
        graph.add(A3);
        A4 = new BasicUser("4");
        A4.setType("Target");
        graph.add(A4);
        A5 = new BasicUser("5");
        A5.setType("Target");
        graph.add(A5);
        A6 = new BasicUser("6");
        A6.setType("Target");
        graph.add(A6);
        A7 = new BasicUser("7");
        A7.setType("Target");
        graph.add(A7);
        A8 = new BasicUser("8");
        A8.setType("Target");
        graph.add(A8);
        
        Link a1 = new BasicUserLink();
        a1.setType("listensTo");
        a1.set(a,1.0,A1);
        graph.add(a1);
        Link a2 = new BasicUserLink();
        a2.setType("listensTo");
        a2.set(a,1.0,A2);
        graph.add(a2);
        Link a3 = new BasicUserLink();
        a3.setType("listensTo");
        a3.set(a,1.0,A3);
        graph.add(a3);
        Link a4 = new BasicUserLink();
        a4.setType("listensTo");
        a4.set(a,1.0,A4);
        graph.add(a4);
        Link b1 = new BasicUserLink();
        b1.setType("listensTo");
        b1.set(b,1.0,A1);
        graph.add(b1);
        Link b2 = new BasicUserLink();
        b2.setType("listensTo");
        b2.set(b,1.0,A2);
        graph.add(b2);
        Link b3 = new BasicUserLink();
        b3.setType("listensTo");
        b3.set(b,1.0,A3);
        graph.add(b3);
        Link c4 = new BasicUserLink();
        c4.setType("listensTo");
        c4.set(c,1.0,A4);
        graph.add(c4);
        Link c5 = new BasicUserLink();
        c5.setType("listensTo");
        c5.set(c,1.0,A5);
        graph.add(c5);
        Link c6 = new BasicUserLink();
        c6.setType("listensTo");
        c6.set(c,1.0,A6);
        graph.add(c6);
        Link c7 = new BasicUserLink();
        c7.setType("listensTo");
        c7.set(c,1.0,A7);
        graph.add(c7);
        Link d5 = new BasicUserLink();
        d5.setType("listensTo");
        d5.set(d,1.0,A5);
        graph.add(d5);
        Link d6 = new BasicUserLink();
        d6.setType("listensTo");
        d6.set(d,1.0,A6);
        graph.add(d6);
        Link d7 = new BasicUserLink();
        d7.setType("listensTo");
        d7.set(d,1.0,A7);
        graph.add(d7);
        Link d8 = new BasicUserLink();
        d8.setType("listensTo");
        d8.set(d,1.0,A8);
        graph.add(d8);
        
        Link A4b = new BasicUserLink();
        A4b.setType("listensTo");
        A4b.set(A4, 1.0, b);
        graph.add(A4b);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class SimilarityByLink.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        SimilarityByLink instance = new SimilarityByLink();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("relation", "listensTo");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Tag Similarity",a);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(b,link[0].getDestination());
        HashMap<Actor,Double> left = instance.getMap(a, new HashSet<Actor>(), graph);
        HashMap<Actor,Double> right = instance.getMap(b, new HashSet<Actor>(), graph);
        double value = distance.distance(new MapDataVector(left,graph.getActorCount("User")), new MapDataVector(right,graph.getActorCount("User")));
        assertEquals(value,link[0].getStrength(),0.001);
    }

    /**
     * Test of execute method, of class SimilarityByLink.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        SimilarityByLink instance = new SimilarityByLink();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("relation", "listensTo");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Tag Similarity", b);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(a,link[0].getDestination());
        HashMap<Actor,Double> left = instance.getMap(b, new HashSet<Actor>(), graph);
        HashMap<Actor,Double> right = instance.getMap(a, new HashSet<Actor>(), graph);
        double value = distance.distance(new MapDataVector(left,graph.getActorCount("User")), new MapDataVector(right,graph.getActorCount("User")));
        assertEquals(value,link[0].getStrength(),0.001);
    }

    /**
     * Test of execute method, of class SimilarityByLink.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        SimilarityByLink instance = new SimilarityByLink();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("relation", "listensTo");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Tag Similarity", c);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(d,link[0].getDestination());
        HashMap<Actor,Double> left = instance.getMap(c, new HashSet<Actor>(), graph);
        HashMap<Actor,Double> right = instance.getMap(d, new HashSet<Actor>(), graph);
        double value = distance.distance(new MapDataVector(left,graph.getActorCount("User")), new MapDataVector(right,graph.getActorCount("User")));
        assertEquals(value,link[0].getStrength(),0.001);
    }

    /**
     * Test of execute method, of class SimilarityByLink.
     */
    public void testExecuteD() {
        System.out.println("executeD");
        SimilarityByLink instance = new SimilarityByLink();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("relation", "listensTo");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Tag Similarity", d);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(c,link[0].getDestination());
        HashMap<Actor,Double> left = instance.getMap(d, new HashSet<Actor>(), graph);
        HashMap<Actor,Double> right = instance.getMap(c, new HashSet<Actor>(), graph);
        double value = distance.distance(new MapDataVector(left,graph.getActorCount("User")), new MapDataVector(right,graph.getActorCount("User")));
        assertEquals(value,link[0].getStrength(),0.001);
    }

    /**
     * Test of getMap method, of class SimilarityByLink.
     */
    public void testGetMapIncoming() {
        System.out.println("getMapIncoming");
        HashSet<Actor> total = new HashSet<Actor>();
        SimilarityByLink instance = new SimilarityByLink();
        Properties props = new Properties();
        props.setProperty("linkDirection", "Incoming");
        props.setProperty("actorType", "User");
        props.setProperty("relation", "listensTo");
        instance.init(props);
        HashMap<Actor, Double> result = instance.getMap(b, total, graph);
        assertEquals(1,result.size());
        assertTrue(result.containsKey(A4));
        assertEquals(1.0,result.get(A4));
    }

    /**
     * Test of getMap method, of class SimilarityByLink.
     */
    public void testGetMapAll() {
        System.out.println("getMapAll");
        HashSet<Actor> total = new HashSet<Actor>();
        SimilarityByLink instance = new SimilarityByLink();
        Properties props = new Properties();
        props.setProperty("linkDirection", "All");
        props.setProperty("relation", "listensTo");
        props.setProperty("actorType", "User");
        instance.init(props);
        HashMap<Actor, Double> result = instance.getMap(b, total, graph);
        assertEquals(4,result.size());
        assertTrue(result.containsKey(A4));
        assertEquals(1.0,result.get(A4));
        assertTrue(result.containsKey(A1));
        assertEquals(1.0,result.get(A1));
        assertTrue(result.containsKey(A2));
        assertEquals(1.0,result.get(A2));
        assertTrue(result.containsKey(A3));
        assertEquals(1.0,result.get(A3));
    }

    /**
     * Test of getMap method, of class SimilarityByLink.
     */
    public void testGetMapOutgoing() {
        System.out.println("getMapOutgoing");
        HashSet<Actor> total = new HashSet<Actor>();
        SimilarityByLink instance = new SimilarityByLink();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("relation", "listensTo");
        instance.init(props);
        HashMap<Actor, Double> result = instance.getMap(b, total, graph);
        assertEquals(3,result.size());
        assertTrue(result.containsKey(A1));
        assertEquals(1.0,result.get(A1));
        assertTrue(result.containsKey(A2));
        assertEquals(1.0,result.get(A2));
        assertTrue(result.containsKey(A3));
        assertEquals(1.0,result.get(A3));
    }


}
