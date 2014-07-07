/**
 * Created Aug 11, 2008 - 7:48:38 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.aggregators;

import java.util.Properties;
import junit.framework.TestCase;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.graph.MemGraph;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.mcennis.graphrat.algorithm.reusablecores.aggregator.SumAggregator;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregateByGraphTest extends TestCase {
    
    Graph base;
    Actor a;
    Actor b;
    Actor c;
    Property pA;
    Property pB;
    Property pC;
    
    public AggregateByGraphTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        base = new MemGraph();
        Properties props = new Properties();
        props.setProperty("ActorType", "User");
        props.setProperty("ActorClass", "Basic");
        props.setProperty("ActorID", "A");
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyID", "property");
        props.setProperty("PropertyClass", "weka.core.Instance");
        
        FastVector vector = new FastVector();
        vector.addElement(new Attribute("Dummy Value"));
        Instances meta = new Instances("Test",vector,4);
        
        a = ActorFactory.newInstance().create(props);
        pA = PropertyFactory.newInstance().create(props);
        Instance tmp = new Instance(1,new double[]{1.0});
        tmp.setDataset(meta);
        pA.add(tmp);
        a.add(pA);
        base.add(a);
        
        props.setProperty("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        pB = PropertyFactory.newInstance().create(props);
        tmp = new Instance(1,new double[]{2.0});
        tmp.setDataset(meta);
        pB.add(tmp);
        b.add(pB);
        base.add(b);
        
        props.setProperty("ActorID", "C");
        c = ActorFactory.newInstance().create(props);
        pC = PropertyFactory.newInstance().create(props);
        tmp = new Instance(1,new double[]{4.0});
        tmp.setDataset(meta);
        pC.add(tmp);
        tmp = new Instance(1,new double[]{8.0});
        tmp.setDataset(meta);
        pC.add(tmp);
        c.add(pC);
        base.add(c);
        
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class AggregateByGraph.
     */
    public void testExecute() {
        System.out.println("execute");
        AggregateByGraph instance = new AggregateByGraph();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("actorProperty","property");
        props.setProperty("graphProperty", "Result");
        props.setProperty("innerAggregatorFunction", SumAggregator.class.getName());
        instance.init(props);
        instance.execute(base);
        
        Property property = base.getProperty("Result");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        Instance value = (Instance)values[0];
        assertEquals(1,value.numAttributes());
        assertEquals(15.0,value.value(0),0.0001);
    }


}
