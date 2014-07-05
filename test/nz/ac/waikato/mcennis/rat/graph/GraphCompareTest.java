/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph;

import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;

/**
 *
 * @author mcennis
 */
public class GraphCompareTest extends TestCase {

    MemGraph empty;
    MemGraph oneActorOneMode;
    MemGraph oneActorOneMode2;
    MemGraph oneActorOneMode3;
    MemGraph twoActorOneMode;
    MemGraph oneActorTwoMode;
    MemGraph oneLinkOneRelation;
    MemGraph oneLinkOneRelation2;
    MemGraph oneLinkTwoRelation;
    MemGraph oneLinkTwoRelation2;
    NullGraph nullGraph;
    Actor a;
    Actor b;
    Actor c;
    Actor d;
    Link l;
    Link m;
    Link n;
    Link o;
    Link p;

    public GraphCompareTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        empty = new MemGraph();
        nullGraph = new NullGraph();
        oneActorOneMode = new MemGraph();
        oneActorOneMode2 = new MemGraph();
        oneActorOneMode3 = new MemGraph();
        twoActorOneMode = new MemGraph();
        oneActorTwoMode = new MemGraph();
        oneLinkOneRelation = new MemGraph();
        oneLinkOneRelation2 = new MemGraph();
        oneLinkTwoRelation = new MemGraph();
        a = ActorFactory.newInstance().create("A", "A");
        b = ActorFactory.newInstance().create("A", "B");
        c = ActorFactory.newInstance().create("B", "A");
        d = ActorFactory.newInstance().create("B", "B");
        l = LinkFactory.newInstance().create("L");
        l.set(a, 1.0, b);
        m = LinkFactory.newInstance().create("L");
        m.set(c, 1.0, d);
        n = LinkFactory.newInstance().create("M");
        n.set(a, 1.0, b);
        o = LinkFactory.newInstance().create("M");
        o.set(c, 1.0, d);
        p = LinkFactory.newInstance().create("M");
        p.set(d, 1.0, c);
        oneActorOneMode.add(a);
        oneActorOneMode2.add(c);
        oneActorOneMode3.add(c);
        Property prop = PropertyFactory.newInstance().create("Type", String.class);
        oneActorOneMode3.add(prop);
        twoActorOneMode.add(a);
        twoActorOneMode.add(b);
        oneActorTwoMode.add(a);
        oneActorTwoMode.add(c);
        oneLinkOneRelation.add(a);
        oneLinkOneRelation.add(b);
        oneLinkOneRelation.add(n);
        oneLinkOneRelation2.add(c);
        oneLinkOneRelation2.add(d);
        oneLinkOneRelation2.add(m);
        oneLinkTwoRelation.add(a);
        oneLinkTwoRelation.add(b);
        oneLinkTwoRelation.add(c);
        oneLinkTwoRelation.add(d);
        oneLinkTwoRelation.add(l);
        oneLinkTwoRelation.add(o);
        oneLinkTwoRelation2.add(a);
        oneLinkTwoRelation2.add(b);
        oneLinkTwoRelation2.add(c);
        oneLinkTwoRelation2.add(d);
        oneLinkTwoRelation2.add(l);
        oneLinkTwoRelation2.add(o);
        oneLinkTwoRelation2.add(p);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testClassDifferenceCompareTo() {
        System.out.println("compareTo");
        int expResult = 1;
        int result = GraphCompare.compareTo(empty, nullGraph);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testDifferingModeListCompareTo() {
        System.out.println("compareTo");
        int expResult = -1;
        int result = GraphCompare.compareTo(oneActorOneMode2, oneActorOneMode);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testUnEqualModeListCompareTo() {
        System.out.println("compareTo");
        int expResult = -1;
        int result = GraphCompare.compareTo(oneActorOneMode, oneActorTwoMode);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testLinkListModeListCompareTo() {
        System.out.println("compareTo");
        int expResult = -1;
        int result = GraphCompare.compareTo(oneLinkOneRelation2, oneLinkOneRelation);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testActorCompareTo() {
        System.out.println("compareTo");
        int expResult = 1;
        int result = GraphCompare.compareTo(oneActorOneMode, twoActorOneMode);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testDifferingRelationListCompareTo() {
        System.out.println("compareTo");
        Graph left = null;
        Object rightO = null;
        int expResult = -1;
        int result = GraphCompare.compareTo(oneLinkOneRelation, oneLinkOneRelation2);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testUnequalRelationListCompareTo() {
        System.out.println("compareTo");
        int expResult = 1;
        int result = GraphCompare.compareTo(oneLinkOneRelation, oneLinkTwoRelation);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testLinkCompareTo() {
        System.out.println("compareTo");
        int expResult = 1;
        int result = GraphCompare.compareTo(oneLinkTwoRelation, oneLinkTwoRelation2);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testLeftNullCompareTo() {
        System.out.println("compareTo");
        int expResult = 1;
        int result = GraphCompare.compareTo(null, oneActorOneMode);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testRightNullCompareTo() {
        System.out.println("compareTo");
        int expResult = -1;
        int result = GraphCompare.compareTo(oneActorOneMode, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testBothNullCompareTo() {
        System.out.println("compareTo");
        int expResult = 0;
        int result = GraphCompare.compareTo(null, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class GraphCompare.
     */
    public void testPropertyPresence() {
        System.out.println("compareTo");
        int expResult = 1;
        int result = GraphCompare.compareTo(oneActorOneMode2, oneActorOneMode3);
        assertEquals(expResult, result);
    }

}
