/*

 * Algorithm.java

 *

 * Created on 1 May 2007, 14:40

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

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


package org.mcennis.graphrat.algorithm;



import org.mcennis.graphrat.graph.Graph;

import org.mcennis.graphrat.Component;



/**

 *


 * 

 * Interface specifying an algorithm that operates against a Graph.  Each is a 

 * Component, with parameters and input and output decsriptors.  Parameters will

 * be changed to be javaBeans in a future release

 * @author Daniel McEnnis
 */

public interface Algorithm extends java.io.Serializable, Component{



    /**

     * execute the algorithm against the given graph

     * @param g graph to be modified

     */

    public void execute(Graph g); 



    /**

     * big O notation execution time over all actors.

     */

    public Algorithm prototype();
//    public int bigO();

}

