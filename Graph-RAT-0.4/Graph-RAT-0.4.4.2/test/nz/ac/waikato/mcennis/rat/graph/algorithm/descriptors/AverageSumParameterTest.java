/*
 * AverageSumParameterTest.java
 * JUnit based test
 *
 * Created on 15 September 2007, 12:43
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.descriptors;

import junit.framework.*;
import nz.ac.waikato.mcennis.rat.graph.descriptors.AverageSumParameter;

/**
 *
 * @author Daniel McEnnis
 */
public class AverageSumParameterTest extends TestCase {
    
    public AverageSumParameterTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of setValue method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.AverageSumParameter.
     */
    public void testSetValue() {
        System.out.println("setValue");
        
        Object o = null;
        AverageSumParameter instance = new AverageSumParameter();
        
        instance.setValue(o);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
