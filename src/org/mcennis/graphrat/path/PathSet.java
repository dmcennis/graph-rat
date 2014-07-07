/*

 * PathSet.java

 *

 * Created on 2 July 2007, 14:41

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package org.mcennis.graphrat.path;



import org.dynamicfactory.model.Model;



/**

 *


 * 

 * Class for grouping sets of path into a single unit with a single id.

 * @author Daniel McEnnis
 */

public interface PathSet extends Model{

    

    public static final int ADD_PATH = 0;

    public static final int SET_TYPE = 1;

    

    /**

     * Return all paths in this object. Returns null if this path set contains no paths.

     * 

     * @return array of all paths in this object

     */

    public Path[] getPath();

    

    /**

     * Return all paths that start with an actor with this ID or null if none exist.

     * 

     * FIXME: should be by actor - not ID - as actors can have the same id if they

     * differ in type.

     * 

     * @param source string ID of actor used to select paths

     * @return all paths starting with actor given by ID source

     */

    public Path[] getPathBySource(String source);

    

    /**

     * Return all paths that end with an actor with this ID or null if none exist

     *

     * FIXME: should be by actor - not ID - as actors can have the same id if they

     * differ in type.

     * 

     * @param destination string ID of actor used to select paths

     * @return paths that end with an actor with the given ID.

     */

    public Path[] getPathByDestination(String destination);

    

    /**

     * Return all paths that start with actor source and end with actor destination.

     * 

     * FIXME: should be by actor - not ID - as actors can have the same id if they

     * differ in type.

     * 

     * @param source string ID of actor used to select paths

     * @param destination string ID of actor used to select paths

     * @return paths that start with actor source and end with actor destination

     */

    public Path[] getPath(String source, String destination);

    

    /**

     * Add a new path to this PathSet

     * 

     * @param p path to be added

     * @throws org.mcennis.graphrat.path.NotConstructedError

     */

    public void addPath(Path p) throws NotConstructedError;

    

    /**

     * Return the smallest cost path between the source and destination.

     * 

     * @param source string ID that of actor used to select paths

     * @param destination string ID that of actor used to select paths

     * @return smallest cost of any path in the PathSet between the source and destination

     */

    public double getGeodesicLength(String source,String destination);

    

    /**

     * Set the PathSet's ID

     * 

     * @param name new ID of this PathSet

     */

    public void setType(String name);

    

    /**

     * Returns the ID of this PathSet

     * 

     * @return PathSet ID

     */

    public String getType();

    

    /**

     * Return total number of paths in this dataset

     * 

     * @return size of the PathSet

     */

    public int size();

}

