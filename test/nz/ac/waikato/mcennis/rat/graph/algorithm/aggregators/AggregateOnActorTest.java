/**
 * Created Jul 31, 2008 - 12:47:55 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.List;

import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.SumAggregator;
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
        Properties props = PropertiesFactory.newInstance().create();
        props.set("ActorClass","Basic");
        props.set("ActorType","User");
        props.set("ActorID","A");
        props.set("PropertyType", "Basic");
        props.set("PropertyClass","weka.core.Instance");
        props.set("PropertyID", "a1");

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

        props.set("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        props.set("PropertyID", "b1");
        b1 = PropertyFactory.newInstance().create(props);
        data = new Instance(2,new double[]{1.0,1.0});
        data.setDataset(meta);
        b1.add(data);
        b.add(b1);
        props.set("PropertyID", "b2");
        b2 = PropertyFactory.newInstance().create(props);
        data = new Instance(2,new double[]{2.0,2.0});
        data.setDataset(meta);
        b2.add(data);
        b.add(b2);
        base.add(b);
        
        props.set("ActorID", "C");
        c = ActorFactory.newInstance().create(props);
        props.set("PropertyID", "c1");
        c1 = PropertyFactory.newInstance().create(props);
        data = new Instance(2,new double[]{1.0,1.0});
        data.setDataset(meta);
        c1.add(data);
        data = new Instance(2,new double[]{2.0,2.0});
        data.setDataset(meta);
        c1.add(data);
        c.add(c1);
        base.add(c);
        
        props.set("ActorID", "D");
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
        Properties props = PropertiesFactory.newInstance().create();
        props.set("innerAggregator", SumAggregator.class.getName());
        props.set("outerAggregator", SumAggregator.class.getName());
        props.set("actorType", "User");
        instance.init(props);
        instance.execute(base);
        
        Property p = a.getProperty("Actor Instance");
        assertNotNull(p);
        List<Object> values = p.getValue();
        assertNotNull(values);
        assertEquals(1,values.size());
        Instance inst = (Instance)values.get(0);
        assertEquals(1.0,inst.value(0),0.001);
        assertEquals(1.0,inst.value(1),0.001);
    }

    /**
     * Test of execute method, of class AggregateOnActor.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        AggregateOnActor instance = new AggregateOnActor();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("innerAggregator", SumAggregator.class.getName());
        props.set("outerAggregator", SumAggregator.class.getName());
        props.set("actorType", "User");
        instance.init(props);
        instance.execute(base);
        
        Property p = b.getProperty("Actor Instance");
        assertNotNull(p);
        List<Object> values = p.getValue();
        assertNotNull(values);
        assertEquals(1,values.size());
        Instance inst = (Instance)values.get(0);
        assertEquals(3.0,inst.value(0),0.001);
        assertEquals(3.0,inst.value(1),0.001);
    }

    /**
     * Test of execute method, of class AggregateOnActor.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        AggregateOnActor instance = new AggregateOnActor();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("innerAggregator", SumAggregator.class.getName());
        props.set("outerAggregator", SumAggregator.class.getName());
        props.set("actorType", "User");
        instance.init(props);
        instance.execute(base);
        
        Property p = c.getProperty("Actor Instance");
        assertNotNull(p);
        List<Object> values = p.getValue();
        assertNotNull(values);
        assertEquals(1,values.size());
        Instance inst = (Instance)values.get(0);
        assertEquals(3.0,inst.value(0),0.001);
        assertEquals(3.0,inst.value(1),0.001);
    }

    /**
     * Test of execute method, of class AggregateOnActor.
     */
    public void testExecuteD() {
        System.out.println("executeD");
        AggregateOnActor instance = new AggregateOnActor();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("actorType", "User");
        props.set("innerAggregator", SumAggregator.class.getName());
        props.set("outerAggregator", SumAggregator.class.getName());
        instance.init(props);
        instance.execute(base);
        
        Property p = d.getProperty("Actor Instance");
        assertNotNull(p);
        List<Object> values = p.getValue();
        assertNotNull(values);
        assertEquals(0,values.size());
    }


}
