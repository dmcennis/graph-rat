/*

 * PathSetFactory.java

 *

 * Created on 2 July 2007, 20:04

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

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

