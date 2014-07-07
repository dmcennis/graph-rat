/*
 * Copyright (c) Daniel McEnnis
 *
 *
 *    This file is part of GraphRAT.
 *
 *    GraphRAT is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    GraphRAT is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with GraphRAT.  If not, see <http://www.gnu.org/licenses/>.
 */



package org.mcennis.graphrat.actor;



import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.property.Property;

import java.util.List;


/**

 *


 * 

 * Actors are any entity (such as nouns).  Also known as nodes.  They have 

 * properties and participate in links (relations, arcs, edges).

 * @author Daniel McEnnis
 */

public interface Actor extends java.io.Serializable, org.mcennis.graphrat.parser.ParsedObject, Comparable{



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

