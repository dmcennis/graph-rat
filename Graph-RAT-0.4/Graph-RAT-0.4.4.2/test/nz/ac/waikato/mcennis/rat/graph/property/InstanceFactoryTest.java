/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.ac.waikato.mcennis.rat.graph.property;

import junit.framework.TestCase;
import weka.core.Instance;

/**
 *
 * @author dm75
 */
public class InstanceFactoryTest extends TestCase {
    
    Instance test;
    String string;
    double[] content = new double[]{0.0,1.0,3.0,Double.NaN};
    
    public InstanceFactoryTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        test = new Instance(1.0,content);
        string = "1.0,0.0,1.0,3.0,NaN";
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of importFromString method, of class InstanceFactory.
     */
    public void testImportFromString() {
        System.out.println("importFromString");
        InstanceFactory instance = new InstanceFactory();
        Instance result = instance.importFromString(string,null);
        assertEquals(1.0, result.weight(),0.0001);
        double[] values = result.toDoubleArray();
        assertEquals(content.length,values.length);
        for(int i=0;i<content.length;++i){
            assertEquals(content[i],values[i],0.0001);
        }
    }

    /**
     * Test of exportToString method, of class InstanceFactory.
     */
    public void testExportToString() {
        System.out.println("exportToString");
        InstanceFactory instance = new InstanceFactory();
        String result = instance.exportToString(test,null);
        assertEquals(string, result);
    }

}
