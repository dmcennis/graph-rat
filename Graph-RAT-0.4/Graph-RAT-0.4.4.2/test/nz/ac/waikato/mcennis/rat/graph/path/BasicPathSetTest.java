/*
 * BasicPathSetTest.java
 * JUnit based test
 *
 * Created on 2 July 2007, 15:26
 *
 * Copyright Daniel McEnnis, all rights reserved
 */

package nz.ac.waikato.mcennis.rat.graph.path;

import junit.framework.*;
import nz.ac.waikato.mcennis.rat.graph.Graph;
import nz.ac.waikato.mcennis.rat.graph.actor.Actor;
import nz.ac.waikato.mcennis.rat.graph.actor.ActorFactory;

/**
 *
 * @author Daniel McEnnis
 */
public class BasicPathSetTest extends TestCase {
    Actor a ;
    Actor b ;
    Actor c ;
    Actor d ;
    Actor e ;
    Path ab ;
    Path ac ;
    Path ae ;
    Path abd ;
    Path ba ;
    Path bc ;
    Path bd ;
    Path bae ;
    Path ca ;
    Path cb ;
    Path cae ;
    Path cbd ;
    Path dc ;
    Path dca ;
    Path dcb ;
    Path dcae ;
    BasicPathSet instance;
    java.util.HashSet<Path> fullSet;
    java.util.HashSet<Path> aSource;
    java.util.HashSet<Path> aDest;
    
    public BasicPathSetTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("ActorType","User");
        props.setProperty("ActorID","A");
        a = ActorFactory.newInstance().create(props);
        props.setProperty("ActorID","B");
        b = ActorFactory.newInstance().create(props);
        props.setProperty("ActorID","C");
        c = ActorFactory.newInstance().create(props);
        props.setProperty("ActorID","D");
        d = ActorFactory.newInstance().create(props);
        props.setProperty("ActorID","E");
        e = ActorFactory.newInstance().create(props);
        props.setProperty("PathType","Basic");
        props.setProperty("PathID","Dummy");
        ab = PathFactory.newInstance().create(props);
        ac = PathFactory.newInstance().create(props);
        ae = PathFactory.newInstance().create(props);
        abd = PathFactory.newInstance().create(props);
        ba = PathFactory.newInstance().create(props);
        bc = PathFactory.newInstance().create(props);
        bd = PathFactory.newInstance().create(props);
        bae = PathFactory.newInstance().create(props);
        ca = PathFactory.newInstance().create(props);
        cb = PathFactory.newInstance().create(props);
        cae = PathFactory.newInstance().create(props);
        cbd = PathFactory.newInstance().create(props);
        dc = PathFactory.newInstance().create(props);
        dca = PathFactory.newInstance().create(props);
        dcb = PathFactory.newInstance().create(props);
        dcae = PathFactory.newInstance().create(props);
        ab.setPath(new Actor[]{a,b},1.0,"Trivial");
        ac.setPath(new Actor[]{a,c},1.0,"Trivial");
        ae.setPath(new Actor[]{a,e},1.0,"Trivial");
        abd.setPath(new Actor[]{a,b,d},2.0,"Trivial");
        ba.setPath(new Actor[]{b,a},1.0,"Trivial");
        bc.setPath(new Actor[]{b,c},1.0,"Trivial");
        bd.setPath(new Actor[]{b,d},1.0,"Trivial");
        bae.setPath(new Actor[]{b,a,e},2.0,"Trivial");
        ca.setPath(new Actor[]{c,a},1.0,"Trivial");
        cb.setPath(new Actor[]{c,b},1.0,"Trivial");
        cae.setPath(new Actor[]{c,a,e},1.0,"Trivial");
        cbd.setPath(new Actor[]{c,a,d},1.0,"Trivial");
        dc.setPath(new Actor[]{d,c},1.0,"Trivial");
        dca.setPath(new Actor[]{d,c,a},2.0,"Trivial");
        dcb.setPath(new Actor[]{d,c,b},2.0,"Trivial");
        dcae.setPath(new Actor[]{d,c,a,e},3.0,"Trivial");
        
        instance = new BasicPathSet();
        instance.addPath(ab);
        instance.addPath(ac);
        instance.addPath(ae);
        instance.addPath(abd);
        instance.addPath(ba);
        instance.addPath(bc);
        instance.addPath(bd);
        instance.addPath(bae);
        instance.addPath(ca);
        instance.addPath(cb);
        instance.addPath(cae);
        instance.addPath(cbd);
        instance.addPath(dc);
        instance.addPath(dca);
        instance.addPath(dcb);
        instance.addPath(dcae);
        
        fullSet = new java.util.HashSet<Path>();
        aSource = new java.util.HashSet<Path>();
        aDest = new java.util.HashSet<Path>();
        
        fullSet.add(ab);
        aSource.add(ab);
        fullSet.add(ac);
        aSource.add(ac);
        fullSet.add(ae);
        aSource.add(ae);
        fullSet.add(abd);
        aSource.add(abd);
        fullSet.add(ba);
        aDest.add(ba);
        fullSet.add(bc);
        fullSet.add(bd);
        fullSet.add(bae);
        fullSet.add(ca);
        aDest.add(ca);
        fullSet.add(cb);
        fullSet.add(cae);
        fullSet.add(cbd);
        fullSet.add(dc);
        fullSet.add(dca);
        aDest.add(dca);
        fullSet.add(dcb);
        fullSet.add(dcae);
    }
    
    protected void tearDown() throws Exception {
    }
    
    /**
     * Test of addPath method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPathSet.
     */
    public void testAddPath() {
        System.out.println("addPath");
        
    }
    
    /**
     * Test of getPath method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPathSet.
     */
    public void testGetPath() {
        System.out.println("getPath");
        
        Path[] result = instance.getPath();
        assertEquals(fullSet.size(), result.length);
        for(int i=0;i<fullSet.size();++i){
            assertTrue(fullSet.contains(result[i]));
        }
    }
    
    /**
     * Test of getPathBySource method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPathSet.
     */
    public void testGetPathBySource() {
        System.out.println("getPathBySource");
        
        Path[] result = instance.getPathBySource("A");
        assertEquals(aSource.size(), result.length);
        for(int i=0;i<aSource.size();++i){
            assertTrue(aSource.contains(result[i]));
        }
    }
    
    /**
     * Test of getPathByDestination method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPathSet.
     */
    public void testGetPathByDestination() {
        System.out.println("getPathByDestination");
        
        
        Path[] result = instance.getPathByDestination("A");
        assertEquals(aDest.size(), result.length);
        for(int i=0;i<aDest.size();++i){
            assertTrue(aDest.contains(result[i]));
        }
    }
    
    /**
     * Test of getPath method, of class nz.ac.waikato.mcennis.arm.graph.path.BasicPathSet.
     */
    public void testGetPathBySourceAndDestination(){
        System.out.println("getPathBySourceAndDestination");
        
        Path[] result = instance.getPath("A","C");
        assertEquals(1,result.length);
        assertEquals(ac,result[0]);
    }
    
    public void testGetGeodesicLength(){
        double ret = instance.getGeodesicLength("A","D");
        assertEquals(2.0,ret,0.001);
    }
    
}
