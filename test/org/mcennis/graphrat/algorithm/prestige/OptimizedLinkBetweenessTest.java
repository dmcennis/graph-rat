/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */



package org.mcennis.graphrat.algorithm.prestige;



import java.util.List;
import junit.framework.TestCase;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.descriptors.IODescriptor;

import org.mcennis.graphrat.path.PathNode;
import org.dynamicfactory.descriptors.ParameterInternal;


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

        LinkBetweeness instance = new LinkBetweeness();

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

        LinkBetweeness instance = new LinkBetweeness();

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

        LinkBetweeness instance = new LinkBetweeness();

//        instance.setSize(size);

        // TODO review the generated test code and remove the default call to fail.

        fail("The test case is a prototype.");

    }



    /**

     * Test of constructOutput method, of class OptimizedLinkBetweeness.

     */

    public void testConstructOutput() {

        System.out.println("constructOutput");

        List<IODescriptor> output = null;

        ParameterInternal[] parameter = null;

        LinkBetweeness instance = new LinkBetweeness();

//        instance.constructOutput(output, parameter);

        // TODO review the generated test code and remove the default call to fail.

        fail("The test case is a prototype.");

    }



}

