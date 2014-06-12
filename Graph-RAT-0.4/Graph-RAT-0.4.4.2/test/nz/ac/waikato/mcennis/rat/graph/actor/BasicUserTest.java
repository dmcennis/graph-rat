/*
 * BasicUserTest.java
 * JUnit based test
 *
 * Created on 28 July 2007, 13:00
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.actor;

import junit.framework.*;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.page.Page;
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
     * Test of getPage method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testGetPage() {
        System.out.println("getPage");
        
        BasicUser instance = new BasicUser("A");
        
        Page[] expResult = null;
        Page[] result = instance.getPage();
        assertEquals(expResult, result);
        
        fail("The test case is not finished.");
    }

    /**
     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testAddPage() {
        System.out.println("add");
        
        Page p = null;
        BasicUser instance = new BasicUser("A");
        
//        instance.add(p);
        fail("Test not yet written");
    }

    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testGetPropertyNotNull() throws Exception{
        System.out.println("getProperty");
        
        BasicUser instance = new BasicUser("A");
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","Type1");
        Property prop1 = PropertyFactory.newInstance().create(props);
        prop1.add("Value");
        instance.add(prop1);
        props.setProperty("PropertyID","Type2");
        Property prop2 = PropertyFactory.newInstance().create(props);
        prop2.add("value");
        instance.add(prop2);
        Property[] result = instance.getProperty();
        assertNotNull(result);
        assertEquals(2, result.length);
        for(int i=0;i<result.length;++i){
            if(!result[i].equals(prop1)&&!result[i].equals(prop2)){
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
        
        Property[] expResult = null;
        Property[] result = instance.getProperty();
        assertEquals(expResult, result);
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testRemoveProperty() {
        System.out.println("removeProperty");
        
        String ID = "Prop";
        BasicUser instance = new BasicUser("A");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","Prop");
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
     * Test of setType method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testSetType() {
        System.out.println("setType");
        
        String type = "Artist";
        BasicUser instance = new BasicUser("A");
        
        instance.setType(type);
    }

    /**
     * Test of getType method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testGetType() {
        System.out.println("getType");
        
        BasicUser instance = new BasicUser("A");
        instance.setType("Artist");
        
        String expResult = "Artist";
        String result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToDifferingType() {
        System.out.println("compareTo");
        
        Object o = new BasicUser("1");
        BasicUser instance = new BasicUser("1");
        instance.setType("Artist");
        int expResult = -1;
        int result = instance.compareTo(o);
        assertTrue(result<0);
    }


    /**
     * Test of compareTo method, of class nz.ac.waikato.mcennis.arm.graph.actor.BasicUser.
     */
    public void testCompareToSameTypeDifferingID() {
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
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","Type");
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
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","Type");
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
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","Type");
        
        Property prop1 = PropertyFactory.newInstance().create(props);
        props.setProperty("PropertyID","Type1");
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
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","Type");
        Property prop1 = PropertyFactory.newInstance().create(props);
        props.setProperty("PropertyID","Type1");
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
                java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","Type");
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
    public void testComparePages() {
        System.out.println("comparePages");
        
        Actor right = new BasicUser("A");
        BasicUser instance = new BasicUser("A");
        
        int expResult = 0;
        int result = instance.comparePages(right);
        assertEquals(expResult, result);
        
        // TODO write test.
        fail("The test case is not finished.");
    }

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