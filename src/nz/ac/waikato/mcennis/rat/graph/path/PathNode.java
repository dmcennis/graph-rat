/*

 * PathNode.java

 *

 * Created on 22 October 2007, 14:12

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.graph.path;



import nz.ac.waikato.mcennis.rat.graph.actor.Actor;

import nz.ac.waikato.mcennis.rat.graph.link.Link;



/**

 *


 * Class for implementing Djikstra's Spanning Tree algorithm.

 * @author Daniel McEnnis

 * 
 */

public class PathNode {

    

    int id=-1;

    

    Actor a;

    

    transient double cost = Double.POSITIVE_INFINITY;

    

    transient PathNode previous = null;

        

    transient Link previousLink = null;

    

    /** Creates a new instance of PathNode */

    public PathNode() {

    }

    

    /**

     * Compares two PathNodes.  

     * <ol>

     * <li>If ids are different, return the difference

     * <li>Return the result of comparing the underlying actors.

     * <li>Return differences in the underlying cost

     * </ol>

     * @param o object to be compared against

     * @return ordering of the PathNode

     * @throws ClassCastException

     */

    public int compareTo(Object o) throws ClassCastException{

        PathNode right = (PathNode)o;

        if(id != right.getId()){

            return id - right.getId();

        }else{

            if(((a == null)&&(right == null))||((a!=null)&&(a.compareTo(right.getActor())==0))){

                return 0;

            }else if(a ==null){

                return -1;

            }else if(right==null){

                return 1;

            }else{

                if(a.compareTo(right) != 0){

                    return a.compareTo(right);

                }else{

                    if(cost < right.cost){

                        return -1;

                    }else if(cost > right.cost){

                        return 1;

                    }else{

                        return 0;

                    }

                }

            }

        }

    }

    

    /**

     * Returns the id associated with this group of PathNodes

     * @return id of the node

     */

    public int getId(){

        return id;

    }

    

    /**

     * Set the id for this node

     * @param i new node ID

     */

    public void setId(int i){

        id = i;

    }

    

    /**

     * Set the actor that this PathNode represents. 

     * @param a actor associated with this PathNode

     */

    public void setActor(Actor a){

        this.a = a;

    }

    

    /**

     * Returns the Actor associated with this PathNode. Returns null if none has been set.

     * 

     * @return associated actor

     */

    public Actor getActor(){

        return a;

    }

    

    /**

     * Set the previous PathNode in the spanning tree implicitly defined by this set of PathNodes.

     * Returns null if no such PathNode is defined

     * @param p previous PathNode in this implicit Path

     */

    public void setPrevious(PathNode p){

        previous = p;

    }

    

    /**

     * Return the previous PathNode in this implicit Path.

     * @return previous PathNode in the spanning tree

     */

    public PathNode getPrevious(){

        return previous;

    }

    

    /**

     * Return the total cost to the root of the spanning tree

     * 

     * 

     * @return total cost to the root of the spanning tree

     */

    public double getCost(){

        return cost;

    }

    

    /**

     * Set the cost to the spanning tree root

     * 

     * @param c new cost to the spanning tree root

     */

    public void setCost(double c){

        cost = c;

    }

    

    public void setPreviousLink(Link l){

        previousLink = l;

    }

    

    public Link getPreviousLink(){

        return previousLink;

    }

}

