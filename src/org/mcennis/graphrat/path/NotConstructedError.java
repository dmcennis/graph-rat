/*

 * NotConstructed.java

 *

 * Created on 2 July 2007, 15:10

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



/**

 *


 * 

 * Error describing then condition of a Path object accessed before it has been initialized

 * with a path.

 * @author Daniel McEnnis
 */

public class NotConstructedError extends Error{

    

    public static final long serialVersionUID = 1;

    

   

    String message;

    

    /** Creates a new instance of NotConstructed 

     * @param className name of the class that was modified

     */

    public NotConstructedError(String className) {

        message = "Class "+className+" used before it was built/defined.";

    }



    public String getMessage() {

        return message;

    }

    

}

