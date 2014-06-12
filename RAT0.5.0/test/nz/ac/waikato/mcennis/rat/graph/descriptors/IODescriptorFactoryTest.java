/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.descriptors;

import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.descriptors.IODescriptor.Type;
import nz.ac.waikato.mcennis.rat.graph.query.Query;
import nz.ac.waikato.mcennis.rat.graph.query.actor.NullActorQuery;
import nz.ac.waikato.mcennis.rat.graph.query.link.NullLinkQuery;

/**
 *
 * @author mcennis
 */
public class IODescriptorFactoryTest extends TestCase {
    
    public IODescriptorFactoryTest(String testName) {
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
     * Test of newInstance method, of class IODescriptorFactory.
     */
    public void testNewInstance() {
        System.out.println("newInstance");
        IODescriptorFactory expResult = null;
        IODescriptorFactory result = IODescriptorFactory.newInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testCreate_6args() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        IODescriptorInternal result = instance.create(type, algorithm, relation, query, property, description);
        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testCreate_7args_1() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        boolean appendGraphID = true;
        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        IODescriptorInternal result = instance.create(type, algorithm, relation, query, property, description, appendGraphID);
        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
        assertTrue(result.appendGraphID());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testPropertiesCreate_7args_2() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";

        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        PropertiesInternal props = PropertiesFactory.newInstance().create();
        props.merge(instance.getParameter());
        props.set("Type", type);
        props.set("AlgorithmName", algorithm);
        props.set("Relation", relation);
        props.set("Property", property);
        props.set("Description",description);
        props.set("Query", query);
        props.set("AppendGraphID", true);
        IODescriptorInternal result = instance.create(null, null, null, null, null, null, props);

        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
        assertTrue(result.appendGraphID());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testPartCreate_7args_2() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        boolean appendGraphID = true;
        Properties props = null;
        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        IODescriptorInternal result = instance.create(type, algorithm, relation, query, property, description, props);

        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
        assertTrue(result.appendGraphID());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testBothCreate_7args_2() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        boolean appendGraphID = true;
        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        PropertiesInternal props = PropertiesFactory.newInstance().create();
        props.merge(instance.getParameter());
        props.set("Type", Type.ACTOR);
        props.set("AlgorithmName", "");
        props.set("Relation", "");
        props.set("Property", "");
        props.set("Description","");
        props.set("Query", new NullActorQuery());
        IODescriptorInternal result = instance.create(type, algorithm, relation, query, property, description, props);

        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testBothCreate_8args() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        boolean appendGraphID = true;

        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        PropertiesInternal props = PropertiesFactory.newInstance().create();
        props.merge(instance.getParameter());
        props.set("Type", Type.GRAPH);
        props.set("AlgorithmName", "a");
        props.set("Relation", "a");
        props.set("Property", "a");
        props.set("Description","a");
        props.set("Query", new NullActorQuery());
        props.set("AppendGraphID", false);
        IODescriptorInternal result = instance.create(type, algorithm, relation, query, property, description, appendGraphID, props);

        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
        assertTrue(result.appendGraphID());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testPartCreate_8args() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        boolean appendGraphID = true;

        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        PropertiesInternal props = null;
        IODescriptorInternal result = instance.create(type, algorithm, relation, query, property, description, appendGraphID, props);

        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
        assertTrue(result.appendGraphID());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testPropertiesCreate_8args() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        boolean appendGraphID = true;

        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        PropertiesInternal props = PropertiesFactory.newInstance().create();
        props.merge(instance.getParameter());
        props.set("Type", type);
        props.set("AlgorithmName", algorithm);
        props.set("Relation", relation);
        props.set("Property", property);
        props.set("Description",description);
        props.set("Query", query);
        props.set("AppendGraphID", true);
        IODescriptorInternal result = instance.create(null, null, null, null, null, null, true, props);

        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
        assertTrue(result.appendGraphID());
    }

    /**
     * Test of create method, of class IODescriptorFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Type type = Type.ACTOR_PROPERTY;
        String algorithm = "Algorithm";
        String relation = "Relation";
        Query query = new NullLinkQuery();
        String property = "Property";
        String description = "Description";
        boolean appendGraphID = true;

        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        PropertiesInternal props = PropertiesFactory.newInstance().create();
        props.merge(instance.getParameter());
        props.set("Type", type);
        props.set("AlgorithmName", algorithm);
        props.set("Relation", relation);
        props.set("Property", property);
        props.set("Description",description);
        props.set("Query", query);
        props.set("AppendGraphID", true);
        IODescriptorInternal result = instance.create(props);

        assertEquals(type,result.getClassType());
        assertEquals(algorithm,result.getAlgorithmName());
        assertEquals(relation,result.getRelation());
        assertSame(query,result.getQuery());
        assertEquals(property,result.getProperty());
        assertEquals(description,result.getDescription());
        assertTrue(result.appendGraphID());
    }

    public void testCreateNull_Properties(){
        IODescriptorFactory.newInstance().create(null);
    }

    /**
     * Test of getClassParameter method, of class IODescriptorFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        IODescriptorFactory instance = IODescriptorFactory.newInstance();
        Parameter result = instance.getClassParameter();
        assertEquals("IODescriptorClass", result.getType());
    }

}
