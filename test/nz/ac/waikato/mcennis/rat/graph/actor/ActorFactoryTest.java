/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.actor;

import junit.framework.TestCase;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;

/**
 *
 * @author mcennis
 */
public class ActorFactoryTest extends TestCase {
    
    public ActorFactoryTest(String testName) {
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
     * Test of newInstance method, of class ActorFactory.
     */
    public void testNewInstance() {
        System.out.println("newInstance");
        ActorFactory expResult = ActorFactory.newInstance();
        ActorFactory result = ActorFactory.newInstance();
        assertNotNull(result);
        assertSame(expResult, result);
    }

    /**
     * Test of create method, of class ActorFactory.
     */
    public void testCreate_String_String() {
        System.out.println("create");
        String mode = "Actor";
        String id = "ID";
        ActorFactory instance = ActorFactory.newInstance();
        Actor result = instance.create(mode, id);
        assertNotNull(result);
        assertEquals(mode, result.getMode());
        assertEquals(id,result.getID());
    }

    /**
     * Test of create method, of class ActorFactory.
     */
    public void testPartCreate_3args() {
        System.out.println("create");
        String mode = "Actor";
        String id = "ID";
        Properties parameters = null;
        ActorFactory instance = ActorFactory.newInstance();
        Actor result = instance.create(mode, id, parameters);
        assertNotNull(result);
        assertEquals(mode, result.getMode());
        assertEquals(id,result.getID());
    }

    /**
     * Test of create method, of class ActorFactory.
     */
    public void testPropertiesCreate_3args() {
        System.out.println("create");
        String mode = null;
        String id = null;
        Properties parameters = PropertiesFactory.newInstance().create();
        parameters.set("ActorMode", "Mode");
        parameters.set("ActorID","ID");
        ActorFactory instance = ActorFactory.newInstance();
        Actor result = instance.create(mode, id, parameters);
        assertNotNull(result);
        assertEquals("Mode", result.getMode());
        assertEquals("ID",result.getID());
    }

    /**
     * Test of create method, of class ActorFactory.
     */
    public void testBothCreate_3args() {
        System.out.println("create");
        String mode = "Mode";
        String id = "ID";
        Properties parameters = PropertiesFactory.newInstance().create();
        parameters.set("ActorMode", "BadMode");
        parameters.set("ActorID","BadID");
        ActorFactory instance = ActorFactory.newInstance();
        Actor result = instance.create(mode, id, parameters);
        assertNotNull(result);
        assertEquals(mode, result.getMode());
        assertEquals(id,result.getID());
    }

    /**
     * Test of create method, of class ActorFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Properties parameters = PropertiesFactory.newInstance().create();
        parameters.set("ActorMode", "Mode");
        parameters.set("ActorID","ID");
        ActorFactory instance = ActorFactory.newInstance();
        Actor expResult = null;
        Actor result = instance.create(parameters);
        assertNotNull(result);
        assertEquals("Mode", result.getMode());
        assertEquals("ID",result.getID());
    }

    /**
     * Test of getClassParameter method, of class ActorFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        ActorFactory instance = ActorFactory.newInstance();
        Parameter result = instance.getClassParameter();
        assertNotNull(result);
        assertEquals("ActorClass",result.getType());
    }

}
