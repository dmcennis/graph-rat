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
public class PropertiesFactoryTest extends TestCase {
    
    public PropertiesFactoryTest(String testName) {
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
     * Test of newInstance method, of class PropertiesFactory.
     */
    public void testNewInstance() {
        System.out.println("newInstance");
        PropertiesFactory expResult = PropertiesFactory.newInstance();
        PropertiesFactory result = PropertiesFactory.newInstance();
        assertNotNull(result);
        assertSame(expResult, result);
    }

    /**
     * Test of create method, of class PropertiesFactory.
     */
    public void testCreate_0args() {
        System.out.println("create");
        PropertiesFactory instance = PropertiesFactory.newInstance();
        PropertiesInternal result = instance.create();
        assertNotNull(result);
        assertTrue(result instanceof PropertiesInternal);
        assertEquals(0,result.get().size());
    }

    /**
     * Test of create method, of class PropertisFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Properties props = PropertiesFactory.newInstance().create();
        PropertiesFactory instance = PropertiesFactory.newInstance();
        PropertiesInternal result = instance.create(props);
        assertNotNull(result);
        assertTrue(result instanceof PropertiesInternal);
        assertEquals(0,result.get().size());
    }

    /**
     * Test of create method, of class PropertisFactory.
     */
    public void testNullCreate_Properties() {
        System.out.println("create");
        Properties props = PropertiesFactory.newInstance().create();
        PropertiesFactory instance = PropertiesFactory.newInstance();
        PropertiesInternal result = instance.create(props);
        assertNotNull(result);
        assertTrue(result instanceof PropertiesInternal);
        assertEquals(0,result.get().size());
    }

    /**
     * Test of getClassParameter method, of class PropertiesFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        PropertiesFactory instance = PropertiesFactory.newInstance();
        Parameter result = instance.getClassParameter();
        assertNotNull(result);
        assertEquals("PropertiesClass",result.getType());
        assertEquals(0,result.getValue().size());
    }

}
