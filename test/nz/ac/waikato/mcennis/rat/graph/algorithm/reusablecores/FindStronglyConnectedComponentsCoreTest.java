/*

 * Copyright Daniel McEnnis, se license.txt

 */



package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores;



import java.util.List;
import nz.ac.waikato.mcennis.rat.reusablecores.FindStronglyConnectedComponentsCore;

import org.dynamicfactory.descriptors.Properties;

import junit.framework.TestCase;

import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.GraphFactory;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

import org.dynamicfactory.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;



/**

 *

 * @author Daniel McEnnis

 */

public class FindStronglyConnectedComponentsCoreTest extends TestCase {

    

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

    

    FindStronglyConnectedComponentsCore test;

    public FindStronglyConnectedComponentsCoreTest(String testName) {

        super(testName);

    }            



    @Override

    protected void setUp() throws Exception {

        super.setUp();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("GraphClass", "MemGraph");

        props.set("GraphID","Base");

        props.set("ActorClass","Basic");

        props.set("ActorType","Member");

        props.set("ActorID","C1A");

        props.set("LinkClass", "Basic");

        props.set("LinkType","IsFriend");

        

        base = GraphFactory.newInstance().create(props);

        base2 = GraphFactory.newInstance().create(props);

        

        clique1A = ActorFactory.newInstance().create(props);

        base.add(clique1A);

        

        props.set("ActorID", "C1B");

        clique1B = ActorFactory.newInstance().create(props);

        base.add(clique1B);

        

        props.set("ActorID", "C1C");

        clique1C = ActorFactory.newInstance().create(props);

        base.add(clique1C);

        

        props.set("ActorID", "G2A");

        group2A = ActorFactory.newInstance().create(props);

        base.add(group2A);

        base2.add(group2A);

        

        props.set("ActorID", "G2B");

        group2B = ActorFactory.newInstance().create(props);

        base.add(group2B);

        base2.add(group2B);

        

        props.set("ActorID", "G2C");

        group2C = ActorFactory.newInstance().create(props);

        base.add(group2C);

        base2.add(group2C);

        

        props.set("ActorID", "Isolated");

        isolated = ActorFactory.newInstance().create(props);

        base.add(isolated);

        

        props.set("ActorID", "Bridge");

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

        

        C1B_Bridge = LinkFactory.newInstance().create(props);

        C1B_Bridge.set(clique1B, 1.0, bridge);

        base.add(C1B_Bridge);

        

        Bridge_G2A = LinkFactory.newInstance().create(props);

        Bridge_G2A.set(bridge, 1.0, group2A);

        base.add(Bridge_G2A);

        

        C1C_G2C = LinkFactory.newInstance().create(props);

        C1C_G2C.set(clique1C, 1.0, group2C);

        base.add(C1C_G2C);

        test = new FindStronglyConnectedComponentsCore();

    }



    @Override

    protected void tearDown() throws Exception {

        super.tearDown();

    }



    /**

     * Test of execute method, of class FindStronglyConnectedComponentsCore.

     */

    public void testExecute() {

        test.execute(base);

    }



    /**

     * Test of setRelation method, of class FindStronglyConnectedComponentsCore.

     */

    public void testSetRelation() {

        String relation = "Relation";

        test.setRelation(relation);

    }



    /**

     * Test of setMode method, of class FindStronglyConnectedComponentsCore.

     */

    public void testSetMode() {

        test.setMode("Mode");

    }



    /**

     * Test of getRelation method, of class FindStronglyConnectedComponentsCore.

     */

    public void testGetRelation() {

        assertEquals("Knows",test.getRelation());

        test.setRelation("Relation");

        assertEquals("Relation",test.getRelation());

    }



    /**

     * Test of getMode method, of class FindStronglyConnectedComponentsCore.

     */

    public void testGetMode() {

        assertEquals("User",test.getMode());

        test.setMode("Mode");

        assertEquals("Mode",test.getMode());

    }



    /**

     * Test of getGraph method, of class FindStronglyConnectedComponentsCore.

     */

    public void testGetGraphBeforeExecute() {

        assertNull(test.getGraph());

    }



    /**

     * Test of getGraph method, of class FindStronglyConnectedComponentsCore.

     */

    public void testGetGraphSingleComponent() {

        test.execute(base2);

        assertNull(test.getGraph());

    }



    /**

     * Test of getGraph method, of class FindStronglyConnectedComponentsCore.

     */

    public void testGetGraph() {

        test.setMode("Member");

        test.setRelation("IsFriend");

        test.execute(base);

        Graph[] graph = test.getGraph();

        assertNotNull(graph);

        assertEquals(4,graph.length);

        for(int i=0;i<graph.length;++i){

            System.out.println("Testing graph "+i);

            if(graph[i].getActor("Member", "C1A") != null){

                List<Actor> actors = graph[i].getActor("Member");

                assertNotNull(actors);

                assertEquals(3,actors.size());

                List<Link> links = graph[i].getLink("IsFriend");

                assertNotNull(links);

                assertEquals(6,links.size());

            }else if(graph[i].getActor("Member","G2A") != null){

                List<Actor> actors = graph[i].getActor("Member");

                assertNotNull(actors);

                assertEquals(3,actors.size());

                List<Link> links = graph[i].getLink("IsFriend");

                assertNotNull(links);

                assertEquals(3,links.size());

            }else if(graph[i].getActor("Member","Bridge") != null){

                List<Actor> actors = graph[i].getActor("Member");

                assertNotNull(actors);

                assertEquals(1,actors.size());

                List<Link> links = graph[i].getLink("IsFriend");

                assertNull(links);

            }else if(graph[i].getActor("Member","Isolated") != null){

                List<Actor> actors = graph[i].getActor("Member");

                assertNotNull(actors);

                assertEquals(1,actors.size());

                List<Link> links = graph[i].getLink("IsFriend");

                assertNull(links);

            }else{

                fail("Unexpected group detected");

            }

        }

    }



    /**

     * Test of setGraphPrefix method, of class FindStronglyConnectedComponentsCore.

     */

    public void testSetGraphPrefix() {

        test.setGraphPrefix("Prefix ");

    }



    /**

     * Test of getGraphPrefix method, of class FindStronglyConnectedComponentsCore.

     */

    public void testGetGraphPrefix() {

        assertEquals("Component ",test.getGraphPrefix());

        test.setGraphPrefix("Prefix ");

        assertEquals("Prefix ",test.getGraphPrefix());

    }



    /**

     * Test of setGraphClass method, of class FindStronglyConnectedComponentsCore.

     */

    public void testSetGraphClass() {

        test.setGraphClass("DBDerby");

    }



    /**

     * Test of getGraphClass method, of class FindStronglyConnectedComponentsCore.

     */

    public void testGetGraphClass() {

        assertEquals("MemGraph",test.getGraphClass());

        test.setGraphClass("DBDerby");

        assertEquals("DBDerby",test.getGraphClass());

    }



}

