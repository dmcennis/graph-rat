/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mcennis.graphrat.graph;

import junit.framework.TestCase;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
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
        String id = "NullGraph";
        GraphFactory instance = new GraphFactory();
        Graph result = instance.create(id);
        assertNotNull(result);
        assertEquals(MemGraph.class,result.getClass());
        assertEquals("NullGraph",result.getID());
    }

    /**
     * Test of create method, of class GraphFactory.
     */
    public void testCreate_String_Properties() {
        System.out.println("create");
        String id = "Null";
        Properties parameters = PropertiesFactory.newInstance().create();
        GraphFactory instance = new GraphFactory();
        parameters.add(instance.getClassParameter().getType(),"UserList");
        Graph result = instance.create(id, parameters);
        assertNotNull( result);
        assertEquals("Null",result.getID());
        assertEquals(UserList.class,result.getClass());
    }

    /**
     * Test of create method, of class GraphFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Properties props = PropertiesFactory.newInstance().create();
        props.add("GraphID","Name");
        GraphFactory instance = new GraphFactory();
        props.add(instance.getClassParameter().getType(),"UserList");
        Graph result = instance.create(props);
        assertEquals("Name", result.getID());
        assertEquals(UserList.class,result.getClass());
    }

    /**
     * Test of getKnownGraphs method, of class GraphFactory.
     */
    public void testGetKnownGraphs() {
        System.out.println("getKnownGraphs");
        GraphFactory instance = new GraphFactory();
        String[] result = instance.getKnownGraphs();
        assertNotNull(result);
    }

    /**
     * Test of getClassParameter method, of class GraphFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        GraphFactory instance = new GraphFactory();
        Parameter result = instance.getClassParameter();
        assertEquals("GraphClass", result.getType());
    }

}
