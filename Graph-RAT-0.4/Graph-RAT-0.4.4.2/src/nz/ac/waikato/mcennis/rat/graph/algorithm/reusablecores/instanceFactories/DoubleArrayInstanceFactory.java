/**
 * Jul 23, 2008-9:42:03 PM
 * Copyright Daniel McEnnis, see license.txt
 */
package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.instanceFactories;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * Class for transforming double[] property values into an Instance.  If the array
 * is null or of zero length, an Instance is returned with no attributes.  If the 
 * array has length greater than zero, each element of the array becomes an attribute
 * named as 'name':'index' with the appropriate array value as its value.
 * 
 * @author Daniel McEnnis
 */
public class DoubleArrayInstanceFactory implements InstanceFactory {

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(0);
        ret.setDataset(new Instances(name, new FastVector(0), 1));
        double[] data = (double[]) value;
        if ((data != null) && (data.length > 0)) {
            FastVector attributes = new FastVector(data.length);
            ret = new Instance(data.length);
            for (int i = 0; i < attributes.size(); ++i) {
                attributes.addElement(new Attribute(name + ":" + i));
                ret.setValue(i, data[i]);
            }
            ret.setDataset(new Instances(name, attributes, 1));
        }
        return ret;
    }
}
