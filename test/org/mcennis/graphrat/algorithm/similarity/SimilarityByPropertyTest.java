/**
 * Created Jul 30, 2008 - 4:04:59 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.similarity;

import java.util.List;
import java.util.SortedSet;

import junit.framework.TestCase;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.algorithm.reusablecores.datavector.DoubleArrayDataVector;
import org.mcennis.graphrat.algorithm.reusablecores.distance.CosineDistance;
import org.mcennis.graphrat.link.Link;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;

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
        Properties props = PropertiesFactory.newInstance().create();
        props.set("Graph", "MemGraph");
        props.set("GraphID","Test");
        graph = GraphFactory.newInstance().create(props);
        
        props.set("ActorType", "User");
        props.set("PropertyClass", (new double[]{}).getClass().getName());
        props.set("PropertyType", "Basic");
        props.set("PropertyID","Array");
        
        props.set("ActorID", "A");
        a = ActorFactory.newInstance().create(props);
        arrayProperty = PropertyFactory.newInstance().create(props);
        arrayProperty.add(new double[]{1.0,1.0,1.0,1.0,0.0,0.0,0.0,0.0});
        a.add(arrayProperty);
        graph.add(a);
        
        
        props.set("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        arrayProperty = PropertyFactory.newInstance().create(props);
        arrayProperty.add(new double[]{1.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0});
        b.add(arrayProperty);
        graph.add(b);
        
        
        props.set("ActorID", "C");
        c = ActorFactory.newInstance().create(props);
        arrayProperty = PropertyFactory.newInstance().create(props);
        arrayProperty.add(new double[]{0.0,0.0,0.0,0.0,1.0,1.0,1.0,0.0});
        c.add(arrayProperty);
        graph.add(c);
        
        
        props.set("ActorID", "D");
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
        Properties props = PropertiesFactory.newInstance().create();
        props.set("actorType", "User");
        props.set("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        SortedSet<Link> link = graph.getLinkBySource("Similarity",a);
        assertNotNull(link);
        assertEquals(1,link.size());
        assertEquals(b,link.first().getDestination());
        double[] left = (double[])a.getProperty("Array").getValue().get(0);
        double[] right = (double[])b.getProperty("Array").getValue().get(0);
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link.first().getStrength(),0.001);
    }

    public void testExecuteB() {
        System.out.println("executeB");
        SimilarityByProperty instance = new SimilarityByProperty();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("actorType", "User");
        props.set("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        SortedSet<Link> link = graph.getLinkBySource("Similarity",b);
        assertNotNull(link);
        assertEquals(1,link.size());
        assertEquals(a,link.first().getDestination());
        double[] left = (double[])b.getProperty("Array").getValue().get(0);
        double[] right = (double[])a.getProperty("Array").getValue().get(0);
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link.first().getStrength(),0.001);
    }

    public void testExecuteC() {
        System.out.println("executeA");
        SimilarityByProperty instance = new SimilarityByProperty();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("actorType", "User");
        props.set("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        SortedSet<Link> link = graph.getLinkBySource("Similarity",c);
        assertNotNull(link);
        assertEquals(1,link.size());
        assertEquals(d,link.first().getDestination());
        double[] left = (double[])c.getProperty("Array").getValue().get(0);
        double[] right = (double[])d.getProperty("Array").getValue().get(0);
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link.first().getStrength(),0.001);
    }

    public void testExecuteD() {
        System.out.println("executeD");
        SimilarityByProperty instance = new SimilarityByProperty();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("actorType", "User");
        props.set("property", "Array");
        instance.init(props);
        instance.execute(graph);
        
        SortedSet<Link> link = graph.getLinkBySource("Similarity",d);
        assertNotNull(link);
        assertEquals(1,link.size());
        assertEquals(c,link.first().getDestination());
        double[] left = (double[])d.getProperty("Array").getValue().get(0);
        double[] right = (double[])c.getProperty("Array").getValue().get(0);
        double value = distance.distance(new DoubleArrayDataVector(left), new DoubleArrayDataVector(right));
        assertEquals(value,link.first().getStrength(),0.001);
    }


}
