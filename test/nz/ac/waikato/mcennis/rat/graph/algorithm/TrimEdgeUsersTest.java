/*


 * TrimEdgeUsersTest.java


 * JUnit based test


 *


 * Created on 13 June 2007, 12:35


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


import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;


import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.property.Property;


import org.dynamicfactory.property.PropertyFactory;





/**


 *


 * @author Daniel McEnnis


 */


public class TrimEdgeUsersTest extends TestCase {


 


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





    public TrimEdgeUsersTest(String testName) {


        super(testName);


    }





    protected void setUp() throws Exception {


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("ActorType","User");


        props.set("ActorID","A");


        props.set("PropertyType","Basic");


        props.set("PropertyID","interest");


        props.set("LinkType","Interest");


        props.set("LinkClass","Basic");


        props.set("Graph","Memory");


        test = GraphFactory.newInstance().create(props);


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


    }





    protected void tearDown() throws Exception {


    }





    /**


     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.TrimEdgeUsers.


     */


    public void testUserRemoval() {


        System.out.println("execute");


        


        TrimEdgeUsers instance = new TrimEdgeUsers();


        


        instance.execute(test);





        assertNotNull(test.getActor("User","A"));


        assertNotNull(test.getActor("User","B"));


        assertNotNull(test.getActor("User","C"));


        assertNotNull(test.getActor("User","D"));


        assertNull(test.getActor("User","E"));


        


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


