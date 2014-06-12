/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat;

import java.util.Collection;
import java.util.Iterator;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.actor.BasicUser;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.ParameterInternal;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;

/**
 *
 * @author mcennis
 */
public class AbstractFactoryTest extends TestCase {
    
    public AbstractFactoryTest(String testName) {
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
     * Test of setDefaultProperty method, of class ActorFactory.
     */
    public void testSetMissingDefaultProperty() {
        System.out.println("setDefaultProperty");
        ParameterInternal value = ParameterFactory.newInstance().create("Type",String.class);
        ActorFactory instance = ActorFactory.newInstance();
        instance.setDefaultProperty(value);
        Parameter result = instance.getParameter(value.getType());
        assertNotNull(result);
        assertNotSame(value,result);
    }

    /**
     * Test of setDefaultProperty method, of class ActorFactory.
     */
    public void testSetPresentDefaultProperty() {
        System.out.println("setDefaultProperty");
        ParameterInternal value = ParameterFactory.newInstance().create("Type",String.class);
        value.add("Value");
        ParameterInternal value1 = ParameterFactory.newInstance().create("Type",String.class);
        ActorFactory instance = ActorFactory.newInstance();
        instance.setDefaultProperty(value);
        instance.setDefaultProperty(value1);
        Parameter result = instance.getParameter(value.getType());
        assertNotNull(result);
        assertNotSame(value1,result);
        assertEquals(0,result.getValue().size());
    }

    /**
     * Test of setDefaultProperty method, of class ActorFactory.
     */
    public void testSetInternalDefaultProperty() {
        System.out.println("setDefaultProperty");
        ParameterInternal value = ParameterFactory.newInstance().create("ActorMode",String.class);
        ActorFactory instance = ActorFactory.newInstance();
        instance.setDefaultProperty(value);
        Parameter result = instance.getParameter(value.getType());
        assertNotNull(result);
        assertNotSame(value,result);
        assertEquals(1,result.getValue().size());
        assertEquals("User",result.get());
    }

    /**
     * Test of addDefaultProperty method, of class ActorFactory.
     */
    public void testAddDefaultProperty() throws Exception {
        System.out.println("addDefaultProperty");
        String type = "Type";
        Object value = "Value";
        ActorFactory instance = ActorFactory.newInstance();
        instance.addDefaultProperty(type, value);
        Parameter result = instance.getParameter("Type");
        assertNotNull(result);
        assertEquals("Type",result.getType());
        assertEquals(1,result.getValue().size());
        assertEquals("Value",result.get());
    }

    /**
     * Test of getParameter method, of class ActorFactory.
     */
    public void testGetDefaultParameter_0args() {
        System.out.println("getParameter");
        ActorFactory instance = ActorFactory.newInstance();
        Properties result = instance.getParameter();
        assertNotNull(result);
        assertEquals(5,result.get().size());
    }

    /**
     * Test of getParameter method, of class ActorFactory.
     */
    public void testGetParameter_0args() {
        System.out.println("getParameter");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        ActorFactory instance = ActorFactory.newInstance();
        instance.setDefaultProperty(param);
        Properties result = instance.getParameter();
        assertEquals(6,result.get().size());
        assertNotNull(result.get("Type"));
    }

    /**
     * Test of getParameter method, of class ActorFactory.
     */
    public void testGetDefaultParameter_String() {
        System.out.println("getParameter");
        String type = "ActorMode";
        ActorFactory instance = ActorFactory.newInstance();
        Parameter result = instance.getParameter(type);
        assertNotNull(result);
    }

    /**
     * Test of getParameter method, of class ActorFactory.
     */
    public void testGetAddedParameter_String() {
        System.out.println("getParameter");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        String type = "Type";
        ActorFactory instance = ActorFactory.newInstance();
        Parameter expResult = null;
        Parameter result = instance.getParameter(type);
        assertNotNull(result);
    }

    /**
     * FIXME: can not set a class property
     * Test of addType method, of class ActorFactory.
     */
    public void testAddType() {
        System.out.println("addType");
        String type = "NewName";
        Actor prototype = new BasicUser("Default");
        ActorFactory instance = ActorFactory.newInstance();
        instance.addType(type, prototype);
        Parameter param = instance.getClassParameter();
        param.set("NewName");
        instance.setDefaultProperty((ParameterInternal)param);
        Actor id = instance.create("A", "B");
        assertNotNull(id);
        assertTrue(id instanceof BasicUser);
    }

    /**
     * Test of check method, of class ActorFactory.
     */
    public void testGoodCheck_Properties() {
        System.out.println("check");
        ActorFactory instance = ActorFactory.newInstance();
        Properties props = instance.getParameter();
        props.set("ActorID", "ID");
        boolean expResult = true;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class ActorFactory.
     */
    public void testBadCheck_Properties() {
        System.out.println("check");
        ActorFactory instance = ActorFactory.newInstance();
        Properties props = instance.getParameter();
        boolean expResult = false;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class ActorFactory.
     */
    public void testGoodCheck_Parameter() {
        System.out.println("check");
        ActorFactory instance = ActorFactory.newInstance();
        Parameter parameter = instance.getParameter("ActorID");
        parameter.add("ID");
        boolean expResult = true;
        boolean result = instance.check(parameter);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class ActorFactory.
     */
    public void testBadCheck_Parameter() {
        System.out.println("check");
        ActorFactory instance = ActorFactory.newInstance();
        Parameter parameter = instance.getParameter("ActorID");
        boolean expResult = false;
        boolean result = instance.check(parameter);
        assertEquals(expResult, result);
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
        assertEquals(1,result.getValue().size());
        assertEquals("BasicUser",result.get());
    }

    /**
     * Test of getKnownTypes method, of class ActorFactory.
     */
    public void testGetKnownTypes() {
        System.out.println("getKnownTypes");
        ActorFactory instance = ActorFactory.newInstance();
        Collection<String> result = instance.getKnownTypes();
        assertNotNull(result);
        assertEquals(2,result.size());
        Iterator<String> it = result.iterator();
        while(it.hasNext()){
            String test = it.next();
            if("BasicUser".equals(test)){

            } else if("DBUser".equals(test)){

            } else {
                fail("Unexpected string '"+test+"' encountered");
            }
        }
    }
}
