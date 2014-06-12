/*

 * GraphIndex.java

 *

 * Created on 9/11/2007, 11:38:12

 *

 * Copyright Daniel McEnnis - see license.txt

 */



package nz.ac.waikato.mcennis.rat.graph;



import java.util.HashMap;

import java.util.HashSet;

import nz.ac.waikato.mcennis.rat.graph.actor.Actor;



/**

 *


 * 

 * Helper Class that maintains an inverse index - the set of all graphs that an

 * actor belongs to.  This is used to facilitate algorithms that change behavior

 * depending on membership in groups.

 * 

 * @author Daniel McEnnis
 */

public class GraphIndex {

    

    HashMap<Actor,HashSet<Graph>> actorMap = new HashMap<Actor,HashSet<Graph>>();

    HashMap<String,Graph> graphMap = new HashMap<String,Graph>();

    

    public GraphIndex() {

    }

    

    /**

     * Add a reference between a graph and an actor.  Implicitly adds the graph

     * as well if it is not already in the index.

     * @param a actor reference

     * @param g graph reference

     */

    public void addActor(Actor a, Graph g){

        if((a != null)&&(g != null)){

            if(!actorMap.containsKey(a)){

                actorMap.put(a,new HashSet<Graph>());

            }

            if(!graphMap.containsKey(g.getID())){

                graphMap.put(g.getID(), g);

            }

            actorMap.get(a).add(g);

        }

    }

    

    /**

     * Adds a graph reference to the object

     * @param g graph to be added

     */

    public void addGraph(Graph g){

        if(g != null){

            graphMap.put(g.getID(), g);

        }

    }

    

    /**

     * Return an array of all graphs that contain the actor or null if no indexed

     * graph contains the actor.

     * 

     * @param a actor to be checked

     * @return array of graphs containing the actor

     */

    public Graph[] getGraph(Actor a){

        if(actorMap.containsKey(a)&&(actorMap.get(a).size()>0)){

            return actorMap.get(a).toArray(new Graph[]{});

        }else{

            return null;

        }

    }

    

    /**

     * Returns the graph with the given ID or null if no graph with this ID is 

     * indexed.

     * 

     * @param s ID to retrieve graphs from

     * @return Graph with the unique ID s

     */

    public Graph getGraph(String s){

        return graphMap.get(s);

    }

    

}

