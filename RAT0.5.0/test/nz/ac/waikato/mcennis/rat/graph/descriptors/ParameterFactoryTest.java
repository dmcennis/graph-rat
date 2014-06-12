/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.descriptors;

import junit.framework.TestCase;

/**
 *
 * @author mcennis
 */
public class ParameterFactoryTest extends TestCase {
    
    public ParameterFactoryTest(String testName) {
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
     * Test of newInstance method, of class ParameterFactory.
     */
    public void testNewInstance() {
        System.out.println("newInstance");
        ParameterFactory expResult = ParameterFactory.newInstance();
        ParameterFactory result = ParameterFactory.newInstance();
        assertNotNull(result);
        assertSame(expResult, result);
    }

    /**
     * Test of create method, of class ParameterFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Properties props = PropertiesFactory.newInstance().create();
        props.set("ParameterClass", "BasicParameter");
        props.set("Name","Type");
        props.set("Class", Double.class);
        props.set("Description","Description");
        ParameterFactory instance = ParameterFactory.newInstance();
        ParameterInternal result = instance.create(props);
        assertNotNull(result);
        assertTrue(result instanceof BasicParameter);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getParameterClass());
        assertEquals("Description",result.getDescription());
        assertEquals(0,result.getValue().size());
    }

    /**
     * Test of create method, of class ParameterFactory.
     */
    public void testCreate_String_Class() {
        System.out.println("create");
        String type = "Type";
        Class classType = Double.class;
        ParameterFactory instance = ParameterFactory.newInstance();
        ParameterInternal result = instance.create(type, classType);
        assertNotNull(result);
        assertTrue(result instanceof BasicParameter);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getParameterClass());
        assertEquals("",result.getDescription());
        assertEquals(0,result.getValue().size());
    }

    /**
     * Test of create method, of class ParameterFactory.
     */
    public void testCreate_3args() {
        System.out.println("create");
        String type = "Type";
        Class classType = Double.class;
        String description = "Description";
        ParameterFactory instance = ParameterFactory.newInstance();
        ParameterInternal result = instance.create(type, classType, description);
        assertNotNull(result);
        assertTrue(result instanceof BasicParameter);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getParameterClass());
        assertEquals("Description",result.getDescription());
        assertEquals(0,result.getValue().size());
    }

    /**
     * Test of create method, of class ParameterFactory.
     */
    public void testPartsCreate_4args() {
        System.out.println("create");
        String type = "Type";
        Class classType = Double.class;
        String description = "Description";
        Properties props = null;
        ParameterFactory instance = ParameterFactory.newInstance();
        ParameterInternal result = instance.create(type, classType, description, props);
        assertNotNull(result);
        assertTrue(result instanceof BasicParameter);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getParameterClass());
        assertEquals("Description",result.getDescription());
        assertEquals(0,result.getValue().size());
    }

    /**
     * Test of create method, of class ParameterFactory.
     */
    public void testPropertiesCreate_4args() {
        System.out.println("create");
        String type = null;
        Class classType = null;
        String description = null;
        Properties props = PropertiesFactory.newInstance().create();
        props.set("ParameterClass", "BasicParameter");
        props.set("Name","Type");
        props.set("Class", Double.class);
        props.set("Description","Description");
        ParameterFactory instance = ParameterFactory.newInstance();
        ParameterInternal result = instance.create(type, classType, description, props);
        assertNotNull(result);
        assertTrue(result instanceof BasicParameter);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getParameterClass());
        assertEquals("Description",result.getDescription());
        assertEquals(0,result.getValue().size());
    }

    /**
     * Test of create method, of class ParameterFactory.
     */
    public void testBothCreate_4args() {
        System.out.println("create");
        String type = "Type";
        Class classType = Double.class;
        String description = "Description";
        Properties props = PropertiesFactory.newInstance().create();
        props.set("ParameterClass", "BasicParameter");
        props.set("Name","T");
        props.set("Class", Long.class);
        props.set("Description","Bogus");
        ParameterFactory instance = ParameterFactory.newInstance();
        ParameterInternal result = instance.create(type, classType, description, props);
        assertNotNull(result);
        assertTrue(result instanceof BasicParameter);
        assertEquals("Type",result.getType());
        assertEquals(Double.class,result.getParameterClass());
        assertEquals("Description",result.getDescription());
        assertEquals(0,result.getValue().size());
    }

    /**
     * Test of getClassParameter method, of class ParameterFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        ParameterFactory instance = null;
        Parameter result = instance.getClassParameter();
        assertNotNull(result);
        assertEquals("ParameterClass",result.getType());
    }

}
