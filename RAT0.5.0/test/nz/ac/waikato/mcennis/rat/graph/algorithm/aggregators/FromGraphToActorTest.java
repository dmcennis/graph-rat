/**
 * Created Aug 11, 2008 - 8:35:39 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.aggregators;

import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;
import junit.framework.TestCase;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.MemGraph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;
import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.reusablecores.aggregator.SumAggregator;
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
public class FromGraphToActorTest extends TestCase {
    
    Graph base;
    Actor a;
    Actor b;
    Property g1;
    Property g2;
    
    public FromGraphToActorTest(String testName) {
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
        props.set("PropertyClass","weka.core.Instance");
        props.set("PropertyType","Basic");
        props.set("PropertyID","property1");
        
        a = ActorFactory.newInstance().create(props);
        base.add(a);
       
        props.set("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        base.add(b);
        
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute("Value 1"));
        Instances meta = new Instances("Test",attributes,2);
        
        g1 = PropertyFactory.newInstance().create(props);
        Instance instance = new Instance(1,new double[]{1.0});
        instance.setDataset(meta);
        g1.add(instance);
        base.add(g1);
        
        props.set("PropertyID","property2");
        g2 = PropertyFactory.newInstance().create(props);
        instance = new Instance(1,new double[]{2.0});
        instance.setDataset(meta);
        g2.add(instance);
        instance = new Instance(1,new double[]{4.0});
        instance.setDataset(meta);
        g2.add(instance);
        base.add(g2);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of execute method, of class FromGraphToActor.
     */
    public void testExecuteA() {
        System.out.println("executeA");
        FromGraphToActor instance = new FromGraphToActor();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("graphAggregator",SumAggregator.class.getName());
        props.set("actorType","User");
        instance.init(props);
        instance.execute(base);
        
        Property property = a.getProperty("GraphProperty");
        assertNotNull(property);
        List<Object> values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.size());
        Instance inst = (Instance)values.get(0);
        assertEquals(1,inst.numAttributes());
        assertEquals(7.0,inst.value(0),0.0001);        
    }

    /**
     * Test of execute method, of class FromGraphToActor.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        FromGraphToActor instance = new FromGraphToActor();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("graphAggregator",SumAggregator.class.getName());
        props.set("actorType","User");
        instance.init(props);
        instance.execute(base);
        
        Property property = b.getProperty("GraphProperty");
        assertNotNull(property);
        List<Object> values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.size());
        Instance inst = (Instance)values.get(0);
        assertEquals(1,inst.numAttributes());
        assertEquals(7.0,inst.value(0),0.0001);        
    }

}
