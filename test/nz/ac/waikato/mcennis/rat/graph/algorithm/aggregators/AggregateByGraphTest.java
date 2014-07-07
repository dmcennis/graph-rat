/**
 * Created Aug 11, 2008 - 7:48:38 PM
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
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;
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
        Properties props = PropertiesFactory.newInstance().create();
        props.set("ActorType", "User");
        props.set("ActorClass", "Basic");
        props.set("ActorID", "A");
        props.set("PropertyType", "Basic");
        props.set("PropertyID", "property");
        props.set("PropertyClass", "weka.core.Instance");
        
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
        
        props.set("ActorID", "B");
        b = ActorFactory.newInstance().create(props);
        pB = PropertyFactory.newInstance().create(props);
        tmp = new Instance(1,new double[]{2.0});
        tmp.setDataset(meta);
        pB.add(tmp);
        b.add(pB);
        base.add(b);
        
        props.set("ActorID", "C");
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
        AggregateToGraph instance = new AggregateToGraph();
        Properties props = PropertiesFactory.newInstance().create();
        props.set("actorType", "User");
        props.set("actorProperty","property");
        props.set("graphProperty", "Result");
        props.set("innerAggregatorFunction", SumAggregator.class.getName());
        instance.init(props);
        instance.execute(base);
        
        Property property = base.getProperty("Result");
        assertNotNull(property);
        List<Object> values = property.getValue();
        assertNotNull(values);
        assertEquals(1,values.size());
        Instance value = (Instance)values.get(0);
        assertEquals(1,value.numAttributes());
        assertEquals(15.0,value.value(0),0.0001);
    }


}
