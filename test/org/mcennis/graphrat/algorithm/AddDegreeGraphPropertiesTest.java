/*


 * AddDegreeGraphPropertiesTest.java


 * JUnit based test


 *


 * Created on 6 July 2007, 14:26


 *


 * Copyright Daniel McEnnis, all rights reserved


 */





package org.mcennis.graphrat.algorithm;





import java.util.List;
import junit.framework.*;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;


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


public class AddDegreeGraphPropertiesTest extends TestCase {





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





    public AddDegreeGraphPropertiesTest(String testName) {


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


    }





    protected void tearDown() throws Exception {


    }





    /**


     * Test of calculateInDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testBasicCalculateInDegreeA() {


        System.out.println("calculateBasicInDegreeA");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        


        Actor resultActor = test.getActor("User","A");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Knows InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);;


        assertEquals(1,value.size());


        assertEquals(2.0,((Double)value.get(0)).doubleValue(),0.001);


    }


    


    /**


     * Test of calculateInDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testBasicCalculateInDegreeB() {


        System.out.println("calculateBasicInDegreeB");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        


        Actor resultActor = test.getActor("User","B");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Knows InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(2.0,((Double)value.get(0)).doubleValue(),0.001);


    }


    


    /**


     * Test of calculateInDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testBasicCalculateInDegreeC() {


        System.out.println("calculateBasicInDegreeC");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


         Actor resultActor = test.getActor("User","C");


        assertNotNull(resultActor);





        Property inDegree = resultActor.getProperty("Knows InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(3.0,((Double)value.get(0)).doubleValue(),0.001);


   }


    


    /**


     * Test of calculateInDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testBasicCalculateInDegreeD() {


        System.out.println("calculateBasicInDegreeD");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        Actor resultActor = test.getActor("User","D");


        assertNotNull(resultActor);





        Property inDegree = resultActor.getProperty("Knows InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(1.0,((Double)value.get(0)).doubleValue(),0.001);


    }


    


    /**


     * Test of calculateInDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testBasicCalculateInDegreeE() {


        System.out.println("calculateBasicInDegreeE");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        Actor resultActor = test.getActor("User","E");


        assertNotNull(resultActor);





        Property inDegree = resultActor.getProperty("Knows InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(1.0,((Double)value.get(0)).doubleValue(),0.001);


    }


    


    public void testComplexCalculateInDegreeA() {


        System.out.println("calculateComplexInDegreeA");


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        Actor resultActor = test.getActor("User","A");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(-1.7435,((Double)value.get(0)).doubleValue(),0.001);


    }





    public void testComplexCalculateInDegreeB() {


        System.out.println("calculateComplexInDegreeB");


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


       instance.execute(test);


        Actor resultActor = test.getActor("User","B");


        assertNotNull(resultActor);


        Property inDegree = resultActor.getProperty("Interest InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(3.85,((Double)value.get(0)).doubleValue(),0.001);


    }


    


    public void testComplexCalculateInDegreeC() {


        System.out.println("calculateComplexInDegreeC");


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        Actor resultActor = test.getActor("User","C");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(-0.95,((Double)value.get(0)).doubleValue(),0.001);


    }


    


    public void testComplexCalculateInDegreeD() {


        System.out.println("calculateComplexInDegreeD");


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        Actor resultActor = test.getActor("User","D");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(-3.0935,((Double)value.get(0)).doubleValue(),0.001);


    }


    


    public void testComplexCalculateInDegreeE() {


        System.out.println("calculateComplexInDegreeE");


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        Actor resultActor = test.getActor("User","E");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest InDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(0.0,((Double)value.get(0)).doubleValue(),0.001);


    }


    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateBasicOutDegreeA() {


        System.out.println("calculateBasicOutDegreeA");


        





        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.execute(test);


        


        Actor resultActor = test.getActor("User","A");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Knows OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(3.0,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateBasicOutDegreeB() {


        System.out.println("calculateBasicOutDegreeB");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.execute(test);


            


        Actor resultActor = test.getActor("User","B");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Knows OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(3.0,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateBasicOutDegreeC() {


        System.out.println("calculateBasicOutDegreeC");


        





        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.execute(test);


        


        Actor resultActor = test.getActor("User","C");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Knows OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(2.0,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateBasicOutDegreeD() {


        System.out.println("calculateBasicOutDegreeD");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.execute(test);


                


        Actor resultActor = test.getActor("User","D");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Knows OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(1.0,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateBasicOutDegreeE() {


        System.out.println("calculateBasicOutDegreeE");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


      instance.execute(test);


        


        


        Actor resultActor = test.getActor("User","E");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Knows OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(0.0,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateComplexOutDegreeA() {


        System.out.println("calculateComplexOutDegreeA");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


       instance.execute(test);


        


        Actor resultActor = test.getActor("User","A");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(1.35,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateComplexOutDegreeB() {


        System.out.println("calculateComplexOutDegreeB");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        


        Actor resultActor = test.getActor("User","B");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(-0.3935,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateComplexOutDegreeC() {


        System.out.println("calculateComplexOutDegreeC");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        


        Actor resultActor = test.getActor("User","C");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(-1.7435,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateComplexOutDegreeD() {


        System.out.println("calculateComplexOutDegreeD");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);


        


        Actor resultActor = test.getActor("User","D");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(-1.15,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateOutDegree method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateComplexOutDegreeE() {


        System.out.println("calculateComplexOutDegreeE");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


       instance.execute(test);


        


        Actor resultActor = test.getActor("User","E");


        assertNotNull(resultActor);


        


        Property inDegree = resultActor.getProperty("Interest OutDegree");


        assertNotNull(inDegree);


        List<Object> value = inDegree.getValue();


        assertNotNull(value);


        assertEquals(1,value.size());


        assertEquals(0.0,((Double)value.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateAbsoluteDensity method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateBasicAbsoluteDensity() {


        System.out.println("calculateAbsoluteDensity");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.execute(test);





        Property prop = test.getProperty("Knows Absolute Density");


        assertNotNull(prop);


        List<Object> values = prop.getValue();


        assertNotNull(values);


        assertEquals(1,values.size());


        assertEquals(0.45,((Double)values.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateAbsoluteDensity method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateComplexAbsoluteDensity() {


        System.out.println("calculateComplexAbsoluteDensity");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.execute(test);





        Property prop = test.getProperty("Interest Absolute Density");


        assertNotNull(prop);


        List<Object> values = prop.getValue();


        assertNotNull(values);


        assertEquals(1,values.size());


        assertEquals(0.75185,((Double)values.get(0)).doubleValue(),0.001);


    }





    /**


     * Test of calculateDensity method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateBasicDensity() {


        System.out.println("calculateBasicDensity");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Knows");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);





        Property prop = test.getProperty("Knows Density");


        assertNotNull(prop);


        List<Object> values = prop.getValue();


        assertNotNull(values);


        assertEquals(1,values.size());


        assertEquals(0.45,((Double)values.get(0)).doubleValue(),0.001);


    }


    


    /**


     * Test of calculateDensity method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddDegreeGraphProperties.


     */


    public void testCalculateComplexDensity() {


        System.out.println("calculateComplexDensity");


        


        DegreeGraphProperties instance = new DegreeGraphProperties();


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("relation","Interest");


        props.set("actorType","User");


        instance.init(props);


        instance.execute(test);





        Property prop = test.getProperty("Interest Density");


        assertNotNull(prop);


        List<Object> values = prop.getValue();


        assertNotNull(values);


        assertEquals(1,values.size());


        assertEquals(-0.09685,((Double)values.get(0)).doubleValue(),0.001);


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


