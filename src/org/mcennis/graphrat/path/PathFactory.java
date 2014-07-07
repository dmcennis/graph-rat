/*

 * PathFactory.java

 *

 * Created on 2 July 2007, 20:05

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


package org.mcennis.graphrat.path;



import java.util.Properties;



/**

 *


 * Class for Creating Path Objects

 * 

 * @author Daniel McEnnis
 */

public class PathFactory {

    private static PathFactory instance = null;

    

    /**

     * Creates a reference to a singelton PathFactory object

     * 

     * @return 

     */

    public static PathFactory newInstance(){

        if(instance == null){

            instance = new PathFactory();

        }

        return instance;

    }

    

    /** Creates a new instance of PathFactory */

    private PathFactory() {

    }

    

    /**

     * Create a new Path.  Currently ignores parameters and only returns

     * an uninitialized BasicPath object.

     * @param props ignored

     * @return new Path object

     */

    public Path create(Properties props){

        return new BasicPath();

    }

}

