/**
 * Jul 23, 2008-9:57:35 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package nz.ac.waikato.mcennis.rat.graph.algorithm.reusablecores.instanceFactories;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * Class for transforming Integer property values into an Instance.  Cretaes a single
 * numeric attribute with the integer value transformed into a double as its value.
 * 
 * @author Daniel McEnnis
 */
public class IntegerInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(1);
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute(name));
        Instances meta = new Instances(name,attributes,1);
        ret.setDataset(meta);
        ret.setValue(0,((Integer)value).intValue());
        return ret;
    }

}
