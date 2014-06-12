/*

 * NotConstructed.java

 *

 * Created on 2 July 2007, 15:10

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.graph.path;



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
