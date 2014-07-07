/**
 * Created Jul 23, 2008-7:11:39 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.reusablecores.instancefactory;

import weka.core.Instance;

/**
 *
 * Interface for transforming Properties into Instance Objects.
 * 
 * @author Daniel McEnnis
 */
public interface InstanceFactory {
    /**
     * Transform an object into an Instance where the name of the dataset and the
     * names of the attributes are derived from the name provided.
     * 
     * @param value Property value to be transformed
     * @param name string utilized to derive a dataset name and attribute names.
     * @return Instance representing this object
     */
    public Instance transform(Object value, String name);
    
    public InstanceFactory prototype();
}
