/*


 * BasicPathTest.java


 * JUnit based test


 *


 * Created on 2 July 2007, 15:23


 *


 * Copyright Daniel McEnnis, all rights reserved


 */





package nz.ac.waikato.mcennis.rat.graph.path;





import junit.framework.*;


import nz.ac.waikato.mcennis.rat.graph.actor.Actor;


import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import org.dynamicfactory.descriptors.PropertiesFactory;





/**


 *


 * @author Daniel McEnnis


 */


public class BasicPathTest extends TestCase {


    


    Path base;


    


    Actor[] test;


    


    Actor a;


    


    Actor b;


    


    Actor c;


    


    Actor d;


    


    double cost;


    


    public BasicPathTest(String testName) {


        super(testName);


    }





    protected void setUp() throws Exception {


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("ActorType","User");


        props.set("ActorID","A");


        a = ActorFactory.newInstance().create(props);


        props.set("ActorID","B");


        b = ActorFactory.newInstance().create(props);


        props.set("ActorID","C");


        c = ActorFactory.newInstance().create(props);


        props.set("ActorID","D");


        d = ActorFactory.newInstance().create(props);


        test= new Actor[]{a,b,c};


    }





    protected void tearDown() throws Exception {


    }





    /**


     * Test of setPath method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testSetPath() {


        System.out.println("setPath");


        


        


        double cost = 2.0;


        BasicPath instance = new BasicPath();


        


        instance.setPath(test, cost,"Trivial");


        


    }


    


    /**


     * Test of setWeak method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testSetWeak() throws Exception{


        System.out.println("setWeak");


        


        boolean weak = true;


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


        


        instance.setWeak(weak);


        assertEquals(true,instance.isWeak());


    }





    /**


     * Test of getPath method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testGetPath() throws Exception {


        System.out.println("getPath");


        


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


        


        Actor[] expResult = test;


        Actor[] result = instance.getPath();





        Actor[] ret = instance.getPath();


        assertEquals(3,ret.length);


        assertEquals(test[0],ret[0]);


        assertEquals(test[1],ret[1]);


        assertEquals(test[2],ret[2]);


}


    


    public void testGetPathBeforeSet(){


        BasicPath instance = new BasicPath();


        try {


            


            instance.getPath();


            fail("Not Constructed Exception not thrown");


        } catch (NotConstructedError ex) {


           


        }


    }





    /**


     * Test of getStart method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testGetStart() throws Exception {


        System.out.println("getStart");


        


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


        


        Actor expResult = test[0];


        instance.setPath(test,2.0,"Trivial");


        Actor result = instance.getStart();


        assertEquals(expResult, result);


    }





    public void testGetStartBeforeSet(){


                BasicPath instance = new BasicPath();


        try {


            


            instance.getStart();


            fail("Not Constructed Exception not thrown");


        } catch (NotConstructedError ex) {


           


        }





    }





    /**


     * Test of getEnd method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testGetEnd() throws Exception {


        System.out.println("getEnd");


        


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


       


        Actor expResult = test[2];


        instance.setPath(test,2.0,"Trivial");


        Actor result = instance.getEnd();


        assertEquals(expResult, result);


    }


    


    public void testGetEndBeforeTest(){


        BasicPath instance = new BasicPath();


        try {


            


            instance.getEnd();


            fail("Not Constructed Exception not thrown");


        } catch (NotConstructedError ex) {


           


        }





    }





    /**


     * Test of getMiddle method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testGetMiddle() throws Exception {


        System.out.println("getMiddle");


        


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


        


        Actor[] expResult = new Actor[]{b};


        instance.setPath(test,2.0,"Trivial");


        Actor[] result = instance.getMiddle();


        assertEquals(expResult.length, result.length);


        assertEquals(expResult[0],result[0]);


    }


    


    public void testGetMiddleBeforeSet(){


                BasicPath instance = new BasicPath();


        try {


            


            instance.getMiddle();


            fail("Not Constructed Exception not thrown");


        } catch (NotConstructedError ex) {


           


        }





    }








    public void testGetCostBeforeSet(){


                BasicPath instance = new BasicPath();


        try {


            


            instance.getCost();


            fail("Not Constructed Exception not thrown");


        } catch (NotConstructedError ex) {


           


        }





    }


    /**


     * Test of isWeak method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testIsWeak() throws Exception {


        System.out.println("isWeak");


        


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


        


        boolean expResult = false;


        boolean result = instance.isWeak();


        assertEquals(expResult, result);


        


    }





    /**


     * Test of contains method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPath.


     */


    public void testContains() throws Exception {


        System.out.println("contains");


        


        Actor node = b;


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


        


        boolean expResult = true;


        boolean result = instance.contains(node);


        assertEquals(expResult, result);


        


    }


    


    public void testNotContains() throws Exception{


        System.out.println("not contains");


        


        org.dynamicfactory.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("ActorType","User");


        props.set("ActorID","D");


        Actor node = ActorFactory.newInstance().create(props);


        BasicPath instance = new BasicPath();


        instance.setPath(test,cost,"Trivial");


        


        boolean expResult = false;


        boolean result = instance.contains(node);


        assertEquals(expResult, result);


        


    }


    


    public void testContainsBeforeSet(){


                BasicPath instance = new BasicPath();


        try {


            


            instance.contains(a);


            fail("Not Constructed Exception not thrown");


        } catch (NotConstructedError ex) {


           


        }





    }


    


    public void testAddActor(){


        BasicPath instance = new BasicPath();


        instance.setPath(test,2.0,"Trivial");


        Path next = instance.addActor(d,1.0);


        assertEquals(3.0,next.getCost(),0.001);


        assertEquals(4,next.getPath().length);


        assertEquals("A",next.getPath()[0].getID());


        assertEquals("B",next.getPath()[1].getID());


        assertEquals("C",next.getPath()[2].getID());


        assertEquals("D",next.getPath()[3].getID());


    }


    


}


