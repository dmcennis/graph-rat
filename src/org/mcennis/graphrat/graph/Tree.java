/*

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


package org.mcennis.graphrat.graph;



import java.util.List;

import java.util.LinkedList;

import org.mcennis.graphrat.actor.Actor;



/**

 * Class for describing a tree.  Does not enforce acyclic condition.  Subclass

 * of MemGraph with MemGraph indexing.<br/>

 * <br/>

 * FIXME: Serialization not implemented - needs its own parser and output function.

 * @author Daniel McEnnis

 */

public class Tree extends MemGraph{

    

    LinkedList<Actor> roots = new LinkedList<Actor>();

    

    /**

     * Lists all the roots of this acyclic graph.

     * @return roots of the forest

     */

    public List<Actor> getRoots(){

        return roots;

    }

    

    /**

     * Add a root to the list of roots of this tree (forest)

     * @param a root actor to be added

     */

    public void addRoot(Actor a){

        roots.add(a);

    }

    

    @Override

    public void remove(Actor a){

        if(roots.contains(a)){

            roots.remove(a);

        }

        super.remove(a);

    }

}

