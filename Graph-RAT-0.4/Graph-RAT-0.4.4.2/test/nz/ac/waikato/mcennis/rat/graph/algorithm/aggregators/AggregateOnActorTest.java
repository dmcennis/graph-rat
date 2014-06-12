/**
 * Created Jul 31, 2008 - 12:47:55 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.aggregator.SumAggregator;
import nz.ac.waikato.mcennis.rat.graph.descriptors.InputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.OutputDescriptor;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Parameter;
import nz.ac.waikato.mcennis.rat.graph.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregateOnActorTest extends TestCase {
    
    Graph base;
    Actor a;
    Actor b;
    Actor c;
    Actor d;
    Property a1;
    Property b1;
    Property b2;
    Property c1;

    
    public AggregateOnActorTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        base = new MemGraph();
        Properties props = new Properties();
        props.setProperty("ActorClass","Basic");
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","A");
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyClass","weka.core.Instance");
        props.setProperty("PropertyID", "a1");

        FastVector attributes = new FastVector(2);
        attributes.addElement(new Attribute("Data1"));
        attributes.addElement(new Attribute("Data2"));
        Instances meta = new Instances("Test",attributes,3);
        
        
        a = ActorFactory.newInstance().create(props);
        a1 = PropertyFactory.newInstance().create(props);
        Instance data = new Instance(2,new double[]{1.0,1.0});
        data.setDataset(meta);
        a1.add(data);
        a.add(a1);
        base.add(a);

        props.setProperty("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        props.setProperty("PropertyID", "b1");
        b1 = PropertyFactory.newInstance().create(props);
        data = new Instance(2,new double[]{1.0,1.0});
        data.setDataset(meta);
        b1.add(data);
        b.add(b1);
        props.setProperty("PropertyID", "b2");
        b2 = PropertyFactory.newInstance().create(props);
        data = new Instance(2,new double[]{2.0,2.0});
        data.setDataset(meta);
        b2.add(data);
        b.add(b2);
        base.add(b);
        
        props.setProperty("ActorID", "C");
        c = ActorFactory.newInstance().create(props);
        props.setProperty("PropertyID", "c1");
        c1 = PropertyFactory.newInstance().create(props);
        data = new Instance(2,new double[]{1.0,1.0});
        data.setDataset(meta);
        c1.add(data);
        data = new Instance(2,new double[]{2.0,2.0});
        data.setDataset(meta);
        c1.add(data);
        c.add(c1);
        base.add(c);
        
        props.setProperty("ActorID", "D");
        d= ActorFactory.newInstance().create(props);
        base.add(d);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class AggregateOnActor.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        AggregateOnActor instance = new AggregateOnActor();
        Properties props = new Properties();
        props.setProperty("innerAggregator", SumAggregator.class.getName());
        props.setProperty("outerAggregator", SumAggregator.class.getName());
        props.setProperty("actorType", "User");
        instance.init(props);
        instance.execute(base);
        
        Property p = a.getProperty("Actor Instance");
        assertNotNull(p);
        Object[] values = p.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        Instance inst = (Instance)values[0];
        assertEquals(1.0,inst.value(0),0.001);
        assertEquals(1.0,inst.value(1),0.001);
    }

    /**
     * Test of execute method, of class AggregateOnActor.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        AggregateOnActor instance = new AggregateOnActor();
        Properties props = new Properties();
        props.setProperty("innerAggregator", SumAggregator.class.getName());
        props.setProperty("outerAggregator", SumAggregator.class.getName());
        props.setProperty("actorType", "User");
        instance.init(props);
        instance.execute(base);
        
        Property p = b.getProperty("Actor Instance");
        assertNotNull(p);
        Object[] values = p.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        Instance inst = (Instance)values[0];
        assertEquals(3.0,inst.value(0),0.001);
        assertEquals(3.0,inst.value(1),0.001);
    }

    /**
     * Test of execute method, of class AggregateOnActor.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        AggregateOnActor instance = new AggregateOnActor();
        Properties props = new Properties();
        props.setProperty("innerAggregator", SumAggregator.class.getName());
        props.setProperty("outerAggregator", SumAggregator.class.getName());
        props.setProperty("actorType", "User");
        instance.init(props);
        instance.execute(base);
        
        Property p = c.getProperty("Actor Instance");
        assertNotNull(p);
        Object[] values = p.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        Instance inst = (Instance)values[0];
        assertEquals(3.0,inst.value(0),0.001);
        assertEquals(3.0,inst.value(1),0.001);
    }

    /**
     * Test of execute method, of class AggregateOnActor.
     */
    public void testExecuteD() {
        System.out.println("executeD");
        AggregateOnActor instance = new AggregateOnActor();
        Properties props = new Properties();
        props.setProperty("actorType", "User");
        props.setProperty("innerAggregator", SumAggregator.class.getName());
        props.setProperty("outerAggregator", SumAggregator.class.getName());
        instance.init(props);
        instance.execute(base);
        
        Property p = d.getProperty("Actor Instance");
        assertNotNull(p);
        Object[] values = p.getValue();
        assertNotNull(values);
        assertEquals(0,values.length);
    }


}
