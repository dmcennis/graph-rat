/*

 * PathSetFactory.java

 *

 * Created on 2 July 2007, 20:04

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


 * 

 * Class for creating anew PathSet object

 * @author Daniel McEnnis
 */

public class PathSetFactory {

    

    static private PathSetFactory instance = null;

    

    /**

     * Returns a reference to a singleton PatSetFactory

     * 

     * @return new PathSetFactory reference

     */

    public static PathSetFactory newInstance(){

        if(instance == null){

            instance = new PathSetFactory();

        }

        return instance;

    }

    

    /** Creates a new instance of PathSetFactory */

    private PathSetFactory() {

    }

    

    /**

     * Create new PathSet object.  Currently accepts no parameters and returns a

     * new BasicPathSet object

     * 

     * @param props ignored

     * @return newly created PathSet object

     */

    public PathSet create(Properties props){

        return new BasicPathSet();

    }

}

