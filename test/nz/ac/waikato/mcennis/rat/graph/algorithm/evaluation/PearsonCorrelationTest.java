/*

 * To change this template, choose Tools | Templates

 * and open the template in the editor.

 */



package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;




import java.util.List;
import junit.framework.TestCase;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;


import nz.ac.waikato.mcennis.rat.graph.actor.Actor;


/**

 *

 * @author dm75

 */

public class PearsonCorrelationTest extends TestCase {

    

    public PearsonCorrelationTest(String testName) {

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

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteA() {

        System.out.println("executeA");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Actor actor = EvaluationTestGraphs.valued.getActor("User", "a");

        assertNotNull(actor);

        Property property = actor.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(1.0,((Double)values.get(0)).doubleValue(),0.0001);

    }



    /**

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteB() {

        System.out.println("executeB");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Actor actor = EvaluationTestGraphs.valued.getActor("User", "b");

        assertNotNull(actor);

        Property property = actor.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(0.25,((Double)values.get(0)).doubleValue(),0.0001);

    }



    /**

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteC() {

        System.out.println("executeC");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Actor actor = EvaluationTestGraphs.valued.getActor("User", "c");

        assertNotNull(actor);

        Property property = actor.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(4.0,((Double)values.get(0)).doubleValue(),0.0001);

    }



    /**

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteD() {

        System.out.println("executeD");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Actor actor = EvaluationTestGraphs.valued.getActor("User", "d");

        assertNotNull(actor);

        Property property = actor.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(1.0,((Double)values.get(0)).doubleValue(),0.0001);

    }



    /**

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteE() {

        System.out.println("executeE");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Actor actor = EvaluationTestGraphs.valued.getActor("User", "e");

        assertNotNull(actor);

        Property property = actor.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(1.0,((Double)values.get(0)).doubleValue(),0.0001);

    }



    /**

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteF() {

        System.out.println("executeF");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Actor actor = EvaluationTestGraphs.valued.getActor("User", "f");

        assertNotNull(actor);

        Property property = actor.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(1.0,((Double)values.get(0)).doubleValue(),0.0001);

    }



    /**

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteG() {

        System.out.println("executeG");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Actor actor = EvaluationTestGraphs.valued.getActor("User", "g");

        assertNotNull(actor);

        Property property = actor.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(1.0,((Double)values.get(0)).doubleValue(),0.0001);

    }



    /**

     * Test of execute method, of class PearsonCorrelation.

     */

    public void testExecuteGraph() {

        System.out.println("executeGraph");

        PearsonCorrelation instance = new PearsonCorrelation();

        instance.execute(EvaluationTestGraphs.valued);

        Property property = EvaluationTestGraphs.valued.getProperty("Evaluation PearsonsCorrelation");

        assertNotNull(property);

        List<Object> values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(1.3214,((Double)values.get(0)).doubleValue(),0.0001);

        property = EvaluationTestGraphs.valued.getProperty("Evaluation PearsonsCorrelationSD");

        assertNotNull(property);

        values = property.getValue();

        assertNotNull(values);

        assertEquals(1,values.size());

        assertEquals(8.8393,((Double)values.get(0)).doubleValue(),0.0001);

    }





}

