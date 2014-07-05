/*
 * Created 26-3-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;

import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.BasicUserLink;

/**
 *
 * @author Daniel McEnnis
 */
public class KendallTauTest extends TestCase {
    
    public KendallTauTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        EvaluationTestGraphs.init();
        EvaluationTestGraphs.gv_c3.set(3.0);
        BasicUserLink link = new BasicUserLink();
        link.setType("Given");
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "a");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation KendallTau");
        assertNull(property);
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "b");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation KendallTau");
        assertNull(property);
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "c");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation KendallTau");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values.length);
        assertEquals(1,values.length);
        assertEquals(-1.0,((Double)values[0]).doubleValue(),0.0001);
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteD() {
        System.out.println("executeD");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "d");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation KendallTau");
        assertNull(property);
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteE() {
        System.out.println("executeE");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "e");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation KendallTau");
        assertNull(property);
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteF() {
        System.out.println("executeF");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "f");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation KendallTau");
        assertNull(property);
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteG() {
        System.out.println("executeG");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Actor actor = EvaluationTestGraphs.valued.getActor("User", "g");
        assertNotNull(actor);
        Property property = actor.getProperty("Evaluation KendallTau");
        assertNull(property);
    }

    /**
     * Test of execute method, of class KendallTau.
     */
    public void testExecuteGraph() {
        System.out.println("executeGraph");
        KendallTau instance = new KendallTau();
        instance.execute(EvaluationTestGraphs.valued);
        Property property = EvaluationTestGraphs.valued.getProperty("Evaluation KendallTau");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values.length);
        assertEquals(1,values.length);
        assertEquals(-0.14286,((Double)values[0]).doubleValue(),0.0001);
        property = EvaluationTestGraphs.valued.getProperty("Evaluation KendallTauSD");
        assertNotNull(property);
        values = property.getValue();
        assertNotNull(values.length);
        assertEquals(1,values.length);
        assertEquals(0.85714,((Double)values[0]).doubleValue(),0.0001);
    }

}
