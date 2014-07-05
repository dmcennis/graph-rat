/*
 * DBActorTest.java
 * JUnit based test
 *
 * Created on 8 October 2007, 16:43
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.actor;

import java.io.File;
import junit.framework.*;

import java.sql.SQLException;
import nz.ac.waikato.mcennis.rat.graph.DerbyGraph;
import nz.ac.waikato.mcennis.rat.graph.model.Model;
import nz.ac.waikato.mcennis.rat.graph.page.Page;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class DBActorTest extends TestCase {
    
    DerbyGraph test;
    DBActor actor = null;
    static boolean initialized = false;
    public DBActorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        if(initialized==false){
            test = new DerbyGraph();
            test.setID("test");
            File db = new File("/research/testdatabase");
            test.setDirectory(db);
            
            try {
                System.out.println("Initializing Database "+db.getAbsolutePath()+"/test");
                test.startup();
                test.initializeDatabase();
                test.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            initialized=true;
        DBActor.setDatabase("test");
        DBActor.setDirectory("/research/database/");
        DBActor.init();
        }
    }

    protected void tearDown() throws Exception {
    }


    /**
     * Test of setID method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testSetID() {
        System.out.println("setID");
        
        String id = "ID";
        DBActor instance = new DBActor();
        
        instance.setID(id);
        
    }

    /**
     * Test of getID method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testGetID() {
        System.out.println("getID");
        
        DBActor instance = new DBActor();
        instance.setID("ID");
        String expResult = "ID";
        String result = instance.getID();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of setType method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testSetType() {
        System.out.println("setType");
        
        String type = "User";
        DBActor instance = new DBActor();
        
        instance.setType(type);
        
    }

    /**
     * Test of getType method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testGetType() {
        System.out.println("getType");
        
        DBActor instance = new DBActor();
        instance.setType("User");
        String expResult = "User";
        String result = instance.getType();
        assertEquals(expResult, result);
        
    }

    public void setName(String name) {
    }
    
    public void testSetIDType(){
        DBActor instance = new DBActor();
        instance.setID("ID");
        instance.setType("User");
        
        assertTrue(instance.id>0);
    }
    
    public void testSetTypeID(){
        DBActor instance = new DBActor();
        instance.setType("User");
        instance.setID("ID");
        
        assertTrue(instance.id>0);
    }

    /**
     * Test of duplicate method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testDuplicate() {
        System.out.println("duplicate");
        
        DBActor instance = new DBActor();
        instance.setID("ID3");
        instance.setType("User");
        
        Actor expResult = instance;
        Actor result = instance.duplicate();
        assertTrue(result instanceof DBActor);
        assertTrue(((DBActor)result).id > 0);
        assertTrue(expResult.compareTo(result)==0);
        
    }

    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testGetPropertyNull() {
        System.out.println("getProperty");
        
        DBActor instance = new DBActor();
        instance.setID("ID4");
        instance.setType("User");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","TestProp");
        Property property = PropertyFactory.newInstance().create(props);
        
        instance.add(property);
        
        Property[] result = instance.getProperty();
        assertNotNull(result);
        assertEquals(result.length,1);
        assertTrue(result[0].compareTo(property)==0);
        
    }

    /**
     * Test of removeProperty method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testRemoveProperty() {
        System.out.println("removeProperty");
        
        String ID = "TestProp2";
        
        DBActor instance = new DBActor();
        instance.setID("ID5");
        instance.setType("User");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","TestProp2");
        Property property = PropertyFactory.newInstance().create(props);
        instance.add(property);
        
        instance.removeProperty("TestProp2");
        
    }

    /**
     * Test of getPage method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testGetPage() {
        System.out.println("getPage");
        
        DBActor instance = new DBActor();
        
        Page[] expResult = null;
        Page[] result = instance.getPage();
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }



    /**
     * Test of publishChange method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.
     */
    public void testPublishChange() throws Exception{
        System.out.println("publishChange");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","TestProp");
        Property property = PropertyFactory.newInstance().create(props);

        Model m = property;
        int type = Property.ADD_VALUE;
        DBActor instance = new DBActor();
        instance.setID("ID6");
        instance.setType("User");
        instance.add(property);
        
        property.add("Demo");
        
        Property test = instance.getProperty("TestProp");
        
        assertTrue(property.compareTo(test)==0);
    }
    
}
