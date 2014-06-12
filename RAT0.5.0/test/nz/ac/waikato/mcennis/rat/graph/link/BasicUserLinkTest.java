/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.link;

import java.util.List;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;

/**
 *
 * @author mcennis
 */
public class BasicUserLinkTest extends TestCase {
    
    public BasicUserLinkTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getStrength method, of class BasicUserLink.
     */
    public void testGetStrength() {
        System.out.println("getStrength");
        BasicUserLink instance = new BasicUserLink();
        double expResult = 0.0;
        instance.set(expResult);
        double result = instance.getStrength();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSource method, of class BasicUserLink.
     */
    public void testGetSource() {
        System.out.println("getSource");
        BasicUserLink instance = new BasicUserLink();
        Actor expResult = ActorFactory.newInstance().create("Mode","ID");
        instance.setSource(expResult);
        Actor result = instance.getSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDestination method, of class BasicUserLink.
     */
    public void testGetDestination() {
        System.out.println("getDestination");
        BasicUserLink instance = new BasicUserLink();
        Actor expResult = ActorFactory.newInstance().create("Mode","ID");
        instance.setDestination(expResult);
        Actor result = instance.getDestination();
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class BasicUserLink.
     */
    public void testSet_3args() {
        System.out.println("set");
        Actor l = ActorFactory.newInstance().create("Mode","ID1");
        double strength = 0.0;
        Actor r = ActorFactory.newInstance().create("Mode","ID2");
        BasicUserLink instance = new BasicUserLink();
        instance.set(l, strength, r);
    }

    /**
     * Test of getRelation method, of class BasicUserLink.
     */
    public void testGetRelation() {
        System.out.println("getRelation");
        BasicUserLink instance = new BasicUserLink();
        String expResult = "Relation";
        instance.setRelation(expResult);
        String result = instance.getRelation();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRelation method, of class BasicUserLink.
     */
    public void testSetRelation() {
        System.out.println("setRelation");
        BasicUserLink instance = new BasicUserLink();
        String expResult = "Relation";
        instance.setRelation(expResult);
    }

    /**
     * Test of setSource method, of class BasicUserLink.
     */
    public void testSetSource() {
        System.out.println("setSource");
        Actor u = ActorFactory.newInstance().create("Mode", "ID");
        BasicUserLink instance = new BasicUserLink();
        instance.setSource(u);
    }

    /**
     * Test of setDestination method, of class BasicUserLink.
     */
    public void testSetDestination() {
        System.out.println("setDestination");
        Actor u = ActorFactory.newInstance().create("Mode", "ID");
        BasicUserLink instance = new BasicUserLink();
        instance.setDestination(u);
    }

    /**
     * Test of set method, of class BasicUserLink.
     */
    public void testSet_double() {
        System.out.println("set");
        double str = 10.0;
        BasicUserLink instance = new BasicUserLink();
        instance.set(str);
    }



    /**
     * Test of add method, of class BasicUserLink.
     */
    public void testAdd() {
        System.out.println("add");
        Property prop = PropertyFactory.newInstance().create("Type", String.class);
        BasicUserLink instance = new BasicUserLink();
        instance.add(prop);
    }

        /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testGetPropertyNotNull() throws Exception{
        System.out.println("getProperty");

        BasicUserLink instance = new BasicUserLink();
        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode1");
        Property prop1 = PropertyFactory.newInstance().create(props);
        prop1.add("Value");
        instance.add(prop1);
        props.set("PropertyID","Mode2");
        Property prop2 = PropertyFactory.newInstance().create(props);
        prop2.add("value");
        instance.add(prop2);
        List<Property> result = instance.getProperty();
        assertNotNull(result);
        assertEquals(2, result.size());
        for(int i=0;i<result.size();++i){
            if(!result.get(i).equals(prop1)&&!result.get(i).equals(prop2)){
                fail("Unexpected property encountered");
            }
        }

    }

    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testGetPropertyNull() {
        System.out.println("getProperty");

        BasicUserLink instance = new BasicUserLink();

        List<Property> result = instance.getProperty();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testRemoveProperty() {
        System.out.println("removeProperty");

        String ID = "Prop";
        BasicUserLink instance = new BasicUserLink();

        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Prop");
        Property prop = PropertyFactory.newInstance().create(props);
        instance.add(prop);
        instance.removeProperty(ID);

        assertNull(instance.getProperty("Prop"));
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testRemovePropertyNotPresent() {
        System.out.println("removePropertyNotPresent");

        String ID = "Prop";
        BasicUserLink instance = new BasicUserLink();

        instance.removeProperty(ID);
    }


    /**
     * Test of prototype method, of class BasicUserLink.
     */
    public void testPrototype() {
        System.out.println("prototype");
        BasicUserLink instance = new BasicUserLink();
        BasicUserLink result = instance.prototype();
        assertNotNull(result);
        assertTrue(result instanceof BasicUserLink);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToDifferingMode() {
        System.out.println("compareTo");

        Object o = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();
        instance.setRelation("Artist");
        int expResult = -1;
        int result = instance.compareTo(o);
        assertTrue(result<0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToSameModeDifferingSource() {
        System.out.println("compareTo");

        Actor s = ActorFactory.newInstance().create("A", "A");
        Actor b = ActorFactory.newInstance().create("A", "B");
        Actor d = ActorFactory.newInstance().create("A", "D");
        BasicUserLink o = new BasicUserLink();
        o.set(b,1.0,d);
        BasicUserLink instance = new BasicUserLink();
        instance.set(s,1.0,d);
        int expResult = 1;
        int result = instance.compareTo(o);
        assertTrue(result>0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToSameModeDifferingDestination() {
        System.out.println("compareTo");

        Actor s = ActorFactory.newInstance().create("A", "A");
        Actor b = ActorFactory.newInstance().create("A", "B");
        Actor d = ActorFactory.newInstance().create("A", "D");
        BasicUserLink o = new BasicUserLink();
        o.set(d,1.0,b);
        BasicUserLink instance = new BasicUserLink();
        instance.set(d,1.0,s);

        int expResult = 1;
        int result = instance.compareTo(o);
        assertTrue(result>0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToNullProperties() {
        System.out.println("compareTo");

        Object o = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();

        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToLeftNull() {
        System.out.println("compareTo");

        Object o = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();

        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode");
        Property prop = PropertyFactory.newInstance().create(props);
        ((BasicUserLink)o).add(prop);
        int expResult = -1;
        int result = instance.compareTo(o);
        assertTrue(result<0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToRightNull() {
        System.out.println("compareTo");

        BasicUserLink o = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();

        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode");
        Property prop =PropertyFactory.newInstance().create(props);
        instance.add(prop);

        int expResult = 1;
        int result = instance.compareTo(o);
        assertTrue(result>0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToDifferingContent() {
        System.out.println("compareTo");

        BasicUserLink o = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();
        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode");

        Property prop1 = PropertyFactory.newInstance().create(props);
        props.set("PropertyID","Mode1");
        Property prop2 = PropertyFactory.newInstance().create(props);
        o.add(prop1);
        instance.add(prop2);

        int expResult = 1;
        int result = instance.compareTo(o);
        assertTrue(result>0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToDifferingSize() {
        System.out.println("compareTo");

        BasicUserLink o = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();
        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode");
        Property prop1 = PropertyFactory.newInstance().create(props);
        props.set("PropertyID","Mode1");
        Property prop2 = PropertyFactory.newInstance().create(props);
        o.add(prop1);
        instance.add(prop1);
        instance.add(prop2);

        int expResult = 1;
        int result = instance.compareTo(o);
        assertTrue(result>0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testCompareToEqualsProperties() {
        System.out.println("compareTo");

        BasicUserLink o = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();
                nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode");
        Property prop1 = PropertyFactory.newInstance().create(props);
        o.add(prop1);
        instance.add(prop1);
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
    }
    /**
     * Test of comparePages method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    /**
     * Test of equals method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUserLink.
     */
    public void testEquals() {
        System.out.println("equals");

        Object obj = new BasicUserLink();
        BasicUserLink instance = new BasicUserLink();

        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }

    /**
     * Test of init method, of class BasicUserLink.
     */
    public void testInit() {
        System.out.println("init");
        Properties properties = null;
        BasicUserLink instance = new BasicUserLink();
        instance.init(properties);
    }

}
