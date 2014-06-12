/*
 * BasicUserTest.java
 * JUnit based test
 *
 * Created on 28 July 2007, 13:00
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.actor;

import java.util.List;
import junit.framework.*;


import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class BasicUserTest extends TestCase {
    
    public BasicUserTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getID method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testGetID() {
        System.out.println("getID");
        
        BasicUser instance = new BasicUser("A");
        
        String expResult = "A";
        String result = instance.getID();
        assertEquals(expResult, result);
        
    }


    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testGetPropertyNotNull() throws Exception{
        System.out.println("getProperty");
        
        BasicUser instance = new BasicUser("A");
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
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testGetPropertyNull() {
        System.out.println("getProperty");
        
        BasicUser instance = new BasicUser("A");
        
        List<Property> result = instance.getProperty();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testRemoveProperty() {
        System.out.println("removeProperty");
        
        String ID = "Prop";
        BasicUser instance = new BasicUser("A");
        
        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Prop");
        Property prop = PropertyFactory.newInstance().create(props);
        instance.add(prop);
        instance.removeProperty(ID);
        
        assertNull(instance.getProperty("Prop"));
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testRemovePropertyNotPresent() {
        System.out.println("removePropertyNotPresent");
        
        String ID = "Prop";
        BasicUser instance = new BasicUser("A");
        
        instance.removeProperty(ID);
    }

    /**
     * Test of setMode method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testSetMode() {
        System.out.println("setMode");
        
        String Mode = "Artist";
        BasicUser instance = new BasicUser("A");
        
        instance.setMode(Mode);
    }

    /**
     * Test of getMode method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testGetMode() {
        System.out.println("getMode");
        
        BasicUser instance = new BasicUser("A");
        instance.setMode("Artist");
        
        String expResult = "Artist";
        String result = instance.getMode();
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToDifferingMode() {
        System.out.println("compareTo");
        
        Object o = new BasicUser("1");
        BasicUser instance = new BasicUser("1");
        instance.setMode("Artist");
        int expResult = -1;
        int result = instance.compareTo(o);
        assertTrue(result<0);
    }


    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToSameModeDifferingID() {
        System.out.println("compareTo");
        
        Object o = new BasicUser("1");
        BasicUser instance = new BasicUser("2");
        
        int expResult = 1;
        int result = instance.compareTo(o);
        assertTrue(result>0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToNullProperties() {
        System.out.println("compareTo");
        
        Object o = new BasicUser("1");
        BasicUser instance = new BasicUser("1");
        
        int expResult = 0;
        int result = instance.compareTo(o);
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToLeftNull() {
        System.out.println("compareTo");
        
        Object o = new BasicUser("A");
        BasicUser instance = new BasicUser("A");
        
        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();
        props.set("PropertyClass",String.class);
        props.set("PropertyID","Mode");
        Property prop = PropertyFactory.newInstance().create(props);
        ((BasicUser)o).add(prop);
        int expResult = -1;
        int result = instance.compareTo(o);
        assertTrue(result<0);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToRightNull() {
        System.out.println("compareTo");
        
        BasicUser o = new BasicUser("A");
        BasicUser instance = new BasicUser("A");
        
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
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToDifferingContent() {
        System.out.println("compareTo");
        
        BasicUser o = new BasicUser("A");
        BasicUser instance = new BasicUser("A");
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
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToDifferingSize() {
        System.out.println("compareTo");
        
        BasicUser o = new BasicUser("A");
        BasicUser instance = new BasicUser("A");
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
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToEqualsProperties() {
        System.out.println("compareTo");
        
        BasicUser o = new BasicUser("A");
        BasicUser instance = new BasicUser("A");
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
     * Test of comparePages method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    /**
     * Test of equals method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testEquals() {
        System.out.println("equals");
        
        Object obj = new BasicUser("A");
        BasicUser instance = new BasicUser("A");
        
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
    }
    
}
