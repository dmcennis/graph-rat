/**
 * Jul 31, 2008-12:34:51 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.instanceFactories;

import weka.core.Instance;

/**
 *
 * Returns the underlying instance without any modification.
 * 
 * @author Daniel McEnnis
 */
public class InstanceInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        return (Instance)value;
    }

}
