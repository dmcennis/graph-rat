/*
 * DerbyGraphTest.java
 * JUnit based test
 *
 * Created on 27 July 2007, 17:20
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.*;
import java.sql.SQLException;
import java.util.Iterator;

import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;
import org.mcennis.graphrat.graph.DerbyGraph;
import org.mcennis.graphrat.graph.PostgresqlGraph;
org.dynamicfactory.propertyQuery.Query;

/**
 *
 * @author Daniel McEnnis
 */
public class PostgresqlGraphTest extends TestCase {
    static boolean initialized = false;
    Actor a;
    Actor aDuplicate;
    Actor b;
    Actor c;
    Actor d;
    Actor e;
    Actor art1;
    Actor art3;
    Actor art5;
    Actor art7;
    Actor a2;
    Link ab;
    Link ba;
    Link bc;
    Link cb;
    Link ac;
    Link ca;
    Link ae;
    Link bd;
    Link dc;
    Link a1;
    Link b1;
    Link b3;
    Link c3;
    Link c5;
    Link d7;
    Link abI;
    Link baI;
    Link acI;
    Link caI;
    Link bcI;
    Link cbI;
    Link bdI;
    Link dcI;
    Link aeI;
    Link abM;
    Link baM;
    Link acM;
    Link caM;
    Link bcM;
    Link cbM;
    Link bdM;
    Link dcM;
    Property graphProp;
    Property graphProp2;
    
    
    PostgresqlGraph test;
    
    public PostgresqlGraphTest(String testName) {
        super(testName);
        if(initialized==false){
            test = new PostgresqlGraph();
            test.setID("test");
            test.setUser("work");
            
            try {
                System.out.println("Initializing Database test");
                test.initializeDatabase();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            initialized=true;
        }
    }
    
    protected void setUp() throws Exception {
        try {
            test = new PostgresqlGraph();
            test.setID("test");
            test.setUser("work");
            File db = new File("\\Users\\work\\Documents\\database");
            test.setDirectory(db);
            test.startup();
        } catch (SQLException ex) {
            System.err.println("Can not initialize database - all tests automatically fail");
            ex.printStackTrace();
        }
    }
    
    protected void tearDown() throws Exception {
        test.clear();
        test.close();
    }
    
    protected void initialise() throws SQLException{
        try {
            java.util.Properties props = new java.util.Properties();
            props.setProperty("ActorType", "User");
            props.setProperty("ActorID", "A");
            props.setProperty("PropertyType", "Basic");
            props.setProperty("PropertyID", "interest");
            props.setProperty("LinkType", "Interest");
            props.setProperty("LinkClass", "Basic");
            a = ActorFactory.newInstance().create(props);
            Property ia = PropertyFactory.newInstance().create(props);
            ia.add("1");
            ia.add("2");
            a.add(ia);
            test.add(a);
            props.setProperty("ActorType", "Artist");
            aDuplicate = ActorFactory.newInstance().create(props);
            props.setProperty("ActorType", "User");
            props.setProperty("ActorID", "B");
            b = ActorFactory.newInstance().create(props);
            Property ib = PropertyFactory.newInstance().create(props);
            ib.add("1");
            ib.add("2");
            ib.add("3");
            ib.add("4");
            b.add(ib);
            test.add(b);
            props.setProperty("ActorID", "C");
            c = ActorFactory.newInstance().create(props);
            Property ic = PropertyFactory.newInstance().create(props);
            ic.add("3");
            ic.add("4");
            ic.add("5");
            ic.add("6");
            c.add(ic);
            test.add(c);
            props.setProperty("ActorID", "D");
            d = ActorFactory.newInstance().create(props);
            Property id = PropertyFactory.newInstance().create(props);
            id.add("7");
            id.add("8");
            d.add(id);
            test.add(d);
            props.setProperty("ActorID", "E");
            e = ActorFactory.newInstance().create(props);
            test.add(e);
            props.setProperty("LinkType", "Knows");
            ab = LinkFactory.newInstance().create(props);
            ab.set(a, 1.0, b);
            test.add(ab);
            ba = LinkFactory.newInstance().create(props);
            ba.set(b, 1.0, a);
            test.add(ba);
            cb = LinkFactory.newInstance().create(props);
            cb.set(c, 1.0, b);
            test.add(cb);
            bc = LinkFactory.newInstance().create(props);
            bc.set(b, 1.0, c);
            test.add(bc);
            ca = LinkFactory.newInstance().create(props);
            ca.set(c, 1.0, a);
            test.add(ca);
            ac = LinkFactory.newInstance().create(props);
            ac.set(a, 1.0, c);
            test.add(ac);
            ae = LinkFactory.newInstance().create(props);
            ae.set(a, 1.0, e);
            test.add(ae);
            bd = LinkFactory.newInstance().create(props);
            bd.set(b, 1.0, d);
            test.add(bd);
            dc = LinkFactory.newInstance().create(props);
            dc.set(d, 1.0, c);
            test.add(dc);
            props.setProperty("ActorType", "Artist");
            props.setProperty("ActorID", "1");
            art1 = ActorFactory.newInstance().create(props);
            test.add(art1);
            props.setProperty("ActorID", "3");
            art3 = ActorFactory.newInstance().create(props);
            test.add(art3);
            props.setProperty("ActorID", "5");
            art5 = ActorFactory.newInstance().create(props);
            test.add(art5);
            props.setProperty("ActorID", "7");
            art7 = ActorFactory.newInstance().create(props);
            test.add(art7);
            props.setProperty("LinkType", "Given");
            a1 = LinkFactory.newInstance().create(props);
            a1.set(a, 1.79769E+308, art1);
            test.add(a1);
            b1 = LinkFactory.newInstance().create(props);
            b1.set(b, 1.79769E+308, art1);
            test.add(b1);
            b3 = LinkFactory.newInstance().create(props);
            b3.set(b, 1.79769E+308, art3);
            test.add(b3);
            c3 = LinkFactory.newInstance().create(props);
            c3.set(c, 1.79769E+308, art3);
            test.add(c3);
            c5 = LinkFactory.newInstance().create(props);
            c5.set(c, 1.79769E+308, art5);
            test.add(c5);
            d7 = LinkFactory.newInstance().create(props);
            d7.set(d, 1.79769E+308, art7);
            test.add(d7);
            props.setProperty("LinkType", "Interest");
            abI = LinkFactory.newInstance().create(props);
            abI.set(a, 2.50, b);
            test.add(abI);
            baI = LinkFactory.newInstance().create(props);
            baI.set(b, 1.35, a);
            test.add(baI);
            acI = LinkFactory.newInstance().create(props);
            acI.set(a, -1.15, c);
            test.add(acI);
            caI = LinkFactory.newInstance().create(props);
            caI.set(c, -3.0935, a);
            test.add(caI);
            bcI = LinkFactory.newInstance().create(props);
            bcI.set(b, 1.35, c);
            test.add(bcI);
            cbI = LinkFactory.newInstance().create(props);
            cbI.set(c, 1.35, b);
            test.add(cbI);
            bdI = LinkFactory.newInstance().create(props);
            bdI.set(b, -3.0935, d);
            test.add(bdI);
            dcI = LinkFactory.newInstance().create(props);
            dcI.set(d, -1.15, c);
            test.add(dcI);
            props.setProperty("LinkType", "Music");
            abM = LinkFactory.newInstance().create(props);
            abM.set(a, 2.0, b);
            test.add(abM);
            baM = LinkFactory.newInstance().create(props);
            baM.set(b, 1.75, a);
            test.add(baM);
            acM = LinkFactory.newInstance().create(props);
            acM.set(a, -0.25, c);
            test.add(acM);
            caM = LinkFactory.newInstance().create(props);
            caM.set(c, -0.525, a);
            test.add(caM);
            bcM = LinkFactory.newInstance().create(props);
            bcM.set(b, 1.75, c);
            test.add(bcM);
            cbM = LinkFactory.newInstance().create(props);
            cbM.set(c, 1.75, b);
            test.add(cbM);
            bdM = LinkFactory.newInstance().create(props);
            bdM.set(b, -0.525, d);
            test.add(bdM);
            dcM = LinkFactory.newInstance().create(props);
            dcM.set(d, -0.25, c);
            test.add(dcM);
            props.setProperty("PropertyID", "GraphProp");
            graphProp = PropertyFactory.newInstance().create(props);
            graphProp.add("Value1");
            graphProp.add("Value2");
            props.setProperty("PropertyID", "GraphProp2");
            graphProp2 = PropertyFactory.newInstance().create(props);
            graphProp2.add("Value1");
            graphProp2.add("Value2");
            test.add(graphProp);
            test.add(graphProp2);
        } catch (InvalidObjectTypeException ex) {
            Logger.getLogger(PostgresqlGraphTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Test of isInitialized method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testIsInitializedFalse() {
        System.out.println("isInitializedFalse");
        
        DerbyGraph instance = new DerbyGraph();
        
        boolean expResult = false;
        boolean result = instance.isInitialized();
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of isInitialized method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testIsInitializedTrue() {
        System.out.println("isInitializedTrue");
        
        boolean expResult = true;
        boolean result = test.isInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testAddActor() throws Exception{
        System.out.println("addActor");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","A");
        a = ActorFactory.newInstance().create(props);
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","interest");
        Property ia = PropertyFactory.newInstance().create(props);
        ia.add("1");
        ia.add("2");
        a.add(ia);
        test.add(a);
   }
    
    /**
     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testAddLink() throws Exception{
        System.out.println("addLink");
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","A");
        a = ActorFactory.newInstance().create(props);
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","interest");
        Property ia = PropertyFactory.newInstance().create(props);
        ia.add("1");
        ia.add("2");
        a.add(ia);
        test.add(a);
        props.setProperty("ActorID","B");
        b = ActorFactory.newInstance().create(props);
        Property ib = PropertyFactory.newInstance().create(props);
        ib.add("1");
        ib.add("2");
        ib.add("3");
        ib.add("4");
        b.add(ib);
        test.add(b);
        props.setProperty("LinkType","Knows");
        props.setProperty("LinkClass","Basic");
        ab = LinkFactory.newInstance().create(props);
        ab.set(a,1.0,b);
        test.add(ab);
    }
    /**
     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testAddSameIDActor() throws Exception{
        System.out.println("addSameIDActor");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ActorID","A");
        props.setProperty("ActorType","User");
        a = ActorFactory.newInstance().create(props);
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","interest");
        Property ia = PropertyFactory.newInstance().create(props);
        ia.add("1");
        ia.add("2");
        a.add(ia);
        test.add(a);
        props.setProperty("ActorType","Artist");
        aDuplicate = ActorFactory.newInstance().create(props);
        test.add(aDuplicate);
    }
    /**
     * Test of add method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testAddProperty() throws Exception{
        System.out.println("addProperty");
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","GraphProp");
        
        graphProp = PropertyFactory.newInstance().create(props);
        graphProp.add("Value1");
        graphProp.add("Value2");
        props.setProperty("PropertyID","GraphProp2");
        graphProp2 = PropertyFactory.newInstance().create(props);
        graphProp2.add("Value1");
        graphProp2.add("Value2");
        test.add(graphProp);
        test.add(graphProp2);
    }
    /**
     * Test of remove method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testRemoveActor() throws SQLException{
        System.out.println("removeActor");
        initialise();
        
        test.remove(a);
        assertNull(test.getActor("User","A"));
        assertNull(test.getLinkBySource("Knows",a));
        assertNull(test.getLinkByDestination("Knows",a));
        assertNull(test.getLinkBySource("Interest",a));
        assertNull(test.getLinkByDestination("Interest",a));
        assertNull(test.getLinkBySource("Music",a));
        assertNull(test.getLinkByDestination("Music",a));
        assertNull(test.getLinkBySource("Given",a));
    }
    
    /**
     * Test of remove method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testRemoveLink()  throws SQLException{
        System.out.println("removeLink");
        initialise();
        
        test.remove(abI);
        assertNull(test.getLink("interest",a,b));
    }
    
    /**
     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorSinglePresent()  throws SQLException{
        System.out.println("getActorSinglePresent");
        initialise();
        String type = "User";
        String ID = "A";
        
        Actor expResult = a;
        Actor result = test.getActor(type, ID);
        assertTrue(expResult.equals(result));
        
    }
    
    /**
     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorSingleNotPresent() throws SQLException {
        System.out.println("getActor");
        initialise();
        String type = "User";
        String ID = "NotPresent";
        
        Actor expResult = null;
        Actor result = test.getActor(type, ID);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorTypePresent()  throws SQLException{
        System.out.println("getActor");
        initialise();
        String type = "Artist";
        
        Actor[] result = test.getActor(type);
        assertNotNull(result);
        assertEquals(4, result.length);
        for(int i=0;i<result.length;++i){
            if((!result[i].equals(art1))&&(!result[i].equals(art3))&&(!result[i].equals(art5))&&(!result[i].equals(art7))){
                fail("Unexpected Artist "+result[i].getID()+" of type "+result[i].getType());
            }
        }
        
    }
    
    /**
     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorTypeNotPresent()  throws SQLException{
        System.out.println("getActor");
        initialise();
        String type = "NotPresent";
        
        Actor[] expResult = null;
        Actor[] result = test.getActor(type);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorAllPresent()  throws SQLException{
        System.out.println("getActor");
        initialise();
        String type = "";
        String ID = "";
        
        Actor[] result = test.getActor();
        assertNotNull(result);
        assertEquals(9, result.length);
        for(int i=0;i<result.length;++i){
            if(result[i].equals(a)){
                
            }else if(result[i].equals(b)){
                
            }else if(result[i].equals(c)){
                
            }else if(result[i].equals(d)){
                
            }else if(result[i].equals(e)){
                
            }else if(result[i].equals(art1)){
                
            }else if(result[i].equals(art3)){
                
            }else if(result[i].equals(art5)){
                
            }else if(result[i].equals(art7)){
                
            }else{
                fail("Unexpected Actor "+result[i].getID()+" of type "+result[i].getType());
            }
        }
    }
    
    /**
     * Test of getActor method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorAllNotPresent()  throws SQLException{
        System.out.println("getActor");

        String type = "";
        String ID = "";
        Actor[] expResult = null;
        Actor[] result = test.getActor();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getActorIterator method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorIterator()  throws SQLException{
        System.out.println("getActorIterator");
        initialise();
        String type = "";
        
        Iterator<Actor> result = test.getActorIterator(type);
        assertNotNull(result);
        int count=0;
        Actor actor;
        while(result.hasNext()){
            actor=result.next();
            if(count >= 9){
                fail("Too many entries in iterator");
            }
            if(actor.equals(a)){
                
            }else if(actor.equals(b)){
                
            }else if(actor.equals(c)){
                
            }else if(actor.equals(d)){
                
            }else if(actor.equals(e)){
                
            }else if(actor.equals(art1)){
                
            }else if(actor.equals(art3)){
                
            }else if(actor.equals(art5)){
                
            }else if(actor.equals(art7)){
                
            }else{
                fail("Unexpected Actor "+actor.getID()+" of type "+actor.getType());
            }
            ++count;
        }
        assertEquals(9,count);
    }
    
    /**
     * Test of getActorTypes method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetActorTypes()  throws SQLException{
        System.out.println("getActorTypes");
        initialise();
        String[] result = test.getActorTypes();
        assertNotNull(result);
        assertEquals(2,result.length);
        for(int i=0;i<result.length;++i){
            if((!result[i].equals("User")&&(!result[i].equals("Artist")))){
                fail("Unexpected Type "+result[i]);
            }
        }
    }
    
    /**
     * Test of getLink method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLink() throws SQLException{
        System.out.println("getLink");
        
        initialise();
        
        Link[] result = test.getLink();
        assertNotNull(result);
        assertEquals(31,result.length);
    }
    
    /**
     * Test of getLink method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkNull() throws SQLException{
        System.out.println("getLinkNull");
        
        Link[] expResult = null;
        Link[] result = test.getLink();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getLinkBySource method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkBySource()  throws SQLException{
        System.out.println("getLinkBySource");
        initialise();
        String type = "Given";
        String sourceActor = "A";
        
        Link[] result = test.getLinkBySource(type, a);
        assertNotNull(result);
        assertEquals(1,result.length);
        assertEquals(a1,result[0]);
    }
    
    /**
     * Test of getLinkBySource method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkBySourceBadType()  throws SQLException{
        System.out.println("getLinkBySourceBadType");
        initialise();
        String type = "Bad";
        String sourceActor = "A";
        
        Link[] result = test.getLinkBySource(type, a);
        assertNull(result);
    }
    
    /**
     * Test of getLinkBySource method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkBySourceBadUser()  throws SQLException{
        System.out.println("getLinkBySource");
        initialise();
        String type = "Given";
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","NotPresent");
        Actor sourceActor = ActorFactory.newInstance().create(props);
        
        Link[] result = test.getLinkBySource(type, sourceActor);
        assertNull(result);
        
    }
    
    /**
     * Test of getLinkByDestination method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkByDestination()  throws SQLException{
        System.out.println("getLinkByDestination");
        initialise();
        String type = "Given";
        
        Link[] result = test.getLinkByDestination(type, art7);
        assertNotNull(result);
        assertEquals(d7,result[0]);
    }
    
    /**
     * Test of getLinkByDestination method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkByDestinationBadType()  throws SQLException{
        System.out.println("getLinkByDestination");
        initialise();
        String type = "NotPresent";
        Actor destActor = a;
        
        Link[] expResult = null;
        Link[] result = test.getLinkByDestination(type, destActor);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getLinkByDestination method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkByDestinationBadActor()  throws SQLException{
        System.out.println("getLinkByDestination");
        initialise();
        String type = "Given";
        
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ActorType","Artist");
        props.setProperty("ActorID","NotPresent");
        Actor destActor = ActorFactory.newInstance().create(props);
        
        Link[] result = test.getLinkByDestination(type, destActor);
        assertNull(result);
    }
    
    /**
     * Test of getLinkTypes method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkTypes()  throws SQLException{
        System.out.println("getLinkTypes");
        initialise();
        
        String[] result = test.getLinkTypes();
        assertNotNull(result);
        assertEquals(4,result.length);
        for(int i=0;i<result.length;++i){
            if(result[i].equals("Given")){
                
            }else if(result[i].equals("Music")){
                
            }else if(result[i].equals("Interest")){
                
            }else if(result[i].equals("Knows")){
                
            }else{
                fail("Unexpected link type "+result[i]+" encountered" );
            }
        }
    }
    
    /**
     * Test of getLinkTypes method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetLinkTypesNull() throws SQLException {
        System.out.println("getLinkTypes");
        
        String[] expResult = null;
        String[] result = test.getLinkTypes();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of setSubGraph method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testSetSubGraph() {
        System.out.println("setSubGraph");
        
        Query q = null;
        DerbyGraph instance = new DerbyGraph();
        
        instance.setSubGraph(q);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetProperty()  throws SQLException{
        System.out.println("getProperty");
        initialise();
        
        Property[] result = test.getProperty();
        assertNotNull(result);
        assertEquals(2,result.length);
        for(int i=0;i<result.length;++i){
            if((!result[i].equals(graphProp))&&(!result[i].equals(graphProp2))){
                fail("Unexpected property of type "+result[i].getType()+" encountered");
            }
        }
    }
    
    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetPropertyIsNull() throws SQLException{
        System.out.println("getProperty");
        
        Property[] expResult = null;
        Property[] result = test.getProperty();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetPropertySingle()  throws SQLException{
        System.out.println("getProperty");
        initialise();
        
        Property result = test.getProperty("GraphProp");
        assertNotNull(result);
        assertEquals(graphProp,result);
    }
    
    /**
     * Test of getProperty method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetPropertySingleIsNull() throws SQLException{
        System.out.println("getProperty");
        Property result = test.getProperty("GraphProp");
        assertNull(result);
    }
    
    
    /**
     * Test of getID method, of class nz.ac.waikato.mcennis.arm.graph.DerbyGraph.
     */
    public void testGetID() {
        System.out.println("getID");
        
        String expResult = "test";
        String result = test.getID();
        assertEquals(expResult, result);
    }
    
}
