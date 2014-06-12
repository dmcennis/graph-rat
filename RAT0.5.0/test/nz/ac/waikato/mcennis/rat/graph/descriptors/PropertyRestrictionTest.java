/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.descriptors;

import java.util.LinkedList;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.property.InvalidObjectTypeException;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import nz.ac.waikato.mcennis.rat.graph.query.property.NullPropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.PropertyQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.StringQuery;
import nz.ac.waikato.mcennis.rat.graph.query.property.StringQuery.Operation;

/**
 *
 * @author mcennis
 */
public class PropertyRestrictionTest extends TestCase {

    PropertyRestriction restriction;

    public PropertyRestrictionTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        restriction = new PropertyRestriction();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of setPropertyQuery method, of class PropertyRestriction.
     */
    public void testSetPropertyQuery() {
        System.out.println("setPropertyQuery");
        PropertyQuery test = new StringQuery();
        PropertyRestriction instance = new PropertyRestriction();
        instance.setPropertyQuery(test);
        assertSame(test, instance.getTest());
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testCheck_Property_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        boolean expResult = true;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadClassCheck_Property_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(Double.class);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadListClassCheck_Property_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        Object o = new Double(0.0);
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(3);
        instance.setMaxCount(3);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCountCheck_Property_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadPropertyCheck_Property_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value");
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCheck_Property_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testCheck_Property() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Basic", String.class);
        type.add("Value");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMaxCount(1);
        instance.setMinCount(1);
        boolean expResult = true;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadPropertyCheck_Property() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Basic", String.class);
        type.add("Value");
        type.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMaxCount(2);
        instance.setMinCount(2);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        boolean expResult = false;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCountCheck_Property() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Basic", String.class);
        type.add("Value");
        type.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMaxCount(1);
        instance.setMinCount(1);
        boolean expResult = false;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadClassCheck_Property() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Basic", String.class);
        type.add("Value");
        type.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setClassType(Double.class);
        instance.setMaxCount(2);
        instance.setMinCount(2);
        boolean expResult = false;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testCheck_Parameter() {
        System.out.println("check");
        BasicParameter type = new BasicParameter();
        type.setType("Type");
        type.setParameterClass(String.class);
        type.add("Value");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setClassType(String.class);
        boolean expResult = true;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCountCheck_Parameter() {
        System.out.println("check");
        BasicParameter type = new BasicParameter();
        type.setType("Type");
        type.setParameterClass(String.class);
        type.add("Value");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMaxCount(2);
        instance.setMinCount(2);
        boolean expResult = false;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadClassCheck_Parameter() {
        System.out.println("check");
        BasicParameter type = new BasicParameter();
        type.setType("Type");
        type.setParameterClass(String.class);
        PropertyRestriction instance = new PropertyRestriction();
        instance.setClassType(Double.class);
        boolean expResult = false;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadPropertyCheck_Parameter() {
        System.out.println("check");
        BasicParameter type = new BasicParameter();
        type.setType("Type");
        type.setParameterClass(String.class);
        type.add("5");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setClassType(String.class);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        boolean expResult = false;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMaxCount method, of class PropertyRestriction.
     */
    public void testGetMaxCount() {
        System.out.println("getMaxCount");
        PropertyRestriction instance = new PropertyRestriction();
        assertEquals(Integer.MAX_VALUE,instance.getMaxCount());
        int expResult = 0;
        instance.setMaxCount(expResult);
        int result = instance.getMaxCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMaxCount method, of class PropertyRestriction.
     */
    public void testSetMaxCount() {
        System.out.println("setMaxCount");
        int maxCount = 0;
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMaxCount(maxCount);
    }

    /**
     * Test of getMinCount method, of class PropertyRestriction.
     */
    public void testGetMinCount() {
        System.out.println("getMinCount");
        PropertyRestriction instance = new PropertyRestriction();
        assertEquals(0,instance.getMinCount());
        int expResult = 1;
        instance.setMinCount(expResult);
        int result = instance.getMinCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMinCount method, of class PropertyRestriction.
     */
    public void testSetMinCount() {
        System.out.println("setMinCount");
        int minCount = 1;
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(minCount);
    }

    /**
     * Test of getTest method, of class PropertyRestriction.
     */
    public void testGetTest() {
        System.out.println("getTest");
        PropertyRestriction instance = new PropertyRestriction();
        assertNotNull(instance.getTest());
        assertTrue(instance.getTest() instanceof NullPropertyQuery);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        assertSame(q, instance.getTest());
    }

    /**
     * Test of setTest method, of class PropertyRestriction.
     */
    public void testSetTest() {
        System.out.println("setTest");
        PropertyRestriction instance = new PropertyRestriction();
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
    }

    /**
     * Test of getClassType method, of class PropertyRestriction.
     */
    public void testGetClassType() {
        System.out.println("getClassType");
        PropertyRestriction instance = new PropertyRestriction();
        Class expResult = Object.class;
        Class result = instance.getClassType();
        assertEquals(expResult, result);
        expResult = String.class;
        instance.setClassType(String.class);
        result = instance.getClassType();
        assertEquals(expResult,result);
    }

    /**
     * Test of setClassType method, of class PropertyRestriction.
     */
    public void testSetClassType() {
        System.out.println("setClassType");
        Class classType = String.class;
        PropertyRestriction instance = new PropertyRestriction();
        instance.setClassType(classType);
    }

    /**
     * Test of duplicate method, of class PropertyRestriction.
     */
    public void testDuplicate() {
        System.out.println("duplicate");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setClassType(Double.class);
        instance.setMaxCount(2);
        instance.setMinCount(1);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        PropertyRestriction result = instance.duplicate();
        assertNotSame(instance, result);
        assertEquals(Double.class,result.getClassType());
        assertEquals(2,result.getMaxCount());
        assertEquals(1,result.getMinCount());
        assertNotSame(q,result.getTest());
    }


    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testCheck_Property_List() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        boolean expResult = true;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadClassCheck_Property_List() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(3);
        instance.setMaxCount(3);
        instance.setClassType(Double.class);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadListClassCheck_Property_List() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        LinkedList list = new LinkedList<Double>();
        list.add(0.0);
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(3);
        instance.setMaxCount(3);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCountCheck_Property_List() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadPropertyCheck_Property_List() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value");
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCheck_Property_List() throws InvalidObjectTypeException {
        System.out.println("check");
        Property type = PropertyFactory.newInstance().create("Type", String.class);
        type.add("Value 1");
        type.add("Value 3");
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }


    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testCheck_String_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(String.class);
        boolean expResult = true;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadClassCheck_String_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(Double.class);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadListClassCheck_String_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        Object o = 0.0;
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCountCheck_String_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadPropertyCheck_String_Object() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        Object o = "Value 2";
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(String.class);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        boolean expResult = false;
        boolean result = instance.check(type, o);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testCheck_String_List() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(String.class);
        boolean expResult = true;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /*
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadClassCheck_String_List() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(Double.class);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /*
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadListClassCheck_String_List() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        LinkedList list = new LinkedList<Double>();
        list.add(0.0);
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadCountCheck_String_List() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(2);
        instance.setMaxCount(2);
        instance.setClassType(String.class);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertyRestriction.
     */
    public void testBadPropertyCheck_String_List() throws InvalidObjectTypeException {
        System.out.println("check");
        String type = "Type";
        LinkedList list = new LinkedList<String>();
        list.add("Value 2");
        PropertyRestriction instance = new PropertyRestriction();
        instance.setMinCount(1);
        instance.setMaxCount(1);
        instance.setClassType(String.class);
        StringQuery q = new StringQuery();
        q.buildQuery("[0-9]*", true, Operation.MATCHES);
        instance.setPropertyQuery(q);
        boolean expResult = false;
        boolean result = instance.check(type, list);
        assertEquals(expResult, result);
    }

}
