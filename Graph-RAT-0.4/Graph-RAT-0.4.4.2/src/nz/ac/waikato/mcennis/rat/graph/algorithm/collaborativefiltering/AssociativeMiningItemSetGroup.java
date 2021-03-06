/*
 * Created 3-2-08
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.collaborativefiltering;

import java.util.TreeSet;
import java.util.Iterator;
import java.util.LinkedList;
import org.mcennis.graphrat.graph.Graph;
import org.mcennis.graphrat.actor.Actor;

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
    TreeSet<AssociativeMiningItems> currentSetPositive = new TreeSet<AssociativeMiningItems>();
    TreeSet<AssociativeMiningItems> currentSetNegative = new TreeSet<AssociativeMiningItems>();
    LinkedList<AssociativeMiningItems> storedSet = new LinkedList<AssociativeMiningItems>();
    Graph graph;
    String relation;
    
    /**
     * Creates a new association group in the given (sub)graph over the given 
     * link relation
     * @param g graph of objects
     * @param relation link relation associations are derived from
     */
    public AssociativeMiningItemSetGroup(Graph g, String relation){
        graph = g;
        this.relation=relation;
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
        TreeSet<AssociativeMiningItems> nextPositive = new TreeSet<AssociativeMiningItems>();
        TreeSet<AssociativeMiningItems> nextNegative = new TreeSet<AssociativeMiningItems>();
        
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
