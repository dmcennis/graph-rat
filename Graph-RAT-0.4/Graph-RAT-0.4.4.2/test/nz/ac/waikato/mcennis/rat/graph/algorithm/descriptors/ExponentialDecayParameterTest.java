/*

 * ExponentialDecayParameterTest.java

 * JUnit based test

 *

 * Created on 15 September 2007, 12:44

 *

 * Copyright Daniel McEnnis, all rights reserved

 */



package org.mcennis.graphrat.algorithm.descriptors;



import junit.framework.*;
import org.dynamicfactory.descriptors.ExponentialDecayParameter;



/**

 *

 * @author Daniel McEnnis

 */

public class ExponentialDecayParameterTest extends TestCase {

    

    public ExponentialDecayParameterTest(String testName) {

        super(testName);

    }



    protected void setUp() throws Exception {

    }



    protected void tearDown() throws Exception {

    }



    /**

     * Test of setValue method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.ExponentialDecayParameter.

     */

    public void testSetValue() {

        System.out.println("setValue");

        

        Object o = null;

        ExponentialDecayParameter instance = new ExponentialDecayParameter();

        

        instance.setValue(o);

        

        // TODO review the generated test code and remove the default call to fail.

        fail("The test case is a prototype.");

    }

    

}

