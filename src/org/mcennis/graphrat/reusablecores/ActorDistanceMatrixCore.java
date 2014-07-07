/*

 * Created 30/05/2008-13:58:39

 * Copyright Daniel McEnnis, see license.txt

 */



package org.mcennis.graphrat.reusablecores;



import cern.colt.matrix.DoubleFactory2D;

import cern.colt.matrix.DoubleMatrix2D;

import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.actor.Actor;

import org.mcennis.graphrat.path.PathNode;



/**

 * Creates a 2D matrix describing the distance between any two actors in a

 * single mode-relation pair using a shortest-path measure of distance.

 * 

 * @author Daniel McEnnis

 */

public class ActorDistanceMatrixCore extends PathBaseCore{

    

    DoubleMatrix2D actorMatrix = null;

    int count=0;

    

    @Override

    protected void doCleanup(PathNode[] path, Graph g) {

        ; // intentionally null

    }



    @Override

    protected void doAnalysis(PathNode[] path, PathNode source) {

        int column = actorToID.get(source);

        for(int i=0;i<path.length;++i){

            actorMatrix.set(path[i].getId(), column, path[i].getCost());

        }

    }



    @Override

    protected void setSize(int size) {

        count=0;

        actorMatrix = DoubleFactory2D.dense.make(size, size, 0.0);

    }



    /**

     * Get the matrix describing the distances of this actor-mode pair

     * @return

     */

    public DoubleMatrix2D getActorMatrix() {

        return actorMatrix;

    }



    /**

     * Set the matrix that will be overwritten with the distance data of this

     * mode-relation pair.

     * @param actorMatrix matrix to be overwritten

     */

    public void setActorMatrix(DoubleMatrix2D actorMatrix) {

        this.actorMatrix = actorMatrix;

    }

    

    /**

     * Get which column the given actor is stored in.

     * @param a actor to be found

     * @return integer of the column where the actor's distance vector is stored.

     */

    public int getColumn(Actor a){

        return actorToID.get(a);

    }

}

