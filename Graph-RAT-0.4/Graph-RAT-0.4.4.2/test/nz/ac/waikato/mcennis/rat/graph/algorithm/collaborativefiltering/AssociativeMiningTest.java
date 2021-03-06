/*
 * Copyright Daniel McEnnis, see license.txt
 */
package org.mcennis.graphrat.algorithm.collaborativefiltering;

import java.util.Properties;
import junit.framework.TestCase;
import org.mcennis.graphrat.graph.MemGraph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class AssociativeMiningTest extends TestCase {

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

    public AssociativeMiningTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        base = new MemGraph();
        base.setID("test");

        Properties props = new Properties();
        props.setProperty("ActorClass", "MemGraph");
        props.setProperty("ActorType", "User");
        props.setProperty("ActorID", "a");
        a = ActorFactory.newInstance().create(props);
        base.add(a);
        props.setProperty("ActorID", "b");
        b = ActorFactory.newInstance().create(props);
        base.add(b);
        props.setProperty("ActorID", "c");
        c = ActorFactory.newInstance().create(props);
        base.add(c);
        props.setProperty("ActorID", "d");
        d = ActorFactory.newInstance().create(props);
        base.add(d);
        props.setProperty("ActorID", "e");
        e = ActorFactory.newInstance().create(props);
        base.add(e);
        props.setProperty("ActorID", "f");
        f = ActorFactory.newInstance().create(props);
        base.add(f);
        props.setProperty("ActorID", "g");
        g = ActorFactory.newInstance().create(props);
        base.add(g);
        props.setProperty("ActorID", "h");
        h = ActorFactory.newInstance().create(props);
        base.add(h);

        props.setProperty("ActorType", "Artist");
        props.setProperty("ActorID", "1");
        artist1 = ActorFactory.newInstance().create(props);
        base.add(artist1);
        props.setProperty("ActorID", "2");
        artist2 = ActorFactory.newInstance().create(props);
        base.add(artist2);
        props.setProperty("ActorID", "3");
        artist3 = ActorFactory.newInstance().create(props);
        base.add(artist3);
        props.setProperty("ActorID", "4");
        artist4 = ActorFactory.newInstance().create(props);
        base.add(artist4);
        props.setProperty("ActorID", "5");
        artist5 = ActorFactory.newInstance().create(props);
        base.add(artist5);
        props.setProperty("ActorID", "6");
        artist6 = ActorFactory.newInstance().create(props);
        base.add(artist6);

        props.setProperty("LinkType", "Given");
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
     * Test of execute method, of class AssociativeMining.
     */
    public void testExecute() {
        System.out.println("execute");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
    }

    public void testArtist1() {
        System.out.println("test artist 1");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
        Property corelations = artist1.getProperty("Correlated Artist");
        assertNull(corelations);
    }

    public void testArtist2() {
        System.out.println("test artist 2");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
        Property corelations = artist2.getProperty("Correlated Artist");
        assertNotNull(corelations);
        assertEquals(AssociativeMiningItems.class, corelations.getPropertyClass());
        Object[] values = corelations.getValue();
        assertNotNull(values);
        assertEquals(1, values.length);
        for (int i = 0; i < values.length; ++i) {
            AssociativeMiningItems item = (AssociativeMiningItems) values[i];
            Actor actor = item.getActors()[0];
            if (actor.getID().contentEquals("5")) {
                assertTrue(item.isPositive());
            } else {
                fail("Unexpected Multi-actor set found");
            }
        }
    }

    public void testArtist3() {
        System.out.println("test artist 3");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
        Property corelations = artist3.getProperty("Correlated Artist");
        assertNotNull(corelations);
        assertEquals(AssociativeMiningItems.class, corelations.getPropertyClass());
        Object[] values = corelations.getValue();
        assertNotNull(values);
        assertEquals(3, values.length);
        for (int i = 0; i < values.length; ++i) {
            AssociativeMiningItems item = (AssociativeMiningItems) values[i];
            if (item.getActors().length > 1) {
                assertFalse(item.isPositive());
            } else {
                if (item.getActors()[0].equals(artist5)) {
                    assertFalse(item.isPositive());
                } else if (item.getActors()[0].equals(artist2)) {
                    assertFalse(item.isPositive());
                } else {
                    fail("Unexpected associative item with actor id " + item.getActors()[0].getID());
                }
            }
        }
    }

    public void testArtist4() {
        System.out.println("test artist 4");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
        Property corelations = artist4.getProperty("Correlated Artist");
        assertNotNull(corelations);
        assertEquals(AssociativeMiningItems.class, corelations.getPropertyClass());
        Object[] values = corelations.getValue();
        assertNotNull(values);
        assertEquals(3, values.length);
        for (int i = 0; i < values.length; ++i) {
            AssociativeMiningItems item = (AssociativeMiningItems) values[i];
            if (item.getActors().length > 1) {
                assertFalse(item.isPositive());
            } else {
                if (item.getActors()[0].equals(artist2)) {
                    assertFalse(item.isPositive());
                } else if (item.getActors()[0].equals(artist5)) {
                    assertFalse(item.isPositive());
                } else {
                    fail("Unexpected associative item with actor id " + item.getActors()[0].getID());
                }
            }
        }
    }

    public void testArtist5() {
        System.out.println("test artist 5");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
        Property corelations = artist5.getProperty("Correlated Artist");
        assertNotNull(corelations);
        assertEquals(AssociativeMiningItems.class, corelations.getPropertyClass());
        Object[] values = corelations.getValue();
        assertNotNull(values);
        assertEquals(1, values.length);
        for (int i = 0; i < values.length; ++i) {
            AssociativeMiningItems item = (AssociativeMiningItems) values[i];
            if (item.getActors()[0].equals(artist2)) {
                assertTrue(item.isPositive());
            } else {
                fail("Unexpected associative item with actor id " + item.getActors()[0].getID());
            }
        }
    }

    public void testArtist6() {
        System.out.println("test artist 6");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
        Property corelations = artist6.getProperty("Correlated Artist");
        assertNull(corelations);
    }

    public void testActorA() {
        System.out.println("test artist 6");
        AssociativeMining instance = new AssociativeMining();
        instance.execute(base);
        Link[] links = base.getLinkBySource("Derived", a);

    }
}
