/*

 * Created 8-5-08

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.graph.algorithm;



import java.util.List;
import nz.ac.waikato.mcennis.rat.graph.algorithm.similarity.HierarchyByCooccurance;

import org.dynamicfactory.descriptors.Properties;

import junit.framework.TestCase;


import nz.ac.waikato.mcennis.rat.graph.MemGraph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

import nz.ac.waikato.mcennis.rat.graph.algorithm.collaborativefiltering.AssociativeMining;




import org.dynamicfactory.descriptors.PropertiesFactory;
import nz.ac.waikato.mcennis.rat.graph.link.Link;

import nz.ac.waikato.mcennis.rat.graph.link.LinkFactory;



/**

 *

 * @author Daniel McEnnis

 */

public class HierarchyByCooccuranceTest extends TestCase {



    MemGraph base;

    

    Actor a1;

    Actor a2;

    Actor a3;

    Actor a4;

    Actor a5;

    Actor a6;

    Actor b1;

    Actor b2;

    Actor b3;

    Actor b4;

    Actor b5;

    Actor b6;

    Actor c1;

    Actor c2;

    Actor c3;

    Actor c4;

    Actor c5;

    Actor c6;

    Actor d1;

    Actor d2;

    Actor d3;

    Actor d4;

    Actor d5;

    Actor d6;

    Actor e1;

    Actor e2;

    Actor e3;

    Actor e4;

    Actor e5;

    Actor e6;

    

    Actor rock;

    Actor alternative;

    Actor jazz;

    Actor blues;

    Actor oldBlues;

    Actor bebop;

    

    Link a1rock;

    Link a2rock;

    Link a3rock;

    Link a1alternative;

    Link a2alternative;

    Link a3alternative;

    Link b1rock;

    Link b2rock;

    Link b3rock;

    Link c1jazz;

    Link c2jazz;

    Link c3jazz;

    Link c1blues;

    Link c2blues;

    Link c3blues;

    Link d1jazz;

    Link d2jazz;

    Link d3jazz;

    Link d1blues;

    Link d2blues;

    Link d3blues;

    Link d1oldBlues;

    Link d2oldBlues;

    Link d3oldBlues;

    Link e1jazz;

    Link e2jazz;

    Link e3jazz;

    Link e1bebop;

    Link e2bebop;

    Link e3bebop;

    Link a4rock;

    Link a5rock;

    Link a6rock;

    Link a4alternative;

    Link a5alternative;

    Link a6alternative;

    Link b4rock;

    Link b5rock;

    Link b6rock;

    Link c4jazz;

    Link c5jazz;

    Link c6jazz;

    Link c4blues;

    Link c5blues;

    Link c6blues;

    Link d4jazz;

    Link d5jazz;

    Link d6jazz;

    Link d4blues;

    Link d5blues;

    Link d6blues;

    Link d4oldBlues;

    Link d5oldBlues;

    Link d6oldBlues;

    Link e4jazz;

    Link e5jazz;

    Link e6jazz;

    Link e4bebop;

    Link e5bebop;

    Link e6bebop;



    public HierarchyByCooccuranceTest(String testName) {

        super(testName);

    }            



    @Override

    protected void setUp() throws Exception {

        base = new MemGraph();

        base.setID("test");

        

        Properties props = PropertiesFactory.newInstance().create();

        props.set("ActorClass", "MemGraph");

        props.set("ActorType","artist");

        props.set("ActorID","a1");

        a1 = ActorFactory.newInstance().create(props);

        base.add(a1);

        props.set("ActorID","a2");

        a2 = ActorFactory.newInstance().create(props);

        base.add(a2);

        props.set("ActorID","a3");

        a3 = ActorFactory.newInstance().create(props);

        base.add(a3);

        props.set("ActorID","a4");

        a4 = ActorFactory.newInstance().create(props);

        base.add(a4);

        props.set("ActorID","a5");

        a5 = ActorFactory.newInstance().create(props);

        base.add(a5);

        props.set("ActorID","a6");

        a6 = ActorFactory.newInstance().create(props);

        base.add(a6);

        props.set("ActorID","b1");

        b1 = ActorFactory.newInstance().create(props);

        base.add(b1);

        props.set("ActorID","b2");

        b2 = ActorFactory.newInstance().create(props);

        base.add(b2);

        props.set("ActorID","b3");

        b3 = ActorFactory.newInstance().create(props);

        base.add(b3);

        props.set("ActorID","b4");

        b4 = ActorFactory.newInstance().create(props);

        base.add(b4);

        props.set("ActorID","b5");

        b5 = ActorFactory.newInstance().create(props);

        base.add(b5);

        props.set("ActorID","b6");

        b6 = ActorFactory.newInstance().create(props);

        base.add(b6);

        props.set("ActorID","c1");

        c1 = ActorFactory.newInstance().create(props);

        base.add(c1);

        props.set("ActorID","c2");

        c2 = ActorFactory.newInstance().create(props);

        base.add(c2);

        props.set("ActorID","c3");

        c3 = ActorFactory.newInstance().create(props);

        base.add(c3);

        props.set("ActorID","c4");

        c4 = ActorFactory.newInstance().create(props);

        base.add(c4);

        props.set("ActorID","c5");

        c5 = ActorFactory.newInstance().create(props);

        base.add(c5);

        props.set("ActorID","c6");

        c6 = ActorFactory.newInstance().create(props);

        base.add(c6);

        props.set("ActorID","d1");

        d1 = ActorFactory.newInstance().create(props);

        base.add(d1);

        props.set("ActorID","d2");

        d2 = ActorFactory.newInstance().create(props);

        base.add(d2);

        props.set("ActorID","d3");

        d3 = ActorFactory.newInstance().create(props);

        base.add(d3);

        props.set("ActorID","d4");

        d4 = ActorFactory.newInstance().create(props);

        base.add(d4);

        props.set("ActorID","d5");

        d5 = ActorFactory.newInstance().create(props);

        base.add(d5);

        props.set("ActorID","d6");

        d6 = ActorFactory.newInstance().create(props);

        base.add(d6);

        props.set("ActorID","e1");

        e1 = ActorFactory.newInstance().create(props);

        base.add(e1);

        props.set("ActorID","e2");

        e2 = ActorFactory.newInstance().create(props);

        base.add(e2);

        props.set("ActorID","e3");

        e3 = ActorFactory.newInstance().create(props);

        base.add(e3);

        props.set("ActorID","e4");

        e4 = ActorFactory.newInstance().create(props);

        base.add(e4);

        props.set("ActorID","e5");

        e5 = ActorFactory.newInstance().create(props);

        base.add(e5);

        props.set("ActorID","e6");

        e6 = ActorFactory.newInstance().create(props);

        base.add(e6);

        

        props.set("ActorType", "tag");

        props.set("ActorID","Rock");

        rock = ActorFactory.newInstance().create(props);

        base.add(rock);

        props.set("ActorID","Alternative");

        alternative = ActorFactory.newInstance().create(props);

        base.add(alternative);

        props.set("ActorID","Jazz");

        jazz = ActorFactory.newInstance().create(props);

        base.add(jazz);

        props.set("ActorID","Blues");

        blues = ActorFactory.newInstance().create(props);

        base.add(blues);

        props.set("ActorID","OldBlues");

        oldBlues = ActorFactory.newInstance().create(props);

        base.add(oldBlues);

        props.set("ActorID","Bebop");

        bebop = ActorFactory.newInstance().create(props);

        base.add(bebop);

        

        props.set("LinkType","Tag");

        a1rock = LinkFactory.newInstance().create(props);

        a1rock.set(a1, 1.0, rock);

        base.add(a1rock);

        a2rock = LinkFactory.newInstance().create(props);

        a2rock.set(a2, 1.0, rock);

        base.add(a2rock);

        a3rock = LinkFactory.newInstance().create(props);

        a3rock.set(a3, 1.0, rock);

        base.add(a3rock);

        a4rock = LinkFactory.newInstance().create(props);

        a4rock.set(a4, 1.0, rock);

        base.add(a4rock);

        a5rock = LinkFactory.newInstance().create(props);

        a5rock.set(a5, 1.0, rock);

        base.add(a5rock);

        a6rock = LinkFactory.newInstance().create(props);

        a6rock.set(a6, 1.0, rock);

        base.add(a6rock);

        a1alternative = LinkFactory.newInstance().create(props);

        a1alternative.set(a1, 1.0, alternative);

        base.add(a1alternative);

        a2alternative = LinkFactory.newInstance().create(props);

        a2alternative.set(a2, 1.0, alternative);

        base.add(a2alternative);

        a3alternative = LinkFactory.newInstance().create(props);

        a3alternative.set(a3, 1.0, alternative);

        base.add(a3alternative);

        a4alternative = LinkFactory.newInstance().create(props);

        a4alternative.set(a4, 1.0, alternative);

        base.add(a4alternative);

        a5alternative = LinkFactory.newInstance().create(props);

        a5alternative.set(a5, 1.0, alternative);

        base.add(a5alternative);

        a6alternative = LinkFactory.newInstance().create(props);

        a6alternative.set(a6, 1.0, alternative);

        base.add(a6alternative);

        b1rock = LinkFactory.newInstance().create(props);

        b1rock.set(b1, 1.0, rock);

        base.add(b1rock);

        b2rock = LinkFactory.newInstance().create(props);

        b2rock.set(b2, 1.0, rock);

        base.add(b2rock);

        b3rock = LinkFactory.newInstance().create(props);

        b3rock.set(b3, 1.0, rock);

        base.add(b3rock);

        b4rock = LinkFactory.newInstance().create(props);

        b4rock.set(b4, 1.0, rock);

        base.add(b4rock);

        b5rock = LinkFactory.newInstance().create(props);

        b5rock.set(b5, 1.0, rock);

        base.add(b5rock);

        b6rock = LinkFactory.newInstance().create(props);

        b6rock.set(b6, 1.0, rock);

        base.add(b6rock);

        c1jazz = LinkFactory.newInstance().create(props);

        c1jazz.set(c1, 1.0, jazz);

        base.add(c1jazz);

        c2jazz = LinkFactory.newInstance().create(props);

        c2jazz.set(c2, 1.0, jazz);

        base.add(c2jazz);

        c3jazz = LinkFactory.newInstance().create(props);

        c3jazz.set(c3, 1.0, jazz);

        base.add(c3jazz);

        c4jazz = LinkFactory.newInstance().create(props);

        c4jazz.set(c4, 1.0, jazz);

        base.add(c4jazz);

        c5jazz = LinkFactory.newInstance().create(props);

        c5jazz.set(c5, 1.0, jazz);

        base.add(c5jazz);

        c6jazz = LinkFactory.newInstance().create(props);

        c6jazz.set(c6, 1.0, jazz);

        base.add(c6jazz);

        c1blues = LinkFactory.newInstance().create(props);

        c1blues.set(c1, 1.0, blues);

        base.add(c1blues);

        c2blues = LinkFactory.newInstance().create(props);

        c2blues.set(c2, 1.0, blues);

        base.add(c2blues);

        c3blues = LinkFactory.newInstance().create(props);

        c3blues.set(c3, 1.0, blues);

        base.add(c3blues);

        c4blues = LinkFactory.newInstance().create(props);

        c4blues.set(c4, 1.0, blues);

        base.add(c4blues);

        c5blues = LinkFactory.newInstance().create(props);

        c5blues.set(c5, 1.0, blues);

        base.add(c5blues);

        c6blues = LinkFactory.newInstance().create(props);

        c6blues.set(c6, 1.0, blues);

        base.add(c6blues);

        d1jazz = LinkFactory.newInstance().create(props);

        d1jazz.set(d1, 1.0, jazz);

        base.add(d1jazz);

        d2jazz = LinkFactory.newInstance().create(props);

        d2jazz.set(d2, 1.0, jazz);

        base.add(d2jazz);

        d3jazz = LinkFactory.newInstance().create(props);

        d3jazz.set(d3, 1.0, jazz);

        base.add(d3jazz);

        d4jazz = LinkFactory.newInstance().create(props);

        d4jazz.set(d4, 1.0, jazz);

        base.add(d4jazz);

        d5jazz = LinkFactory.newInstance().create(props);

        d5jazz.set(d5, 1.0, jazz);

        base.add(d5jazz);

        d6jazz = LinkFactory.newInstance().create(props);

        d6jazz.set(d6, 1.0, jazz);

        base.add(d6jazz);

        d1blues = LinkFactory.newInstance().create(props);

        d1blues.set(d1, 1.0, blues);

        base.add(d1blues);

        d2blues = LinkFactory.newInstance().create(props);

        d2blues.set(d2, 1.0, blues);

        base.add(d2blues);

        d3blues = LinkFactory.newInstance().create(props);

        d3blues.set(d3, 1.0, blues);

        base.add(d3blues);

        d4blues = LinkFactory.newInstance().create(props);

        d4blues.set(d4, 1.0, blues);

        base.add(d4blues);

        d5blues = LinkFactory.newInstance().create(props);

        d5blues.set(d5, 1.0, blues);

        base.add(d5blues);

        d6blues = LinkFactory.newInstance().create(props);

        d6blues.set(d6, 1.0, blues);

        base.add(d6blues);

        d1oldBlues = LinkFactory.newInstance().create(props);

        d1oldBlues.set(d1, 1.0, oldBlues);

        base.add(d1oldBlues);

        d2oldBlues = LinkFactory.newInstance().create(props);

        d2oldBlues.set(d2, 1.0, oldBlues);

        base.add(d2oldBlues);

        d3oldBlues = LinkFactory.newInstance().create(props);

        d3oldBlues.set(d3, 1.0, oldBlues);

        base.add(d3oldBlues);

        d4oldBlues = LinkFactory.newInstance().create(props);

        d4oldBlues.set(d4, 1.0, oldBlues);

        base.add(d4oldBlues);

        d5oldBlues = LinkFactory.newInstance().create(props);

        d5oldBlues.set(d5, 1.0, oldBlues);

        base.add(d5oldBlues);

        d6oldBlues = LinkFactory.newInstance().create(props);

        d6oldBlues.set(d6, 1.0, oldBlues);

        base.add(d6oldBlues);

        e1jazz = LinkFactory.newInstance().create(props);

        e1jazz.set(e1, 1.0, jazz);

        base.add(e1jazz);

        e2jazz = LinkFactory.newInstance().create(props);

        e2jazz.set(e2, 1.0, jazz);

        base.add(e2jazz);

        e3jazz = LinkFactory.newInstance().create(props);

        e3jazz.set(e3, 1.0, jazz);

        base.add(e3jazz);

        e4jazz = LinkFactory.newInstance().create(props);

        e4jazz.set(e4, 1.0, jazz);

        base.add(e4jazz);

        e5jazz = LinkFactory.newInstance().create(props);

        e5jazz.set(e5, 1.0, jazz);

        base.add(e5jazz);

        e6jazz = LinkFactory.newInstance().create(props);

        e6jazz.set(e6, 1.0, jazz);

        base.add(e6jazz);

        e1bebop = LinkFactory.newInstance().create(props);

        e1bebop.set(e1, 1.0, bebop);

        base.add(e1bebop);

        e2bebop = LinkFactory.newInstance().create(props);

        e2bebop.set(e2, 1.0, bebop);

        base.add(e2bebop);

        e3bebop = LinkFactory.newInstance().create(props);

        e3bebop.set(e3, 1.0, bebop);

        base.add(e3bebop);

        e4bebop = LinkFactory.newInstance().create(props);

        e4bebop.set(e4, 1.0, bebop);

        base.add(e4bebop);

        e5bebop = LinkFactory.newInstance().create(props);

        e5bebop.set(e5, 1.0, bebop);

        base.add(e5bebop);

        e6bebop = LinkFactory.newInstance().create(props);

        e6bebop.set(e6, 1.0, bebop);

        base.add(e6bebop);



        AssociativeMining prepare = new AssociativeMining();

        props.set("relation", "Tag");

        props.set("userActor", "artist");

        props.set("artistActor", "tag");

        prepare.init(props);

        prepare.execute(base);



    }



    @Override

    protected void tearDown() throws Exception {

        super.tearDown();

    }



    /**

     * Test of execute method, of class HierarchyByCooccurance.

     */

    public void testExecute() {

        System.out.println("execute");

        HierarchyByCooccurance instance = new HierarchyByCooccurance();

        instance.execute(base);

    }



    /**

     * Test of execute method, of class HierarchyByCooccurance.

     */

    public void testExecuteRock() {

        System.out.println("executeRock");

        HierarchyByCooccurance instance = new HierarchyByCooccurance();

        instance.execute(base);

        

        List<Link> general = base.getLinkBySource("Generalization", rock);

        assertNull(general);

        

        List<Link> specialization = base.getLinkBySource("Specialization", rock);

        assertNotNull(specialization);

        assertEquals(1,specialization.size());

        assertEquals(alternative,specialization.get(0).getDestination());

        

        List<Link> disjoint = base.getLinkBySource("Disjoint", rock);

        assertNotNull(disjoint);

        assertEquals(4,disjoint.size());

        for(int i=0;i<disjoint.size();++i){

            if(disjoint.get(i).getDestination().getID().contentEquals("Blues")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("OldBlues")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Bebop")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Jazz")){

                

            }else{

                fail("Unexpected tag '"+disjoint.get(i).getDestination().getID()+"' detected in set of disjoint links");

            }

        }

    }



    /**

     * Test of execute method, of class HierarchyByCooccurance.

     */

    public void testExecuteAlternative() {

        System.out.println("executeAlternative");

        HierarchyByCooccurance instance = new HierarchyByCooccurance();

        instance.execute(base);

        

        List<Link> general = base.getLinkBySource("Generalization", alternative);

        assertNotNull(general);

        assertEquals(1,general.size());

        assertEquals(rock,general.get(0).getDestination());

        

        List<Link> specialization = base.getLinkBySource("Specialization", alternative);

        assertNull(specialization);

        

        List<Link> disjoint = base.getLinkBySource("Disjoint", alternative);

        assertNotNull(disjoint);

        assertEquals(4,disjoint.size());

        for(int i=0;i<disjoint.size();++i){

            if(disjoint.get(i).getDestination().getID().contentEquals("Blues")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Jazz")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("OldBlues")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Bebop")){

                

            }else{

                fail("Unexpected tag '"+disjoint.get(i).getDestination().getID()+"' detected in set of disjoint links");

            }

        }



    }



    /**

     * Test of execute method, of class HierarchyByCooccurance.

     */

    public void testExecuteJazz() {

        System.out.println("executeJazz");

        HierarchyByCooccurance instance = new HierarchyByCooccurance();

        instance.execute(base);

        

        List<Link> general = base.getLinkBySource("Generalization", jazz);

        assertNull(general);

        

        List<Link> specialization = base.getLinkBySource("Specialization", jazz);

        assertNotNull(specialization);

        assertEquals(3,specialization.size());

         for(int i=0;i<specialization.size();++i){

            if(specialization.get(i).getDestination().getID().contentEquals("Blues")){

                

            }else if(specialization.get(i).getDestination().getID().contentEquals("OldBlues")){

                

            }else if(specialization.get(i).getDestination().getID().contentEquals("Bebop")){

                

            }else{

                fail("Unexpected tag '"+specialization.get(i).getDestination().getID()+"' detected in set of specialization links");

            }

        }

        

        List<Link> disjoint = base.getLinkBySource("Disjoint", jazz);

        assertNotNull(disjoint);

        assertEquals(2,disjoint.size());

        for(int i=0;i<disjoint.size();++i){

            if(disjoint.get(i).getDestination().getID().contentEquals("Rock")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Alternative")){

                

            }else{

                fail("Unexpected tag '"+disjoint.get(i).getDestination().getID()+"' detected in set of disjoint links");

            }

        }

    }



    /**

     * Test of execute method, of class HierarchyByCooccurance.

     */

    public void testExecuteBlues() {

        System.out.println("executeBlues");

        HierarchyByCooccurance instance = new HierarchyByCooccurance();

        instance.execute(base);

        

        List<Link> general = base.getLinkBySource("Generalization", blues);

        assertNotNull(general);

        assertEquals(1,general.size());

        assertEquals(jazz,general.get(0).getDestination());

        

        List<Link> specialization = base.getLinkBySource("Specialization", blues);

        assertNotNull(specialization);

        assertEquals(1,specialization.size());

        assertEquals(oldBlues,specialization.get(0).getDestination());

        

        List<Link> disjoint = base.getLinkBySource("Disjoint", blues);

        assertNotNull(disjoint);

        assertEquals(3,disjoint.size());

        for(int i=0;i<disjoint.size();++i){

            if(disjoint.get(i).getDestination().getID().contentEquals("Rock")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Alternative")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Bebop")){

                

            }else{

                fail("Unexpected tag '"+disjoint.get(i).getDestination().getID()+"' detected in set of disjoint links");

            }

        }



    }



    /**

     * Test of execute method, of class HierarchyByCooccurance.

     */

    public void testExecuteOldBlues() {

        System.out.println("executeOldBlues");

        HierarchyByCooccurance instance = new HierarchyByCooccurance();

        instance.execute(base);

        

        List<Link> general = base.getLinkBySource("Generalization", oldBlues);

        assertNotNull(general);

        assertEquals(2,general.size());

        for(int i=0;i<general.size();++i){

            if(general.get(i).getDestination().getID().contentEquals("Blues")){

                

            }else if(general.get(i).getDestination().getID().contentEquals("Jazz")){

                

            }else{

                fail("Unexpected tag '"+general.get(i).getDestination().getID()+"' detected in set of general links");

            }

        }

        

        List<Link> specialization = base.getLinkBySource("Specialization", oldBlues);

        assertNull(specialization);

        

        List<Link> disjoint = base.getLinkBySource("Disjoint", oldBlues);

        assertNotNull(disjoint);

        assertEquals(3,disjoint.size());

        for(int i=0;i<disjoint.size();++i){

            if(disjoint.get(i).getDestination().getID().contentEquals("Rock")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Bebop")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Alternative")){

                

            }else {

                fail("Unexpected tag '"+disjoint.get(i).getDestination().getID()+"' detected in set of disjoint links");

            }

        }



    }



    /**

     * Test of execute method, of class HierarchyByCooccurance.

     */

    public void testExecuteBebop() {

        System.out.println("executeBebop");

        HierarchyByCooccurance instance = new HierarchyByCooccurance();

        instance.execute(base);

        

        List<Link> general = base.getLinkBySource("Generalization", bebop);

        assertNotNull(general);

        assertEquals(1,general.size());

        assertEquals(jazz,general.get(0).getDestination());

        

        List<Link> specialization = base.getLinkBySource("Specialization", bebop);

        assertNull(specialization);

        

        List<Link> disjoint = base.getLinkBySource("Disjoint", bebop);

        assertNotNull(disjoint);

        assertEquals(4,disjoint.size());

        for(int i=0;i<disjoint.size();++i){

            if(disjoint.get(i).getDestination().getID().contentEquals("Rock")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Blues")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("Alternative")){

                

            }else if(disjoint.get(i).getDestination().getID().contentEquals("OldBlues")){

                

            }else{

                fail("Unexpected tag '"+disjoint.get(i).getDestination().getID()+"' detected in set of disjoint links");

            }

        }

    }

}

