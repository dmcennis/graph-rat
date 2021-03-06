/**
 * Created Jul 30, 2008 - 4:56:34 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.similarity;

import java.util.Properties;
import junit.framework.TestCase;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.graph.MemGraph;
import org.mcennis.graphrat.algorithm.reusablecores.datavector.DoubleArrayDataVector;
import org.mcennis.graphrat.algorithm.reusablecores.distance.CosineDistance;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.util.Duples;

/**
 *
 * @author Daniel McEnnis
 */
public class GraphSimilarityByPropertyTest extends TestCase {
    
    CosineDistance distance = new CosineDistance();
    Graph base;
    Graph child1;
    Graph child2;
    Graph child3;
    Graph blacksheep;
    Property array;
    
    public GraphSimilarityByPropertyTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        base = new MemGraph();
        base.setID("Base");
        
        child1 = new MemGraph();
        child1.setID("child1");
        Properties props = new Properties();
        props.setProperty("PropertyClass", (new double[]{}).getClass().getName());
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyID", "ID");
        array = PropertyFactory.newInstance().create(props);
        array.add(new double[]{1.0,1.0,0.0,0.0});
        child1.add(array);
        base.addChild(child1);
        
        child2 = new MemGraph();
        child2.setID("child2");
        props = new Properties();
        props.setProperty("PropertyClass", (new double[]{}).getClass().getName());
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyID", "ID");
        array = PropertyFactory.newInstance().create(props);
        array.add(new double[]{1.0,1.0,0.0,0.0});
        child2.add(array);
        base.addChild(child2);
        
        child3 = new MemGraph();
        child3.setID("child3");
        props = new Properties();
        props.setProperty("PropertyClass", (new double[]{}).getClass().getName());
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyID", "ID");
        array = PropertyFactory.newInstance().create(props);
        array.add(new double[]{0.0,0.0,1.0,1.0});
        child3.add(array);
        base.addChild(child3);
        
        blacksheep = new MemGraph();
        blacksheep.setID("blacksheep");
        props = new Properties();
        props.setProperty("PropertyClass", (new double[]{}).getClass().getName());
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyID", "ID");
        array = PropertyFactory.newInstance().create(props);
        array.add(new double[]{0.0,0.0,0.0,0.0});
        blacksheep.add(array);
        base.addChild(blacksheep);
        

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class GraphSimilarityByProperty.
     */
    public void testExecuteChild1() {
        System.out.println("executeChild1");
        GraphSimilarityByProperty instance = new GraphSimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "ID");
        props.setProperty("destinationProperty", "Data");
        instance.init(props);
        instance.execute(base);
        
        Property data = child1.getProperty("Data");
        assertNotNull(data);
        Object[] dataValue = data.getValue();
        assertNotNull(dataValue);
        assertEquals(1,dataValue.length);
        Duples<String,Double> duple= (Duples<String,Double>)dataValue[0];
        assertTrue("child2".contentEquals(duple.getLeft()));
        double[] left = (double[])child1.getProperty("ID").getValue()[0];
        double[] right = (double[])child2.getProperty("ID").getValue()[0];
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,duple.getRight(),0.001);
    }

    /**
     * Test of execute method, of class GraphSimilarityByProperty.
     */
    public void testExecuteChild2() {
        System.out.println("executeChild2");
        GraphSimilarityByProperty instance = new GraphSimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "ID");
        props.setProperty("destinationProperty", "Data");
        instance.init(props);
        instance.execute(base);
        
        Property data = child2.getProperty("Data");
        assertNotNull(data);
        Object[] dataValue = data.getValue();
        assertNotNull(dataValue);
        assertEquals(1,dataValue.length);
        Duples<String,Double> duple= (Duples<String,Double>)dataValue[0];
        assertTrue("child1".contentEquals(duple.getLeft()));
        double[] left = (double[])child2.getProperty("ID").getValue()[0];
        double[] right = (double[])child1.getProperty("ID").getValue()[0];
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,duple.getRight(),0.001);
    }
    /**
     * Test of execute method, of class GraphSimilarityByProperty.
     */
    public void testExecuteChild3() {
        System.out.println("executeChild3");
        GraphSimilarityByProperty instance = new GraphSimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "ID");
        props.setProperty("destinationProperty", "Data");
        instance.init(props);
        instance.execute(base);
        
        Property data = child3.getProperty("Data");
        assertNull(data);
    }
    /**
     * Test of execute method, of class GraphSimilarityByProperty.
     */
    public void testExecuteBlacksheep() {
        System.out.println("executeblacksheep");
        GraphSimilarityByProperty instance = new GraphSimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "ID");
        props.setProperty("destinationProperty", "Data");
        instance.init(props);
        instance.execute(base);
        
        Property data = blacksheep.getProperty("Data");
        assertNull(data);
    }

    /**
     * Test of execute method, of class GraphSimilarityByProperty.
     */
    public void testExecuteByPattern() {
        System.out.println("execute");
        GraphSimilarityByProperty instance = new GraphSimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "ID");
        props.setProperty("destinationProperty", "Data");
        props.setProperty("byChildren", "false");
        props.setProperty("pattern", "child.*");
        instance.init(props);
        instance.execute(base);
        
        Property data = child1.getProperty("Data");
        assertNotNull(data);
        Object[] dataValue = data.getValue();
        assertNotNull(dataValue);
        assertEquals(1,dataValue.length);
        Duples<String,Double> duple= (Duples<String,Double>)dataValue[0];
        assertTrue("child2".contentEquals(duple.getLeft()));
        double[] left = (double[])child1.getProperty("ID").getValue()[0];
        double[] right = (double[])child2.getProperty("ID").getValue()[0];
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,duple.getRight(),0.001);
    }
}
