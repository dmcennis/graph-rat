/**
 * Created Sep 15, 2008 - 9:11:58 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;

/**
 *
 * @author Daniel McEnnis
 */
public class PropertyToLinkTest extends TestCase {
    
    public PropertyToLinkTest(String testName) {
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
     * Test of execute method, of class PropertyToLink.
     */
    public void testExecute() {
        System.out.println("execute");
        Graph g = null;
        PropertyToLink instance = new PropertyToLink();
        instance.execute(g);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInputType method, of class PropertyToLink.
     */
    public void testGetInputType() {
        System.out.println("getInputType");
        PropertyToLink instance = new PropertyToLink();
        List<IODescriptor> expResult = null;
        List<IODescriptor> result = instance.getInputType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutputType method, of class PropertyToLink.
     */
    public void testGetOutputType() {
        System.out.println("getOutputType");
        PropertyToLink instance = new PropertyToLink();
        List<IODescriptor> expResult = null;
        List<IODescriptor> result = instance.getOutputType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class PropertyToLink.
     */
    public void testGetParameter_0args() {
        System.out.println("getParameter");
        PropertyToLink instance = new PropertyToLink();
        Properties expResult = null;
        Properties result = instance.getParameter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class PropertyToLink.
     */
    public void testGetParameter_String() {
        System.out.println("getParameter");
        String param = "";
        PropertyToLink instance = new PropertyToLink();
        Parameter expResult = null;
        Parameter result = instance.getParameter(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of init method, of class PropertyToLink.
     */
    public void testInit() {
        System.out.println("init");
        Properties map = null;
        PropertyToLink instance = new PropertyToLink();
        instance.init(map);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
