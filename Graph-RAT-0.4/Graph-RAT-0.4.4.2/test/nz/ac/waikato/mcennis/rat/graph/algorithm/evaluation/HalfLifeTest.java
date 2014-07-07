/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mcennis.graphrat.algorithm.evaluation;

import junit.framework.TestCase;
import org.mcennis.graphrat.actor.Actor;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;

/**
 *
 * @author dm75
 */
public class HalfLifeTest extends TestCase {
    
    public HalfLifeTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        EvaluationTestGraphs.init();
        EvaluationTestGraphs.dv_a2.set(0.95);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation HalfLife");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation HalfLife");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.7040,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation HalfLife");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteD() {
        System.out.println("executeD");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation HalfLife");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteE() {
        System.out.println("executeE");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation HalfLife");
        assertNull(property);
    }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteF() {
        System.out.println("executeF");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation HalfLife");
        assertNull(property);
   }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteG() {
        System.out.println("executeG");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation HalfLife");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class HalfLife.
     */
    public void testExecuteGraph() {
        System.out.println("executeGraph");
        HalfLife instance = new HalfLife();
        instance.execute(EvaluationTestGraphs.valued);
        Property property = EvaluationTestGraphs.valued.getProperty("Evaluation HalfLife");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.6720,((Double)values[0]).doubleValue(),0.0001);
        property = EvaluationTestGraphs.valued.getProperty("Evaluation HalfLifeSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.3345,((Double)values[0]).doubleValue(),0.0001);
    }


}
