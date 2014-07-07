/*

 * GraphIndex.java

 *

 * Created on 9/11/2007, 11:38:12

 *

 * Copyright Daniel McEnnis - see license.txt

 */

/*
 *   This file is part of GraphRAT.
 *
 *   GraphRAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GraphRAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.mcennis.graphrat.graph;



import java.util.HashMap;

import java.util.HashSet;

import org.mcennis.graphrat.actor.Actor;



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

