/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamicfactory.descriptors;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

/**
 *
 * @author mcennis
 */
public class PropertiesImplementationTest extends TestCase {
    
    public PropertiesImplementationTest(String testName) {
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
     * Test of get method, of class PropertiesImplementation.
     */
    public void testGet_String() {
        System.out.println("get");
        String string = "BasicType";
        PropertiesImplementation instance = new PropertiesImplementation();
        ParameterInternal expResult = ParameterFactory.newInstance().create("BasicType", String.class);
        instance.add(expResult);
        ParameterInternal result = instance.get(string);
        assertSame(expResult, result);
    }

    /**
     * Test of get method, of class PropertiesImplementation.
     */
    public void testNullGet_String() {
        System.out.println("get");
        String string = "Type";
        PropertiesImplementation instance = new PropertiesImplementation();
        ParameterInternal expResult = null;
        ParameterInternal result = instance.get(string);
        assertEquals(expResult, result);
    }

    /**
     * Test of set method, of class PropertiesImplementation.
     */
    public void testSet_Property() {
        System.out.println("set");
        Property value = PropertyFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.set(value);
        ParameterInternal result = instance.get("Type");
        assertNotNull(result);
        assertEquals("Type",result.getType());
        assertEquals(String.class,result.getParameterClass());
    }

    /**
     * Test of set method, of class PropertiesImplementation.
     */
    public void testNullSet_Property() {
        System.out.println("set");
        Property value = null;
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.set(value);
        assertEquals(0,instance.get().size());
    }

    /**
     * Test of add method, of class PropertiesImplementation.
     */
    public void testAdd_String_Object() {
        System.out.println("add");
        String type = "Type";
        Object value = "Object";
        BasicParameter start = new BasicParameter();
        start.setType("Type");
        start.setParameterClass(String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(start);
        instance.add(type, value);
        Parameter param = instance.get("Type");
        assertEquals("Object",param.get());
    }

    /**
     * Test of add method, of class PropertiesImplementation.
     */
    public void testBadDefaultAdd_String_Object() {
        System.out.println("add");
        String type = "Type";
        Object value = new Double(0.0);
        PropertiesImplementation instance = new PropertiesImplementation();
        SyntaxObject object = SyntaxCheckerFactory.newInstance().create(0, Integer.MAX_VALUE, null, String.class);
        instance.setDefaultRestriction(object);
        instance.add(type, value);
        assertNull(instance.get("Type"));
    }

    /**
     * Test of add method, of class PropertiesImplementation.
     */
    public void testMissingAdd_String_Object() {
        System.out.println("add");
        String type = "Type";
        Object value = "Object";
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(type, value);
        Parameter param = instance.get("Type");
        assertEquals("Object",param.get());
    }

    /**
     * Test of add method, of class PropertiesImplementation.
     */
    public void testAdd_ParameterInternal() {
        System.out.println("add");
        ParameterInternal parameter = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(parameter);
        assertSame(parameter,instance.get("Type"));
    }

    /**
     * Test of add method, of class PropertiesImplementation.
     */
    public void testMissingAdd_ParameterInternal() {
        ParameterInternal parameter = ParameterFactory.newInstance().create("Type", String.class);
        ParameterInternal start = ParameterFactory.newInstance().create("Type", String.class);
        parameter.add("Object");
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(start);
        instance.add(parameter);
        assertEquals("Object",instance.get("Type").get());
    }

    /**
     * Test of remove method, of class PropertiesImplementation.
     */
    public void testRemove() {
        System.out.println("remove");
        ParameterInternal start = ParameterFactory.newInstance().create("Type", String.class);
        start.add("Object");
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(start);
        instance.remove("Type");
        assertNull(instance.get("Type"));
    }

    /**
     * Test of remove method, of class PropertiesImplementation.
     */
    public void testMissingRemove() {
        System.out.println("remove");
        String type = "";
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.remove("Type");
    }
    
    /**
     * Test of getDefaultRestriction method, of class PropertiesImplementation.
     */
    public void testGetDefaultRestriction() {
        System.out.println("getDefaultRestriction");
        PropertiesImplementation instance = new PropertiesImplementation();
        SyntaxObject expResult = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        instance.setDefaultRestriction(expResult);
        SyntaxObject result = instance.getDefaultRestriction();
        assertSame(expResult, result);
    }

    /**
     * Test of setDefaultRestriction method, of class PropertiesImplementation.
     */
    public void testSetDefaultRestriction() {
        System.out.println("getDefaultRestriction");
        PropertiesImplementation instance = new PropertiesImplementation();
        SyntaxObject expResult = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        instance.setDefaultRestriction(expResult);
    }

    /**
     * Test of duplicate method, of class PropertiesImplementation.
     */
    public void testDuplicate() {
        System.out.println("duplicate");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        param.add("value");
        SyntaxObject restriction = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param);
        instance.setDefaultRestriction(restriction);
        PropertiesImplementation result = instance.duplicate();
        assertEquals(param.getType(), result.get("Type").getType());
        assertEquals(param.get(),result.get("Type").get());
        assertEquals(restriction.getMaxCount(),result.getDefaultRestriction().getMaxCount());
    }

    /**
     * Test of get method, of class PropertiesImplementation.
     */
    public void testGet_0args() {
        System.out.println("get");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        ParameterInternal param2 = ParameterFactory.newInstance().create("Type2", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        instance.add(param2);
        List<Parameter> result = instance.get();
        assertEquals(2,result.size());
        Iterator<Parameter> it= result.iterator();
        while(it.hasNext()){
            String type = it.next().getType();
            if("Type".equals(type) ){

            }else if("Type2".equals(type)){

            }else{
                fail("Unexpected type '"+type+"' found");
            }
        }
    }

    /**
     * Test of get method, of class PropertiesImplementation.
     */
    public void testEmptyGet_0args() {
        System.out.println("get");
        PropertiesImplementation instance = new PropertiesImplementation();
        List<Parameter> result = instance.get();
        assertEquals(0, result.size());
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testCheck_Parameter() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        boolean expResult = true;
        boolean result = instance.check(param1);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testCheckMissing_Parameter() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        boolean expResult = true;
        boolean result = instance.check(param1);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testBadCheck_Parameter() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        SyntaxObject object = SyntaxCheckerFactory.newInstance().create(1, 1, null, Double.class);
        instance.setDefaultRestriction(object);
        boolean expResult = false;
        boolean result = instance.check(param1);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testDefaultCheck_Properties() {
        System.out.println("check");
        PropertiesImplementation props = new PropertiesImplementation();
        PropertiesImplementation instance = new PropertiesImplementation();
        boolean expResult = true;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testBasicCheck_Properties() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation props = new PropertiesImplementation();
        props.add(param1);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        boolean expResult = true;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testBasicBadRightCheck_Properties() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param1.setRestrictions(syntax);
        PropertiesImplementation props = new PropertiesImplementation();
        props.add(param1);
        PropertiesImplementation instance = new PropertiesImplementation();
        ParameterInternal param2 = ParameterFactory.newInstance().create("Type", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param2.setRestrictions(syntax);
        param2.add("Correct");
        instance.add(param2);
        boolean expResult = false;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testBadLeftCheck_Properties() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param1.setRestrictions(syntax);
        PropertiesImplementation props = new PropertiesImplementation();
        props.add(param1);
        PropertiesImplementation instance = new PropertiesImplementation();
        ParameterInternal param2 = ParameterFactory.newInstance().create("Type", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param2.setRestrictions(syntax);
        param2.add("Correct");
        instance.add(param2);
        boolean expResult = true;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testRightMissingCheck_Properties() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation props = new PropertiesImplementation();
        props.add(param1);
        PropertiesImplementation instance = new PropertiesImplementation();
        boolean expResult = true;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testLeftMissingCheck_Properties() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation props = new PropertiesImplementation();
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        boolean expResult = true;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testBothMissingCheck_Properties() {
        System.out.println("check");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        ParameterInternal param2 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation props = new PropertiesImplementation();
        props.add(param1);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param2);
        boolean expResult = true;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of check method, of class PropertiesImplementation.
     */
    public void testCheckNull_Properties() {
        System.out.println("check");
        PropertiesImplementation props = null;
        PropertiesImplementation instance = new PropertiesImplementation();
        boolean expResult = false;
        boolean result = instance.check(props);
        assertEquals(expResult, result);
    }

    /**
     * Test of replace method, of class PropertiesImplementation.
     */
    public void testDefaultReplace() {
        System.out.println("replace");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        ParameterInternal param2 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        instance.replace(param2);
        assertSame(param2,instance.get("Type"));
    }

    /**
     * Test of replace method, of class PropertiesImplementation.
     */
    public void testMissingReplace() {
        System.out.println("replace");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.replace(param);
        assertNotNull(instance.get("Type"));
        assertSame(param,instance.get("Type"));
    }

    /**
     * Test of replace method, of class PropertiesImplementation.
     */
    public void testNullReplace() {
        System.out.println("replace");
        Parameter type = null;
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.replace(type);
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testDefaultMerge() {
        System.out.println("merge");
        PropertiesImplementation right = new PropertiesImplementation();
        PropertiesImplementation instance = new PropertiesImplementation();
        PropertiesInternal result = instance.merge(right);
        assertEquals(0,result.get().size());
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testSameMerge() {
        System.out.println("merge");
        ParameterInternal param = ParameterFactory.newInstance().create("Tye", String.class);
        PropertiesImplementation right = new PropertiesImplementation();
        right.add(param);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param);
        PropertiesInternal result = instance.merge(right);
        assertEquals(1,result.get().size());
        assertSame(param,result.get("Tye"));
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testConsistantMerge() {
        System.out.println("merge");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        param1.add("String");
        PropertiesImplementation right = new PropertiesImplementation();
        right.add(param);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        PropertiesInternal result = instance.merge(right);
        assertEquals(1,result.get().size());
        assertEquals(1,result.get("Type").getValue());
        assertEquals("String",result.get("Type").get());
    }


    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testInconsistantMerge() {
        System.out.println("merge");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(0, 1, null, String.class);
        param.setRestrictions(syntax);
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param1.setRestrictions(syntax);
        param1.add("String");
        PropertiesImplementation right = new PropertiesImplementation();
        right.add(param);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        PropertiesInternal result = instance.merge(right);
        assertNull(result);
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testLeftMissingMerge() {
        System.out.println("merge");
        PropertiesImplementation right = new PropertiesImplementation();
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        PropertiesInternal result = instance.merge(right);
        assertEquals(1,result.get().size());
        assertSame(param1,result.get("Type"));
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testRightMissingMerge() {
        System.out.println("merge");
        PropertiesImplementation right = new PropertiesImplementation();
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        PropertiesImplementation instance = new PropertiesImplementation();
        right.add(param1);
        PropertiesInternal result = instance.merge(right);
        assertEquals(1,result.get().size());
        assertSame(param1,result.get("Type"));
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testBothMissingMerge() {
        System.out.println("merge");
        PropertiesImplementation right = new PropertiesImplementation();
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        right.add(param1);
        PropertiesImplementation instance = new PropertiesImplementation();
        ParameterInternal param2 = ParameterFactory.newInstance().create("Type2", String.class);
        instance.add(param2);
        PropertiesInternal result = instance.merge(right);
        assertEquals(2,result.get().size());
        assertSame(param1,result.get("Type"));
        assertSame(param2,result.get("Type2"));
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testBadLeftMerge() {
        System.out.println("merge");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(syntax);
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param1.setRestrictions(syntax);
        param1.add("String");
        PropertiesImplementation right = new PropertiesImplementation();
        right.add(param);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        PropertiesInternal result = instance.merge(right);
        assertEquals(1,result.get().size());
        assertEquals(1,result.get("Type").getValue().size());
        assertSame(param1,result.get("Type"));
    }

    /**
     * Test of merge method, of class PropertiesImplementation.
     */
    public void testBadRightMerge() {
        System.out.println("merge");
        ParameterInternal param = ParameterFactory.newInstance().create("Type", String.class);
        SyntaxObject syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param.setRestrictions(syntax);
        param.add("String");
        ParameterInternal param1 = ParameterFactory.newInstance().create("Type", String.class);
        syntax = SyntaxCheckerFactory.newInstance().create(1, 1, null, String.class);
        param1.setRestrictions(syntax);
        PropertiesImplementation right = new PropertiesImplementation();
        right.add(param);
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(param1);
        PropertiesInternal result = instance.merge(right);
        assertNull(result);
    }

    /**
     * Test of add method, of class PropertiesImplementation.
     */
    public void testAdd_String_List() {
        System.out.println("add");
        String type = "Type";
        List value = new LinkedList<String>();
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.add(type, value);
        assertEquals(1,instance.get().size());
    }

    /**
     * Test of set method, of class PropertiesImplementation.
     */
    public void testSet_String_Object() {
        System.out.println("set");
        String type = "Type";
        Object value = "Value";
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.set(type, value);
        assertEquals(1,instance.get().size());
    }

    /**
     * Test of set method, of class PropertiesImplementation.
     */
    public void testSet_String_List() {
        System.out.println("set");
        String type = "Type";
        List value = new LinkedList<String>();
        PropertiesImplementation instance = new PropertiesImplementation();
        instance.set(type, value);
        assertEquals(1,instance.get().size());
    }

}
