/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mcennis.graphrat.graph;

import junit.framework.TestCase;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.graph.GraphFactory;

/**
 *
 * @author mcennis
 */
public class GraphFactoryTest extends TestCase {
    
    public GraphFactoryTest(String testName) {
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
     * Test of newInstance method, of class GraphFactory.
     */
    public void testNewInstance() {
        System.out.println("newInstance");
        GraphFactory result = GraphFactory.newInstance();
        assertNotNull(result);
        assertEquals(result,GraphFactory.newInstance());
    }

    /**
     * Test of create method, of class GraphFactory.
     */
    public void testCreate_String() {
        System.out.println("create");
        String id = "";
        GraphFactory instance = new GraphFactory();
        Graph expResult = null;
        Graph result = instance.create(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class GraphFactory.
     */
    public void testCreate_String_Properties() {
        System.out.println("create");
        String id = "";
        Properties parameters = null;
        GraphFactory instance = new GraphFactory();
        Graph expResult = null;
        Graph result = instance.create(id, parameters);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class GraphFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Properties props = null;
        GraphFactory instance = new GraphFactory();
        Graph expResult = null;
        Graph result = instance.create(props);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getKnownGraphs method, of class GraphFactory.
     */
    public void testGetKnownGraphs() {
        System.out.println("getKnownGraphs");
        GraphFactory instance = new GraphFactory();
        String[] expResult = null;
        String[] result = instance.getKnownGraphs();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClassParameter method, of class GraphFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        GraphFactory instance = new GraphFactory();
        Parameter expResult = null;
        Parameter result = instance.getClassParameter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
