/**
 * Jul 23, 2008-9:40:24 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.instanceFactories;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * Class for transforming Double property values into an Instance.  Cretaes a single
 * numeric attribute with the double value as its value.
 * 
 * @author Daniel McEnnis
 */
public class DoubleInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(1);
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute(name));
        Instances meta = new Instances(name,attributes,1);
        ret.setDataset(meta);
        ret.setValue(0,((Double)value).doubleValue());
        return ret;
    }

}
