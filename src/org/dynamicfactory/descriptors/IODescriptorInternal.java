/*

 * IODescriptorInternal.java

 *

 * Created on 14 September 2007, 13:56

 *

 * Copyright Daniel McEnnis, published under Aferro GPL (see license.txt)

 */



package org.dynamicfactory.descriptors;

import nz.ac.waikato.mcennis.rat.graph.query.Query;


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

