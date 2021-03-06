/**
 * Created Jul 30, 2008 - 4:04:59 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.similarity;

import java.util.Properties;
import junit.framework.TestCase;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.algorithm.reusablecores.datavector.DoubleArrayDataVector;
import org.mcennis.graphrat.algorithm.reusablecores.distance.CosineDistance;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.link.Link;

/**
 *
 * @author Daniel McEnnis
 */
public class SimilarityByPropertyTest extends TestCase {
    
    CosineDistance distance = new CosineDistance();
    Graph graph;
    Actor a;
    Actor b;
    Actor c;
    Actor d;
    Property arrayProperty;
    
    
    public SimilarityByPropertyTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Properties props = new Properties();
        props.setProperty("Graph", "MemGraph");
        props.setProperty("GraphID","Test");
        graph = GraphFactory.newInstance().create(props);
        
        props.setProperty("ActorType", "User");
        props.setProperty("PropertyClass", (new double[]{}).getClass().getName());
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyID","Array");
        
        props.setProperty("ActorID", "A");
        a = ActorFactory.newInstance().create(props);
        arrayProperty = PropertyFactory.newInstance().create(props);
        arrayProperty.add(new double[]{1.0,1.0,1.0,1.0,0.0,0.0,0.0,0.0});
        a.add(arrayProperty);
        graph.add(a);
        
        
        props.setProperty("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        arrayProperty = PropertyFactory.newInstance().create(props);
        arrayProperty.add(new double[]{1.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0});
        b.add(arrayProperty);
        graph.add(b);
        
        
        props.setProperty("ActorID", "C");
        c = ActorFactory.newInstance().create(props);
        arrayProperty = PropertyFactory.newInstance().create(props);
        arrayProperty.add(new double[]{0.0,0.0,0.0,0.0,1.0,1.0,1.0,0.0});
        c.add(arrayProperty);
        graph.add(c);
        
        
        props.setProperty("ActorID", "D");
        d = ActorFactory.newInstance().create(props);
        arrayProperty = PropertyFactory.newInstance().create(props);
        arrayProperty.add(new double[]{0.0,0.0,0.0,0.0,1.0,1.0,1.0,1.0});
        d.add(arrayProperty);
        graph.add(d);
        
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class SimilarityByProperty.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        SimilarityByProperty instance = new SimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Similarity",a);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(b,link[0].getDestination());
        double[] left = (double[])a.getProperty("Array").getValue()[0];
        double[] right = (double[])b.getProperty("Array").getValue()[0];
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link[0].getStrength(),0.001);
    }

    public void testExecuteB() {
        System.out.println("executeB");
        SimilarityByProperty instance = new SimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Similarity",b);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(a,link[0].getDestination());
        double[] left = (double[])b.getProperty("Array").getValue()[0];
        double[] right = (double[])a.getProperty("Array").getValue()[0];
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link[0].getStrength(),0.001);
    }

    public void testExecuteC() {
        System.out.println("executeA");
        SimilarityByProperty instance = new SimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Similarity",c);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(d,link[0].getDestination());
        double[] left = (double[])c.getProperty("Array").getValue()[0];
        double[] right = (double[])d.getProperty("Array").getValue()[0];
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link[0].getStrength(),0.001);
    }

    public void testExecuteD() {
        System.out.println("executeD");
        SimilarityByProperty instance = new SimilarityByProperty();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        Link[] link = graph.getLinkBySource("Similarity",d);
        assertNotNull(link);
        assertEquals(1,link.length);
        assertEquals(c,link[0].getDestination());
        double[] left = (double[])d.getProperty("Array").getValue()[0];
        double[] right = (double[])c.getProperty("Array").getValue()[0];
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link[0].getStrength(),0.001);
    }


}
