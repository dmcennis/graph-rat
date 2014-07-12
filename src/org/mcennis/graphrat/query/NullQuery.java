/**
 * NullActorQuery
 * Created Jan 11, 2009 - 3:04:51 PM
 * Copyright Daniel McEnnis, see license.txt
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
package org.mcennis.graphrat.query;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import org.dynamicfactory.propertyQuery.Query.State;

/**
 *
 * @author Daniel McEnnis
 */
public class NullQuery<Type extends Comparable> {

    State state = State.UNINITIALIZED;
    
    String type = "Graph";


    protected SortedSet<Type> execute() {
            return new TreeSet<Type>();
    }

    protected Iterator<Type> executeIterator(){
        return new TreeSet<Type>().iterator();
    }

    public void exportQuery(Writer writer) throws IOException{
        writer.append("<NullQuery Class=\""+type+"\"/>\n");
    }

    public int compareTo(Object o) {
        return this.getClass().getName().compareTo(o.getClass().getName());
    }
    
    protected void buildQuery(String type){
        this.type = type;
        state = State.READY;
    }

   public State buildingStatus() {
        return state;
    }
    
}   
