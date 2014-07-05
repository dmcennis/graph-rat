/*

 * DataAquisition.java

 *

 * Created on 3 October 2007, 13:49

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.dataAquisition;



import nz.ac.waikato.mcennis.rat.Component;

import nz.ac.waikato.mcennis.rat.graph.Graph;



/**

 * Class for acquiring data into RAT.  This module makes extensive use of the 

 * Crawler and Parser classes.

 *

 * @author Daniel McEnnis

 * 

 */

public interface DataAquisition extends Component {

    

    /**

     * Begin executing the data aquisition module

     */

    public void start();

    

    /**

     * Set the graph to be populated by this object

     * 

     * @param g graph to be created for analysis

     */

    public void set(Graph g);

    

    /**

     * Obtain a reference to the graph this object holds

     * 

     * @return graph created by this object

     */

    public Graph get();

    

    /**

     * Stop all collection at the end of the next entity (file, web-page, etc.)

     */

    public void cancel();

}

