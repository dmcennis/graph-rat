/*
 * Created 8-5-08
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm;

import nz.ac.waikato.mcennis.rat.graph.algorithm.similarity.SimilarityByLink;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;

/**
 *
 * @author Daniel McEnnis
 */
public class SimilarityByLinkTest extends TestCase {
    
    public SimilarityByLinkTest(String testName) {
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
     * Test of execute method, of class SimilarityByLink.
     */
    public void testExecute() {
        System.out.println("execute");
        Graph g = null;
        SimilarityByLink instance = new SimilarityByLink();
        instance.execute(g);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
