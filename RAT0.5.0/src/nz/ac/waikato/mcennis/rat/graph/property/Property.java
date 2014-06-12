/*

 * Properties.java

 *

 * Created on 1 May 2007, 14:47

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.graph.property;



import nz.ac.waikato.mcennis.rat.graph.model.Model;

import java.util.Collection;



/**

 *


 * Class for defining a property on either actor, link, or graph

 * @author Daniel McEnnis

 * 
 */

public interface Property extends java.io.Serializable, Comparable, Model {

    

    public static final int ADD_VALUE = 0;

    /**

     * Add a new value to this property

     * 

     * @param value new object to be added

     */

    public void add(Object value) throws InvalidObjectTypeException;

    

    /**

     * return this property's unique id

     * 

     * @return id of this object

     */

    public String getType();

    

    /**

     * return array of all values. Returns an empty array if no properties are present

     * 

     * @return array of property values

     */

    public java.util.List getValue();
    
    
    public void setType(String id);

    public void setClass(Class classType);
    

    /**

     * create a deep copy of this property

     * 

     * @return deep copy of this property

     */

    public Property duplicate();

    

    /**

     * Return the type of class that this property represents.  All objects in

     * one property must be of the same type.

     * @return name of class types of objects

     */

    public Class getPropertyClass();

    

    

}

