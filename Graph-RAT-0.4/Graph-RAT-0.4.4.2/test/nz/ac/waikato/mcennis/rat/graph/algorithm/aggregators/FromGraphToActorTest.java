/**
 * Created Aug 11, 2008 - 8:35:39 PM
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
        Properties props = new Properties();
        props.setProperty("ActorClass","Basic");
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","A");
        props.setProperty("PropertyClass","weka.core.Instance");
        props.setProperty("PropertyType","Basic");
        props.setProperty("PropertyID","property1");
        
        a = ActorFactory.newInstance().create(props);
        base.add(a);
       
        props.setProperty("ActorID", "B");
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
        
        props.setProperty("PropertyID","property2");
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
        Properties props = new Properties();
        props.setProperty("graphAggregator",SumAggregator.class.getName());
        props.setProperty("actorType","User");
        instance.init(props);
        instance.execute(base);
        
        Property property = a.getProperty("GraphProperty");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        Instance inst = (Instance)values[0];
        assertEquals(1,inst.numAttributes());
        assertEquals(7.0,inst.value(0),0.0001);        
    }

    /**
     * Test of execute method, of class FromGraphToActor.
     */
    public void testExecuteB() {
        System.out.println("executeB");
        FromGraphToActor instance = new FromGraphToActor();
        Properties props = new Properties();
        props.setProperty("graphAggregator",SumAggregator.class.getName());
        props.setProperty("actorType","User");
        instance.init(props);
        instance.execute(base);
        
        Property property = b.getProperty("GraphProperty");
        assertNotNull(property);
        Object[] values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.length);
        Instance inst = (Instance)values[0];
        assertEquals(1,inst.numAttributes());
        assertEquals(7.0,inst.value(0),0.0001);        
    }

}
