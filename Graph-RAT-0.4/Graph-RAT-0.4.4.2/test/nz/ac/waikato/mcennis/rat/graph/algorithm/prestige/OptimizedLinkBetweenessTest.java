/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.prestige;

import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.path.PathNode;

/**
 *
 * @author mcennis
 */
public class OptimizedLinkBetweenessTest extends TestCase {
    
    public OptimizedLinkBetweenessTest(String testName) {
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
     * Test of doCleanup method, of class OptimizedLinkBetweeness.
     */
    public void testDoCleanup() {
        System.out.println("doCleanup");
        PathNode[] path = null;
        Graph g = null;
        OptimizedLinkBetweeness instance = new OptimizedLinkBetweeness();
        instance.doCleanup(path, g);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doAnalysis method, of class OptimizedLinkBetweeness.
     */
    public void testDoAnalysis() {
        System.out.println("doAnalysis");
        PathNode[] path = null;
        PathNode source = null;
        OptimizedLinkBetweeness instance = new OptimizedLinkBetweeness();
//        instance.doAnalysis(path, source);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSize method, of class OptimizedLinkBetweeness.
     */
    public void testSetSize() {
        System.out.println("setSize");
        int size = 0;
        OptimizedLinkBetweeness instance = new OptimizedLinkBetweeness();
//        instance.setSize(size);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of constructOutput method, of class OptimizedLinkBetweeness.
     */
    public void testConstructOutput() {
        System.out.println("constructOutput");
        OutputDescriptor[] output = null;
        ParameterInternal[] parameter = null;
        OptimizedLinkBetweeness instance = new OptimizedLinkBetweeness();
//        instance.constructOutput(output, parameter);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
