/*
 * Created 26-3-08
 * Copyright Daniel McEnnis, see liceanse.txt
 */

package org.mcennis.graphrat.algorithm.evaluation;

import junit.framework.TestCase;
import org.mcennis.graphrat.actor.Actor;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;

/**
 *
 * @author Daniel McEnnis
 */
public class ROCAreaEvaluationTest extends TestCase {
    
    public ROCAreaEvaluationTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        EvaluationTestGraphs.init();
        EvaluationTestGraphs.dv_a2.set(0.95);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "a");
        Property property = actor.getProperty("Evaluation ROCArea");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "b");
        Property property = actor.getProperty("Evaluation ROCArea");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "c");
        Property property = actor.getProperty("Evaluation ROCArea");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteD() {
        System.out.println("executeD");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "d");
        Property property = actor.getProperty("Evaluation ROCArea");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteE() {
        System.out.println("executeE");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "e");
        Property property = actor.getProperty("Evaluation ROCArea");
        assertNull(property);
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteF() {
        System.out.println("executeF");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "f");
        Property property = actor.getProperty("Evaluation ROCArea");
        assertNull(property);
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteG() {
        System.out.println("executeG");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "g");
        Property property = actor.getProperty("Evaluation ROCArea");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class ROCAreaEvaluation.
     */
    public void testExecuteGraph() {
        System.out.println("executeGraph");
        ROCAreaEvaluation instance = new ROCAreaEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Property property = EvaluationTestGraphs.valued.getProperty("Evaluation ROCArea");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.6429,((Double)values[0]).doubleValue(),0.0001);
        property = EvaluationTestGraphs.valued.getProperty("Evaluation ROCAreaSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.3571,((Double)values[0]).doubleValue(),0.0001);
    }


}
