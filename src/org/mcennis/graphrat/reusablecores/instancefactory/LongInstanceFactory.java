/**
 * Jul 23, 2008-9:42:32 PM
 * Copyright Daniel McEnnis, see license.txt
 */

package org.mcennis.graphrat.reusablecores.instancefactory;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * 
 * Class for transforming Long property values into an Instance.  Cretaes a single
 * numeric attribute with the long value transformed into a double as its value.
 * 
 * @author Daniel McEnnis
 */
public class LongInstanceFactory implements InstanceFactory{

    @Override
    public Instance transform(Object value, String name) {
        Instance ret = new Instance(1);
        FastVector attributes = new FastVector();
        attributes.addElement(new Attribute(name));
        Instances meta = new Instances(name,attributes,1);
        ret.setDataset(meta);
        ret.setValue(0,((Long)value).longValue());
        return ret;
    }

    public LongInstanceFactory prototype(){
        return new LongInstanceFactory();
    }
}
