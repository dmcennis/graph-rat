/*

 * User.java

 *

 * Created on 1 May 2007, 14:16

 *

 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)

 */



package nz.ac.waikato.mcennis.rat.graph.actor;



import java.util.List;

import nz.ac.waikato.mcennis.rat.graph.property.Property;
import nz.ac.waikato.mcennis.rat.graph.descriptors.Properties;


/**

 *


 * 

 * Actors are any entity (such as nouns).  Also known as nodes.  They have 

 * properties and participate in links (relations, arcs, edges).

 * @author Daniel McEnnis
 */

public interface Actor extends java.io.Serializable, nz.ac.waikato.mcennis.rat.parser.ParsedObject, Comparable{



    /**

     * Return the id (unique within its type)

     * 

     * @return ID of this actor

     */

    public String getID();



    /**

     * Sets an id unique (which must be its type).

     * 

     * @param id ID this actor should be set to

     */

    public void setID(String id);



    /**

     * Return an array of all properties associated with this actor. Returns null

     * if no properties are attached.

     * 

     * @return array of properties

     */

    public List<Property> getProperty();



    /**

     * Return the property with the given value.  Returns null if no properties 

     * are attached.

     * @param ID key-name for the property.

     * @return Property object having this key or null

     */

    public Property getProperty(String ID);



    /**

     * Remove a given property from this actor

     * @param ID key-name of property to remove.

     */

    public void removeProperty(String ID);



    /**

     * Add the given property to this user.  If a property with this name 

     * already exists, it is replaced by this property.

     * @param prop property to be added

     */

    public void add(Property prop);



 


    /**

     * Set the type (mode) of this actor.  User ID's must be unique within a type.

     * @param type new type (mode) of this actor

     */

    public void setMode(String type);



    /**

     * Returns the type (mode) of this actor

     * @return type (mode) of this actor

     */

    public String getMode();



    /**

     * Return a copy of this Actor that is equal by compareTo()

     * @return new duplicate Actor

     */

    public Actor prototype();
    

    @Override

    public boolean equals(Object o);
    
    public void init(Properties properties);

}

