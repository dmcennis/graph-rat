/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.link;

import junit.framework.TestCase;
import org.dynamicfactory.descriptors.Parameter;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

/**
 *
 * @author mcennis
 */
public class LinkFactoryTest extends TestCase {
    
    public LinkFactoryTest(String testName) {
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
     * Test of newInstance method, of class LinkFactory.
     */
    public void testNewInstance() {
        System.out.println("newInstance");
        LinkFactory expResult = LinkFactory.newInstance();
        LinkFactory result = LinkFactory.newInstance();
        assertNotNull(result);
        assertSame(expResult,result);
    }

    /**
     * Test of create method, of class LinkFactory.
     */
    public void testCreate_String() {
        System.out.println("create");
        String relation = "Relation";
        LinkFactory instance = new LinkFactory();
        Link result = instance.create(relation);
        assertNotNull(result);
        assertEquals("Relation",result.getRelation());
    }

    /**
     * Test of create method, of class LinkFactory.
     */
    public void testCreate_Properties() {
        System.out.println("create");
        Properties props = PropertiesFactory.newInstance().create();
        props.set("Relation", "Relation");
        LinkFactory instance = new LinkFactory();
        Link result = instance.create(props);
        assertNotNull(result);
        assertEquals("Relation",result.getRelation());
    }

    /**
     * Test of create method, of class LinkFactory.
     */
    public void testPartsCreate_String_Properties() {
        System.out.println("create");
        String relation = "Relation";
        Properties parameters = null;
        LinkFactory instance = new LinkFactory();
        Link result = instance.create(relation, parameters);
        assertNotNull(result);
        assertEquals("Relation",result.getRelation());
    }

    /**
     * Test of create method, of class LinkFactory.
     */
    public void testPropertiesCreate_String_Properties() {
        System.out.println("create");
        String relation = null;
        Properties props = PropertiesFactory.newInstance().create();
        props.set("Relation", "Relation");
        LinkFactory instance = new LinkFactory();
        Link result = instance.create(relation, props);
        assertNotNull(result);
        assertEquals("Relation",result.getRelation());
    }

    /**
     * Test of create method, of class LinkFactory.
     */
    public void testBothCreate_String_Properties() {
        System.out.println("create");
        String relation = "Relation";
        Properties props = PropertiesFactory.newInstance().create();
        props.set("Relation", "BadRelation");
        LinkFactory instance = new LinkFactory();
        Link result = instance.create(relation, props);
        assertNotNull(result);
        assertEquals("Relation",result.getRelation());
    }

    /**
     * Test of getClassParameter method, of class LinkFactory.
     */
    public void testGetClassParameter() {
        System.out.println("getClassParameter");
        LinkFactory instance = new LinkFactory();
        Parameter result = instance.getClassParameter();
        assertNotNull(result);
        assertEquals("LinkClass",result);
    }

}
