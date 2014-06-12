/*
 * Created 26-3-08
 * Copyright Daniel McEnnis, see license.txt
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
 * @author Daniel McEnnis
 */
public class AdHocClassificationTest extends TestCase {
    
    public AdHocClassificationTest(String testName) {
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
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation AdHocClassification");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation AdHocClassification");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.5,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation AdHocClassification");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteD() {
        System.out.println("executeD");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation AdHocClassification");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteE() {
        System.out.println("executeE");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation AdHocClassification");
        assertNull(property);
    }

    /**
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteF() {
        System.out.println("executeF");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation AdHocClassification");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteG() {
        System.out.println("executeG");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Actor actor = EvaluationTestGraphs.binary.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation AdHocClassification");
        assertNull(property);
    }

    /**
     * Test of execute method, of class AdHocClassification.
     */
    public void testExecuteGraph() {
        System.out.println("executeGraph");
        RecommendationError instance = new RecommendationError();
        instance.execute(EvaluationTestGraphs.binary);
        Property property = EvaluationTestGraphs.binary.getProperty("Evaluation AdHocClassification");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.28571,((Double)values[0]).doubleValue(),0.0001);
        property = EvaluationTestGraphs.binary.getProperty("Evaluation AdHocClassificationSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals(0.9286,((Double)values[0]).doubleValue(),0.0001);
    }

}
