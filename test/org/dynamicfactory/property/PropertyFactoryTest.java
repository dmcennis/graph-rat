/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamicfactory.property;

import junit.framework.TestCase;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;

/**
 *
 * @author mcennis
 */
public class PropertyFactoryTest extends TestCase {
    
    public PropertyFactoryTest(String testName) {
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
     * Test of newInstance method, of class PropertyFactory.
     */
    public void testNewInstance() {
        System.out.println("newInstance");
        PropertyFactory expResult = PropertyFactory.newInstance();
        PropertyFactory result = PropertyFactory.newInstance();
        assertNotNull(result);
        assertSame(expResult,result);
    }

    /**;
     * Test of create method, of class PropertyFactory.
     */
    public void testCreate_String_Class() {
        System.out.println("create");
        String id = "Type";
        Class objectType = Double.class;
        PropertyFactory instance = new PropertyFactory();
        Property result = instance.create(id, objectType);
        assertNotNull(result);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getPropertyClass());
    }

    /**
     * Test of create method, of class PropertyFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Properties props = null;
        PropertyFactory instance = new PropertyFactory();
        Property expResult = null;
        Property result = instance.create(props);
        assertNotNull(result);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getPropertyClass());
    }

    /**
     * Test of create method, of class PropertyFactory.
     */
    public void testPartCreate_3args() {
        System.out.println("create");
        String id = "Type";
        Class objectType = Double.class;
        Properties props = null;
        PropertyFactory instance = new PropertyFactory();
        Property result = instance.create(id, objectType, props);
        assertNotNull(result);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getPropertyClass());
    }

    /**
     * Test of create method, of class PropertyFactory.
     */
    public void testPropertiesCreate_3args() {
        System.out.println("create");
        String id = null;
        Class objectType = null;
        Properties props = PropertiesFactory.newInstance().create();
        props.add("PropertyType", "Type");
        props.add("PropertyValueClass", Double.class);
        PropertyFactory instance = new PropertyFactory();
        Property result = instance.create(id, objectType, props);
        assertNotNull(result);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getPropertyClass());
    }

    /**
     * Test of create method, of class PropertyFactory.
     */
    public void testBothCreate_3args() {
        System.out.println("create");
        String id = "Type";
        Class objectType = Double.class;
        Properties props = PropertiesFactory.newInstance().create();
        props.add("PropertyType", "BadType");
        props.add("PropertyValueClass", Long.class);
        PropertyFactory instance = new PropertyFactory();
        Property result = instance.create(id, objectType, props);
        assertNotNull(result);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getPropertyClass());
    }

    /**
     * Test of getClassParameter method, of class PropertyFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        PropertyFactory instance = new PropertyFactory();
        Parameter result = instance.getClassParameter();
        assertNotNull(result);
        assertEquals("PropertyClass",result.getType());
        assertEquals(String.class,result.getParameterClass());
    }

}
