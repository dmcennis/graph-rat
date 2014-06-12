/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;

import java.util.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.property.Property;

/**
 *
 * @author dm75
 */
public class MeanErrorEvaluationTest extends TestCase {
    
    public MeanErrorEvaluationTest(String testName) {
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
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorA() {
        System.out.println("executeMeanErrorA");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorB() {
        System.out.println("executeMeanErrorB");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorC() {
        System.out.println("executeMeanErrorC");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.125,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorD() {
        System.out.println("executeMeanErrorD");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorE() {
        System.out.println("executeMeanErrorE");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorMeanErrorF() {
        System.out.println("executeF");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorG() {
        System.out.println("executeMeanErrorG");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanErrorGraph() {
        System.out.println("executeMeanErrorGraph");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);        
        Property property = EvaluationTestGraphs.valued.getProperty("Evaluation MeanError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.4464,((Double)values[0]).doubleValue(),0.0001);
        property = EvaluationTestGraphs.valued.getProperty("Evaluation MeanErrorSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.3705,((Double)values[0]).doubleValue(),0.0001);
    }
    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorA() {
        System.out.println("executeMeanSquaredErrorA");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorB() {
        System.out.println("executeMeanSquaredErrorB");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorC() {
        System.out.println("executeMeanSquaredErrorC");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.40625,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorD() {
        System.out.println("executeMeanSquaredErrorD");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorE() {
        System.out.println("executeMeanSquaredErrorE");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorF() {
        System.out.println("executeMeanSquaredErrorF");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorG() {
        System.out.println("executeMeanSquaredErrorG");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteMeanSquaredErrorGraph() {
        System.out.println("executeMeanSquaredErrorGraph");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Property property = EvaluationTestGraphs.valued.getProperty("Evaluation MeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.4866,((Double)values[0]).doubleValue(),0.0001);
        property = EvaluationTestGraphs.valued.getProperty("Evaluation MeanSquaredErrorSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.820,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorA() {
        System.out.println("executeRootMeanSquaredErrorA");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorB() {
        System.out.println("executeRootMeanSquaredErrorB");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorC() {
        System.out.println("executeRootMeanSquaredErrorC");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.8385,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorD() {
        System.out.println("executeRootMeanSquaredErrorD");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorE() {
        System.out.println("executeRootMeanSquaredErrorE");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorF() {
        System.out.println("executeRootMeanSquaredErrorF");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorG() {
        System.out.println("executeRootMeanSquaredErrorG");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class MeanErrorEvaluation.
     */
    public void testExecuteRootMeanSquaredErrorGraph() {
        System.out.println("executeRootMeanSquaredErrorGraph");
        MeanErrorEvaluation instance = new MeanErrorEvaluation();
        instance.execute(EvaluationTestGraphs.valued);
        Property property = EvaluationTestGraphs.valued.getProperty("Evaluation RootMeanSquaredError");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.4055,((Double)values[0]).doubleValue(),0.0001);
        property = EvaluationTestGraphs.valued.getProperty("Evaluation RootMeanSquaredErrorSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.052,((Double)values[0]).doubleValue(),0.0001);
    }

    
}
