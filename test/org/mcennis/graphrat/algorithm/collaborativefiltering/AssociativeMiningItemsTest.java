/*

 * Created 5-2-08

 * Copyright Daniel McEnnis, see license.txt

 */



package org.mcennis.graphrat.algorithm.collaborativefiltering;


import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;

import java.util.TreeSet;

import junit.framework.TestCase;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.MemGraph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.actor.ActorFactory;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.link.LinkFactory;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;
import org.mcennis.graphrat.query.LinkQueryFactory;
import org.mcennis.graphrat.query.link.LinkByRelation;



/**

 *

 * @author Daniel McEnnis

 */

public class AssociativeMiningItemsTest extends TestCase {

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

    LinkByRelation relation;

    public AssociativeMiningItemsTest(String testName) {

        super(testName);

    }            



    @Override

    protected void setUp() throws Exception {

        LinkByRelation relation = (LinkByRelation)LinkQueryFactory.newInstance().create("LinkByRelation");
        base = new MemGraph();

        base.setID("test");

        

        Properties props = PropertiesFactory.newInstance().create();

        props.set("ActorClass", "MemGraph");

        props.set("ActorType","User");

        props.set("ActorID","a");

        a = ActorFactory.newInstance().create(props);

        base.add(a);

        props.set("ActorID","b");

        b = ActorFactory.newInstance().create(props);

        base.add(b);

        props.set("ActorID","c");

        c = ActorFactory.newInstance().create(props);

        base.add(c);

        props.set("ActorID","d");

        d = ActorFactory.newInstance().create(props);

        base.add(d);

        props.set("ActorID","e");

        e = ActorFactory.newInstance().create(props);

        base.add(e);

        props.set("ActorID","f");

        f = ActorFactory.newInstance().create(props);

        base.add(f);

        props.set("ActorID","g");

        g = ActorFactory.newInstance().create(props);

        base.add(g);

        props.set("ActorID","h");

        h = ActorFactory.newInstance().create(props);

        base.add(h);

        

        props.set("ActorType", "Artist");

        props.set("ActorID","1");

        artist1 = ActorFactory.newInstance().create(props);

        base.add(artist1);

        props.set("ActorID","2");

        artist2 = ActorFactory.newInstance().create(props);

        base.add(artist2);

        props.set("ActorID","3");

        artist3 = ActorFactory.newInstance().create(props);

        base.add(artist3);

        props.set("ActorID","4");

        artist4 = ActorFactory.newInstance().create(props);

        base.add(artist4);

        props.set("ActorID","5");

        artist5 = ActorFactory.newInstance().create(props);

        base.add(artist5);

        props.set("ActorID","6");

        artist6 = ActorFactory.newInstance().create(props);

        base.add(artist6);

        

        props.set("LinkType","Given");

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

    

    public void testConstructor(){
        relation.buildQuery("Given", false);
        AssociativeMiningItems item = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist1);

        assertEquals(7,item.activeUsers.size());

        assertEquals(1,item.actors.size());

        assertEquals(artist1,item.maxMember);

        assertEquals(true,item.positive);

        assertEquals(8.0,item.total,0.0001);

    }



    /**

     * Test of isPositive method, of class AssociativeMiningItems.

     */

    public void testIsPositive() {
        relation.buildQuery("Given", false);

        System.out.println("isPositive");

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist1);

        boolean expResult = true;

        boolean result = instance.isPositive();

        assertEquals(expResult, result);

        instance.setPositive(false);

        assertEquals(false,instance.isPositive());

    }



    /**

     * Test of setPositive method, of class AssociativeMiningItems.

     */

    public void testSetPositive() {

        System.out.println("setPositive");
        relation.buildQuery("Given", false);

        boolean isPositive = false;

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist1);

        instance.setPositive(isPositive);

    }



    /**

     * Test of expand method, of class AssociativeMiningItems.

     */

    public void testExpand() {

        System.out.println("expand");
        relation.buildQuery("Given", false);

        Graph g = null;

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist1);

        AssociativeMiningItems result = instance.expand(artist2);

        assertEquals(2,result.activeUsers.size());

        assertEquals(2,result.actors.size());

        assertEquals(true,result.positive);

        assertEquals(artist2,result.maxMember);

        assertEquals(8.0,result.total,0.0001);

    }



    /**

     * Test of getActors method, of class AssociativeMiningItems.

     */

    public void testGetActors() {

        System.out.println("getActors");
        relation.buildQuery("Given", false);

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist1);

        Actor[] result = instance.getActors();

        assertEquals(1, result.length);

        assertEquals(artist1,result[0]);

    }



    /**

     * Test of isGreater method, of class AssociativeMiningItems.

     */

    public void testIsGreaterTrue() {

        System.out.println("isGreaterPositive");

        relation.buildQuery("Given", false);
        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        boolean result = instance.isGreater(artist3);

        assertEquals(true, result);

    }



    /**

     * Test of isGreater method, of class AssociativeMiningItems.

     */

    public void testIsGreaterFalse() {

        System.out.println("isGreaterNegative");

        relation.buildQuery("Given", false);
        Actor a = null;

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        boolean result = instance.isGreater(artist1);

        assertEquals(false, result);

    }



    /**

     * Test of significanceTest method, of class AssociativeMiningItems.

     */

    public void testSignificanceTestPositive() {

        System.out.println("significanceTest+");

        relation.buildQuery("Given", false);
        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        int expResult = 1;

        int result = instance.significanceTest(artist5);

        assertEquals(expResult, result);

    }



    /**

     * Test of significanceTest method, of class AssociativeMiningItems.

     */

    public void testSignificanceTestNegative() {

        System.out.println("significanceTest-");

        relation.buildQuery("Given", false);
        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        int expResult = -1;

        int result = instance.significanceTest(artist4);

        assertEquals(expResult, result);

    }

    

    /**

     * Test of significanceTest method, of class AssociativeMiningItems.

     */

    public void testSignificanceTest1To2() {

        System.out.println("significanceTest1To2");
        relation.buildQuery("Given", false);

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist1);

        int expResult = 0;

        int result = instance.significanceTest(artist2);

        assertEquals(expResult, result);

    }

    

    



    /**

     * Test of significanceTest method, of class AssociativeMiningItems.

     */

    public void testSignificanceTestZero() {

        System.out.println("significanceTest0");

        relation.buildQuery("Given", false);
        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        int expResult = 0;

        int result = instance.significanceTest(artist1);

        assertEquals(expResult, result);

    }



    /**

     * Test of clearTransients method, of class AssociativeMiningItems.

     */

    public void testClearTransients() {

        System.out.println("clearTransients");

        relation.buildQuery("Given", false);
        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        instance.clearTransients();

        assertEquals(0,instance.activeUsers.size());

    }



    /**

     * Test of applies method, of class AssociativeMiningItems.

     */

    public void testAppliesTrue() {

        System.out.println("applies");

        TreeSet<Actor> data = new TreeSet<Actor>();

        data.add(artist1);

        data.add(artist2);

        data.add(artist3);
        relation.buildQuery("Given", false);

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        assertEquals(true, instance.applies(data));

    }



    /**

     * Test of applies method, of class AssociativeMiningItems.

     */

    public void testAppliesFalse() {

        System.out.println("applies");
        relation.buildQuery("Given", false);

        TreeSet<Actor> data = new TreeSet<Actor>();

        data.add(artist1);

        data.add(artist3);

        AssociativeMiningItems instance = new AssociativeMiningItems(base,relation,LinkEnd.SOURCE,artist2);

        assertEquals(false, instance.applies(data));

    }



}

