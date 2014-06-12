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

import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.DerbyGraph;


import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.model.Model;
import nz.ac.waikato.mcennis.rat.graph.property.Property;

import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;



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

     * Test of setMode method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.

     */

    public void testSetMode() {

        System.out.println("setMode");

        

        String Mode = "User";

        DBActor instance = new DBActor();

        

        instance.setMode(Mode);

        

    }



    /**

     * Test of getMode method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.

     */

    public void testGetMode() {

        System.out.println("getMode");

        

        DBActor instance = new DBActor();

        instance.setMode("User");

        String expResult = "User";

        String result = instance.getMode();

        assertEquals(expResult, result);

        

    }



    public void setName(String name) {

    }

    

    public void testSetIDMode(){

        DBActor instance = new DBActor();

        instance.setID("ID");

        instance.setMode("User");

        

        assertTrue(instance.id>0);

    }

    

    public void testSetModeID(){

        DBActor instance = new DBActor();

        instance.setMode("User");

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

        instance.setMode("User");

        

        Actor expResult = instance;

        Actor result = instance.prototype();

        assertTrue(result instanceof DBActor);

    }



    /**

     * Test of getProperty method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.

     */

    public void testGetPropertyNull() {

        System.out.println("getProperty");

        

        DBActor instance = new DBActor();

        instance.setID("ID4");

        instance.setMode("User");

        

        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("PropertyMode","Basic");

        props.set("PropertyID","TestProp");

        Property property = PropertyFactory.newInstance().create(props);

        

        instance.add(property);

        

        List<Property> result = instance.getProperty();

        assertNotNull(result);

        assertEquals(result.size(),1);

        assertTrue(result.get(0).compareTo(property)==0);

        

    }



    /**

     * Test of removeProperty method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.

     */

    public void testRemoveProperty() {

        System.out.println("removeProperty");

        

        String ID = "TestProp2";

        

        DBActor instance = new DBActor();

        instance.setID("ID5");

        instance.setMode("User");

        

        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("PropertyMode","Basic");

        props.set("PropertyID","TestProp2");

        Property property = PropertyFactory.newInstance().create(props);

        instance.add(property);

        

        instance.removeProperty("TestProp2");

        

    }







    /**

     * Test of publishChange method, of class nz.ac.waikato.mcennis.rat.graph.actor.DBActor.

     */

    public void testPublishChange() throws Exception{

        System.out.println("publishChange");

        

        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();

        props.set("PropertyMode","Basic");

        props.set("PropertyID","TestProp");

        Property property = PropertyFactory.newInstance().create(props);



        Model m = property;

        int Mode = Property.ADD_VALUE;

        DBActor instance = new DBActor();

        instance.setID("ID6");

        instance.setMode("User");

        instance.add(property);

        

        property.add("Demo");

        

        Property test = instance.getProperty("TestProp");

        

        assertTrue(property.compareTo(test)==0);

    }

    

}

