/*

 * Created 5-2-08

 * Copyright Daniel McEnnis, see license.txt

 */



package org.mcennis.graphrat.algorithm.collaborativefiltering;



import java.util.List;

import junit.framework.TestCase;

import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;

import org.mcennis.graphrat.graph.MemGraph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.actor.ActorFactory;


import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.link.LinkFactory;



/**

 *

 * @author Daniel McEnnis

 */

public class User2UserTest extends TestCase {

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

    

    public User2UserTest(String testName) {

        super(testName);

    }            



    @Override

    protected void setUp() throws Exception {

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



    /**

     * Test of execute method, of class User2User.

     */

    public void testExecute() {

        System.out.println("execute");

        User2User instance = new User2User();

        instance.execute(base);

    }



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserA() {

        System.out.println("execute test user A");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", a);

        assertNotNull(user2user);

        assertEquals(1,user2user.size());

        assertEquals(1.0,user2user.get(0).getStrength(),0.0001);

        assertEquals(f,user2user.get(0).getDestination());

        List<Link> user2artist = base.getLinkBySource("Derived", a);

        assertNull(user2artist);

    }



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserB() {

        System.out.println("execute test user B");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", b);

        assertNotNull(user2user);

        assertEquals(3,user2user.size());

        for(int i=0;i<user2user.size();++i){

            if(user2user.get(i).getDestination().equals(d)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(e)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(g)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else{

                fail("Unexpected link '"+user2user.get(i).getDestination().getID()+"' found");

            }

        }

        List<Link> user2artist = base.getLinkBySource("Derived", b);

        assertEquals(1,user2artist.size());

        assertEquals(artist4,user2artist.get(0).getDestination());

        assertEquals(1.4142,user2artist.get(0).getStrength(),0.0001);

    }

    

    



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserC() {

        System.out.println("execute test user C");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", c);

        assertNotNull(user2user);

        for(int i=0;i<user2user.size();++i){

            if(user2user.get(i).getDestination().equals(d)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(e)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(g)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else{

                fail("Unexpected link '"+user2user.get(i).getDestination().getID()+"' found");

            }

        }

        List<Link> user2artist = base.getLinkBySource("Derived", c);

        assertEquals(1,user2artist.size());

        assertEquals(artist3,user2artist.get(0).getDestination());

        assertEquals(1.4142,user2artist.get(0).getStrength(),0.0001);

    }



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserD() {

        System.out.println("execute test user D");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", d);

        assertNotNull(user2user);

        assertEquals(3,user2user.size());

        for(int i=0;i<user2user.size();++i){

            if(user2user.get(i).getDestination().equals(b)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(c)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(e)){

                assertEquals(1.0,user2user.get(i).getStrength(),0.0001);

            }else{

                fail("Unexpected link '"+user2user.get(i).getDestination().getID()+"' found");

            }

        }

        List<Link> user2artist = base.getLinkBySource("Derived", d);

        assertNull(user2artist);

    }



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserE() {

        System.out.println("execute test user E");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", e);

        assertNotNull(user2user);

        assertEquals(3,user2user.size());

        for(int i=0;i<user2user.size();++i){

            if(user2user.get(i).getDestination().equals(b)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(c)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(d)){

                assertEquals(1.0,user2user.get(i).getStrength(),0.0001);

            }else{

                fail("Unexpected link '"+user2user.get(i).getDestination().getID()+"' found");

            }

        }

        List<Link> user2artist = base.getLinkBySource("Derived", e);

        assertNull(user2artist);

    }



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserF() {

        System.out.println("execute test user F");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", f);

        assertNotNull(user2user);

        assertEquals(1,user2user.size());

        assertEquals(1.0,user2user.get(0).getStrength(),0.0001);

        assertEquals(a,user2user.get(0).getDestination());

        List<Link> user2artist = base.getLinkBySource("Derived", f);

        assertNull(user2artist);

    }



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserG() {

        System.out.println("execute test user G");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", g);

        assertNotNull(user2user);

        assertEquals(2,user2user.size());

        for(int i=0;i<user2user.size();++i){

            if(user2user.get(i).getDestination().equals(b)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2user.get(i).getDestination().equals(c)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else{

                fail("Unexpected link '"+user2user.get(i).getDestination().getID()+"' found");

            }

        }

        List<Link> user2artist = base.getLinkBySource("Derived", g);

        assertNotNull(user2artist);

        for(int i=0;i<user2artist.size();++i){

            if(user2artist.get(i).getDestination().equals(artist3)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else if(user2artist.get(i).getDestination().equals(artist4)){

                assertEquals(0.7071,user2user.get(i).getStrength(),0.0001);

            }else{

                fail("Unexpected link '"+user2artist.get(i).getDestination().getID()+"' found");

            }

        }

    }



    /**

     * Test of execute method, of class User2User.

     */

    public void testUserH() {

        System.out.println("execute test user H");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("createUserSimilarity", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2user = base.getLinkBySource("Nearest Neighbor", h);

        assertNull(user2user);

        List<Link> user2artist = base.getLinkBySource("Derived", h);

        assertNull(user2artist);

    }



    public void testAlreadyMade(){

        System.out.println("execute test user H");

        User2User instance = new User2User();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("evaluation", "true");

        instance.init(props);

        instance.execute(base);

        

        List<Link> user2artist = base.getLinkBySource("Derived", d);

        assertNotNull(user2artist);

        assertEquals(3,user2artist.size());

    }

}

