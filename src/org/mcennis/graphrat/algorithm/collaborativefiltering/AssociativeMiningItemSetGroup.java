/*

 * Created 3-2-08

 * Copyright Daniel McEnnis, see license.txt

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


package org.mcennis.graphrat.algorithm.collaborativefiltering;



import java.util.HashSet;

import java.util.Iterator;

import java.util.LinkedList;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;



/**

 * Class for bundling Association Objects.  It stores references to the paraent 

 * graph object and handles all bookkeeping for expanding association objects

 * until no larger group of items has a significant association.  All previous

 * associations are kept during each expansion.  See Associative mining for a 

 * description of the algorithm this is a part of.

 *

 * @author Daniel McEnnis

 */

public class AssociativeMiningItemSetGroup {

    LinkedList<Actor> baseSetPositive = new LinkedList<Actor>();

    LinkedList<Actor> baseSetNegative = new LinkedList<Actor>();

    HashSet<AssociativeMiningItems> currentSetPositive = new HashSet<AssociativeMiningItems>();

    HashSet<AssociativeMiningItems> currentSetNegative = new HashSet<AssociativeMiningItems>();

    LinkedList<AssociativeMiningItems> storedSet = new LinkedList<AssociativeMiningItems>();

    Graph graph;

    LinkQuery relation;

    LinkEnd direction;

    /**
     * 
     * Creates a new association group in the given (sub)graph over the given 

     * link relation

     * @param g graph of objects

     * @param relation link relation associations are derived from

     */

    public AssociativeMiningItemSetGroup(Graph g, LinkQuery relation, LinkEnd direction){

        graph = g;

        this.relation=relation;

        this.direction = direction;
    }

    

    /**

     * Add to the given base set of associations this actor and associated item

     * describing its (positive) relationship to the source actor.

     * @param a actor that has the sigificant relation

     * @param ami object describing this significant relation.

     */

    public void addPositiveBase(Actor a, AssociativeMiningItems ami){

        baseSetPositive.add(a);

        currentSetPositive.add(ami);

    }

    

   /**

     * Add to the given base set of associations this actor and associated item

     * describing its (negative) relationship to the source actor.

     * @param a actor that has the sigificant relation

     * @param ami object describing this significant relation.

     */

     public void addNegativeBase(Actor a, AssociativeMiningItems ami){

        baseSetNegative.add(a);

        currentSetNegative.add(ami);

    }

    

     /**

      * Combine all significant relations found by exporting the objects that 

      * describe them in an array.

      * 

      * @return array of objects describing associations

      */

    public AssociativeMiningItems[] exportAssociations(){

        LinkedList<AssociativeMiningItems> ret = new LinkedList<AssociativeMiningItems>();

        ret.addAll(storedSet);

        ret.addAll(currentSetPositive);

        ret.addAll(currentSetNegative);

        return ret.toArray(new AssociativeMiningItems[]{});

    }

    

    /**

     * Expand the current set of associations with all other actors in either the

     * positive or negaqtive base set. To avoid duplication, only actors greater

     * than all actors in the association are tested.

     * 

     * @param targetActor actor to check for a larger-member association

     * @return number of new associations created.

     */

    public int expandSet(Actor targetActor){

        // Create the next set of candidates

        HashSet<AssociativeMiningItems> nextPositive = new HashSet<AssociativeMiningItems>();

        HashSet<AssociativeMiningItems> nextNegative = new HashSet<AssociativeMiningItems>();

        

        // expand the positive set

        Iterator<AssociativeMiningItems> positiveIT = currentSetPositive.iterator();

        while(positiveIT.hasNext()){

            AssociativeMiningItems currentItemSet = positiveIT.next();

            Iterator<Actor> actorIT = baseSetPositive.iterator();

            while(actorIT.hasNext()){

                Actor newActor = actorIT.next();

                if(currentItemSet.isGreater(newActor)){

                    AssociativeMiningItems nextItemSet = currentItemSet.expand(newActor);

                    if(nextItemSet.significanceTest(targetActor)>0){

                        nextItemSet.setPositive(true);

                        nextPositive.add(nextItemSet);

                    }

                }

            }

            currentItemSet.clearTransients();

        }

        

        // now expand the negative set

        Iterator<AssociativeMiningItems> negativeIT = currentSetNegative.iterator();

        while(negativeIT.hasNext()){

            AssociativeMiningItems currentItemSet = negativeIT.next();

            Iterator<Actor> actorIT = baseSetNegative.iterator();

            while(actorIT.hasNext()){

                Actor newActor = actorIT.next();

                if(currentItemSet.isGreater(newActor)){

                    AssociativeMiningItems nextItemSet = currentItemSet.expand(newActor);

                    if(nextItemSet.significanceTest(targetActor)<0){

                        nextItemSet.setPositive(false);

                        nextNegative.add(nextItemSet);

                    }

                }

            }

            currentItemSet.clearTransients();

        }

        storedSet.addAll(currentSetPositive);

        storedSet.addAll(currentSetNegative);

        currentSetPositive = nextPositive;

        currentSetNegative = nextNegative;

        return currentSetPositive.size()+currentSetNegative.size();

    }

}

