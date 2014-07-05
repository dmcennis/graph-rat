/*

 * Created 26-3-08

 * Copyright Daniel McEnnis, see license.txt

 */



package nz.ac.waikato.mcennis.rat.graph.algorithm.evaluation;



import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.graph.MemGraph;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.actor.BasicUser;

import nz.ac.waikato.mcennis.rat.graph.link.BasicUserLink;

import nz.ac.waikato.mcennis.rat.graph.link.Link;



/**

 *

 * @author Daniel McEnnis

 */

public class EvaluationTestGraphs {



    static public Graph binary;

    static public Actor b_a;

    static public Actor b_b;

    static public Actor b_c;

    static public Actor b_d;

    static public Actor b_e;

    static public Actor b_f;

    static public Actor b_g;

    static public Actor b_1;

    static public Actor b_2;

    static public Actor b_3;

    static public Actor b_4;

    static public Link  gb_a1;

    static public Link  gb_b1;

    static public Link  gb_b2;

    static public Link  gb_c2;

    static public Link  gb_c3;

    static public Link  gb_d4;

    static public Link  gb_g1;

    

    static public Link  db_a1;

    static public Link  db_a2;

    static public Link  db_b2;

    static public Link  db_b3;

    static public Link  db_c2;

    static public Link  db_d4;

    static public Link  db_f2;



    

    static public Graph valued;

    static public Actor v_a;

    static public Actor v_b;

    static public Actor v_c;

    static public Actor v_d;

    static public Actor v_e;

    static public Actor v_f;

    static public Actor v_g;

    static public Actor v_1;

    static public Actor v_2;

    static public Actor v_3;

    static public Actor v_4;

    static public Link  gv_a1;

    static public Link  gv_b1;

    static public Link  gv_b2;

    static public Link  gv_c2;

    static public Link  gv_c3;

    static public Link  gv_d4;

    static public Link  gv_g1;



    static public Link  dv_a1;

    static public Link  dv_a2;

    static public Link  dv_b2;

    static public Link  dv_b3;

    static public Link  dv_c2;

    static public Link  dv_c3;

    static public Link  dv_d4;

    static public Link  dv_f2;

    

    public static void init(){

        binary = new MemGraph();

        b_a = new BasicUser("a");

        binary.add(b_a);

        b_b = new BasicUser("b");

        binary.add(b_b);

        b_c = new BasicUser("c");

        binary.add(b_c);

        b_d = new BasicUser("d");

        binary.add(b_d);

        b_e = new BasicUser("e");

        binary.add(b_e);

        b_f = new BasicUser("f");

        binary.add(b_f);

        b_g = new BasicUser("g");

        binary.add(b_g);

        b_1 = new BasicUser("1");

        b_1.setMode("Artist");

        binary.add(b_1);

        b_2 = new BasicUser("2");

        b_2.setMode("Artist");

        binary.add(b_2);

        b_3 = new BasicUser("3");

        b_3.setMode("Artist");

        binary.add(b_3);

        b_4 = new BasicUser("4");

        b_4.setMode("Artist");

        binary.add(b_4);

        gb_a1 = new BasicUserLink();

        gb_a1.setRelation("Given");

        gb_a1.set(b_a, 1.0, b_1);

        binary.add(gb_a1);

        gb_b1 = new BasicUserLink();

        gb_b1.setRelation("Given");

        gb_b1.set(b_b, 1.0, b_1);

        binary.add(gb_b1);

        gb_b2 = new BasicUserLink();

        gb_b2.setRelation("Given");

        gb_b2.set(b_b, 1.0, b_2);

        binary.add(gb_b2);

        gb_c2 = new BasicUserLink();

        gb_c2.setRelation("Given");

        gb_c2.set(b_c, 1.0, b_2);

        binary.add(gb_c2);

        gb_c3 = new BasicUserLink();

        gb_c3.setRelation("Given");

        gb_c3.set(b_c, 1.0, b_3);

        binary.add(gb_c3);

        gb_d4 = new BasicUserLink();

        gb_d4.setRelation("Given");

        gb_d4.set(b_d, 1.0, b_4);

        binary.add(gb_d4);

        gb_g1 = new BasicUserLink();

        gb_g1.setRelation("Given");

        gb_g1.set(b_g, 1.0, b_1);

        binary.add(gb_g1);

               

        

        db_a1 = new BasicUserLink();

        db_a1.setRelation("Derived");

        db_a1.set(b_a, 1.0, b_1);

        binary.add(db_a1);

        db_a2 = new BasicUserLink();

        db_a2.setRelation("Derived");

        db_a2.set(b_a, 1.0, b_2);

        binary.add(db_a2);

        db_b2 = new BasicUserLink();

        db_b2.setRelation("Derived");

        db_b2.set(b_b, 1.0, b_2);

        binary.add(db_b2);

        db_b3 = new BasicUserLink();

        db_b3.setRelation("Derived");

        db_b3.set(b_b, 1.0, b_3);

        binary.add(db_b3);

        db_c2 = new BasicUserLink();

        db_c2.setRelation("Derived");

        db_c2.set(b_c, 1.0, b_2);

        binary.add(db_c2);

        db_d4 = new BasicUserLink();

        db_d4.setRelation("Derived");

        db_d4.set(b_d, 1.0, b_4);

        binary.add(db_d4);

        db_f2 = new BasicUserLink();

        db_f2.setRelation("Derived");

        db_f2.set(b_f, 1.0, b_2);

        binary.add(db_f2);



        valued = new MemGraph();

        v_a = new BasicUser("a");

        valued.add(v_a);

        v_b = new BasicUser("b");

        valued.add(v_b);

        v_c = new BasicUser("c");

        valued.add(v_c);

        v_d = new BasicUser("d");

        valued.add(v_d);

        v_e = new BasicUser("e");

        valued.add(v_e);

        v_f = new BasicUser("f");

        valued.add(v_f);

        v_g = new BasicUser("g");

        valued.add(v_g);

        v_1 = new BasicUser("1");

        v_1.setMode("Artist");

        valued.add(v_1);

        v_2 = new BasicUser("2");

        v_2.setMode("Artist");

        valued.add(v_2);

        v_3 = new BasicUser("3");

        v_3.setMode("Artist");

        valued.add(v_3);

        v_4 = new BasicUser("4");

        v_4.setMode("Artist");

        valued.add(v_4);   

        gv_a1 = new BasicUserLink();

        gv_a1.setRelation("Given");

        gv_a1.set(v_a, 1.0, v_1);

        valued.add(gv_a1);

        gv_b1 = new BasicUserLink();

        gv_b1.setRelation("Given");

        gv_b1.set(v_b, 1.0, v_1);

        valued.add(gv_b1);

        gv_b2 = new BasicUserLink();

        gv_b2.setRelation("Given");

        gv_b2.set(v_b, 2.0, v_2);

        valued.add(gv_b2);

        gv_c2 = new BasicUserLink();

        gv_c2.setRelation("Given");

        gv_c2.set(v_c, 2.0, v_2);

        valued.add(gv_c2);

        gv_c3 = new BasicUserLink();

        gv_c3.setRelation("Given");

        gv_c3.set(v_c, 1.0, v_3);

        valued.add(gv_c3);

        gv_d4 = new BasicUserLink();

        gv_d4.setRelation("Given");

        gv_d4.set(v_d, 3.0, v_4);

        valued.add(gv_d4);

        gv_g1 = new BasicUserLink();

        gv_g1.setRelation("Given");

        gv_g1.set(v_g, -1.0, v_1);

        valued.add(gv_g1);



        dv_a1 = new BasicUserLink();

        dv_a1.setRelation("Derived");

        dv_a1.set(v_a, 1.0, v_1);

        valued.add(dv_a1);

        dv_a2 = new BasicUserLink();

        dv_a2.setRelation("Derived");

        dv_a2.set(v_a, 1.0, v_2);

        valued.add(dv_a2);

        dv_b2 = new BasicUserLink();

        dv_b2.setRelation("Derived");

        dv_b2.set(v_b, 2.0, v_2);

        valued.add(dv_b2);

        dv_b3 = new BasicUserLink();

        dv_b3.setRelation("Derived");

        dv_b3.set(v_b, 1.0, v_3);

        valued.add(dv_b3);

        dv_c2 = new BasicUserLink();

        dv_c2.setRelation("Derived");

        dv_c2.set(v_c, 0.5, v_2);

        valued.add(dv_c2);

        dv_c3 = new BasicUserLink();

        dv_c3.setRelation("Derived");

        dv_c3.set(v_c, 0.25, v_3);

        valued.add(dv_c3);

        dv_d4 = new BasicUserLink();

        dv_d4.setRelation("Derived");

        dv_d4.set(v_d, 3.0, v_4);

        valued.add(dv_d4);

        dv_f2 = new BasicUserLink();

        dv_f2.setRelation("Derived");

        dv_f2.set(v_f, 1.0, v_2);

        valued.add(dv_f2);

}

}

