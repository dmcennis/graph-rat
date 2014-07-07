/*

 * 

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

import cern.jet.stat.Probability;


import java.util.Collection;


import java.util.HashSet;

import java.util.Iterator;

import java.util.LinkedList;
import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.link.Link;
import org.mcennis.graphrat.query.LinkQuery;
import org.mcennis.graphrat.query.LinkQuery.LinkEnd;

/**
 * TODO: update XML import and export
 * Represents a single association between a set of actors and a destiation actor

 * This association has a significance level and may be positive or negative.

 * 

 * @author Daniel McEnnis

 */
public class AssociativeMiningItems implements Comparable {

    HashSet<Actor> actors;
    transient Actor maxMember;
    transient HashSet<Actor> activeUsers = new HashSet();
    transient double total;
    boolean positive;
    transient Graph graph;
    LinkQuery relation;
    LinkEnd direction;
    double positiveSignificance = 0.10;
    double negativeSignificance = 0.10;
    double significance = Double.NaN;

    /**

     * Get the statistical significance of the association

     * @return statistical significance of this association

     */
    public double getSignificance() {

        return significance;

    }

    /**

     * Set the statistical significance of the association

     * @param significance statistical significance of the association

     */
    public void setSignificance(double significance) {

        this.significance = significance;

    }

    /**

     * Is this association positively or negatively correlated with the destination actor

     * @return positive or negative correlation.

     */
    public boolean isPositive() {

        return positive;

    }

    /**

     * Set whether this association positively or negatively correlated with the destination actor

     * @param isPositive is the correlation positive

     */
    public void setPositive(boolean isPositive) {

        this.positive = isPositive;

    }

    /**

     * Create a new association on this graph, with this relation, using this actor.

     * The destination actor is not maintained in this object. 

     * @param g graph for data

     * @param relation type of link to get assocaitions from

     * @param value seed actor for this association

     */
    public AssociativeMiningItems(Graph g, LinkQuery relation, LinkEnd direction, Actor value) {

        this.graph = g;

        this.relation = relation;

        this.direction = direction;

        actors = new HashSet<Actor>();

        maxMember = value;

        actors.add(value);

        Iterator<Link> links = null;
        if (direction == LinkEnd.SOURCE) {
            links = relation.executeIterator(g, actors, null, null);
        } else {
            links = relation.executeIterator(g, null, actors, null);
        }
//        Link[] links = g.getLinkByDestination(relation, value);


        activeUsers = new HashSet<Actor>();

        while (links.hasNext()) {
            if (direction == LinkEnd.SOURCE) {
                activeUsers.add(links.next().getDestination());
            } else {
                activeUsers.add(links.next().getSource());
            }
        }
        activeUsers = null;
        Iterator<Link> totalLink = relation.executeIterator(g, null, null, null);

        if (totalLink.hasNext()) {
            String type = "";
            if (direction == LinkEnd.SOURCE) {
                type = totalLink.next().getDestination().getMode();
            } else {
                type = totalLink.next().getSource().getMode();
            }

            totalLink = null;

            total = g.getActorCount(type);

        } else {

            total = 0;

        }

        positive = true;

    }

    /**

     * Create a new association containing all actors in the collection over the given relation

     * declaring its significance level.

     * @param col collections of actors in this association

     * @param relation link relation from which this association is derived

     * @param significance level of significance of this association

     */
    public AssociativeMiningItems(Collection<Actor> col, LinkQuery relation, LinkEnd direction, double significance) {

        this.positive = true;

        this.relation = relation;

        this.direction = direction;

        this.significance = significance;

        actors = new HashSet<Actor>();

        actors.addAll(col);

    }

    private AssociativeMiningItems() {
    }

    /**

     * Create a new association that also includes the given new actor.  It is assumed

     * that the actor is greater (by actor ordering) than any actor already in 

     * this the association and that a significane test is performed elsewhere.

     * 

     * @param newActor actor that is to added to the new association

     * @return new association.

     */
    public AssociativeMiningItems expand(Actor newActor) {

        AssociativeMiningItems ret = new AssociativeMiningItems();

        ret.actors = new HashSet<Actor>();

        ret.actors.addAll(actors);

        ret.actors.add(newActor);

        ret.maxMember = newActor;

        ret.positive = positive;

        ret.total = total;

        ret.graph = graph;

        ret.relation = relation;

        ret.direction = direction;

        ret.activeUsers = new HashSet<Actor>();

        ret.activeUsers.addAll(activeUsers);

        ret.positiveSignificance = positiveSignificance;

        ret.negativeSignificance = negativeSignificance;

        LinkedList<Actor> newActorList = new LinkedList<Actor>();
        newActorList.add(newActor);
        Iterator<Link> links = null;
        if (direction.equals(LinkEnd.SOURCE)) {
            links = relation.executeIterator(graph, newActorList, null, null);
        } else {
            links = relation.executeIterator(graph, null, newActorList, null);
        }

        HashSet<Actor> tmp = new HashSet<Actor>();

        while (links.hasNext()) {
            Actor actor = null;
            if (direction == LinkEnd.SOURCE) {
                actor = links.next().getDestination();
            } else {
                actor = links.next().getSource();
            }
            if (ret.activeUsers.contains(actor)) {

                tmp.add(actor);

            }

        }

        ret.activeUsers = tmp;

        return ret;

    }

    /**

     * Get the actors in this association backed by this array.  This will always

     * be at least one actor, but it is not guarenteed to be either ordered or in

     * a consistant order.

     * @return array of actors in the associative array

     */
    public Actor[] getActors() {

        return actors.toArray(new Actor[]{});

    }

    /**

     * Is this actor greater than all actors in this association

     * @param a actor to be compared

     * @return is actor larger than all actors in the association

     */
    public boolean isGreater(Actor a) {

        if (a.compareTo(maxMember) > 0) {

            return true;

        } else {

            return false;

        }

    }

    /**

     * Given the set of all actors in this association and the target, is their 

     * a significant difference between the population of the destination actor

     * in the set of all references over the relation in the candidate association

     * compared to over the entire relation.

     * 

     * @param target actor to be tentatively added to the association

     * @return negative or positive if correlated, zero otherwise.

     */
    public int significanceTest(Actor target) {
        LinkedList<Actor> newActorList = new LinkedList<Actor>();
        newActorList.add(target);
        Iterator<Link> targetEntries = null;
        if (direction.equals(LinkEnd.SOURCE)) {
            targetEntries = relation.executeIterator(graph, newActorList, null, null);
        } else {
            targetEntries = relation.executeIterator(graph, null, newActorList, null);
        }

        if (targetEntries.hasNext()) {


//            HashSet<Actor> intersection = new HashSet<Actor>();

            double intersection = 0.0;
            double localTotal = 0.0;
            while (targetEntries.hasNext()) {
                localTotal += 1.0;
                Actor actor = null;
                if (direction == LinkEnd.SOURCE) {
                    actor = targetEntries.next().getDestination();
                } else {
                    actor = targetEntries.next().getSource();
                }
                if (activeUsers.contains(actor)) {

                    intersection += 1.0;

                }

            }

            if ((localTotal < 2.0) || (total < 2.0)) {

                return 0;

            }

            if (intersection == localTotal) {

                intersection -= 0.1;

            } else if (intersection == 0) {

                intersection += 0.1;

            }

            double variance = Math.sqrt((intersection - (intersection * intersection) / localTotal)) / (localTotal - 1);

            double observedMean = (intersection) / localTotal;

            double mean = ((double) activeUsers.size()) / ((double) total);



            // test for positive correlation

            double value = (observedMean - mean) / (variance / Math.sqrt(localTotal));

            if (Probability.studentT((int) total - 1, value) > 0.5) {

                significance = 1.0 - Probability.studentT((int) total - 1, value);

            } else {

                significance = Probability.studentT((int) total - 1, value);

            }

            if (Probability.studentTInverse(positiveSignificance, (int) total - 1) < value) {

//                significance = 1.0-Probability.studentT((int)localTotal-1, value);

                return 1;

            } else if ((-Probability.studentTInverse(negativeSignificance, (int) total - 1)) > value) {

//                significance = Probability.studentT((int)localTotal-1, value);

                return -1;

            } else {

                return 0;

            }

        } else {

            return 0;

        }

    }

    /**

     * Return the threshold that determines whether or not a candidate association

     * has a negative correlation with the destination.  Value is a double between

     * 0.0 and 1.0.  

     * 

     * @return threshold for declaring a significant negative correlation

     */
    public double getNegativeSignificance() {

        return negativeSignificance;

    }

    /**

     * Set the threshold that determines whether or not a candidate association

     * has a negative correlation with the destination.  Value is a double between

     * 0.0 and 1.0.  

     * 

     * @param negativeSignificance threshold for declaring a significant negative correlation

     */
    public void setNegativeSignificance(double negativeSignificance) {

        this.negativeSignificance = negativeSignificance;

    }

    /**

     * Return the threshold that determines whether or not a candidate association

     * has a positive correlation with the destination.  Value is a double between

     * 0.0 and 1.0.  

     * 

     * @return threshold for declaring a significant positive correlation

     */
    public double getPositiveSignificance() {

        return positiveSignificance;

    }

    /**

     * Set the threshold that determines whether or not a candidate association

     * has a positive correlation with the destination.  Value is a double between

     * 0.0 and 1.0.  

     * 

     * @param positiveSignificance threshold for declaring a significant positive correlation

     */
    public void setPositiveSignificance(double positiveSignificance) {

        this.positiveSignificance = positiveSignificance;

    }

    /**

     * Eliiminates all transient objects.  This reduces memory usage, but, after

     * calling this, the association is effectively immutable.

     */
    public void clearTransients() {

        activeUsers.clear();

    }

    /**

     * Are all the given actors present in this association?

     * @param data set of actors to check

     * @return are all actors present or not

     */
    public boolean applies(HashSet<Actor> data) {

        Iterator<Actor> it = actors.iterator();

        while (it.hasNext()) {

            if (!data.contains(it.next())) {

                return false;

            }

        }

        return true;

    }

    @Override
    public int compareTo(Object o) {

        AssociativeMiningItems right = (AssociativeMiningItems) o;

        Actor[] leftActors = this.getActors();

        Actor[] rightActors = right.getActors();

        java.util.Arrays.sort(leftActors);

        java.util.Arrays.sort(rightActors);

        int i = 0;

        while ((i < leftActors.length) && (i < rightActors.length)) {

            String leftType = leftActors[i].getMode();

            String rightType = rightActors[i].getMode();

            if (leftType.compareTo(rightType) != 0) {

                return leftType.compareTo(rightType);

            }

            String leftID = leftActors[i].getID();

            String rightID = rightActors[i].getID();

            if (leftID.compareTo(rightID) != 0) {

                return leftID.compareTo(rightID);

            }



            ++i;

        }

        if (leftActors.length > rightActors.length) {

            return 1;

        } else if (leftActors.length < rightActors.length) {

            return -1;

        }

        if (positive && !right.positive) {

            return 1;

        } else if (!positive && right.positive) {

            return -1;

        }

        return 0;

    }

    @Override
    public String toString() {

        StringBuffer ret = new StringBuffer();

        Iterator<Actor> it = actors.iterator();

        while (it.hasNext()) {

            Actor a = it.next();

            ret.append(a.getMode()).append(":");

            ret.append(a.getID());

            if (it.hasNext()) {

                ret.append(":");

            }

        }



        ret.append(",");

        ret.append(relation).append(",");

        ret.append(Double.toString(significance)).append(",");

        ret.append(Boolean.toString(positive)).append(",");

        ret.append(Double.toString(positiveSignificance)).append(",");

        ret.append(Double.toString(negativeSignificance));



        return ret.toString();



    }
}

