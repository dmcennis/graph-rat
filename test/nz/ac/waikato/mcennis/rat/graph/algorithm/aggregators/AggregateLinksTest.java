/**
 * Created Sep 15, 2008 - 9:09:11 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.List;
import org.dynamicfactory.descriptors.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.IODescriptor;
import org.dynamicfactory.descriptors.Parameter;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregateLinksTest extends TestCase {
    
    public AggregateLinksTest(String testName) {
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
     * Test of execute method, of class AggregateLinks.
     */
    public void testExecute() {
        System.out.println("execute");
        Graph g = null;
        AggregateLinks instance = new AggregateLinks();
        instance.execute(g);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of buildInstances method, of class AggregateLinks.
     */
    public void testBuildInstances() {
        System.out.println("buildInstances");
        Graph g = null;
        AggregateLinks instance = new AggregateLinks();
        Instances expResult = null;
        Instances result = instance.buildInstances(g);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInputType method, of class AggregateLinks.
     */
    public void testGetInputType() {
        System.out.println("getInputType");
        AggregateLinks instance = new AggregateLinks();
        List<IODescriptor> expResult = null;
        List<IODescriptor> result = instance.getInputType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutputType method, of class AggregateLinks.
     */
    public void testGetOutputType() {
        System.out.println("getOutputType");
        AggregateLinks instance = new AggregateLinks();
        List<IODescriptor> expResult = null;
        List<IODescriptor> result = instance.getOutputType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class AggregateLinks.
     */
    public void testGetParameter_0args() {
        System.out.println("getParameter");
        AggregateLinks instance = new AggregateLinks();
        Properties expResult = null;
        Properties result = instance.getParameter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class AggregateLinks.
     */
    public void testGetParameter_String() {
        System.out.println("getParameter");
        String param = "";
        AggregateLinks instance = new AggregateLinks();
        Parameter expResult = null;
        Parameter result = instance.getParameter(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of init method, of class AggregateLinks.
     */
    public void testInit() {
        System.out.println("init");
        Properties map = null;
        AggregateLinks instance = new AggregateLinks();
        instance.init(map);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
