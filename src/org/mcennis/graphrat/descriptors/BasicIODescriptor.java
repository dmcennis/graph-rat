/*

 * BasicIODescriptor.java

 *

 * Created on 14 September 2007, 13:54

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

 *


 * 

 * Class for describing the input that a component requires. Implements the 

 * InputDesciptor and IODescriptorInternal interfaces.

 * @author Daniel McEnnis
 */

public class    BasicIODescriptor implements IODescriptorInternal{
    String propertyName;
    IODescriptor.Type type;
    String relation = null;
    Query query = null;
    String algorithmName;
    String description="";
    boolean appendGraphID;

    public boolean appendGraphID() {
        return appendGraphID;
    }

    public void setAppendGraphID(boolean appendGraphID) {
        this.appendGraphID = appendGraphID;
    }

    /** Creates a new instance of BasicIODescriptor */

    public BasicIODescriptor() {

    }

    public void setQuery(Query q) {
        query = q;
    }

    public Query getQuery() {
        return query;
    }



    @Override

    public void setProperty(String s) {

        propertyName = s;

    }



    @Override

    public void setClassType(IODescriptor.Type t) {

        type = t;

    }



    @Override

    public void setRelation(String s) {

        relation = s;

    }



    @Override

    public void setAlgorithmName(String s) {

        algorithmName = s;

    }



    @Override

    public String getAlgorithmName() {

        return algorithmName;

    }



    @Override

    public String getRelation() {

        return relation;

    }



    @Override

    public IODescriptor.Type getClassType() {

        return type;

    }



    @Override

    public String getProperty() {

        return propertyName;

    }

    @Override
    public BasicIODescriptor prototype(){
        return new BasicIODescriptor();
    }

    public void setDescription(String s) {
        description = s;
    }

    public String getDescription() {
        return description;
    }

}

