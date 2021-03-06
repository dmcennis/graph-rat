/*
 * Link.java
 *
 * Created on 1 May 2007, 14:21
 *
 * copyright Daniel McEnnis - published under Aferro GPL (see license.txt)
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
package org.mcennis.graphrat.link;

import java.util.List;
import org.mcennis.graphrat.actor.Actor;

import org.dynamicfactory.model.Model;
import org.dynamicfactory.descriptors.Properties;
import org.dynamicfactory.property.Property;

/**
 * Interface for describing a link (arc, edge) in a graph.
 *
 * @author Daniel McEnnis
 * 
 */
public interface Link extends java.io.Serializable, Comparable, Model {
    public static int ALL=0;
    public static int SOURCE=1;
    public static int DESTINATION=2;
    public static int RELATION=3;
    public static int STRENGTH=4;

    /**
     * return the strength of this link.  This can be positive or negative or non-numeric
     * values such as NaN, -Infinity, or Inifinity for links where only the 
     * existance of the link is important.
     * @return strength of the link
     */
    public double getStrength();

    /**
     * Returns the actor from which the link (edge, arc) begins.
     * @return source actor
     */
    public Actor getSource();

    /**
     * Returns the actor where the link (edge, arc) terminates.
     * 
     * @return destination actor
     */
    public Actor getDestination();

    /**
     * Returns the type (relation) of the link.
     * 
     * @return typer (relation) of this link.
     */
    public String getRelation();

    /**
     * sets all aspects of the link at once
     * 
     * @param l source actor
     * @param strength strength of this length
     * @param r destination actor
     */
    public void set(Actor l, double strength, Actor r);

    /**
     * Sets the source of this link.  This may not be null.
     * @param u actor source
     */
    public void setSource(Actor u);

    /**
     * Sets the destination of this link. This may not be null.
     * 
     * @param u destination actor.
     */
    public void setDestination(Actor u);

    /**
     * Sets the type (relation) of the link.
     * 
     * @param type string describing the type
     */
    public void setRelation(String type);

    /**
     * Sets the strength that this link has. This may be a non-numeric value.
     * 
     * @param str sterngth of this link.
     */
    public void set(double str);

    /**
     * Add the given property to this link
     * 
     * @param prop
     */
    public void add(Property prop);

    /**
     * Get the property with the given name.  Returns null if no property with
     * that id exists.
     * 
     * @param id id of the property to be returned
     * @return property with the given id
     */
    public Property getProperty(String id);

    /**
     * Return all properties associated with this link.  Returns null if no 
     * properties are associated with this link.
     * 
     * @return array of properties associated with this object.
     */
    public List<Property> getProperty();

    public void removeProperty(String id);
    
    public Link prototype();
    
    public void init(Properties properties);
}



