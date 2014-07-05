/*


 * BasicParameterTest.java


 * JUnit based test


 *


 * Created on 15 September 2007, 12:44


 *


 * Copyright Daniel McEnnis, all rights reserved


 */





package nz.ac.waikato.mcennis.rat.graph.algorithm.descriptors;





import junit.framework.*;


/**


 *


 * @author Daniel McEnnis


 */


public class BasicParameterTest extends TestCase {


    


    public BasicParameterTest(String testName) {


        super(testName);


    }





    protected void setUp() throws Exception {


    }





    protected void tearDown() throws Exception {


    }





    /**


     * Test of setName method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.BasicParameter.


     */


    public void testSetName() {


        System.out.println("setName");


        


        String name = "";


        BasicParameter instance = new BasicParameter();


        


        instance.setType(name);


        


        // TODO review the generated test code and remove the default call to fail.


        fail("The test case is a prototype.");


    }





    /**


     * Test of getType method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.BasicParameter.


     */


    public void testGetType() {


        System.out.println("getType");


        


        Class c = null;


        BasicParameter instance = new BasicParameter();


        


        instance.getType();


        


        // TODO review the generated test code and remove the default call to fail.


        fail("The test case is a prototype.");


    }





    /**


     * Test of setStructural method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.BasicParameter.


     */


    public void testSetStructural() {


        System.out.println("setStructural");


        


        boolean b = true;


        BasicParameter instance = new BasicParameter();


        


        instance.setStructural(b);


        


        // TODO review the generated test code and remove the default call to fail.


        fail("The test case is a prototype.");


    }





    /**


     * Test of getName method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.BasicParameter.


     */


    public void testGetName() {


        System.out.println("getName");


        


        BasicParameter instance = new BasicParameter();


        


        String expResult = "";


        String result = instance.getType();


        assertEquals(expResult, result);


        


        // TODO review the generated test code and remove the default call to fail.


        fail("The test case is a prototype.");


    }





    /**


     * Test of getValue method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.BasicParameter.


     */


    public void testGetValue() {


        System.out.println("getValue");


        


        BasicParameter instance = new BasicParameter();


        


        Object expResult = null;


        Object result = instance.getValue();


        assertEquals(expResult, result);


        


        // TODO review the generated test code and remove the default call to fail.


        fail("The test case is a prototype.");


    }





    /**


     * Test of isStructural method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.BasicParameter.


     */


    public void testIsStructural() {


        System.out.println("isStructural");


        


        BasicParameter instance = new BasicParameter();


        


        boolean expResult = true;


        boolean result = instance.isStructural();


        assertEquals(expResult, result);


        


        // TODO review the generated test code and remove the default call to fail.


        fail("The test case is a prototype.");


    }





    /**


     * Test of setValue method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.descriptors.BasicParameter.


     */


    public void testSetValue() {


        System.out.println("setValue");


        


        Object o = null;


        BasicParameter instance = new BasicParameter();


        


        instance.set(o);


        


        // TODO review the generated test code and remove the default call to fail.


        fail("The test case is a prototype.");


    }


    


}


