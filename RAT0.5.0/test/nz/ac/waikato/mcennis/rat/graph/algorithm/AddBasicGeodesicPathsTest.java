/*


 * AddGeodesicPathsTest.java


 * JUnit based test


 *


 * Created on 5 July 2007, 19:26


 *


 * Copyright Daniel McEnnis, all rights reserved


 */





package nz.ac.waikato.mcennis.rat.graph.algorithm;





import java.util.List;
import junit.framework.*;


import nz.ac.waikato.mcennis.rat.graph.Graph;


import nz.ac.waikato.mcennis.rat.graph.GraphFactory;


import nz.ac.waikato.mcennis.rat.graph.actor.Actor;


import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;


import nz.ac.waikato.mcennis.rat.graph.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;


import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;


import nz.ac.waikato.mcennis.rat.graph.path.Path;




import nz.ac.waikato.mcennis.rat.graph.path.PathSet;




import nz.ac.waikato.mcennis.rat.graph.property.Property;


import nz.ac.waikato.mcennis.rat.graph.property.PropertyFactory;





/**


 *


 * @author Daniel McEnnis


 */


public class AddBasicGeodesicPathsTest extends TestCase {


    Actor a;


    Actor b;


    Actor c;


    Actor d;


    Actor e;


    Link ab;


    Link ba;


    Link bc;


    Link cb;


    Link ac;


    Link ca;


    Link ae;


    Link bd;


    Link dc;


    Graph g;


    


    public AddBasicGeodesicPathsTest(String testName) {


        super(testName);


    }





    protected void setUp() throws Exception {


        nz.ac.waikato.mcennis.rat.graph.descriptors.Properties props = PropertiesFactory.newInstance().create();


        props.set("ActorType","User");


        props.set("ActorID","A");


        props.set("PropertyType","Basic");


        props.set("PropertyID","interest");


        props.set("LinkType","Interest");


        props.set("LinkClass","Basic");


        props.set("GraphType","Memory");


        g = GraphFactory.newInstance().create(props);


        a = ActorFactory.newInstance().create(props);


        Property ia = PropertyFactory.newInstance().create(props);


        ia.add("1");


        ia.add("2");


        a.add(ia);


        g.add(a);


        props.set("ActorType","User");


        props.set("ActorID","B");


        b = ActorFactory.newInstance().create(props);


        Property ib = PropertyFactory.newInstance().create(props);


        ib.add("1");


        ib.add("2");


        ib.add("3");


        ib.add("4");


        b.add(ib);


        g.add(b);


        props.set("ActorID","C");


        c = ActorFactory.newInstance().create(props);


        Property ic = PropertyFactory.newInstance().create(props);


        ic.add("3");


        ic.add("4");


        ic.add("5");


        ic.add("6");


        c.add(ic);


        g.add(c);


        props.set("ActorID","D");


        d = ActorFactory.newInstance().create(props);


        Property id = PropertyFactory.newInstance().create(props);


        id.add("7");


        id.add("8");


        d.add(id);


        g.add(d);


        props.set("ActorID","E");


        e = ActorFactory.newInstance().create(props);


        g.add(e);


        props.set("LinkType","Knows");


        ab = LinkFactory.newInstance().create(props);


        ab.set(a,1.0,b);


        g.add(ab);


        ba = LinkFactory.newInstance().create(props);


        ba.set(b,1.0,a);


        g.add(ba);


        cb = LinkFactory.newInstance().create(props);


        cb.set(c,1.0,b);


        g.add(cb);


        bc = LinkFactory.newInstance().create(props);


        bc.set(b,1.0,c);


        g.add(bc);


        ca = LinkFactory.newInstance().create(props);


        ca.set(c,1.0,a);


        g.add(ca);


        ac = LinkFactory.newInstance().create(props);


        ac.set(a,1.0,c);


        g.add(ac);


        ae = LinkFactory.newInstance().create(props);


        ae.set(a,1.0,e);


        g.add(ae);


        bd = LinkFactory.newInstance().create(props);


        bd.set(b,1.0,d);


        g.add(bd);


        dc = LinkFactory.newInstance().create(props);


        dc.set(d,1.0,c);


        g.add(dc);        


    }





    protected void tearDown() throws Exception {


    }





    public void testPathSetName(){


        GeodesicPaths test = new GeodesicPaths();


        test.execute(g);


        


        PathSet set = g.getPathSet("Directional Geodesic by Knows");


        assertNotNull(set);


    }


    


    


    /**


     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddGeodesicPaths.


     */


    public void testAPaths(){


        GeodesicPaths test = new GeodesicPaths();


        test.execute(g);


        


        List<PathSet> set = g.getPathSet();


        assertEquals(1,set.size());


        Path[] paths = set.get(0).getPathBySource("A");


        assertNotNull(paths);


        assertEquals(4,paths.length);


        for(int i=0;i<paths.length;++i){


            if(paths[i].getEnd().getID().contentEquals("B")){


                assertEquals(2,paths[i].getPath().length);


            }else if(paths[i].getEnd().getID().contentEquals("C")){


                assertEquals(2,paths[i].getPath().length);


            }else if(paths[i].getEnd().getID().contentEquals("D")){


                Actor[] sequence = paths[i].getPath();


                assertNotNull(sequence);


                assertEquals(3,sequence.length);


                assertEquals("B",sequence[1].getID());


            }else if(paths[i].getEnd().getID().contentEquals("E")){


                assertEquals(2,paths[i].getPath().length);


            }


        }


    }


    


    /**


     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddGeodesicPaths.


     */


    public void testBPaths(){


        GeodesicPaths test = new GeodesicPaths();


        test.execute(g);


        


        List<PathSet> set = g.getPathSet();


        assertEquals(1,set.size());


        Path[] paths = set.get(0).getPathBySource("B");


        assertNotNull(paths);


        assertEquals(4,paths.length);


        for(int i=0;i<paths.length;++i){


            if(paths[i].getEnd().getID().contentEquals("A")){


                assertEquals(2,paths[i].getPath().length);


            }else if(paths[i].getEnd().getID().contentEquals("C")){


                assertEquals(2,paths[i].getPath().length);


            }else if(paths[i].getEnd().getID().contentEquals("D")){


                assertEquals(2,paths[i].getPath().length);


            }else if(paths[i].getEnd().getID().contentEquals("E")){


                Actor[] sequence = paths[i].getPath();


                assertNotNull(sequence);


                assertEquals(3,sequence.length);


                assertEquals("A",sequence[1].getID());


            }


        }


    }


    


    /**


     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddGeodesicPaths.


     */


    public void testCPaths(){


        GeodesicPaths test = new GeodesicPaths();


        test.execute(g);


        


        List<PathSet> set = g.getPathSet();


        assertEquals(1,set.size());


        Path[] paths = set.get(0).getPathBySource("C");


        assertNotNull(paths);


        assertEquals(4,paths.length);


        for(int i=0;i<paths.length;++i){


            if(paths[i].getEnd().getID().contentEquals("B")){


                assertEquals(2,paths[i].getPath().length);


            }else if(paths[i].getEnd().getID().contentEquals("A")){


                assertEquals(2,paths[i].getPath().length);


            }else if(paths[i].getEnd().getID().contentEquals("D")){


                Actor[] sequence = paths[i].getPath();


                assertNotNull(sequence);


                assertEquals(3,sequence.length);


                assertEquals("B",sequence[1].getID());


            }else if(paths[i].getEnd().getID().contentEquals("E")){


                Actor[] sequence = paths[i].getPath();


                assertNotNull(sequence);


                assertEquals(3,sequence.length);


                assertEquals("A",sequence[1].getID());


            }


        }


    }


    


    /**


     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddGeodesicPaths.


     */


    public void testDPaths(){


        GeodesicPaths test = new GeodesicPaths();


        test.execute(g);


        


        List<PathSet> set = g.getPathSet();


        assertEquals(1,set.size());


        Path[] paths = set.get(0).getPathBySource("D");


        assertNotNull(paths);


        assertEquals(4,paths.length);


        for(int i=0;i<paths.length;++i){


            if(paths[i].getEnd().getID().contentEquals("B")){


                Actor[] sequence = paths[i].getPath();


                assertNotNull(sequence);


                assertEquals(3,sequence.length);


                assertEquals("C",sequence[1].getID());


            }else if(paths[i].getEnd().getID().contentEquals("C")){


                assertEquals(2,paths[i].getPath().length);


           }else if(paths[i].getEnd().getID().contentEquals("A")){


                Actor[] sequence = paths[i].getPath();


                assertNotNull(sequence);


                assertEquals(3,sequence.length);


                assertEquals("C",sequence[1].getID());


            }else if(paths[i].getEnd().getID().contentEquals("E")){


                Actor[] sequence = paths[i].getPath();


                assertNotNull(sequence);


                assertEquals(4,sequence.length);


                assertEquals("C",sequence[1].getID());


                assertEquals("A",sequence[2].getID());


            }


        }


    }


    


    /**


     * Test of execute method, of class nz.ac.waikato.mcennis.arm.graph.algorithm.AddGeodesicPaths.


     */


    public void testEPaths(){


        GeodesicPaths test = new GeodesicPaths();


        test.execute(g);


        


        List<PathSet> set = g.getPathSet();


        assertEquals(1,set.size());


        Path[] paths = set.get(0).getPathBySource("E");


        assertNull(paths);


    }


        public void testGetName() {


        fail("Test not yet written");


    }





    public void testSetName() {


        fail("Test not yet written");


    }





    public void testGetInputType() {


        fail("Test not yet written");


    }





    public void testGetOutputType() {


        fail("Test not yet written");


    }





    public void testGetParameter() {


        fail("Test not yet written");


    }





    public void testGetParameterString() {


        fail("Test not yet written");


    }





    public void testGetParameterObject() {


        fail("Test not yet written");


    }





    public void testSetParameter() {


        fail("Test not yet written");


    }


}


