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
public class PrecisionRecallFMeasureTest extends TestCase {
    
    
    public PrecisionRecallFMeasureTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        EvaluationTestGraphs.init();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionA() {
        System.out.println("executePrecisionA");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionB() {
        System.out.println("executePrecisionB");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionC() {
        System.out.println("executePrecisionC");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionD() {
        System.out.println("executePrecisionD");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionE() {
        System.out.println("executePrecisionE");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionF() {
        System.out.println("executePrecisionF");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionG() {
        System.out.println("executePrecisionG");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecutePrecisionGraph() {
        System.out.println("executePrecisionGraph");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Property property = EvaluationTestGraphs.binary.getProperty("Evaluation Precision");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5714,((Double)values[0]).doubleValue(),0.001);
        property = EvaluationTestGraphs.binary.getProperty("Evaluation PrecisionSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.214,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallA() {
        System.out.println("executeRecallA");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallB() {
        System.out.println("executeRecallB");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallC() {
        System.out.println("executeRecallC");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallD() {
        System.out.println("executeRecallD");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallE() {
        System.out.println("executeRecallE");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallF() {
        System.out.println("executeRecallF");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallG() {
        System.out.println("executeRecallG");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteRecallGraph() {
        System.out.println("executeRecallGraph");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Property property = EvaluationTestGraphs.binary.getProperty("Evaluation Recall");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5714,((Double)values[0]).doubleValue(),0.001);
        property = EvaluationTestGraphs.binary.getProperty("Evaluation RecallSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.214,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureA() {
        System.out.println("executeFMeasureA");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.6667,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureB() {
        System.out.println("executeFMeasureB");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureC() {
        System.out.println("executeFMeasureC");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.6667,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureD() {
        System.out.println("executeFMeasureD");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureE() {
        System.out.println("executeFMeasureE");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureF() {
        System.out.println("executeFMeasureF");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureG() {
        System.out.println("executeFMeasureG");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.001);
    }

    /**
     * Test of execute method, of class PrecisionRecallFMeasure.
     */
    public void testExecuteFMeasureGraph() {
        System.out.println("executeFMeasureGraph");
        PrecisionRecallFMeasure instance = new PrecisionRecallFMeasure();
        instance.execute(EvaluationTestGraphs.binary);
        Property property = EvaluationTestGraphs.binary.getProperty("Evaluation FMeasure");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5476,((Double)values[0]).doubleValue(),0.001);
        property = EvaluationTestGraphs.binary.getProperty("Evaluation FMeasureSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.040,((Double)values[0]).doubleValue(),0.001);
    }

    
}
