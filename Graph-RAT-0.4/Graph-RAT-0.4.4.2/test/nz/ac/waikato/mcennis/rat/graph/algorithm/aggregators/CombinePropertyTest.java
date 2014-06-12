/**
 * Created Sep 15, 2008 - 9:11:33 AM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;

/**
 *
 * @author Daniel McEnnis
 */
public class CombinePropertyTest extends TestCase {
    
    public CombinePropertyTest(String testName) {
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
     * Test of execute method, of class CombineProperty.
     */
    public void testExecute() {
        System.out.println("execute");
        Graph g = null;
        CombineProperty instance = new CombineProperty();
        instance.execute(g);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInputType method, of class CombineProperty.
     */
    public void testGetInputType() {
        System.out.println("getInputType");
        CombineProperty instance = new CombineProperty();
        InputDescriptor[] expResult = null;
        InputDescriptor[] result = instance.getInputType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutputType method, of class CombineProperty.
     */
    public void testGetOutputType() {
        System.out.println("getOutputType");
        CombineProperty instance = new CombineProperty();
        OutputDescriptor[] expResult = null;
        OutputDescriptor[] result = instance.getOutputType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class CombineProperty.
     */
    public void testGetParameter_0args() {
        System.out.println("getParameter");
        CombineProperty instance = new CombineProperty();
        Parameter[] expResult = null;
        Parameter[] result = instance.getParameter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class CombineProperty.
     */
    public void testGetParameter_String() {
        System.out.println("getParameter");
        String param = "";
        CombineProperty instance = new CombineProperty();
        Parameter expResult = null;
        Parameter result = instance.getParameter(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSettableParameter method, of class CombineProperty.
     */
    public void testGetSettableParameter_0args() {
        System.out.println("getSettableParameter");
        CombineProperty instance = new CombineProperty();
        SettableParameter[] expResult = null;
        SettableParameter[] result = instance.getSettableParameter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSettableParameter method, of class CombineProperty.
     */
    public void testGetSettableParameter_String() {
        System.out.println("getSettableParameter");
        String param = "";
        CombineProperty instance = new CombineProperty();
        SettableParameter expResult = null;
        SettableParameter result = instance.getSettableParameter(param);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of init method, of class CombineProperty.
     */
    public void testInit() {
        System.out.println("init");
        Properties map = null;
        CombineProperty instance = new CombineProperty();
        instance.init(map);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
