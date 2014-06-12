/*

 * Algorithm.java

 *

 * Created on 1 May 2007, 14:40

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.graph.algorithm;



import nz.ac.waikato.mcennis.rat.graph.Graph;

import nz.ac.waikato.mcennis.rat.Component;



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

