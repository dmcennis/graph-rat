/**
 * Jul 23, 2008-7:18:06 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.algorithm.reusablecores.instanceFactories;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * This class is used when an approprite Object specific factory does not exist.
 * It always returns an Instance wihout attributes.
 * 
 * @author Daniel McEnnis
 */
public class DefaultInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        FastVector attributes = new FastVector(0);
        Instances meta = new Instances(name,attributes,1);
        Instance data = new Instance(0);
        data.setDataset(meta);
        return data;
    }

}
