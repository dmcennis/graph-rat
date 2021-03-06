/*

 * Copyright Daniel McEnnis, see license.txt

 */



package org.mcennis.graphrat.algorithm.clustering;



import java.util.List;
import java.util.SortedSet;

import junit.framework.TestCase;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.descriptors.PropertiesFactory;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.graph.GraphFactory;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.actor.ActorFactory;

import org.mcennis.graphrat.link.Link;

import org.mcennis.graphrat.link.LinkFactory;



/**

 *

 * @author Daniel McEnnis

 */

public class TraditionalEdgeBetweenessClusteringTest extends TestCase {

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

        

    public TraditionalEdgeBetweenessClusteringTest(String testName) {

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

    }



    @Override

    protected void tearDown() throws Exception {

        super.tearDown();

    }

    

     /**

     * Test of execute method, of class TraditionalEdgeBetweenessClustering.

     */

    public void testSimpleExecute() {

        System.out.println("executeCOmplicated");

        TraditionalEdgeBetweenessClustering instance = new TraditionalEdgeBetweenessClustering();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("actorType", "Member");

        props.set("relation","IsFriend");

        instance.init(props);

        instance.execute(base2);

       

        SortedSet<Graph> firstLevel = base2.getChildren();

        assertNotNull("First level of graph children is null.",firstLevel);

        assertEquals(3,firstLevel.size());

        for(Graph i : firstLevel){

            if(i.getActor("Member","G2A")!=null){

                assertEquals(1,firstLevel.first().getActor().size());

            }else if(firstLevel.first().getActor("Member","G2B")!=null){

                assertEquals(1,firstLevel.first().getActor().size());

            }else if(firstLevel.first().getActor("Member","G2C")!=null){

                assertEquals(1,firstLevel.first().getActor().size());

            }else{

                fail("Unexpected Graph encountered");

            }

        }

    }   



    /**

     * Test of execute method, of class TraditionalEdgeBetweenessClustering.

     */

    public void testComplicatedExecute() {

        System.out.println("executeCOmplicated");

        TraditionalEdgeBetweenessClustering instance = new TraditionalEdgeBetweenessClustering();

        Properties props = PropertiesFactory.newInstance().create();

        props.set("actorType", "Member");

        props.set("relation","IsFriend");

        instance.init(props);

        instance.execute(base);

       

        SortedSet<Graph> firstLevel = base.getChildren();

        assertNotNull("First level of graph children is null.",firstLevel);

        assertEquals(4,firstLevel.size());

        for(Graph i : firstLevel){

            if(i.getActor("Member","Isolated")!=null){

                assertEquals(1,i.getActor().size());

            }else if(i.getActor("Member","Bridge")!=null){

                assertEquals(1,i.getActor().size());

            }else if(i.getActor("Member","C1A")!=null){

                assertEquals(3,i.getActor().size());

                SortedSet<Graph> secondLevel = i.getChildren();

                assertNotNull("Second Level of clique is null",secondLevel);

                assertEquals("Expected second level of clique to be length 2, was length "+secondLevel.size(),2,secondLevel.size());

                for(Graph j : secondLevel){

                    if(j.getActor("Member", "C1A")!=null){

                        assertEquals(2,j.getActor().size());

                    }else if(j.getActor("Member", "C1B")!=null){

                        assertEquals(1,j.getActor().size());

                    }else{

                        fail("Unexpected graph at clique second level");

                    }

                }

            }else if(i.getActor("Member","G2A")!=null){

                assertEquals(3,i.getActor().size());

                SortedSet<Graph> secondLevel = i.getChildren();

                assertNotNull("Second Level of cycle is null",secondLevel);

                assertEquals("Expected second level of cycle to be length 3, was length "+secondLevel.size(),3,secondLevel.size());

            }

        }

    }





}

