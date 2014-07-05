/**
 * Created Jul 30, 2008 - 5:45:22 PM
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
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import nz.ac.waikato.mcennis.rat.graph.link.Link;
import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;
import org.dynamicfactory.property.Property;
import org.dynamicfactory.property.PropertyFactory;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Daniel McEnnis
 */
public class AggregateByLinkPropertyTest extends TestCase {
    
    Graph base;
    Actor a;
    Actor b;
    Actor c;
    Actor t1;
    Actor t2;
    Actor t3;
    Actor t4;
    Link at1;
    Link at2;
    Link at3;
    Link bt2;
    Link bt3;
    Link bt4;
    Link t2c;
    
    public AggregateByLinkPropertyTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        base = new MemGraph();
        Properties props = new Properties();
        props.setProperty("ActorClass", "Basic");
        props.setProperty("ActorType","User");
        props.setProperty("PropertyClass", Instance.class.getName());
        props.setProperty("PropertyType", "Basic");
        props.setProperty("PropertyID", "Source");
        props.setProperty("LinkType", "Targets");
        props.setProperty("LinkClass","Basic");
        
        FastVector attributes = new FastVector(4);
        attributes.addElement(new Attribute("Data1"));
        attributes.addElement(new Attribute("Data2"));
        attributes.addElement(new Attribute("Data3"));
        attributes.addElement(new Attribute("Data4"));
        Instances meta = new Instances("Testing",attributes,5);
        
        props.setProperty("ActorID", "A");
        a = ActorFactory.newInstance().create(props);
        base.add(a);
        
        props.setProperty("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        base.add(b);
        
        props.setProperty("ActorID", "C");
        c = ActorFactory.newInstance().create(props);
        base.add(c);
        
        props.setProperty("ActorType","Target");
        props.setProperty("ActorID", "1");
        t1 = ActorFactory.newInstance().create(props);
        Property source = PropertyFactory.newInstance().create(props);
        Instance data = new Instance(4,new double[]{1.0,2.0,3.0,4.0});
        data.setDataset(meta);
        source.add(data);
        t1.add(source);
        base.add(t1);

        props.setProperty("ActorID", "2");
        t2 = ActorFactory.newInstance().create(props);
        source = PropertyFactory.newInstance().create(props);
        data = new Instance(4,new double[]{1.0,-2.0,3.0,-4.0});
        data.setDataset(meta);
        source.add(data);
        t2.add(source);
        base.add(t2);

        props.setProperty("ActorID", "3");
        t3 = ActorFactory.newInstance().create(props);
        source = PropertyFactory.newInstance().create(props);
        data = new Instance(4,new double[]{0.0,0.0,0.0,0.0});
        data.setDataset(meta);
        source.add(data);
        t3.add(source);
        base.add(t3);

        props.setProperty("ActorID", "4");
        t4 = ActorFactory.newInstance().create(props);
        source = PropertyFactory.newInstance().create(props);
        data = new Instance(4,new double[]{1.0,2.0,3.0,4.0});
        data.setDataset(meta);
        source.add(data);
        data = new Instance(4,new double[]{1.0,1.0,1.0,1.0});
        data.setDataset(meta);
        source.add(data);
        t4.add(source);
        base.add(t4);

        
        at1 = LinkFactory.newInstance().create(props);
        at1.set(a, 1.0, t1);
        base.add(at1);
        at2 = LinkFactory.newInstance().create(props);
        at2.set(a, 1.0, t2);
        base.add(at2);
        at3 = LinkFactory.newInstance().create(props);
        at3.set(a, 1.0, t3);
        base.add(at3);
        bt2 = LinkFactory.newInstance().create(props);
        bt2.set(b, -1.0, t2);
        base.add(bt2);
        bt3 = LinkFactory.newInstance().create(props);
        bt3.set(b, 1.0, t3);
        base.add(bt3);
        bt4 = LinkFactory.newInstance().create(props);
        bt4.set(b, 1.0, t4);
        base.add(bt4);
        t2c = LinkFactory.newInstance().create(props);
        t2c.set(t2, 1.0, c);
        base.add(t2c);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class AggregateByLink.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        AggregateByLinkProperty instance = new AggregateByLinkProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "Source");
        props.setProperty("actorType","User");
        props.setProperty("relation","Targets");
        props.setProperty("destinationProperty","Agg");
        props.setProperty("innerAggregator",SumAggregator.class.getName());
        props.setProperty("outerAggregator",SumAggregator.class.getName());
        instance.init(props);
        instance.execute(base);

        Property agg = a.getProperty("Agg");
        assertNotNull(agg);
        Object[] values = agg.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals("weka.core.Instance",values[0].getClass().getName());
        Instance data = (Instance)values[0];
        assertEquals(2.0,data.value(0));
        assertEquals(0.0,data.value(1));
        assertEquals(6.0,data.value(2));
        assertEquals(0.0,data.value(3));
    }


    /**
     * Test of execute method, of class AggregateByLink.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        AggregateByLinkProperty instance = new AggregateByLinkProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "Source");
        props.setProperty("actorType","User");
        props.setProperty("relation","Targets");
        props.setProperty("destinationProperty","Agg");
        props.setProperty("innerAggregator",SumAggregator.class.getName());
        props.setProperty("outerAggregator",SumAggregator.class.getName());
        instance.init(props);
        instance.execute(base);

        Property agg = b.getProperty("Agg");
        assertNotNull(agg);
        Object[] values = agg.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals("weka.core.Instance",values[0].getClass().getName());
        Instance data = (Instance)values[0];
        assertEquals(1.0,data.value(0));
        assertEquals(5.0,data.value(1));
        assertEquals(1.0,data.value(2));
        assertEquals(9.0,data.value(3));
    }

    /**
     * Test of execute method, of class AggregateByLink.
     */
    public void testExecuteC() {
        System.out.println("executeC");
        AggregateByLinkProperty instance = new AggregateByLinkProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "Source");
        props.setProperty("actorType","User");
        props.setProperty("relation","Targets");
        props.setProperty("destinationProperty","Agg");
        props.setProperty("innerAggregator",SumAggregator.class.getName());
        props.setProperty("outerAggregator",SumAggregator.class.getName());
        instance.init(props);
        instance.execute(base);

        Property agg = c.getProperty("Agg");
        assertNotNull(agg);
        Object[] values = agg.getValue();
        assertNotNull(values);
        assertEquals(0,values.length);
    }

    /**
     * Test of execute method, of class AggregateByLink.
     */
    public void testExecuteIncoming() {
        System.out.println("execute");
        AggregateByLinkProperty instance = new AggregateByLinkProperty();
        Properties props = new Properties();
        props.setProperty("sourceProperty", "Source");
        props.setProperty("actorType","User");
        props.setProperty("relation","Targets");
        props.setProperty("destinationProperty","Agg");
        props.setProperty("innerAggregator",SumAggregator.class.getName());
        props.setProperty("outerAggregator",SumAggregator.class.getName());
        props.setProperty("linkDirection", "Incoming");
        instance.init(props);
        instance.execute(base);

        Property agg = c.getProperty("Agg");
        assertNotNull(agg);
        Object[] values = agg.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        assertEquals("weka.core.Instance",values[0].getClass().getName());
        Instance data = (Instance)values[0];
        assertEquals(1.0,data.value(0));
        assertEquals(-2.0,data.value(1));
        assertEquals(3.0,data.value(2));
        assertEquals(-4.0,data.value(3));
    }

}
