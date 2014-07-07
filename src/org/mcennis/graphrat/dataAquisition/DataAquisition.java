/*

 * DataAquisition.java

 *

 * Created on 3 October 2007, 13:49

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

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


package org.mcennis.graphrat.dataAquisition;



import org.mcennis.graphrat.Component;

import org.mcennis.graphrat.graph.Graph;



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

