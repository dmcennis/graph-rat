/*

 * IODescriptorInternal.java

 *

 * Created on 14 September 2007, 13:56

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

 * Interface for modifying an input descriptor

 * @author Daniel McEnnis
 */

public interface IODescriptorInternal extends IODescriptor{



    /**

     * Change the property name that this algorithm requires as input

     * 

     * @param s name of the new property input that is required.

     */

    public void setProperty(String s);

    

    /**

     * Change the class of input that is utilized

     * 

     * @param t new input type.

     */

    public void setClassType(Type t);

    

    /**

     * Change the relation (type, mode) that this algorithm uses.

     * 

     * @param s name of the new relation.

     */

    public void setRelation(String s);

    public void setQuery(Query q);


    /**

     * Change the name of the algorithm.

     * 

     * @param s new name of this algorithm.

     */

    public void setAlgorithmName(String s);

    public void setDescription(String s);

    public void setAppendGraphID(boolean b);
    
    public IODescriptorInternal prototype();
}

