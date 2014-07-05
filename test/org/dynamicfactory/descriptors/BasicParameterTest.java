/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamicfactory.descriptors;

import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author mcennis
 */
public class BasicParameterTest extends TestCase {

    BasicParameter param;

    public BasicParameterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        param = new BasicParameter();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of setStructural method, of class BasicParameter.
     */
    public void testSetStructural(){
        System.out.println("setStructural");
        boolean b = false;
        BasicParameter instance = new BasicParameter();
        instance.setStructural(b);
    }

    /**
     * Test of isStructural method, of class BasicParameter.
     */
    public void testIsStructural() {
        System.out.println("isStructural");
        BasicParameter instance = new BasicParameter();
        instance.setStructural(false);
        boolean expResult = false;
        boolean result = instance.isStructural();
        assertEquals(expResult, result);

        instance.setStructural(true);
        expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class BasicParameter.
     */
    public void testSet_Property() {
        System.out.println("set");
        Property o = PropertyFactory.newInstance().create("Param", String.class);
        BasicParameter instance = new BasicParameter();
        instance.set(o);
    }

    /**
     * Test of getDescription method, of class BasicParameter.
     */
    public void testGetDescription() {
        System.out.println("getDescription");
        BasicParameter instance = new BasicParameter();
        instance.setDescription("This is this parameter's description");
        String expResult = "This is this parameter's description";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDescription method, of class BasicParameter.
     */
    public void testSetDescription() {
        System.out.println("setDescription");
        BasicParameter instance = new BasicParameter();
        instance.setDescription("This is this parameter's description");
    }

    /**
     * Test of setType method, of class BasicParameter.
     */
    public void testSetType() {
        System.out.println("setType");
        String name = "New Type";
        BasicParameter instance = new BasicParameter();
        instance.setType(name);
    }

    /**
     * Test of setRestrictions method, of class BasicParameter.
     */
    public void testSetRestrictions() {
        System.out.println("setRestrictions");
        PropertyRestriction syntax = new PropertyRestriction();
        BasicParameter instance = new BasicParameter();
        instance.setRestrictions(syntax);
    }

    /**
     * Test of getRestrictions method, of class BasicParameter.
     */
    public void testGetRestrictions() {
        System.out.println("getRestrictions");
        BasicParameter instance = new BasicParameter();
        PropertyRestriction expResult = new PropertyRestriction();
        SyntaxObject result = instance.getRestrictions();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class BasicParameter.
     */
    public void testAdd_Object() {
        System.out.println("add");
        Object o = "Test Object";
        BasicParameter instance = new BasicParameter();
        instance.setParameterClass(String.class);
        instance.add(o);
    }

    /**
     * Test of add method, of class BasicParameter.
     */
    public void testGood_Add_Object_With_Restrictions() {
        System.out.println("add");
        Object o = "String Value";
        BasicParameter instance = new BasicParameter();
        PropertyRestriction r = new PropertyRestriction();
        r.setClassType(String.class);
        r.setMinCount(1);
        r.setMaxCount(1);
        instance.setRestrictions(r);
        instance.add(o);
        assertEquals(1,instance.getValue().size());
    }

    /**
     * Test of add method, of class BasicParameter.
     */
    public void testBad_Add_Object() {
        System.out.println("add");
        Object o = "String Value";
        BasicParameter instance = new BasicParameter();
        PropertyRestriction r = new PropertyRestriction();
        r.setClassType(String.class);
        r.setMinCount(2);
        r.setMaxCount(2);
        instance.setRestrictions(r);
        instance.add(o);
        assertEquals(0,instance.getValue().size());
    }

    /**
     * Test of getType method, of class BasicParameter.
     */
    public void testGetType() {
        System.out.println("getType");
        BasicParameter instance = new BasicParameter();
        String expResult = "Type";
        instance.setType(expResult);
        String result = instance.getType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getValue method, of class BasicParameter.
     */
    public void testGetEmptyValue() {
        System.out.println("getEmptyValue");
        BasicParameter instance = new BasicParameter();
        List<Object> result = instance.getValue();
        assertEquals(0, result.size());
    }

    /**
     * Test of getValue method, of class BasicParameter.
     */
    public void testGetNonZeroValue() {
        System.out.println("getNonZeroValue");
        BasicParameter instance = new BasicParameter();
        instance.add("A String");
        List<Object> result = instance.getValue();
        assertEquals(1, result.size());
        assertEquals("A String",result.get(0));
    }

    /**
     * Test of getParameterClass method, of class BasicParameter.
     */
    public void testGetParameterClass() {
        System.out.println("getParameterClass");
        BasicParameter instance = new BasicParameter();
        instance.setParameterClass(String.class);
        Class expResult = String.class;
        Class result = instance.getParameterClass();
        assertEquals(expResult, result);
    }

    /**
     * Test of setParameterClass method, of class BasicParameter.
     */
    public void testSetParameterClass() {
        System.out.println("setParameterClass");
        BasicParameter instance = new BasicParameter();
        instance.setParameterClass(String.class);
    }

    /**
     * Test of check method, of class BasicParameter.
     */
    public void testCheck_Default_Property() {
        System.out.println("checkDefaultDefault");
        Property property = PropertyFactory.newInstance().create("Type", String.class);
        BasicParameter instance = new BasicParameter();
        instance.setParameterClass(String.class);
        instance.setType("Type");
        boolean expResult = true;
        boolean result = instance.check(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class BasicParameter.
     */
    public void testBadTypeCheck_Default_Property() {
        System.out.println("checkDefaultDefault");
        Property property = PropertyFactory.newInstance().create("Bad ID", String.class);
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        instance.setParameterClass(String.class);
        boolean expResult = false;
        boolean result = instance.check(property);
        assertEquals(expResult, result);
    }


    /**
     * Test of check method, of class BasicParameter.
     */
    public void testBadClassCheck_Default_Property() {
        System.out.println("checkDefaultDefault");
        Property property = PropertyFactory.newInstance().create("Type", Double.class);
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        instance.setParameterClass(String.class);
        boolean expResult = false;
        boolean result = instance.check(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class BasicParameter.
     */
    public void testCheck_Property() throws InvalidObjectTypeException {
        System.out.println("check");
        Property property = PropertyFactory.newInstance().create("Type", String.class);
        property.add("Value 1");
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        instance.setParameterClass(String.class);
        instance.add("Original Value");
        boolean expResult = true;
        boolean result = instance.check(property);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class BasicParameter.
     */
    public void testCheck_String_Object() {
        System.out.println("check");
        String type = "Type";
        String value = "Value";
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        instance.setParameterClass(String.class);
        instance.add("Old Value");
        instance.getRestrictions().setMaxCount(1);
        boolean expResult = true;
        boolean result = instance.check(type, value);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class BasicParameter.
     */
    public void testCheck_Parameter() {
        System.out.println("check");
        BasicParameter type = new BasicParameter();
        type.setType("Type");
        type.setParameterClass(String.class);
        type.set("Value 1");
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        instance.setParameterClass(String.class);
        instance.set("Value 0");
        instance.getRestrictions().setMaxCount(1);
        boolean expResult = true;
        boolean result = instance.check(type);
        assertEquals(expResult, result);
    }

    /**
     * Test of clear method, of class BasicParameter.
     */
    public void testClear() {
        System.out.println("clear");
        BasicParameter instance = new BasicParameter();
        instance.add("Test");
        instance.clear();
        assertEquals(0,instance.getValue().size());
    }

    /**
     * Test of add method, of class BasicParameter.
     */
    public void testAdd_List() {
        System.out.println("add");
        List value = new LinkedList<String>();
        value.add("Test 2");
        value.add("Test 3");
        BasicParameter instance = new BasicParameter();
        instance.add("Test");
        instance.add(value);
        instance.getRestrictions().setMaxCount(3);
        instance.getRestrictions().setMinCount(3);
        assertEquals(3,instance.getValue().size());
    }

    /**
     * Test of check method, of class BasicParameter.
     */
    public void testBadTypeCheck_String_List() {
        System.out.println("check");
        String type = "Other";
        List value = new LinkedList<String>();
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        boolean expResult = false;
        boolean result = instance.check(type, value);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class BasicParameter.
     */
    public void testCheck_String_List() {
        System.out.println("check");
        String type = "Type";
        List value = new LinkedList<String>();
        value.add("Test 1");
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        instance.add("Value");
        instance.getRestrictions().setMaxCount(1);
        boolean expResult = false;
        boolean result = instance.check(type, value);
        assertEquals(expResult, result);
    }

    /**
     * Test of duplicate method, of class BasicParameter.
     */
    public void testDuplicate() {
        System.out.println("duplicate");
        BasicParameter instance = new BasicParameter();
        instance.setType("Type");
        instance.setParameterClass((String.class));
        instance.getRestrictions().setMaxCount(13);
        instance.add("Value");
        ParameterInternal result = instance.duplicate();
        assertNotSame(instance, result);
        assertEquals(instance.getType(),result.getType());
        assertEquals(instance.getParameterClass(),result.getParameterClass());
        assertNotSame(instance.getRestrictions(),result.getRestrictions());
        assertEquals(instance.getRestrictions().getMaxCount(),result.getRestrictions().getMaxCount());
        assertEquals(instance.getValue().size(),result.getValue().size());
        assertEquals(instance.get(),result.get());
    }

    /**
     * Test of get method, of class BasicParameter.
     */
    public void testGet() {
        System.out.println("get");
        BasicParameter instance = new BasicParameter();
        instance.add("Value");
        Object expResult = "Value";
        Object result = instance.get();
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class BasicParameter.
     */
    public void testSet_Object() {
        System.out.println("set");
        Object value = "Value";
        BasicParameter instance = new BasicParameter();
        instance.add("Value2");
        instance.set(value);
        assertEquals(1,instance.getValue().size());
        assertEquals("Value2",instance.get());
    }

    /**
     * Test of set method, of class BasicParameter.
     */
    public void testSet_List() {
        System.out.println("set");
        List value = new LinkedList<String>();
        value.add("Value 1");
        value.add("Value 2");
        BasicParameter instance = new BasicParameter();
        instance.add("Old Value");
        instance.set(value);
        assertEquals(2,instance.getValue().size());
        assertEquals("Value 1",instance.get());
        assertEquals("Value 2",instance.getValue().get(1));
    }

}
