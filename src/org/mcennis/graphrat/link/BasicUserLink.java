/*
 * BasicLink.java
 *
 * Created on 1 May 2007, 16:07
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
import java.util.Iterator;

import java.util.LinkedList;

import org.mcennis.graphrat.actor.Actor;
import org.dynamicfactory.descriptors.*;
import org.dynamicfactory.property.Property;

import org.dynamicfactory.model.ModelShell;

/**
 * Class providing a base implementation of the Link Interface
 *
 * @author Daniel McEnnis
 * 
 */
public class BasicUserLink extends ModelShell implements Link {

    public static final long serialVersionUID = 1;
    private double strength;
    private Actor source;
    private Actor destination;
    private String type = "Basic";
    private LinkedList<Property> properties = new LinkedList<Property>();

    /**
     * Creates a generic link with no source, destination, and a strength of -1.0.
     * Note that this state is invalid and must be altered before it can be used.
     */
    public BasicUserLink() {

        source = null;

        strength = -1.0;

        destination = null;

    }

    /** Creates a new instance of BasicLink in a self-consistent state
     * @param source source actor - may not be null
     * @param strength link strength
     * @param destination destination actor - may not be null
     */
    public BasicUserLink(Actor source, double strength, Actor destination) {

        this.source = source;

        this.strength = strength;

        this.destination = destination;

    }

    @Override
    public double getStrength() {

        return strength;

    }

    @Override
    public Actor getSource() {

        return source;

    }

    @Override
    public Actor getDestination() {

        return destination;

    }

    @Override
    public void set(Actor l, double strength, Actor r) {

        source = l;

        destination = r;

        this.strength = strength;

        this.fireChange(ALL, 0);

    }

    @Override
    public String getRelation() {

        return type;

    }

    @Override
    public void setRelation(String t) {

        type = t;

        this.fireChange(RELATION,0);

    }

    @Override
    public void setSource(Actor u) {

        source = u;

        this.fireChange(SOURCE,0);

    }

    @Override
    public void setDestination(Actor u) {

        destination = u;

        this.fireChange(DESTINATION,0);

    }

    @Override
    public void set(double str) {

        strength = str;

        this.fireChange(STRENGTH,0);

    }

    @Override
    public int compareTo(Object o) {

        Link link = (Link) o;

        if (this.type.compareTo(link.getRelation()) == 0) {

            if (this.strength == link.getStrength()) {

                if (this.source.getID().compareTo(link.getSource().getID()) == 0) {

                    return this.destination.getID().compareTo(link.getDestination().getID());

                } else {

                    return this.source.getID().compareTo(link.getSource().getID());

                }

            } else {

                return Double.compare(strength, link.getStrength());

            }

        } else {

            return this.type.compareTo(link.getRelation());

        }

    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Link) {

            return (this.compareTo(obj) == 0);

        } else {

            return false;

        }

    }

    @Override
    public void add(Property prop) {

        properties.add(prop);

    }

    @Override
    public Property getProperty(String id) {

        Iterator<Property> it = properties.iterator();

        while (it.hasNext()) {

            Property ret = it.next();

            if (ret.getType().equals(id)) {

                return ret;

            }

        }

        return null;

    }

    @Override
    public List<Property> getProperty() {

        return properties;

    }
    
    public BasicUserLink prototype(){
        BasicUserLink ret = new BasicUserLink();
        return ret;
    }

    public void init(Properties properties) {
        ; // deliberate no-op
    }

    public void removeProperty(String id){
        Iterator<Property> it = properties.iterator();
        while(it.hasNext()){
            Property prop = it.next();
            if(prop.getType().equals(id)){
                it.remove();
            }
        }
    }
}

