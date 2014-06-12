/**
 * Created Aug 12, 2008 - 3:32:34 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator;

import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.ConcatenationAggregator;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.AssertionHandler;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class ConcatenationAggregatorTest extends TestCase {
    
    AssertionHandler handler = new AssertionHandler();
    Instances emptyDataset;
    Instances numericDataset;
    Instance emptyInstanceWithDataset;
    Instance emptyInstanceWithoutDataset;
    Instance numericInstanceWithDataset;
    Instance numericInstanceWithDataset2;
    Instance extraNumericInstance;
    Instance numericInstanceWithoutDataset;
    
    public ConcatenationAggregatorTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        FastVector attributes = new FastVector();
        emptyDataset = new Instances("Empty",attributes,1);
        attributes = new FastVector();
        attributes.addElement(new Attribute("Numeric"));
        numericDataset = new Instances("Numeric",attributes,1);
        
        emptyInstanceWithDataset = new Instance(0,new double[]{});
        emptyInstanceWithDataset.setDataset(emptyDataset);
        
        emptyInstanceWithoutDataset = new Instance(0,new double[]{});
        
        numericInstanceWithDataset = new Instance(1,new double[]{1.0});
        numericInstanceWithDataset.setDataset(numericDataset);
        
        numericInstanceWithDataset2 = new Instance(1,new double[]{2.0});
        numericInstanceWithDataset2.setDataset(numericDataset);
        
        attributes = new FastVector();
        attributes.addElement(new Attribute("Extra"));
        attributes.addElement(new Attribute("Xtra2"));
        extraNumericInstance = new Instance(2,new double[]{4.0,8.0});
        extraNumericInstance.setDataset(new Instances("Extra",attributes,1));
        
        numericInstanceWithoutDataset = new Instance(1,new double[]{2.0});
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Logger.getLogger("nz.ac.waikato.mcennis.rat").removeHandler(handler);
    }


    // Test the good conditions

    public void testAggregateSameDataset(){
        System.out.println("aggSameDataset");
        
        handler = new AssertionHandler();
        handler.setErrorExpected(false);
        handler.setPattern("[a&&b]");
        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);
        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);
        
        ConcatenationAggregator instance = new ConcatenationAggregator();
        Instance[] data = new Instance[]{numericInstanceWithDataset,numericInstanceWithDataset2};
        double[] weights = new double[]{1.0,1.0};
        Instance[] result = instance.aggregate(data, weights);
        assertNotNull(result);
        assertEquals(1,result.length);
        assertEquals(2,result[0].numAttributes());
        assertEquals(numericInstanceWithDataset.attribute(0),result[0].attribute(0));
        assertEquals(numericInstanceWithDataset.value(0),result[0].value(0),0.0001);
        assertEquals(numericInstanceWithDataset2.attribute(0),result[0].attribute(1));
        assertEquals(numericInstanceWithDataset2.value(0),result[0].value(1),0.0001);
        assertEquals("Unexpected logging found",false,handler.isTriggered());
    }
    
    public void testAggregateDifferingDataset(){
        System.out.println("aggDifferingDataset");
        
        handler = new AssertionHandler();
        handler.setErrorExpected(false);
        handler.setPattern("[a&&b]");
        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);
        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);
        
        ConcatenationAggregator instance = new ConcatenationAggregator();
        Instance[] data = new Instance[]{numericInstanceWithDataset,emptyInstanceWithDataset};
        double[] weights = new double[]{1.0,1.0};
        Instance[] result = instance.aggregate(data, weights);
        assertNotNull(result);
        assertEquals(1,result.length);
        assertEquals(1,result[0].numAttributes());
        assertEquals(numericInstanceWithDataset.dataset().relationName()+" Concatenated",result[0].dataset().relationName());
        assertEquals(1.0,result[0].value(0),0.0001);
        
        assertEquals("Unexpected logging found",false,handler.isTriggered());
    }
    
    public void testSingleEntry(){
        System.out.println("aggSingleEntry");
        
        handler = new AssertionHandler();
        handler.setErrorExpected(false);
        handler.setPattern("[a&&b]");
        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);
        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);
        
        ConcatenationAggregator instance = new ConcatenationAggregator();
        Instance[] data = new Instance[]{numericInstanceWithDataset};
        double[] weights = new double[]{1.0};
        Instance[] result = instance.aggregate(data, weights);
        assertNotNull(result);
        assertEquals(1,result.length);
        assertEquals(1,result[0].numAttributes());
        assertEquals(1.0,result[0].value(0));
        assertEquals(numericInstanceWithDataset.dataset().relationName()+" Concatenated",result[0].dataset().relationName());
        
        assertEquals("Unexpected logging found",false,handler.isTriggered());
    }
    
    // test all bad conditions
    public void testNullWeight() throws NullPointerException{
        System.out.println("NullWeight");
        
        handler = new AssertionHandler();
        handler.setErrorExpected(false);
        handler.setPattern(".*[Nn]ull.*");
        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);
        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);
        
        ConcatenationAggregator instance = new ConcatenationAggregator();
        Instance[] data = new Instance[]{emptyInstanceWithDataset};
        double[] weights = null;
        Instance[] result = instance.aggregate(data, weights);
        assertNotNull(result);
        assertEquals(0,result.length);
        
        assertEquals("Expected logging is missing",true,handler.isTriggered());
    }
    
    public void testNullInstance() throws NullPointerException{
        System.out.println("NullDataset");
        
        handler = new AssertionHandler();
        handler.setErrorExpected(false);
        handler.setPattern(".*[Nn]ull.*");
        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);
        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);
        
        ConcatenationAggregator instance = new ConcatenationAggregator();
        Instance[] data = null;
        double[] weights = null;
        Instance[] result = instance.aggregate(data, weights);
        assertNotNull(result);
        assertEquals(0,result.length);
        
        assertEquals("Expected logging is missing",true,handler.isTriggered());
    }
    
    public void testArrayLengthMismatch(){
        System.out.println("ArrayLengthMismatch");
        
        handler = new AssertionHandler();
        handler.setErrorExpected(false);
        handler.setPattern(".*[Ll]ength.*");
        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);
        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);
        
        ConcatenationAggregator instance = new ConcatenationAggregator();
        Instance[] data = new Instance[]{emptyInstanceWithDataset};
        double[] weights = new double[]{};
        Instance[] result = instance.aggregate(data, weights);
        assertNotNull(result);
        assertEquals(0,result.length);
        
        assertEquals("Expected logging is missing",true,handler.isTriggered());
    }
    
    public void testNoDataSet(){
        System.out.println("NoDataSet");
        
        handler = new AssertionHandler();
        handler.setErrorExpected(false);
        handler.setPattern(".*[Mm]issing a [Dd]ataset.*");
        Logger.getLogger("nz.ac.waikato.mcennis.rat").addHandler(handler);
        Logger.getLogger("nz.ac.waikato.mcennis.rat").setLevel(Level.INFO);
        
        ConcatenationAggregator instance = new ConcatenationAggregator();
        Instance[] data = new Instance[]{emptyInstanceWithoutDataset,numericInstanceWithoutDataset};
        double[] weights = new double[]{1.0,1.0};
        Instance[] result = instance.aggregate(data, weights);
        assertNotNull(result);
        assertEquals(0,result.length);
        
        assertEquals("Expected logging is missing",true,handler.isTriggered());
    }
}
