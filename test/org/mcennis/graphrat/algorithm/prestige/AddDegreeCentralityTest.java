/*

 * AddDegreeCentralityTest.java

 * JUnit based test

 *

 * Created on 9 July 2007, 14:49

 *

 * Copyright Daniel McEnnis, all rights reserved

 */



package org.mcennis.graphrat.algorithm.prestige;



import java.util.List;

import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

import junit.framework.*;

import org.mcennis.graphrat.algorithm.DegreeGraphProperties;
import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.GraphFactory;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.actor.ActorFactory;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.link.LinkFactory;


/**

 *

 * @author Daniel McEnnis

 */

public class AddDegreeCentralityTest extends TestCase {

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

    DegreeGraphProperties degree;

    

    public AddDegreeCentralityTest(String testName) {

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

        props.set("LinkType","Interest");

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

        props.set("LinkType","Music");

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

        props.set("PropertyID","GraphProp");

        degree = new DegreeGraphProperties();

        

    }



    protected void tearDown() throws Exception {

    }



    /**

     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testExecute() {

        System.out.println("execute");

        

       DegreePrestige instance = new DegreePrestige();

        

        instance.execute(test);

        

    }



    /**

     * Test of calculatePrestige method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculatePrestigeA() {

        System.out.println("calculatePrestigeA");

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculatePrestige(test);

        

        Actor ret = test.getActor("User","A");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreePrestige");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.5,((Double)(value.get(0))).doubleValue(),0.001);

    }



    /**

     * Test of calculatePrestige method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculatePrestigeB() {

        System.out.println("calculatePrestigeB");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculatePrestige(test);

        

        Actor ret = test.getActor("User","B");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreePrestige");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.5,((Double)(value.get(0))).doubleValue(),0.001);

    }



    /**

     * Test of calculatePrestige method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculatePrestigeC() {

        System.out.println("calculatePrestigeC");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculatePrestige(test);

        

        Actor ret = test.getActor("User","C");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreePrestige");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.75,((Double)(value.get(0))).doubleValue(),0.001);

    }



    /**

     * Test of calculatePrestige method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculatePrestigeD() {

        System.out.println("calculatePrestigeD");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculatePrestige(test);

        

        Actor ret = test.getActor("User","D");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreePrestige");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.25,((Double)(value.get(0))).doubleValue(),0.001);

    }



    /**

     * Test of calculatePrestige method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculatePrestigeE() {

        System.out.println("calculatePrestigeE");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculatePrestige(test);

        

        Actor ret = test.getActor("User","E");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreePrestige");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.25,((Double)(value.get(0))).doubleValue(),0.001);

    }



    /**

     * Test of calculateCentrality method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculateCentralityA() {

        System.out.println("calculateCentralityA");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculateCentrality(test);

        

        Actor ret = test.getActor("User","A");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreeCentrality");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.75,((Double)(value.get(0))).doubleValue(),0.001);

    }

    /**

     * Test of calculateCentrality method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculateCentralityB() {

        System.out.println("calculateCentralityB");

         

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculateCentrality(test);

        

        Actor ret = test.getActor("User","B");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreeCentrality");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.75,((Double)(value.get(0))).doubleValue(),0.001);

    }

    /**

     * Test of calculateCentrality method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculateCentralityC() {

        System.out.println("calculateCentralityC");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculateCentrality(test);

        

        Actor ret = test.getActor("User","C");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreeCentrality");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.5,((Double)(value.get(0))).doubleValue(),0.001);

        

    }

    /**

     * Test of calculateCentrality method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculateCentralityD() {

        System.out.println("calculateCentralityD");

         

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculateCentrality(test);

        

        Actor ret = test.getActor("User","D");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreeCentrality");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.25,((Double)(value.get(0))).doubleValue(),0.001);

        

    }

    /**

     * Test of calculateCentrality method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeCentrality.

     */

    public void testCalculateCentralityE() {

        System.out.println("calculateCentralityE");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.calculateCentrality(test);

        

        Actor ret = test.getActor("User","E");

        assertNotNull(ret);

        Property prop = ret.getProperty("Knows DegreeCentrality");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.0,((Double)(value.get(0))).doubleValue(),0.001);

        

    }

    

    public void testGraphCentrality(){

        System.out.println("graphCentrality");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.execute(test);

        

        Property prop = test.getProperty("Knows DegreeCentrality");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.125,((Double)(value.get(0))).doubleValue(),0.001);

        

    }

    

    public void testGraphCentralitySD(){

        System.out.println("graphCentralitySD");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.execute(test);

        

        Property prop = test.getProperty("Knows DegreeCentralitySD");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.425,((Double)(value.get(0))).doubleValue(),0.001);

        

    }

    

    public void testGraphPrestige(){

        System.out.println("calculateGraphPrestige");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.execute(test);

        

        Property prop = test.getProperty("Knows DegreePrestige");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.125,((Double)(value.get(0))).doubleValue(),0.001);

        

    }

    

    public void testGraphPrestigeSD(){

        System.out.println("calculateGraphPrestigeSD");

        

        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("relation","Knows");

        props.set("actorType","User");

        degree.init(props);

        degree.execute(test);

        DegreePrestige instance = new DegreePrestige();

        instance.init(props);

        instance.execute(test);

        

        Property prop = test.getProperty("Knows DegreePrestigeSD");

        assertNotNull(prop);

        List<Object> value = prop.getValue();

        assertNotNull(value);

        assertEquals(1,value.size());

        assertEquals(0.175,((Double)(value.get(0))).doubleValue(),0.001);

        

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

