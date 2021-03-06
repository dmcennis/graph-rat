/*
 * DataDescriptor.java
 *
 * Created on 14 September 2007, 12:41
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

package org.mcennis.graphrat.descriptors;

import org.dynamicfactory.propertyQuery.Query;

/**
 * Class describing one input parameter of an object.  This object is guaranteed
 * to be a valid description of an algorith iff the creating algorithm has no 
 * status property changes.
 *
 * @author Daniel McEnnis
 */
public interface IODescriptor {

    

    /**
     * Set of all enumerations representing each type of input that an algorithm
     * can consume.
     * <ul>
     * <li>ACTOR - Algorithm behavior changes becuase of the presence or absence of actors.
     * <li>ACTOR_PROPERTY - Reads properties of an actor property.
     * <li>GRAPH - Algorithm behavior changes because of the presense or absense of graphs.
     * <li>GRAPH_PROPERTY - Reads properties of a graph.
     * <li>LINK - Reads the existance or strength of links.
     * <li>PATHSET - Utilizes a path set in the algorithm.
     * </ul>
     */
    public enum Type {ACTOR,ACTOR_PROPERTY,GRAPH,GRAPH_PROPERTY,LINK,LINK_PROPERTY,PATHSET};
    
    /**
     * Acquire the name of the algorithm that generated this data descriptor.  
     * Names are unique within a single application, but multiple instances of 
     * the same class with differing names are permitted. No set operation defined 
     * as it is an immutable object created by the parent algorithm.
     *
     * @return Algorithm name
     */
    public String getAlgorithmName();
    
    /**
     * Return the actor types and link types that this algorithm utilizes during
     * input.  This is not necessarily immutable and is no longer valid after any
     * parameter changes.
     *
     * @return list of input actors that matc
     */
    public String getRelation();
    
    public Query getQuery();

    /**
     * what type of object - Graph, Link, Actor, or PathSet - does this object describe. 
     *
     * @return type of graph object
     */
    public Type getClassType();

    

    /**
     * Name of the property of the object that is utilized.  This is null if only
     * the existance of the object, not its properties, is utilized.  Link 
     * objects always return null since they can not hold properties.
     *
     * @return name of the property that this algorithm works on.
     */
    public String getProperty();

    public String getDescription();
    
    public IODescriptor prototype();

    public boolean appendGraphID();
}



