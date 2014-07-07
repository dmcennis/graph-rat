/*
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.clustering;

import java.util.Properties;
import junit.framework.TestCase;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.graph.GraphFactory;
import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.actor.ActorFactory;
import org.dynamicfactory.descriptors.InputDescriptor;
import org.dynamicfactory.descriptors.OutputDescriptor;
import org.dynamicfactory.descriptors.SettableParameter;
import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.link.LinkFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class FindWeaklyConnectedComponentsTest extends TestCase {

        Graph base;
    Graph base2;
    
    Actor clique1A;
    Actor clique1B;
    Actor clique1C;
    Actor isolated;
    Actor group2A;
    Actor group2B;
    Actor group2C;
    Actor bridge;
    
    Link C1A_C1B;
    Link C1A_C1C;
    Link C1B_C1A;
    Link C1B_C1C;
    Link C1C_C1A;
    Link C1C_C1B;
    Link C1A_Isolated;
    Link G2A_G2B;
    Link G2B_G2C;
    Link G2C_G2A;
    Link C1B_Bridge;
    Link Bridge_G2A;
    Link C1C_G2C;
    
    FindWeaklyConnectedComponents test;

    public FindWeaklyConnectedComponentsTest(String testName) {
        super(testName);
        Properties props = new Properties();
        props.setProperty("GraphClass", "MemGraph");
        props.setProperty("GraphID","Base");
        props.setProperty("ActorClass","Basic");
        props.setProperty("ActorType","Member");
        props.setProperty("ActorID","C1A");
        props.setProperty("LinkClass", "Basic");
        props.setProperty("LinkType","IsFriend");
        
        base = GraphFactory.newInstance().create(props);
        base2 = GraphFactory.newInstance().create(props);
        
        clique1A = ActorFactory.newInstance().create(props);
        base.add(clique1A);
        
        props.setProperty("ActorID", "C1B");
        clique1B = ActorFactory.newInstance().create(props);
        base.add(clique1B);
        
        props.setProperty("ActorID", "C1C");
        clique1C = ActorFactory.newInstance().create(props);
        base.add(clique1C);
        
        props.setProperty("ActorID", "G2A");
        group2A = ActorFactory.newInstance().create(props);
        base.add(group2A);
        base2.add(group2A);
        
        props.setProperty("ActorID", "G2B");
        group2B = ActorFactory.newInstance().create(props);
        base.add(group2B);
        base2.add(group2B);
        
        props.setProperty("ActorID", "G2C");
        group2C = ActorFactory.newInstance().create(props);
        base.add(group2C);
        base2.add(group2C);
        
        props.setProperty("ActorID", "Isolated");
        isolated = ActorFactory.newInstance().create(props);
        base.add(isolated);
        
        props.setProperty("ActorID", "Bridge");
        bridge= ActorFactory.newInstance().create(props);
        base.add(bridge);
        
        C1A_C1B = LinkFactory.newInstance().create(props);
        C1A_C1B.set(clique1A, 1.0, clique1B);
        base.add(C1A_C1B);
        
        C1A_C1C = LinkFactory.newInstance().create(props);
        C1A_C1C.set(clique1A, 1.0, clique1C);
        base.add(C1A_C1C);
        
        C1B_C1A = LinkFactory.newInstance().create(props);
        C1B_C1A.set(clique1B, 1.0, clique1A);
        base.add(C1B_C1A);
        
        C1B_C1C = LinkFactory.newInstance().create(props);
        C1B_C1C.set(clique1B, 1.0, clique1C);
        base.add(C1B_C1C);
        
        C1C_C1A = LinkFactory.newInstance().create(props);
        C1C_C1A.set(clique1C, 1.0, clique1A);
        base.add(C1C_C1A);
        
        C1C_C1B = LinkFactory.newInstance().create(props);
        C1C_C1B.set(clique1C, 1.0, clique1B);
        base.add(C1C_C1B);
        
        C1A_Isolated = LinkFactory.newInstance().create(props);
        C1A_Isolated.set(clique1A, 1.0, isolated);
        base.add(C1A_Isolated);
        
        G2A_G2B = LinkFactory.newInstance().create(props);
        G2A_G2B.set(group2A, 1.0, group2B);
        base.add(G2A_G2B);
        base2.add(G2A_G2B);
        
        G2B_G2C = LinkFactory.newInstance().create(props);
        G2B_G2C.set(group2B, 1.0, group2C);
        base.add(G2B_G2C);
        base2.add(G2B_G2C);
        
        G2C_G2A = LinkFactory.newInstance().create(props);
        G2C_G2A.set(group2C, 1.0, group2A);
        base.add(G2C_G2A);
        base2.add(G2C_G2A);
        
        Bridge_G2A = LinkFactory.newInstance().create(props);
        Bridge_G2A.set(bridge, 1.0, group2A);
        base.add(Bridge_G2A);
        
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testExecuteNoActorLinks(){
        System.out.println("execute");
        FindWeaklyConnectedComponents instance = new FindWeaklyConnectedComponents();
        instance.execute(base);
        assertNull(base.getChildren());
    }
    /**
     * Test of execute method, of class FindWeaklyConnectedComponents.
     */
    public void testExecuteOneComponent() {
        System.out.println("execute");
        FindWeaklyConnectedComponents instance = new FindWeaklyConnectedComponents();
        Properties props = new Properties();
        props.setProperty("actorType", "Member");
        props.setProperty("relation","IsFriend");
        instance.init(props);
        instance.execute(base2);
        assertNull(base2.getChildren());
    }

    public void testExecuteTwoComponents() {
        System.out.println("execute");
        FindWeaklyConnectedComponents instance = new FindWeaklyConnectedComponents();
        Properties props = new Properties();
        props.setProperty("actorType", "Member");
        props.setProperty("relation","IsFriend");
        instance.init(props);
        instance.execute(base);
        Graph[] graphs = base.getChildren();
        assertNotNull(graphs);
        assertEquals(2,graphs.length);
        for(int i=0;i<graphs.length;++i){
            if(graphs[i].getActor("Member", "C1A")!= null){
                assertEquals(4,graphs[i].getActor().length);
            }else if(graphs[i].getActor("Member","G2A")!= null){
                assertEquals(4,graphs[i].getActor().length);
            }else{
                fail("Unknown subgraph");
            }
        }
    }


}
